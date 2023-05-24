package com.mananews.apandts.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.mananews.apandts.Adapter.Into_Adapter;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.SPmanager;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Into_Adapter slider_adapter;
    private LinearLayout lin_dots, linIntro;
    private TextView[] mDots;
    private TextView txtGetStarted;
    private TextView textview;
    private Button buttonNext;
    private int mCurrentPage;
    private RelativeLayout relIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        init();
//        getAppTheme();
    }

    private void init() {
        SPmanager.setFirsttime(IntroActivity.this, true);

        viewPager = findViewById(R.id.viewPager);
        lin_dots = findViewById(R.id.lin_dots);

        slider_adapter = new Into_Adapter(this);
        viewPager.setAdapter(slider_adapter);
        buttonNext = findViewById(R.id.btn_next);
        linIntro = findViewById(R.id.linIntro);
        relIntro = findViewById(R.id.relIntro);
        txtGetStarted = findViewById(R.id.txtGetStarted);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListner);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buttonNext.getText().toString().equalsIgnoreCase("next")) {
                    viewPager.setCurrentItem(mCurrentPage + 1);
                } else {
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                }

            }
        });

        txtGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addDotsIndicator(int position) {

        mDots = new TextView[3];
        lin_dots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(50);
            mDots[i].setTextColor(getResources().getColor(R.color.color_gray));
            lin_dots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.yellow));
        }

    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            mCurrentPage = position;

            if (position == 0) {//we are on first page
                buttonNext.setEnabled(true);
                buttonNext.setText("Next");
                txtGetStarted.setVisibility(View.GONE);
                lin_dots.setVisibility(View.VISIBLE);


            } else if (position == mDots.length - 1) { //last page

                txtGetStarted.setVisibility(View.VISIBLE);
                lin_dots.setVisibility(View.GONE);
                buttonNext.setVisibility(View.INVISIBLE);

            } else { //neither on first nor on last page
                buttonNext.setEnabled(true);
                txtGetStarted.setVisibility(View.GONE);
                buttonNext.setText("Next");
                lin_dots.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}