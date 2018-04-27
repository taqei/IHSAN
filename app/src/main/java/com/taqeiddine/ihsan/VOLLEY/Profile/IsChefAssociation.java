package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

public class IsChefAssociation extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/usischef";


    public IsChefAssociation(Utilisateur me,Response.Listener<String> listener) {
        super(Request.Method.GET, URL + "?idutilisateur=" + me.getIdprofile(), listener,null);

    }
}
