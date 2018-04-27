package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Publications.Publication;

public class InterventionsGet extends StringRequest {
    private static final String URL = Help.getURL() + "apiintervention/intbypub";

    public InterventionsGet (Publication publication, Response.Listener<String> listener) {
        super(Method.GET, URL+"?idpub="+publication.getIdpub(), listener,null);
    }
}
