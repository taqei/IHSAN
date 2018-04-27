package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Article;
import com.taqeiddine.ihsan.Model.DemandeAdr;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Association.AccepterAdr;
import com.taqeiddine.ihsan.VOLLEY.Association.Demande;

import java.util.ArrayList;

public class DemandeAdrAdapter extends ArrayAdapter<DemandeAdr> {
    private final Context context;
    private final ArrayList<DemandeAdr> objects;
    private final ChefAssociation chefAssociation;
    private final RequestQueue requestQueue;

    public DemandeAdrAdapter(Context context,ArrayList<DemandeAdr> objects,ChefAssociation chefAssociation,RequestQueue requestQueue) {
        super(context, -1, objects);
        this.context=context;
        this.objects=objects;
        this.chefAssociation=chefAssociation;
        this.requestQueue=requestQueue;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.row_demande_adr,parent,false);
        de.hdodenhof.circleimageview.CircleImageView circleImageView=(de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.row_demande_adr_photo);
        TextView nomprenom=(TextView) rowView.findViewById(R.id.row_demande_adr_nomprenom),date=(TextView) rowView.findViewById(R.id.row_demande_adr_date);
        final ImageView accepter=(ImageView) rowView.findViewById(R.id.row_demande_adr_accepte),refuser=(ImageView) rowView.findViewById(R.id.row_demande_adr_refuse);
        final DemandeAdr demandeAdr=objects.get(position);
        nomprenom.setText(demandeAdr.getDemandeur().getNom()+"   "+demandeAdr.getDemandeur().getPrenom());
        if (demandeAdr.getDemandeur().getPhotodeprofil() != null) {
            Glide.with(rowView.getContext()).load(Help.getMedia()+demandeAdr.getDemandeur().getPhotodeprofil().getUrl()).into(circleImageView);
        }
        date.setText(Help.getLaDate().format(demandeAdr.getDate()));
        nomprenom.setClickable(true);
        nomprenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        accepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccepterAdr accepterAdr=new AccepterAdr(demandeAdr, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("ovitale",s);
                        accepter.setClickable(false);
                        refuser.setClickable(false);
                    }
                });
                requestQueue.add(accepterAdr);
            }
        });
        return rowView;
    }
}
