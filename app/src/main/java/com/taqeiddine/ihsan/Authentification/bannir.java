package com.taqeiddine.ihsan.Authentification;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.getProfileInfo;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class bannir extends AppCompatActivity {
    RequestQueue requestQueue;
    CircleImageView photodeprofile;
    TextView name,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannir);
        requestQueue= Volley.newRequestQueue(this);
        Help.deleteData(this);
        Utilisateur me;
        me=new Utilisateur();
        me.setIdprofile(getIntent().getStringExtra("myidutilisateur"));

        name=(TextView) findViewById(R.id.myname);
        text=(TextView) findViewById(R.id.textbanni);
        photodeprofile=(CircleImageView) findViewById(R.id.my_profile_image);


        getProfileInfo getProfileInfo=new getProfileInfo(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    final Utilisateur utilisateur=(Utilisateur) Profile.fromJsonINFOPROFILE(jsonObject);
                    name.setText("Bonjour "+utilisateur.getPrenom()+" !");
                    if (utilisateur.getPhotodeprofil()!=null){
                        Glide.with(bannir.this).load(Help.getMedia()+utilisateur.getPhotodeprofil().getUrl()).into(photodeprofile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(getProfileInfo);
    }
}
