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
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.taqeiddine.ihsan.Activities.Messages.MessageActivity;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.DisFollow;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.Follow;
import com.taqeiddine.ihsan.VOLLEY.Profile.Follow.Isfollower;
import com.taqeiddine.ihsan.VOLLEY.Profile.getProfileInfo;
import com.taqeiddine.ihsan.VOLLEY.Publications.getSmallPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UtilisateurActivity extends AppCompatActivity {
   protected TextView nameandlastname,nbrfollowers,nbrpub,nivconfiance;
    protected Button follow,disfollow,messages;
    protected CircleImageView photodeprofile;


    private RequestQueue requestQueue;
    final ArrayList<Publication> publications=new ArrayList<>();
    final ArrayList<String> currentpubs=new ArrayList<>();
    private boolean allPubs=false;
    final PublicationSmallAdapter publicationSmallAdapter=new PublicationSmallAdapter(publications,null,null);

    ProgressBar progressBar;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    private Utilisateur me;
    private Utilisateur other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileother);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameandlastname =(TextView) findViewById(R.id.profileotherName);

        follow=(Button) findViewById(R.id.profileotherBtnOne);
        disfollow=(Button) findViewById(R.id.profileotherBtnOneO);
        messages=(Button) findViewById(R.id.profileBtnTwo);
        photodeprofile=(CircleImageView) findViewById(R.id.other_profile_image);
        me=new Utilisateur();
        me.setIdprofile(getIntent().getStringExtra("myidutilisateur"));
        other=new Utilisateur();
        other.setIdprofile(getIntent().getStringExtra("otheridprofil"));

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UtilisateurActivity.this,MessageActivity.class);
                intent.putExtra("myidprofil",me.getIdprofile());
                intent.putExtra("otheridprofil",other.getIdprofile());
                startActivity(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        publicationSmallAdapter.setRequestQueue(requestQueue);
        getProfileInfo getProfileInfo =new getProfileInfo(other, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")){
                        Utilisateur utilisateur;
                        utilisateur= (Utilisateur) Profile.fromJsonINFOPROFILE(jsonObject);
                        initRatingBar(utilisateur.getConfiance());
                        initNBRS(utilisateur.getNbfollowers(),utilisateur.getNbfollowee(),utilisateur.getNbpublications());
                        nameandlastname.setText(utilisateur.getNom()+"  "+utilisateur.getPrenom());
                        myToolbar.setTitle(utilisateur.getNom()+"  "+utilisateur.getPrenom());
                        if (utilisateur.getPhotodeprofil()!=null){
                            Glide.with(UtilisateurActivity.this).load(Help.getMedia()+utilisateur.getPhotodeprofil().getUrl()).into(photodeprofile);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(UtilisateurActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(UtilisateurActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(UtilisateurActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        publicationSmallAdapter.setMe(me);
        requestQueue.add(getProfileInfo);
        progressBar=(ProgressBar) findViewById(R.id.profile_other_progress_follow);



        recyclerView=(RecyclerView) findViewById(R.id.recy_pub_other_profile);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(publicationSmallAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView=(NestedScrollView) findViewById(R.id.scroll_other_profile);
        progressBar=(ProgressBar) findViewById(R.id.profile_other_progress);

        initfollowing();

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follow followw=new Follow(me, other, new Response.Listener<String>() {
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
        });
        disfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisFollow disfolloww=new DisFollow(me, other, new Response.Listener<String>() {
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
        });

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
    private void initfollowing(){
        progressBar.setVisibility(View.VISIBLE);
        disfollow.setVisibility(View.GONE);
        follow.setVisibility(View.GONE);
        Isfollower isfollower=new Isfollower(me, other, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getBoolean("isfollower")){
                        progressBar.setVisibility(View.GONE);
                        disfollow.setVisibility(View.VISIBLE);
                        follow.setVisibility(View.GONE);
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        follow.setVisibility(View.VISIBLE);
                        disfollow.setVisibility(View.GONE);
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

   private void GETTHEPUBLICATIONS(){
        getSmallPublication getSmallPublication=new getSmallPublication(currentpubs,other,new Response.Listener<String>() {
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
    private void initRatingBar(int confiance){
        RatingBar ratingBar=(RatingBar)findViewById(R.id.rating_bar_confiance);
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
        TextView fs=(TextView) findViewById(R.id.myprofileNumberFollowers);
        TextView fe=(TextView) findViewById(R.id.myprofilNumberFollowee);
        TextView pubs=(TextView) findViewById(R.id.myprofileNumberPub);
        fs.setText(""+nbfollowers);
        fe.setText(""+nbfollowee);
        pubs.setText(""+nbpubs);
    }

}
