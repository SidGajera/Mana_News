package com.mananews.apandts.utils;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class FlipPageViewTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        float percentage = 1 - Math.abs(position);
        page.setCameraDistance(12000);
        setVisibility(page, position);
        setTranslation(page);
        setSize(page, position, percentage);
        setRotation(page, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View page) {
        ViewPager2 viewPager = (ViewPager2) page.getParent();
        int scroll = viewPager.getScrollX() - page.getLeft();
        page.setTranslationX(scroll);
    }

    private void setSize(View page, float position, float percentage) {
        page.setScaleX((position != 0 && position != 1) ? percentage : 1);
        page.setScaleY((position != 0 && position != 1) ? percentage : 1);
    }

    private void setRotation(View page, float position, float percentage) {
        if (position > 0) {
            page.setRotationY(-180 * (percentage + 1));
        } else {
            page.setRotationY(180 * (percentage + 1));
        }
    }
}
