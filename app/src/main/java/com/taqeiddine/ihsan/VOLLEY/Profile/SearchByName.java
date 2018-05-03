package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;

public class SearchByName extends StringRequest {
    private static final String URL = Help.getURL() + "apiprofil/search";

    public SearchByName (String name, Response.Listener<String> listener) {
        super(Request.Method.GET, URL+"?name="+name, listener,null);
    }
}
