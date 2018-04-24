package com.ding.commonlibrary.utils;

import android.app.Activity;
import android.app.AlertDialog;

public class DialogUtil {

    private Activity activity;

    public DialogUtil(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(message)
                .setNegativeButton("确认", (dialog, which) -> activity.finish())
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    public void onDestory() {
        activity = null;
    }
}