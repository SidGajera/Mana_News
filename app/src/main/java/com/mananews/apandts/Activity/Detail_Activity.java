package com.mananews.apandts.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.mananews.apandts.BuildConfig;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.OnSwipeTouchListener;
import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.utils.SqliteHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

public class Detail_Activity extends AppCompatActivity {

    private static final String TAG = "Detal----Activity----";
    public static int countBackPress = 0;
    public static RelativeLayout relay_detail;
    private static Bitmap imagess;
    private static boolean adKEY;
    public TextToSpeech tts;
    String[] codelist = new String[]{"ar", "zh", "cs", "da", "nl", "en", "fi",
            "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", "no",
            "pl", "pt", "ro", "ru", "sk", "es", "sv", "th", "tr", "te", "mr"};
    private ImageView btn_play, imgBackD, img_Addbookmark, img_Deletebookmark, imgShareD, btn_pause, img_detail, img_playD, img_playurl, imgComment;
    private ImageView img_detail2;
    private SqliteHelper sqliteHelper;
    private String news_id, short_desc, long_desc, medialink, media, is_image, image, bann_ads, int_ads, ad_type, visibility, news_date, reference, shortname;
    private TextView tx_shortdesc, tx_longdesc, txtNewsDate, txtRefName, txtWaterMark;
    private TextView tx_shortdesc2, tx_longdesc2, txtNewsDate2, txtRefName2;
    private LinearLayout ll_ads, bottom, linReference;
    private RelativeLayout relay_tts, relAdD, relay_adviewD, layitemImg, share, realyDetailpg, relayDescription, relayFullD;
    private RelativeLayout layitemImg2, relayDescription2;
    private ProgressDialog progressDialog;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    private ScrollView scrollView, scrollView2;
    private int counter;
    private Bitmap bitmap__;
    private Spanned Text;

