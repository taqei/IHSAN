package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 08/03/2018.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_URL = Help.getURL() + "apiprofil/register";
    private Map<String, String> parameters;

    public RegisterRequest(Utilisateur utilisateur, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("mail", utilisateur.getEmail());
        parameters.put("pass", utilisateur.getPass());
        parameters.put("nom", utilisateur.getNom());
        parameters.put("prenom", utilisateur.getPrenom());
        parameters.put("mob", utilisateur.getPhone());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
