package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 03/04/2018.
 */

public class InsertAssociation extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/newassociation";
    private Map<String, String> parameters;

    public InsertAssociation (ChefAssociation chefAssociation, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("nom", chefAssociation.getAssociation().getNomassociation());
        parameters.put("mobile", chefAssociation.getAssociation().getPhone());
        parameters.put("latadresse", ""+chefAssociation.getAssociation().getAdresse().latitude);
        parameters.put("lngadresse",""+ chefAssociation.getAssociation().getAdresse().longitude);
        parameters.put("idchefassociation", chefAssociation.getIdprofile());


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
