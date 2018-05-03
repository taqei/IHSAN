package com.taqeiddine.ihsan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurMyActivity;
import com.taqeiddine.ihsan.Adapters.ProfileSearchAdapter;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Firebase.SharedPrefManager;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;



public class MySearch extends Fragment {
    SearchView searchView;
    ListView listView;
    TextView textView;
    RequestQueue requestQueue;
    Utilisateur me;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(SharedPrefManager.getInstance(getContext()).isChef()==1){
            me=new ChefAssociation(new Utilisateur(),new Association(SharedPrefManager.getInstance(getContext()).getIDAssociation()));

        }else{
            me=new Utilisateur();
        }
        me.setIdprofile(SharedPrefManager.getInstance(getContext()).getID());

        searchView=(SearchView) getView().findViewById(R.id.mysearch_searchview);
        listView=(ListView) getView().findViewById(R.id.mysearch_listview);
        textView=(TextView) getView().findViewById(R.id.mysearch_text);
        requestQueue= Volley.newRequestQueue(getContext());

        final ProfileSearchAdapter adapter=new ProfileSearchAdapter(getContext(),requestQueue,listView,textView);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapter.filter(text);

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profile profile=adapter.getItem(position);
                if (profile instanceof Utilisateur){
                    if(me.getIdprofile().compareTo(profile.getIdprofile())==0){
                        // my profile
                        Intent intent = new Intent(getContext().getApplicationContext(), UtilisateurMyActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        getContext().startActivity(intent);
                    }else{
                        //other's profile
                        Intent intent = new Intent(getContext().getApplicationContext(), UtilisateurActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        intent.putExtra("otheridprofil", profile.getIdprofile());
                        getContext().startActivity(intent);
                    }
                }else{
                    if(profile instanceof Association){
                        if(me instanceof ChefAssociation){
                            ChefAssociation chefAssociation=(ChefAssociation) me;
                            if(chefAssociation.getAssociation().getIdprofile().compareTo(profile.getIdprofile())==0){
                                // my association
                                Intent intent = new Intent(getContext().getApplicationContext(), AssociationAdminActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidchef", me.getIdprofile());
                                getContext().startActivity(intent);
                            }else{
                                // other's association
                                Intent intent = new Intent(getContext().getApplicationContext(), AssociationActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidutilisateur", me.getIdprofile());
                                getContext().startActivity(intent);
                            }
                        }else{
                            // other's association
                            Intent intent = new Intent(getContext().getApplicationContext(), AssociationActivity.class);
                            intent.putExtra("myidassociation",profile.getIdprofile());
                            intent.putExtra("myidutilisateur", me.getIdprofile());
                            getContext().startActivity(intent);
                        }
                    }
                }
            }
        });

    }
}
