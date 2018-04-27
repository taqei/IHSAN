package com.taqeiddine.ihsan.Model.Profile;

import com.google.android.gms.maps.model.LatLng;
import com.taqeiddine.ihsan.Model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Association extends Profile {
    private String nomassociation,phone;
    private LatLng adresse;

    public Association() {
    }

    public Association(String idprofile) {
        super(idprofile);
    }

    public Association(String idprofile, String nomassociation, String phone,LatLng adresse) {
        super(idprofile);
        this.nomassociation = nomassociation;
        this.phone = phone;
        this.adresse = adresse;
    }


    public String getNomassociation() {
        return nomassociation;
    }

    public void setNomassociation(String nomassociation) {
        this.nomassociation = nomassociation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public static Association fromJSONLite(JSONObject jsonObject){
        try{
            Association association=new Association();
            association.setIdprofile(jsonObject.getString("idprofil"));
            association.setNomassociation(jsonObject.getString("nomassociation"));
            association.setPhotodeprofil(new Photo(jsonObject.getString("urlphoto")));
            return association;
        }catch(JSONException e){
            return null;
        }
    }

    public LatLng getAdresse() {
        return adresse;
    }

    public void setAdresse(LatLng adresse) {
        this.adresse = adresse;
    }

    public static Association fromJsonASSOCIATION(JSONObject jsonObject){
        try{
            Association association=new Association();
            association.setIdprofile(jsonObject.getString("idprofile"));
            association.setNomassociation(jsonObject.getString("nomassociation"));
            association.setPhone(jsonObject.getString("numphoneassociation"));
            try {
                double lat=jsonObject.getDouble("latadresse"),lng=jsonObject.getDouble("lngadresse");
                LatLng latLng=new LatLng(lat,lng);
                association.setAdresse(latLng);
            }catch(JSONException e){

            }


            association.setPhotodeprofil(new Photo(jsonObject.getString("url")));
            return association;
        }catch(JSONException e){

        }
        return null;
    }
}
