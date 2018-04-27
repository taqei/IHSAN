package com.taqeiddine.ihsan.VOLLEY.Profile;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Projet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 09/03/2018.
 */

public class getProfileInfo extends StringRequest {
   private final static  String URL = Help.getURL()+ "apiprofil/profileinfo";


    public getProfileInfo(Profile profile, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL+"?idprofil="+profile.getIdprofile(), listener, errorListener);

    }

}
