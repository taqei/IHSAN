package com.taqeiddine.ihsan.VOLLEY.RapportAct;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.ActivityRapport;

import java.util.HashMap;
import java.util.Map;

public class InsertRapport extends StringRequest{
    private static final String URL = Help.getURL()+"apirapport/new";
    private Map<String, String> parameters;

    public InsertRapport(ActivityRapport rapport, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("titre",rapport.getTitre());
        parameters.put("desc",rapport.getText());
        parameters.put("idassociation",rapport.getAssociation().getIdprofile());
        parameters.put("idpublicateur",rapport.getChefAssociation().getIdprofile());
        parameters.put("photo",rapport.getAlbumPhoto().toJson().toString());
        parameters.put("idpub",rapport.getPublication().getIdpub());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
