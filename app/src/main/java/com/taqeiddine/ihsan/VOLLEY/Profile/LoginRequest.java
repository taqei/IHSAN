package com.taqeiddine.ihsan.VOLLEY.Profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 08/03/2018.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_URL = Help.getURL() + "apiprofil/login";


    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, LOGIN_URL + "?email=" + email + "&password=" + password, listener, errorListener);

    }
}

