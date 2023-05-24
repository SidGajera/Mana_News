package com.mananews.apandts.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mananews.apandts.Adapter.AdapterComment;
import com.mananews.apandts.Model_Class.ModelComment;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.SPmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private Button btnAddComment;
    private ImageView imgClose;
    private TextView txtComment, txtNoData;
    private RecyclerView recycleComment;
    private ProgressBar pb, pb2, pbLoadMore1, pbLoadMore2;
    private String news_id;
    private int pageNo = 1;
    public static int noOfrecords = 10;
    ArrayList<ModelComment> commentList = new ArrayList<>();
    private LinearLayout linMain, linTop;
    private String loginSuccessfull, registeredSuccessfull, userid, message;
    private ProgressDialog progressDialog;
    private AdapterComment adapterComment;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    boolean isLogin = false;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().hide();

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



        getPrefrences();
        init();
        getAllComments(news_id);
        getThemes();
    }


    private void getPrefrences() {
        news_id = SPmanager.getPreference(CommentActivity.this, "news_id");
        userid = SPmanager.getPreference(CommentActivity.this, "userid");

        loginSuccessfull = SPmanager.getPreference(CommentActivity.this, "loginSuccessful");
        registeredSuccessfull = SPmanager.getPreference(CommentActivity.this, "registeredSuccessful");
        registeredSuccessfull = SPmanager.getPreference(CommentActivity.this, "registeredSuccessful");
        isLogin = SPmanager.getPreferenceBoolean(CommentActivity.this, "isLogin");
        Log.e(TAG, "getIntents: " + news_id + userid + loginSuccessfull);
    }

    private void init() {
        imgClose = findViewById(R.id.imgClose);
        btnAddComment = findViewById(R.id.btnAddComment);
        txtComment = findViewById(R.id.txtComment);
        txtNoData = findViewById(R.id.txtNoData);
        recycleComment = findViewById(R.id.recycleComment);
        pb = findViewById(R.id.pb);
        pb2 = findViewById(R.id.pb2);
        pbLoadMore1 = findViewById(R.id.pbLoadMore1);
        pbLoadMore2 = findViewById(R.id.pbLoadMore2);
        linMain = findViewById(R.id.linMain);
        linTop = findViewById(R.id.linTop);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isLogin){
                        commentDialog();
                    }else {
                        Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                        intent.putExtra("news_id", news_id);
                        startActivity(intent);
                        finish();
                    }

