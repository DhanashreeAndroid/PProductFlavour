package com.salescube.healthcare.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.vDayreportRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.ApiManager;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.sysctrl.XProgressBar;
import com.salescube.healthcare.demo.view.vDayReport;
import com.salescube.healthcare.demo.view.vMonthReport;
import com.salescube.healthcare.demo.view.vMonthYear;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SoMonthReportActivity extends BaseActivity {

    private TableLayout tblHead;
    private TableLayout tblSpn;
    private LinearLayout tblData;
    private Spinner mSpn;

    private Context mContext;
    private Handler mHandler;

    private LinearLayout mTblTotal;
    private vDayreportRepo repo;


    private double percent = 0;
    private double tc = 0;
    private double pc = 0;
    private double totalTc = 0;
    private double achievement = 0;
    private double targetAmount = 0;
    private double  cumulativeTarget = 0;

    private String pdfPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_month_report);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        ImageView shareReport = getWindow().findViewById(R.id.shareReport);
        if (titleBar != null) {
            titleBar.setText("SO Wise Monthly Achievement");
        }

        shareReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File outputFile = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS), "MonthReport.pdf");

                Uri uri;
                if(android.os.Build.VERSION.SDK_INT >= 24){

                    uri = FileProvider.getUriForFile(SoMonthReportActivity.this, getApplicationContext().getPackageName() + ".provider", outputFile);

                }else {

                    uri = Uri.fromFile(outputFile);
                }

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Day wise Report");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));


            }
        });

        mContext = SoMonthReportActivity.this;

        try {
            initControls();
            initData();
            initListener();

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {
        mSpn = (Spinner) findViewById(R.id.so_day_report_spn_month_year);
        tblSpn = (TableLayout) findViewById(R.id.so_day_report_tbl_month_year);
        tblSpn.setVisibility(View.VISIBLE);
        tblHead = (TableLayout) findViewById(R.id.so_day_report_tbl_head);
        tblData = (LinearLayout) findViewById(R.id.main);
        mTblTotal = findViewById(R.id.so_month_report_tbl_total1);

    }

    private void initData() {

        loadGridHeader();
        SpinnerHelper.FillMonthYear(mSpn, 0, 3);

        Date today = DateFunc.getDate();

        String fromDate = DateFunc.FO_MonthStr(today);
        String toDate = DateFunc.EO_MonthStr(today);

        /*try {
            new DownloadDayReport(mContext, mHandler, fromDate, toDate).execute();
        } catch (Exception ex) {
            ex.getMessage();
        }*/
    }

    private void initListener() {
        mSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vMonthYear date = (vMonthYear) mSpn.getSelectedItem();
                if (date != null) {

                    new DownloadDayReport(mContext, mHandler, date.getFromDate(), date.getToDate()).execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public final static float WGT_SO_NAME = 2;
    public final static float WGT_MONTH_TARGET = 2;
    public final static float WGT_CUMULATIVE_TARGET = 2;
    public final static float WGT_CUMULATIVE_ACHIEVEMENT = 2;
    public final static float WGT_ACH_PERCENT = 2;
    public final static float WGT_TC_PC = 1;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private void loadGridHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("SO Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Monthly Target");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_MONTH_TARGET));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Cumulative Target");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_TARGET));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Cumulative Ach.");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_ACHIEVEMENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Ach. %");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACH_PERCENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("TC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("PC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);


        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("%");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);


        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);


    }

    private void LoadToatl(vMonthReport dayReport) {
        mTblTotal.removeAllViews();

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        double percent = (int) (dayReport.getAchPer());

        int tc = (int) (dayReport.getTc());
        int pc = (int) (dayReport.getPc());
        int totTC = (int) (dayReport.getTotalTC());
        int resId = R.drawable.table_cell_bg;

        double tgt = dayReport.getCumulativeTargetAmount();
        double cAch = dayReport.getCumulativeAchAmount();

        if (tgt == 0 && cAch > 0) {
            percent = 100;
        } else {
            if (tgt > 0) {
                percent = ((cAch * 100 / tgt));
            } else {
                percent = 0;
            }
        }

        if (percent < 90) {
            resId = R.drawable.table_cell_red;
        } else if (percent <= 99) {
            resId = R.drawable.table_cell_yellow;
        } else {
            resId = R.drawable.table_cell_green;

        }

        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(dayReport.getSoName());
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(dayReport.getMonthTargetAmount()));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_MONTH_TARGET));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(dayReport.getCumulativeTargetAmount()));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_TARGET));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        int ach = (int) Math.round(dayReport.getCumulativeAchAmount());
        tv.setText(Parse.toStr(ach));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_ACHIEVEMENT));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(Math.round(percent)) + "  %");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACH_PERCENT));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(tc));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(pc));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_bg);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);


        tv = new TextView(mContext);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(Parse.toStr(totTC) + " %");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
        tv.setTextSize(FONT_SIZE);

        tv.setBackgroundResource(R.drawable.table_cell_plain);

        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        mTblTotal.addView(tv);

        mTblTotal.setBackgroundResource(R.drawable.table_row_header);


    }




    public class DownloadDayReport extends AsyncTask<Object, String, Boolean> {


        //private Handler mHandler = null;

        private Context mContext;

        private Gson gson;
        private OkHttpClient client;

        private String fromDate;
        private String toDate;
        private vMonthReport[] reportList;

        public DownloadDayReport(Context _context, Handler _handler, String _fromDate, String _toDate) {
            this.mContext = _context;

            this.fromDate = _fromDate;
            this.toDate = _toDate;
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                downloadReport();
            } catch (Exception e) {
                Logger.e(e.getMessage());
                UtilityFunc.dismissDialog();
            }

            return null;
        }

        private void downloadReport() throws FileNotFoundException, DocumentException {

            client = new OkHttpClient();
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            String url;
            String responseStr = "";

            publishProgress("Downloading SO day report...");
            url = ApiManager.SOReport.getMonthReport(AppControl.getmEmployeeId(), fromDate, toDate);
            responseStr = getRequestString(url);


            if (responseStr != "") {
                reportList = gson.fromJson(responseStr, vMonthReport[].class);

                repo = new vDayreportRepo();
                repo.deleteAll();
                repo.insert(reportList);

                exportDatabaseToPdf(reportList);


                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {

                        vMonthReport report = reportList[0];
                        String role = report.getSoRole();

                        vMonthReport[] rptrList = repo.getList1(role).toArray(new vMonthReport[0]);

                        DisplayReport(rptrList);


                    }


                });

            }



        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void DisplayReport(vMonthReport[] rptrList) {

            for (vMonthReport report : rptrList) {
                diplayData(report, 0, null, "");
            }

        }


        private void diplayData(final vMonthReport dayReport, int status, final LinearLayout child_, String OwnData) {

            // tblData.removeAllViews();

            LayoutInflater inflater_chapter = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mLinearView_Chapter = inflater_chapter.inflate(R.layout.expand_child, null);
            final LinearLayout main = mLinearView_Chapter.findViewById(R.id.so_day_report_tbl_data);
            final LinearLayout child = mLinearView_Chapter.findViewById(R.id.divider);


            final boolean[] btnclicked = {false};
            final TableRow tr;
            TextView tv;
            Typeface tf;


            percent = 0;
            tc = 0;
            pc = 0;
            totalTc = 0;
            achievement = 0;
            targetAmount = 0;
            cumulativeTarget = 0;


            final LinearLayout layout;

            int headingColor = Color.BLACK;

            layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);


            final String role = dayReport.getSoRole();
            final String soName = dayReport.getSoName();

            int drawableLayout;

            if (role.equalsIgnoreCase("ASM")) {
                drawableLayout = R.drawable.table_cell_asm;
            } else if (role.equalsIgnoreCase("RSM")) {
                drawableLayout = R.drawable.table_cell_rsm;
            } else if (role.equalsIgnoreCase("ZSM")) {
                drawableLayout = R.drawable.table_cell_zsm;
            } else if (role.equalsIgnoreCase("NSM")) {
                drawableLayout = R.drawable.table_cell_nsm;
            } else {
                drawableLayout = R.drawable.table_cell_bg;
            }

            tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

            layout.setPadding(5, 10, 5, 10);


            int soId = dayReport.getSoId();

            getTotalData(soId);

            if (!OwnData.equalsIgnoreCase("")){

                percent = dayReport.getAchPer();
                tc =dayReport.getTc();
                pc =dayReport.getPc();
                totalTc = dayReport.getTotalTC();
                achievement = dayReport.getCumulativeAchAmount();
                targetAmount = dayReport.getMonthTargetAmount();

                drawableLayout = R.drawable.table_cell_bg;

            }else {

                percent = percent + dayReport.getAchPer();
                tc = tc + dayReport.getTc();
                pc = pc + dayReport.getPc();
                totalTc = totalTc + dayReport.getTotalTC();
                achievement = achievement + dayReport.getCumulativeAchAmount();
                targetAmount = targetAmount + dayReport.getMonthTargetAmount();

            }




            int resId = R.drawable.table_cell_bg;

            if (percent < 90) {
                resId = R.drawable.table_cell_red;
            } else if (percent <= 99) {
                resId = R.drawable.table_cell_yellow;
            } else {
                resId = R.drawable.table_cell_green;
            }



            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            //tv.setText(dayReport.getSoName());
            if (!OwnData.equalsIgnoreCase("")){

                tv.setText(dayReport.getSoName()+ " "+"(Own)");
            }else {
                tv.setText(dayReport.getSoName());
            }
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(" "+dayReport.getMonthTargetAmount());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_MONTH_TARGET));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int amt = (int) Math.round(cumulativeTarget);
            tv.setText(Parse.toStr(amt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_TARGET));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int amt1 = (int) Math.round(achievement);
            tv.setText(Parse.toStr(amt1));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_CUMULATIVE_ACHIEVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);

            int amt2 = (int) Math.round(percent);
            tv.setText(Parse.toStr(amt2));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACH_PERCENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(""+tc);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(""+pc);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);


            tv = new TextView(getApplicationContext());
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(""+totalTc);
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC_PC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(drawableLayout);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            layout.addView(tv);

            layout.setBackgroundResource(R.drawable.table_row_total_border);


            if (status == 0) {
                main.addView(layout);
            } else if (status == 1) {
                main.addView(layout);
                child_.addView(mLinearView_Chapter);

            }

            if (OwnData.equalsIgnoreCase("")){

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (btnclicked[0] == false) {

                            child.setVisibility(View.VISIBLE);

                            int soId = dayReport.getSoId();

                            child.removeAllViews();

                            vMonthReport[] rptrList = repo.getListonId1(soId).toArray(new vMonthReport[0]);

                            if (rptrList.length != 0) {

                                diplayData(dayReport, 1, child, "Own");

                                for (int i = 0; i < rptrList.length; i++) {
                                    diplayData(rptrList[i], 1, child, "");
                                }
                            }
                            btnclicked[0] = true;
                        } else {
                            child.setVisibility(View.GONE);
                            btnclicked[0] = false;

                        }

                    }
                });

            }





            if (status == 0) {
                tblData.addView(mLinearView_Chapter);
            }


        }






        private Response getRequest(String url) {

            if (isCancelled()) {
                return null;
            }

            Response response = null;
            Request request;

            try {
                request = new Request.Builder()
                        .url(url)
                        .addHeader("USER_NAME", AppControl.getUserName())
                        .addHeader("IMEI", MobileInfo.getIMEI(SoMonthReportActivity.this))
                        .build();
            } catch (Exception ex) {
                errorFound("GET_REQUEST", ex, "Error!", "Error while building request");
                return response;
            }

            try {
                response = client.newCall(request).execute();
            } catch (SocketTimeoutException ex) {
                errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Connection timeout! Server is busy or not available, please try again.");
                return response;
            } catch (UnknownHostException ex) {
                errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Failed to connect server, please check your internet connection and try again");
            } catch (Exception ex) {
                errorFound("GET_REQUEST", ex, "Unknwon Error!", "");
                return response;
            }

            return response;

        }

        private String getRequestString(String url) {

            if (isCancelled()) {
                return "";
            }

            String responseStr = "";
            Response response = getRequest(url);

            if (response != null) {

                try {
                    responseStr = response.body().string();
                } catch (Exception e) {
                    Logger.log(Logger.ERROR, "GET_REQUEST", e.getMessage(), e);
                    return "";
                }

                if (response.code() == HttpURLConnection.HTTP_OK) {

                    if (isJSONValid(responseStr)) {
                        return responseStr;
                    }
                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
                } else {
                    errorFound("DOWNLOADER", null, "Unknown Error!", response.message());
                }
            }

            return "";
        }

        public boolean isJSONValid(String test) {

            if (test.length() <= 4) {
                return false;
            }

            try {
                new JSONObject(test);
            } catch (JSONException ex) {
                try {
                    new JSONArray(test);
                } catch (JSONException ex1) {
                    return false;
                }
            }
            return true;
        }

        private void errorFound(String tag, Throwable e, String title, String customMsg) {
            this.cancel(true);
            String excepMsg = "";

            if (e != null) {
                Logger.log(Logger.ERROR, tag, e.getMessage(), e);
                excepMsg = e.getMessage();
            }

            if (customMsg == "") {
                sendAlert(title, excepMsg);
            } else {
                sendAlert(title, customMsg);
            }
        }

        private void sendAlert(String title, String message) {

            Message msg = new Message();

            List<String> appMsg = new ArrayList<>();
            appMsg.add(title);
            appMsg.add(message);
            msg.obj = appMsg;

            msg.arg1 = Constant.MessageType.Alert;

            mHandler.sendMessage(msg);
        }

        private void getTotalData(int soId) {

            vDayReport[] rptrList = repo.getListonId(soId).toArray(new vDayReport[0]);
            if (rptrList.length != 0) {
                for (int i = 0; i < rptrList.length; i++) {

                    vDayReport report = rptrList[i];
                    int id = report.getSoId();

                    percent = percent + report.getAchPer();
                    tc = tc + report.getTc();
                    pc = pc + report.getPc();
                    totalTc = totalTc + report.getTotalTc();
                    achievement = achievement + report.getAchAmount();
                    targetAmount = targetAmount + report.getTargetAmount();

                    cumulativeTarget = cumulativeTarget + report.getCumulative_target_amt();

                    getTotalData1(report);


                }

            }
        }

        private void getTotalData1(vDayReport report) {

            int id = report.getSoId();

            vDayReport[] rptrList1 = repo.getListonId(id).toArray(new vDayReport[0]);
            if (rptrList1.length != 0) {
                for (int i = 0; i < rptrList1.length; i++) {

                    vDayReport report1 = rptrList1[i];
                    //int soId = report.getSoId();

                    percent = percent + report1.getAchPer();
                    tc = tc + report1.getTc();
                    pc = pc + report1.getPc();
                    totalTc = totalTc + report1.getTotalTc();

                    double ach = report1.getAchAmount();

                    Log.d("Achievemnet Data", "" + ach);
                    achievement = achievement + ach;
                    targetAmount = targetAmount + report1.getTargetAmount();

                    cumulativeTarget = cumulativeTarget + report1.getCumulative_target_amt();

                    getTotalData1(report1);


                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!UtilityFunc.IsConnected(SoMonthReportActivity.this)) {
                sendAlert("No Connectivity!", "Internet connection not available, please check your network.");
                cancel(true);
                return;
            }
            UtilityFunc.showDialog(SoMonthReportActivity.this, "", "");

        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    UtilityFunc.dismissDialog();

                    vMonthReport report = new vMonthReport();
                    double tc = 0,pc = 0 , monthTarget =0, cumTarget =0, cumAch =0;
                    for(int i =0 ; i< reportList.length; i++){
                        tc += reportList[i] .getTc();
                        pc += reportList[i] .getPc();
                        monthTarget += reportList[i].getMonthTargetAmount();
                        cumTarget += reportList[i].getCumulativeTargetAmount();
                        cumAch += reportList[i].getCumulativeAchAmount();
                    }
                    report.setSoName("Total");
                    report.setMonthTargetAmount(monthTarget);
                    report.setCumulativeTargetAmount(cumTarget);
                    report.setCumulativeAchAmount(Math.round(cumAch));
                    report.setPc(pc);
                    report.setTc(tc);
                    if(tc != 0) {
                        report.setTotalTC(Math.round((pc * 100) / tc));
                    }

                    LoadToatl(report);

                }
            });

        }

        private void sendMessage(String message) {
            Message msg = new Message();
            msg.obj = message;
            mHandler.sendMessage(msg);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    @Override
    protected void onDestroy() {
        UtilityFunc.dismissDialog();
        super.onDestroy();
    }

    public String exportDatabaseToPdf(vMonthReport[] reportlist)  throws FileNotFoundException, DocumentException {


        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return "";
        }
        else
        {
            //We use the Download directory for saving our .csv file.
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /*    if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }*/
            exportDir.mkdirs();
            File file = new File(exportDir, "MonthReport.pdf");

            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph p3 = new Paragraph();
            p3.add( "Monthly Sales Report" +"\n");
            p3.add("\n");
            p3.setAlignment(Element.ALIGN_CENTER);
            document.add(p3);



            PdfPTable table = new PdfPTable(8);

            table.addCell("Name");
            table.addCell("Monthly Target");
            table.addCell("Cumulative Target");
            table.addCell("Achievement");
            table.addCell("Ach(%)");
            table.addCell("TC");
            table.addCell("PC");
            table.addCell("%");


            int tc = 0,pc = 0 , target =0, ach =0;
            for (vMonthReport report : reportlist)
            {

                table.addCell(report.getSoName());
                table.addCell(String.valueOf(report.getMonthTargetAmount()));
                table.addCell(String.valueOf(report.getCumulativeTargetAmount()));
                table.addCell(String.valueOf(report.getCumulativeAchAmount()));
                table.addCell(String.valueOf(report.getAchPer()));
                table.addCell(String.valueOf(report.getTc()));
                table.addCell(String.valueOf(report.getPc()));
                table.addCell(String.valueOf(report.getTotalTC()));

                tc += report.getTc();
                pc += report.getPc();
                target += report.getMonthTargetAmount();
                ach +=report.getCumulativeAchAmount();



            }

            table.addCell("Total");
            table.addCell(String.valueOf(target));
            table.addCell(String.valueOf(ach));
            if(tc != 0) {
                int achper = ((pc * 100) / tc);
                table.addCell(String.valueOf(achper));
            }
            table.addCell(String.valueOf(tc));
            table.addCell(String.valueOf(pc));
            if(target != 0) {
                int totalTc = ((ach * 100) / target);
                table.addCell(String.valueOf(totalTc));
            }


            document.add(table);
            document.addCreationDate();
            document.close();


            pdfPath = file.getAbsolutePath();

            return file.getAbsolutePath();
        }

    }


}
