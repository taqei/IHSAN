package com.taqeiddine.ihsan.VOLLEY;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 13/03/2018.
 */

public class GetArticles extends StringRequest {
    private static final String URL = Help.getURL() + "apipublication/articles";

    public GetArticles (String nomarticle,Response.Listener<String> listener) {
        super(Method.GET, URL+"?nomarticle="+nomarticle, listener,null);
    }
}
