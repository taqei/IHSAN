package com.taqeiddine.ihsan.Activities.PublicationActivities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Adapters.PubsInterventionsAdapter;
import com.taqeiddine.ihsan.Adapters.StableArrayAdapter;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Intervenire.Approuver;
import com.taqeiddine.ihsan.VOLLEY.Intervenire.SupprimerIntervention;
import com.taqeiddine.ihsan.VOLLEY.Publications.InterventionsGet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InterventionsActivity extends AppCompatActivity {
    Publication publication;
    ExpandableListView listView;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interventions);
        listView=(ExpandableListView) findViewById(R.id.interventions_listview);
        requestQueue= Volley.newRequestQueue(this);
        publication=new Publication(getIntent().getStringExtra("idpublication"));
        InterventionsGet interventionsGet=new InterventionsGet(publication, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject= null;
                Log.i("lenovo",s);
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")){
                        publication=Publication.fromJSONinterventions(jsonObject);
                        final PubsInterventionsAdapter adapter=new PubsInterventionsAdapter(publication,InterventionsActivity.this);
                        listView.setAdapter(adapter);
                        /*listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            @Override
                            public void onGroupExpand(int groupPosition) {
                                Toast.makeText(InterventionsActivity.this,"heuuh",Toast.LENGTH_LONG).show();
                            }
                        });*/
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final Dialog dialog=new Dialog(InterventionsActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialogue_plus);
                                dialog.show();

                                final ListView listeView=(ListView)dialog.findViewById(R.id.dialogue_plus_listview);
                                final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.dialogue_plus_progress);
                                progressBar.setVisibility(View.GONE);
                                listeView.setVisibility(View.VISIBLE);

                                final Intervention intervention=adapter.getGroup(position);
                                String[] values ;
                                if (intervention.isInterventionvalidee())
                                    values= new String[] { "Supprimer cette intervention" };
                                else
                                    values= new String[] { "Approuver l'intevention","Supprimer cette intervention" };

                                final ArrayList<String> list = new ArrayList<String>();
                                for (int i = 0; i < values.length; ++i) {
                                    list.add(values[i]);
                                }
                                final StableArrayAdapter adaptere = new StableArrayAdapter(InterventionsActivity.this,
                                        android.R.layout.simple_list_item_1, list);

                                listeView.setAdapter(adaptere);
                                listeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, final View view,
                                                            final int position, long id) {
                                        final String item = (String) parent.getItemAtPosition(position);
                                        view.animate().setDuration(500).alpha(0)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        view.setAlpha(1);
                                                        if (item.compareTo("Supprimer cette intervention") == 0) {
                                                            SupprimerIntervention supprimerIntervention=new SupprimerIntervention(intervention, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    try{
                                                                        JSONObject jsonObject1=new JSONObject(s);
                                                                        if(jsonObject1.getBoolean("success")){

                                                                            adapter.getInterventions().remove(position);
                                                                            adapter.notifyDataSetChanged();

                                                                            Toast.makeText(InterventionsActivity.this,"L'intervention a été supprimée",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }catch (JSONException e){
                                                                        e.printStackTrace();
                                                                    }


                                                                }
                                                            });
                                                            dialog.dismiss();
                                                            requestQueue.add(supprimerIntervention);
                                                        }
                                                        if (item.compareTo("Approuver l'intevention") == 0) {
                                                            Approuver approuver=new Approuver(intervention, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    try{
                                                                        JSONObject jsonObject1=new JSONObject(s);
                                                                        if(jsonObject1.getBoolean("success")){
                                                                            intervention.setInterventionvalidee(true);
                                                                            adapter.notifyDataSetChanged();

                                                                            Toast.makeText(InterventionsActivity.this,"L'intervention a été approuvée",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }catch (JSONException e){
                                                                        e.printStackTrace();
                                                                    }


                                                                }
                                                            });
                                                            dialog.dismiss();
                                                            requestQueue.add(approuver);
                                                        }

                                                    }
                                                });
                                    }});

                                return false;
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(interventionsGet);
    }
}
