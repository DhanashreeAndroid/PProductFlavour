package com.salescube.healthcare.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.CompetitorInfoRepo;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vCompetitorReport;

import java.util.List;

public class CompititorReportActivity extends BaseActivity {

    private LinearLayout mainLayout;
    private TableLayout tblHead;
    private ScrollView scrollView;
    private TableLayout tblData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compititor_report);

        try {
            initControls();
            initListner();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            finish();
        }

    }

    private void initControls() {
        mainLayout = (LinearLayout) findViewById(R.id.comp_rpt_rootLayout);

        tblHead = new TableLayout(CompititorReportActivity.this);
        mainLayout.addView(tblHead);

        scrollView = new ScrollView(CompititorReportActivity.this);
        mainLayout.addView(scrollView);

        tblData = new TableLayout(CompititorReportActivity.this);
        scrollView.addView(tblData);

        mainLayout.setPadding(5, 5, 5, 5);

    }

    private void initListner() {

    }

    private void initData() {
        addHeading();
    }

    private final static float WGT_OUR_PRODUCT = 2;
    private final static float WGT_PRODUCT = 2;
    private final static float WGT_GMS = 1;
    private final static float WGT_RET_RATE = 1.5f;
    private final static float WGT_SCHEME = 2;
    private final static float WGT_STOCK = 1;
    private final static float WGT_MRP = 1;

    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private void addHeading() {

        TableRow tr;
        int headingColor = Color.BLACK;

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        addHeadCell(tr, "Product", WGT_OUR_PRODUCT, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Comp.Product", WGT_PRODUCT, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Gms", WGT_GMS, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Ret.Rate", WGT_RET_RATE, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Scheme", WGT_SCHEME, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Stock", WGT_STOCK, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "MRP", WGT_MRP, Gravity.LEFT, tf, headingColor, true);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

        List<vCompetitorReport> workList = new CompetitorInfoRepo().getCompetitorReport(AppControl.getmEmployeeId());

        for (vCompetitorReport work : workList) {
            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);

            addHeadCell(tr, work.getTargetProduct(), WGT_OUR_PRODUCT, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, work.getProduct(), WGT_PRODUCT, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, work.getGrams(), WGT_GMS, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, String.valueOf(work.getRetailerRate()), WGT_RET_RATE, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, work.getScheme(), WGT_SCHEME, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, work.getStock(), WGT_STOCK, Gravity.LEFT, null, headingColor);
            addHeadCell(tr, String.valueOf(work.getMrp()), WGT_MRP, Gravity.LEFT, null, headingColor, true);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblData.addView(tr);
        }

    }


    private void addHeadCell(TableRow _tr, String _value, float _cellWeight, int _gravity, Typeface _tf, int _textColor) {
        addHeadCell(_tr, _value, _cellWeight, _gravity, _tf, _textColor, false);
    }

    private void addHeadCell(TableRow _tr, String _value, float _cellWeight, int _gravity, Typeface _tf, int _textColor, boolean isLastCell) {

        TextView tv = new TextView(this);

        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(_value);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, _cellWeight));
        tv.setTextSize(FONT_SIZE);
        if (isLastCell) {
            tv.setBackgroundResource(R.drawable.table_cell_plain);
        } else {
            tv.setBackgroundResource(R.drawable.table_cell_bg);
        }
        tv.setGravity(_gravity);
        if (_tf != null) {
            tv.setTypeface(_tf);
        }
        tv.setTextColor(_textColor);

        _tr.addView(tv);
    }

}
