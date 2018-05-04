package com.taqeiddine.ihsan.Activities.RapportActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.RapportAdapter;
import com.taqeiddine.ihsan.Model.ActivityRapport;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.RapportAct.RapportsBYAsso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RapportViewActivity extends AppCompatActivity {

    Utilisateur me;
    Association association;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport_view);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Activities");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        requestQueue= Volley.newRequestQueue(this);


        me=new Utilisateur(getIntent().getStringExtra("myidutilisateur"));
        association=new Association(getIntent().getStringExtra("idassociation"));

        initActivities();
    }

    private void initActivities(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.rapport_view_recy);
        final ArrayList<ActivityRapport> rapports=new ArrayList<>();
        final RapportAdapter adapter=new RapportAdapter(rapports,me,this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(RapportViewActivity.this);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(RapportViewActivity.this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(adapter);

        RapportsBYAsso rapportsBYAsso=new RapportsBYAsso(association, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getBoolean("success")){
                        int nbr=jsonObject.getInt("nbRapp");
                        for (int i=0;i<nbr;i++){
                            ActivityRapport rapport=ActivityRapport.fromJsonView(jsonObject.getJSONObject(""+i));
                            rapports.add(rapport);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(rapportsBYAsso);
    }
}
