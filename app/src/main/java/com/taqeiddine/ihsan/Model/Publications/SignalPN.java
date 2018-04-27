package com.taqeiddine.ihsan.Model.Publications;

import com.google.android.gms.maps.model.LatLng;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Taqei on 09/03/2018.
 */

public class SignalPN extends Publication {
    private Profile profile;
    private String typePN;

    public SignalPN(Profile profile, Utilisateur publicateur, String idpub, String titrepub, String descriptionpub, String numphone, String typePN, AlbumPhoto albumPhoto, Date date, ArrayList<Besoin> besoins, boolean pubfinalisee, LatLng adresse) {
        super(idpub, titrepub, descriptionpub, numphone,albumPhoto,publicateur,date,besoins,pubfinalisee,adresse);
        this.typePN = typePN;
        this.profile=profile;
    }

    public SignalPN(String idpub) {
        super(idpub);
    }

    public SignalPN(Publication p) {
        super(p.getIdpub(), p.getTitrepub(), p.getDescriptionpub(),p.getNumphone(),p.getAlbumPhoto(),p.getPublicateur(),p.getDate(),p.getListeDesBesoins(),p.isPubfinalisee(),p.getAdressepublication());
    }

    public String getTypePN() {
        return typePN;
    }

    public void setTypePN(String typePN) {
        this.typePN = typePN;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public static SignalPN fromJSON(JSONObject jsonObject,Publication publication) {
        SignalPN signalPN=null;
        try {
            signalPN=new SignalPN(publication);
            signalPN.setTypePN(jsonObject.getString("typepn"));
            signalPN.setProfile(Profile.fromJsonINFOPROFILELITE(jsonObject.getJSONObject("infoprofil")));

        }catch (JSONException e){
            return null;
        }

        return signalPN;
    }
}
