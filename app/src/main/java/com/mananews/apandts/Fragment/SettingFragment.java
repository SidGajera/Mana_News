package com.mananews.apandts.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mananews.apandts.Activity.LoginActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Activity.ProfileActivity;
import com.mananews.apandts.utils.SPmanager;
import com.mananews.apandts.R;

public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SettingFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView theme_off, theme_on, notification_off, notification_on, img_daynight, img_notification, imhProfile,imgLogout;
    public static String nKEY = "yes";
    public static String key = "0";
    private LinearLayout lin_item, linProfile,btnLogout;
    public static String themeKEY;
    private TextView tx_lightmode, tx_lightmode2, tx_notification, tx_notification2, tx_Profile, tx_Profile2,tx_logout;
    AdView mAdView;

    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        theme_off = view.findViewById(R.id.theme_off);
        theme_on = view.findViewById(R.id.theme_on);
        notification_off = view.findViewById(R.id.notification_off);
        notification_on = view.findViewById(R.id.notification_on);
        lin_item = view.findViewById(R.id.lin_item);
        linProfile = view.findViewById(R.id.linProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        img_daynight = view.findViewById(R.id.img_daynight);
        img_notification = view.findViewById(R.id.img_notification);
        imhProfile = view.findViewById(R.id.imhProfile);
        imgLogout = view.findViewById(R.id.imgLogout);
        tx_lightmode = view.findViewById(R.id.tx_lightmode);
        tx_lightmode2 = view.findViewById(R.id.tx_lightmode2);
        tx_notification = view.findViewById(R.id.tx_notification);
        tx_notification2 = view.findViewById(R.id.tx_notification2);
        tx_Profile = view.findViewById(R.id.tx_Profile);
        tx_Profile2 = view.findViewById(R.id.tx_Profile2);
        tx_logout = view.findViewById(R.id.tx_logout);

        if (SPmanager.getPreference(getActivity(),"userid") != null){
            btnLogout.setVisibility(View.VISIBLE);
        }else {
            btnLogout.setVisibility(View.GONE);
        }

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

        themeKEY = SPmanager.getPreference(getActivity(), "themeKEY");
        Log.e(TAG, "onCreate: " + themeKEY);

        if (themeKEY == null) {
            if (getActivity().getResources().getString(R.string.isLight).equalsIgnoreCase("1")){
                themeKEY = "1";
            }else {
                themeKEY = "0";
            }
        }
        if (MainActivity.themeKEY != null) {
            if (themeKEY.equals("1")) {
                theme_off.setVisibility(View.GONE);
                theme_on.setVisibility(View.VISIBLE);
                lin_item.setBackgroundColor(getResources().getColor(R.color.white));
                img_daynight.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_notification.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imhProfile.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgLogout.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                tx_lightmode.setTextColor(getResources().getColor(R.color.darkDray));
                tx_lightmode2.setTextColor(getResources().getColor(R.color.darkDray));
                tx_notification.setTextColor(getResources().getColor(R.color.darkDray));
                tx_notification2.setTextColor(getResources().getColor(R.color.darkDray));
                tx_Profile.setTextColor(getResources().getColor(R.color.darkDray));
                tx_Profile2.setTextColor(getResources().getColor(R.color.darkDray));
                tx_logout.setTextColor(getResources().getColor(R.color.darkDray));

                theme_on.setImageResource(R.drawable.ic_light_on);
                theme_off.setImageResource(R.drawable.ic_light_off);
                notification_on.setImageResource(R.drawable.ic_light_on);
                notification_off.setImageResource(R.drawable.ic_light_off);

            } else {
                theme_on.setVisibility(View.GONE);
                theme_off.setVisibility(View.VISIBLE);
                lin_item.setBackgroundColor(getResources().getColor(R.color.darkDray));
                img_daynight.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_notification.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imhProfile.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgLogout.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

                tx_lightmode.setTextColor(getResources().getColor(R.color.white));
                tx_lightmode2.setTextColor(getResources().getColor(R.color.white));
                tx_notification.setTextColor(getResources().getColor(R.color.white));
                tx_notification2.setTextColor(getResources().getColor(R.color.white));
                tx_Profile.setTextColor(getResources().getColor(R.color.white));
                tx_Profile2.setTextColor(getResources().getColor(R.color.white));
                tx_logout.setTextColor(getResources().getColor(R.color.white));
            }
        }
        theme_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme_on.setVisibility(View.GONE);
                theme_off.setVisibility(View.VISIBLE);
                key = "0";
                SPmanager.saveValue(getActivity(), "themeKEY", key);
                lin_item.setBackgroundColor(getResources().getColor(R.color.darkDray));
                tx_lightmode.setTextColor(getResources().getColor(R.color.white));
                tx_notification.setTextColor(getResources().getColor(R.color.white));

                img_daynight.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_notification.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imhProfile.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgLogout.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

                lin_item.setBackgroundColor(getResources().getColor(R.color.darkDray));
                tx_lightmode.setTextColor(getResources().getColor(R.color.white));
                tx_lightmode2.setTextColor(getResources().getColor(R.color.white));
                tx_notification.setTextColor(getResources().getColor(R.color.white));
                tx_notification2.setTextColor(getResources().getColor(R.color.white));
                tx_Profile.setTextColor(getResources().getColor(R.color.white));
                tx_Profile2.setTextColor(getResources().getColor(R.color.white));
                tx_logout.setTextColor(getResources().getColor(R.color.white));

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        theme_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme_off.setVisibility(View.GONE);
                theme_on.setVisibility(View.VISIBLE);
                key = "1";
                SPmanager.saveValue(getActivity(), "themeKEY", key);
                lin_item.setBackgroundColor(getResources().getColor(R.color.white));
                tx_lightmode.setTextColor(getResources().getColor(R.color.darkDray));
                tx_notification.setTextColor(getResources().getColor(R.color.darkDray));
                tx_lightmode.setTextColor(getResources().getColor(R.color.darkDray));
                tx_lightmode2.setTextColor(getResources().getColor(R.color.darkDray));
                tx_notification.setTextColor(getResources().getColor(R.color.darkDray));
                tx_notification2.setTextColor(getResources().getColor(R.color.darkDray));

                img_daynight.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                img_notification.setColorFilter(ContextCompat.getColor(getActivity(), R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        notification_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_on.setVisibility(View.GONE);
                notification_off.setVisibility(View.VISIBLE);
                nKEY = "no";
                SPmanager.saveValue(getActivity(), "notification_Visibility", nKEY);

            }
        });

        notification_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_off.setVisibility(View.GONE);
                notification_on.setVisibility(View.VISIBLE);
                nKEY = "yes";
                SPmanager.saveValue(getActivity(), "notification_Visibility", nKEY);
            }
        });


        linProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SPmanager.getPreferenceBoolean(getActivity(), "isLogin");
                if (isLogin) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SPmanager.getPreferenceBoolean(getActivity(), "isLogin");
                if (isLogin) {
                    SPmanager.isLogOut(getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "First login your account", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (nKEY != null) {
            if (nKEY.equals("no")) {
                notification_off.setVisibility(View.VISIBLE);
                notification_on.setVisibility(View.GONE);
            } else if (nKEY.equals("yes")) {
                notification_off.setVisibility(View.GONE);
                notification_on.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

}