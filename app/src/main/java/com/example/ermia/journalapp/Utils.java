package com.example.ermia.journalapp;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Utils {

    public static void showMessage(View view, String message) {
        Snackbar.make(view,
                message, Snackbar.LENGTH_SHORT).show();
    }

    public static String getCurrentDateAsString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getFormattedDateString(String stringDate){
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        Date date = new Date(stringDate);
        return dateFormat.format(date);
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
