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
import com.salescube.healthcare.demo.data.repo.LeaveRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vLeaveReport;

import java.util.Date;
import java.util.List;

public class LeaveReportActivity extends BaseActivity {

    private LinearLayout mainLayout;
    private TableLayout tblHead;
    private ScrollView scrollView;
    private TableLayout tblData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_report);

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
        mainLayout = (LinearLayout) findViewById(R.id.leave_rpt_layout);

        tblHead = new TableLayout(this);
        mainLayout.addView(tblHead);

        scrollView = new ScrollView(this);
        mainLayout.addView(scrollView);

        tblData = new TableLayout(this);
        scrollView.addView(tblData);

        mainLayout.setPadding(5, 5, 5, 5);

    }

    private void initListner() {

    }

    private void initData() {
        addHeading();
    }

    private final static float WGT_FROM_DATE = 2;
    private final static float WGT_TO_DATE = 2;
    private final static float WGT_LEAVE_TYPE = 1;
    private final static float WGT_DURATION = 2;
    private final static float WGT_CO_FOR = 2;

    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private void addHeading() {

        TableRow tr;
        int headingColor = Color.BLACK;

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        addHeadCell(tr, "From Date", WGT_FROM_DATE, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "To Date", WGT_TO_DATE, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Leave", WGT_LEAVE_TYPE, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "Duration", WGT_DURATION, Gravity.LEFT, tf, headingColor);
        addHeadCell(tr, "CO For", WGT_CO_FOR, Gravity.LEFT, tf, headingColor, true);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

        List<vLeaveReport> workList = new LeaveRepo().getLeaveReport(AppControl.getmEmployeeId());

        if(workList.size() > 0 ){
            for (vLeaveReport work : workList) {
                tr = new TableRow(this);
                tr.setPadding(5, 10, 5, 10);

                addHeadCell(tr, DateFunc.getDateStr(work.getFromDate(), "dd/MM/yyyy"), WGT_FROM_DATE, Gravity.LEFT, null, headingColor);
                addHeadCell(tr, DateFunc.getDateStr(work.getToDate(), "dd/MM/yyyy"), WGT_FROM_DATE, Gravity.LEFT, null, headingColor);
                addHeadCell(tr, work.getLeaveType(), WGT_LEAVE_TYPE, Gravity.LEFT, null, headingColor);
                addHeadCell(tr, work.getDuration(), WGT_DURATION, Gravity.LEFT, null, headingColor);

                Date co_for = work.getCoFor();
                if (co_for != null) {
                    addHeadCell(tr, DateFunc.getDateStr(work.getCoFor(), "dd/MM/yyyy"), WGT_FROM_DATE, Gravity.LEFT, null, headingColor, true);
                }else{
                    addHeadCell(tr, "-", WGT_FROM_DATE, Gravity.LEFT, null, headingColor, true);
                }

                tr.setBackgroundResource(R.drawable.table_row_bg);
                tblData.addView(tr);
            }
        }else{
            Toast.makeText(LeaveReportActivity.this,"No Data found...!!",Toast.LENGTH_SHORT).show();
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
