package com.salescube.healthcare.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.view.vHoReport;

import java.util.List;

public class HoReportActivity extends BaseAppCompatActivity {

    private TableLayout tblHead;
    private TableLayout tblData;
    private TextView lblStatus;

    private final static float WGT_REPORT = 6;
    private final static float WGT_STATUS = 4;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 2;
    private final static int RIGHT_PADDING = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_report);
        setTitle("Pending Records");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            initControls();
            addHeader();
            showOrders();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void initControls() {

        tblHead = (TableLayout) findViewById(R.id.ho_report_tbl_head);
        tblData = (TableLayout) findViewById(R.id.ho_report_tbl_data);
        lblStatus = (TextView) findViewById(R.id.ho_report_lbl_status);
    }

    private void addHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Report");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_REPORT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Status");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_STATUS));
        tv.setTextSize(FONT_SIZE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);


        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

    }

    private void showOrders() {

        List<vHoReport> reports = new TableInfoRepo().getHOReport();

        if (reports != null) {
            loadData(reports);
        }
    }

    private void loadData(List<vHoReport> reports) {

        TableRow tr;
        TextView tv;
        int dataCellColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        tblData.removeAllViews();
        int pendingCount = 0;

        for (vHoReport report : reports) {

            tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(report.getReport());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_REPORT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(report.getStatus()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_STATUS));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblData.addView(tr);

            pendingCount += report.getRecordCount();

        }

        if (pendingCount > 0){
            lblStatus.setText(String.format("Pending (%s)", pendingCount));
            lblStatus.setBackgroundResource( R.drawable.button_danger);
        }else{
            lblStatus.setText("Done");
            lblStatus.setBackgroundResource( R.drawable.button_success);
        }
    }

}
