package com.taqeiddine.ihsan.Activities.PublicationActivities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taqeiddine.ihsan.Adapters.BesoinViewAdapter;
import com.taqeiddine.ihsan.Adapters.DonAdapter;
import com.taqeiddine.ihsan.Adapters.InstaAdapter;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.StableArrayAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Don;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Projet;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.Model.Report;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Intervenire.InsertIntervention;
import com.taqeiddine.ihsan.VOLLEY.Publications.FinaliserPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.DisFollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.FollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.IsFollowerPub;
import com.taqeiddine.ihsan.VOLLEY.Publications.InsertReport;
import com.taqeiddine.ihsan.VOLLEY.Publications.getAPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjetDetailsActivity extends AppCompatActivity {
    private TextView publicateur, date, heure, titre, descri, numphone, adresse,destination,datedebut,datefin,hopen,hclose;
    private ImageView photoPublicateur,plus;
    private RecyclerView besoins,images;
    SupportMapFragment mapFragment;
    Toolbar myToolbar ;
    private RelativeLayout intervenir;


    private final ArrayList<Besoin> listbesoins = new ArrayList<>();
    private final BesoinViewAdapter besoinViewAdapter = new BesoinViewAdapter(listbesoins);

    final Publication publication = new Publication();
    Utilisateur me;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_pub_projet_details);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_map);

        date = (TextView) findViewById(R.id.detailspn_date);
        heure = (TextView) findViewById(R.id.detailspn_heure);
        titre = (TextView) findViewById(R.id.detailspn_Titre);
        descri = (TextView) findViewById(R.id.detailspn_desctip);
        numphone = (TextView) findViewById(R.id.detailspn_numphone);


        besoins = (RecyclerView) findViewById(R.id.detailspn_recyclerbesoins);
        plus=(ImageView) findViewById(R.id.detailspn_plus);
        images=(RecyclerView) findViewById(R.id.detailspn_recy_photos);
        adresse=(TextView) findViewById(R.id.detailspn_lieucollecte);
        destination=(TextView) findViewById(R.id.detailspn_destination) ;
        datedebut=(TextView) findViewById(R.id.detail_projet_ddebut);
        datefin=(TextView) findViewById(R.id.detail_projet_dfin);
        hopen=(TextView) findViewById(R.id.detail_projet_hopen);
        hclose=(TextView) findViewById(R.id.detail_projet_hclose);

        intervenir=(RelativeLayout) findViewById(R.id.detailspn_intervenir);
        intervenir.setClickable(false);
        requestQueue = Volley.newRequestQueue(this);


        if(getIntent().getIntExtra("chef",3)==1){
            ChefAssociation chefAssociation;
            chefAssociation=new ChefAssociation(getIntent().getExtras().getString("myidutilisateur"));
            chefAssociation.setAssociation(new Association(getIntent().getExtras().getString("myidassociation")));
            me=chefAssociation;
        }else{
            me=new Utilisateur(getIntent().getExtras().getString("myidutilisateur"));
        }
        publication.setIdpub(getIntent().getExtras().getString("idpublication"));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        besoins.setLayoutManager(mLayoutManager);
        besoins.setItemAnimator(new DefaultItemAnimator());
        besoins.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        besoins.setAdapter(besoinViewAdapter);

        Actualiser();


    }

    private void Actualiser() {
        getAPublication getAPublication = new getAPublication(publication, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.i("OpooOoOo",s);
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                        final Publication publication=Publication.fromJSONLarge(jsonObject);
                        Projet projet = (Projet)  publication;
                        Log.i("takii", projet.getIdpub());

                        date.setText(Help.getLaDate().format(projet.getDate()));
                        heure.setText(Help.getLHeure().format(projet.getDate()));
                        titre.setText(projet.getTitrepub());
                        descri.setText(projet.getDescriptionpub());
                        numphone.setText(projet.getNumphone());

                        datedebut.setText(Help.getLaDate().format(projet.getDatedebutcollecte()));
                        datefin.setText(Help.getLaDate().format(projet.getDatefincollecte()));
                        hopen.setText(Help.getLHeure().format(projet.getHeureopen()));
                        hclose.setText(Help.getLHeure().format(projet.getHeureclose()));
                        if(publication.isPubfinalisee())
                            intervenir.setVisibility(View.INVISIBLE);
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(ProjetDetailsActivity.this, Locale.getDefault());
                        try {
                            if(publication.getAdressepublication()!=null){
                                addresses = geocoder.getFromLocation(publication.getAdressepublication().latitude, publication.getAdressepublication().longitude, 1);
                                adresse.setText(addresses.get(0).getAddressLine(0));
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(publication.getAdressepublication())
                                                .title("Lieu de collecte")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(publication.getAdressepublication(), 10));
                                    }
                                });
                            }

                        } catch (IOException e) {
                            e.printStackTrace();

                            adresse.setText("Adresse non disponible");
                        }
                        try {
                            if(projet.getDestination()!=null){
                                addresses = geocoder.getFromLocation(projet.getDestination().latitude, projet.getDestination().longitude, 1);
                                destination.setText(addresses.get(0).getAddressLine(0));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            destination.setText("Adresse non disponible");
                        }

                        listbesoins.removeAll(listbesoins);
                        listbesoins.addAll(projet.getListeDesBesoins());
                        besoinViewAdapter.notifyDataSetChanged();
                        //adresse.s

                        // photos
                        if(publication.getAlbumPhoto().getListPhoto().size()==0)
                            images.setVisibility(View.GONE);
                        else{
                            InstaAdapter instaAdapter=new InstaAdapter(publication.getAlbumPhoto(),ProjetDetailsActivity.this);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ProjetDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            images.setLayoutManager(mLayoutManager1);
                            images.setItemAnimator(new DefaultItemAnimator());
                            images.addItemDecoration(new MyDividerItemDecoration(ProjetDetailsActivity.this, LinearLayoutManager.HORIZONTAL, 16));
                            images.setAdapter(instaAdapter);
                        }


                        Profile profile=projet.getAssociation();


                        if(profile instanceof Association){

                            myToolbar.setTitle(((Association)profile).getNomassociation());
                        }


                        initPlus(me,projet);

                        intervenir.setClickable(true);
                        intervenir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog interv=new Dialog(ProjetDetailsActivity.this);
                                final ArrayList<Don> dons=Don.donsfrombesoins(publication.getListeDesBesoins());
                                DonAdapter donAdapter=new DonAdapter(dons);
                                interv.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                interv.setContentView(R.layout.dialogue_intervenir);
                                interv.show();
                                RecyclerView donsrecycle=(RecyclerView) interv.findViewById(R.id.dialogue_intervenir_recycler);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                                donsrecycle.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 16));
                                donsrecycle.setLayoutManager(mLayoutManager1);
                                donsrecycle.setItemAnimator(new DefaultItemAnimator());
                                donsrecycle.setAdapter(donAdapter);
                                TextView validerinterventio=(TextView) interv.findViewById(R.id.dialogue_intervenir_valider);
                                validerinterventio.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        interv.setCancelable(false);

                                        Intervention intervention=new Intervention(publication,me,dons);
                                        InsertIntervention insertIntervention=new InsertIntervention(intervention, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                interv.dismiss();
                                                Actualiser();
                                                Toast.makeText(ProjetDetailsActivity.this,"Votre intervention a ete effectuée",Toast.LENGTH_LONG).show();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Log.i("ISIL", volleyError.toString());
                                            }
                                        });
                                        requestQueue.add(insertIntervention);

                                    }
                                });



                            }
                        });
                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(getAPublication);

    }

    public void initPlus(final Utilisateur utilisateur, final Publication pub){
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(ProjetDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_plus);
                dialog.show();

                final ListView listView=(ListView)dialog.findViewById(R.id.dialogue_plus_listview);
                final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.dialogue_plus_progress);


                String[] values ;
                Profile profile=((Projet) pub).getAssociation();


                if ((profile instanceof Utilisateur && utilisateur.getIdprofile().compareTo(profile.getIdprofile())==0) || (profile instanceof Association &&(utilisateur instanceof ChefAssociation) && ((ChefAssociation) utilisateur).getAssociation().getIdprofile().compareTo(profile.getIdprofile())==0) ){
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    // my publication
                    if (pub.isPubfinalisee())
                        values= new String[] { "Voir les interventions","Supprimer la publication" };
                    else
                        values= new String[] { "Finaliser La publication","Voir les interventions","Supprimer la publication" };

                    final ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < values.length; ++i) {
                        list.add(values[i]);
                    }

                    final StableArrayAdapter adapter = new StableArrayAdapter(ProjetDetailsActivity.this,
                            android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,
                                                int position, long id) {
                            final String item = (String) parent.getItemAtPosition(position);
                            view.animate().setDuration(2000).alpha(0)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {

                                            view.setAlpha(1);
                                            if(item.compareTo("Finaliser La publication")==0){
                                                FinaliserPublication finaliserPublication=new FinaliserPublication(pub, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        try {
                                                            JSONObject jsonObject=new JSONObject(s);
                                                            if (jsonObject.getBoolean("success")){
                                                                pub.setPubfinalisee(true);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
                                                dialog.dismiss();
                                                requestQueue.add(finaliserPublication);
                                            }
                                            if(item.compareTo("Voir les interventions")==0){
                                                Intent intent=new Intent(ProjetDetailsActivity.this, InterventionsActivity.class);
                                                intent.putExtra("idpublication",pub.getIdpub());
                                                startActivity(intent);
                                            }
                                            if(item.compareTo("Supprimer la publication")==0){

                                            }

                                        }
                                    });
                        }

                    });


                }else{
                    listView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    IsFollowerPub followerPub=new IsFollowerPub(utilisateur, pub, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {

                                String etat="Suivre la publication";
                                JSONObject jsonObject=new JSONObject(s);

                                if(jsonObject.getBoolean("success")){
                                    if(jsonObject.getBoolean("isfollow")){
                                        etat="Ne plus suivre la publication";
                                    }else{
                                        etat="Suivre la publication";
                                    }
                                }
                                String[] val= new String[] { etat,"Signaler la publication" };
                                final ArrayList<String> list = new ArrayList<String>();
                                for (int i = 0; i < val.length; ++i) {
                                    list.add(val[i]);
                                }
                                final StableArrayAdapter adapter = new StableArrayAdapter(ProjetDetailsActivity.this,
                                        android.R.layout.simple_list_item_1, list);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, final View view,
                                                            int position, long id) {
                                        final String item = (String) parent.getItemAtPosition(position);
                                        view.animate().setDuration(2000).alpha(0)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        view.setAlpha(1);
                                                        if (item.compareTo("Suivre la publication")==0){
                                                            FollowPublication followPublication=new FollowPublication(utilisateur, pub, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    Log.i("hp",s);
                                                                    list.set(0,"Ne plus suivre la publication");
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                            });

                                                            requestQueue.add(followPublication);
                                                            dialog.dismiss();
                                                        }
                                                        if(item.compareTo("Ne plus suivre la publication")==0){
                                                            DisFollowPublication disFollowPublication=new DisFollowPublication(utilisateur, pub, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    Log.i("hp",s);
                                                                    list.set(0,"Suivre la publication");
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                            });
                                                            requestQueue.add(disFollowPublication);
                                                            dialog.dismiss();
                                                        }
                                                        if (item.compareTo("Signaler la publication")==0){
                                                            final Dialog signaler=new Dialog(ProjetDetailsActivity.this);
                                                            signaler.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                            signaler.setContentView(R.layout.dialogue_signaler_publication);
                                                            signaler.show();
                                                            final AutoCompleteTextView textView=(AutoCompleteTextView) signaler.findViewById(R.id.dialogue_signaler_text);
                                                            Button valider=(Button) signaler.findViewById(R.id.dialogue_signaler_valider);
                                                            valider.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    signaler.setCancelable(false);
                                                                    Report report=new Report(utilisateur,pub,textView.getText().toString());
                                                                    InsertReport insertReport=new InsertReport(report, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            Toast.makeText(ProjetDetailsActivity.this,"Votre requette a été bien effectué",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });
                                                                    requestQueue.add(insertReport);
                                                                    signaler.dismiss();
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                    }

                                });
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },null);
                    requestQueue.add(followerPub);
                }

                /*
                 */
            }
        });
    }
}
