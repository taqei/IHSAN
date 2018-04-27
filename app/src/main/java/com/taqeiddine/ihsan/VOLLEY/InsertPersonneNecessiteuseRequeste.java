package com.taqeiddine.ihsan.VOLLEY;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 11/03/2018.
 */

public class InsertPersonneNecessiteuseRequeste extends StringRequest {
    private static final String REGISTER_URL = Help.getMyIP()+"IHSAN/insertPublicationPersonneNecessiteuse.php";
    private Map<String, String> parameters;

    public InsertPersonneNecessiteuseRequeste(SignalPN spn, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idpublicateur", spn.getPublicateur().getIdprofile());
        parameters.put("idprofil", spn.getProfile().getIdprofile());
        parameters.put("titre", spn.getTitrepub());
        parameters.put("descriptionpub",spn.getDescriptionpub());
        parameters.put("typeprenec", spn.getTypePN());
        parameters.put("numphonepub", spn.getNumphone());
        parameters.put("listbesoins", spn.besoinsToJson().toString());
        parameters.put("photo",spn.getAlbumPhoto().toJson().toString());



    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
