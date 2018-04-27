package com.taqeiddine.ihsan.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.Messages.MessageActivity;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Projet;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Message.MessageByProfil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class MessageSAdapter extends RecyclerView.Adapter<MessageSAdapter.MyViewHolder> {
    Profile me;
    RequestQueue requestQueue;
    LinkedList<Profile> profiles=new LinkedList<>();
    Activity activity;
    View thisview;

    public MessageSAdapter(Profile me, RequestQueue requestQueue,Activity activity) {
        this.me = me;
        this.activity=activity;
        this.requestQueue = requestQueue;
        MessageByProfil messageByProfil=new MessageByProfil(me, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getBoolean("success")){
                        int nbr=jsonObject.getInt("nbr");
                        LinkedList<Profile> list=new LinkedList<>();
                        for (int i=0;i<nbr;i++){
                            Profile profile=Profile.fromJsonINFOPROFILELITE(jsonObject.getJSONObject(""+i));
                            list.addFirst(profile);
                        }
                        profiles.clear();
                        profiles.addAll(list);
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(messageByProfil);

    }

    @Override
    public MessageSAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_messages, parent, false);
        thisview=itemView;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageSAdapter.MyViewHolder holder, int position) {
        final Profile profile=profiles.get(position);
        if (profile instanceof Utilisateur){
            holder.nomprenom.setText(((Utilisateur) profile).getNom()+"  "+((Utilisateur) profile).getPrenom());
        }
        if (profile instanceof Association){
            holder.nomprenom.setText(((Association) profile).getNomassociation());
        }

        if (profile.getPhotodeprofil() != null) {
            Glide.with(thisview.getContext()).load(Help.getMedia()+profile.getPhotodeprofil().getUrl()).into(holder.photoprofil);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.animate().setDuration(1000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(activity, MessageActivity.class);
                                intent.putExtra("myidprofil",me.getIdprofile());
                                intent.putExtra("otheridprofil",profile.getIdprofile());
                                activity.startActivity(intent);
                                v.setAlpha(1);
                                }});

            }
        });

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomprenom;
        public ImageView photoprofil;
        public RelativeLayout relativeLayout;

        public MyViewHolder(final View view) {
            super(view);
            nomprenom=(TextView) view.findViewById(R.id.messages_nomprenom);
            photoprofil=(ImageView) view.findViewById(R.id.messages_photoprofil);
            relativeLayout=(RelativeLayout) view.findViewById(R.id.messages_relativelayout);
        }
    }
}
