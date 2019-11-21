package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DayWiseAchievementActivity extends BaseTransactionActivity {

    private final static int LEFT_PADDING = 5;
    private final static int RIGHT_PADDING = 10;
    private static final float FONT_SIZE = 18;
    private static final float WGT_DAY = 1.6f;
    private static final float WGT_ACHIVEMENT = 1.2f;
    private static final float WGT_TC = 0.6f;

    TableLayout mTblHead;
    RecyclerView mDayAchvmnt;
    Button mViewReport;
    TextView mFromDate;
    TextView mToDate;


    private SimpleDateFormat dateFormatter;
    Date fDate,tDate;
    private SimpleDateFormat cnvrtFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    List<DayWiseAchievementReport> lstDayAchivmnt;
    DayAchievmentAdapter achievmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_achievements);
        title("Day Wise Achievement ");

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

    private void initControls() {
        lstDayAchivmnt=new ArrayList<>();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        cnvrtFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mFromDate=findViewById(R.id.day_tv_from_date);
        mToDate=findViewById(R.id.day_tv_to_date);
        mTblHead=findViewById(R.id.day_tbl_header);
        mDayAchvmnt=findViewById(R.id.day_rcv_lst);
        mDayAchvmnt.setLayoutManager(new LinearLayoutManager(DayWiseAchievementActivity.this));
        mViewReport=findViewById(R.id.day_btn_report);
    }

    private void initData() {
        mFromDate.setText(DateFunc.getDateStrSimple(getFirstDateOfCurrentMonth()));
        mToDate.setText(DateFunc.getDateStrSimple(CurrentDate()));
        setDateField();
        loadGridHeader();
        fetchDayWIseList(AppControl.getmEmployeeId(),DateFunc.getDate(DateFunc.getDateStr(getFirstDateOfCurrentMonth()),cnvrtFormatter),DateFunc.getDate(DateFunc.getDateStr(CurrentDate()),cnvrtFormatter));
//        loadDayAchivements(DateFunc.getDate(mFromDate.getText().toString()),DateFunc.getDate(mToDate.getText().toString()),lstDayAchivmnt);
    }


    private void fetchDayWIseList(int soId, final Date fromDate, final Date toDate) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService=ApiClient.getClient().create(ApiService.class);
        Call<List<DayWiseAchievementReport>> query = apiService.getDayWiseAchievementReport(soId,DateFunc.getDateStr(fromDate, "yyyy-MM-dd"), DateFunc.getDateStr(toDate, "yyyy-MM-dd"));

        if (query == null) {
            Log.e("","");
        }else{
            query.enqueue(new Callback<List<DayWiseAchievementReport>>() {
                @Override
                public void onResponse(Call<List<DayWiseAchievementReport>> call, Response<List<DayWiseAchievementReport>> response) {
                    if(response.isSuccessful()){
                        lstDayAchivmnt.clear();
                        List<DayWiseAchievementReport> reports=response.body();
                        lstDayAchivmnt.addAll(reports);
                        loadDayAchivements(fromDate,toDate,lstDayAchivmnt);
                        UtilityFunc.dismissDialog();
                    }else{
                        String message;
                        try {
                            message = response.errorBody().string();
                        } catch (Exception e) {
                            message = e.getMessage();
                            errMsg("While downloading", e);
//                        togger.e(e);
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

                    if(t instanceof IOException){
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

    private void setDateField() {
        Calendar newCalendar=Calendar.getInstance();
        fromDatePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                fDate=DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()),"dd-MM-yyyy");
                if(fDate.before(DateFunc.getDate((mToDate.getText().toString()),"dd-MM-yyyy")) || fDate.equals(DateFunc.getDate((mToDate.getText().toString()),"dd-MM-yyyy"))) {
//                    if(mFromDate.getError().equals(true)){
                        mFromDate.setError(null);
                        mFromDate.clearFocus();
//                    }
                    mFromDate.setText(DateFunc.getDateStrSimple(fDate));
                }else{
                    mFromDate.setError("Please select before To Date");
                    Toast.makeText(DayWiseAchievementActivity.this,"Please select before To Date",Toast.LENGTH_SHORT).show();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                tDate=DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()),"dd-MM-yyyy");
                if(tDate.after(DateFunc.getDate((mFromDate.getText().toString()),"dd-MM-yyyy")) || tDate.equals(DateFunc.getDate((mFromDate.getText().toString()),"dd-MM-yyyy"))) {
//                    /if(mToDate.getError().equals(true)){
                        mToDate.setError(null);
                        mToDate.clearFocus();
//                    }
                    mToDate.setText(DateFunc.getDateStrSimple(tDate));
                }else{
                    mToDate.setError("Please select after From Date");
                    Toast.makeText(DayWiseAchievementActivity.this,"Please select after From Date",Toast.LENGTH_SHORT).show();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private Date CurrentDate(){

        return DateFunc.getDate();
    }

    private void initListener() {

//        loadDayAchivements(AppControl.getTodayDate(), AppControl.getTodayDate(), lstDayAchivmnt);
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
        mViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if(mFromDate.getError().equals(true) || mToDate.getError().equals(true)){
//                        if(mFromDate!=null || mToDate!=null){
//                        mFromDate.setError(null);
//                        mFromDate.clearFocus();
//                        mToDate.setError(null);
//                        mToDate.clearFocus();
//                    }
//                }
//                loadDayAchivements(DateFunc.getDate(mFromDate.getText().toString()),DateFunc.getDate(mFromDate.getText().toString()),lstDayAchivmnt);
                fDate=DateFunc.getDate((mFromDate.getText().toString()),"dd-MM-yyyy");
                tDate=DateFunc.getDate((mToDate.getText().toString()),"dd-MM-yyyy");

//                loadDayAchivements(fDate,tDate,lstDayAchivmnt);
                fetchDayWIseList(AppControl.getmEmployeeId(),DateFunc.getDate(DateFunc.getDateStr(fDate),cnvrtFormatter),DateFunc.getDate(DateFunc.getDateStr(tDate),cnvrtFormatter));

            }
        });

    }

    private void loadDayAchivements(Date fDate, Date tDate, List<DayWiseAchievementReport> lstDayAchivmnt) {
        List<DayWiseAchievementReport> lst2=new ArrayList<>();
        for (DayWiseAchievementReport report : lstDayAchivmnt){
            if (report.getOrderDate().after(fDate) || report.getOrderDate().equals(fDate)) {
                if (report.getOrderDate().before(tDate) || report.getOrderDate().equals(tDate)) {
                    if(!lst2.contains(report)) {
                        lst2.add(report);
                    }else{
                        lst2.remove(report);
                    }
//                    lst2.add(report);
                    if ((report.getOrderDate().equals(fDate) && report.getOrderDate().equals(tDate))) {
                        if(!lst2.contains(report)) {
                            lst2.add(report);
                        }
                    }
                }else {
                    lst2.remove(report);
                }
            }else {
                lst2.remove(report);
            }
        }
        Collections.sort(lst2, new Comparator<DayWiseAchievementReport>() {
            @Override
            public int compare(DayWiseAchievementReport o1, DayWiseAchievementReport o2) {
                return (o1.getOrderDate().compareTo(o2.getOrderDate()));
            }
        });
        achievmentAdapter=new DayAchievmentAdapter(DayWiseAchievementActivity.this,fDate,tDate, lst2);
        achievmentAdapter.notifyDataSetChanged();
        if(achievmentAdapter.getItemCount()==0){
            Toast.makeText(DayWiseAchievementActivity.this,"Reocord Not Found",Toast.LENGTH_SHORT).show();
        }
        mDayAchvmnt.setAdapter(achievmentAdapter);
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
        tv.setText("Day");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DAY));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, LEFT_PADDING, 0);
        tv.setText("Achivement");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACHIVEMENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("TC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("PC");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TC));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        mTblHead.addView(tr);
    }

    private class DayAchievmentAdapter extends RecyclerView.Adapter<DayAchievmentAdapter.ViewHolder>{

        Context mContext;
        Date fromDate;
        Date toDate;
        List<DayWiseAchievementReport> lstDayAchivmnt;

        public DayAchievmentAdapter(Context mContext, Date fromDate, Date toDate, List<DayWiseAchievementReport> lstDayAchivmnt) {
            this.mContext=mContext;
            this.fromDate=fromDate;
            this.toDate=toDate;
            this.lstDayAchivmnt=lstDayAchivmnt;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.item_day_achievement,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DayWiseAchievementReport dayReport=lstDayAchivmnt.get(position);
//            Date date=dayReport.getOrderDate();
            loadDay(holder.ll_day,dayReport,true);

//                if(date.after(fromDate) || date.equals(fromDate))
//                  {
//                   if(date.before(toDate) || date.equals(toDate))
//                     {
//                          try{
//                          loadDay(holder.ll_day,dayReport,true);
//                              if ((date.equals(fromDate) && date.equals(toDate))) {
//                                  try {
//                                      loadDay(holder.ll_day,dayReport,true);
//                                  } catch (Exception ex) {
//                                      ex.getMessage();
//                                  }
//                              }
//                          }catch(Exception ex){
//                              ex.getMessage();
//                          }
//                     }else {
//                          try{
//                              loadDay(holder.ll_day, dayReport, false);
//                          }catch(Exception ex){
//                              ex.getMessage();
//                          }
//
//                      }
//                  }else {
//                       try{
//                        loadDay(holder.ll_day, dayReport, false);
//                       }catch (Exception ex) {
//                           ex.getMessage();
//                       }
//                  }


        }

        private void loadDay(LinearLayout holder, DayWiseAchievementReport report, boolean b) {
            // add day achivement list
            TextView tv;

            LinearLayout.LayoutParams ll_product=new LinearLayout.LayoutParams(0, -1, WGT_DAY);
            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(DateFunc.getDateStrSimple(report.getOrderDate()));
            tv.setLayoutParams(ll_product);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.CENTER);
            if(b) {
                holder.addView(tv);
            }else{
                holder.removeView(tv);
            }

            LinearLayout.LayoutParams ll_QTY=new LinearLayout.LayoutParams(0, -1, WGT_ACHIVEMENT);
            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int acv=(int)Math.round(report.getAchievement());
            tv.setText(String.valueOf(acv));
            tv.setLayoutParams(ll_QTY);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.RIGHT);
            if(b) {
                holder.addView(tv);
            }else{
                holder.removeView(tv);
            }

            LinearLayout.LayoutParams ll_TC=new LinearLayout.LayoutParams(0, -1, WGT_TC);
            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(report.getTC()));
            tv.setLayoutParams(ll_TC);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.RIGHT);
            if(b) {
                holder.addView(tv);
            }else{
                holder.removeView(tv);
            }

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(report.getPC()));
            tv.setLayoutParams(ll_TC);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.RIGHT);
            if(b) {
                holder.addView(tv);
            }else{
                holder.removeView(tv);
            }

        }

        @Override
        public int getItemCount() {
            return lstDayAchivmnt.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout ll_day;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_day=itemView.findViewById(R.id.item_ll_day);
            }
        }
    }
}
