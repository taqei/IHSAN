package com.taqeiddine.ihsan.Model;

import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Publications.Publication;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ActivityRapport {
    private String idActivity,titre,text;
    private Date date;
    private AlbumPhoto albumPhoto;
    private ChefAssociation chefAssociation;
    private Association association;
    private Publication publication;

    public ActivityRapport() {
    }

    public ActivityRapport(String idActivity, String titre, String text, Date date, AlbumPhoto albumPhoto, ChefAssociation chefAssociation, Association association, Publication publication) {
        this.idActivity = idActivity;
        this.titre = titre;
        this.text = text;
        this.date = date;
        this.albumPhoto = albumPhoto;
        this.chefAssociation = chefAssociation;
        this.association = association;
        this.publication = publication;
    }

    public String getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        this.idActivity = idActivity;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AlbumPhoto getAlbumPhoto() {
        return albumPhoto;
    }

    public void setAlbumPhoto(AlbumPhoto albumPhoto) {
        this.albumPhoto = albumPhoto;
    }

    public ChefAssociation getChefAssociation() {
        return chefAssociation;
    }

    public void setChefAssociation(ChefAssociation chefAssociation) {
        this.chefAssociation = chefAssociation;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }



    public static ActivityRapport fromJsonView(JSONObject jsonObject){
        ActivityRapport rapport=new ActivityRapport();
        try{
            rapport.setIdActivity(jsonObject.getString("idrapp"));
            rapport.setTitre(jsonObject.getString("titre"));
            rapport.setText(jsonObject.getString("descriptiorapp"));
            rapport.setAssociation(new Association(jsonObject.getString("idassociation")));

            //photos
            AlbumPhoto albumPhoto=new AlbumPhoto();
            JSONObject photojson=jsonObject.getJSONObject("photos");
            int nbr=photojson.getInt("nbrPhoto");
            for (int i=0;i<nbr;i++){
                JSONObject aphoto=photojson.getJSONObject(""+i);
                albumPhoto.addPhoto(new Photo(aphoto.getString("urlphoto")));
            }
            rapport.setAlbumPhoto(albumPhoto);

            //publication
            rapport.setPublication(Publication.fromJSONgetTitre(jsonObject.getJSONObject("publication")));
            return rapport;

        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
