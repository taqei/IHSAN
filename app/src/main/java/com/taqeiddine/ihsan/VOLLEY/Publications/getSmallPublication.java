package com.taqeiddine.ihsan.VOLLEY.Publications;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 18/03/2018.
 */

public class getSmallPublication extends StringRequest {
    private static final String URL = Help.getURL() + "apipublication/litepubs";
    private ArrayList<String> idpublications;
    private Map<String, String> parameters;
    public getSmallPublication(ArrayList<String> idpublications, Profile pubsofprofile,Utilisateur me,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        this.idpublications=idpublications;
        parameters = new HashMap<>();
        if(me!=null){
            parameters.put("myid", me.getIdprofile());
        }

        parameters.put("current_pubs", Publication.listedesPubtoJson(idpublications).toString());
        if(pubsofprofile!=null){
            parameters.put("pubsofprofil", pubsofprofile.getIdprofile());
        }


    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
