package com.taqeiddine.ihsan.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taqei on 17/03/2018.
 */

public class PhotoAddAdapter extends RecyclerView.Adapter<PhotoAddAdapter.MyViewHolder> {
    private AlbumPhoto albumPhoto;

    public PhotoAddAdapter(AlbumPhoto albumPhoto){
        this.albumPhoto=albumPhoto;
        albumPhoto.getListPhoto().add(0,new Photo());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_photo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position==0){
            holder.theImage.setVisibility(View.GONE);
        }
        else{
            Photo photo=albumPhoto.getListPhoto().get(position);
            holder.linearLayout.setVisibility(View.GONE);
            holder.theImage.setImageBitmap(photo.getPhoto());
        }
    }

    @Override
    public int getItemCount() {
        return albumPhoto.getListPhoto().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
            public ImageView theImage;
            public LinearLayout linearLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                theImage=(ImageView) itemView.findViewById(R.id.add_photo_imageview);
                linearLayout=(LinearLayout) itemView.findViewById(R.id.add_photo_linearlayout);
            }
        }
}
