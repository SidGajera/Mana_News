package com.mananews.apandts.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mananews.apandts.Activity.CommentActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Adapter.ViewPagerAdapter;
import com.mananews.apandts.Model_Class.Model_News;

import com.mananews.apandts.utils.DepthTransformation;
import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.utils.SqliteHelper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.mananews.apandts.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class Bookmark_Fragment extends Fragment  {

    private static final String TAG = "Bookmark Fragment";
    private SqliteHelper sqliteHelper;
    ArrayList<Model_News> bookmarkList = new ArrayList<>();
    private ImageView img_Deletebookmark, btn_play, btn_pause, imgComment;
    private TextToSpeech tts;
    String[] codelist = new String[]{"ar", "zh", "cs", "da", "nl", "en", "fi",
            "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", "no",
            "pl", "pt", "ro", "ru", "sk", "es", "sv", "th", "tr", "te", "mr"};
    private TextView tx_nodata;
    private RelativeLayout lay_Bottom, lay_btnplay, layBottom2;
    private ViewPager2 viewPager2;
    private int pos;
    private ViewPagerAdapter viewPagerAdapter;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    AdView mAdView;

    public Bookmark_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_, container, false);
        viewPager2 = view.findViewById(R.id.viewPager2BookMark);
        img_Deletebookmark = (ImageView) view.findViewById(R.id.img_DeletebookmarkB);
        btn_play = view.findViewById(R.id.btn_playyB);
        btn_pause = view.findViewById(R.id.btn_pauseB);
        tx_nodata = view.findViewById(R.id.tx_nodata);
        lay_Bottom = view.findViewById(R.id.lay_Bottom);
        lay_btnplay = view.findViewById(R.id.lay_btnplay);
        layBottom2 = view.findViewById(R.id.layBottom2);
        imgComment = view.findViewById(R.id.imgComment);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        init();

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                layBottom2.setBackgroundColor(getResources().getColor(R.color.footercolor));
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                tx_nodata.setTextColor(getResources().getColor(R.color.darkDray));
                tx_nodata.setBackgroundColor(getResources().getColor(R.color.pagecolor));
            } else if (MainActivity.themeKEY.equals("0")) {
                layBottom2.setBackgroundColor(getResources().getColor(R.color.gray));
                img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            layBottom2.setBackgroundColor(getResources().getColor(R.color.gray));
            img_Deletebookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgComment.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if (getString(R.string.tts_visibility).equals("yes")) {
            lay_btnplay.setVisibility(View.VISIBLE);
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("1")) {

                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (MainActivity.themeKEY.equals("0")) {
                    btn_play.setVisibility(View.VISIBLE);
                    btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                btn_play.setVisibility(View.VISIBLE);
                btn_play.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } else {
            lay_btnplay.setVisibility(View.GONE);
        }

        getData();
        if (bookmarkList.size() != 0) {
            tx_nodata.setVisibility(View.GONE);
        } else {
            tx_nodata.setVisibility(View.VISIBLE);
            lay_Bottom.setVisibility(View.GONE);

        }

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("news_id", bookmarkList.get(pos).getId1());
                SPmanager.saveValue(getActivity(),"news_id",bookmarkList.get(pos).getId1());
                startActivity(intent);
            }
        });

        return view;
    }


    private void init() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                pos = position;
                try {
                    supportLan(bookmarkList.get(pos).getDescription());
                    btn_pause.setVisibility(View.GONE);
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

                    supportLan(bookmarkList.get(pos).getDescription());
                    btn_pause.setVisibility(View.GONE);
                    tts.stop();
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onPageScrollStateChanged: " + e.getMessage());
                }

            }
        });

        bookmarkList.clear();
        img_Deletebookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteData();
