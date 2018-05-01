package com.taqeiddine.ihsan.Authentification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Activities.HomeActivity;
import com.taqeiddine.ihsan.Firebase.SharedPrefManager;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.LoginRequest;


import org.json.JSONException;
import org.json.JSONObject;



public class Authentification extends AppCompatActivity {
    AutoCompleteTextView email,mdp;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(Authentification.this);
        final String user= SharedPrefManager.getInstance(this).getUserName();
        final String mdpe=SharedPrefManager.getInstance(this).getMDP();
        if(user!=null && mdpe!=null){

            LoginRequest loginRequest=new LoginRequest(user, mdpe, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getBoolean("success")) {

                            Intent loginSuccess = new Intent(Authentification.this, HomeActivity.class);
                            loginSuccess.putExtra("myidutilisateur",jsonObject.getString("idprofil"));
                            //Passing all received data from server to next activity
                            startActivity(loginSuccess);
                            finish();
                        }}catch (JSONException e){

                        }
                }
            },null);
            requestQueue.add(loginRequest);
        }
        setContentView(R.layout.activity_authentification);
        getWindow().setBackgroundDrawableResource(R.drawable.appbackground);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inti();
        requestQueue = Volley.newRequestQueue(Authentification.this);



    }

    public void InscrivezVous(View view){
        Intent inte=new Intent(this,Register.class);
        startActivity(inte);

    }
    public void Connexion(View view){
        final String mail=email.getText().toString(),mp=mdp.getText().toString();

        if (validateEmail(mail) && validatePassword(mp)){
            final ProgressDialog progressDialog = new ProgressDialog(Authentification.this);
            progressDialog.setTitle("Attendez");
            progressDialog.setMessage("Logging You In");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final LoginRequest loginRequest = new LoginRequest(mail, mp, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Login Response", response);
                    progressDialog.dismiss();
                    // Response from the server is in the form if a JSON, so we need a JSON Object
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            Utilisateur utilisateur=new Utilisateur();
                            utilisateur.setEmail(email.getText().toString());
                            utilisateur.setPass(mdp.getText().toString());
                            SharedPrefManager.getInstance(Authentification.this).saveUserInfo(utilisateur);

                            Log.i("AOCCCCC",SharedPrefManager.getInstance(Authentification.this).getUserName()+ " hhhhh  "+SharedPrefManager.getInstance(Authentification.this).getMDP());
                            Intent loginSuccess = new Intent(Authentification.this, HomeActivity.class);
                            loginSuccess.putExtra("myidutilisateur",jsonObject.getString("idprofil"));
                            //Passing all received data from server to next activity
                            startActivity(loginSuccess);
                            finish();
                        } else {
                            if(jsonObject.getString("problem").equals("USER")){
                                Toast.makeText(Authentification.this, "User Not Found", Toast.LENGTH_SHORT).show();
                               }
                            else{
                                Toast.makeText(Authentification.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Authentification.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();

                    if (error instanceof ServerError)
                        Toast.makeText(Authentification.this, "Server Error", Toast.LENGTH_SHORT).show();
                    else if (error instanceof TimeoutError)
                        Toast.makeText(Authentification.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                    else if (error instanceof NetworkError)
                        Toast.makeText(Authentification.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(loginRequest);
        }
    }


    public void inti(){
        email=(AutoCompleteTextView) findViewById(R.id.userMailAuth);
        mdp=(AutoCompleteTextView) findViewById(R.id.userPasswordAuth);
    }

    private boolean validateEmail(String emaile){
        if (emaile.equals("")){
            email.setError("e-mail obl");
            return false;}
        return true;
    }
    private boolean validatePassword(String pass){
        if (pass.equals("")){
            mdp.setError("e-mail obl");
            return false;}
        return true;
    }
}
