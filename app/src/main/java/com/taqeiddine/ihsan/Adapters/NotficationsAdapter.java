package com.taqeiddine.ihsan.Adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Notification;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;

public class NotficationsAdapter extends RecyclerView.Adapter<NotficationsAdapter.MyViewHolder> {

    ArrayList<Notification> notifications=new ArrayList<>();

    public NotficationsAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notification, parent, false);

        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification notification=notifications.get(position);
        holder.titre.setText(notification.getTitle());
        holder.text.setText(notification.getMessage());
        switch (notification.getCode()){
            case 0:{holder.frameLayout.setBackgroundResource(R.color.a0);holder.titre.setTextColor(Color.parseColor("#FF1744"));break;}
            case 1:{holder.frameLayout.setBackgroundResource(R.color.a1);holder.titre.setTextColor(Color.parseColor("#D500F9"));break;}
            case 2:{holder.frameLayout.setBackgroundResource(R.color.a2);holder.titre.setTextColor(Color.parseColor("#2979FF"));break;}
            case 3:{holder.frameLayout.setBackgroundResource(R.color.a3);holder.titre.setTextColor(Color.parseColor("#3D5AFE"));break;}
            case 4:{holder.frameLayout.setBackgroundResource(R.color.a4);holder.titre.setTextColor(Color.parseColor("#651FFF"));break;}
            case 5:{holder.frameLayout.setBackgroundResource(R.color.a5);holder.titre.setTextColor(Color.parseColor("#1DE9B6"));break;}
            case 6:{holder.frameLayout.setBackgroundResource(R.color.a6);holder.titre.setTextColor(Color.parseColor("#00E676"));break;}
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView titre,text;
        public FrameLayout frameLayout;


        public MyViewHolder(View view) {
            super(view);
            titre = (TextView) view.findViewById(R.id.notif_titre);
            text = (TextView) view.findViewById(R.id.notif_msg);
            frameLayout=(FrameLayout) view.findViewById(R.id.notif_framelayout);

        }
    }
}
