package com.mananews.apandts.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mananews.apandts.Activity.CategoriesActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Adapter.Adapter_Cat;
import com.mananews.apandts.Model_Class.Model_Cat;
import com.mananews.apandts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar mainProgress, progressDark;
    ArrayList<Model_Cat> catList = new ArrayList<>();
    private Adapter_Cat adapter_cat;
    private ListView cat_ListView;

    AdView mAdView;
    public CatListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CatListFragment newInstance(String param1, String param2) {
        CatListFragment fragment = new CatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_list, container, false);
        mainProgress = view.findViewById(R.id.mainProgress);
        progressDark = view.findViewById(R.id.progressDark);
        cat_ListView = view.findViewById(R.id.cat_ListView);
        getCategory();

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




        return view;
    }

    private void getCategory() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressDark.setVisibility(View.VISIBLE);
                mainProgress.setVisibility(View.GONE);
            } else if (MainActivity.themeKEY.equals("0")) {
                mainProgress.setVisibility(View.VISIBLE);
                progressDark.setVisibility(View.GONE);
            } else {
                mainProgress.setVisibility(View.VISIBLE);
                progressDark.setVisibility(View.GONE);
            }
        } else {
            mainProgress.setVisibility(View.VISIBLE);
        }
        String catimgPath = "webservices/category.php";
        String urlCatImg = getString(R.string.server_url) + catimgPath;
        Log.e(TAG, "getData: " + urlCatImg);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCatImg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);
                catList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        JSONObject resp = jsonObject.getJSONObject("response");

                        JSONObject data = resp.getJSONObject("data");
                        JSONArray category = data.getJSONArray("category");
                        for (int i = 0; i < category.length(); i++) {

                            Model_Cat model_cat = new Model_Cat();
                            JSONObject jsonObject1 = category.getJSONObject(i);

                            String cate_id = jsonObject1.getString("id");
                            String category_name = jsonObject1.getString("category_name");
                            String image = jsonObject1.getString("image");

                            model_cat.setCat_id(cate_id);
                            model_cat.setCategory_name(category_name);
                            model_cat.setCatImg(image);

                            catList.add(model_cat);
                        }
                        mainProgress.setVisibility(View.GONE);
                        progressDark.setVisibility(View.GONE);
                        setCatUI(catList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        });

        requestQueue.add(stringRequest);
    }

    private void setCatUI(ArrayList<Model_Cat> catList) {
        try {
            adapter_cat = new Adapter_Cat(getActivity(), catList);
            cat_ListView.setAdapter(adapter_cat);
            cat_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getActivity(), CategoriesActivity.class);
                    intent.putExtra("cat_id", catList.get(i).getCat_id());
                    intent.putExtra("cat_name", catList.get(i).getCategory_name());
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "setCatUI: " + e.getMessage());
        }
    }
//    getFragmentManager().popBackStackImmediate();


    public boolean onBackPressed() {
        getFragmentManager().popBackStackImmediate();
        return false;
    }


}