//                    if (loginSuccessfull != null) {
//                        if (loginSuccessfull.equals("yes")) {
//                            commentDialog();
//                        } else if (registeredSuccessfull.equals("yes")) {
//                            Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
//                            intent.putExtra("news_id", news_id);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } else {
//                        Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
//                        intent.putExtra("news_id", news_id);
//                        startActivity(intent);
//                        finish();
//                    }
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onClick: " + e.getMessage());
                }
            }
        });
    }

    private void getThemes() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                linTop.setBackgroundColor(getResources().getColor(R.color.footercolor));
                linMain.setBackgroundColor(getResources().getColor(R.color.white));
                pb2.setVisibility(View.VISIBLE);
                txtNoData.setTextColor(getResources().getColor(R.color.darkDray));
                imgClose.setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (MainActivity.themeKEY.equals("0")) {
                linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
                linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
                txtNoData.setTextColor(getResources().getColor(R.color.white));
                imgClose.setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                pb.setVisibility(View.VISIBLE);
            }
        } else {
            linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
            linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
            imgClose.setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            txtNoData.setTextColor(getResources().getColor(R.color.white));
            pb.setVisibility(View.VISIBLE);
        }
    }

    private void getAllComments(String news_id) {
        commentList.clear();
        pageNo = 1;
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                pb2.setVisibility(View.VISIBLE);
            } else if (MainActivity.themeKEY.equals("2")) {
                pb.setVisibility(View.VISIBLE);
            }
        } else {
            pb.setVisibility(View.VISIBLE);
        }
        recycleComment.setVisibility(View.GONE);
        String url = getString(R.string.server_url) + "webservices/get_comment.php?news_id=" + news_id + "&page=" + pageNo + "&pp=" + noOfrecords;
        Log.e(TAG, "getAllComments: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(CommentActivity.this);
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
                        String short_description = data.getString("short_description");
                        JSONArray comment = data.getJSONArray("comment");

                        if (comment.length() == 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < comment.length(); i++) {
                            JSONObject object = comment.getJSONObject(i);
                            String id = object.getString("id");
                            String username = object.getString("username");
                            String profile = object.getString("profile");
                            String comment_ = object.getString("comment");
                            String status_ = object.getString("status");
                            String comment_time = object.getString("comment_time");

                            ModelComment getset = new ModelComment();
                            getset.setId(id);
                            getset.setUsername(username);
                            getset.setProfile(profile);
                            getset.setComment(comment_);
                            getset.setComment_time(comment_time);
                            getset.setStatus_(status_);
                            commentList.add(getset);
                        }
                        setui(commentList);
                        recycleComment.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (comment.length() == 0) {
                                    pb.setVisibility(View.GONE);
                                    pb2.setVisibility(View.GONE);
                                    txtNoData.setVisibility(View.VISIBLE);
                                } else {
                                    pb.setVisibility(View.GONE);
                                    pb2.setVisibility(View.GONE);
                                    txtNoData.setVisibility(View.GONE);
                                }
                            }
                        }, 200);

                    } else {
                        pb.setVisibility(View.GONE);
                        pb2.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    pb.setVisibility(View.GONE);
                    pb2.setVisibility(View.GONE);

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

    private void setui(ArrayList<ModelComment> commentList) {
        if (commentList.size() == 0) {
            linMain.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(CommentActivity.this, RecyclerView.VERTICAL, false);
            adapterComment = new AdapterComment(recycleComment, CommentActivity.this, commentList);
            recycleComment.setLayoutManager(manager);
            recycleComment.setAdapter(adapterComment);
            adapterComment.notifyDataSetChanged();
            recycleComment.invalidate();

            adapterComment.setOnLoadMoreListener(new AdapterComment.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (MainActivity.themeKEY != null) {
                        if (MainActivity.themeKEY.equals("1")) {
                            pbLoadMore1.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.darkDray), PorterDuff.Mode.SRC_IN);

                        } else if (MainActivity.themeKEY.equals("0")) {
                            pbLoadMore1.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);
                        }
                    } else {
                        pbLoadMore1.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(CommentActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);
                    }
                    pageNo = pageNo + 1;
                    loadMore();
                }
            });
        }
    }

    private void loadMore() {
        pbLoadMore1.setVisibility(View.VISIBLE);
        final ArrayList<ModelComment> dataList = new ArrayList<>();
        String url = getString(R.string.server_url) + "/webservices/get_comment.php?news_id=" + news_id + "&page=" + pageNo + "&pp=" + noOfrecords;
        Log.e(TAG, "getAllComments: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(CommentActivity.this);
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
                        String short_description = data.getString("short_description");
                        JSONArray comment = data.getJSONArray("comment");

                        if (comment.length() == 0) {
                            pbLoadMore1.setVisibility(View.GONE);
                            pbLoadMore2.setVisibility(View.GONE);
                            Toast.makeText(CommentActivity.this, "No more comments", Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 0; i < comment.length(); i++) {
                            JSONObject object = comment.getJSONObject(i);
                            String id = object.getString("id");
                            String username = object.getString("username");
                            String profile = object.getString("profile");
                            String comment_ = object.getString("comment");
                            String status_ = object.getString("status");
                            String comment_time = object.getString("comment_time");

                            ModelComment getset = new ModelComment();
                            getset.setId(id);
                            getset.setUsername(username);
                            getset.setProfile(profile);
                            getset.setComment(comment_);
                            getset.setComment_time(comment_time);
                            getset.setStatus_(status_);
                            dataList.add(getset);
                        }
                        pbLoadMore1.setVisibility(View.GONE);
                        pbLoadMore2.setVisibility(View.GONE);
                        if (dataList.size() != 0) {
                            adapterComment.setLoaded();
                            adapterComment.addItem(dataList, commentList.size());
                        }

                    } else {
                        pbLoadMore1.setVisibility(View.GONE);
                        pbLoadMore2.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    pbLoadMore1.setVisibility(View.GONE);
                    pbLoadMore2.setVisibility(View.GONE);

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

    private void commentDialog() {
        final Dialog dialog = new Dialog(CommentActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_comment, null, false);

        LinearLayout linCommet = view.findViewById(R.id.linCommet);
        RelativeLayout relComment = view.findViewById(R.id.relComment);
        TextView txtTitComment = view.findViewById(R.id.txtTitComment);
        TextView txtCancle = view.findViewById(R.id.txtCancle);
        TextView txtSend = view.findViewById(R.id.txtSend);
        EditText edtComment = view.findViewById(R.id.edtComment);

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                linCommet.setBackgroundResource(R.drawable.shape_comment_white);
                relComment.setBackgroundResource(R.drawable.shape_comment_light);
                txtTitComment.setTextColor(getResources().getColor(R.color.darkDray));
                txtCancle.setTextColor(getResources().getColor(R.color.darkDray));
                txtSend.setTextColor(getResources().getColor(R.color.white));
                txtCancle.setBackgroundResource(R.drawable.button_no_light);
                txtSend.setBackgroundResource(R.drawable.button_no);
                edtComment.setTextColor(getResources().getColor(R.color.darkDray));

            } else if (MainActivity.themeKEY.equals("0")) {
                linCommet.setBackgroundResource(R.drawable.shape_comment_dark);
                txtTitComment.setTextColor(getResources().getColor(R.color.yellow));
                txtCancle.setTextColor(getResources().getColor(R.color.white));
                txtSend.setTextColor(getResources().getColor(R.color.darkDray));
                txtCancle.setBackgroundResource(R.drawable.button_no);
                txtSend.setBackgroundResource(R.drawable.button_yes);
                edtComment.setTextColor(getResources().getColor(R.color.white));

            }
        } else {
            linCommet.setBackgroundResource(R.drawable.shape_comment_dark);
            txtCancle.setTextColor(getResources().getColor(R.color.white));
            txtSend.setTextColor(getResources().getColor(R.color.darkDray));
            txtTitComment.setTextColor(getResources().getColor(R.color.yellow));
            txtCancle.setBackgroundResource(R.drawable.button_no);
            txtSend.setBackgroundResource(R.drawable.button_yes);
            edtComment.setTextColor(getResources().getColor(R.color.white));
        }

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = edtComment.getText().toString();
                if (!comment.isEmpty()) {
                    sendComment(comment);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CommentActivity.this, "Please add comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void sendComment(String comment) {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressDialog = new ProgressDialog(CommentActivity.this, R.style.MyAlertDialogStyleLight);
            } else {
                progressDialog = new ProgressDialog(CommentActivity.this, R.style.MyAlertDialogStyle);
            }
        } else {
            progressDialog = new ProgressDialog(CommentActivity.this, R.style.MyAlertDialogStyle);
        }
//        final ProgressDialog progressDialog = new ProgressDialog(CommentActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Wait a moment...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String url = getString(R.string.server_url) + "webservices/add_comment.php?user_id=" + userid + "&news_id=" + news_id + "&comment=" + comment;
        Log.e(TAG, "sendComment: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(CommentActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        JSONObject res = jsonObject.getJSONObject("response");
                        message = res.getString("message");
                        Log.e(TAG, "onResponse: " + message);
                    } else {
                    }
                    progressDialog.dismiss();
                    getAllComments(news_id);
                    Toast.makeText(CommentActivity.this, "Your comment added", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
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
            mInterstitialAd.show(CommentActivity.this);
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