package com.taqeiddine.ihsan.VOLLEY.Intervenire;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Intervention;

import java.util.HashMap;
import java.util.Map;

public class InsertIntervention extends StringRequest {
    private static final String URL = Help.getURL()+"apiintervention/intervention";
    private Map<String, String> parameters;

    public InsertIntervention(Intervention intervention, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("idutilisateur",intervention.getUtilisateur().getIdprofile());
        parameters.put("idpublication",intervention.getPublication().getIdpub());
        parameters.put("dons",intervention.donsToJson().toString());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
