package com.mananews.apandts.Activity;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.mananews.apandts.Adapter.ViewPagerAdapter;
import com.mananews.apandts.Model_Class.Model_News;
import com.mananews.apandts.utils.DepthTransformation;
import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.utils.SqliteHelper;
import com.mananews.apandts.R;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CategoriesActivity extends AppCompatActivity {

    private static final String TAG = "CategoriesActivity";
    private ProgressBar mainProgress, mainProgressC2;
    private String cat_id, cat_name, long_desc, ad_type, bann_ads, visibility;
    private ImageView imgBack, btn_playC, btn_pauseC, img_AddbookmarkC, img_DeletebookmarkC, imgComment;
    private RelativeLayout laybottoM, lay_btnplayC, layBookMarkC, relAd, relay_full, relay_adviewC;
    private LinearLayout lay_drawerCat;
    ArrayList<Model_News> catNewsList = new ArrayList<>();
    private int pageNo = 1;
    public static int noOfrecords = 10;
    private TextView tx_nodataC, txtCatName;
    private int counterPageScroll;
    private TextToSpeech tts;
    private SqliteHelper sqliteHelper;
    String[] codelist = new String[]{"ar", "zh", "cs", "da", "nl", "en", "fi",
            "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", "no",
            "pl", "pt", "ro", "ru", "sk", "es", "sv", "th", "tr", "te", "mr"};
    private LinearLayout ll_ads;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    private ViewPager2 viewPager2;
    private int pos;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getSupportActionBar().hide();


        statusBarColor();
        getIntents();
        init();
        getCatData();
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                layBookMarkC.setBackgroundColor(getResources().getColor(R.color.footercolor));
                lay_drawerCat.setBackgroundColor(getResources().getColor(R.color.footercolor));
                relay_full.setBackgroundColor(getResources().getColor(R.color.white));
                mainProgressC2.setVisibility(View.VISIBLE);
                img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (MainActivity.themeKEY.equals("0")) {
                layBookMarkC.setBackgroundColor(getResources().getColor(R.color.gray));
                lay_drawerCat.setBackgroundColor(getResources().getColor(R.color.yellow));
                relay_full.setBackgroundColor(getResources().getColor(R.color.darkDray));
                img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                mainProgress.setVisibility(View.VISIBLE);
            }
        } else {
            layBookMarkC.setBackgroundColor(getResources().getColor(R.color.gray));
            lay_drawerCat.setBackgroundColor(getResources().getColor(R.color.yellow));
            relay_full.setBackgroundColor(getResources().getColor(R.color.darkDray));
            img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgComment.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            mainProgress.setVisibility(View.VISIBLE);
        }

        if (getString(R.string.tts_visibility).equals("yes")) {
            lay_btnplayC.setVisibility(View.VISIBLE);
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {

                    btn_playC.setVisibility(View.VISIBLE);
                    btn_playC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (MainActivity.themeKEY.equals("0")) {
                    btn_playC.setVisibility(View.VISIBLE);
                    btn_playC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                btn_playC.setVisibility(View.VISIBLE);
                btn_playC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            lay_btnplayC.setVisibility(View.GONE);
        }
    }

    private void getIntents() {
        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        cat_name = intent.getStringExtra("cat_name");
    }

    private void init() {
        viewPager2 = findViewById(R.id.viewPager2Cat);
        mainProgress = findViewById(R.id.progressC);
        mainProgressC2 = findViewById(R.id.progressC2);
        imgBack = findViewById(R.id.imgBack);
        laybottoM = findViewById(R.id.laybottoM);
        lay_btnplayC = findViewById(R.id.lay_btnplayC);
        txtCatName = findViewById(R.id.txtCatName);
        btn_playC = findViewById(R.id.btn_playC);
        btn_pauseC = findViewById(R.id.btn_pauseC);
        tx_nodataC = findViewById(R.id.tx_nodataC);
        img_AddbookmarkC = findViewById(R.id.img_AddbookmarkC);
        img_DeletebookmarkC = findViewById(R.id.img_DeletebookmarkC);
        lay_drawerCat = findViewById(R.id.lay_drawerCat);
        layBookMarkC = findViewById(R.id.layBookMarkC);
        relAd = findViewById(R.id.relAd);
        relay_adviewC = findViewById(R.id.relay_adviewC);
        relay_full = findViewById(R.id.relay_full);
        imgComment = findViewById(R.id.imgComment);

        txtCatName.setText(cat_name);
        initTTS();

        viewPager2.setCurrentItem(pos);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                pos = position;
                try {
                    if (position == catNewsList.size() - 1 && (int) positionOffset == 0 /*&& !isLastPageSwiped*/) {
                        if (counterPageScroll != 0) {
                            pageNo = pageNo + 1;
                            loadMore();

                        }
                        counterPageScroll++;
                    } else {
                        counterPageScroll = 0;
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onPageScrolled: " + e.getMessage());
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                try {
                    pos = position;

                    supportLan(catNewsList.get(pos).getDescription());
                    btn_pauseC.setVisibility(View.GONE);
                    showdb();
                    Log.e("Selected_Page", String.valueOf(position));
                } catch (Exception e) {
                    e.getMessage();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                try {
                    supportLan(catNewsList.get(pos).getDescription());
                    btn_pauseC.setVisibility(View.GONE);
                    tts.stop();
                } catch (Exception e) {
                    e.getMessage();

                }

            }
        });

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesActivity.this, CommentActivity.class);
                intent.putExtra("news_id", catNewsList.get(pos).getId1());
                SPmanager.saveValue(CategoriesActivity.this, "news_id", catNewsList.get(pos).getId1());
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_AddbookmarkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookmark();
            }
        });

        img_DeletebookmarkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        btn_playC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_pauseC.setVisibility(View.VISIBLE);
                btn_playC.setVisibility(View.INVISIBLE);

                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        btn_pauseC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                    } else if (MainActivity.themeKEY.equals("0")) {
                        btn_pauseC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    btn_pauseC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                String speak = catNewsList.get(pos).getDescription().toString();
                tts.speak(String.valueOf(Html.fromHtml(speak)), QUEUE_ADD, null);
                speakOut();

            }
        });

        btn_pauseC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_playC.setVisibility(View.VISIBLE);
                btn_pauseC.setVisibility(View.INVISIBLE);
                tts.stop();

            }
        });
    }

    private void initTTS() {
        tts = new TextToSpeech(CategoriesActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

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
                        btn_pauseC.setVisibility(View.INVISIBLE);
                        btn_playC.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CategoriesActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void addBookmark() {
        try {
            sqliteHelper = new SqliteHelper(CategoriesActivity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + catNewsList.get(pos).getId1() + "';", null);
            Log.e(TAG, "addBookmark: " + catNewsList.get(pos).getId1());
            if (cur.moveToFirst()) {
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

    private void insertData() {
        try {
            sqliteHelper = new SqliteHelper(CategoriesActivity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();

            ContentValues insertValues = new ContentValues();
            insertValues.put("newsID", catNewsList.get(pos).getId1());
            insertValues.put("short_desc", catNewsList.get(pos).getShort_desc());
            insertValues.put("long_desc", catNewsList.get(pos).getDescription());
            insertValues.put("image", catNewsList.get(pos).getImage());
            insertValues.put("medialink", catNewsList.get(pos).getMediaLink());
            insertValues.put("media", catNewsList.get(pos).getMedia());
            insertValues.put("is_image", catNewsList.get(pos).getIs_image());
            insertValues.put("news_date", catNewsList.get(pos).getNews_date());
            insertValues.put("reference", catNewsList.get(pos).getReference());
            insertValues.put("short_name", catNewsList.get(pos).getShortname());

            db1.insert("bookmark", null, insertValues);
            db1.close();
            img_DeletebookmarkC.setVisibility(View.VISIBLE);
            img_AddbookmarkC.setVisibility(View.GONE);

            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {
                    img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else if (MainActivity.themeKEY.equals("0")) {
                    img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            Log.e(TAG, "insertData: " + catNewsList.get(pos).getMediaLink());

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("table Add bookmark....", "onMinusClicked: " + e.getMessage());
        }
    }

    private void deleteData() {
        sqliteHelper = new SqliteHelper(CategoriesActivity.this);
        SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
        db1.execSQL("DELETE FROM bookmark Where newsID ='" + catNewsList.get(pos).getId1() + "';");
        db1.close();
        img_DeletebookmarkC.setVisibility(View.GONE);
        img_AddbookmarkC.setVisibility(View.VISIBLE);
    }

    private void supportLan(String description) {
        final FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();

        languageIdentifier.identifyLanguage(String.valueOf(Html.fromHtml(description)))
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != "und") {

                                    if (Arrays.asList(codelist).contains(languageCode)) {
                                        btn_playC.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_playC.setVisibility(View.INVISIBLE);
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

                                Log.e(TAG, "onFailure: " + e.getMessage());
                            }
                        });

    }

    private void showdb() {
        try {
            sqliteHelper = new SqliteHelper(CategoriesActivity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + catNewsList.get(pos).getId1() + "';", null);

            if (cur.moveToFirst()) {

                img_DeletebookmarkC.setVisibility(View.VISIBLE);
                img_AddbookmarkC.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Already Added !", Toast.LENGTH_SHORT).show();

                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else if (MainActivity.themeKEY.equals("0")) {
                        img_DeletebookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_AddbookmarkC.setColorFilter(ContextCompat.getColor(CategoriesActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }

                cur.close();
                db1.close();

            } else {

                img_DeletebookmarkC.setVisibility(View.GONE);
                img_AddbookmarkC.setVisibility(View.VISIBLE);

                cur.close();
                db1.close();
            }
        } catch (Exception e) {

            e.getMessage();
            e.printStackTrace();
        }

    }

    private void getCatData() {

        mainProgress.setVisibility(View.VISIBLE);
        laybottoM.setVisibility(View.GONE);

        String url = getString(R.string.server_url) + "webservices/category-wise-news.php?id=" + cat_id + "&page=" + pageNo + "&pp=" + noOfrecords;
        Log.e(TAG, "getData: " + url);
        RequestQueue requestQueue = Volley.newRequestQueue(CategoriesActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);
                catNewsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        JSONObject resp = jsonObject.getJSONObject("response");

                        JSONObject data = resp.getJSONObject("data");
                        JSONArray news = data.getJSONArray("news");
                        for (int i = 0; i < news.length(); i++) {

                            Model_News model_class = new Model_News();
                            JSONObject jsonObject1 = news.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String cat_id = jsonObject1.getString("cat_id");
                            String thumbnail = jsonObject1.getString("thumbnail");
                            String short_desc = jsonObject1.getString("short_desc");
                            long_desc = jsonObject1.getString("long_desc");
                            String media = jsonObject1.getString("media");
                            String mediaLink = jsonObject1.getString("medialink");
                            String is_image = jsonObject1.getString("is_image");
                            String news_date = jsonObject1.getString("news_date");
                            String reference = jsonObject1.getString("reference");
                            String shortname = jsonObject1.getString("shortname");

                            model_class.setId1(id);
                            model_class.setCat_id(cat_id);
                            model_class.setImage(thumbnail);
                            model_class.setShort_desc(short_desc);
                            model_class.setDescription(long_desc);
                            model_class.setMedia(media);
                            model_class.setMediaLink(mediaLink);
                            model_class.setIs_image(is_image);
                            model_class.setNews_date(news_date);
                            model_class.setReference(reference);
                            model_class.setShortname(shortname);
                            catNewsList.add(model_class);

                        }
                        setui();
                        laybottoM.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_playC.setVisibility(View.VISIBLE);
                                mainProgress.setVisibility(View.GONE);
                                mainProgressC2.setVisibility(View.GONE);
                            }
                        }, 200);

                    } else {
                        tx_nodataC.setVisibility(View.VISIBLE);
                        mainProgress.setVisibility(View.GONE);
                        mainProgressC2.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    mainProgress.setVisibility(View.GONE);
                    mainProgressC2.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                mainProgress.setVisibility(View.GONE);
                mainProgressC2.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);

    }

    private void setui() {

        viewPagerAdapter = new ViewPagerAdapter(CategoriesActivity.this, catNewsList, viewPager2);
        viewPager2.setAdapter(viewPagerAdapter);
//        viewPager2.setAdapter(new ViewPagerAdapter(getActivity(), imageList, viewPager2));

        BookFlipPageTransformer2 bookFlipPageTransformer = new BookFlipPageTransformer2();
// Enable / Disable scaling while flipping. If true, then next page will scale in (zoom in). By default, its true.
        bookFlipPageTransformer.setEnableScale(true);
// The amount of scale the page will zoom. By default, its 5 percent.
        bookFlipPageTransformer.setScaleAmountPercent(5f);
// Assign the page transformer to the ViewPager2.
        viewPager2.setPageTransformer(new DepthTransformation());


        try {
            supportLan(catNewsList.get(pos).getDescription());
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "setui: " + e.getMessage());
        }
        showdb();
    }

    private void loadMore() {

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                mainProgressC2.setVisibility(View.VISIBLE);
            } else if (MainActivity.themeKEY.equals("0")) {
                mainProgress.setVisibility(View.VISIBLE);
            }
        } else {
            mainProgress.setVisibility(View.VISIBLE);
        }
        String url = getString(R.string.server_url) + "webservices/category-wise-news.php?id=" + cat_id + "&page=" + pageNo + "&pp=" + noOfrecords;
        final ArrayList<Model_News> dataList = new ArrayList<>();

        Log.e(TAG, "getjson: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(CategoriesActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        JSONObject resp = jsonObject.getJSONObject("response");
                        JSONObject data = resp.getJSONObject("data");
                        JSONArray news = data.getJSONArray("news");
                        if (news.length() == 0) {
                            Toast.makeText(CategoriesActivity.this, "No more News !", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < news.length(); i++) {

                            Model_News model_news = new Model_News();
                            JSONObject jsonObject1 = news.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String cat_id = jsonObject1.getString("cat_id");
                            String thumbnail = jsonObject1.getString("thumbnail");
                            String short_desc = jsonObject1.getString("short_desc");
                            String long_desc = jsonObject1.getString("long_desc");
                            String media = jsonObject1.getString("media");
                            String mediaLink = jsonObject1.getString("medialink");
                            String is_image = jsonObject1.getString("is_image");
                            String news_date = jsonObject1.getString("news_date");
                            String reference = jsonObject1.getString("reference");
                            String shortname = jsonObject1.getString("shortname");

                            model_news.setId1(id);
                            model_news.setCat_id(cat_id);
                            model_news.setImage(thumbnail);
                            model_news.setShort_desc(short_desc);
                            model_news.setDescription(long_desc);
                            model_news.setMedia(media);
                            model_news.setMediaLink(mediaLink);
                            model_news.setIs_image(is_image);
                            model_news.setNews_date(news_date);
                            model_news.setReference(reference);
                            model_news.setShortname(shortname);
                            dataList.add(model_news);

                        }
                        mainProgress.setVisibility(View.GONE);
                        mainProgressC2.setVisibility(View.GONE);
                        if (dataList.size() != 0) {
                            Log.e("adapter", "" + dataList.size());
//                            viewPagerAdapter.setLoaded();
                            viewPagerAdapter.addItem(dataList, catNewsList.size());
                        }

                    } else {
                        mainProgress.setVisibility(View.GONE);
                        mainProgressC2.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    mainProgress.setVisibility(View.GONE);
                    mainProgressC2.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                mainProgress.setVisibility(View.GONE);
                mainProgressC2.setVisibility(View.GONE);
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
    public void onStop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tts != null) {
            tts.stop();
            btn_pauseC.setVisibility(View.GONE);
            btn_playC.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initTTS();
        if (MainActivity.visibility.equalsIgnoreCase("1")){
            if (MainActivity.counterAPI == MainActivity.counter){
                loadAd();
            }else {
                if ((MainActivity.counterAPI + 1 ) < MainActivity.counter){
                    MainActivity.counter = 1;
                }
            }
        }
        try {
            if (MainActivity.isSearch.equals("yes")) {
                getCatData();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.counter = MainActivity.counter + 1;
        if (mInterstitialAd != null) {
            MainActivity.counter = 0;
            mInterstitialAd.show(CategoriesActivity.this);
        } else {
            Log.e("TAG", "The interstitial ad wasn't ready yet.");
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
                                Log.e("TAG", "The ad was dismissed.");
//                        loadAd();
                                MainActivity.counter = 0;
                                onBackPressed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.e("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.e("TAG", "The ad was shown.");
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