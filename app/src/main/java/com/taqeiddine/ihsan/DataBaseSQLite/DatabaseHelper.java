package com.taqeiddine.ihsan.DataBaseSQLite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.taqeiddine.ihsan.Model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        String  create="CREATE TABLE notification ( idnotification INTEGER PRIMARY KEY AUTOINCREMENT, noti TEXT, datetimenot DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(create);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS notification");

        // Create tables again
        onCreate(db);
    }

    public long insertNotification(String noti) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put("noti", noti);

        // insert row
        long id = db.insert("notification", null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }



    public ArrayList<Notification> notificationsTen(Activity activity,long lastid){
        ArrayList<Notification> notifications=new ArrayList<>();
        String selectQuery;
        if(lastid<0)
            selectQuery = "select t.* from ( SELECT  * FROM notification n ORDER BY " +
                "n.idnotification" + " DESC)t ";
        else
            selectQuery = "select t.* from ( SELECT  * FROM notification n where n.idnotification < "+lastid + " ORDER BY " +
                "idnotification" + " DESC)t ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               Notification notification=new Notification(activity);
                try {
                    notification.setTheNotification(new JSONObject(cursor.getString(cursor.getColumnIndex("noti"))));
                    notification.setId(cursor.getInt(cursor.getColumnIndex("idnotification")));
                    notifications.add(notification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notifications;
    }

    public void deleteRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from notification");
        db.close();
    }







}
