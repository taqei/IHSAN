package com.taqeiddine.ihsan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taqeiddine.ihsan.Authentification.Authentification;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this, Authentification.class);
        startActivity(intent);
        /*MyProfile taq=);
        taq.setIdUtilisateur("1");*/
        //MyProfile profilFragment=new MyProfile();
        //profilFragment.onCreate(savedInstanceState);

        /*FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.myfrag,new MyProfile());
        ft.commit();*/




    }


}
