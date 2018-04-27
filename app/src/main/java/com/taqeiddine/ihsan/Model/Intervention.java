package com.taqeiddine.ihsan.Model;


import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class Intervention {


    private String idintervention;
    private Date datetime;
    private Publication publication;
    private Utilisateur utilisateur;
    private ArrayList<Don> donsInterventions;
    private boolean interventionvalidee;


    public Intervention() {
    }

    public Intervention(Publication publication, Utilisateur utilisateur, ArrayList<Don> dInterventions) {
        this.publication = publication;
        this.utilisateur = utilisateur;
        this.donsInterventions = dInterventions;
    }

    public Intervention(String idintervention, Date datetime, Publication publication, Utilisateur utilisateur, ArrayList<Don> dInterventions) {
        this.idintervention = idintervention;
        this.datetime = datetime;
        this.publication = publication;
        this.utilisateur = utilisateur;
        this.donsInterventions = dInterventions;
    }

    public boolean isInterventionvalidee() {
        return interventionvalidee;
    }

    public void setInterventionvalidee(boolean interventionvalidee) {
        this.interventionvalidee = interventionvalidee;
    }

    public ArrayList<Don> getDonsInterventions() {
        return donsInterventions;
    }

    public void setDonsInterventions(ArrayList<Don> donsInterventions) {
        this.donsInterventions = donsInterventions;
    }

    public String getIdintervention() {
        return idintervention;
    }

    public void setIdintervention(String idintervention) {
        this.idintervention = idintervention;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }


    public JSONObject donsToJson(){
        JSONObject thereturn=new JSONObject();
        int j=0;
        for (int i=0;i<donsInterventions.size();i++){
            if (donsInterventions.get(i).getQte()!=0){
                JSONObject anobject=new JSONObject();
                try{
                    anobject.put("idarticle",donsInterventions.get(i).getBesoin().getArticle().getIdarticle());
                    anobject.put("qte",donsInterventions.get(i).getQte());
                    thereturn.put(""+j,anobject);
                    j++;
                }catch(JSONException e){

                }
            }
        }

        return thereturn;

    }

    public static Intervention fromJSON(JSONObject s){

        try {
            Intervention intervention=new Intervention();
            intervention.setIdintervention(s.getString("idintervention"));
            intervention.setPublication(new Publication(s.getString("idpublication")));
            JSONObject donss=s.getJSONObject("dons");
            int nbrdons=donss.getInt("nbDons");
            ArrayList<Don> donArrayList=new ArrayList<>();
            for (int i=0;i<nbrdons;i++){
                Don don= Don.fromJSON(donss.getJSONObject(""+i));
                donArrayList.add(don);
            }
            intervention.setDonsInterventions(donArrayList);
            intervention.setUtilisateur(Utilisateur.fromJsonLite(s.getJSONObject("infoprofil")));
            if(s.getInt("interventionvalidee")==0)
                intervention.setInterventionvalidee(false);
            else
                intervention.setInterventionvalidee(true);
            //manque la date
            return intervention;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
