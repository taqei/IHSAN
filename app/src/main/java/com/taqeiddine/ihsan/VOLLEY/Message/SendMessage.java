package com.taqeiddine.ihsan.VOLLEY.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Message;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 02/04/2018.
 */

public class SendMessage extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/message";
    private Map<String, String> parameters;

    public SendMessage(Message message, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("idexp", message.getExp().getIdprofile());
        parameters.put("idrecep", message.getRecep().getIdprofile());
        parameters.put("msg", message.getMessage());


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
