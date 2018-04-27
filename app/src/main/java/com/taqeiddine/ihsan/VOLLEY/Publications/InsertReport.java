package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Report;

import java.util.HashMap;
import java.util.Map;

public class InsertReport extends StringRequest {
    private static final String URL = Help.getURL()+"apireports/new";
    private Map<String, String> parameters;

    public InsertReport(Report report, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("idpublication", report.getPublication().getIdpub());
        parameters.put("idutilisateur", report.getUtilisateur().getIdprofile());
        parameters.put("rapport", report.getRapport());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
