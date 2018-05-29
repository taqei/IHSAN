package com.taqeiddine.ihsan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Taqei on 10/03/2018.
 */

public class Help {
    private static final String myIP="http://192.168.1.18/";
    public static String getMyIP(){
        return myIP;
      //return "http://192.168.43.215/";
    }

    public static String getURL(){
        //return "http://192.168.1.18/ihsanapp/";
        return "http://192.168.43.215/ihsanapp/";
    }
    public  static  String getMedia(){
        //return "http://192.168.1.18/ihsanapp/MEDIA/";
        return "http://192.168.43.215/ihsanapp/MEDIA/";
    }


    public static SimpleDateFormat getLaDate(){
        return new SimpleDateFormat("EEE dd MMMM yy", Locale.FRANCE);
    }

    public static SimpleDateFormat getLHeure(){
        return new SimpleDateFormat("hh:mm", Locale.FRANCE);
    }

    public static SimpleDateFormat getDATE(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDATEYMD(){
        return new SimpleDateFormat("yyyy-MM-dd");
    }
    public static SimpleDateFormat getDATEHMS(){
        return new SimpleDateFormat("HH:mm");
    }
    public static SimpleDateFormat getDATEHMS1(){
        return new SimpleDateFormat("HH:mm:ss");
    }

    public static LatLngBounds getAd(){
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(36.740776,3.0378333),new LatLng(36.792521,3.0570713)
                );
        return  latLngBounds;
    }
}
