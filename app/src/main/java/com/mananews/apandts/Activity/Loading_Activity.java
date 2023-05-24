package com.mananews.apandts.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.mananews.apandts.R;


import com.mananews.apandts.utils.ConnectionDetector;

public class Loading_Activity extends AppCompatActivity {

    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {

            if (intent.getAction().matches(ConnectivityManager.CONNECTIVITY_ACTION)) {

                if (checkInternet(Loading_Activity.this)) {

                    Intent a = new Intent(context, MainActivity.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(a);

                } else {

                    doWork();
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Please Connect Internet, and Restart App", Toast.LENGTH_SHORT).show();
                }

            } else {

                Intent a = new Intent(context, MainActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(a);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(gpsLocationReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();

    }

    @Override
    protected void onPause() {
//        unregisterReceiver(gpsLocationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsLocationReceiver);
        super.onPause();

    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsLocationReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsLocationReceiver);
        super.onDestroy();

    }

    private ProgressBar progressBar;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_);

        getSupportActionBar().hide();
        statusBarColor();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(gpsLocationReceiver, filter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = new Intent(Loading_Activity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void statusBarColor() {
        // custom status bar color
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean checkInternet(Loading_Activity loadingActivity) {
        // TODO Auto-generated method stub
        ConnectionDetector cd = new ConnectionDetector(loadingActivity);
        return cd.isConnectingToInternet();
    }

    private void doWork() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }

        for (int progress = 0; progress < 100; progress += 10) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
