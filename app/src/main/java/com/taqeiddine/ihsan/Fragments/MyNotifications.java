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

import com.taqeiddine.ihsan.Adapters.BesoinAddAdapter;
import com.taqeiddine.ihsan.Adapters.ClickListener;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.NotficationsAdapter;
import com.taqeiddine.ihsan.Adapters.RecyclerTouchListener;
import com.taqeiddine.ihsan.DataBaseSQLite.DatabaseHelper;
import com.taqeiddine.ihsan.Model.Notification;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;


public class MyNotifications extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Notification> notifications=new ArrayList<>();
    com.taqeiddine.ihsan.Adapters.NotficationsAdapter adapter=new NotficationsAdapter(notifications);
    DatabaseHelper databaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper=new DatabaseHelper(getContext());

        recyclerView=(RecyclerView) getView().findViewById(R.id.notifs_recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getNotfications(-1);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(notifications.get(position).getIntent());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void getNotfications(int id){
        ArrayList<Notification> notificationArrayList=databaseHelper.notificationsTen(getActivity(),id);
        ArrayList<Notification> notifications=new ArrayList<>();
        for(int i=0;i<notificationArrayList.size();i++){
            Notification notification=notificationArrayList.get(i);
            if(notification.getCode()!=7)
                notifications.add(notification);
        }
        notifications.addAll(notifications);
        adapter.notifyDataSetChanged();
    }
}
