package com.taqeiddine.ihsan.Activities.ProfileActivities;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.Messages.MessageActivity;
import com.taqeiddine.ihsan.Activities.RapportActivities.RapportViewActivity;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.ProfileRecyAdapter;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Association.AdrEtat;
import com.taqeiddine.ihsan.VOLLEY.Association.AdrsbyAsso;
import com.taqeiddine.ihsan.VOLLEY.Association.Demande;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.DisFollow;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.Follow;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.Isfollower;
import com.taqeiddine.ihsan.VOLLEY.Profile.IsChefAssociation;
import com.taqeiddine.ihsan.VOLLEY.Profile.getProfileInfo;
import com.taqeiddine.ihsan.VOLLEY.Publications.getSmallPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssociationActivity extends AppCompatActivity {
    CircleImageView photodeprofil;
    TextView name;

    Button messages;

    RequestQueue requestQueue;

    Association association;
    Utilisateur me;

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
        setContentView(R.layout.activity_association);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        photodeprofil=(CircleImageView) findViewById(R.id.association_image);
        name=(TextView) findViewById(R.id.association_Name);



        progressBar=(ProgressBar) findViewById(R.id.association_progress);
        nestedScrollView=(NestedScrollView) findViewById(R.id.scroll_assoc);
        recyclerView=(RecyclerView) findViewById(R.id.recy_pub_association);

        requestQueue= Volley.newRequestQueue(this);
        publicationSmallAdapter.setRequestQueue(requestQueue);
        association=new Association(getIntent().getExtras().getString("myidassociation"));
        me=new Utilisateur(getIntent().getStringExtra("myidutilisateur"));
        final IsChefAssociation isChefAssociation=new IsChefAssociation(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getInt("chef")==1){
                        Association association=Association.fromJSONLite(jsonObject.getJSONObject("infoasso"));
                        ChefAssociation chefAssociation=new ChefAssociation(me,association);
                        me=chefAssociation;
                    }
                    initListeDesAderents();
                    publicationSmallAdapter.setMe(me);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(isChefAssociation);



        initfollowing();


        getProfileInfo profileInfo=new getProfileInfo(association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    Association association1=(Association) Profile.fromJsonINFOPROFILE(new JSONObject(s));
                    name.setText(association1.getNomassociation());
                    myToolbar.setTitle(association1.getNomassociation());
                    initNBRS(association1.getNbfollowers(),association1.getNbpublications());
                    if (association.getPhotodeprofil()!=null){
                        Glide.with(AssociationActivity.this).load(Help.getMedia()+association.getPhotodeprofil().getUrl()).into(photodeprofil);
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

        publicationSmallAdapter.setMe(me);
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
        initAdr();
        GETTHEPUBLICATIONS();
        initActivities();

    }

    private void initActivities(){
        TextView textView=(TextView) findViewById(R.id.association_actions_action3_text);
        ImageView imageView=(ImageView) findViewById(R.id.association_actions_action3_logo);

        textView.setClickable(true);imageView.setClickable(true);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AssociationActivity.this, RapportViewActivity.class);
                intent.putExtra("myidutilisateur",me.getIdprofile());
                intent.putExtra("idassociation",association.getIdprofile());
                startActivity(intent);
            }
        };
        imageView.setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);

    }

    private void initListeDesAderents() {
        final ArrayList<Profile> utilisateurs=new ArrayList<>();
        final ProfileRecyAdapter adapter=new ProfileRecyAdapter(utilisateurs,me);

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
        getSmallPublication getSmallPublication=new getSmallPublication(currentpubs,association,new Response.Listener<String>() {
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

    private void initfollowing(){
        final TextView demaT=(TextView) findViewById(R.id.association_actions_action0_sabonner),desaT=(TextView) findViewById(R.id.association_actions_action0_desabonner);
        final ImageView demaI=(ImageView)findViewById(R.id.association_actions_action0_logo),desaI=(ImageView) findViewById(R.id.association_actions_action0_logo1);
        final ProgressBar progree=(ProgressBar) findViewById(R.id.association_actions_action0_progress);
        demaI.setClickable(true);demaT.setClickable(true);
        desaI.setClickable(true);desaT.setClickable(true);
        demaI.setVisibility(View.GONE);demaT.setVisibility(View.GONE);
        desaI.setVisibility(View.GONE);desaT.setVisibility(View.GONE);
        View.OnClickListener abonner=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follow followw=new Follow(me, association, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        initfollowing();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        initfollowing();
                    }
                });
                requestQueue.add(followw);
            }
        };
        View.OnClickListener desabonner=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisFollow disfolloww=new DisFollow(me, association, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        initfollowing();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        initfollowing();
                    }
                });
                requestQueue.add(disfolloww);
            }
        };

        demaI.setOnClickListener(abonner);demaT.setOnClickListener(abonner);
        desaI.setOnClickListener(desabonner);desaT.setOnClickListener(desabonner);


        progree.setVisibility(View.VISIBLE);

        Isfollower isfollower=new Isfollower(me, association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getBoolean("isfollower")){
                        progree.setVisibility(View.GONE);

                        desaI.setVisibility(View.VISIBLE);desaT.setVisibility(View.VISIBLE);

                    }
                    else{
                        progree.setVisibility(View.GONE);
                        demaI.setVisibility(View.VISIBLE);demaT.setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(isfollower);
    }

    private void initAdr(){

        final ProgressBar progressBar2=(ProgressBar) findViewById(R.id.association_actions_action2_progress);
        final TextView dejadr=(TextView) findViewById(R.id.association_actions_action2_deja),dema=(TextView) findViewById(R.id.association_actions_action2_adrdem);
        final ImageView image=(ImageView)findViewById(R.id.association_actions_action2_logo);


        dema.setVisibility(View.GONE);
        dejadr.setVisibility(View.GONE);
        progressBar2.setVisibility(View.VISIBLE);
        AdrEtat adrEtat=new AdrEtat(me, association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    Log.i("ovitale",s);
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.getBoolean("success")){

                        if (jsonObject.getBoolean("adr")){
                            // HERE : soit il est adr soit sa demande n'est pas encore validée
                            if(jsonObject.getBoolean("accepte")){
                                //HERE : il est ADR
                                progressBar2.setVisibility(View.GONE);
                                dejadr.setText("Déja Adhérent ! ");
                                dejadr.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                            }else {
                                progressBar2.setVisibility(View.GONE);
                                dejadr.setText("Demandé ! ");
                                dejadr.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                            }
                        }else{
                            dema.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Demande demande1=new Demande(me, association, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            Log.i("ovitale",s);
                                            initAdr();
                                        }
                                    });
                                    requestQueue.add(demande1);
                                }
                            });
                            progressBar2.setVisibility(View.GONE);
                            dema.setVisibility(View.VISIBLE);
                            image.setVisibility(View.VISIBLE);

                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(adrEtat);
    }
    private void initNBRS(int nbfollowers,int nbpubs){
        TextView fs=(TextView) findViewById(R.id.my_association_NumberFollowers);
        TextView pubs=(TextView) findViewById(R.id.my_association_NumberPub);
        fs.setText(""+nbfollowers);
        ;
        pubs.setText(""+nbpubs);
    }
}
