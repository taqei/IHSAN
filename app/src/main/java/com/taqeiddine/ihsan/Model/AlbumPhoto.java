package com.taqeiddine.ihsan.Model;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Taqei on 17/03/2018.
 */

public class AlbumPhoto {
    private String idAlbum,nomALbum;
    private ArrayList<Photo>  listPhoto;

    public AlbumPhoto() {
        listPhoto=new ArrayList<Photo>();
    }

    public void addPhoto(Photo photo){
        listPhoto.add(photo);
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getNomALbum() {
        return nomALbum;
    }

    public void setNomALbum(String nomALbum) {
        this.nomALbum = nomALbum;
    }

    public ArrayList<Photo> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(ArrayList<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public JSONObject toJson(){
        JSONObject thereturn=new JSONObject();
        for (int i=1;i<listPhoto.size();i++){
            String theString=listPhoto.get(i).convertBitmapToString();
            try {
                thereturn.put(""+i,theString);
            }catch (Exception e){

            }
        }
        return thereturn;
    }
}
