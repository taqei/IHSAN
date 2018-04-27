package com.taqeiddine.ihsan.VOLLEY.Publications.Follow;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;

public class IsFollowerPub extends StringRequest {

    private static final String URL = Help.getURL() + "apipublication/isfollow";


    public IsFollowerPub(Utilisateur me, Publication publication, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL + "?idutilisateur=" + me.getIdprofile() + "&idpublication=" + publication.getIdpub(), listener, errorListener);

    }
}
