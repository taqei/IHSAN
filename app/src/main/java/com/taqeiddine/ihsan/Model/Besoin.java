package com.taqeiddine.ihsan.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Taqei on 13/03/2018.
 */

public class Besoin {
    private String idPub;
    private int qte,qterecu;
    private Article article;


    public Besoin() {
    }

    public String getIdPub() {
        return idPub;
    }

    public void setIdPub(String idPub) {
        this.idPub = idPub;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public int getQterecu() {
        return qterecu;
    }

    public void setQterecu(int qterecu) {
        this.qterecu = qterecu;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public static Besoin fromJson (JSONObject jsonObject){
        Besoin besoin= new Besoin();
        try{
            //Article a=new Article(jsonObject.getString("idarticle"),jsonObject.getString("nomarticle"),jsonObject.getString("nomarticle"));
            besoin.setArticle(Article.fromJson(jsonObject));
            besoin.setQte(jsonObject.getInt("qte"));
            besoin.setQterecu(jsonObject.getInt("qterecu"));
            return besoin;
        }catch (JSONException e){
            return null;
        }
    }
}
