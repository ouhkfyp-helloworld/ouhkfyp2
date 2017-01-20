package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 19/1/2017.
 */
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppManager extends Application {
    public static boolean loading = false;
    public static User user = null;
    public static int profileNumber = 0;
    public static int detailRestaurantNumber = 0;

    public static Product watchDetailProduct = null;
    //public static Profile watchDetailProfile = null;
    @Override
    public void onCreate() {
        super.onCreate();



    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

