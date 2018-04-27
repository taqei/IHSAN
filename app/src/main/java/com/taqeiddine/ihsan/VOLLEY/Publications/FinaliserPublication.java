package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Publications.Publication;

import java.util.HashMap;
import java.util.Map;

public class FinaliserPublication extends StringRequest {
    private static final String URL = Help.getURL()+"apipublication/finaliser";
    private Map<String, String> parameters;

    public FinaliserPublication(Publication publication, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("idpublication", publication.getIdpub());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
