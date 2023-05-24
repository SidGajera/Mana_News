package com.mananews.apandts.Activity;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.mananews.apandts.Adapter.ViewPagerAdapter;
import com.mananews.apandts.Model_Class.Model_News;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.DepthTransformation;
import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.utils.SqliteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Search_Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "Search Activity";
    private SearchView searchView;
    private TextView tx_noSearch, tx_noData, textView;
    private ImageView btn_playS, btn_pauseS, img_AddbookmarkS, img_DeletebookmarkS, imgBack, imgCancle, imgComment;
    private ProgressBar progressS, progressS2;
    public static int noOfrecords = 10;
    ArrayList<Model_News> searchList = new ArrayList<>();
    private TextToSpeech tts;
    public static String tagName;
    private SqliteHelper sqliteHelper;
    private RelativeLayout relay_full, lay_btnplay, lay_bottoMM, layBookmark;
    private String ad_type, bann_ads, tag, visibility;
    private LinearLayout lay_headerS;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    String[] codelist = new String[]{"ar", "zh", "cs", "da", "nl", "en", "fi",
            "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", "no",
            "pl", "pt", "ro", "ru", "sk", "es", "sv", "th", "tr", "te", "mr"};

    private Typeface cerebrisans_regular;
    private int id, pos, counterPageScroll, pageNo = 1;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);


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



        getSupportActionBar().hide();
        statusBarColor();
        init();
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                lay_headerS.setBackgroundColor(getResources().getColor(R.color.darkDray));
                relay_full.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                tx_noSearch.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                tx_noData.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                layBookmark.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                searchView.setBackgroundColor(getResources().getColor(R.color.white));
                tx_noSearch.setTextColor(getResources().getColor(R.color.gray));
                tx_noData.setTextColor(getResources().getColor(R.color.gray));
                tx_noData.setBackgroundColor(getResources().getColor(R.color.pagecolor));
                searchView.setBackgroundResource(R.drawable.seachview_bg);
                progressS2.setVisibility(View.VISIBLE);
                progressS.setVisibility(View.GONE);
                imgCancle.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgBack.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (MainActivity.themeKEY.equals("0")) {
                tx_noSearch.setBackgroundColor(getResources().getColor(R.color.darkDray));
                relay_full.setBackgroundColor(getResources().getColor(R.color.darkDray));
                tx_noData.setBackgroundColor(getResources().getColor(R.color.darkDray));
                tx_noSearch.setTextColor(getResources().getColor(R.color.darkDray));
                tx_noData.setTextColor(getResources().getColor(R.color.darkDray));
                progressS.setVisibility(View.VISIBLE);
                progressS2.setVisibility(View.GONE);
                tx_noData.setTextColor(getResources().getColor(R.color.white));
                tx_noSearch.setTextColor(getResources().getColor(R.color.white));
                lay_bottoMM.setBackgroundColor(getResources().getColor(R.color.gray));
                imgCancle.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgBack.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        } else {
            lay_headerS.setBackgroundColor(getResources().getColor(R.color.yellow));
            lay_bottoMM.setBackgroundColor(getResources().getColor(R.color.gray));
            relay_full.setBackgroundColor(getResources().getColor(R.color.darkDray));
            tx_noSearch.setBackgroundColor(getResources().getColor(R.color.darkDray));
            tx_noData.setBackgroundColor(getResources().getColor(R.color.darkDray));
            tx_noSearch.setTextColor(getResources().getColor(R.color.white));
            tx_noData.setTextColor(getResources().getColor(R.color.white));
            searchView.setBackgroundResource(R.drawable.round_button);
            progressS.setVisibility(View.VISIBLE);
            progressS2.setVisibility(View.GONE);
            tx_noData.setTextColor(getResources().getColor(R.color.white));
            tx_noSearch.setTextColor(getResources().getColor(R.color.white));
            imgCancle.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgBack.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if (getString(R.string.tts_visibility).equals("yes")) {
            lay_btnplay.setVisibility(View.VISIBLE);
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {
                    btn_playS.setVisibility(View.VISIBLE);
                    btn_playS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (MainActivity.themeKEY.equals("0")) {
                    btn_playS.setVisibility(View.VISIBLE);
                    btn_playS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

                }
            } else {
                btn_playS.setVisibility(View.VISIBLE);
                btn_playS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        } else {
            lay_btnplay.setVisibility(View.GONE);
        }


    }



    private void init() {
        viewPager2 = findViewById(R.id.viewPager2Search);

        searchView = findViewById(R.id.searchView);
        imgCancle = findViewById(R.id.imgCancle);
        imgBack = findViewById(R.id.imgBack);
        imgComment = findViewById(R.id.imgComment);
        btn_playS = findViewById(R.id.btn_playS);
        btn_pauseS = findViewById(R.id.btn_pauseS);
        img_AddbookmarkS = findViewById(R.id.img_AddbookmarkS);
        img_DeletebookmarkS = findViewById(R.id.img_DeletebookmarkS);
        tx_noSearch = findViewById(R.id.tx_noSearch);
        progressS = findViewById(R.id.progressS);
        progressS2 = findViewById(R.id.progressS2);
        tx_noData = findViewById(R.id.tx_noData);
        relay_full = findViewById(R.id.relay_full);
        lay_btnplay = findViewById(R.id.lay_btnplay);

        lay_headerS = findViewById(R.id.lay_headerS);
        lay_bottoMM = findViewById(R.id.lay_bottoMM);
        layBookmark = findViewById(R.id.layBookmark_);
        cerebrisans_regular = Typeface.createFromAsset(getAssets(), "cerebrisans_regular.ttf");

        searchView.clearFocus();
        if (!searchView.isFocused()) {
            searchView.clearFocus();

        }

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        id = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {

                textView = (TextView) searchView.findViewById(id);
                textView.setTextColor(getResources().getColor(R.color.darkDray));
                textView.setHintTextColor(getResources().getColor(R.color.darkDray));
            } else if (MainActivity.themeKEY.equals("0")) {

                textView = (TextView) searchView.findViewById(id);
                textView.setTextColor(Color.WHITE);
                textView.setHintTextColor(Color.WHITE);
            }
        } else {
            textView = (TextView) searchView.findViewById(id);
            textView.setTextColor(Color.WHITE);
            textView.setHintTextColor(Color.WHITE);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setTypeface(cerebrisans_regular);

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);

        }

        initTTS();
        viewPager2.setPageTransformer(new DepthTransformation());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                pos = position;
                try {
                    if (position == searchList.size() - 1 && (int) positionOffset == 0 /*&& !isLastPageSwiped*/) {
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

                pos = position;
                try {
                    supportLan(searchList.get(pos).getDescription());
                    btn_pauseS.setVisibility(View.GONE);
                    showdb();

                    Log.e("Selected_Page", String.valueOf(position));
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onPageSelected: " + e.getMessage());
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                try {
                    supportLan(searchList.get(pos).getDescription());

                    btn_pauseS.setVisibility(View.GONE);
                    tts.stop();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                finish();
                onBackPressed();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Search_Activity.this, CommentActivity.class);
                        intent.putExtra("news_id", searchList.get(pos).getId1());
                        SPmanager.saveValue(Search_Activity.this, "news_id", searchList.get(pos).getId1());
                        startActivity(intent);
                    }
                });
            }
        });

        btn_playS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_pauseS.setVisibility(View.VISIBLE);
                btn_playS.setVisibility(View.INVISIBLE);

                try {
                    String speak = searchList.get(pos).getDescription().toString();
                    tts.speak(String.valueOf(Html.fromHtml(speak)), QUEUE_ADD, null);
                    speakOut();

                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onClick: " + e.getMessage());
                }


                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        btn_pauseS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                    } else if (MainActivity.themeKEY.equals("0")) {
                        btn_pauseS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    btn_pauseS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        btn_pauseS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_playS.setVisibility(View.VISIBLE);
                btn_pauseS.setVisibility(View.INVISIBLE);
                tts.stop();

            }
        });

        img_AddbookmarkS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBookmark();

            }
        });

        img_DeletebookmarkS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteData();

            }
        });
    }

    private void initTTS() {
        tts = new TextToSpeech(Search_Activity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.getDefault());

                } else {

                    Toast.makeText(Search_Activity.this, "Language Not Suppourted !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void speakOut() {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                final String keyword = s;
                Search_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onDone(String s) {

                Search_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                        btn_pauseS.setVisibility(View.GONE);
                        btn_playS.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(String s) {

                Search_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

    }

    private void addBookmark() {
        try {
            sqliteHelper = new SqliteHelper(Search_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + searchList.get(pos).getId1() + "';", null);

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
            sqliteHelper = new SqliteHelper(Search_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();

            ContentValues insertValues = new ContentValues();
            insertValues.put("newsID", searchList.get(pos).getId1());
            insertValues.put("short_desc", searchList.get(pos).getShort_desc());
            insertValues.put("long_desc", searchList.get(pos).getDescription());
            insertValues.put("image", searchList.get(pos).getImage());
            insertValues.put("medialink", searchList.get(pos).getMediaLink());
            insertValues.put("media", searchList.get(pos).getMedia());
            insertValues.put("is_image", searchList.get(pos).getIs_image());
            insertValues.put("news_date", searchList.get(pos).getNews_date());
            insertValues.put("reference", searchList.get(pos).getReference());
            insertValues.put("short_name", searchList.get(pos).getShortname());

            Log.e(TAG, "insertData: " + searchList.get(pos).getShort_desc());

            db1.insert("bookmark", null, insertValues);
            db1.close();
            img_DeletebookmarkS.setVisibility(View.VISIBLE);
            img_AddbookmarkS.setVisibility(View.GONE);

            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {
                    layBookmark.setBackgroundColor(getResources().getColor(R.color.footercolor));
                    img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else if (MainActivity.themeKEY.equals("0")) {
                    img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_AddbookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    layBookmark.setBackgroundColor(getResources().getColor(R.color.gray));

                }
            } else {
                layBookmark.setBackgroundColor(getResources().getColor(R.color.gray));
                img_DeletebookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_AddbookmarkS.setColorFilter(ContextCompat.getColor(Search_Activity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            }

            Log.e(TAG, "insertData: " + searchList.get(pos).getMediaLink());

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("table Add bookmark....", "onMinusClicked: " + e.getMessage());
        }

    }

    private void deleteData() {

        sqliteHelper = new SqliteHelper(Search_Activity.this);
        SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
        db1.execSQL("DELETE FROM bookmark Where newsID ='" + searchList.get(pos).getId1() + "';");
        db1.close();
        img_DeletebookmarkS.setVisibility(View.GONE);
        img_AddbookmarkS.setVisibility(View.VISIBLE);

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
                                        btn_playS.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_playS.setVisibility(View.INVISIBLE);
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
            sqliteHelper = new SqliteHelper(Search_Activity.this);
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + searchList.get(pos).getId1() + "';", null);

            if (cur.moveToFirst()) {

                img_DeletebookmarkS.setVisibility(View.VISIBLE);
                img_AddbookmarkS.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Already Added !", Toast.LENGTH_SHORT).show();

                cur.close();
                db1.close();

            } else {
                img_DeletebookmarkS.setVisibility(View.GONE);
                img_AddbookmarkS.setVisibility(View.VISIBLE);

                cur.close();
                db1.close();
            }

        } catch (Exception e) {

            e.getMessage();
            e.printStackTrace();
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

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (searchList.size() != 0) {
            searchList.clear();
            tx_noSearch.setVisibility(View.GONE);

        }
        if (searchList.size() == 0) {
            tagName = query;
            get_searchData(tagName);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void get_searchData(String tagName) {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressS2.setVisibility(View.VISIBLE);
                progressS.setVisibility(View.GONE);
            } else if (MainActivity.themeKEY.equals("0")) {
                progressS.setVisibility(View.VISIBLE);
                progressS2.setVisibility(View.GONE);
            } else {
                progressS.setVisibility(View.VISIBLE);
                progressS2.setVisibility(View.GONE);
            }
        } else {
            progressS.setVisibility(View.VISIBLE);
            progressS2.setVisibility(View.GONE);
        }

        tx_noData.setVisibility(View.GONE);
        tx_noSearch.setVisibility(View.GONE);
        lay_bottoMM.setVisibility(View.GONE);

        String url = getString(R.string.server_url) + "webservices/searchdetail.php?tag=" + tagName + "&page=" + pageNo + "&pp=" + noOfrecords;
        Log.e(TAG, "get_searchData: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(Search_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        relay_full.setVisibility(View.VISIBLE);
                        tx_noData.setVisibility(View.GONE);

                        JSONObject resp = jsonObject.getJSONObject("response");
                        JSONObject data = resp.getJSONObject("data");
                        JSONArray news = data.getJSONArray("news");

                        for (int i = 0; i < news.length(); i++) {

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

                            Model_News model_class = new Model_News();
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
                            searchList.add(model_class);

                        }
                        relay_full.setVisibility(View.VISIBLE);
                        progressS.setVisibility(View.GONE);
                        progressS2.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setui();
                                lay_bottoMM.setVisibility(View.VISIBLE);
                                btn_playS.setVisibility(View.VISIBLE);
                                progressS.setVisibility(View.GONE);
                                progressS2.setVisibility(View.GONE);
                            }
                        }, 200);

                    } else if (status.equals("404")) {
                        tx_noSearch.setVisibility(View.GONE);
                        tx_noData.setVisibility(View.VISIBLE);
                        progressS.setVisibility(View.GONE);
                        progressS2.setVisibility(View.GONE);
                        relay_full.setVisibility(View.GONE);
                        lay_bottoMM.setVisibility(View.GONE);
                    } else {
                        tx_noSearch.setVisibility(View.GONE);
                        progressS.setVisibility(View.GONE);
                        progressS2.setVisibility(View.GONE);
                        tx_noData.setVisibility(View.VISIBLE);
                        relay_full.setVisibility(View.GONE);
                        lay_bottoMM.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    progressS.setVisibility(View.GONE);
                    progressS2.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void setui() {
        if (searchList.size() != 0) {
            lay_bottoMM.setVisibility(View.VISIBLE);
            viewPagerAdapter = new ViewPagerAdapter(Search_Activity.this, searchList, viewPager2);
            viewPager2.setAdapter(viewPagerAdapter);

            try {
                supportLan(searchList.get(pos).getDescription());
                showdb();
            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG, "setui: " + e.getMessage());
            }

        } else {
            lay_bottoMM.setVisibility(View.GONE);
        }
    }

    private void loadMore() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressS2.setVisibility(View.VISIBLE);
            } else if (MainActivity.themeKEY.equals("0")) {
                progressS.setVisibility(View.VISIBLE);
            }
        } else {
            progressS.setVisibility(View.VISIBLE);
        }
        String url = getString(R.string.server_url) + "webservices/searchdetail.php?tag=" + tagName + "&page=" + pageNo + "&pp=" + noOfrecords;
        Log.e(TAG, "get_searchData: " + url);
        final ArrayList<Model_News> dataList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(Search_Activity.this);
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
                            Toast.makeText(Search_Activity.this, "No more News !", Toast.LENGTH_SHORT).show();
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
                        progressS.setVisibility(View.GONE);
                        progressS2.setVisibility(View.GONE);

                        if (dataList.size() != 0) {
                            Log.e("adapter", "" + dataList.size());
                            viewPagerAdapter.addItem(dataList, searchList.size());
                        }

                    } else {

                        if (searchList.size() != -1) {
                            progressS.setVisibility(View.GONE);
                            progressS2.setVisibility(View.GONE);
                        }

                    }
                } catch (JSONException e) {
                    progressS.setVisibility(View.GONE);
                    progressS2.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressS.setVisibility(View.GONE);
                progressS2.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        });
        requestQueue.add(stringRequest);
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
            btn_pauseS.setVisibility(View.GONE);
            btn_playS.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.counter = MainActivity.counter + 1;
        if (mInterstitialAd != null) {
            MainActivity.counter = 0;
            mInterstitialAd.show(Search_Activity.this);
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
