package com.taqeiddine.ihsan.Model.Publications;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Taqei on 09/03/2018.
 */

public class Publication {
    private  String idpub,titrepub,descriptionpub,numphone;
    private Date date;
    private AlbumPhoto albumPhoto;
    private int child; //0 spn / 1 projet
    private Utilisateur publicateur;
    private boolean pubfinalisee;

    private LatLng adressepublication;

    private ArrayList<Intervention> interventions;



    public Utilisateur getPublicateur() {
        return publicateur;
    }

    public void setPublicateur(Utilisateur publicateur) {
        this.publicateur = publicateur;
    }

    public AlbumPhoto getAlbumPhoto() {
        return albumPhoto;
    }

    public void setAlbumPhoto(AlbumPhoto albumPhoto) {
        this.albumPhoto = albumPhoto;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    private ArrayList<Besoin> listeDesBesoins;

    public Publication() {
    }

    public ArrayList<Intervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(ArrayList<Intervention> interventions) {
        this.interventions = interventions;
    }

    public Publication(String idpub) {
        this.idpub = idpub;
    }

    public LatLng getAdressepublication() {
        return adressepublication;
    }

    public void setAdressepublication(LatLng adressepublication) {
        this.adressepublication = adressepublication;
    }

    public Publication(String idpub, String titrepub, String descriptionpub, String numphone, AlbumPhoto albumPhoto, Utilisateur publicateur, Date date, ArrayList<Besoin> besoins, boolean pubfinalisee, LatLng adressepublication) {
        this.idpub = idpub;
        this.titrepub = titrepub;
        this.descriptionpub = descriptionpub;
        this.numphone = numphone;
        this.albumPhoto=albumPhoto;
        this.publicateur=publicateur;
        this.date=date;

        this.listeDesBesoins=besoins;
        this.pubfinalisee=pubfinalisee;
        this.adressepublication=adressepublication;
    }



    public void setIdpub(String idpub) {
        this.idpub = idpub;
    }

    public String getTitrepub() {
        return titrepub;
    }

    public void setTitrepub(String titrepub) {
        this.titrepub = titrepub;
    }

    public String getDescriptionpub() {
        return descriptionpub;
    }

    public void setDescriptionpub(String descriptionpub) {
        this.descriptionpub = descriptionpub;
    }

    public String getNumphone() {
        return numphone;
    }

    public void setNumphone(String numphone) {
        this.numphone = numphone;
    }

    public boolean isPubfinalisee() {
        return pubfinalisee;
    }

    public void setPubfinalisee(boolean pubfinalisee) {
        this.pubfinalisee = pubfinalisee;
    }

    public String getIdpub() {
        return idpub;
    }

    public ArrayList<Besoin> getListeDesBesoins() {
        return listeDesBesoins;
    }

    public void setListeDesBesoins(ArrayList<Besoin> listeDesBesoins) {
        this.listeDesBesoins = listeDesBesoins;
    }

    public static Publication fromJsonSmall(JSONObject s){
        Publication publication;
        publication=new Publication();
        // manque DATE TIME
        try {

            int i=0;
            publication.setIdpub(s.getString("idpublication"));
            publication.setTitrepub(s.getString("titrepub"));
            publication.setDescriptionpub(s.getString("descriptionpub"));
            if (s.getInt("pubfinalisee")==0)
                publication.setPubfinalisee(false);
            else
                publication.setPubfinalisee(true);
            ArrayList<Besoin> arrayListb=new ArrayList<>();
            JSONObject besoinjson=s.getJSONObject("besoins");
            int nbr=besoinjson.getInt("nbrbesoins");
            for (i=0;i<nbr;i++){
                Besoin b=Besoin.fromJson(besoinjson.getJSONObject(""+i));
                if (b!=null){
                    arrayListb.add(b);
                }
            }
            publication.setListeDesBesoins(arrayListb);
            Date datepub=new Date();
            try{
                datepub= Help.getDATE().parse(s.getString("datetimepublication"));
            } catch (ParseException e) {
                e.printStackTrace();
                datepub=null;
            }
            publication.setDate(datepub);
            try{
                Double latadresse =s.getDouble("latadresse"),lngadresse=s.getDouble("lngadresse");
                publication.setAdressepublication(new LatLng(latadresse,lngadresse));
            }catch (JSONException e){
                publication.setAdressepublication(null);
            }



            if (s.getInt("child")==0){
                return SignalPN.fromJSON(s,publication);
            }else{
                return Projet.fromJSON(s,publication);
            }

        } catch (JSONException e) {
            Log.i("jalaaal",e.toString());

            return null;
        }
    }
    public static Publication fromJSONLarge(JSONObject s) {
        Log.i("taki",s.toString());
        Publication publication;
        publication=new Publication();
        try {
            int i=0;

            publication.setIdpub(s.getString("idpublication"));
            publication.setTitrepub(s.getString("titrepub"));
            publication.setDescriptionpub(s.getString("descriptionpub"));
            publication.setNumphone(s.getString("numphonepub"));
            try{
                Double latadresse =s.getDouble("latadresse"),lngadresse=s.getDouble("lngadresse");
                publication.setAdressepublication(new LatLng(latadresse,lngadresse));
            }catch (JSONException e){
                publication.setAdressepublication(null);
            }
            if (s.getInt("pubfinalisee")==0)
                publication.setPubfinalisee(false);
            else
                publication.setPubfinalisee(true);

            Date datepub=new Date();
            try{
                datepub= Help.getDATE().parse(s.getString("datetimepublication"));
            } catch (ParseException e) {
                e.printStackTrace();
                datepub=null;
            }
            publication.setDate(datepub);
            ArrayList<Besoin> arrayListb=new ArrayList<Besoin>();
            JSONObject besoinjson=s.getJSONObject("besoins");
            int nbr=besoinjson.getInt("nbrbesoins");
            for (i=0;i<nbr;i++){
                Besoin b=Besoin.fromJson(besoinjson.getJSONObject(""+i));
                if (b!=null){
                    arrayListb.add(b);
                }
            }
            publication.setListeDesBesoins(arrayListb);
            AlbumPhoto albumPhoto=new AlbumPhoto();
            JSONObject photojson=s.getJSONObject("photos");
            nbr=photojson.getInt("nbrPhoto");
            for ( i=0;i<nbr;i++){
                JSONObject aphoto=photojson.getJSONObject(""+i);
                albumPhoto.addPhoto(new Photo(aphoto.getString("urlphoto")));
            }
            publication.setAlbumPhoto(albumPhoto);
            if (s.getInt("child")==0){
                return SignalPN.fromJSON(s,publication);
            }else{
                return Projet.fromJSON(s,publication);
            }

        } catch (JSONException e) {
            e.printStackTrace();


            return null;
        }

    }

    public JSONObject besoinsToJson(){
        JSONObject thereturn=new JSONObject();
        for (int i=1;i<listeDesBesoins.size();i++){
            JSONObject anobject=new JSONObject();
            try{
                anobject.put("idarticle",listeDesBesoins.get(i).getArticle().getIdarticle());
                anobject.put("qte",listeDesBesoins.get(i).getQte());
                thereturn.put(""+i,anobject);
            }catch(JSONException e){

            }

        }

        return thereturn;
    }

    public static JSONObject listedesPubtoJson(ArrayList<String> list){
            JSONObject thereturn=new JSONObject();
        try{
            for (int i=0;i<list.size();i++){
                thereturn.put(""+i,list.get(i));
            }
        }catch (JSONException e){

        }
        return thereturn;
    }

    public static  Publication fromJSONgetTitre(JSONObject s){
        Publication publication;
        try {
            String idpub=s.getString("idpublication");
            String titre=s.getString("titre");
            if (s.getInt("child")==0){
                publication=new SignalPN(idpub);
                publication.setTitrepub(titre);
                return publication;
            }
            if (s.getInt("child")==1){
                publication=new Projet(idpub);
                publication.setTitrepub(titre);
                return publication;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Publication fromJSONinterventions(JSONObject s){

        try {
            Publication publication= new Publication();
            publication.setIdpub(s.getString("idpublication"));
            int nbr=s.getInt("nbInterventions");
            ArrayList<Intervention> interventions=new ArrayList<>();
            for (int i=0;i<nbr;i++){
                Intervention intervention=Intervention.fromJSON(s.getJSONObject(""+i));
                interventions.add(intervention);
            }
            publication.setInterventions(interventions);
            return publication;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
