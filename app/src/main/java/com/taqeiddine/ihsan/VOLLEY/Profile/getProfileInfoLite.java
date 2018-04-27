package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;

/**
 * Created by Taqei on 03/04/2018.
 */

public class getProfileInfoLite extends StringRequest {
    private final static  String URL = Help.getURL()+ "apiprofil/profileinfolite";


    public getProfileInfoLite(Profile profile, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL+"?idprofil="+profile.getIdprofile(), listener, errorListener);

    }

}