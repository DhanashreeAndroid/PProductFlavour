package com.salescube.healthcare.demo;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.SOMonthPerformance;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vDailySalesReport;
import com.salescube.healthcare.demo.view.vMonthYear;
import com.salescube.healthcare.demo.view.vSO;
import com.salescube.healthcare.demo.view.vSOMonthPerformance;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoMonthPerformanceReportActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private TableLayout TblSpn;
    private Spinner mSpn;
    private Spinner mSpnSO;

    private TableLayout TblHead;
    private LinearLayout mTblTotal;

    private RecyclerView mSOProductData;

    private final static int LEFT_PADDING = 5;
    private final static int RIGHT_PADDING = 10;
    private static final float FONT_SIZE = 18;
    private static final float WGT_DAY = 1.6f;
    private static final float WGT_ACHIVEMENT = 1.2f;
    private static final float WGT_TC = 0.6f;

    private List<SOMonthPerformance> lstSoPerformance;
    SoMonthPerformanceadapter adapter;
    private double totTC,totPC,totTgt,totAch,totValue,totalTC;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        setContentView(R.layout.activity_month_so_report);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_month_so_report);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null){
            titleBar.setText("SO Wise Performance");
        }

        mContext = SoMonthPerformanceReportActivity.this;

        try
        {
            initControls();
            initData();
            initListener();
        }catch(Exception e){
            Logger.e(e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initListener() {
        mSpn.setOnItemSelectedListener(this);
        mSpnSO.setOnItemSelectedListener(this);
    }

    private void initControls() {
        totAch=0;
        totPC=0;
        totTC=0;
        totValue=0;
        totTgt=0;
        totalTC=0;


        lstSoPerformance=new ArrayList<>();

        TblSpn=findViewById(R.id.so_month_report_tbl_spinner);
        TblSpn.setVisibility(View.VISIBLE);

        mSpn=findViewById(R.id.so_month_report_spn_month_year) ;
        mSpnSO=findViewById(R.id.so_month_report_spn_so) ;

        mTblTotal =findViewById(R.id.so_month_report_tbl_total);
        TblHead=findViewById(R.id.so_month_report_tbl_head);
        mSOProductData=findViewById(R.id.so_month_report_rcv_lst);
        mSOProductData.setLayoutManager(new LinearLayoutManager(SoMonthPerformanceReportActivity.this));
    }

    private void initData() {
        SpinnerHelper.FillMonthYear(mSpn,0,3);
        loadGridHeader();

        Date today=DateFunc.getDate();

//        String fromDate = DateFunc.FO_MonthStr(today);
//        String toDate = DateFunc.EO_MonthStr(today);
//        fetchSOMOnthPerformance(AppControl.getmEmployeeId(),fromDate,toDate);
    }

    private void fetchSOMOnthPerformance(int soId, String fromDate, String toDate) {
        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<vSOMonthPerformance> query = apiService.getSOMonthPerformance(soId,fromDate,toDate);
        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<vSOMonthPerformance>() {
                @Override
                public void onResponse(Call<vSOMonthPerformance> call, Response<vSOMonthPerformance> response) {
                    if (response.isSuccessful()) {

                        List<SOMonthPerformance> reports = response.body().getData();
                        List<vSO> soList = response.body().getSoList();

                        lstSoPerformance.clear();
                        lstSoPerformance.addAll(reports);

                        // loadEmployeeProductList(reports);

                        loadSpinner(soList, mSpnSO);
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
                public void onFailure(Call<vSOMonthPerformance> call, Throwable t) {
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


    private List<vDailySalesReport> insertDumm1List() {
        List<vDailySalesReport> lst = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                lst.add(i, new vDailySalesReport(i, "All", i, "Product" + i, (i * 5000.01), (i * 5000.01), ((i + 1) * 20)));
            } else {
                lst.add(i, new vDailySalesReport(i, "SO  " + i, i, "Product" + i, (i * 5000.01), (i * 5000.01), ((i + 1) * 20)));
            }
        }
        return lst;
    }

    private static void loadSpinner(List<vSO> lstProduct, Spinner mSpnSO) {

        lstProduct.add(0,new vSO(0,"--SELECT--"));
        ArrayAdapter adp = new ArrayAdapter<>(mSpnSO.getContext(), android.R.layout.simple_spinner_item, lstProduct);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSO.setAdapter(adp);
    }


    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

//        if(mTblHead.getChildCount()==0) {
        tr = new TableRow(this);
        tr.setPadding(5, 2, 5, 2);


        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Date");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DAY));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("Tgt Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("Ach Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("%");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("TC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("PC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("%");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);


        tr.setBackgroundResource(R.drawable.table_row_header);
        TblHead.addView(tr);
    }

    private void loadEmployeeProductList(List<SOMonthPerformance> lstEmployeeProduct) {
        List<SOMonthPerformance> lstProduct=new ArrayList<>();
        lstProduct.clear();
        lstProduct.addAll(lstEmployeeProduct);
        adapter = new SoMonthPerformanceadapter(SoMonthPerformanceReportActivity.this, lstProduct);
        if(adapter.getItemCount()== 0){
//            Toast.makeText(SoMonthPerformanceReportActivity.this,"Data not found...!!",Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        mSOProductData.setAdapter(adapter);
        addTotal(lstProduct);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vMonthYear date = new vMonthYear();
//        Date d= DateFunc.getDate();
        String fromDate = null;
        String toDate = null;
        List<SOMonthPerformance> lst=new ArrayList<>();

        int i = parent.getId();
        if (i == R.id.so_month_report_spn_month_year) {
            date = (vMonthYear) mSpn.getSelectedItem();

            fromDate = date.getFromDate();
            toDate = date.getToDate();

            if (date != null) {
                fetchSOMOnthPerformance(AppControl.getmEmployeeId(), fromDate, toDate);
            }


        } else if (i == R.id.so_month_report_spn_so) {
            vSO so = (vSO) mSpnSO.getSelectedItem();

            if (date != null || so != null) {
                if (so.getSoId() == 0) {
                    lst.removeAll(lstSoPerformance);
                } else {
                    for (SOMonthPerformance s : lstSoPerformance) {
                        if (so.getSoId() == s.getSoId()) {
                            lst.add(s);
                        }
                    }
                }
            }
            loadEmployeeProductList(lst);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class SoMonthPerformanceadapter extends RecyclerView.Adapter<SoMonthPerformanceadapter.ViewHolder> {

        Context context;
        List<SOMonthPerformance> lstProduct;

        public SoMonthPerformanceadapter(Context context, List<SOMonthPerformance> lstProduct) {
            this.context = context;
            this.lstProduct = lstProduct;
        }


        @NonNull
        @Override
        public SoMonthPerformanceadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.item_so_month_performance,parent,false);
            return new SoMonthPerformanceadapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SoMonthPerformanceadapter.ViewHolder holder, int position) {
            SOMonthPerformance product = lstProduct.get(position);
            loadDay(holder, product);

        }

        private void loadDay(ViewHolder holder, SOMonthPerformance dayReport) {
            TableRow tr;
            TextView tv;
            int headingColor = Color.GRAY;
            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);


            tr = new TableRow(context);
            tr.setPadding(5, 2, 5, 2);


            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(DateFunc.getDateStrSimple(dayReport.orderDate));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DAY));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText(Parse.toStr((int)dayReport.getTargetAmount()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText(Parse.toStr((int)dayReport.getAchievement()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            double totAch=(int)Math.round(dayReport.getTotalAmount());
            double tgt = dayReport.getTargetAmount();
            double ach = dayReport.getAchievement();

            if (tgt == 0 && ach > 0) {
                totAch = 100;
            } else {
                if (tgt > 0) {
                    totAch = ((ach * 100 / tgt));
                } else {
                    totAch = 0;
                }
            }


            tv.setText(Parse.toStr(Math.round(totAch))+ "%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            int TC=(int)Math.round(dayReport.getTC());
            tv.setText(Parse.toStr(TC));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            int PC=(int)Math.round(dayReport.getPC());
            tv.setText(Parse.toStr(PC));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            int TotTC=(int)Math.round(dayReport.getTotalTC());
            tv.setText(Parse.toStr(TotTC)+ "%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            holder.ll_product.removeAllViews();
            holder.ll_product.addView(tr);
        }

        @Override
        public int getItemCount() {
            return lstProduct.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TableLayout ll_product;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_product = itemView.findViewById(R.id.item_so_month_performance);
            }
        }
    }

    private void addTotal(List<SOMonthPerformance> lstProduct) {
        totAch=0;
        totPC=0;
        totTC=0;
        totValue=0;
        totTgt=0;
        totalTC=0;

        TableRow tr;
        TextView tv;
        Context mContext = SoMonthPerformanceReportActivity.this;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);


        if(lstProduct.size() > 0){
            for(SOMonthPerformance report : lstProduct){
                totTgt+=report.getTargetAmount();
                totAch+=report.getAchievement();
                totTC+=report.getTC();
                totPC+=report.getPC();
            }
            totValue=((totAch*100)/totTgt);
            totalTC=((totPC*100)/(totTC));
        }else{
            totAch=0;
            totPC=0;
            totTC=0;
            totValue=0;
            totTgt=0;
            totalTC=0;
        }
        if (Double.isInfinite(totValue))
        {
            totValue = 100;
        }
        if (totAch > totTgt) {
            totValue = 100;
        }

        if(Double.isInfinite(totalTC))
        {
            totalTC =0;
        }
        if (totPC > totTC) {
            totalTC = 100;
        }

        if(mTblTotal.getChildCount()==0)
        {
            tr=new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Total :");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DAY));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(headingColor);
            tv.setTypeface(tf);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int)totTgt));;
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int ach=(int)Math.round(totAch);
            tv.setText(Parse.toStr(ach));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int val=(int)Math.round(totValue);
            tv.setText(Parse.toStr(((val)+" %")));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tc=(int)Math.round(totTC);
            tv.setText(Parse.toStr(tc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int pc=(int)Math.round(totPC);
            tv.setText(Parse.toStr(pc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int tot=(int)Math.round(totalTC);
            tv.setText(Parse.toStr(tot)+" %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            mTblTotal.setBackgroundResource(R.drawable.table_row_header);
        }else{
            mTblTotal.removeAllViews();
            tr=new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Total :");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DAY));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(headingColor);
            tv.setTypeface(tf);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int)totTgt));;
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int ach=(int)Math.round(totAch);
            tv.setText(Parse.toStr(ach));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int val=(int)Math.round(totValue);
            tv.setText(Parse.toStr(((val)+" %")));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tc=(int)Math.round(totTC);
            tv.setText(Parse.toStr(tc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int pc=(int)Math.round(totPC);
            tv.setText(Parse.toStr(pc));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0,RIGHT_PADDING, 0);
            int tot=(int)Math.round(totalTC);
            tv.setText(Parse.toStr(tot)+" %");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            mTblTotal.addView(tv);

            mTblTotal.setBackgroundResource(R.drawable.table_row_header);
        }

    }

}
