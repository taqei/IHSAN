package com.taqeiddine.ihsan.Model.Profile;

import android.util.Log;

import com.taqeiddine.ihsan.Model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Utilisateur extends Profile  {

    private String nom,prenom,phone,email,pass;
    private int confiance;

    public Utilisateur(String idprofile) {
        super(idprofile);
    }

    public Utilisateur() {

    }
    public Utilisateur(String iduser, String nom, String prenom, String phone, String email) {
        super(iduser);
        this.nom = nom;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
    }
    public Utilisateur(String iduser, String nom, String prenom, String phone, String email,String pass) {
        super(iduser);
        this.nom = nom;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
        this.pass=pass;
    }



    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public int getConfiance() {
        return confiance;
    }

    public void setConfiance(int confiance) {
        this.confiance = confiance;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public static  Utilisateur fromJsonINFOUTILISATEUR(JSONObject jsonObject){
        try{
            if (jsonObject.getBoolean("success")){
                if (jsonObject.getInt("child")==0){
                    Utilisateur utilisateur=new Utilisateur();
                    utilisateur.setIdprofile(jsonObject.getString("idprofil"));
                    utilisateur.setNom(jsonObject.getString("nomutilisateur"));
                    utilisateur.setPrenom(jsonObject.getString("prenomutilisateur"));
                    utilisateur.setPhone(jsonObject.getString("numphoneutilisateur"));
                    utilisateur.setEmail(jsonObject.getString("emailutilisateur"));
                    utilisateur.setConfiance(jsonObject.getInt("confiance"));
                    utilisateur.setNbfollowee(jsonObject.getInt("nbfollowee"));
                    utilisateur.setNbfollowers(jsonObject.getInt("nbfollowers"));
                    utilisateur.setNbpublications(jsonObject.getInt("nbpubs"));
                    if (jsonObject.getString("url")!=null){
                        Photo photo=new Photo();
                        photo.setUrl(jsonObject.getString("url").toString());
                        utilisateur.setPhotodeprofil(photo);
                    }
                    if (jsonObject.getInt("chef")==1){
                        Association association=Association.fromJSONLite(jsonObject.getJSONObject("infoasso"));
                        ChefAssociation chefAssociation=new ChefAssociation(utilisateur,association);
                        return chefAssociation;
                    }
                    return utilisateur;
                }
            }
        }catch (JSONException e){
            Log.i("takii",e.toString());
            return null;
        }
        return null;
    }

    public static Utilisateur fromJsonLite(JSONObject jsonObject){
        try{
            Utilisateur utilisateur=new Utilisateur();
            utilisateur.setIdprofile(jsonObject.getString("idprofil"));
            utilisateur.setPhotodeprofil(new Photo(jsonObject.getString("urlphoto")));
            utilisateur.setNom(jsonObject.getString("nomutilisateur"));
            utilisateur.setPrenom(jsonObject.getString("prenomutilisateur"));
            utilisateur.setConfiance(jsonObject.getInt("confiance"));
            return utilisateur;
        }catch (JSONException e){
            return null;
        }
    }
}
