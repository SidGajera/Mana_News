package com.mananews.apandts.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.R;


public class Into_Adapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    String[] onbording_title, onbording_desc;

    public int[] slideImages = {
            R.drawable.ic_onboard_img_1,
            R.drawable.ic_onboard_img_2,
            R.drawable.ic_onboard_img_3
    };

    public Into_Adapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_image, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView tx_slide_heading = view.findViewById(R.id.tx_slide_heading);
        TextView tx_slide_description = view.findViewById(R.id.tx_slide_description);

        Resources res = context.getResources();
        onbording_title = res.getStringArray(R.array.onbording_title);
        onbording_desc = res.getStringArray(R.array.onbording_desc);

        imageView.setImageResource(slideImages[position]);
        tx_slide_heading.setText(onbording_title[position]);
        tx_slide_description.setText(onbording_desc[position]);

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                tx_slide_heading.setTextColor(context.getResources().getColor(R.color.darkDray));
                tx_slide_description.setTextColor(context.getResources().getColor(R.color.darkDray));
            } else if (MainActivity.themeKEY.equals("0")) {
                tx_slide_heading.setTextColor(context.getResources().getColor(R.color.darkDray));
                tx_slide_description.setTextColor(context.getResources().getColor(R.color.darkDray));
            }
        } else {
            tx_slide_heading.setTextColor(context.getResources().getColor(R.color.yellow));
            tx_slide_description.setTextColor(context.getResources().getColor(R.color.white));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);

    }
}
