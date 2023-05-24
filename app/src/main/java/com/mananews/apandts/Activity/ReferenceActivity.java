package com.mananews.apandts.Activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.mananews.apandts.R;



public class ReferenceActivity extends AppCompatActivity {

    private static final String TAG = "ReferenceActivity";
    private ImageView imgDown;
    private WebView webViewRef;
    private String reference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        getSupportActionBar().hide();

        statusBarColor();
        reference = getIntent().getStringExtra("strReference");
        imgDown = findViewById(R.id.imgDown);
        webViewRef = findViewById(R.id.webViewRef);
        progressBar = findViewById(R.id.progressBar);

        webViewRef.getSettings().setJavaScriptEnabled(true);
        webViewRef.setWebViewClient(new WebViewClient());
        webViewRef.loadUrl(reference);

        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                openActivityfromBottom();

            }
        });
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void openActivityfromBottom() {
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
    public void onBackPressed() {
        super.onBackPressed();
        openActivityfromBottom();
    }
}