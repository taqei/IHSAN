package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalPnDetailsActivity;
import com.taqeiddine.ihsan.Model.ActivityRapport;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.R;

import java.util.List;

public class RapportAdapter extends RecyclerView.Adapter<RapportAdapter.MyViewHolder> {

    private List<ActivityRapport> rapports;
    private Utilisateur me;
    View thisview;
    Context context;

    public RapportAdapter(List<ActivityRapport> rapports, Utilisateur me,Context context) {
        this.rapports = rapports;
        this.me = me;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rapport_view, parent, false);
        thisview = itemView;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ActivityRapport rapport=rapports.get(position);
        holder.titre.setText(rapport.getTitre());
        holder.descri.setText(rapport.getText());
        holder.pubtitre.setText(rapport.getPublication().getTitrepub());

        holder.publication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rapport.getPublication() instanceof SignalPN){
                    Intent intent = new Intent(context, SignalPnDetailsActivity.class);
                    intent.putExtra("myidutilisateur",me.getIdprofile());
                    intent.putExtra("idpublication", rapport.getPublication().getIdpub());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return rapports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titre,descri,pubtitre;
        LinearLayout publication;

        public MyViewHolder(final View itemView) {
            super(itemView);
            titre=(TextView) itemView.findViewById(R.id.row_rapport_titre);
            descri=(TextView) itemView.findViewById(R.id.row_rapport_desc);
            pubtitre=(TextView) itemView.findViewById(R.id.row_rapport_titrepub);
            publication=(LinearLayout) itemView.findViewById(R.id.row_rapport_pub);

        }
    }
}
