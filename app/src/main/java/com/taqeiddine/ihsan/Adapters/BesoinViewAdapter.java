package com.taqeiddine.ihsan.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taqei on 18/03/2018.
 */

public class BesoinViewAdapter extends RecyclerView.Adapter<BesoinViewAdapter.MyViewHolder> {

    private ArrayList<Besoin> theBesoins;
    private View thisview;

    public BesoinViewAdapter( ArrayList<Besoin> theBesoins) {
        this.theBesoins = theBesoins;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_besoin_view, parent, false);
        thisview=itemView;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Besoin besoin=theBesoins.get(position);
        holder.nomarticle.setText(besoin.getArticle().getNomArticle());
        holder.qtearticle.setText(besoin.getQte()+" "+besoin.getArticle().getUniteArticle());
        holder.qte1article.setText(besoin.getQterecu()+" /");
        holder.idarticle.setText(besoin.getArticle().getIdarticle());
        if (besoin.getArticle().getIcone() != null) {
            Glide.with(thisview.getContext()).load(Help.getMedia()+besoin.getArticle().getIcone().getUrl()).into(holder.icone);
        }
    }

    @Override
    public int getItemCount() {
        return theBesoins.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nomarticle,qtearticle,qte1article,idarticle;
        public ImageView icone;

        //manque l'icone
        public MyViewHolder(View view) {
            super(view);
            nomarticle = (TextView) view.findViewById(R.id.viewBesoinnomBesoin);
            qtearticle = (TextView) view.findViewById(R.id.viewBesoinqteBesoin);
            qte1article = (TextView) view.findViewById(R.id.viewBesoinqte1Besoin);
            idarticle = (TextView) view.findViewById(R.id.viewBesoinIDARTICLE);
            icone=(ImageView) view.findViewById(R.id.viewBesoinimageBesoin);

        }
    }
}
