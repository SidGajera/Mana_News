package com.mananews.apandts.Activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.mananews.apandts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class About_Us extends AppCompatActivity {

    private static final String TAG = "About Us";
    private ProgressBar progress, progress2;
    private TextView tx_detailAboutUs;
    private RelativeLayout lay_header, relay_aboutus, relayAbout;
    private TextView tx_aboutus;
    private Button btnSS;
    private File imagePath;
    private File file;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().hide();
        init();
        statusBarColor();
        getAbout_Us();
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                lay_header.setBackgroundColor(getResources().getColor(R.color.header));
                relay_aboutus.setBackgroundColor(getResources().getColor(R.color.white));
                tx_aboutus.setTextColor(getResources().getColor(R.color.darkDray));
                tx_detailAboutUs.setTextColor(getResources().getColor(R.color.darkDray));
            }
        }
    }


    private void init() {
        tx_detailAboutUs = findViewById(R.id.tx_detailAboutUs);
        progress = findViewById(R.id.progress);
        progress2 = findViewById(R.id.progress2);
        lay_header = findViewById(R.id.lay_header);
        relay_aboutus = findViewById(R.id.relay_aboutus);
        relayAbout = findViewById(R.id.relayAbout);
        tx_aboutus = findViewById(R.id.tx_aboutus);

    }

    private Object getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_"
                    + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getLocalBitmapUri: " + e.getMessage());
        }
        return bmpUri;
    }

    private void getAbout_Us() {

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progress2.setVisibility(View.VISIBLE);
            } else if (MainActivity.themeKEY.equals("0")) {
                progress.setVisibility(View.VISIBLE);
            }
        } else {
            progress.setVisibility(View.VISIBLE);
        }
        String url = getString(R.string.server_url) + "webservices/about.php";
        Log.e(TAG, "getPrivacy_Policy: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(About_Us.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);
                try {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")) {

                            JSONObject resp = jsonObject.getJSONObject("response");

                            JSONObject data = resp.getJSONObject("data");
                            JSONObject about = data.getJSONObject("about");
                            String about_us = about.getString("about_us");

                            progress.setVisibility(View.GONE);
                            progress2.setVisibility(View.GONE);
                            tx_detailAboutUs.setText(Html.fromHtml(about_us));

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 200);

                        } else {
                            progress.setVisibility(View.GONE);
                            progress2.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e.getMessage());
                        progress.setVisibility(View.GONE);
                        progress2.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        requestQueue.add(stringRequest);
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



    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.visibility.equalsIgnoreCase("1")){
            if (MainActivity.counterAPI == MainActivity.counter){
                loadAd();
            }else {
                if ((MainActivity.counterAPI + 1 ) < MainActivity.counter){
                    MainActivity.counter = 1;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.counter = MainActivity.counter + 1;
        if (mInterstitialAd != null) {
            MainActivity.counter = 0;
            mInterstitialAd.show(About_Us.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }
    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this, getApplicationContext().getString(R.string.interstitial_id),
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.e("TAG", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
//                        loadAd();
                                MainActivity.counter = 0;
                                onBackPressed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.e("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }



}
