package com.taqeiddine.ihsan.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.taqeiddine.ihsan.Model.Article;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.AjouterArticle;
import com.taqeiddine.ihsan.VOLLEY.GetArticles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Taqei on 05/04/2018.
 */

public class ArticleSearchAdapter extends BaseAdapter {
    /*private final Context context;
    private final ArrayList<Article> objects;

    public ArticleSearchAdapter(Context context,ArrayList<Article> objects) {
        super(context, -1, objects);
        this.context=context;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.row_article_search,parent,false);
        TextView nomarticle=(TextView)rowView.findViewById(R.id.row_article_search_nomarticle);
        ImageView icone=(ImageView)rowView.findViewById(R.id.row_article_search_iconarticle);
        nomarticle.setText(objects.get(position).getNomArticle());
        return rowView;
    }*/
    Context mContext;
    LayoutInflater inflater;
    RequestQueue requestQueue;
    private List<Article> articles = null;
    private ArrayList<Article> arraylist;


    public ArticleSearchAdapter(Context context,RequestQueue requestQueue) {
        mContext = context;
        this.articles = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Article>();
        this.arraylist.addAll(articles);
        this.requestQueue=requestQueue;
    }
    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Article getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_article_search, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.dialogue_searche_article);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(articles.get(position).getNomArticle());
        return view;
    }

    // Filter Class
    public void filter(String charText) {

        articles.clear();
        notifyDataSetChanged();
        final GetArticles getArticles=new GetArticles(charText, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                        try {
                            arraylist.clear();
                            final JSONObject jsonObject=new JSONObject(s);
                            int nbr=jsonObject.getInt("nbrArticles");
                            for(int i=0;i<nbr;i++){
                                Article a=Article.fromJson(jsonObject.getJSONObject(""+i));
                                arraylist.add(a);
                            }
                            articles.addAll(arraylist);
                            notifyDataSetChanged();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(getArticles);
    }


}
