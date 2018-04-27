package com.taqeiddine.ihsan.Model.Publications;


import com.google.android.gms.maps.model.LatLng;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Projet extends Publication {
    private Association association;
    private Date datedebutcollecte,datefincollecte,heureopen,heureclose;
    private LatLng destination;

    public Projet(String idpub) {
        super(idpub);
    }

    public Projet(String idpub, String titrepub, String descriptionpub, String numphone, AlbumPhoto albumPhoto, Utilisateur publicateur, Date date, Association association, Date datedebutcollecte, Date datefincollecte, Date heureopen, Date heureclose,LatLng destination, ArrayList<Besoin> besoins, boolean pubfinalisee, LatLng adressepub) {
        super(idpub, titrepub, descriptionpub, numphone, albumPhoto, publicateur, date,besoins,pubfinalisee,adressepub);
        this.association = association;
        this.datedebutcollecte = datedebutcollecte;
        this.datefincollecte = datefincollecte;
        this.heureopen = heureopen;
        this.heureclose = heureclose;
        this.destination=destination;
    }

    public Projet(Publication p) {
        super(p.getIdpub(), p.getTitrepub(), p.getDescriptionpub(),p.getNumphone(),p.getAlbumPhoto(),p.getPublicateur(),p.getDate(),p.getListeDesBesoins(),p.isPubfinalisee(),p.getAdressepublication());
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Date getDatedebutcollecte() {
        return datedebutcollecte;
    }

    public void setDatedebutcollecte(Date datedebutcollecte) {
        this.datedebutcollecte = datedebutcollecte;
    }

    public Date getDatefincollecte() {
        return datefincollecte;
    }

    public void setDatefincollecte(Date datefincollecte) {
        this.datefincollecte = datefincollecte;
    }

    public Date getHeureopen() {
        return heureopen;
    }

    public void setHeureopen(Date heureopen) {
        this.heureopen = heureopen;
    }

    public Date getHeureclose() {
        return heureclose;
    }

    public void setHeureclose(Date heureclose) {
        this.heureclose = heureclose;
    }

    public static  Projet fromJSON(JSONObject jsonObject,Publication publication){
        Projet projet=null;
        try {
            projet=new Projet(publication);
            Date deb,fin,open,close;
            try{
                Double latdesti=jsonObject.getDouble("latdesti");
                Double lngdesti=jsonObject.getDouble("lngdesti");
                LatLng latLng=new LatLng(latdesti,lngdesti);
                projet.setDestination(latLng);
            }catch(JSONException e){
                projet.setDestination(null);
            }


            deb= Help.getDATEYMD().parse(jsonObject.getString("datedebutcollecte"));
            fin= Help.getDATEYMD().parse(jsonObject.getString("datefincollecte"));
            open= Help.getDATEHMS().parse(jsonObject.getString("heureopen"));
            close= Help.getDATEHMS().parse(jsonObject.getString("heureclose"));
            projet.setDatedebutcollecte(deb);
            projet.setDatefincollecte(fin);
            projet.setHeureopen(open);
            projet.setHeureclose(close);
            projet.setAssociation((Association)Profile.fromJsonINFOPROFILELITE(jsonObject.getJSONObject("infoprofil")));

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return projet;
    }


}
