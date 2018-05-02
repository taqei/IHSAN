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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taqeiddine.ihsan.Adapters.DonAdapter;
import com.taqeiddine.ihsan.Adapters.InstaAdapter;
import com.taqeiddine.ihsan.Adapters.StableArrayAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Don;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Report;
import com.taqeiddine.ihsan.VOLLEY.Intervenire.InsertIntervention;
import com.taqeiddine.ihsan.VOLLEY.Publications.FinaliserPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.DisFollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.FollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.IsFollowerPub;
import com.taqeiddine.ihsan.VOLLEY.Publications.InsertReport;
import com.taqeiddine.ihsan.VOLLEY.Publications.getAPublication;

import com.android.volley.RequestQueue;

import com.taqeiddine.ihsan.Adapters.BesoinViewAdapter;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SignalPnDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //private MapView mapView;
    private TextView publicateur, date, heure, titre, descri, type, numphone, adresse;
    private ImageView photoPublicateur,plus;
    private RecyclerView besoins,images;
    SupportMapFragment mapFragment;


    private RelativeLayout intervenir;


    private final ArrayList<Besoin> listbesoins = new ArrayList<>();
    private final BesoinViewAdapter besoinViewAdapter = new BesoinViewAdapter(listbesoins);

    final Publication publication = new Publication();
    Utilisateur me;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_spn_details);
        //mapView = (MapView) findViewById(R.id.detailspn_localisation);
        publicateur = (TextView) findViewById(R.id.detailspn_nom_prenom);
        date = (TextView) findViewById(R.id.detailspn_date);
        heure = (TextView) findViewById(R.id.detailspn_heure);
        titre = (TextView) findViewById(R.id.detailspn_Titre);
        descri = (TextView) findViewById(R.id.detailspn_desctip);
        type = (TextView) findViewById(R.id.detailspn_type);
        numphone = (TextView) findViewById(R.id.detailspn_numphone);
        adresse = (TextView) findViewById(R.id.detailspn_adresse);
        photoPublicateur = (ImageView) findViewById(R.id.detailspn_photo_profil);
        besoins = (RecyclerView) findViewById(R.id.detailspn_recyclerbesoins);
        plus=(ImageView) findViewById(R.id.detailspn_plus);
        images=(RecyclerView) findViewById(R.id.detailspn_recy_photos);
        adresse=(TextView) findViewById(R.id.detailspn_localisation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_map);

        intervenir=(RelativeLayout) findViewById(R.id.detailspn_intervenir);
        intervenir.setClickable(false);
        requestQueue = Volley.newRequestQueue(this);

        Intent intent=getIntent();
        Bundle extraData=intent.getExtras();
        String a=extraData.getString("myidutilisateur");
        if(getIntent().getIntExtra("chef",3)==1){
            ChefAssociation chefAssociation;
            chefAssociation=new ChefAssociation(getIntent().getExtras().getString("myidutilisateur"));
            chefAssociation.setAssociation(new Association(getIntent().getExtras().getString("myidassociation")));
            me=chefAssociation;
        }else{
            me=new Utilisateur(getIntent().getExtras().getString("myidutilisateur"));
        }
        publication.setIdpub(getIntent().getExtras().getString("idpublication"));


        // Adapter for liste des besoins
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        besoins.setLayoutManager(mLayoutManager);
        besoins.setItemAnimator(new DefaultItemAnimator());
        besoins.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        besoins.setAdapter(besoinViewAdapter);




        Actualiser();





    }

    public void Actualiser(){
        getAPublication getAPublication = new getAPublication(publication, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.i("OpooOoOo",s);
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                        final Publication publication=Publication.fromJSONLarge(jsonObject);
                        SignalPN signalPN = (SignalPN)  publication;
                        Log.i("takii", signalPN.getIdpub());

                        date.setText(Help.getLaDate().format(signalPN.getDate()));
                        heure.setText(Help.getLHeure().format(signalPN.getDate()));
                        titre.setText(signalPN.getTitrepub());
                        descri.setText(signalPN.getDescriptionpub());
                        type.setText(signalPN.getTypePN());
                        numphone.setText(signalPN.getNumphone());

                        mapFragment.getMapAsync(SignalPnDetailsActivity.this);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(SignalPnDetailsActivity.this, Locale.getDefault());
                        try {
                            if(publication.getAdressepublication()!=null){
                                addresses = geocoder.getFromLocation(publication.getAdressepublication().latitude, publication.getAdressepublication().longitude, 1);
                                adresse.setText(addresses.get(0).getAddressLine(0));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            adresse.setText("Erreuuure");
                        }

                        listbesoins.removeAll(listbesoins);
                        listbesoins.addAll(signalPN.getListeDesBesoins());
                        besoinViewAdapter.notifyDataSetChanged();
                        //adresse.s

                        // photos
                        if(publication.getAlbumPhoto().getListPhoto().size()==0)
                            images.setVisibility(View.GONE);
                        else{
                            InstaAdapter instaAdapter=new InstaAdapter(publication.getAlbumPhoto(),SignalPnDetailsActivity.this);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(SignalPnDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            images.setLayoutManager(mLayoutManager1);
                            images.setItemAnimator(new DefaultItemAnimator());
                            images.addItemDecoration(new MyDividerItemDecoration(SignalPnDetailsActivity.this, LinearLayoutManager.HORIZONTAL, 16));
                            images.setAdapter(instaAdapter);
                        }


                        // info profile
                        Profile profile=signalPN.getProfile();
                        if(profile.getPhotodeprofil()!=null){
                            Glide.with(SignalPnDetailsActivity.this).load(Help.getURL() + signalPN.getProfile().getPhotodeprofil().getUrl()).into(photoPublicateur);
                        }
                        if(profile instanceof Utilisateur){
                            publicateur.setText(((Utilisateur) profile).getNom() + "  " + ((Utilisateur) profile).getPrenom());
                        }
                        if(profile instanceof Association){
                            publicateur.setText(((Association)profile).getNomassociation());
                        }

                        initPlus(me,signalPN);

                        intervenir.setClickable(true);
                        intervenir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog interv=new Dialog(SignalPnDetailsActivity.this);
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
                                        Log.i("ifruit raisin",dons.toString()+"   taki  "+dons.get(0).getQte());
                                        Intervention intervention=new Intervention(publication,me,dons);
                                        Log.i("ifruiit",intervention.donsToJson().toString());
                                        Log.i("ifruiit",intervention.getPublication().getIdpub());
                                        Log.i("ifruiit",intervention.getUtilisateur().getIdprofile());
                                        InsertIntervention insertIntervention=new InsertIntervention(intervention, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                interv.dismiss();
                                                Actualiser();
                                                Toast.makeText(SignalPnDetailsActivity.this,"Votre intervention a ete effectuée",Toast.LENGTH_LONG).show();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
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
                final Dialog dialog=new Dialog(SignalPnDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_plus);
                dialog.show();

                final ListView listView=(ListView)dialog.findViewById(R.id.dialogue_plus_listview);
                final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.dialogue_plus_progress);


                String[] values ;
                Profile profile=((SignalPN) pub).getProfile();


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

                    final StableArrayAdapter adapter = new StableArrayAdapter(SignalPnDetailsActivity.this,
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
                                                Intent intent=new Intent(SignalPnDetailsActivity.this, InterventionsActivity.class);
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
                                final StableArrayAdapter adapter = new StableArrayAdapter(SignalPnDetailsActivity.this,
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
                                                            final Dialog signaler=new Dialog(SignalPnDetailsActivity.this);
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
                                                                            Toast.makeText(SignalPnDetailsActivity.this,"Votre requette a été bien effectué",Toast.LENGTH_LONG).show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
