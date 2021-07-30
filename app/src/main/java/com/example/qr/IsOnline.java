package com.example.qr;

import android.content.Context;
import android.net.ConnectivityManager;

public class IsOnline {
    //Проверка подключения к интернету
    protected boolean isOnline(Context context) {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else
            return true;
    }
}
