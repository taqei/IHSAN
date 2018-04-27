package com.taqeiddine.ihsan.VOLLEY;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.taqeiddine.ihsan.Help;

public class  GetIcones extends StringRequest{

        private static final String URL = Help.getURL() + "apiicones/icones";

        public GetIcones (Response.Listener<String> listener) {
            super(Method.GET, URL, listener,null);
        }

}