//                adapter.removeView(pos);
//                adapter.notifyDataSetChanged();

                viewPagerAdapter.removeView(pos);
                viewPagerAdapter.notifyDataSetChanged();

                Snackbar snackbar = Snackbar
                        .make(view, "Bookmark Deleted !", Snackbar.LENGTH_LONG);
                snackbar.show();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(Bookmark_Fragment.this).attach(Bookmark_Fragment.this).commit();

                if (bookmarkList.size() == 1) {
                    FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                    ft2.detach(Bookmark_Fragment.this).attach(Bookmark_Fragment.this).commit();
                }

            }
        });

        initTTS();

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_pause.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.INVISIBLE);

                if (MainActivity.themeKEY != null) {
                    if (MainActivity.themeKEY.equals("1")) {
                        btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                    } else if (MainActivity.themeKEY.equals("0")) {
                        btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    btn_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                String speak = bookmarkList.get(pos).getDescription();
                tts.speak(String.valueOf(Html.fromHtml(speak)), TextToSpeech.QUEUE_ADD, null);
                speakOut();
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
               Log.e("------------","0000000000000000000000000");
               Log.e("------------","0000000000000000000000000");
               Log.e("------------","0000000000000000000000000");
               Log.e("------------","0000000000000000000000000");
               Log.e("------------","0000000000000000000000000");
               Log.e("------------","0000000000000000000000000");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


    }

    private void initTTS() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
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
                        Toast.makeText(getActivity(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        String text = String.valueOf(Html.fromHtml(bookmarkList.get(pos).getDescription()));
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "");

    }

    private void deleteData() {

        try {
            sqliteHelper = new SqliteHelper(getActivity());
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();
            db1.execSQL("DELETE FROM bookmark Where newsID ='" + bookmarkList.get(pos).getId1() + "';");
            db1.close();
            viewPagerAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "deleteData: " + e.getMessage());
        }

    }

    private void getData() {

        try {
            sqliteHelper = new SqliteHelper(getActivity());
            SQLiteDatabase db1 = sqliteHelper.getWritableDatabase();

            String query = "select * from bookmark";
            Cursor cur = db1.rawQuery(query, null);

            if (cur.getCount() != 0) {
                if (cur.moveToFirst()) {
                    do {

                        Model_News getSet = new Model_News();
                        String newsID = (cur.getString(cur.getColumnIndex("newsID")));
                        String short_desc = cur.getString(cur.getColumnIndex("short_desc"));
                        String long_desc = cur.getString(cur.getColumnIndex("long_desc"));
                        String image = cur.getString(cur.getColumnIndex("image"));
                        String media = cur.getString(cur.getColumnIndex("media"));
                        String medialink = cur.getString(cur.getColumnIndex("medialink"));
                        String is_image = cur.getString(cur.getColumnIndex("is_image"));
                        String news_date = cur.getString(cur.getColumnIndex("news_date"));
                        String reference = cur.getString(cur.getColumnIndex("reference"));
                        String short_name = cur.getString(cur.getColumnIndex("short_name"));

                        getSet.setId1(newsID);
                        getSet.setShort_desc(short_desc);
                        getSet.setDescription(long_desc);
                        getSet.setImage(image);
                        getSet.setMedia(media);
                        getSet.setMediaLink(medialink);
                        getSet.setIs_image(is_image);
                        getSet.setNews_date(news_date);
                        getSet.setReference(reference);
                        getSet.setShortname(short_name);
                        bookmarkList.add(getSet);

                        Log.e(TAG, "onCreate: " + newsID);

                    } while (cur.moveToNext());
                    Log.e(TAG, "getData: " + bookmarkList);
                }
            }

            setUI();
            cur.close();
            db1.close();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getCause());
            e.printStackTrace();

        }
    }

    private void setUI() {
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), bookmarkList, viewPager2);
        viewPager2.setAdapter(viewPagerAdapter);

//        BookFlipPageTransformer2 bookFlipPageTransformer = new BookFlipPageTransformer2();
//// Enable / Disable scaling while flipping. If true, then next page will scale in (zoom in). By default, its true.
//        bookFlipPageTransformer.setEnableScale(true);
//// The amount of scale the page will zoom. By default, its 5 percent.
//        bookFlipPageTransformer.setScaleAmountPercent(10f);

// Assign the page transformer to the ViewPager2.
        viewPager2.setPageTransformer(new DepthTransformation());

        try {
            supportLan(bookmarkList.get(pos).getDescription());
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "setui: " + e.getMessage());
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
            btn_pause.setVisibility(View.GONE);
            btn_play.setVisibility(View.VISIBLE);
        }
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



}
