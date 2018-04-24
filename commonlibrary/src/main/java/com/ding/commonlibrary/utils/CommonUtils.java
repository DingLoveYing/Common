package com.ding.commonlibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.ding.commonlibrary.utils.env.DeviceInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class CommonUtils {


    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        CommonUtils.debugLog("设置透明度");
    }

    /**
     * 创建dialog对象
     *
     * @param context
     * @param view
     */
    public static Dialog getDialog(Context context, View view, boolean cancelable) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(cancelable);
        return dialog;
    }

    public static AlertDialog getDialog(Context context, View view, int style, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context, style).setView(view).create();
        dialog.setCancelable(cancelable);
        return dialog;
    }

    /**
     * 跳转页面
     *
     * @param context
     * @param activity
     */
    public static void toNextActivity(Context context, Class activity) {
        context.startActivity(new Intent(context, activity));
    }

    /**
     * 携带数据跳转页面
     *
     * @param context
     * @param activity
     */
    public static void sendDataToNextActivity(Context context, Class activity, String[] key, String[] data) {
        Intent intent = new Intent(context, activity);
        for (int i = 0; i < key.length; i++) {
            intent.putExtra(key[i], data[i]);
        }
        context.startActivity(intent);
    }


    /**
     * 获取当前系统时间
     *
     * @param type true 详细时间,精确到秒  false 精确到日
     * @return
     */
    public static String getCurrentTime(boolean type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type ? "yyyy-MM-dd  HH:mm:ss " : "yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * toast
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * debug log信息
     *
     * @param content
     */
    public static void debugLog(String content) {
        if (true) {
            Log.d("wr", "----946----" + content);
        }
    }








    public static boolean checkNet() {
        String networkTypeName = DeviceInfo.getNetworkTypeName();
        if (TextUtils.isEmpty(networkTypeName)) {//没网络
            ToastUtils.showToast("网络错误");
            return false;
        }
        return true;
    }

//    public static void checkPermission(Activity activity, MPermissionUtils.OnPermissionListener listener) {
//        MPermissionUtils.requestPermissionsResult(activity,
//                1,
//                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.CHANGE_WIFI_STATE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                listener);
//    }





    /**
     * 启动当前应用设置页面
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
