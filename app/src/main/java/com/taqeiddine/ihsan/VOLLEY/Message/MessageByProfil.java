package com.taqeiddine.ihsan.VOLLEY.Message;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;

public class MessageByProfil extends StringRequest {
    private final static  String URL = Help.getURL()+ "apiprofil/messprofil";


    public MessageByProfil(Profile profile, Response.Listener<String> listener) {
        super(Method.GET, URL+"?idprofil="+profile.getIdprofile(), listener, null);

    }
}
