package com.taqeiddine.ihsan.VOLLEY;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Article;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;

import java.util.HashMap;
import java.util.Map;

public class AjouterArticle extends StringRequest {
    private static final String URL = Help.getURL()+"apipublication/article";
    private Map<String, String> parameters;

    public AjouterArticle(Article article, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("nomarticle", article.getNomArticle());
        parameters.put("unitearticle", article.getUniteArticle());
        parameters.put("idicone", article.getIcone().getIdicone());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
