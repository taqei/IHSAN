package com.taqeiddine.ihsan.VOLLEY.Publications;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Publications.Projet;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taqei on 01/04/2018.
 */

public class PublierRequest extends StringRequest {
    private static final String URL = Help.getURL()+"apipublication/publier";
    private Map<String, String> parameters;

    public PublierRequest(Publication publication, Response.Listener<String> listener,Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("idpublicateur", publication.getPublicateur().getIdprofile());
        parameters.put("titre", publication.getTitrepub());
        parameters.put("descriptionpub",publication.getDescriptionpub());
        parameters.put("numphonepub", publication.getNumphone());
        parameters.put("listbesoins", publication.besoinsToJson().toString());
        parameters.put("photo",publication.getAlbumPhoto().toJson().toString());
        if(publication.getAdressepublication()!=null){
            parameters.put("latadresse",""+publication.getAdressepublication().latitude);
            parameters.put("lngadresse",""+publication.getAdressepublication().longitude);
        }

        if (publication instanceof SignalPN==true){
            SignalPN spn=(SignalPN) publication;
            parameters.put("child","0");
            parameters.put("idprofil", spn.getProfile().getIdprofile());
            parameters.put("typeprenec", spn.getTypePN());
        }
        if(publication instanceof Projet){
            Projet projet=(Projet) publication;

            parameters.put("child","1");
            parameters.put("idassociation",projet.getAssociation().getIdprofile() );
            parameters.put("debut",Help.getDATEYMD().format(projet.getDatedebutcollecte()));
            parameters.put("fin", Help.getDATEYMD().format(projet.getDatefincollecte()));
            parameters.put("open",Help.getDATEHMS1().format(projet.getHeureopen()) );
            parameters.put("close", Help.getDATEHMS1().format(projet.getHeureclose()));
            if(projet.getDestination()!=null){
                parameters.put("latdesti",""+projet.getDestination().latitude);
                parameters.put("lngdesti",""+projet.getDestination().longitude);
            }
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
