package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.R;



public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.MyViewHolder> {
    private AlbumPhoto albumPhoto;
    private Context mContext;

    public InstaAdapter(AlbumPhoto albumPhoto,Context context) {
        this.albumPhoto = albumPhoto;
        this.mContext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_photo_insta, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo photo=albumPhoto.getListPhoto().get(position);
        Glide.with(mContext).load(Help.getMedia()+photo.getUrl()).into(holder.theImage);
        holder.place.setText(" "+(position+1)+" / "+getItemCount()+" ");
    }

    @Override
    public int getItemCount() {
        return albumPhoto.getListPhoto().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView theImage;
        public TextView place;
        public MyViewHolder(View itemView) {
            super(itemView);
            theImage=(ImageView) itemView.findViewById(R.id.insta_photo);
            place=(TextView) itemView.findViewById(R.id.insta_nbr);
        }
    }
}