package com.taqeiddine.ihsan.Activities.Messages;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taqeiddine.ihsan.Adapters.MessageAdapter;
import com.taqeiddine.ihsan.Help;
import com.taqeiddine.ihsan.Model.Message;
import com.taqeiddine.ihsan.Model.Profile.Association;
import com.taqeiddine.ihsan.Model.Profile.Profile;
import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.R;
import com.taqeiddine.ihsan.VOLLEY.Message.*;
import com.taqeiddine.ihsan.VOLLEY.Profile.getProfileInfoLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    CircleImageView photodeprofile;
    TextView nomprenom;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    ImageView sendmessage;
    EditText themessage;


    Profile me,other;

    String lastid;

    final LinkedList<Message> arrayList=new LinkedList<>();
    final MessageAdapter messageAdapter=new MessageAdapter(arrayList,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        me=new Profile(getIntent().getExtras().getString("myidprofil"));
        other=new Profile(getIntent().getExtras().getString("otheridprofil"));
        messageAdapter.setMe(me);
        requestQueue = Volley.newRequestQueue(this);
        nomprenom=(TextView) findViewById(R.id.message_nomprenom);
        photodeprofile=(CircleImageView) findViewById(R.id.message_photoprofil);
        progressBar=(ProgressBar) findViewById(R.id.message_progress);
        themessage=(EditText) findViewById(R.id.message_message);
        //send a message
        sendmessage=(ImageView) findViewById(R.id.message_send);
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.setExp(me);
                message.setRecep(other);
                message.setMessage(themessage.getText().toString());

                SendMessage sendMessage=new SendMessage(message, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try{
                            JSONObject j=new JSONObject(s);
                            if(j.getBoolean("success")){
                                Toast.makeText(MessageActivity.this, "Votre message a été envoyé", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MessageActivity.this, "Votre message n'a pas été envoyé", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){

                        }
                        themessage.setText("");
                    }
                });
                requestQueue.add(sendMessage);
            }
        });
        // info of other profile
        getProfileInfoLite profileInfoLite =new getProfileInfoLite(other, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Oppooo",s);
                try{
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getBoolean("success")){
                        other= Profile.fromJsonINFOPROFILELITE(jsonObject);
                        if (other instanceof Utilisateur){
                            Utilisateur utilisateur=(Utilisateur) other;
                            nomprenom.setText(utilisateur.getNom()+"  "+utilisateur.getPrenom());
                            if (utilisateur.getPhotodeprofil()!=null){
                                Glide.with(MessageActivity.this).load(Help.getMedia()+utilisateur.getPhotodeprofil().getUrl()).into(photodeprofile);
                            }
                        }
                        if(other instanceof Association){
                            Association asso=(Association) other;
                            nomprenom.setText(asso.getNomassociation());
                            if (asso.getPhotodeprofil()!=null){
                                Glide.with(MessageActivity.this).load(Help.getMedia()+asso.getPhotodeprofil().getUrl()).into(photodeprofile);
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(profileInfoLite);




        recyclerView=(RecyclerView) findViewById(R.id.message_recy);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView=(NestedScrollView) findViewById(R.id.message_scroll);
        nestedScrollView.setBottom(1);

        recyclerView.setAdapter(messageAdapter);
        getTheMessages("");

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // We take the last son in the scrollview
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int distanceToEnd = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                //int distanceToTop = (view.getTop() - ( nestedScrollView.getScrollY()));

                if (distanceToEnd == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    getTheMessages(arrayList.getLast().getIdMessage());
                }
            }
        });
    }

    private void getTheMessages(String idmessage){
        getMessages getmessages=new getMessages(me, other,idmessage, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("messages",s.toString());
                try{
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getBoolean("success")){
                        int i=jsonObject.getInt("nbMsg");
                        for(int j=0;j<i;j++){
                            JSONObject t=jsonObject.getJSONObject(""+j);
                            Message message=Message.fromJson(t);
                            arrayList.add(message);
                            Log.i("tako",message.getIdMessage());
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(getmessages);
    }
}
