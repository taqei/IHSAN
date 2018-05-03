package com.taqeiddine.ihsan.Model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.InterventionsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.ProjetDetailsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalPnDetailsActivity;
import com.taqeiddine.ihsan.Firebase.MyNotificationManager;
import com.taqeiddine.ihsan.Firebase.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    private String title,message;
    private Intent intent;
    private Activity activity;
    private int code;
    private int id;



    public Notification(Activity activity){
        this.activity=activity;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTheNotification(JSONObject json){

        Log.e("db", "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            this.message=message;


            try {
                JSONObject jsonObject=new JSONObject(title);
                this.code=jsonObject.getInt("code");
                switch (code){
                    case 0:{
                        String idpub=jsonObject.getString("idpublication");

                        if (jsonObject.getInt("child")==0){
                            Intent intent = new Intent(activity, SignalPnDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur", SharedPrefManager.getInstance(activity).getID());
                            if (SharedPrefManager.getInstance(activity).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(activity).isChef());
                            this.title="IHSAN APP";
                            this.intent=intent;

                        }else{
                            Intent intent = new Intent(activity, ProjetDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(activity).getID());
                            if (SharedPrefManager.getInstance(activity).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(activity).isChef());
                            this.title="IHSAN APP";
                            this.intent=intent;

                        }

                        break;
                    }
                    case 1 :{
                        Intent intent = new Intent(activity, SignalPnDetailsActivity.class);
                        String idpub=jsonObject.getString("idpublication");
                        intent.putExtra("idpublication",idpub);
                        intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(activity).getID());
                        if (SharedPrefManager.getInstance(activity).isChef()==1){
                            intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                        }
                        intent.putExtra("chef",SharedPrefManager.getInstance(activity).isChef());
                        this.title="IHSAN APP";
                        this.intent=intent;

                        break;
                    }

                    case 2 :{
                        Intent intent = new Intent(activity, InterventionsActivity.class);
                        String idpub=jsonObject.getString("idpublication");
                        intent.putExtra("idpublication",idpub);
                        this.title=jsonObject.getString("titre");
                        this.intent=intent;

                        break;
                    }

                    case 3:{
                        String idpub=jsonObject.getString("idpublication");
                        String titree=jsonObject.getString("titre");
                        if (jsonObject.getInt("child")==0){
                            Intent intent = new Intent(activity, SignalPnDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(activity).getID());
                            if (SharedPrefManager.getInstance(activity).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(activity).isChef());
                            this.intent=intent;
                            this.title=titree;

                        }else{
                            Intent intent = new Intent(activity, ProjetDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(activity).getID());
                            if (SharedPrefManager.getInstance(activity).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(activity).isChef());
                            this.intent=intent;
                            this.title=titree;


                        }

                        break;
                    }

                    case 4:{
                        String titree=jsonObject.getString("titre");
                        Intent intent = new Intent(activity, AssociationAdminActivity.class);
                        intent.putExtra("myidchef",SharedPrefManager.getInstance(activity).getID());
                        intent.putExtra("myidassociation",SharedPrefManager.getInstance(activity).getIDAssociation());
                        this.intent=intent;
                        this.title=titree;


                        break;
                    }

                    case 5:{
                        String titree=jsonObject.getString("titre");
                        String idasso=jsonObject.getString("idassociation");

                        Intent intent = new Intent(activity, AssociationActivity.class);
                        intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(activity).getID());
                        intent.putExtra("myidassociation",idasso);
                        this.intent=intent;
                        this.title=titree;


                        break;
                    }

                    case 6:{
                        String titree=jsonObject.getString("titre");
                        String idasso=jsonObject.getString("idassociation");
                        Intent intent = new Intent(activity, AssociationAdminActivity.class);
                        intent.putExtra("myidchef",SharedPrefManager.getInstance(activity).getID());
                        intent.putExtra("myidassociation",idasso);
                        this.intent=intent;
                        this.title=titree;


                        break;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        } catch (JSONException e) {
            Log.e("db", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("db", "Exception: " + e.getMessage());
        }
    }

}
