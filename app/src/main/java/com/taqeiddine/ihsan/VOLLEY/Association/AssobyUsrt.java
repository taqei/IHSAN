package com.taqeiddine.ihsan.VOLLEY.Association;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

public class AssobyUsrt extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/asbyut";


    public AssobyUsrt(Utilisateur utilisateur, Response.Listener<String> listener) {
        super(Request.Method.GET, URL + "?idutilisateur=" + utilisateur.getIdprofile() , listener, null);


    }
}
