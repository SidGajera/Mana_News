package com.mananews.apandts.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.mananews.apandts.api.ApiClient;
import com.mananews.apandts.api.ApiService;
import com.mananews.apandts.R;

public class SPmanager {
    public static String preferenceName = "myPref";
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;

    public static ApiService getUserService(Context context){
        return ApiClient.getClient(context.getResources().getString(R.string.server_url)).create(ApiService.class);
    }


    public static boolean isConnected(Activity mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !((NetworkInfo) netInfo).isConnectedOrConnecting()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("No Internet Connection")
                    .setMessage("Please Check Your Internet Connection..")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
            return false;
        }

        NetworkInfo wifi = cm.getNetworkInfo(1);
        NetworkInfo mobile = cm.getNetworkInfo(0);
        if (mobile != null && mobile.isConnectedOrConnecting()) {
            return true;
        }
        return wifi != null && wifi.isConnectedOrConnecting();
    }





    public static String saveValue(Context context, String key, String value) {
         sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        return key;
    }
    public static String saveValueBoolean(Context context, String key, boolean value) {
         sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        return key;
    }

    public static String getPreference(Context context, String key) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(key, null);
    }
    public static boolean getPreferenceBoolean(Context context, String key) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static void setFirsttime(Context context, boolean b) {
         sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("firsttime", b);
        editor.apply();
    }


    public static boolean getFirsttime(Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean("firsttime", false);
    }

    public static void isLogOut(Context context) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}

