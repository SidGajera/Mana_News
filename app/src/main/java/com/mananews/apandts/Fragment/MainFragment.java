package com.mananews.apandts.Fragment;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.mananews.apandts.Activity.CommentActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Activity.ReporterActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "Fragment Main";
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private ProgressBar mainProgress, mainProgress2;
    private int pageNo = 1, pos;
    public static int noOfrecords = 10;
    ArrayList<Model_News> imageList = new ArrayList<>();
    private ImageView btn_play, btn_pause, img_Addbookmark, img_Deletebookmark, imgComment, trendingnews;
    private TextToSpeech tts;
    private int counterPageScroll;
    private RelativeLayout lay_btnplay, layBookmark, relayMainFrag, relay_Bott;
    private SqliteHelper sqliteHelper;
    String[] codelist = new String[]{"ar", "zh", "cs", "da", "nl", "en", "fi",
            "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", "no",
            "pl", "pt", "ro", "ru", "sk", "es", "sv", "th", "tr", "te", "mr"};
    private TextView tx_noData;
    private String url;
    public static String strSTATEId, strCITYId, mediaLink;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    ViewFlipper viewFlipper;
    private String username, email, password;
    private FrameLayout frameLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String title;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mainProgress = (ProgressBar) view.findViewById(R.id.mainProgress);
        mainProgress2 = (ProgressBar) view.findViewById(R.id.mainProgress2);
        btn_play = (ImageView) view.findViewById(R.id.btn_playy);
        btn_pause = (ImageView) view.findViewById(R.id.btn_pause);
        img_Addbookmark = (ImageView) view.findViewById(R.id.img_Addbookmark);
        img_Deletebookmark = (ImageView) view.findViewById(R.id.img_Deletebookmark);
        lay_btnplay = view.findViewById(R.id.lay_btnplay);
        tx_noData = view.findViewById(R.id.tx_noData);
        layBookmark = view.findViewById(R.id.layBookmark);
        relayMainFrag = view.findViewById(R.id.relayMainFrag);
        viewPager2 = view.findViewById(R.id.viewPager2Main);
        relay_Bott = view.findViewById(R.id.relay_Bott);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        imgComment = view.findViewById(R.id.imgComment);
        trendingnews = view.findViewById(R.id.trendingnews);
        frameLayout = view.findViewById(R.id.frameLayout);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh_items);

        getPreferenceData();
        init();
        getData();
        setAppTheme();

        if (getString(R.string.tts_visibility).equals("yes")) {
            lay_btnplay.setVisibility(View.VISIBLE);
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {

//                    btn_pause.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (MainActivity.themeKEY.equals("0")) {
//                    btn_pause.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
//                btn_pause.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.VISIBLE);
                btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            lay_btnplay.setVisibility(View.GONE);
        }

        return view;

    }

    private void getPreferenceData() {
        password = SPmanager.getPreference(getActivity(), "password");
        username = SPmanager.getPreference(getActivity(), "username");
        email = SPmanager.getPreference(getActivity(), "email");

        Log.e(TAG, "getPreferenceData: " + password + username + email);
    }

    private void init() {


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });


        strSTATEId = SPmanager.getPreference(getActivity(), "strSTATEId");
        if (strSTATEId == null) {
            SPmanager.saveValue(getActivity(), "strSTATEId", "");
            strSTATEId = "";
        }

        strCITYId = SPmanager.getPreference(getActivity(), "strCITYId");
        if (strCITYId == null) {
            SPmanager.saveValue(getActivity(), "strCITYId", "");
            strCITYId = "";
        }

        initTTS();


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                pos = position;
                try {
                    if (position == imageList.size() - 1 && (int) positionOffset == 0 /*&& !isLastPageSwiped*/) {
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
                if (imageList.size() != 0) {
                    try {
                        supportLan(imageList.get(pos).getDescription());
                    } catch (Exception e) {
                        Log.e(TAG, "onPageSelected: " + e.getMessage());
                    }
                }
                btn_pause.setVisibility(View.GONE);
//                btn_play.setVisibility(View.GONE);
                showdb();
                Log.e("Selected_Page", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (imageList.size() != 0) {
                    try {
                        supportLan(imageList.get(pos).getDescription());

//                        if (imageList.size() % 4 == 0) {
//                        if (pos == 2) {
//                            refreshAd();
//                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                btn_pause.setVisibility(View.GONE);
                tts.stop();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_pause.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.INVISIBLE);
                btnPlayTheme();

                try {
                    String speak = imageList.get(pos).getDescription().toString();
                    tts.speak(String.valueOf(Html.fromHtml(speak)), QUEUE_ADD, null);
                    speakOut();

                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onClick: " + e.getMessage());
                }
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_play.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.INVISIBLE);
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

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Fragment fragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.lay_container, fragment).commit();*/

                MainActivity.isSearch = "no";
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("news_id", imageList.get(pos).getId1());
                SPmanager.saveValue(getActivity(), "news_id", imageList.get(pos).getId1());
                startActivity(intent);
            }
        });

        trendingnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                fragmentManager.beginTransaction().replace(R.id.lay_container, fragment).commit();

                /*MainActivity.isSearch = "no";
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("news_id", imageList.get(pos).getId1());
                SPmanager.saveValue(getActivity(), "news_id", imageList.get(pos).getId1());
                startActivity(intent);*/
            }
        });
    }

    private void initTTS() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(getActivity(), "Language Not Suppourted !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAppTheme() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                layBookmark.setBackgroundColor(getResources().getColor(R.color.footercolor));
                mainProgress2.setVisibility(View.VISIBLE);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                trendingnews.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (MainActivity.themeKEY.equals("0")) {
                layBookmark.setBackgroundColor(getResources().getColor(R.color.gray));
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                trendingnews.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

                mainProgress.setVisibility(View.VISIBLE);
            }
        } else {
            layBookmark.setBackgroundColor(getResources().getColor(R.color.gray));
            img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            trendingnews.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            mainProgress.setVisibility(View.VISIBLE);
        }
    }

    private void trendingnews() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (MainActivity.themeKEY.equals("0")) {
                btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }


    private void btnPlayTheme() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (MainActivity.themeKEY.equals("0")) {
                btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
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
                                        btn_play.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_play.setVisibility(View.INVISIBLE);
//                                        btn_pause.setVisibility(View.INVISIBLE);
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

    private void addBookmark() {
        try {
            sqliteHelper = new SqliteHelper(getActivity());
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + imageList.get(pos).getId1() + "';", null);

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
            sqliteHelper = new SqliteHelper(getActivity());
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put("newsID", imageList.get(pos).getId1());
            insertValues.put("short_desc", imageList.get(pos).getShort_desc());
            insertValues.put("long_desc", imageList.get(pos).getDescription());
            insertValues.put("image", imageList.get(pos).getImage());
            insertValues.put("medialink", imageList.get(pos).getMediaLink());
            insertValues.put("media", imageList.get(pos).getMedia());
            insertValues.put("is_image", imageList.get(pos).getIs_image());
            insertValues.put("news_date", imageList.get(pos).getNews_date());
            insertValues.put("reference", imageList.get(pos).getReference());
            insertValues.put("short_name", imageList.get(pos).getShortname());

            Log.e(TAG, "insertData: " + imageList.get(pos).getShort_desc());

            db1.insert("bookmark", null, insertValues);
            db1.close();
            img_Deletebookmark.setVisibility(View.VISIBLE);
            img_Addbookmark.setVisibility(View.GONE);

            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {
                    img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else if (MainActivity.themeKEY.equals("0")) {
                    img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("table Add bookmark....", "onMinusClicked: " + e.getMessage());
        }

    }

    private void deleteData() {

        sqliteHelper = new SqliteHelper(getActivity());
        SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
        db1.execSQL("DELETE FROM bookmark Where newsID ='" + imageList.get(pos).getId1() + "';");
        db1.close();
        img_Deletebookmark.setVisibility(View.GONE);
        img_Addbookmark.setVisibility(View.VISIBLE);
    }

    private void showdb() {

        try {
            sqliteHelper = new SqliteHelper(getActivity());
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            Cursor cur = db1.rawQuery("SELECT * FROM bookmark Where newsID ='" + imageList.get(pos).getId1() + "';", null);

            if (cur.moveToFirst()) {

                img_Deletebookmark.setVisibility(View.VISIBLE);
                img_Addbookmark.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Already Added !", Toast.LENGTH_SHORT).show();
                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else if (MainActivity.themeKEY.equals("0")) {
                        img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                        img_Addbookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
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

    private void speakOut() {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                final String keyword = s;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onDone(String s) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_pause.setVisibility(View.GONE);
                        btn_play.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(String s) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        String text = String.valueOf(Html.fromHtml(imageList.get(pos).getDescription()));
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "");

    }

    private void getData() {
        imageList.clear();
        pageNo = 1;
        mainProgress.setVisibility(View.VISIBLE);
        relay_Bott.setVisibility(View.GONE);

        url = getString(R.string.server_url) + "webservices/news.php?page=" + pageNo + "&pp=" + noOfrecords + "&state_id=" + strSTATEId + "&city_id=" + strCITYId;
        Log.e(TAG, "getData: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                            tx_noData.setVisibility(View.VISIBLE);
                            tx_noData.setText("No news avilable");
                        }

                        for (int i = 0; i < news.length(); i++) {

                            Model_News model_class = new Model_News();
                            JSONObject jsonObject1 = news.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String cat_id = jsonObject1.getString("cat_id");
                            String thumbnail = jsonObject1.getString("thumbnail");
                            String short_desc = jsonObject1.getString("short_desc");
                            String long_desc = jsonObject1.getString("long_desc");
                            String media = jsonObject1.getString("media");
                            mediaLink = jsonObject1.getString("medialink");
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
                            imageList.add(model_class);

                        }
                        setui();
                        layBookmark.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (news.length() == 0) {
                                    mainProgress.setVisibility(View.GONE);
                                    mainProgress2.setVisibility(View.GONE);
                                    relay_Bott.setVisibility(View.GONE);
                                } else {
                                    mainProgress.setVisibility(View.GONE);
                                    mainProgress2.setVisibility(View.GONE);
                                    relay_Bott.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 200);

                    } else {
                        mainProgress.setVisibility(View.GONE);
                        mainProgress2.setVisibility(View.GONE);
                        tx_noData.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    mainProgress.setVisibility(View.GONE);
                    mainProgress2.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void setui() {

        viewPagerAdapter = new ViewPagerAdapter(getActivity(), imageList, viewPager2);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setPageTransformer(new DepthTransformation());

        if (pos == 0) {
            try {
                btnPlayTheme();
                btn_play.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG, "onClick: " + e.getMessage());
            }

        } else {
            btn_play.setVisibility(View.GONE);
        }

        try {
            supportLan(imageList.get(pos).getDescription());
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "setui: " + e.getMessage());
        }
        showdb();

    }

    private void loadMore() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                mainProgress2.setVisibility(View.VISIBLE);
            } else if (MainActivity.themeKEY.equals("0")) {
                mainProgress.setVisibility(View.VISIBLE);
            }
        } else {
            mainProgress.setVisibility(View.VISIBLE);
        }

        url = getString(R.string.server_url) + "webservices/news.php?page=" + pageNo + "&pp=" + noOfrecords + "&state_id=" + strSTATEId + "&city_id=" + strCITYId;
        Log.e(TAG, "getData: " + url);

        final ArrayList<Model_News> dataList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                            Toast.makeText(getActivity(), "No more News !", Toast.LENGTH_SHORT).show();
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
                            mediaLink = jsonObject1.getString("medialink");
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
                        mainProgress2.setVisibility(View.GONE);

                        if (dataList.size() != 0) {
                            Log.e("adapter", "" + dataList.size());
                            viewPagerAdapter.addItem(dataList, imageList.size());
                        }

                    } else {

                        if (imageList.size() != -1) {
                            mainProgress.setVisibility(View.GONE);
                            mainProgress2.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    mainProgress.setVisibility(View.GONE);
                    mainProgress2.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                mainProgress.setVisibility(View.GONE);
                mainProgress2.setVisibility(View.GONE);
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
    public void onGlobalLayout() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initTTS();
        try {
            if (MainActivity.isSearch.equals("yes")) {
                getData();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tts != null) {
            tts.stop();
            btn_pause.setVisibility(View.GONE);
            btn_play.setVisibility(View.VISIBLE);
        }
    }

}
