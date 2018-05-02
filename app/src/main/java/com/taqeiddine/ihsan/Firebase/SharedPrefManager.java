package com.taqeiddine.ihsan.Firebase;

import android.content.Context;
import android.content.SharedPreferences;

import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref",SHARED_PREF_USER="USERSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    public boolean saveUserInfo(Utilisateur utilisateur){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailutilisateur", utilisateur.getEmail());
        editor.putString("passwordutilisateur",utilisateur.getPass());
        editor.putString("idutilisateur",utilisateur.getIdprofile());
        editor.commit();
        editor.apply();

        return true;
    }

    public  boolean saveChefAsso(ChefAssociation chefAssociation){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idassociation", chefAssociation.getAssociation().getIdprofile());
        editor.putInt("ichef",1);
        editor.commit();
        editor.apply();
        return true;
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("emailutilisateur", null);
    }

    public String getMDP(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("passwordutilisateur", null);
    }
    public String getID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("idutilisateur", null);
    }

    public int isChef(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt("ichef",0);
    }

    public String getIDAssociation(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("idassociation", null);
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }
}
