package com.taqeiddine.ihsan.Firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationActivity;
import com.taqeiddine.ihsan.Activities.ProfileActivities.AssociationAdminActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.InterventionsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.ProjetActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.ProjetDetailsActivity;
import com.taqeiddine.ihsan.Activities.PublicationActivities.SignalPnDetailsActivity;
import com.taqeiddine.ihsan.DataBaseSQLite.DatabaseHelper;
import com.taqeiddine.ihsan.MainActivity;
import com.taqeiddine.ihsan.Model.Profile.Association;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        Log.e(TAG, "Notification JSON " + json.toString());
        DatabaseHelper databaseHelper=new DatabaseHelper(getBaseContext());
        long i=databaseHelper.insertNotification(json.toString());
        Log.i("khourii is speed","+"+i);
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");


            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            try {
                JSONObject jsonObject=new JSONObject(title);
                switch (jsonObject.getInt("code")){
                    case 0:{
                        String idpub=jsonObject.getString("idpublication");

                        if (jsonObject.getInt("child")==0){
                            Intent intent = new Intent(getApplicationContext(), SignalPnDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                            if (SharedPrefManager.getInstance(this).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(this).isChef());


                            mNotificationManager.showNotification("IHSAN APP", message, intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), ProjetDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                            if (SharedPrefManager.getInstance(this).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(this).isChef());


                            mNotificationManager.showNotification("IHSAN APP", message, intent);
                        }

                        break;
                    }
                    case 1 :{
                        Intent intent = new Intent(getApplicationContext(), SignalPnDetailsActivity.class);
                        String idpub=jsonObject.getString("idpublication");
                        intent.putExtra("idpublication",idpub);
                        intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                        if (SharedPrefManager.getInstance(this).isChef()==1){
                            intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());
                        }
                        intent.putExtra("chef",SharedPrefManager.getInstance(this).isChef());

                        mNotificationManager.showNotification("IHSAN APP", message, intent);
                        break;
                    }

                    case 2 :{
                        Intent intent = new Intent(getApplicationContext(), InterventionsActivity.class);
                        String idpub=jsonObject.getString("idpublication");
                        intent.putExtra("idpublication",idpub);


                        mNotificationManager.showNotification(jsonObject.getString("titre"), message, intent);
                        break;
                    }

                    case 3:{
                        String idpub=jsonObject.getString("idpublication");
                        String titree=jsonObject.getString("titre");
                        if (jsonObject.getInt("child")==0){
                            Intent intent = new Intent(getApplicationContext(), SignalPnDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                            if (SharedPrefManager.getInstance(this).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(this).isChef());


                            mNotificationManager.showNotification(titree, message, intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), ProjetDetailsActivity.class);
                            intent.putExtra("idpublication",idpub);
                            intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                            if (SharedPrefManager.getInstance(this).isChef()==1){
                                intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());
                            }
                            intent.putExtra("chef",SharedPrefManager.getInstance(this).isChef());


                            mNotificationManager.showNotification(titree, message, intent);
                        }

                        break;
                    }

                    case 4:{
                        String titree=jsonObject.getString("titre");
                        Intent intent = new Intent(getApplicationContext(), AssociationAdminActivity.class);
                        intent.putExtra("myidchef",SharedPrefManager.getInstance(this).getID());
                        intent.putExtra("myidassociation",SharedPrefManager.getInstance(this).getIDAssociation());

                        mNotificationManager.showNotification(titree, message, intent);
                        break;
                    }

                    case 5:{
                        String titree=jsonObject.getString("titre");
                        String idasso=jsonObject.getString("idassociation");

                        Intent intent = new Intent(getApplicationContext(), AssociationActivity.class);
                        intent.putExtra("myidutilisateur",SharedPrefManager.getInstance(this).getID());
                        intent.putExtra("myidassociation",idasso);

                        mNotificationManager.showNotification(titree, message, intent);
                        break;
                    }

                    case 6:{
                        String titree=jsonObject.getString("titre");
                        String idasso=jsonObject.getString("idassociation");
                        Intent intent = new Intent(getApplicationContext(), AssociationAdminActivity.class);
                        intent.putExtra("myidchef",SharedPrefManager.getInstance(this).getID());
                        intent.putExtra("myidassociation",idasso);

                        mNotificationManager.showNotification(titree, message, intent);
                        break;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}
