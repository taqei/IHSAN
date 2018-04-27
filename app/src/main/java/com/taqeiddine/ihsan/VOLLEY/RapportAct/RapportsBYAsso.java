package com.taqeiddine.ihsan.VOLLEY.RapportAct;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;


public class RapportsBYAsso extends StringRequest {
    private static final String URL = Help.getURL() + "apirapport/rapportsbyasso";
    public RapportsBYAsso(Association association, Response.Listener<String> listener) {
        super(Request.Method.GET, URL+"?idassociation="+association.getIdprofile(), listener, null);
    }
}
