package com.salescube.healthcare.demo.ctrl;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;

import java.util.List;

/**
 * Created by user on 30/01/2018.
 */

public class TSAdapter  extends ArrayAdapter {

    Typeface tf;
    int colorId;

    public TSAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);

        tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        colorId =  this.getContext().getResources().getColor(R.color.colorGray);
    }

    public TSAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setPadding(0, 0, view.getPaddingRight(), view.getPaddingBottom());

        TextView spinnerText = (TextView)view;
        spinnerText.setTypeface(tf);
        spinnerText.setTextColor(colorId);
        return view;
    }
}