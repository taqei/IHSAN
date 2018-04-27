package com.taqeiddine.ihsan.Model;

import android.util.JsonReader;

import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class DemandeAdr {
    private Utilisateur demandeur;
    private Association association;
    private Date date;
    private boolean accepté;

    public DemandeAdr() {
    }

    public DemandeAdr(Utilisateur demandeur, Association association) {
        this.demandeur = demandeur;
        this.association = association;
    }

    public DemandeAdr(Utilisateur demandeur, Association association, Date date) {
        this.demandeur = demandeur;
        this.association = association;
        this.date = date;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur demandeur) {
        this.demandeur = demandeur;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAccepté() {
        return accepté;
    }

    public void setAccepté(boolean accepté) {
        this.accepté = accepté;
    }

    public static DemandeAdr fromJson(JSONObject jsonObject,Association association){
        DemandeAdr demandeAdr=new DemandeAdr();
        try{
            demandeAdr.setDemandeur(Utilisateur.fromJsonLite(jsonObject.getJSONObject("infoprofil")));
            Date datepub;
            try{
                datepub= Help.getDATE().parse(jsonObject.getString("dateadheison"));
            } catch (ParseException e) {
                e.printStackTrace();
                datepub=null;
            }
            demandeAdr.setDate(datepub);
            demandeAdr.setAssociation(association);
            demandeAdr.setAccepté(false);
        }catch (JSONException e){

        }

        return demandeAdr;
    }
}
