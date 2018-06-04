package com.taqeiddine.ihsan.Activities.ProfileActivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.Messages.MessagesActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.ProjetActivity;
import com.taqeiddine.ihsan.Activities.RapportActivities.RapportActivitieActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalerPNActiviy;
import com.taqeiddine.ihsan.Activities.RapportActivities.RapportViewActivity;
import com.taqeiddine.ihsan.Adapters.DemandeAdrAdapter;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.ProfileRecyAdapter;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.DemandeAdr;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Association.AdrsbyAsso;
import com.taqeiddine.ihsan.VOLLEY.Association.LesDemandes;
import com.taqeiddine.ihsan.VOLLEY.Profile.*;
import com.taqeiddine.ihsan.VOLLEY.Publications.getSmallPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AssociationAdminActivity extends AppCompatActivity {
    de.hdodenhof.circleimageview.CircleImageView photodeprofil;
    ImageView changephotoprofil;
    TextView name;


    RequestQueue requestQueue;

    Association association;
    ChefAssociation chefAssociation;

    final ArrayList<Publication> publications=new ArrayList<>();
    final ArrayList<String> currentpubs=new ArrayList<>();
    private boolean allPubs=false;

    final PublicationSmallAdapter publicationSmallAdapter=new PublicationSmallAdapter(publications,null,null);

    ProgressBar progressBar;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association_admin);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

        photodeprofil=(CircleImageView) findViewById(R.id.my_association_image);
        changephotoprofil=(ImageView) findViewById(R.id.my_association_changeImage);
        name=(TextView) findViewById(R.id.my_association_Name);




        progressBar=(ProgressBar) findViewById(R.id.association_my_progress);
        nestedScrollView=(NestedScrollView) findViewById(R.id.scroll_my_assoc);
        recyclerView=(RecyclerView) findViewById(R.id.recy_pub_my_association);

        requestQueue= Volley.newRequestQueue(this);
        publicationSmallAdapter.setRequestQueue(requestQueue);
        association=new Association(getIntent().getExtras().getString("myidassociation"));
        chefAssociation =new ChefAssociation(getIntent().getExtras().getString("myidchef"));
        chefAssociation.setAssociation(association);

        changephotoprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        getProfileInfo profileInfo=new getProfileInfo(association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    Association association1=(Association) Profile.fromJsonINFOPROFILE(new JSONObject(s));
                    name.setText(association1.getNomassociation());

                    myToolbar.setTitle(association1.getNomassociation());
                    initNBRS(association1.getNbfollowers(),association1.getNbpublications());
                    if (association1.getPhotodeprofil()!=null){
                        Glide.with(AssociationAdminActivity.this).load(Help.getMedia()+association1.getPhotodeprofil().getUrl()).into(photodeprofil);
                    }


                }catch(JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(profileInfo);


        publicationSmallAdapter.setMe(chefAssociation);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(publicationSmallAdapter);
        recyclerView.setNestedScrollingEnabled(false);

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

        initSPN();
        initADR();
        initProjet();
        initActivities();
        GETTHEPUBLICATIONS();
        initListeDesAderents();
        initMessages();
        initActivitiesAll();





    }


    private void initListeDesAderents() {
        final ArrayList<Profile> utilisateurs=new ArrayList<>();
        final ProfileRecyAdapter adapter=new ProfileRecyAdapter(utilisateurs,chefAssociation);

        final RecyclerView recyclerView=(RecyclerView) findViewById(R.id.association_adr_list_recy);
        final ProgressBar progressBar=(ProgressBar) findViewById(R.id.association_adr_list_progress);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        AdrsbyAsso asso=new AdrsbyAsso(association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.i("ovitale",s);
                    JSONObject jsonObject=new JSONObject(s);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if(jsonObject.getBoolean("success")){
                        int nbr=jsonObject.getInt("nbrAdherents");
                        if (nbr==0){
                            RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.association_adr_list_relative);
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
    private void GETTHEPUBLICATIONS(){
        getSmallPublication getSmallPublication=new getSmallPublication(currentpubs,association,null,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try{

                    JSONObject jsonObject=new JSONObject(s);

                    int nbr=jsonObject.getInt("nbrPub");
                    Log.i("imaad",""+nbr);

                    ArrayList<Publication> arrayList=new ArrayList<>();
                    for (int i=0;i<nbr;i++){
                        JSONObject jsonObject1=jsonObject.getJSONObject(""+i);
                        Publication publication= Publication.fromJsonSmall(jsonObject1);
                        if (publication!=null){
                                arrayList.add(publication);
                                currentpubs.add(publication.getIdpub());

                        }


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
                BitmapFactory.Options options = new BitmapFactory.Options();
                //swt the color scheme to something less memory consuming
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //scale the image by factor 2
                options.inSampleSize = 2;
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                Photo photo= new Photo(BitmapFactory.decodeStream(imageStream,null,options));//decoding the input stream to bitmap
                association.setPhotodeprofil(photo);
                photodeprofil.setImageBitmap(photo.getPhoto());

                //Glide.with(MyProfile.this).load(photo.getPhoto()).asBitmap().into(photodeprofile);

                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Please Wait");
                progress.setMessage("Uploading");
                progress.setCancelable(false);
                progress.show();

                uploadPhotoProfilRequest uploadPhotoProfilRequest=new uploadPhotoProfilRequest(association, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progress.dismiss();
                        try {
                            if (new JSONObject(s).getBoolean("success")) {
                               // Toast.makeText(AssociationAdminActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();

                                // this.finish();
                            } else
                                Toast.makeText(AssociationAdminActivity.this, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
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


    public void initSPN(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action00_logo);
        TextView textView=(TextView) findViewById(R.id.association_actions_action00_text);

        imageView.setClickable(true);textView.setClickable(true);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationAdminActivity.this, SignalerPNActiviy.class);
                intent.putExtra("myidutilisateur",chefAssociation.getIdprofile());
                intent.putExtra("chef",1);
                intent.putExtra("myidassociation",chefAssociation.getAssociation().getIdprofile());
                startActivity(intent);
            }
        };

        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);


    }
    public void initProjet(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action01_logo);
        TextView textView=(TextView) findViewById(R.id.association_actions_action01_text);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationAdminActivity.this, ProjetActivity.class);
                intent.putExtra("myidutilisateur",chefAssociation.getIdprofile());
                intent.putExtra("chef",1);
                intent.putExtra("myidassociation",chefAssociation.getAssociation().getIdprofile());
                startActivity(intent);
            }
        };

        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);

    }

    public void initActivities(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action02_logo);
        TextView textView=(TextView) findViewById(R.id.association_actions_action02_text);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationAdminActivity.this, RapportActivitieActivity.class);
                intent.putExtra("myidutilisateur",chefAssociation.getIdprofile());
                intent.putExtra("idassociation",chefAssociation.getAssociation().getIdprofile());
                startActivity(intent);
            }
        };

        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);
    }

    public void initMessages(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action03_logo);
        TextView textView=(TextView) findViewById(R.id.myassociation_actions_action03_text);


        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationAdminActivity.this, MessagesActivity.class);
                intent.putExtra("myidprofil",chefAssociation.getAssociation().getIdprofile());
                startActivity(intent);

            }
        };

        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);
    }

    public void initADR(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action04_logo);
        TextView textView=(TextView) findViewById(R.id.myassociation_actions_action04_text);

        imageView.setClickable(true);textView.setClickable(true);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(ViSHOWDEMANDESew v) {
                ();
            }
        };

        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);

    }

    public void initActivitiesAll(){
        ImageView imageView=(ImageView) findViewById(R.id.myassociation_actions_action05_logo);
        TextView textView=(TextView) findViewById(R.id.myassociation_actions_action05_text);
        textView.setClickable(true);imageView.setClickable(true);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationAdminActivity.this, RapportViewActivity.class);
                intent.putExtra("myidutilisateur",chefAssociation.getIdprofile());
                intent.putExtra("idassociation",chefAssociation.getAssociation().getIdprofile());
                startActivity(intent);
            }
        };
        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);
    }

    private void SHOWDEMANDES() {
        final Dialog dialog=new Dialog(AssociationAdminActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_demande_adr);
        final ListView listView=(ListView)dialog.findViewById(R.id.dialogue_demande_adr_listview);
        final ArrayList<DemandeAdr> demandeAdrs=new ArrayList<>();
        final DemandeAdrAdapter adapter=new DemandeAdrAdapter(AssociationAdminActivity.this,demandeAdrs,chefAssociation,requestQueue);
        listView.setAdapter(adapter);
        LesDemandes demandes=new LesDemandes(association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    Log.i("ovitale",s);
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.getBoolean("success")){
                        dialog.show();
                        int nbr=jsonObject.getInt("nbrDeamndes");
                        if(nbr==0){
                            listView.setVisibility(View.GONE);
                            TextView textView =(TextView) dialog.findViewById(R.id.dialogue_demande_adr_no);
                            textView.setVisibility(View.VISIBLE);
                        }
                        for (int i=0;i<nbr;i++){
                            demandeAdrs.add(DemandeAdr.fromJson(jsonObject.getJSONObject(""+i),association));
                            adapter.notifyDataSetChanged();

                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(demandes);

    }
    private void initNBRS(int nbfollowers,int nbpubs){
        TextView fs=(TextView) findViewById(R.id.my_association_NumberFollowers);
        TextView pubs=(TextView) findViewById(R.id.my_association_NumberPub);
        fs.setText(""+nbfollowers);
        ;
        pubs.setText(""+nbpubs);
    }
}
