package com.taqeiddine.ihsan.Adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.R;

import java.util.List;

/**
 * Created by Taqei on 13/03/2018.
 */

public class BesoinAddAdapter extends RecyclerView.Adapter<BesoinAddAdapter.MyViewHolder> {

    private List<Besoin> theBesoins;
    View thisview;

    public BesoinAddAdapter(List<Besoin> theBesoins) {
        this.theBesoins = theBesoins;
        this.theBesoins.add(0,new Besoin());
    }

    @Override

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_besoin_add, parent, false);
        thisview=itemView;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Besoin besoin=theBesoins.get(position);
        if (position==0){
            holder.nomarticle.setText("Ajouter Besoin");
            holder.qtearticle.setVisibility(View.GONE);
           // holder.idarticle=null;
        }else{
            holder.nomarticle.setText(besoin.getArticle().getNomArticle());
            holder.qtearticle.setText(besoin.getQte()+" "+besoin.getArticle().getUniteArticle());
            //holder.idarticle=besoin.getArticle().getIdarticle();
            if (besoin.getArticle().getIcone() != null) {
                Glide.with(thisview.getContext()).load(Help.getMedia()+besoin.getArticle().getIcone().getUrl()).into(holder.icone);
            }
        }
    }

    @Override
    public int getItemCount() {
        return theBesoins.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nomarticle,qtearticle;
        //public String idarticle;
        public ImageView icone;

        public MyViewHolder(View view) {
            super(view);
            nomarticle = (TextView) view.findViewById(R.id.addBesoinnomBesoin);
            qtearticle = (TextView) view.findViewById(R.id.addBesoinqteBesoin);
            icone=(ImageView) view.findViewById(R.id.addBesoinimageBesoin);
        }
    }

}
