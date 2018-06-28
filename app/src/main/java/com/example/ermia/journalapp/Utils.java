package com.example.ermia.journalapp;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Utils {

    public static void showMessage(View view, String message){
        Snackbar.make(view,
                message, Snackbar.LENGTH_SHORT).show();
    }
}
