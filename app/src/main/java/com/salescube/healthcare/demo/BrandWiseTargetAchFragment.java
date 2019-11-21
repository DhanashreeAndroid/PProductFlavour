package com.salescube.healthcare.demo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.view.ProductSecondary;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrandWiseTargetAchFragment extends Fragment {

    private Context context;

    public BrandWiseTargetAchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand_wise_target_ach, container, false);

        Bundle bndle = getArguments();

        ProductSecondary[] sec = (ProductSecondary[])bndle.getParcelableArray("data");
        //setData(view, sec);

        setData(view, sec);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setData(View view, ProductSecondary[] data) {

        int row1BackColor = ContextCompat.getColor(context, R.color.tableRow1BackColor);
        int row1TextColor = ContextCompat.getColor(context, R.color.tableRow1TextColor);
        int row2BackColor = ContextCompat.getColor(context, R.color.tableRow2BackColor);
        int row2TextColor = ContextCompat.getColor(context, R.color.tableRow2TextColor);

        TextView tv;
        TableRow tr;
        int ix = 0;

        int rowColor;
        int textColor;

        TableLayout tblData = view.findViewById(R.id.tbl_product_secondary);

        for (ProductSecondary soc : data) {

            ix += 1;

            tr = new TableRow(context);
            //tr.setPadding(5, 5, 5, 5);

            if (ix % 2 == 1) {
                rowColor = row1BackColor;
                textColor = row1TextColor;
            } else {
                rowColor = row2BackColor;
                textColor = row2TextColor;
            }


            tv = new TextView(context);
            tv.setPadding(15, 0, 10, 0);
            tv.setText(soc.getProductName());
            tv.setTextColor(textColor);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
            tv.setTextSize(14);
            tv.setMinLines(2);
            //tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER | Gravity.LEFT);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(5, 0, 10, 0);
            tv.setText(Parse.ruppe(soc.getTargetValue()));
            tv.setTextColor(textColor);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
            tv.setTextSize(14);
            tv.setMinLines(2);
            //tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER | Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(5, 0, 10, 0);
            tv.setText(Parse.ruppe(soc.getAchValue()));
            tv.setTextColor(textColor);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
            tv.setTextSize(14);
            tv.setMinLines(2);
            tv.setGravity(Gravity.CENTER | Gravity.RIGHT);
            //tv.setBackgroundResource(R.drawable.table_cell_plain);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(5, 0, 10, 0);
            tv.setText(Parse.toStr(soc.getPercent()));
            tv.setTextColor(textColor);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 0.5f));
            tv.setTextSize(14);
            tv.setMinLines(2);
            tv.setGravity(Gravity.CENTER | Gravity.RIGHT);
            //tv.setBackgroundResource(R.drawable.table_cell_plain);
            tr.addView(tv);

            //tr.setBackgroundResource(R.drawable.table_row_bg);

            tr.setBackgroundColor(rowColor);

            tblData.addView(tr);

        }

    }
}
