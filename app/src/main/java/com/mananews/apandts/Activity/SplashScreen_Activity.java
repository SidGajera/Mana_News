package com.mananews.apandts.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.R;

public class SplashScreen_Activity extends AppCompatActivity {

    private Handler handler;
    public static String themeKEY;
    private RelativeLayout relayFull;
    private TextView txtSplashTitle, txtSplashSubTitle;
    private SplashScreen_Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_);

        getSupportActionBar().hide();
        statusBarColor();
        context = SplashScreen_Activity.this;
        relayFull = findViewById(R.id.relayFull);
        txtSplashTitle = findViewById(R.id.txtSplashTitle);
        txtSplashSubTitle = findViewById(R.id.txtSplashSubTitle);

        themeKEY = SPmanager.getPreference(SplashScreen_Activity.this, "themeKEY");

        if (themeKEY == null) {
            if (getApplicationContext().getResources().getString(R.string.isLight).equalsIgnoreCase("1")){
                themeKEY = "1";
            }else {
                themeKEY = "0";
            }
        }
        if (themeKEY != null) {
            if (themeKEY.equals("1")) {
                relayFull.setBackgroundResource(R.drawable.ic_spash_light);
                txtSplashTitle.setTextColor(getResources().getColor(R.color.darkDray));
                txtSplashSubTitle.setTextColor(getResources().getColor(R.color.darkDray));

            } else if (themeKEY.equals("0")) {
                relayFull.setBackgroundResource(R.drawable.ic_spash_dark);
                txtSplashTitle.setTextColor(getResources().getColor(R.color.yellow));
                txtSplashSubTitle.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            relayFull.setBackgroundResource(R.drawable.ic_spash_dark);
            txtSplashTitle.setTextColor(getResources().getColor(R.color.yellow));
            txtSplashSubTitle.setTextColor(getResources().getColor(R.color.white));
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (SPmanager.getFirsttime(context)) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(context, IntroActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }, 1500);
            }
        }, 1500);
    }

    private void statusBarColor() {
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
}
