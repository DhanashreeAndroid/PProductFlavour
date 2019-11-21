package com.salescube.healthcare.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.AttendanceReport;
import com.salescube.healthcare.demo.data.model.SoAttendance;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceReportActivity extends BaseTransactionActivity {

    TableLayout TblHead;
    RecyclerView mAttendenceData;

    List<SoAttendance> lstSoList;
    AttendenceAdapter adapter;

    private final static int LEFT_PADDING = 5;
    private final static int RIGHT_PADDING = 10;
    private static final float FONT_SIZE = 16;
    private static final float WGT_SO_NAME = 1.6f;
    private static final float WGT_STATUS = 0.6f;
    private static final float WGT_START_TIME = 1f;
    private static final float WGT_VIEW = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_report);
        title("SO Attendance Report");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("SO Attendance Report");
//            actionBar.setIcon(R.drawable.icon_company);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }

        try {
            initControls();
            initData();
//            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initControls() {

        lstSoList = new ArrayList<>();

        TblHead = findViewById(R.id.attendence_tbl_head);
        mAttendenceData = findViewById(R.id.attendence_rcv_lst);
        mAttendenceData.setLayoutManager(new LinearLayoutManager(AttendenceReportActivity.this));
    }

    private void initData() {
        loadGridHeader();
//        lstSoList=insertDummyList();
        loadAttendence();

    }

    private List<AttendanceReport> insertDummyList() {
        List<AttendanceReport> lst = new ArrayList<>();
        int n = 20;
        for (int i = 1; i <= n; i++) {
            if (i % 4 == 0) {
                if (i % 2 == 0) {
                    lst.add(i - 1, new AttendanceReport(i, "SO " + i, "Other", "11:30 AM", "", ""));
                } else {
                    lst.add(i - 1, new AttendanceReport(i, "SO " + i, "Leave", "9:30 AM", "", ""));
                }
            } else {
                if (i % 3 == 0) {
                    lst.add(i - 1, new AttendanceReport(i, "SO " + i, "Working", "10:30 AM", "", ""));
                } else {
                    lst.add(i - 1, new AttendanceReport(i, "SO " + i, "Not Received", "12:30 PM", "", ""));
                }
            }/*else if(i % 4==0){
                lst.add(i,new AttendanceReport(i,"SO "+i,"Other","11:30 AM","",""));
            }else if(i % 5==0){
                lst.add(i,new AttendanceReport(i,"SO "+i,"Not Received","12:30 PM","",""));
            }*/
        }
        return lst;
    }

    public void loadAttendence() {
        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<SoAttendance>> query = apiService.getAttendence(AppControl.getmEmployeeId());

        query.enqueue(new Callback<List<SoAttendance>>() {
            @Override
            public void onResponse(Call<List<SoAttendance>> call, Response<List<SoAttendance>> response) {
                if(response.isSuccessful()){
                    try {
                        List<SoAttendance> objUser = response.body();
                        lstSoList.addAll(objUser);
                        UtilityFunc.dismissDialog();
                        showAttendence(lstSoList);
                    } catch (Exception e) {
                        UtilityFunc.dismissDialog();
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }
                }else {
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
            public void onFailure(Call<List<SoAttendance>> call, Throwable t) {
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

    @Override
    protected void onDestroy () {
        UtilityFunc.dismissDialog();
        super.onDestroy();
    }


        private void showAttendence (List < SoAttendance > lstSoList) {
            adapter = new AttendenceAdapter(AttendenceReportActivity.this, lstSoList);
            adapter.notifyDataSetChanged();
            mAttendenceData.setAdapter(adapter);
        }

        private void loadGridHeader () {
            TableRow tr;
            TextView tv;
            int headingColor = Color.BLACK;
            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Name");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
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
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Start\nTime");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_START_TIME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("View");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VIEW));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            TblHead.addView(tr);
        }

        private class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.ViewHolder> {

            Context context;
            List<SoAttendance> lstSoList;

            AttendenceAdapter(Context context, List<SoAttendance> lstSoList) {
                this.context = context;
                this.lstSoList = lstSoList;
            }

            @NonNull
            @Override
            public AttendenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_attendance_report, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull AttendenceAdapter.ViewHolder holder, int position) {
                SoAttendance report = lstSoList.get(position);
                loadAttendance(holder, report);
          /*  List<SoAttendance> detailList = report.getDetail();
            for (SoAttendance list : detailList){
                loadAttendance(holder,list);
            }*/


            }

            private void loadAttendance(ViewHolder holder, final SoAttendance report) {
                TableRow tr;
                TextView tv;

                String role = report.getSoRole();


                Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
                int headingColor = Color.BLACK;

                tr = new TableRow(context);
                tr.setPadding(5, 5, 5, 5);

                tv = new TextView(context);
                tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
                tv.setText(report.getSoName());
                tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
                tv.setTextSize(FONT_SIZE);
                if (role.equalsIgnoreCase("ASM")) {
                    tv.setBackgroundResource(R.drawable.table_cell_asm);
                } else {
                    tv.setBackgroundResource(R.drawable.table_cell_bg);
                }

                tv.setTypeface(tf);
                tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                tv.setTextColor(headingColor);
                tr.addView(tv);

                tv = new TextView(context);
                tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
                tv.setText(report.getStatus());
                tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_STATUS));
                tv.setTextSize(FONT_SIZE);
                if (role.equalsIgnoreCase("ASM")) {
                    tv.setBackgroundResource(R.drawable.table_cell_asm);
                } else {
                    tv.setBackgroundResource(R.drawable.table_cell_bg);
                }
                tv.setTypeface(tf);
                tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                tv.setTextColor(headingColor);
                tr.addView(tv);
//            holder.ll_attendence.addView(tv);


                tv = new TextView(context);
                tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);

                String test = report.getStartTime();
                String test2 = "";
                if (!test.equals("")) {
                    test2 = test.substring(test.indexOf(" "));
                }

                tv.setText(test2);
                tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_START_TIME));
                tv.setTextSize(FONT_SIZE);
                if (role.equalsIgnoreCase("ASM")) {
                    tv.setBackgroundResource(R.drawable.table_cell_asm);
                } else {
                    tv.setBackgroundResource(R.drawable.table_cell_bg);
                }
                tv.setTypeface(tf);
                tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                tv.setTextColor(headingColor);
                tr.addView(tv);
//            holder.ll_attendence.addView(tv);


                tv = new Button(context);
                tv.setPadding(LEFT_PADDING, 20, RIGHT_PADDING, 20);
                tv.setText("");
                tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VIEW));
                Drawable drawable = getResources().getDrawable(R.drawable.map_view);
                drawable.setBounds(0, (int) (drawable.getIntrinsicWidth() * 0.5), 0,
                        (int) (drawable.getIntrinsicHeight() * 0.5));
                ScaleDrawable sd = new ScaleDrawable(drawable, 0, -1, -1);
                tv.setBackground(sd.getDrawable());
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(tf);
                tv.setTextColor(headingColor);
                if (!report.getLat().equals("") && !report.getLng().equals("")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //                    q is location details z = zoom
                                String uri = "geo:0,0?q=" + (report.getLat()) + "," + (report.getLng()) + "&z=10";
                                Uri gmmIntentUri = Uri.parse(uri);
                                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            } catch (Exception ex) {
                                errMsg("While sales order!", ex);
                            }
                        }
                    });
                } else {

                    if (role.equalsIgnoreCase("ASM")) {
                        tv.setBackgroundResource(R.drawable.table_cell_plain_asm);
                    } else {
                        tv.setBackgroundResource(R.drawable.table_cell_plain);
                    }

                }
                tr.addView(tv);

                tr.setBackgroundResource(R.drawable.table_row_header);
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
                    tbl_attendence = itemView.findViewById(R.id.item_attendence);
                }
            }
        }

}
