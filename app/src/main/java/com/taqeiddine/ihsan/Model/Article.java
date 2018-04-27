package com.taqeiddine.ihsan.Model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Taqei on 13/03/2018.
 */

public class Article {
    private String idarticle;
    private String uniteArticle,nomArticle;
    private Icone icone;

    public Article() {
    }

    public Icone getIcone() {
        return icone;
    }

    public void setIcone(Icone icone) {
        this.icone = icone;
    }

    public Article(String idarticle, String uniteArticle, String nomArticle, Icone icone) {
        this.idarticle = idarticle;

        this.uniteArticle = uniteArticle;
        this.nomArticle = nomArticle;
        this.icone=icone;
    }



    public Article(String idarticle, String nomArticle) {
        this.idarticle = idarticle;
        this.nomArticle = nomArticle;
    }

    public String getIdarticle() {
        return idarticle;
    }

    public void setIdarticle(String idarticle) {
        this.idarticle = idarticle;
    }

    public String getUniteArticle() {
        return uniteArticle;
    }

    public void setUniteArticle(String uniteArticle) {
        this.uniteArticle = uniteArticle;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public static Article fromJson(JSONObject jsonObject){
        try{
            Article a=new Article(jsonObject.getString("idarticle"),jsonObject.getString("unitearticle"),jsonObject.getString("nomarticle"),new Icone(jsonObject.getString("urlicone")));
            return a;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
