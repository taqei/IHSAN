package com.taqeiddine.ihsan.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Adapters.MessageSAdapter;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.R;



public class MyMessages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    Profile me;
    RequestQueue requestQueue;
    RecyclerView messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_messages, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        me=new Profile(getActivity().getIntent().getStringExtra("myidutilisateur"));
        requestQueue= Volley.newRequestQueue(getActivity());

        MessageSAdapter adapter=new MessageSAdapter(me,requestQueue,getActivity());
        messages=(RecyclerView) getView().findViewById(R.id.activiti_mesgs_recy);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        messages.setLayoutManager(mLayoutManager);
        messages.setItemAnimator(new DefaultItemAnimator());
        messages.setAdapter(adapter);
    }
}
