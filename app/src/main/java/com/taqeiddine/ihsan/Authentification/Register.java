package com.taqeiddine.ihsan.Authentification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaSync;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class Register extends AppCompatActivity {
    private AutoCompleteTextView email,nom,prenom,numphone,motdepasse,motdepasseConfirme;
    private RadioButton accepteTerme;


    private SimpleDateFormat simpleDateFormat;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.appbackground);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        requestQueue = Volley.newRequestQueue(Register.this);

    }



    public void CreerCompte(View view){

        String semail,snom,sprenom,smob,spass,spassConf,sdn;
        semail=email.getText().toString();
        snom=nom.getText().toString();
        spass=motdepasse.getText().toString();
        spassConf=motdepasseConfirme.getText().toString();
        sprenom=prenom.getText().toString();
        smob=numphone.getText().toString();
        Utilisateur utilisateur=new Utilisateur("",snom,sprenom,smob,semail,spass);
        if (validateMail(semail)&& validateMDP(spass,spassConf) && validateNom(snom) && validatePrenom(sprenom) && validatePhone(smob) && accepteTerme.isChecked() ){
            final ProgressDialog progress = new ProgressDialog(Register.this);
            progress.setTitle("Please Wait");
            progress.setMessage("Creating Your Account");
            progress.setCancelable(false);
            progress.show();
            //Validation Success

            RegisterRequest registerRequest = new RegisterRequest(utilisateur, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Response", response);
                    progress.dismiss();
                    try {
                        if (new JSONObject(response).getBoolean("success")) {
                            Toast.makeText(Register.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();

                            //Intent inte=new Intent(Register.this,Main2Activity.class);
                           // startActivity(inte);
                           // finish();
                        } else{
                            if (new JSONObject(response).getString("status").equals("email")){
                                Toast.makeText(Register.this, "UserName éxiste déja !", Toast.LENGTH_SHORT).show();
                                email.setError("UserName éxiste déja");
                            }
                            Toast.makeText(Register.this, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
                    }} catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            requestQueue.add(registerRequest);
        }

    }

    private void init(){
        email=(AutoCompleteTextView) findViewById(R.id.userMailReg);
        nom=(AutoCompleteTextView) findViewById(R.id.userNomFamilleReg);
        prenom=(AutoCompleteTextView) findViewById(R.id.userPrenomReg);
        numphone=(AutoCompleteTextView) findViewById(R.id.usernumPhoneReg);
        motdepasse=(AutoCompleteTextView) findViewById(R.id.userPasswordReg);
        motdepasseConfirme=(AutoCompleteTextView) findViewById(R.id.userPasswordConfirmeReg);
        accepteTerme=(RadioButton) findViewById(R.id.radioconfirmeReg);
    }

    private boolean validateMail(String string){
        if (string.equals("")){
            email.setError("username obli");
            return false;}


      return true;
    }
    private boolean validatePhone(String string){
        if (string.equals("")){
            numphone.setError("num phone obl");
            return false;}
        if (string.length()!=10){
            numphone.setError("Numéro érronée");
            return false;
        }
        if(string.charAt(0)!='0'){
            numphone.setError("Numéro érronée");
            return false;
        }
        return true;
    }
    private boolean validateNom(String string){
        if (string.equals("")){
            nom.setError("nom obl");
            return false;}
        return true;
    }
    private boolean validatePrenom(String string){
        if (string.equals("")){
            prenom.setError("prenom obl");
            return false;}
        return true;
    }

    private boolean validateMDP(String string,String string2){
        if (string.equals("") || string.length()<6){
            motdepasse.setError("mot de passe éroné");
            return false;}
        if (string.compareTo(string2)<0){
            motdepasseConfirme.setError("mot de passe éroné");
            return false;}
        return true;
    }
}
