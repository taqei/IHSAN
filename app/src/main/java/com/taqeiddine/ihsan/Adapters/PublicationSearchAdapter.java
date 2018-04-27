package com.taqeiddine.ihsan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Publications.SearchPublications;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PublicationSearchAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    RequestQueue requestQueue;
    private List<Publication> PublicationsList = null;
    private ArrayList<Publication> arraylist;
    private Profile profile;
    public PublicationSearchAdapter(Context context,Profile profile,RequestQueue requestQueue) {
        mContext = context;
        this.PublicationsList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Publication>();
        this.arraylist.addAll(PublicationsList);
        this.profile=profile;
        this.requestQueue=requestQueue;
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return PublicationsList.size();
    }

    @Override
    public Publication getItem(int position) {
        return PublicationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_search_publication, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.dialogue_searche_pubname);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(PublicationsList.get(position).getTitrepub());
        return view;
    }

    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        PublicationsList.clear();
        notifyDataSetChanged();
        final SearchPublications searchPublications=new SearchPublications(charText, profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    arraylist.clear();
                    JSONObject jsonObject=new JSONObject(s);
                    int nbr=jsonObject.getInt("nbr");
                    for (int i=0;i<nbr;i++){
                        Publication publication=Publication.fromJSONgetTitre(jsonObject.getJSONObject(""+i));
                        arraylist.add(publication);
                    }
                    PublicationsList.addAll(arraylist);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(searchPublications);
    }
}
