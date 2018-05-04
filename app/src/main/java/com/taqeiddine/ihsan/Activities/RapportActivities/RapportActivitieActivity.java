package com.taqeiddine.ihsan.Activities.RapportActivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.taqeiddine.ihsan.Adapters.ClickListener;
import com.taqeiddine.ihsan.Adapters.MyDividerItemDecoration;
import com.taqeiddine.ihsan.Adapters.PhotoAddAdapter;
import com.taqeiddine.ihsan.Adapters.PublicationSearchAdapter;
import com.taqeiddine.ihsan.Adapters.RecyclerTouchListener;
import com.taqeiddine.ihsan.Model.ActivityRapport;
import com.taqeiddine.ihsan.Model.AlbumPhoto;
import com.taqeiddine.ihsan.Model.Photo;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.ChefAssociation;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.RapportAct.InsertRapport;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RapportActivitieActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private AlbumPhoto albumPhoto;
    private PhotoAddAdapter photoAddAdapter;

    private RecyclerView photoRecycler;
    private Button publier;
    private MultiAutoCompleteTextView titre,desc,publication;


    private Association association;
    private ChefAssociation chefAssociation;

    private Publication thePublication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport_activitie);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Ajouter un rapport d'activity");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue= Volley.newRequestQueue(this);

        photoRecycler=(RecyclerView) findViewById(R.id.activitie_act_recy_photo);
        publier=(Button) findViewById(R.id.activitie_act_publier);
        titre=(MultiAutoCompleteTextView) findViewById(R.id.activitie_act_text);
        desc=(MultiAutoCompleteTextView) findViewById(R.id.activitie_act_text);
        publication=(MultiAutoCompleteTextView) findViewById(R.id.activitie_act_publication);

        association=new Association(getIntent().getStringExtra("idassociation"));
        chefAssociation=new ChefAssociation(new Utilisateur(getIntent().getStringExtra("myidutilisateur")),association);

        albumPhoto=new AlbumPhoto();
        photoAddAdapter=new PhotoAddAdapter(albumPhoto);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        photoRecycler.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 16));
        photoRecycler.setLayoutManager(mLayoutManager1);
        photoRecycler.setItemAnimator(new DefaultItemAnimator());
        photoRecycler.setAdapter(photoAddAdapter);

        photoRecycler.addOnItemTouchListener(new RecyclerTouchListener(RapportActivitieActivity.this, photoRecycler, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (albumPhoto.getListPhoto().size()<10){
                    if (position==0){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1001);
                    }
                }
                if (position!=0){
                    final AlertDialog.Builder alertDial=new AlertDialog.Builder(RapportActivitieActivity.this);
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
        initPublier();
        initDialog();

    }

    public void initPublier(){
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(RapportActivitieActivity.this);
                progressDialog.setTitle("Attendez");
                progressDialog.setMessage("Upploading !");
                progressDialog.setCancelable(false);
                progressDialog.show();

                ActivityRapport rapport=new ActivityRapport();
                rapport.setAlbumPhoto(albumPhoto);
                rapport.setAssociation(association);
                rapport.setChefAssociation(chefAssociation);
                rapport.setText(desc.getText().toString());
                rapport.setTitre(titre.getText().toString());
                //si rapport dactivity
                if(thePublication!=null){
                    rapport.setPublication(thePublication);
                }


                InsertRapport insertRapport=new InsertRapport(rapport, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.dismiss();
                        Toast.makeText(RapportActivitieActivity.this,"Votre requette a été bien effectué",Toast.LENGTH_LONG).show();
                        RapportActivitieActivity.this.finish();
                    }
                });
                requestQueue.add(insertRapport);
            }
        };
        publier.setOnClickListener(onClickListener);
    }

    public void initDialog(){
        publication.setClickable(true);
        publication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog searche=new Dialog(RapportActivitieActivity.this);
                searche.requestWindowFeature(Window.FEATURE_NO_TITLE);
                searche.setContentView(R.layout.dialogue_search_publication);
                searche.show();
                final PublicationSearchAdapter adapter=new PublicationSearchAdapter(RapportActivitieActivity.this,association,requestQueue);
                android.widget.SearchView searchView=(android.widget.SearchView) searche.findViewById(R.id.dialogue_search_publication);
                ListView listView=(ListView) searche.findViewById(R.id.dialogue_search_listview);
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
                        thePublication=adapter.getItem(position);
                        publication.setText(thePublication.getTitrepub());
                        searche.dismiss();
                    }
                });

            }
        });
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
                InputStream imageStream = RapportActivitieActivity.this.getContentResolver().openInputStream(imageUri);//creating an imputstrea
                Photo photo=new Photo(BitmapFactory.decodeStream(imageStream,null,options));
                albumPhoto.addPhoto(photo);//decoding the input stream to bitmap
                photoAddAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
