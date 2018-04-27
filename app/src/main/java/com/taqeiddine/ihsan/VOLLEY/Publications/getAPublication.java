package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Publications.Publication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 27/03/2018.
 */

public class getAPublication extends StringRequest {
    private static final String URL = Help.getURL() + "apipublication/pub";
    public getAPublication(Publication publication, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, URL+"?idpublication="+publication.getIdpub(), listener, errorListener);
    }


}
