package com.example.mobileapp.utils;

import android.content.Context;
import android.widget.Toast;

public class NotificationManager {
    static public void showNotification(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
