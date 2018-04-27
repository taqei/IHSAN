package com.taqeiddine.ihsan.Activities.Messages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Adapters.MessageSAdapter;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.R;

public class MessagesActivity extends AppCompatActivity {
    Profile me;
    RequestQueue requestQueue;
    RecyclerView messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        me=new Profile(getIntent().getStringExtra("myidprofil"));
        requestQueue= Volley.newRequestQueue(this);

        MessageSAdapter adapter=new MessageSAdapter(me,requestQueue,this);
        messages=(RecyclerView) findViewById(R.id.activiti_mesgs_recy);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        messages.setLayoutManager(mLayoutManager);
        messages.setItemAnimator(new DefaultItemAnimator());
        messages.setAdapter(adapter);

    }
}
