package com.mananews.apandts.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Model_Class.Model_Cat;
import com.mananews.apandts.R;

import java.util.ArrayList;

public class Adapter_Cat extends BaseAdapter {

    private static final String TAG = "Adapter cat";
    Context activity;
    LayoutInflater inflater;
    ArrayList<Model_Cat> catList;

    public Adapter_Cat(Context mainActivity, ArrayList<Model_Cat> catList) {

        try {
            activity = mainActivity;
            inflater = LayoutInflater.from(mainActivity);
            this.catList = catList;
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "Adapter_Cat: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return catList.size();

    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.item_cat, viewGroup, false);
        ImageView img_cat = (ImageView) view.findViewById(R.id.img_category);
        ImageView imgNext = (ImageView) view.findViewById(R.id.imgNext);
        TextView txt_catName = (TextView) view.findViewById(R.id.txt_catName);

        String path = "uploads/category-image/";
        String fullPath = activity.getString(R.string.server_url) + path;

        Log.e("hhhhh", "getView: " + fullPath);

        Glide.with(activity).load(fullPath + catList.get(i).getCatImg()).into(img_cat);
        Animation animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        img_cat.startAnimation(animFadeIn);

        txt_catName.setText(catList.get(i).getCategory_name());

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                txt_catName.setTextColor(activity.getResources().getColor(R.color.darkDray));
                img_cat.setColorFilter(ContextCompat.getColor(activity, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgNext.setColorFilter(ContextCompat.getColor(activity, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (MainActivity.themeKEY.equals("0")) {
                txt_catName.setTextColor(activity.getResources().getColor(R.color.white));
                img_cat.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgNext.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        }

        return view;
    }
}
