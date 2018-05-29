package com.taqeiddine.ihsan.Activities.PublicationActivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.taqeiddine.ihsan.Adapters.ArticleSearchAdapter;
import com.taqeiddine.ihsan.Adapters.BesoinAddAdapter;
import com.taqeiddine.ihsan.Adapters.ClickListener;
import com.taqeiddine.ihsan.Adapters.IconesAdapter;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.PhotoAddAdapter;
import com.taqeiddine.ihsan.Adapters.RecyclerTouchListener;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.MainActivity;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Article;
import com.taqeiddine.ihsan.Model.Besoin;
import com.taqeiddine.ihsan.Model.Icone;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.SignalPN;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.AjouterArticle;
import com.taqeiddine.ihsan.VOLLEY.GetArticles;
import com.taqeiddine.ihsan.VOLLEY.GetIcones;
import com.taqeiddine.ihsan.VOLLEY.Publications.PublierRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SignalerPNActiviy extends AppCompatActivity {

    private List<Besoin> besoins =new ArrayList<>();

    Utilisateur user;
    Association association;
    ChefAssociation chefAssociation;

    RequestQueue requestQueue;
    private RecyclerView besoinsrecycler,photoRecycler;
    private BesoinAddAdapter besoinAddAdapter;
    private PhotoAddAdapter photoAddAdapter;
    private AlbumPhoto albumPhoto;

    EditText nomArticle,QteBesoin;
    TextView idarticle,unitearticlee,urliconee;
    EditText searchArticle;
    ProgressBar progressBarArticle;
    Button go;
    Button AjouterLarticle;
    MultiAutoCompleteTextView titre,description,type,numphone,location;
    SignalPN spn;
    Place place;

    LatLng adresseLatLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler_pn);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Signaler une personne nécéssiteuse.");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue= Volley.newRequestQueue(this);

        final String idUtilisateur=getIntent().getStringExtra("myidutilisateur");
        Log.i("ifri",idUtilisateur);
        user=new Utilisateur();
        user.setIdprofile(idUtilisateur);
        if (getIntent().getIntExtra("chef",0)==1){
            Log.i("ifri", "" +getIntent().getIntExtra("chef", 0));
            association=new Association(getIntent().getStringExtra("myidassociation"));
            Log.i("ifri",association.getIdprofile());
            chefAssociation=new ChefAssociation(user,association);
        }


        titre=(MultiAutoCompleteTextView) findViewById(R.id.publierTitre);
        description=(MultiAutoCompleteTextView) findViewById(R.id.publierDescr);
        type =(MultiAutoCompleteTextView) findViewById(R.id.publierTypePN);
        location =(MultiAutoCompleteTextView) findViewById(R.id.publierLocation);

        numphone=(MultiAutoCompleteTextView) findViewById(R.id.publierPhone);
        besoinsrecycler = (RecyclerView) findViewById(R.id.recyvler_add_besoins);
        photoRecycler=(RecyclerView) findViewById(R.id.recyvler_add_photo);





        besoinAddAdapter=new BesoinAddAdapter(besoins);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        besoinsrecycler.setLayoutManager(mLayoutManager);
        besoinsrecycler.setItemAnimator(new DefaultItemAnimator());
        besoinsrecycler.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 16));
        besoinsrecycler.setAdapter(besoinAddAdapter);

        albumPhoto=new AlbumPhoto();
        photoAddAdapter=new PhotoAddAdapter(albumPhoto);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        photoRecycler.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 16));
        photoRecycler.setLayoutManager(mLayoutManager1);
        photoRecycler.setItemAnimator(new DefaultItemAnimator());
        photoRecycler.setAdapter(photoAddAdapter);


        besoinsrecycler.addOnItemTouchListener(new RecyclerTouchListener(SignalerPNActiviy.this, besoinsrecycler, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if(position==0){
                    final Dialog box=new Dialog(SignalerPNActiviy.this);
                    box.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    box.setContentView(R.layout.dialogue_add_besoin);

                    box.show();
                    nomArticle=(EditText)   box.findViewById(R.id.dialogue_nomarticle);
                    idarticle=(TextView) box.findViewById(R.id.dialogue_idarticle);
                    urliconee=(TextView) box.findViewById(R.id.dialogue_urlicone);
                    unitearticlee=(TextView) box.findViewById(R.id.dialogue_unitearticle);


                    nomArticle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog searche=new Dialog(SignalerPNActiviy.this);
                            searche.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            searche.setContentView(R.layout.dialogue_searchbesoin);
                            searche.show();

                            final ArticleSearchAdapter adapter=new ArticleSearchAdapter(SignalerPNActiviy.this,requestQueue);
                            final android.widget.SearchView searchView=(android.widget.SearchView) searche.findViewById(R.id.dialogue_search_article);
                            ListView listView=(ListView) searche.findViewById(R.id.dialogue_search_listview);
                            Button addarticle =(Button) searche.findViewById(R.id.dialogue_search_addarticle);

                            listView.setAdapter(adapter);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    String text = newText;
                                    adapter.filter(text);
                                    return false;
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Article article=(Article)adapter.getItem(position);
                                    nomArticle.setText(article.getNomArticle());
                                    idarticle.setText(article.getIdarticle());
                                    urliconee.setText(article.getIcone().getUrl());
                                    unitearticlee.setText(article.getUniteArticle());
                                    searche.dismiss();
                                }
                            });

                            addarticle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog addarticle=new Dialog(SignalerPNActiviy.this);
                                    addarticle.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    addarticle.setContentView(R.layout.dialogue_add_article);

                                    addarticle.show();

                                    final GridView gridView=(GridView) addarticle.findViewById(R.id.dialogue_add_article_gridview);

                                    GetIcones getIcones=new GetIcones(new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {

                                            try {
                                                ArrayList<Icone> icones=new ArrayList<>();
                                                JSONObject jsonObject=new JSONObject(s);
                                                int nbr=jsonObject.getInt("nbr");
                                                for (int i=0;i<nbr;i++){
                                                    Icone icone=Icone.fromJSON(jsonObject.getJSONObject(""+i));
                                                    icones.add(icone);
                                                }
                                                IconesAdapter iconesAdapter=new IconesAdapter(SignalerPNActiviy.this,icones);
                                                gridView.setAdapter(iconesAdapter);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    requestQueue.add(getIcones);

                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
                                            final Icone item = (Icone) parent.getItemAtPosition(position);
                                            view.animate().setDuration(500).alpha(0).withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    gridView.setVisibility(View.GONE);
                                                    ImageView imageView=(ImageView)addarticle.findViewById(R.id.dialogue_add_article_iconarticle);
                                                    TextView textView=(TextView) addarticle.findViewById(R.id.dialogue_add_article_iconarticle_id);
                                                    imageView.setVisibility(View.VISIBLE);
                                                    Glide.with(SignalerPNActiviy.this).load(Help.getMedia()+item.getUrl()).into(imageView);
                                                    textView.setText(item.getIdicone());


                                                }
                                            });
                                        }
                                    });



                                    Button button=(Button) addarticle.findViewById(R.id.dialogue_add_article_add);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final AutoCompleteTextView nomarticle1,unitearticle;


                                            unitearticle=(AutoCompleteTextView) addarticle.findViewById(R.id.dialogue_add_article_unite);
                                            nomarticle1=(AutoCompleteTextView) addarticle.findViewById(R.id.dialogue_add_article_nom);
                                            TextView textView=(TextView) addarticle.findViewById(R.id.dialogue_add_article_iconarticle_id);
                                            Icone icone=new Icone();
                                            icone.setIdicone(textView.getText().toString());
                                            final Article article1=new Article("",unitearticle.getText().toString(), nomarticle1.getText().toString(),icone);
                                            final AjouterArticle ajouterArticle=new AjouterArticle(article1, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    try{
                                                        addarticle.setCancelable(false);
                                                        JSONObject jsonObject1=new JSONObject(s);
                                                        if(jsonObject1.getBoolean("success")){
                                                            Toast.makeText(SignalerPNActiviy.this,"L'article a été bien ajouté",Toast.LENGTH_SHORT).show();
                                                        }

                                                    }catch(JSONException e){
                                                        e.printStackTrace();
                                                    }

                                                    addarticle.dismiss();
                                                    searchView.setQuery(nomarticle1.getText().toString(),true);
                                                }

                                            });
                                            requestQueue.add(ajouterArticle);
                                        }
                                    });
                                }
                            });

                        }
                    });
                    QteBesoin=(EditText) box.findViewById(R.id.dialogue_qte);
                    AjouterLarticle=(Button) box.findViewById(R.id.dialogue_ok);
                    AjouterLarticle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Besoin besoin=new Besoin();
                            String nomart=nomArticle.getText().toString();
                            String idartic=idarticle.getText().toString();
                            String uniteart=unitearticlee.getText().toString();
                            int qt=Integer.parseInt(QteBesoin.getText().toString());
                            Icone icone=new Icone(urliconee.getText().toString());
                            Article ab=new Article(idartic,uniteart,nomart,icone);
                            besoin.setQte(qt);
                            besoin.setArticle(ab);

                            besoins.add(besoin);

                            besoinAddAdapter.notifyDataSetChanged();

                            box.dismiss();
                        }
                    });
                }
                if (position!=0){
                    final AlertDialog.Builder alertDial1=new AlertDialog.Builder(SignalerPNActiviy.this);
                    alertDial1.setMessage("Supprimer Cette Article?");
                    alertDial1.setTitle("Suppression d'un article");
                    //alertDial.setIcon( albumPhoto.getListPhoto().get(position).getPhoto());
                    alertDial1.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            besoins.remove(position);
                            photoAddAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDial1.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDial1.show();
                }
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }) );

        photoRecycler.addOnItemTouchListener(new RecyclerTouchListener(SignalerPNActiviy.this, photoRecycler, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (albumPhoto.getListPhoto().size()<4){
                    if (position==0){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1001);
                    }
                }
                if (position!=0){
                    final AlertDialog.Builder alertDial=new AlertDialog.Builder(SignalerPNActiviy.this);
                    alertDial.setMessage("Ne Pas juindre cette photo?");
                    alertDial.setTitle("Suppression d'une image");
                    //alertDial.setIcon( albumPhoto.getListPhoto().get(position).getPhoto());
                    alertDial.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            albumPhoto.getListPhoto().remove(position);
                            photoAddAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDial.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDial.show();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        initLocation();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                //swt the color scheme to something less memory consuming
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //scale the image by factor 2
                options.inSampleSize = 2;
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = SignalerPNActiviy.this.getContentResolver().openInputStream(imageUri);//creating an imputstrea
                Photo photo=new Photo(BitmapFactory.decodeStream(imageStream,null,options));
                albumPhoto.addPhoto(photo);//decoding the input stream to bitmap
                photoAddAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        if(requestCode==2000){
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace( data,this);
                location.setText(place.getAddress());
                adresseLatLang=place.getLatLng();
            }
        }
    }

    public void publier(View view){
        if (ValiderLesChamps()){
            final ProgressDialog progressDialog = new ProgressDialog(SignalerPNActiviy.this);
            progressDialog.setTitle("Attendez");
            progressDialog.setMessage("Upploading !");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (chefAssociation==null)
                spn=new SignalPN(user,user,"",titre.getText().toString(),description.getText().toString(),numphone.getText().toString(),type.getText().toString(),albumPhoto,null,(ArrayList<Besoin>)besoins,false,adresseLatLang);
            else
                spn=new SignalPN(chefAssociation.getAssociation(),chefAssociation,"",titre.getText().toString(),description.getText().toString(),numphone.getText().toString(),type.getText().toString(),albumPhoto,null,(ArrayList<Besoin>)besoins,false,adresseLatLang);

            PublierRequest publierRequest=new PublierRequest(spn, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.i("taq", s);
                    progressDialog.dismiss();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.i("taq", volleyError.toString());
                }
            });
            requestQueue.add(publierRequest);
        }
    }

    public boolean ValiderLesChamps(){
        if (titre.getText().toString()=="" || titre.getText().toString().length()>30){
            titre.setError("A probleme here");
            return false;
        }
        if (description.getText().toString().length()>200){
            description.setError("A probleme here");
            return false;
        }
        if (numphone.getText().toString().length()>10){
            numphone.setError("A probleme here");
            return false;
        }
        if (besoins.size()==1){
            Toast.makeText(SignalerPNActiviy.this,"Vous devez introduire au moin un besoin",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void initLocation(){
        location.setClickable(true);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLngBounds latLngBounds = Help.getAd();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(latLngBounds);

                try {
                    startActivityForResult(builder.build(SignalerPNActiviy.this), 2000);
                } catch (Exception e) {
                    Log.e("taqi", e.getStackTrace().toString());
                }
            }
        });
    }



}
