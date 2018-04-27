package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Icone;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.GetIcones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IconesAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;

    List<Icone> icones;


    public IconesAdapter(Context mContext,List<Icone> icones) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.icones=icones;

    }


    @Override
    public int getCount() {
        return icones.size();
    }

    @Override
    public Icone getItem(int position) {
        return icones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.row_icone_single,null);
        }
        final ImageView imageView=(ImageView) convertView.findViewById(R.id.icone_single_image);
        final TextView textView=(TextView) convertView.findViewById(R.id.icone_single_text);
        Glide.with(mContext).load(Help.getMedia()+icones.get(position).getUrl()).into(imageView);
        textView.setText(icones.get(position).getIdicone()+"");
        return convertView;
    }
}