    public static Bitmap ConvertUrlToBitmap(final String src) {
        try {
            URL url = null;
            try {
                url = new URL(src);
                imagess = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imagess;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        progressDialog = new ProgressDialog(Detail_Activity.this);
        getSupportActionBar().hide();

        statusBarColor();
        getIntents();
        init();
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                layitemImg.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                layitemImg2.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                scrollView.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                scrollView2.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                tx_shortdesc.setTextColor(getResources().getColor(R.color.darkDray));
                tx_shortdesc2.setTextColor(getResources().getColor(R.color.darkDray));
                tx_longdesc.setTextColor(getResources().getColor(R.color.darkDray));
                tx_longdesc2.setTextColor(getResources().getColor(R.color.darkDray));
                bottom.setBackgroundColor(getResources().getColor(R.color.darkDray));
                relayFullD.setBackgroundColor(getResources().getColor(R.color.white));
                txtNewsDate.setTextColor(getResources().getColor(R.color.darkDray));
                txtNewsDate2.setTextColor(getResources().getColor(R.color.darkDray));
                txtRefName.setTextColor(getResources().getColor(R.color.darkDray));
                txtRefName2.setTextColor(getResources().getColor(R.color.darkDray));
                txtWaterMark.setTextColor(getResources().getColor(R.color.darkDray));
                imgBackD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgShareD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_playD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            if (MainActivity.themeKEY.equals("0")) {
                layitemImg.setBackgroundColor(getResources().getColor(R.color.darkDray));
                layitemImg2.setBackgroundColor(getResources().getColor(R.color.darkDray));
                scrollView.setBackgroundColor(getResources().getColor(R.color.darkDray));
                scrollView2.setBackgroundColor(getResources().getColor(R.color.darkDray));
                tx_shortdesc.setTextColor(getResources().getColor(R.color.yellow));
                tx_shortdesc2.setTextColor(getResources().getColor(R.color.yellow));
                tx_longdesc.setTextColor(getResources().getColor(R.color.white));
                tx_longdesc2.setTextColor(getResources().getColor(R.color.white));
                bottom.setBackgroundColor(getResources().getColor(R.color.darkDray));
                relayFullD.setBackgroundColor(getResources().getColor(R.color.darkDray));
                txtNewsDate.setTextColor(getResources().getColor(R.color.white));
                txtNewsDate2.setTextColor(getResources().getColor(R.color.white));
                txtRefName.setTextColor(getResources().getColor(R.color.yellow));
                txtRefName2.setTextColor(getResources().getColor(R.color.yellow));
                txtWaterMark.setTextColor(getResources().getColor(R.color.yellow));
                imgBackD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgShareD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_playD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            layitemImg.setBackgroundColor(getResources().getColor(R.color.darkDray));
            layitemImg2.setBackgroundColor(getResources().getColor(R.color.darkDray));
            scrollView.setBackgroundColor(getResources().getColor(R.color.darkDray));
            scrollView2.setBackgroundColor(getResources().getColor(R.color.darkDray));
            tx_shortdesc.setTextColor(getResources().getColor(R.color.yellow));
            tx_shortdesc2.setTextColor(getResources().getColor(R.color.yellow));
            tx_longdesc.setTextColor(getResources().getColor(R.color.white));
            tx_longdesc2.setTextColor(getResources().getColor(R.color.white));
            bottom.setBackgroundColor(getResources().getColor(R.color.darkDray));
            txtNewsDate.setTextColor(getResources().getColor(R.color.white));
            txtNewsDate2.setTextColor(getResources().getColor(R.color.white));
            txtRefName.setTextColor(getResources().getColor(R.color.yellow));
            txtRefName2.setTextColor(getResources().getColor(R.color.yellow));
            txtWaterMark.setTextColor(getResources().getColor(R.color.yellow));
            imgBackD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgShareD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_playD.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgComment.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        showdb();

        if (getString(R.string.tts_visibility).equals("yes")) {
            relay_tts.setVisibility(View.VISIBLE);
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {

                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (MainActivity.themeKEY.equals("0")) {
                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                btn_play.setVisibility(View.VISIBLE);
                btn_play.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            relay_tts.setVisibility(View.GONE);
        }
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

    private void getIntents() {

        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");
        short_desc = intent.getStringExtra("short_desc");
        long_desc = intent.getStringExtra("long_desc");

        image = intent.getStringExtra("image");
        medialink = intent.getStringExtra("medialink");
        media = intent.getStringExtra("media");
        is_image = intent.getStringExtra("is_image");
        news_date = intent.getStringExtra("news_date");
        shortname = intent.getStringExtra("shortname");
        reference = getIntent().getStringExtra("reference");

        Log.e(TAG, "getIntents: " + image + medialink + media + is_image + long_desc + reference + shortname);


        supportLan(long_desc);
    }

    private void init() {

        btn_play = (ImageView) findViewById(R.id.btn_playD);
        btn_pause = (ImageView) findViewById(R.id.btn_pauseD);
        img_Addbookmark = (ImageView) findViewById(R.id.img_AddbookmarkD);
        img_Deletebookmark = (ImageView) findViewById(R.id.img_DeletebookmarkD);
        relAdD = findViewById(R.id.relAdD);
        relay_adviewD = findViewById(R.id.relay_adviewD);
        imgBackD = findViewById(R.id.imgBackD);

        img_detail = (ImageView) findViewById(R.id.img_detail);
        img_playD = (ImageView) findViewById(R.id.img_playD);
        tx_shortdesc = (TextView) findViewById(R.id.tx_shortdesc);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tx_longdesc = (TextView) findViewById(R.id.tx_longdesc);
        share = (RelativeLayout) findViewById(R.id.share);
        relay_tts = findViewById(R.id.relay_tts);
        bottom = findViewById(R.id.bottom);
        img_playurl = findViewById(R.id.img_playurl);
        imgShareD = findViewById(R.id.imgShareD);
        relay_detail = findViewById(R.id.relay_detail);
        layitemImg = findViewById(R.id.layitemImg);
        ll_ads = findViewById(R.id.ll_ads);
        realyDetailpg = findViewById(R.id.realyDetailpg);
        relayDescription = findViewById(R.id.relayDescription);
        linReference = findViewById(R.id.linReference);
        txtNewsDate = findViewById(R.id.txtNewsDateD);
        txtRefName = findViewById(R.id.txtRefNameD);
        relayFullD = findViewById(R.id.relayFullD);

        scrollView2 = findViewById(R.id.scrollView2);
        layitemImg2 = findViewById(R.id.layitemImg2);
        img_detail2 = (ImageView) findViewById(R.id.img_detail2);
        relayDescription2 = findViewById(R.id.relayDescription2);
        tx_shortdesc2 = (TextView) findViewById(R.id.tx_shortdesc2);
        tx_longdesc2 = (TextView) findViewById(R.id.tx_longdesc2);
        txtNewsDate2 = findViewById(R.id.txtNewsDateD2);
        txtRefName2 = findViewById(R.id.txtRefNameD2);
        txtWaterMark = findViewById(R.id.txtWaterMark);
        imgComment = findViewById(R.id.imgComment);

        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        img_detail.startAnimation(animFadeIn);

        imgBackD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tx_shortdesc.setText(Html.fromHtml(short_desc));
        Text = Html.fromHtml(long_desc);
        tx_longdesc.setMovementMethod(LinkMovementMethod.getInstance());
        tx_longdesc.setText(Text);
        txtNewsDate.setText(news_date);
        txtRefName.setText(shortname);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(12f);
        circularProgressDrawable.setCenterRadius(50);
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                circularProgressDrawable.setColorSchemeColors(this.getResources().getColor(R.color.darkDray));
            } else if (MainActivity.themeKEY.equals("0")) {
                circularProgressDrawable.setColorSchemeColors(this.getResources().getColor(R.color.yellow));
            }
        } else {
            circularProgressDrawable.setColorSchemeColors(this.getResources().getColor(R.color.yellow));
        }
        circularProgressDrawable.start();

        if (is_image.equals("0")) {
            Glide.with(Detail_Activity.this).load(getString(R.string.server_url) + "uploads/thumbnail/" + image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail);
            img_playD.setVisibility(View.VISIBLE);
        }
        if (is_image.equals("1")) {
            Glide.with(Detail_Activity.this).load(getString(R.string.server_url) + "uploads/thumbnail/" + image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail);
            img_playD.setVisibility(View.GONE);
        }
        if (is_image.equals("2")) {
            Glide.with(Detail_Activity.this).load(image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail);
            img_playurl.setVisibility(View.VISIBLE);
        }

        tx_shortdesc2.setText(Html.fromHtml(short_desc));
        tx_longdesc2.setMovementMethod(LinkMovementMethod.getInstance());
        tx_longdesc2.setText(Text);
        txtNewsDate2.setText(news_date);
        txtRefName2.setText(shortname);

        if (is_image.equals("0")) {
            Glide.with(Detail_Activity.this).load(getString(R.string.server_url) + "uploads/thumbnail/" + image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail2);
            img_playD.setVisibility(View.VISIBLE);
        }
        if (is_image.equals("1")) {
            Glide.with(Detail_Activity.this).load(getString(R.string.server_url) + "uploads/thumbnail/" + image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail2);
            img_playD.setVisibility(View.GONE);
        }
        if (is_image.equals("2")) {
            Glide.with(Detail_Activity.this).load(image).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(img_detail2);
            img_playurl.setVisibility(View.VISIBLE);
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bmp = ConvertUrlToBitmap(getString(R.string.server_url) + "uploads/thumbnail/" + image);
                takeScreenshot();
                if (is_image.equals("0")) {
                    shareImageNews();
                } else if (is_image.equals("1")) {
                    shareImageNews();
                } else if (is_image.equals("2")) {
                    shareYoutubeLinkNews(medialink);
                }
            }
        });

        String image_ = getString(R.string.server_url) + "uploads/thumbnail/" + image;

        if (img_playD.getVisibility() == View.VISIBLE || img_playurl.getVisibility() == View.VISIBLE) {
            img_detail.setEnabled(false);
        } else {
            img_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Detail_Activity.this, FullImageActivity.class);
                    intent.putExtra("image", image_);
                    intent.putExtra("newsTitle", short_desc);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) Detail_Activity.this,
                                    view,
                                    ViewCompat.getTransitionName(img_detail));
                    Detail_Activity.this.startActivity(intent, options.toBundle());
                }
            });
        }

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());

                } else {
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {

                btn_play.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);

            }

            @Override
            public void onError(String s) {

            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_pause.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.INVISIBLE);

                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        btn_pause.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                    } else if (MainActivity.themeKEY.equals("0")) {
                        btn_pause.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    btn_pause.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                speakOut();

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_play.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                tts.stop();

            }
        });

        img_Addbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBookmark();

            }
        });

        img_Deletebookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteData();
            }
        });

        img_playD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_Activity.this, VideoPlayer_Activity.class);
                intent.putExtra("localVideo", media);
                startActivity(intent);
                Detail_Activity.this.overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);
            }
        });

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isSearch = "no";
                Intent intent = new Intent(Detail_Activity.this, CommentActivity.class);
                intent.putExtra("news_id", news_id);
                SPmanager.saveValue(Detail_Activity.this, "news_id", news_id);
                startActivity(intent);
            }
        });

        img_playurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_Activity.this, VideoPlayer_Activity.class);
                intent.putExtra("mediaLink", medialink);
                startActivity(intent);
                Detail_Activity.this.overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);
            }
        });

        tx_longdesc.setOnTouchListener(new OnSwipeTouchListener(Detail_Activity.this) {
            public void onSwipeRight() {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                onBackPressed();
            }

        });

        scrollView.setOnTouchListener(new OnSwipeTouchListener(Detail_Activity.this) {
            public void onSwipeRight() {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                onBackPressed();
            }
        });

        linReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_Activity.this, ReferenceActivity.class);
                intent.putExtra("strReference", reference);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);

            }
        });

    }

    private void shareYoutubeLinkNews(String medialink) {
        Uri uri = (Uri) getLocalBitmapUri(bitmap__);
        Intent sharingImage = new Intent(Intent.ACTION_SEND);
        sharingImage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingImage.setType("image/png");
        sharingImage.putExtra(Intent.EXTRA_STREAM, uri);
        sharingImage.putExtra(Intent.EXTRA_TEXT, "Watch on youtube :" + "\n" + medialink + "\n\n" + "FAST WAY OF GETTING UPDATE :" + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        sharingImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingImage, "Share news using"));

    }

    private void shareImageNews() {
        try {
            shareScrenshot(bitmap__);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public Bitmap takeScreenshot() {

        bitmap__ = Bitmap.createBitmap(
                scrollView2.getChildAt(0).getWidth(),
                scrollView2.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap__);
        scrollView2.getChildAt(0).draw(c);
        return bitmap__;

    }

    private void shareScrenshot(Bitmap bitmap__) {
        Uri uri = (Uri) getLocalBitmapUri(bitmap__);
        Intent sharingImage = new Intent(Intent.ACTION_SEND);
        sharingImage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        sharingImage.setType("image/png");
        sharingImage.putExtra(Intent.EXTRA_STREAM, uri);
        sharingImage.putExtra(Intent.EXTRA_TEXT, "\n\n" + "FAST WAY OF GETTING UPDATE :" + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());

        sharingImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingImage, "Share news using"));

    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(Detail_Activity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void speakOut() {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                final String keyword = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onDone(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_pause.setVisibility(View.GONE);
                        btn_play.setVisibility(View.VISIBLE);

                    }
                });
            }

            @Override
            public void onError(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        String text = String.valueOf(Html.fromHtml(long_desc));
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "");

    }

    private void showdb() {
        try {
            sqliteHelper = new SqliteHelper(Detail_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + news_id + "';", null);

            if (cur.moveToFirst()) {

                img_Deletebookmark.setVisibility(View.VISIBLE);
                img_Addbookmark.setVisibility(View.GONE);

                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else if (MainActivity.themeKEY.equals("0")) {
                        img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }

                cur.close();
                db1.close();

            } else {
                img_Deletebookmark.setVisibility(View.GONE);
                img_Addbookmark.setVisibility(View.VISIBLE);

                cur.close();
                db1.close();
            }

        } catch (Exception e) {

            e.getMessage();
            e.printStackTrace();
        }
    }

    private void addBookmark() {

        try {
            sqliteHelper = new SqliteHelper(Detail_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + news_id + "';", null);

            if (cur.moveToFirst()) {

//                Toast.makeText(MainActivity.this, "Already Added !", Toast.LENGTH_SHORT).show();
                deleteData();
                cur.close();
                db1.close();

            } else {

                insertData();
                cur.close();
                db1.close();
            }

        } catch (Exception e) {

            e.getMessage();
            e.printStackTrace();
        }

    }

    private void deleteData() {
        sqliteHelper = new SqliteHelper(Detail_Activity.this);
        SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
        db1.execSQL("DELETE FROM bookmark Where newsID ='" + news_id + "';");
        db1.close();
        img_Deletebookmark.setVisibility(View.GONE);
        img_Addbookmark.setVisibility(View.VISIBLE);

    }

    private void insertData() {
        try {
            sqliteHelper = new SqliteHelper(Detail_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();

            ContentValues insertValues = new ContentValues();

            insertValues.put("newsID", news_id);
            insertValues.put("short_desc", short_desc);
            insertValues.put("long_desc", long_desc);
            insertValues.put("image", image);
            insertValues.put("medialink", medialink);
            insertValues.put("media", media);
            insertValues.put("is_image", is_image);
            insertValues.put("news_date", news_date);
            insertValues.put("reference", reference);
            insertValues.put("short_name", shortname);

            db1.insert("bookmark", null, insertValues);
            db1.close();
            img_Deletebookmark.setVisibility(View.VISIBLE);
            img_Addbookmark.setVisibility(View.GONE);

            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {
                    img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else if (MainActivity.themeKEY.equals("0")) {
                    img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(Detail_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            Log.e(TAG, "insertData: " + medialink);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("table Add bookmark....", "onMinusClicked: " + e.getMessage());
        }
    }

    private void supportLan(String description) {
        final FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();

        try {
            languageIdentifier.identifyLanguage(String.valueOf(Html.fromHtml(description)))
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    if (languageCode != "und") {

                                        if (Arrays.asList(codelist).contains(languageCode)) {
                                            relay_tts.setVisibility(View.VISIBLE);
                                            btn_play.setVisibility(View.VISIBLE);
                                        } else {
                                            relay_tts.setVisibility(View.GONE);
                                            btn_play.setVisibility(View.GONE);
                                        }

                                        Log.e("", "Language: " + languageCode);

                                    } else {
                                        Log.e("", "Can't identify language.");
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
// Model couldn’t be loaded or other internal error.
// ...
                                    Log.e(TAG, "onFailure: " + e.getMessage());
                                }
                            });
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "supportLan: " + e.getMessage());
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
            mInterstitialAd.show(Detail_Activity.this);
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


    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        super.onDestroy();
    }
}
