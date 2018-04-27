package com.taqeiddine.ihsan.VOLLEY.Association;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

public class LesDemandes extends StringRequest {
    private static final String URL = Help.getURL() + "apiassociation/demandes";


    public LesDemandes(Association association, Response.Listener<String> listener) {
        super(Method.GET, URL + "?idassociation=" + association.getIdprofile(), listener, null);


    }
}
