package com.taqeiddine.ihsan.Adapters;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurMyActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.InterventionsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.ProjetDetailsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalPnDetailsActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurActivity;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Projet;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.Model.Report;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Publications.FinaliserPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.InsertReport;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.DisFollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.FollowPublication;
import com.taqeiddine.ihsan.VOLLEY.Publications.Follow.IsFollowerPub;
import com.taqeiddine.ihsan.VOLLEY.Publications.SupprimerPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Taqei on 15/03/2018.
 */

public class PublicationSmallAdapter extends RecyclerView.Adapter<PublicationSmallAdapter.PublicationHolder> {

    private List<Publication> listPub;
    private Utilisateur me;
    View thisview;
    RequestQueue requestQueue;

    public PublicationSmallAdapter(List<Publication> listPub,Utilisateur utilisateur,RequestQueue requestQueue) {
        this.listPub = listPub;
        this.me=utilisateur;
        this.requestQueue=requestQueue;
    }

    @Override
    public PublicationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){

            case 0: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_pub_prenec,parent, false);
                thisview = itemView;
                return (new SPNHolder(itemView));

            }
            case 1: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_pub_projet,parent, false);
                thisview = itemView;
                return (new ProjetHolder(itemView));
            }
        }
        return null;
    }

    public void setMe(Utilisateur me) {
        this.me = me;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void onBindViewHolder(PublicationHolder viewholder, int position) {
        // contenu commun pour SPN et Projet
        final Publication publication = listPub.get(position);
        viewholder.date.setText(Help.getLaDate().format(publication.getDate()));
        viewholder.heure.setText(Help.getLHeure().format(publication.getDate()));
        viewholder.titre.setText(publication.getTitrepub());
        viewholder.descri.setText(publication.getDescriptionpub());
        BesoinViewAdapter besoinViewAdapter = new BesoinViewAdapter(publication.getListeDesBesoins());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(thisview.getContext(), LinearLayoutManager.HORIZONTAL, false);
        viewholder.besoins.setLayoutManager(mLayoutManager);
        viewholder.besoins.setItemAnimator(new DefaultItemAnimator());
        viewholder.besoins.addItemDecoration(new MyDividerItemDecoration(thisview.getContext(), LinearLayoutManager.HORIZONTAL, 16));
        viewholder.besoins.setAdapter(besoinViewAdapter);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(thisview.getContext(), Locale.getDefault());
        try {
            if(publication.getAdressepublication()!=null){
                addresses = geocoder.getFromLocation(publication.getAdressepublication().latitude, publication.getAdressepublication().longitude, 1);
                viewholder.adressepub.setText(addresses.get(0).getAddressLine(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        switch (viewholder.getItemViewType()){
            case 0:{
                final SPNHolder holder=(SPNHolder) viewholder;
                SignalPN signalPn=(SignalPN) publication;

                Profile u=signalPn.getProfile();
                if (u.getPhotodeprofil() != null) {
                    Glide.with(thisview.getContext()).load(Help.getMedia()+signalPn.getProfile().getPhotodeprofil().getUrl()).into(holder.photoprofil);
                }
                if (u instanceof Utilisateur == true) {
                    Utilisateur ut = (Utilisateur) u;
                    holder.nomprenom.setText(ut.getNom() + " " + ut.getPrenom());
                }
                if (u instanceof Association==true){
                    Association a=(Association) u;
                    holder.nomprenom.setText(a.getNomassociation());

                }


                holder.afficherdétail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), SignalPnDetailsActivity.class);
                        intent.putExtra("idpublication", publication.getIdpub());
                        intent.putExtra("myidutilisateur", me.getIdprofile());
                        if(me instanceof ChefAssociation){
                            intent.putExtra("chef", 1);
                            intent.putExtra("myidassociation",((ChefAssociation)me).getAssociation().getIdprofile());
                        }else
                            intent.putExtra("chef", 0);

                        thisview.getContext().startActivity(intent);

                    }
                });

                break;
            }
            case 1:{
                final ProjetHolder holder=(ProjetHolder) viewholder;

                Projet projet=(Projet) publication;



                holder.datedebut.setText(Help.getLaDate().format(projet.getDatedebutcollecte()));
                holder.datefin.setText(Help.getLaDate().format(projet.getDatefincollecte()));
                holder.hopen.setText(Help.getLHeure().format(projet.getHeureopen()));
                holder. hclose.setText(Help.getLHeure().format(projet.getHeureclose()));

                try {
                    if(projet.getDestination()!=null){
                        addresses = geocoder.getFromLocation(projet.getDestination().latitude, projet.getDestination().longitude, 1);
                        ((ProjetHolder) viewholder).destii.setText(addresses.get(0).getAddressLine(0));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    ((ProjetHolder) viewholder).destii.setText("Erreuuure");
                }


                Association a=projet.getAssociation();
                if (a.getPhotodeprofil() != null) {
                    Glide.with(thisview.getContext()).load(Help.getMedia()+a.getPhotodeprofil().getUrl()).into(holder.photoprofil);
                }
                holder.nomprenom.setText(a.getNomassociation());

                holder.afficherdétail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), ProjetDetailsActivity.class);
                        intent.putExtra("idpublication", publication.getIdpub());
                        intent.putExtra("myidutilisateur", me.getIdprofile());
                        if(me instanceof ChefAssociation){
                            intent.putExtra("chef", 1);
                            intent.putExtra("myidassociation",((ChefAssociation)me).getAssociation().getIdprofile());
                        }else
                            intent.putExtra("chef", 0);

                        thisview.getContext().startActivity(intent);

                    }
                });

                break;
            }
        }
        View.OnClickListener on=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile;
                if (publication instanceof SignalPN){
                    profile=((SignalPN) publication).getProfile();
                }else{
                    profile=((Projet) publication).getAssociation();
                }
                if (profile instanceof Utilisateur){
                    if(me.getIdprofile().compareTo(profile.getIdprofile())==0){
                        // my profile
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), UtilisateurMyActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        thisview.getContext().startActivity(intent);
                    }else{
                        //other's profile
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), UtilisateurActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        intent.putExtra("otheridprofil", profile.getIdprofile());
                        thisview.getContext().startActivity(intent);
                    }
                }else{
                    if(profile instanceof Association){
                        if(me instanceof ChefAssociation){
                            ChefAssociation chefAssociation=(ChefAssociation) me;
                            if(chefAssociation.getAssociation().getIdprofile().compareTo(profile.getIdprofile())==0){
                                // my association
                                Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationAdminActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidchef", me.getIdprofile());
                                thisview.getContext().startActivity(intent);
                            }else{
                                // other's association
                                Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidutilisateur", me.getIdprofile());
                                thisview.getContext().startActivity(intent);
                            }
                        }else{
                            // other's association
                            Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationActivity.class);
                            intent.putExtra("myidassociation",profile.getIdprofile());
                            intent.putExtra("myidutilisateur", me.getIdprofile());
                            thisview.getContext().startActivity(intent);
                        }
                    }
                }
            }

        };
        viewholder.nomprenom.setOnClickListener(on);

        View.OnClickListener plusClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // final Dialog dialog=new Dialog(thisview.getContext().getApplicationContext());
                final Dialog dialog=new Dialog(thisview.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_plus);
                dialog.show();

                final ListView listView=(ListView)dialog.findViewById(R.id.dialogue_plus_listview);
                final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.dialogue_plus_progress);


                String[] values ;
                Profile profile;
                if (publication instanceof SignalPN){
                    profile=((SignalPN) publication).getProfile();
                }else{
                    profile=((Projet) publication).getAssociation();

                }

                if ((profile instanceof Utilisateur && me.getIdprofile().compareTo(profile.getIdprofile())==0) || (profile instanceof Association &&(me instanceof ChefAssociation) && ((ChefAssociation) me).getAssociation().getIdprofile().compareTo(profile.getIdprofile())==0) ){
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    // my publication
                    if (publication.isPubfinalisee())
                        values= new String[] { "Voir les interventions","Supprimer la publication" };
                    else
                        values= new String[] { "Finaliser La publication","Voir les interventions","Supprimer la publication" };

                    final ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < values.length; ++i) {
                        list.add(values[i]);
                    }

                    final StableArrayAdapter adapter = new StableArrayAdapter(thisview.getContext(),
                            android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,
                                                final int position, long id) {
                            final String item = (String) parent.getItemAtPosition(position);
                            view.animate().setDuration(2000).alpha(0)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {

                                            view.setAlpha(1);
                                            if(item.compareTo("Finaliser La publication")==0){
                                                FinaliserPublication finaliserPublication=new FinaliserPublication(publication, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        try {
                                                            JSONObject jsonObject=new JSONObject(s);
                                                            if (jsonObject.getBoolean("success")){
                                                                publication.setPubfinalisee(true);
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
                                                Intent intent=new Intent(thisview.getContext().getApplicationContext(), InterventionsActivity.class);
                                                intent.putExtra("idpublication",publication.getIdpub());
                                                thisview.getContext().startActivity(intent);
                                            }
                                            if(item.compareTo("Supprimer la publication")==0){
                                                SupprimerPublication supprimerPublication=new SupprimerPublication(publication, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        try {
                                                            JSONObject jsonObject=new JSONObject(s);
                                                            if (jsonObject.getBoolean("success")){
                                                                listPub.remove(position);
                                                                notifyDataSetChanged();
                                                                Toast.makeText(thisview.getContext().getApplicationContext(),"Votre Publication a été supprimée",Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                dialog.dismiss();
                                                requestQueue.add(supprimerPublication);
                                            }

                                        }
                                    });
                        }

                    });


                }else{
                    listView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    IsFollowerPub followerPub=new IsFollowerPub(me, publication, new Response.Listener<String>() {
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
                                final StableArrayAdapter adapter = new StableArrayAdapter(thisview.getContext(),
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
                                                            FollowPublication followPublication=new FollowPublication(me, publication, new Response.Listener<String>() {
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
                                                            DisFollowPublication disFollowPublication=new DisFollowPublication(me, publication, new Response.Listener<String>() {
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
                                                            final Dialog signaler=new Dialog(thisview.getContext());
                                                            signaler.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                            signaler.setContentView(R.layout.dialogue_signaler_publication);
                                                            signaler.show();
                                                            final AutoCompleteTextView textView=(AutoCompleteTextView) signaler.findViewById(R.id.dialogue_signaler_text);
                                                            Button valider=(Button) signaler.findViewById(R.id.dialogue_signaler_valider);
                                                            valider.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    signaler.setCancelable(false);
                                                                    Report report=new Report(me,publication,textView.getText().toString());
                                                                    InsertReport insertReport=new InsertReport(report, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            Toast.makeText(thisview.getContext(),"Votre requette a été bien effectué",Toast.LENGTH_LONG).show();
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
        };
        viewholder.plus.setOnClickListener(plusClick);
    }


    public int getItemViewType(int position) {
        if(listPub.get(position) instanceof  SignalPN)
            return 0;

        if(listPub.get(position) instanceof  Projet)
            return 1;
        return 2;
    }

    @Override
    public int getItemCount() {
        return listPub.size();
    }


    public class PublicationHolder extends  RecyclerView.ViewHolder{
        ImageView photoprofil;
        TextView nomprenom, date, heure, titre, descri,adressepub;
        RecyclerView besoins;
        ImageView plus;

        public PublicationHolder(View itemView) {
            super(itemView);
            photoprofil = (ImageView) itemView.findViewById(R.id.pub__photo_profil);
            nomprenom = (TextView) itemView.findViewById(R.id.pub__nom_prenom);
            date = (TextView) itemView.findViewById(R.id.pub__date);
            heure = (TextView) itemView.findViewById(R.id.pub__heure);
            titre = (TextView) itemView.findViewById(R.id.pub__titre);
            descri = (TextView) itemView.findViewById(R.id.pub__descri);
            besoins = (RecyclerView) itemView.findViewById(R.id.pub_recy_besoins);
            plus=(ImageView) itemView.findViewById(R.id.pub__plus);
            adressepub=(TextView) itemView.findViewById(R.id.detail_adressePub);

        }
    }
    public class SPNHolder extends PublicationHolder{
        TextView type;
        Button afficherdétail;


        public SPNHolder(final View itemView) {
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.pub_prenec_type);
            afficherdétail = (Button) itemView.findViewById(R.id.pub_prenec_afficher_detail);




        }
    }
    public class ProjetHolder extends PublicationHolder {

        TextView datedebut,datefin,hopen,hclose,destii;
        Button afficherdétail;


        public ProjetHolder(final View itemView) {
            super(itemView);
            afficherdétail = (Button) itemView.findViewById(R.id.pub_projet_afficher_detail);
            datedebut=(TextView) itemView.findViewById(R.id.detail_projet_ddebut);
            datefin=(TextView) itemView.findViewById(R.id.detail_projet_dfin);
            hopen=(TextView) itemView.findViewById(R.id.detail_projet_hopen);
            hclose=(TextView) itemView.findViewById(R.id.detail_projet_hclose);
            destii=(TextView) itemView.findViewById(R.id.detail_adresseDesti);
        }
    }



}
