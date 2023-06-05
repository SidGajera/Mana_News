package com.mananews.apandts.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mananews.apandts.Adapter.GalleryCircleAdapter;
import com.mananews.apandts.Adapter.ViewPagerStatusAdapter;
import com.mananews.apandts.Model_Class.Model_News;
import com.mananews.apandts.Model_Class.StatusModel.Example;
import com.mananews.apandts.OnItemClickListner;
import com.mananews.apandts.api.ApiService;
import com.mananews.apandts.databinding.FragmentStatusBinding;
import com.mananews.apandts.utils.DepthTransformation;
import com.mananews.apandts.utils.SPmanager;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatusFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "FATZ";
    private int pageNo = 1;
    ArrayList<Model_News> imageList = new ArrayList<>();
    private TextToSpeech tts;
    public static String strSTATEId, strCITYId;
    private ViewPagerStatusAdapter viewPagerAdapter;
    private String username, email, password;

    public StatusFragment() {
    }

    private FragmentStatusBinding binding;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(getLayoutInflater());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getPreferenceData();
        init();
        getData();
        return binding.getRoot();
    }

    private void getPreferenceData() {
        password = SPmanager.getPreference(getActivity(), "password");
        username = SPmanager.getPreference(getActivity(), "username");
        email = SPmanager.getPreference(getActivity(), "email");
        Log.e(TAG, "getPreferenceData: " + password + username + email);
    }

    private void init() {

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

        binding.swiperefreshItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

//        binding.viewPager2Main.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//
//                try {
//                    if (position == imageList.size() - 1 && (int) positionOffset == 0 /*&& !isLastPageSwiped*/) {
//                        if (counterPageScroll != 0) {
//                            pageNo = pageNo + 1;
//                            Log.e(TAG, "Load More");
//                        }
//                        counterPageScroll++;
//                    } else {
//                        counterPageScroll = 0;
//                    }
//                } catch (Exception e) {
//                    e.getMessage();
//                    Log.e(TAG, "onPageScrolled: " + e.getMessage());
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                pos = position;
//                Log.e("Selected_Page", String.valueOf(position));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//                tts.stop();
//            }
//        });
    }

    private void initTTS() {
        tts = new TextToSpeech(getActivity(), status -> {

            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.getDefault());
            } else {
                Toast.makeText(getActivity(), "Language Not Suppourted !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        binding.swiperefreshItems.setRefreshing(true);
        imageList.clear();
        pageNo = 1;
        binding.mainProgress.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mananews.tirupatihost.in/").addConverterFactory(GsonConverterFactory.create()).build();

        ApiService statusservice = retrofit.create(ApiService.class);

        Log.e(TAG, "11");
        statusservice.getStatusService("1", "0").enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                binding.mainProgress.setVisibility(View.GONE);
                binding.swiperefreshItems.setRefreshing(false);
                Log.e(TAG, "22: " + response.code());
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.e(TAG, "Data: " + response.body().getCurrentPage());
                        viewPagerAdapter = new ViewPagerStatusAdapter(getActivity(), response.body().getResponse().getData(), new OnItemClickListner<Object>() {
                            @Override
                            public void onItemClick(String image, int position) {
                                Log.e(TAG, "onItemClick: image => "+image + "pos = " + position);
                            }
                        });
                        Log.e(TAG, "onItemCount: ItemCount => "+viewPagerAdapter.getItemCount());
                        binding.viewPager2Main.setAdapter(viewPagerAdapter);
                        binding.viewPager2Main.setPageTransformer(new DepthTransformation());
                        binding.rv.setAdapter(new GalleryCircleAdapter(getActivity(), response.body().getResponse().getData(), new GalleryCircleAdapter.onClick() {
                            @Override
                            public void onClickItem(String image, int pos) {
//                                binding.viewPager2Main.canScrollVertically(pos);
                                binding.viewPager2Main.setCurrentItem(pos);
                                Log.e(TAG, "onItemClick: image => "+image + "pos = " + pos);
                            }
                        }));
                    } else {
                        Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e(TAG, "OnFailer");
                binding.swiperefreshItems.setRefreshing(false);
                binding.mainProgress.setVisibility(View.GONE);
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
    public void onGlobalLayout() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initTTS();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tts != null) {
            tts.stop();
        }
    }
}
