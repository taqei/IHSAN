package com.taqeiddine.ihsan.Model;

import android.util.Log;

import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Taqei on 02/04/2018.
 */

public class Message {
    private Profile exp,recep;
    private String message,idMessage;
    private Date date;
    private boolean vu;


    public Message() {
    }

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
    }

    public Message(Profile exp, Profile recep, String message, String idMessage, Date date) {
        this.exp = exp;
        this.recep = recep;
        this.message = message;
        this.idMessage = idMessage;
        this.date = date;
    }

    public Profile getExp() {
        return exp;
    }

    public void setExp(Profile exp) {
        this.exp = exp;
    }

    public Profile getRecep() {
        return recep;
    }

    public void setRecep(Profile recep) {
        this.recep = recep;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static Message fromJson(JSONObject j){
        Message message=new Message();

                //"datetimemessage": "2018-04-02 17:34:08"
        try{
            message.setIdMessage(j.getString("idmessage"));

            if (j.getInt("type")==0){
                message.setExp(Profile.fromJsonINFOPROFILELITE(j.getJSONObject("idexp")));
                message.setRecep(Profile.fromJsonINFOPROFILELITE(j.getJSONObject("idrecep")));
                int i=j.getInt("vu");
                if (i==0)
                    message.setVu(false);
                else
                    message.setVu(true);
            }else{
                message.setExp(new Profile(j.getString("idexp")));
                message.setRecep(new Profile(j.getString("idrecep")));
            }


            message.setMessage(j.getString("message"));
            Date date;
            try{
                date= Help.getDATE().parse(j.getString("datetimemessage"));
            } catch (ParseException e) {
                e.printStackTrace();
                date=null;
            }
            message.setDate(date);
            return message;

        }catch(JSONException e){
            Log.i("jalal",e.toString());
            return null;
        }
    }
}
