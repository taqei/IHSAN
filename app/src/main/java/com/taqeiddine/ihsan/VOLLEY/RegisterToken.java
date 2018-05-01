package com.taqeiddine.ihsan.VOLLEY;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

public class RegisterToken extends StringRequest {
    private static final String URL = Help.getURL()+"apiprofil/registerdevice";
    private Map<String, String> parameters;

    public RegisterToken(Utilisateur ustilsateur,String token, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idutilisateur",ustilsateur.getIdprofile());
        parameters.put("token",token);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
