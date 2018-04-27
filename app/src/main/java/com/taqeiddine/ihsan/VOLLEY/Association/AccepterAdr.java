package com.taqeiddine.ihsan.VOLLEY.Association;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.DemandeAdr;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

public class AccepterAdr extends StringRequest {
    private static final String URL = Help.getURL()+"apiassociation/accepter";
    private Map<String, String> parameters;

    public AccepterAdr(DemandeAdr demandeAdr, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        Log.i("oviiii",demandeAdr.getDemandeur().getIdprofile()+" heyy "+demandeAdr.getAssociation().getIdprofile());
        parameters.put("idutilisateur",demandeAdr.getDemandeur().getIdprofile());
        parameters.put("idassociation",demandeAdr.getAssociation().getIdprofile());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
