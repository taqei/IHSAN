package com.taqeiddine.ihsan.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Message;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Taqei on 02/04/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private LinkedList<Message> messages;
    private Profile mep;

    public void setMe(Profile mep) {
        this.mep = mep;
    }

    public MessageAdapter(LinkedList<Message> messages, Profile mep) {
        this.messages = messages;
        this.mep = mep;
    }

    public Message getMax()
    {
        if (messages.size()>0)
            return this.messages.get(0);
        return null;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){

            case 0: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_message_me,parent, false);

                return (new MessageMe(itemView));

            }
            case 1: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_message_other,parent, false);

                return (new MessageOther(itemView));
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Message message=messages.get(position);

        switch (holder.getItemViewType()){
            case 0:{
                final MessageMe messageMe=(MessageMe) holder;

                messageMe.message.setText(message.getMessage());
                messageMe.thelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageMe.datetime.setText(Help.getLaDate().format(message.getDate())+"  "+Help.getLHeure().format(message.getDate()));
                        messageMe.datetime.setVisibility(View.VISIBLE);
                        messageMe.thelayout.setClickable(false);

                    }
                });
                break;
            }
            case 1:{
                final MessageOther messageOther=(MessageOther) holder;
                messageOther.message.setText(message.getMessage());
                messageOther.thelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageOther.datetime.setText(Help.getLaDate().format(message.getDate())+"  "+Help.getLHeure().format(message.getDate()));
                        messageOther.datetime.setVisibility(View.VISIBLE);
                        messageOther.thelayout.setClickable(false);
                    }
                });
                break;
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
       if(messages.get(position).getExp().getIdprofile().compareTo(mep.getIdprofile())==0){
           return 0;
       }
       else
           return 1;
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageMe extends RecyclerView.ViewHolder {
        public RelativeLayout thelayout;
        public TextView message, datetime;


        public MessageMe(View view) {
            super(view);
            thelayout = (RelativeLayout) view.findViewById(R.id.messageme_layout);
            datetime = (TextView) view.findViewById(R.id.messageme_datetime);
            message = (TextView) view.findViewById(R.id.messageme_text);

        }
    }
    public class  MessageOther extends RecyclerView.ViewHolder{
        public RelativeLayout thelayout;
        public TextView message,datetime;
        public MessageOther(View view) {
            super(view);
            thelayout=(RelativeLayout) view.findViewById(R.id.messageother_layout);
            datetime=(TextView) view.findViewById(R.id.messageother_datetime);
            message=(TextView) view.findViewById(R.id.messageother_text);

        }
    }
}
