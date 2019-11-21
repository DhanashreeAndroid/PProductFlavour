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
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.OtherWorkRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vOtherWorkReport;

import java.util.List;

public class OtherWorkReportActivity extends BaseActivity {

    private LinearLayout mainLayout;
    private TableLayout tblHead;
    private ScrollView scrollView;
    private TableLayout tblData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_work_report);

        try {
            initControls();
            initListner();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            Alert("Error", e.getMessage());
        }

    }

    private void initControls() {
        mainLayout = (LinearLayout) findViewById(R.id.other_work_report_layout);

        tblHead = new TableLayout(OtherWorkReportActivity.this);
        mainLayout.addView(tblHead);

        scrollView = new ScrollView(OtherWorkReportActivity.this);
        mainLayout.addView(scrollView);

        tblData = new TableLayout(OtherWorkReportActivity.this);
//        if(tblData.getChildCount() > 0){
            scrollView.addView(tblData);
//        }else{
//            Toast.makeText(OtherWorkReportActivity.this,"No Data found...!!",Toast.LENGTH_SHORT).show();
//        }

        mainLayout.setPadding(5, 5, 5, 5);

    }

    private void initListner() {

    }

    private void initData() {
        addHeading();
    }

    private final static float WGT_DATE = 1;
    private final static float WGT_AGENT_NAME = 2;
    private final static float WGT_WORK = 3;

    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private void addHeading() {

        TableRow tr;
        int headingColor = Color.BLACK;

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        addHeadCell(tr, "Date", WGT_DATE, Gravity.LEFT, tf, headingColor);
        //addHeadCell(tr, "Agent", WGT_AGENT_NAME, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Work", WGT_WORK, Gravity.LEFT, tf, headingColor, true);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

        List<vOtherWorkReport> workList = new OtherWorkRepo().getOtherWorkReport(AppControl.getmEmployeeId());

        if(workList.size() > 0){
            for (vOtherWorkReport work : workList) {
                tr = new TableRow(this);
                tr.setPadding(5, 10, 5, 10);

                addHeadCell(tr, DateFunc.getDateStr(work.getTxnDate(), "dd/MM/yyyy"), WGT_DATE, Gravity.LEFT, null, headingColor);
                //addHeadCell(tr, work.getAgent(), WGT_AGENT_NAME, Gravity.LEFT, null, headingColor);
                addHeadCell(tr, work.getWork(), WGT_WORK, Gravity.LEFT, null, headingColor, true);

                tr.setBackgroundResource(R.drawable.table_row_bg);
                tblData.addView(tr);
            }
        }else{
            Toast.makeText(OtherWorkReportActivity.this,"No Data found...!!",Toast.LENGTH_SHORT).show();
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
