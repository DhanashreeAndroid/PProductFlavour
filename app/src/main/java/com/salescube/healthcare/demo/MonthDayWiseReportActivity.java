package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.DayWiseAchievementReport;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthDayWiseReportActivity extends BaseActivity {

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float FONT_SIZE = 16;
    private static final float WGT_PRODUCT_NAME = 1.4f;
    private static final float WGT_TGT_ACH_COL = 1.5f;
    private static final float WGT_COL = 0.8f;

    List<DayWiseAchievementReport> lstProductReport, fetchSortedList;
    MonthDayWiseDataAdapter dataAdapter;

    private TableLayout mTblHead;
    private LinearLayout mTblTotal;

    private double totTC, totPC, totTgt, totAch, totValue, totalTC;

    private SimpleDateFormat dateFormatter;
    private Date fDate, tDate;
    private SimpleDateFormat cnvrtFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private TextView mFromDate;
    private TextView mToDate;
    private RecyclerView mProductData;
    private Button viewReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_month_day_report);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null) {
            titleBar.setText("Day Wise Target / Ach");
        }

        try {
            initControls();
            initData();
            initListener();
//            addTotal();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }


    private void initControls() {
        totAch = 0;
        totPC = 0;
        totTC = 0;
        totValue = 0;
        totTgt = 0;
        totalTC = 0;

        lstProductReport = new ArrayList<>();
        fetchSortedList = new ArrayList<>();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cnvrtFormatter = new SimpleDateFormat("yyyy-MM-dd");
//
        viewReport = findViewById(R.id.month_day_btn_report);
        mFromDate = findViewById(R.id.month_day_tv_from_date);
        mToDate = findViewById(R.id.month_day_tv_to_date);
        mTblHead = findViewById(R.id.month_day_tbl_header);
        mTblTotal = findViewById(R.id.month_day_ll_total);
        mProductData = findViewById(R.id.month_day_rcv_lst);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MonthDayWiseReportActivity.this);
        mProductData.setLayoutManager(layoutManager);
    }

    private void initListener() {
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSortedList = new ArrayList<>();
                fDate = DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy");
                tDate = DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy");

                fetchDayWiseReport(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(fDate), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(tDate), cnvrtFormatter));
            }
        });
        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                fromDatePickerDialog.show();
            }
        });
        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                toDatePickerDialog.show();
            }
        });
    }

    private void setDateField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fDate = DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()), "dd-MM-yyyy");
                if (fDate.before(DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy")) || fDate.equals(DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy"))) {
//                    /if(mToDate.getError().equals(true)){
                    mFromDate.setError(null);
                    mFromDate.clearFocus();
//                    }
                    mFromDate.setText(DateFunc.getDateStrSimple(fDate));
                } else {
                    mFromDate.setError("Please select before To Date");
                    Toast.makeText(MonthDayWiseReportActivity.this, "Please select before To Date", Toast.LENGTH_SHORT).show();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tDate = DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()), "dd-MM-yyyy");
                if (tDate.after(DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy")) || tDate.equals(DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy"))) {
//                    /if(mToDate.getError().equals(true)){
                    mToDate.setError(null);
                    mToDate.clearFocus();
//                    }
                    mToDate.setText(DateFunc.getDateStrSimple(tDate));
                } else {
                    mToDate.setError("Please select after From Date");
                    Toast.makeText(MonthDayWiseReportActivity.this, "Please select after From Date", Toast.LENGTH_SHORT).show();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void initData() {
        mFromDate.setText(DateFunc.getDateStrSimple(getFirstDateOfCurrentMonth()));
        mToDate.setText(DateFunc.getDateStrSimple(CurrentDate()));
        setDateField();
        loadGridHeader();
        fetchDayWiseReport(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(getFirstDateOfCurrentMonth()), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(CurrentDate()), cnvrtFormatter));
    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private Date CurrentDate() {
        return DateFunc.getDate();
    }

    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        if (mTblHead.getChildCount() == 0) {
            tr = new TableRow(this);
            tr.setPadding(5, 2, 5, 2);
//

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Date");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Tgt Value");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Ach Value");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("TC");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("PC");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblHead.addView(tr);
        }
    }


    private void fetchDayWiseReport(int soId, final Date fromDate, final Date toDate) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<DayWiseAchievementReport>> query = apiService.getMonthDayWiseAchievementReport(soId, DateFunc.getDateStr(fromDate, "yyyy-MM-dd"), DateFunc.getDateStr(toDate, "yyyy-MM-dd"));
        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<List<DayWiseAchievementReport>>() {
                @Override
                public void onResponse(Call<List<DayWiseAchievementReport>> call, Response<List<DayWiseAchievementReport>> response) {
                    if (response.isSuccessful()) {
                        List<DayWiseAchievementReport> reports = response.body();
                        lstProductReport.clear();
                        lstProductReport.addAll(reports);
                        loadDayList(fromDate, toDate, lstProductReport);
                        UtilityFunc.dismissDialog();
                    } else {
                        String message;
                        try {
                            message = response.errorBody().string();
                        } catch (Exception e) {
                            message = e.getMessage();
                            errMsg("While downloading", e);
                        }

                        if (message.equals("")) {
                            message = response.raw().message();
                        }
                        Alert("Error!", message);

                    }
                    UtilityFunc.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<DayWiseAchievementReport>> call, Throwable t) {
                    String message;

                    if (t instanceof IOException) {
                        message = t.toString();
                    }
                    if (t instanceof SocketTimeoutException) {
                        message = getString(R.string.connection_timeout);
                    } else if (t instanceof ConnectException) {
                        message = getString(R.string.no_connection);
                    } else {
                        message = getString(R.string.unknown_error);
                    }
                    UtilityFunc.dismissDialog();
                    Alert("Error!", message);
                }
            });
        }


    }


    @Override
    protected void onDestroy() {
        UtilityFunc.dismissDialog();
        super.onDestroy();
    }

    private void loadDayList(Date fDate, Date tDate, List<DayWiseAchievementReport> lstProductReport) {
     /*   List<DayWiseAchievementReport> lst2=new ArrayList<>();
//        dataAdapter=new ProductWiseDataAdapter(ProductWiseOrderActivity.this,fromDate,toDate,lstProductReport);
        for (DayWiseAchievementReport report : lstProductReport){
            if (report.getOrderDate().after(fDate) || report.getOrderDate().equals(fDate)) {
                if (report.getOrderDate().before(tDate) || report.getOrderDate().equals(tDate)) {
                    lst2.add(report);
                    if ((report.getOrderDate().equals(fDate) && report.getOrderDate().equals(tDate))) {
                        if(!lst2.contains(report))
                            lst2.add(report);
                    }
                }else {
                    lst2.remove(report);
                }
            }else {
                lst2.remove(report);
            }
        }*/
        Collections.sort(lstProductReport, new Comparator<DayWiseAchievementReport>() {
            @Override
            public int compare(DayWiseAchievementReport o1, DayWiseAchievementReport o2) {
                return (o1.getOrderDate().compareTo(o2.getOrderDate()));
            }
        });
        dataAdapter = new MonthDayWiseDataAdapter(MonthDayWiseReportActivity.this, lstProductReport);
        dataAdapter.notifyDataSetChanged();
        mProductData.setAdapter(dataAdapter);
        addTotal();
    }

    private class MonthDayWiseDataAdapter extends RecyclerView.Adapter<MonthDayWiseDataAdapter.ViewHolder> {
        Context mContext;
        List<DayWiseAchievementReport> lstProductReport;

        public MonthDayWiseDataAdapter(Context mContext, List<DayWiseAchievementReport> lstProductReport) {
            this.mContext = mContext;
            this.lstProductReport = lstProductReport;
        }

        @NonNull
        @Override
        public MonthDayWiseDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_order, parent, false);
            return new MonthDayWiseDataAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthDayWiseDataAdapter.ViewHolder holder, int position) {
            DayWiseAchievementReport report = lstProductReport.get(position);
            loadProductItem(holder, report);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        private void loadProductItem(@NonNull MonthDayWiseDataAdapter.ViewHolder holder, DayWiseAchievementReport report) {

            TableRow tr;
            TextView tv;

            tr = new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(DateFunc.getDateStrSimple(report.getOrderDate()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.toStr((int) report.getTargetAmount()));
            ;
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int qty = (int) Math.round(report.getAchievement());
            tv.setText(Parse.toStr(qty));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            double tgtVal =  report.getTargetAmount();
            double totAch =  report.getAchievement();

            if (report.getTargetAmount() == 0 && report.getAchievement() > 0) {
                report.setTotalAmount(100);
            } else {
                if (tgtVal > 0) {
                    report.setTotalAmount((totAch * 100 / tgtVal));
                } else {
                    report.setTotalAmount(0);
                }

            }

            tv.setText(Parse.toStr((int) Math.round(report.getTotalAmount())) + " %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.toStr((int) report.getTC()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.toStr((int) report.getPC()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.toStr((int) report.getTotalTC()) + " %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            holder.ll_product.removeAllViews();
            holder.ll_product.addView(tr);


        }

        @Override
        public int getItemCount() {
            if(lstProductReport != null  && lstProductReport.size() > 0){
                return lstProductReport.size();
            }else{
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TableLayout ll_product;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_product = itemView.findViewById(R.id.item_ll_prod);
            }
        }
    }

    private void addTotal() {
        totAch = 0;
        totPC = 0;
        totTC = 0;
        totValue = 0;
        totTgt = 0;
        totalTC = 0;

        TableRow tr;
        TextView tv;
        Context mContext = MonthDayWiseReportActivity.this;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);


        for (DayWiseAchievementReport report : lstProductReport) {
            totTgt += report.getTargetAmount();
            totAch += report.getAchievement();
            totTC += report.getTC();
            totPC += report.getPC();
        }
        totValue = ((totAch * 100) / totTgt);
        totalTC = ((totPC * 100) / (totTC));

        if (Double.isInfinite(totValue)) {
            totValue = 100;
        }
        if (totAch > totTgt) {
            totValue = 100;
        }

        if (Double.isInfinite(totalTC)) {
            totalTC = 0;
        }

        if (totPC > totTC) {
            totalTC = 100;
        }

        if (mTblTotal.getChildCount() == 0) {
            tr = new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Total :");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(headingColor);
            tv.setTypeface(tf);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int) totTgt));
            ;
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int ach = (int) Math.round(totAch);
            tv.setText(Parse.toStr(ach));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int val = (int) Math.round(totValue);
            tv.setText(Parse.toStr(((val) + " %")));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tc = (int) Math.round(totTC);
            tv.setText(Parse.toStr(tc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int pc = (int) Math.round(totPC);
            tv.setText(Parse.toStr(pc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tot = (int) Math.round(totalTC);
            tv.setText(Parse.toStr(tot) + " %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            mTblTotal.setBackgroundResource(R.drawable.table_row_header);
        } else {
            mTblTotal.removeAllViews();
            tr = new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Total :");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(headingColor);
            tv.setTypeface(tf);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int) totTgt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int ach = (int) Math.round(totAch);
            tv.setText(Parse.toStr(ach));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TGT_ACH_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int val = (int) Math.round(totValue);
            tv.setText(Parse.toStr(((val) + " %")));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tc = (int) Math.round(totTC);
            tv.setText(Parse.toStr(tc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int pc = (int) Math.round(totPC);
            tv.setText(Parse.toStr(pc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tot = (int) Math.round(totalTC);
            tv.setText(Parse.toStr(tot) + " %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            mTblTotal.setBackgroundResource(R.drawable.table_row_header);
        }
    }

}
