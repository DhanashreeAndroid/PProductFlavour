package com.salescube.healthcare.demo.ctrl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

// http://www.gadgetsaint.com/tips/dynamic-square-rectangular-layouts-android/
public class SqareFrameLayout extends FrameLayout {

    public SqareFrameLayout(Context context) {
        super(context);
    }


    public SqareFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SqareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


// here we are returning the width in place of height, so width = height
// you may modify further to create any proportion you like ie. height = 2*width etc

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }

}
