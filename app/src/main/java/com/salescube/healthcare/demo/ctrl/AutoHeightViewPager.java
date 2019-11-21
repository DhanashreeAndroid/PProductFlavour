package com.salescube.healthcare.demo.ctrl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


public class AutoHeightViewPager extends ViewPager {

    public AutoHeightViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = 0;
//
//        //View v = getChildAt(getCurrentItem());
//        ViewPagerAdapter tabsCircleAdapter = (ViewPagerAdapter) getAdapter();
//        //View v = tabsCircleAdapter.getCurrentItem(getCurrentItem());
//        View v = null;
//        if (tabsCircleAdapter != null) v = tabsCircleAdapter.getCurrentItem(getCurrentItem());
//
//        if(v != null) {
//
//            v.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//
//            height = v.getMeasuredHeight();
//
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        } else {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
//    }

}
