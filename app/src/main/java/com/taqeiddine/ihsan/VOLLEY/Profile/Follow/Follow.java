package com.taqeiddine.ihsan.VOLLEY.Profile.Follow;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

/**
 * Created by Taqei on 01/04/2018.
 */

public class Follow extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/follow";


    public Follow(Utilisateur me, Profile other, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL + "?myid=" + me.getIdprofile() + "&otherid=" + other.getIdprofile(), listener, errorListener);

    }
}
