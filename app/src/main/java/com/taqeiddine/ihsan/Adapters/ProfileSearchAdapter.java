package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Article;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Profile.SearchByName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileSearchAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    RequestQueue requestQueue;
    ListView listView;
    TextView textView;
    private List<Profile> profiles = null;
    private ArrayList<Profile> arraylist;

    public ProfileSearchAdapter(Context mContext, RequestQueue requestQueue,ListView listView,TextView textView) {
        this.mContext = mContext;
        this.requestQueue = requestQueue;
        inflater = LayoutInflater.from(mContext);
        this.profiles=new ArrayList<>();
        this.arraylist = new ArrayList<Profile>();
        this.arraylist.addAll(profiles);
        this.requestQueue=requestQueue;
        this.listView=listView;
        this.textView=textView;

    }
    public class ViewHolder {
        TextView name;
        de.hdodenhof.circleimageview.CircleImageView photoprofil;
        RatingBar ratingBar;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Profile getItem(int position) {
        return profiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        Profile profile=profiles.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_profil_search, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.profile_searche_name);
            holder.photoprofil=(de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.profil_search_photoprofil);
            holder.ratingBar=(RatingBar) view.findViewById(R.id.profile_searche_ratingbar);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        if(profile.getPhotodeprofil()!=null){
            Glide.with(mContext).load(Help.getMedia()+profile.getPhotodeprofil().getUrl()).into(holder.photoprofil);
        }
        if(profile instanceof Utilisateur){
            Utilisateur utilisateur=(Utilisateur) profile;
            holder.name.setText(utilisateur.getNom()+"  "+utilisateur.getPrenom());
            if(utilisateur.getConfiance()<0)
                holder.ratingBar.setRating(0);
            else{
                if (utilisateur.getConfiance()>100)
                    holder.ratingBar.setRating(5);
                else
                    holder.ratingBar.setRating(utilisateur.getConfiance()/20);
            }
        }
        if(profile instanceof Association){
            holder.name.setText(((Association) profile).getNomassociation());
        }
        return view;
    }

    public void filter(String charText) {

        profiles.clear();
        notifyDataSetChanged();
        final SearchByName getArticles=new SearchByName(charText, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    arraylist.clear();
                    final JSONObject jsonObject=new JSONObject(s);
                    int nbr=jsonObject.getInt("nbr");
                    if(nbr==0){
                        textView.setText("Aucun résultat");
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }else{
                        textView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(int i=0;i<nbr;i++){
                            Profile profile=Profile.fromJsonINFOPROFILELITE(jsonObject.getJSONObject(""+i));

                            arraylist.add(profile);
                        }
                    }

                    profiles.addAll(arraylist);
                    notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(getArticles);
    }


}
