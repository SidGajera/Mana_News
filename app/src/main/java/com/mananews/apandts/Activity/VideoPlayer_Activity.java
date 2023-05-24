package com.mananews.apandts.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.mananews.apandts.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
public class VideoPlayer_Activity extends AppCompatActivity {

    private static final String TAG = "Video Player Activity";
    private ImageView down;
    private String mediaLink, localVideo;
    private RelativeLayout lay_main;
    private VideoView videoView;
    private String videoAddress;
    private YouTubePlayerView youtubePlayerView;
    private ProgressBar progressV;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_);

        getSupportActionBar().hide();
        statusBarColor();
        mediaLink = getIntent().getStringExtra("mediaLink");
        localVideo = getIntent().getStringExtra("localVideo");

        Log.e(TAG, "onCreate: " + mediaLink);
        Log.e(TAG, "onCreate: " + localVideo);

        down = (ImageView) findViewById(R.id.down);
        lay_main = (RelativeLayout) findViewById(R.id.lay_main);
        videoView = findViewById(R.id.videoView);
        progressV = findViewById(R.id.progressV);

        youtubePlayerView = findViewById(R.id.youtube_player_view);
//        getLifecycle().addObserver(youtubePlayerView);

        videoAddress = getString(R.string.server_url) + "uploads/news-media/" + localVideo;
        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        final Uri vidAddress = Uri.parse(videoAddress);
        videoView.setMediaController(mediacontroller);

        try {
            if (!localVideo.equals("null")) {
                youtubePlayerView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.requestFocus();
                videoView.setVideoURI(vidAddress);
                videoView.start();
                progressV.setVisibility(View.GONE);

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        Toast.makeText(VideoPlayer_Activity.this, "Video Complete", Toast.LENGTH_SHORT).show();

                    }
                });

                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.d("API123", "What " + what + " extra " + extra);
                        return false;
                    }
                });

            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

        try {
            if (mediaLink != null) {
                progressV.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                youtubePlayerView.setVisibility(View.VISIBLE);
                getLifecycle().addObserver(youtubePlayerView);
                youtubePlayerView.initialize(new YouTubePlayerInitListener() {
                    @Override
                    public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                        initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady() {
//                        String videoId = "6JYIGclVQdw";
                                try {
                                    String[] split = mediaLink.split("=");
                                    String vidId = split[1];

                                    initializedYouTubePlayer.loadVideo(vidId, 0);
                                } catch (Exception e) {
                                    e.getMessage();
                                    Log.e(TAG, "onReady: " + e.getMessage());
                                }

                            }
                        });
                    }
                }, true);
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "onCreate: " + e.getMessage());
        }


        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

                openActivityfromBottom();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


            }
        });

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
        openActivityfromBottom();
        MainActivity.counter = MainActivity.counter + 1;
        if (mInterstitialAd != null) {
            MainActivity.counter = 0;
            mInterstitialAd.show(VideoPlayer_Activity.this);
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
