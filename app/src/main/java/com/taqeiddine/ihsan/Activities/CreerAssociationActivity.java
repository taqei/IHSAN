package com.taqeiddine.ihsan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.InsertAssociation;

import org.json.JSONException;
import org.json.JSONObject;

public class CreerAssociationActivity extends AppCompatActivity {
    AutoCompleteTextView nom,phone,adress;
    Button valider;

    Association association;
    ChefAssociation chefAssociation;

    Place place;
    LatLng adressea;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_association);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Créer une Association");
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);



        nom=(AutoCompleteTextView)findViewById(R.id.creerassociation_nomasso);
        phone=(AutoCompleteTextView) findViewById(R.id.creerassociation_numphone);
        adress=(AutoCompleteTextView) findViewById(R.id.creerassociation_adresse);

        requestQueue= Volley.newRequestQueue(this);
        chefAssociation=new ChefAssociation(getIntent().getExtras().getString("myidutilisateur"));
        initAdresse();
        valider=(Button) findViewById(R.id.go);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    association=new Association("",nom.getText().toString(),phone.getText().toString(),adressea);
                    chefAssociation.setAssociation(association);
                    InsertAssociation insertAssociation=new InsertAssociation(chefAssociation, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try{
                                Log.i("arwa",s);

                                JSONObject jsonObject=new JSONObject(s);
                                if(jsonObject.getBoolean("success")){
                                    Toast.makeText(CreerAssociationActivity.this,"Votre demande a été prise en charge",Toast.LENGTH_SHORT).show();
                                    CreerAssociationActivity.this.finish();
                                }
                            }catch(JSONException e){

                            }
                        }
                    });
                    requestQueue.add(insertAssociation);
                }
            }
        });

    }


    public boolean validate(){
        String text=nom.getText().toString();
        if (text==""){
            nom.setError("le nom de l'association est obligatoire");
            return false;
        }
        text=phone.getText().toString();
        if (text==""){
            nom.setError("le numero de téléphone est obligatoire");
            return false;
        }
        text=adress.getText().toString();
        if (text==""){
            adress.setError("l'adresse est obligatoire");
            return false;
        }
        return true;
    }

    public void initAdresse(){
        adress.setClickable(true);
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adress.setClickable(false);
                LatLngBounds latLngBounds =Help.getAd();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(latLngBounds);

                try {
                    startActivityForResult(builder.build(CreerAssociationActivity.this), 2000);
                } catch (Exception e) {
                    Log.e("taqi", e.getStackTrace().toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000){
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace( data,this);
                adress.setText(place.getAddress());
                adress.setClickable(true);
                adressea=place.getLatLng();

            }
        }
    }
}
