package com.taqeiddine.ihsan.Model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

public class Icone {
    private String idicone,url,lib;
    private Bitmap icone;

    public Icone() {
    }

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public Icone(String url) {
        this.url = url;
    }

    public String getIdicone() {
        return idicone;
    }

    public void setIdicone(String idicone) {
        this.idicone = idicone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getIcone() {
        return icone;
    }

    public void setIcone(Bitmap icone) {
        this.icone = icone;
    }

    public static Icone fromJSON(JSONObject jsonObject){

        try {
            Icone icone=new Icone();
            icone.setIdicone(jsonObject.getString("idicone"));
            icone.setUrl(jsonObject.getString("urlicone"));
            return icone;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
