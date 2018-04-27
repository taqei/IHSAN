package com.taqeiddine.ihsan.VOLLEY.Association;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;

public class AdrEtat extends StringRequest {

        private static final String URL = Help.getURL() + "apiassociation/etat";


        public AdrEtat(Utilisateur me, Association association, Response.Listener<String> listener) {
            super(Method.GET, URL + "?idassociation=" + association.getIdprofile() + "&idutilisateur=" + me.getIdprofile(), listener, null);


    }
}
