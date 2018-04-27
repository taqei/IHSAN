package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 11/03/2018.
 */

public class uploadPhotoProfilRequest extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/photoprofil";
    private Map<String, String> parameters;

    public uploadPhotoProfilRequest(Profile profile, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idprofil", profile.getIdprofile());
        parameters.put("photo", profile.getPhotodeprofil().convertBitmapToString());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
