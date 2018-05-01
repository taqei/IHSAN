package com.taqeiddine.ihsan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurMyActivity;
import com.taqeiddine.ihsan.Firebase.SharedPrefManager;
import com.taqeiddine.ihsan.Fragments.MyHome;
import com.taqeiddine.ihsan.Fragments.MyMessages;
import com.taqeiddine.ihsan.Fragments.MyUtilisateur;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.RegisterToken;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RequestQueue requestQueue;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.maincontent,new MyHome()).commit();


                case R.id.navigation_notifications:
                    //transaction.replace(R.id.maincontent,new BlankFragment()).commit();
                    return true;
                case R.id.navigation_myprofile:{
                    transaction.replace(R.id.maincontent,new MyUtilisateur()).commit();

                    return true;
                }
                case R.id.navigation_messages:{
                    transaction.replace(R.id.maincontent,new MyMessages()).commit();

                    return true;
                }

                case  R.id.navigation_recherche:
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        requestQueue=new Volley().newRequestQueue(this);

        Utilisateur utilisateur=new Utilisateur(getIntent().getStringExtra("myidutilisateur"));
        Log.i("khourii",SharedPrefManager.getInstance(this).getDeviceToken());
        Log.i("khourii",SharedPrefManager.getInstance(this).getDeviceToken());
        RegisterToken registerToken=new RegisterToken(utilisateur, SharedPrefManager.getInstance(this).getDeviceToken(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        });
        requestQueue.add(registerToken);

    }

}
