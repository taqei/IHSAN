package com.taqeiddine.ihsan.VOLLEY.Message;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Message;
import com.taqeiddine.ihsan.Model.Profile.Profile;

/**
 * Created by Taqei on 02/04/2018.
 */

public class getMessages extends StringRequest {
    private final static  String URL = Help.getURL()+ "apiprofil/messages";


    public getMessages(Profile profile, Profile profile1,String idmessage, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL+"?id1="+profile.getIdprofile()+"&id2="+profile1.getIdprofile()+"&last="+idmessage, listener, errorListener);

    }
}
