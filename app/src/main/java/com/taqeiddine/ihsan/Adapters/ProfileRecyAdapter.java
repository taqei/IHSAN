package com.taqeiddine.ihsan.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.UtilisateurMyActivity;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;

import java.util.ArrayList;

public class ProfileRecyAdapter extends RecyclerView.Adapter<ProfileRecyAdapter.MyViewHolder>{
    private ArrayList<Profile> profiles;
    private Utilisateur me;
    private View thisview;

    public ProfileRecyAdapter(ArrayList<Profile> profiles, Utilisateur me) {
        this.profiles = profiles;
        this.me = me;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_profile_view_circle, parent, false);
        thisview=itemView;
        return new ProfileRecyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Profile profile=profiles.get(position);
        if (profile.getPhotodeprofil() != null) {
            Glide.with(thisview.getContext()).load(Help.getMedia()+profile.getPhotodeprofil().getUrl()).into(holder.theImage);
        }
        if (profile instanceof Utilisateur){
            Utilisateur utilisateur=(Utilisateur) profile;
            holder.nomprenom.setText(utilisateur.getNom()+" "+utilisateur.getPrenom());
        }
        if (profile instanceof Association){
            Association association=(Association) profile;
            holder.nomprenom.setText(association.getNomassociation());
        }
        Log.i("ovitale",holder.nomprenom.getText().toString());
        holder.theImage.setClickable(true);
        holder.nomprenom.setClickable(true);

        View.OnClickListener onclick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile instanceof Utilisateur){
                    if(me.getIdprofile().compareTo(profile.getIdprofile())==0){
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), UtilisateurMyActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        thisview.getContext().startActivity(intent);
                    }else{
                        Intent intent = new Intent(thisview.getContext().getApplicationContext(), UtilisateurActivity.class);
                        intent.putExtra("myidutilisateur",me.getIdprofile());
                        intent.putExtra("otheridprofil", profile.getIdprofile());
                        thisview.getContext().startActivity(intent);
                    }
                }else{
                    if(profile instanceof Association){
                        if(me instanceof ChefAssociation){
                            ChefAssociation chefAssociation=(ChefAssociation) me;
                            if(chefAssociation.getAssociation().getIdprofile().compareTo(profile.getIdprofile())==0){
                                Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationAdminActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidchef", me.getIdprofile());
                                thisview.getContext().startActivity(intent);
                            }else{
                                Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationActivity.class);
                                intent.putExtra("myidassociation",profile.getIdprofile());
                                intent.putExtra("myidutilisateur", me.getIdprofile());
                                thisview.getContext().startActivity(intent);
                            }
                        }else{
                            Intent intent = new Intent(thisview.getContext().getApplicationContext(), AssociationActivity.class);
                            intent.putExtra("myidassociation",profile.getIdprofile());
                            intent.putExtra("myidutilisateur", me.getIdprofile());
                            thisview.getContext().startActivity(intent);
                        }
                    }
                }
            }
        };
        holder.theImage.setOnClickListener(onclick);
        holder.nomprenom.setOnClickListener(onclick);


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public de.hdodenhof.circleimageview.CircleImageView theImage;
        public TextView nomprenom;
        public MyViewHolder(View itemView) {
            super(itemView);
            theImage=(de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.circle_profile_view_image);
            nomprenom=(TextView) itemView.findViewById(R.id.circle_profile_view_nom);
        }
    }
}
