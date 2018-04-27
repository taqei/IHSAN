package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;

public class SearchPublications extends StringRequest {
    private static final String URL = Help.getURL() + "apipublication/searchtitre";

    public SearchPublications (String titre, Profile profile, Response.Listener<String> listener) {
        super(Request.Method.GET, URL+"?titre="+titre+"&idprofil="+profile.getIdprofile(), listener,null);
    }
}
