package com.taqeiddine.ihsan.VOLLEY.Intervenire;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Intervention;

import java.util.HashMap;
import java.util.Map;

public class Approuver extends StringRequest {
    private static final String URL = Help.getURL() + "apiintervention/approuver";
    private Map<String, String> parameters;


    public Approuver(Intervention intervention, Response.Listener<String> listener) {
        super(Method.POST, URL , listener,null);
        parameters = new HashMap<>();
        parameters.put("idintervention",intervention.getIdintervention());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
