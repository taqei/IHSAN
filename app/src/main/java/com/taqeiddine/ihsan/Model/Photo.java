package com.taqeiddine.ihsan.Model;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Taqei on 17/03/2018.
 */

public class Photo {
    private String idphoto,url;
    private Bitmap photo;

    public Photo(){

    }

    public Photo(Bitmap photo) {
        this.photo = photo;
    }
    public Photo(String url){
        if (url!=null)

            this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Photo fromJson(JSONObject jsonObject){
        try{
            return new Photo(jsonObject.getString("urlphoto"));
        }catch (JSONException e){
            return null;
        }

    }

    public String getIdphoto() {
        return idphoto;
    }

    public void setIdphoto(String idphoto) {
        this.idphoto = idphoto;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }



    public  String  convertBitmapToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(array, Base64.DEFAULT);
    }
    public static  Bitmap convertStringtoBitMap(String string) {
        try{
            byte [] encodeByte=Base64.decode(string,Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }

    }



}
