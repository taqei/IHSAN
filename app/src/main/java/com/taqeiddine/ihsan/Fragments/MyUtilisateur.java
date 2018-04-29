package com.taqeiddine.ihsan.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.CreerAssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurMyActivity;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.ProfileRecyAdapter;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Association.AssobyUsrt;
import com.taqeiddine.ihsan.VOLLEY.Profile.getProfileInfo;
import com.taqeiddine.ihsan.VOLLEY.Profile.uploadPhotoProfilRequest;
import com.taqeiddine.ihsan.VOLLEY.Publications.getSmallPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class MyUtilisateur extends Fragment {
    private TextView nameandlastname;
    private Button buttonOne,buttonTwo;
    private CircleImageView photodeprofile;
    private ImageView changePhoto;

    private LinearLayout associationLayout;
    private TextView associationText;


    private RequestQueue requestQueue;
    final ArrayList<Publication> publications=new ArrayList<>();
    final ArrayList<String> currentpubs=new ArrayList<>();
    private boolean allPubs=false;
    final PublicationSmallAdapter publicationSmallAdapter=new PublicationSmallAdapter(publications,null,null);

    ProgressBar progressBar;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    protected Utilisateur me;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_utilisateur, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameandlastname =(TextView) getView().findViewById(R.id.myprofileName);

        //buttonOne=(Button) findViewById(R.id.myprofileBtnOne);
        photodeprofile=(CircleImageView) getView().findViewById(R.id.my_profile_image);
        changePhoto=(ImageView) getView().findViewById(R.id.my_profile_changeImage);
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });
        me=new Utilisateur();
        me.setIdprofile(getActivity().getIntent().getStringExtra("myidutilisateur"));

        requestQueue = Volley.newRequestQueue(getActivity());
        publicationSmallAdapter.setRequestQueue(requestQueue);

        getProfileInfo getProfileInfo =new getProfileInfo(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("taqi",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")){
                        final Utilisateur utilisateur=(Utilisateur) Profile.fromJsonINFOPROFILE(jsonObject);
                        nameandlastname.setText(utilisateur.getNom()+"  "+utilisateur.getPrenom());
                        initRatingBar(utilisateur.getConfiance());

                        initNBRS(utilisateur.getNbfollowers(),utilisateur.getNbfollowee(),utilisateur.getNbpublications());

                        if (utilisateur.getPhotodeprofil()!=null){
                            Glide.with(getActivity()).load(Help.getMedia()+utilisateur.getPhotodeprofil().getUrl()).into(photodeprofile);
                        }
                        if (utilisateur instanceof ChefAssociation){
                            final ChefAssociation chefAssociation=(ChefAssociation) utilisateur;
                            associationLayout=(LinearLayout) getView().findViewById(R.id.profile_my_layout_myasso);
                            associationText=(TextView) getView().findViewById(R.id.profile_my_myasso);
                            associationLayout.setVisibility(View.VISIBLE);
                            me=chefAssociation;
                            associationText.setText(chefAssociation.getAssociation().getNomassociation());
                            associationLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getActivity(), AssociationAdminActivity.class);
                                    intent.putExtra("myidassociation",chefAssociation.getAssociation().getIdprofile());
                                    intent.putExtra("myidchef",chefAssociation.getIdprofile());
                                    startActivity(intent);
                                }
                            });
                        }else{
                            me=utilisateur;
                            associationLayout=(LinearLayout) getView().findViewById(R.id.profile_my_layout_newasso);
                            associationLayout.setVisibility(View.VISIBLE);
                            associationLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getActivity(), CreerAssociationActivity.class);
                                    intent.putExtra("myidutilisateur",utilisateur.getIdprofile());

                                    startActivity(intent);
                                }
                            });
                        }
                        initListeDesAderents();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });

        publicationSmallAdapter.setMe(me);
        requestQueue.add(getProfileInfo);
        recyclerView=(RecyclerView) getView().findViewById(R.id.recy_pub_my_profile);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(publicationSmallAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView=(NestedScrollView) getView().findViewById(R.id.scroll_my_profile);
        progressBar=(ProgressBar) getView().findViewById(R.id.profile_my_progress);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // We take the last son in the scrollview
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int distanceToEnd = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                if (distanceToEnd == 0 && !allPubs) {
                    progressBar.setVisibility(View.VISIBLE);
                    GETTHEPUBLICATIONS();
                }
            }
        });

        GETTHEPUBLICATIONS();


    }

    private void GETTHEPUBLICATIONS(){
        getSmallPublication getSmallPublication=new getSmallPublication(currentpubs,me,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try{

                    JSONObject jsonObject=new JSONObject(s);

                    int nbr=jsonObject.getInt("nbrPub");
                    Log.i("imaad",""+nbr);

                    ArrayList<Publication> arrayList=new ArrayList<>();
                    for (int i=0;i<nbr;i++){
                        JSONObject jsonObject1=jsonObject.getJSONObject(""+i);
                        SignalPN publication=(SignalPN) Publication.fromJsonSmall(jsonObject1);
                        arrayList.add(publication);
                        currentpubs.add(publication.getIdpub());
                    }
                    if (arrayList.size()>0)
                        allPubs=true;
                    else
                        allPubs=false;
                    publications.addAll(arrayList);
                    progressBar.setVisibility(View.GONE);
                    publicationSmallAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(getSmallPublication);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);//creating an imputstrea
                Photo photo= new Photo(BitmapFactory.decodeStream(imageStream));//decoding the input stream to bitmap
                me.setPhotodeprofil(photo);
                photodeprofile.setImageBitmap(photo.getPhoto());

                //Glide.with(MyProfile.this).load(photo.getPhoto()).asBitmap().into(photodeprofile);

                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle("Please Wait");
                progress.setMessage("Uploading");
                progress.setCancelable(false);
                progress.show();

                uploadPhotoProfilRequest uploadPhotoProfilRequest=new uploadPhotoProfilRequest(me, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progress.dismiss();
                        try {
                            if (new JSONObject(s).getBoolean("success")) {
                                Toast.makeText(getActivity(), "Account Successfully Created", Toast.LENGTH_SHORT).show();

                                // this.finish();
                            } else
                                Toast.makeText(getActivity(), "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                requestQueue.add(uploadPhotoProfilRequest);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void initRatingBar(int confiance){
        RatingBar ratingBar=(RatingBar)getView().findViewById(R.id.rating_bar_confiance);
        if(confiance<0)
            ratingBar.setRating(0);
        else{
            if (confiance>100)
                ratingBar.setRating(5);
            else
                ratingBar.setRating(confiance/20);
        }

    }

    private void initNBRS(int nbfollowers,int nbfollowee,int nbpubs){
        TextView fs=(TextView) getView().findViewById(R.id.myprofileNumberFollowers);
        TextView fe=(TextView) getView().findViewById(R.id.myprofilNumberFollowee);
        TextView pubs=(TextView) getView().findViewById(R.id.myprofileNumberPub);
        fs.setText(""+nbfollowers);
        fe.setText(""+nbfollowee);
        pubs.setText(""+nbpubs);
    }

    private void initListeDesAderents() {
        final ArrayList<Profile> utilisateurs=new ArrayList<>();
        final ProfileRecyAdapter adapter=new ProfileRecyAdapter(utilisateurs,me);

        final RecyclerView recyclerView=(RecyclerView) getView().findViewById(R.id.utilisateur_adr_list_recy);
        final ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.utilisateur_adr_list_progress);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        AssobyUsrt asso=new AssobyUsrt(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.i("ovitale",s);
                    JSONObject jsonObject=new JSONObject(s);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if(jsonObject.getBoolean("success")){
                        int nbr=jsonObject.getInt("nbrAssociations");
                        if (nbr==0){
                            RelativeLayout relativeLayout=(RelativeLayout) getView().findViewById(R.id.utilisateur_adr_list_relative);
                            relativeLayout.setVisibility(View.GONE);
                        }
                        for (int i=0;i<nbr;i++){
                            JSONObject row=jsonObject.getJSONObject(""+i);
                            Profile profile=Profile.fromJsonINFOPROFILELITE(row.getJSONObject("infoprofil"));
                            utilisateurs.add(profile);
                            adapter.notifyDataSetChanged();
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(asso);


    }

}
