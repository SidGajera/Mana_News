package com.mananews.apandts.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mananews.apandts.R;


public class FullImageActivity extends AppCompatActivity {

    private PhotoView imgPhotoView;
    private ImageView imgClose;
    private String image, newsTitle;
    private TextView txtNewsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        getSupportActionBar().hide();

        statusBarColor();
        Intent intent = getIntent();
        image = intent.getStringExtra("image");
        newsTitle = intent.getStringExtra("newsTitle");
        imgPhotoView = findViewById(R.id.imgPhotoView);
        imgClose = findViewById(R.id.imgClose);
        txtNewsTitle = findViewById(R.id.txtNewsTitle);

        txtNewsTitle.setText(Html.fromHtml(newsTitle));
        Glide.with(this).load(image).into(imgPhotoView);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(FullImageActivity.this);
            }
        });
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
        ActivityCompat.finishAfterTransition(FullImageActivity.this);

    }
}