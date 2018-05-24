package com.taqeiddine.ihsan.Model.Profile;

import android.graphics.Bitmap;
import android.util.Log;

import com.taqeiddine.ihsan.Model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.BitSet;
import java.util.Objects;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Profile {
    private String idprofile;
    private Photo photodeprofil;
    private int nbfollowers=0,nbfollowee=0,nbpublications=0;

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

    public int getNbfollowers() {
        return nbfollowers;
    }

    public void setNbfollowers(int nbfollowers) {
        this.nbfollowers = nbfollowers;
    }

    public int getNbfollowee() {
        return nbfollowee;
    }

    public void setNbfollowee(int nbfollowee) {
        this.nbfollowee = nbfollowee;
    }

    public int getNbpublications() {
        return nbpublications;
    }

    public void setNbpublications(int nbpublications) {
        this.nbpublications = nbpublications;
    }

    @Override
    public boolean equals(Object o) {
        Profile profile = (Profile) o;
        return Objects.equals(idprofile, profile.idprofile);
    }


}
