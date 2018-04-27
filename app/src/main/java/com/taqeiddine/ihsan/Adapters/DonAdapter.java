package com.taqeiddine.ihsan.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taqeiddine.ihsan.Model.Don;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;
import java.util.List;

public class DonAdapter extends RecyclerView.Adapter<DonAdapter.MyViewHolder> {

    ArrayList<Don> dons;
    ArrayList<Don> retour;


    DonAdapter.MyViewHolder myholder;

    public DonAdapter(ArrayList<Don> dons) {
        this.dons = dons;
        retour =new ArrayList<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_don, parent, false);
        myholder = new DonAdapter.MyViewHolder(itemView);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Don don =dons.get(position);
        holder.nomarticle.setText(don.getBesoin().getArticle().getNomArticle());
        holder.qtetotal.setText(don.getBesoin().getQte()+" "+don.getBesoin().getArticle().getUniteArticle());
        holder.qtearticle.setText(don.getBesoin().getQterecu()+" /");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.donqte.setVisibility(View.VISIBLE);
                TextWatcher textWatcher=new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        don.setQte(Integer.parseInt(s.toString()));
                    }
                };
                holder.donqte.addTextChangedListener(textWatcher);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dons.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomarticle,qtearticle,qtetotal;
        public AutoCompleteTextView donqte;
        public ImageView icone;
        public RelativeLayout relativeLayout;
        public MyViewHolder(final View view) {
        super(view);
        nomarticle = (TextView) view.findViewById(R.id.intervenir_row_nomBesoin);
        qtearticle = (TextView) view.findViewById(R.id.intervenir_row_qte1Besoin);
        qtetotal = (TextView) view.findViewById(R.id.intervenir_row_qtetotalBesoin);
        donqte=(AutoCompleteTextView) view.findViewById(R.id.intervenir_row_qteIntervention);
        icone=(ImageView) view.findViewById(R.id.intervenir_row_imageBesoin);
        relativeLayout=(RelativeLayout) view.findViewById(R.id.intervenir_row_relative_layout);

        }
}
}
