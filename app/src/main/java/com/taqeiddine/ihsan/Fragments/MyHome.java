package com.taqeiddine.ihsan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalerPNActiviy;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.PublicationSmallAdapter;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.IsChefAssociation;
import com.taqeiddine.ihsan.VOLLEY.Publications.getSmallPublication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyHome extends Fragment {
    RequestQueue requestQueue;
    final ArrayList<Publication> publications=new ArrayList<>();
    final ArrayList<String> currentpubs=new ArrayList<>();

    private boolean allPubs=false;
    final PublicationSmallAdapter publicationSmallAdapter=new PublicationSmallAdapter(publications,null,null);

    ProgressBar progressBar;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;

    Utilisateur me;
    private OnFragmentInteractionListener mListener;

    public MyHome() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_home, container, false);
    }


    public void onStart(){

        super.onStart();
        me=new Utilisateur();
        me.setIdprofile(getActivity().getIntent().getStringExtra("myidutilisateur"));
        Button mybtn=(Button) getView().findViewById(R.id.buttonsignalerprenec);
        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intet=new Intent(getActivity(), SignalerPNActiviy.class);
                intet.putExtra("myidutilisateur",me.getIdprofile());
                intet.putExtra("chef",0);
                startActivity(intet);
            }
        });

        nestedScrollView=(NestedScrollView) getView().findViewById(R.id.home_scroll);
        progressBar=(ProgressBar) getView().findViewById(R.id.home_progress);
        recyclerView=(RecyclerView) getView().findViewById(R.id.recy_pub_small);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(publicationSmallAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        requestQueue=Volley.newRequestQueue(this.getActivity());
        publicationSmallAdapter.setRequestQueue(requestQueue);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // We take the last son in the scrollview
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int distanceToEnd = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                if (distanceToEnd == 0 && !allPubs) {
                    progressBar.setVisibility(View.VISIBLE);
                    GETTHEPUBLICATIONS(me);
                }
            }
        });
        final IsChefAssociation isChefAssociation=new IsChefAssociation(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getInt("chef")==1){
                        Association association=Association.fromJSONLite(jsonObject.getJSONObject("infoasso"));
                        ChefAssociation chefAssociation=new ChefAssociation(me,association);
                        me=chefAssociation;
                    }
                    publicationSmallAdapter.setMe(me);
                    GETTHEPUBLICATIONS(me);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(isChefAssociation);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.i("Taqi","a probleme hereeeeeeeeeeee");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     */

    private void GETTHEPUBLICATIONS(Utilisateur my){
        getSmallPublication getSmallPublication=new getSmallPublication(currentpubs,null,my,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try{

                    JSONObject jsonObject=new JSONObject(s);

                    int nbr=jsonObject.getInt("nbrPub");
                    Log.i("imaad",""+nbr);
                    Log.i("imaad",s);


                    ArrayList<Publication> arrayList=new ArrayList<>();
                    for (int i=0;i<nbr;i++){
                        JSONObject jsonObject1=jsonObject.getJSONObject(""+i);
                        Publication publication= Publication.fromJsonSmall(jsonObject1);
                        if (publication!=null){
                                arrayList.add(publication);
                                currentpubs.add(publication.getIdpub());

                        }
                    }

                    publications.addAll(arrayList);
                    progressBar.setVisibility(View.GONE);
                    publicationSmallAdapter.notifyDataSetChanged();
                    if(publications.size()==0){
                        ((TextView) getView().findViewById(R.id.nothing)).setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(getSmallPublication);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
