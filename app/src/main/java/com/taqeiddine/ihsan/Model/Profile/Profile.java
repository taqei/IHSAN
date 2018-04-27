package com.taqeiddine.ihsan.Model.Profile;

import android.graphics.Bitmap;
import android.util.Log;

import com.taqeiddine.ihsan.Model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.BitSet;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Profile {
    private String idprofile;
    private Photo photodeprofil;


    public Profile() {

    }

    public Profile(String idprofile, Photo photodeprofil) {
        this.idprofile = idprofile;
        this.photodeprofil = photodeprofil;
    }

    public Profile(String idprofile) {
        this.idprofile = idprofile;
    }

    public String getIdprofile() {
        return idprofile;
    }

    public void setIdprofile(String idprofile) {
        this.idprofile = idprofile;
    }

    public Photo getPhotodeprofil() {
        return photodeprofil;
    }
    public void setPhotodeprofil(Photo photodeprofil) {
        this.photodeprofil = photodeprofil;
    }
    public static Profile fromJsonINFOPROFILE(JSONObject jsonObject){

        try{
            if (jsonObject.getBoolean("success")){
                if (jsonObject.getInt("child")==0){
                    return Utilisateur.fromJsonINFOUTILISATEUR(jsonObject);
                }
                if (jsonObject.getInt("child")==1){
                    return Association.fromJsonASSOCIATION(jsonObject);
                }
            }
        }catch (JSONException e){
            Log.i("takii",e.toString());
            return null;
        }
        return null;
    }

    public static Profile fromJsonINFOPROFILELITE(JSONObject jsonObject){

        try{

                if (jsonObject.getInt("child")==0){
                    Utilisateur utilisateur=Utilisateur.fromJsonLite(jsonObject);
                    return utilisateur;
                }
                if (jsonObject.getInt("child")==1) {
                    Association association = Association.fromJSONLite(jsonObject);
                    return association;
                }

        }catch (JSONException e){
            Log.i("takii",e.toString());
            return null;
        }
        return null;
    }
}
