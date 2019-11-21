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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.SOMonthAttendance;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vMonthYear;
import com.salescube.healthcare.demo.view.vSO;
import com.salescube.healthcare.demo.view.vSOAttendenceTotal;
import com.salescube.healthcare.demo.view.vSOMonthAttendenceReport;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoMonthAttendenceReportActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {

    private TableLayout TblSpn;

    private Spinner mSpn;
    private Spinner mSpnSO;

    private TextView totLeaves;
    private TextView totOtherWork;
    private TextView totPresent;

    private TableLayout TblHead;
    private TableLayout Tbltotal;
    private RecyclerView mAttendenceData;

    private List<SOMonthAttendance> lstSoList;
    public int presentDays ;
    public int otherWorkDays ;
    public int leavesDays ;

    SoMonthAdapter adapter;

    private final static int LEFT_PADDING = 5;
    private final static int RIGHT_PADDING = 10;
    private static final float FONT_SIZE = 16;
    private static final float WGT_DATE = 1f;
    private static final float WGT_STATUS = 1f;
    private static final float WGT_START_TIME = 1f;
    private static final float WGT_TOTAL_NAME = 2f;
    private static final float WGT_TOTAL = 1f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_attendence_report);
        title("Attendence Report");
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Attendance Report");
//            actionBar.setIcon(R.drawable.icon_company);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }

        try
        {
            initControls();
            initData();
            initListener();
        }catch(Exception e){
            Logger.e(e.getMessage());
        }


    }

    private void initListener() {
        mSpn.setOnItemSelectedListener(this);
        mSpnSO.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initControls() {
        totLeaves=findViewById(R.id.so_month_attendence_report_tv_leaves);
        totOtherWork=findViewById(R.id.so_month_attendence_report_tv_other_work);
        totPresent=findViewById(R.id.so_month_attendence_report_tv_present);

        lstSoList=new ArrayList<SOMonthAttendance>();

        TblSpn=findViewById(R.id.so_month_attendence_report_tbl_spinner);
        TblSpn.setVisibility(View.VISIBLE);

        mSpn=(Spinner)findViewById(R.id.so_month_attendence_spn_month_year) ;
        mSpnSO=(Spinner)findViewById(R.id.so_month_attendence_spn_so) ;

        Tbltotal=findViewById(R.id.so_month_attendence_report_tbl_total);
        TblHead=findViewById(R.id.so_month_attendence_report_tbl_head);


        mAttendenceData=findViewById(R.id.so_month_attendence_report_rcv_lst);
        mAttendenceData.setLayoutManager(new LinearLayoutManager(SoMonthAttendenceReportActivity.this));
    }

    private void initData() {
        SpinnerHelper.FillMonthYear(mSpn,0,3);
        loadGridHeader();

        Date today = DateFunc.getDate();

        String fromDate = DateFunc.FO_MonthStr(today);
        String toDate = DateFunc.EO_MonthStr(today);

        String appRole = AppControl.getAppRole();

        fetchSOProductList(AppControl.getmEmployeeId(),fromDate,toDate);
    }

    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Date");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("Status");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_STATUS));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Start Time");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_START_TIME));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        TblHead.addView(tr);
    }


    private void fetchSOProductList(int soId, String fromDate ,String toDate) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<vSOMonthAttendenceReport> query = apiService.getSOMonthAttendenceReport(soId,fromDate,toDate);

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<vSOMonthAttendenceReport>() {
                @Override
                public void onResponse(Call<vSOMonthAttendenceReport> call, Response<vSOMonthAttendenceReport> response) {
                    if (response.isSuccessful()) {

                        List<SOMonthAttendance> reports = response.body().getData();
                        List<vSO> soList = response.body().getSoList();

                        lstSoList.clear();
                        lstSoList.addAll(reports);

                        //refresh list on so spinner item selection : fixed for double reload the list
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
                public void onFailure(Call<vSOMonthAttendenceReport> call, Throwable t) {
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

    private List<SOMonthAttendance> insertDumm1List() {
        List<SOMonthAttendance> lst = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i==0 || i % 2 == 0) {
                lst.add(i, new SOMonthAttendance( i,"2019-03-0"+(i+1),"" + i," 9:30 AM "));
            } else {
                lst.add(i, new SOMonthAttendance( i,"2019-03-0"+(i),"" + i," 11:30 AM "));
            }
        }
        return lst;
    }

    private static void loadSpinner(List<vSO> lstProduct, Spinner mSpnSO) {

        lstProduct.add(0,new vSO(0, "-- SELECT --"));
        ArrayAdapter adp = new ArrayAdapter<>(mSpnSO.getContext(), android.R.layout.simple_spinner_item, lstProduct);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSO.setAdapter(adp);
    }


    private void showAttendenceList(List<SOMonthAttendance> lstSoList) {
        List<SOMonthAttendance> lstAttend = new ArrayList<>();
        lstAttend.clear();
        lstAttend.addAll(lstSoList);
        adapter = new SoMonthAdapter(SoMonthAttendenceReportActivity.this, lstAttend);
        if(adapter.getItemCount()== 0){
//            Toast.makeText(SoMonthAttendenceReportActivity.this,"Data not found...!!",Toast.LENGTH_LONG).show();
            addTotal(0,0,0);
//        }else{
//            addTotal(presentDays,leavesDays,otherWorkDays);
        }
        adapter.notifyDataSetChanged();
        mAttendenceData.setAdapter(adapter);
        if(lstSoList != null && lstSoList.size() > 0) {
            for(int i = 0; i< lstSoList.size(); i++) {
                for (vSOAttendenceTotal days : lstSoList.get(i).getSoTotalList()) {
                    addTotal(days.presentDays, days.leavesDays, days.otherWorkDays);
                }
            }
        }

    }

    private void addTotal(int presentDays, int leavesDays, int otherWorkDays) {
        if(presentDays < 10)
        {
            totPresent.setText(":\t0" + Parse.toStr(presentDays));
        }else{
            totPresent.setText(":\t" + Parse.toStr(presentDays));
        }
        if(leavesDays < 10)
        {
            totLeaves.setText(":\t0" + Parse.toStr(leavesDays));
        }else{
            totLeaves.setText(":\t" + Parse.toStr(leavesDays));
        }
        if(otherWorkDays < 10)
        {
            totOtherWork.setText(":\t0" + Parse.toStr(otherWorkDays));
        }else{
            totOtherWork.setText(":\t" + Parse.toStr(otherWorkDays));
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vMonthYear date = new vMonthYear();

        int i = parent.getId();
        if (i == R.id.so_month_attendence_spn_month_year) {
            vMonthYear dt = (vMonthYear) mSpn.getSelectedItem();
            if (dt != null) {
                fetchSOProductList(AppControl.getmEmployeeId(), dt.getFromDate(), dt.getToDate());
            }

        } else if (i == R.id.so_month_attendence_spn_so) {
            vSO so = (vSO) mSpnSO.getSelectedItem();

            List<SOMonthAttendance> lst = new ArrayList<>();

            if (so != null) {
                if (so.getSoId() == 0) {
                    lst.removeAll(lstSoList);
//                        addTotal(0,0,0);
                } else {
                    for (SOMonthAttendance s : lstSoList) {

                        if (so.getSoId() == s.getSoId()) {
                            lst.add(s);
                        }
                    }
                }
            }

//                        fetchSOProductList(AppControl.getmEmployeeId(),date.getFromDate(), date.getToDate());
            showAttendenceList(lst);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private class SoMonthAdapter extends RecyclerView.Adapter<SoMonthAdapter.ViewHolder>{

        Context context;
        List<SOMonthAttendance> lstSoList;

        SoMonthAdapter(Context context, List<SOMonthAttendance> lstSoList) {
            this.context=context;
            this.lstSoList=lstSoList;
        }

        @NonNull
        @Override
        public SoMonthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.item_attendance_report,parent,false);
            return new SoMonthAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SoMonthAdapter.ViewHolder holder, int position) {
            SOMonthAttendance report =lstSoList.get(position);
            loadAttendance(holder,report);
        }

        private void loadAttendance(SoMonthAdapter.ViewHolder holder, final SOMonthAttendance report) {
            TableRow tr;
            TextView tv;

            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
            int headingColor = Color.BLACK;

            tr = new TableRow(context);
            tr.setPadding(5, 5, 5, 5);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(report.getDate());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setTypeface(tf);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(report.getAction());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_STATUS));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setTypeface(tf);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tv.setTextColor(headingColor);
            tr.addView(tv);
//            holder.ll_attendence.addView(tv);


            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(report.getTime());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_START_TIME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setTypeface(tf);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tv.setTextColor(headingColor);
            tr.addView(tv);
//            holder.ll_attendence.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_total_border);
            tr.setBackgroundResource(R.drawable.table_row_total_border);
            holder.tbl_attendence.removeAllViews();
            holder.tbl_attendence.addView(tr);

        }

        @Override
        public int getItemCount() {
            return lstSoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TableLayout tbl_attendence;

            public ViewHolder(View itemView) {
                super(itemView);
                tbl_attendence=itemView.findViewById(R.id.item_attendence);
            }
        }
    }
}
