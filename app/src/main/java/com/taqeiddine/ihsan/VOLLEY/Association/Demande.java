package com.taqeiddine.ihsan.VOLLEY.Association;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

public class Demande extends StringRequest {

    private static final String URL = Help.getURL()+"apiassociation/new";
    private Map<String, String> parameters;

    public Demande(Utilisateur me, Association association, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idutilisateur",me.getIdprofile());
        parameters.put("idassociation",association.getIdprofile());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
