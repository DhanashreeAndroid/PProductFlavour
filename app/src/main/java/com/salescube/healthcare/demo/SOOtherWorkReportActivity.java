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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.SOOtherWork;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SOOtherWorkReportActivity extends BaseActivity {

    private RecyclerView mOtherWorkData;
    private Spinner mSpnSO;

    private TableLayout mTblHead;
    private List<SOOtherWork> lstSoWork, lstSortSO;
    SOOtherWork employee;

    private SOOtherWorkAdapter adapter;

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float FONT_SIZE = 16;
    private static final float WGT_SO_NAME = 1.4f;
    private static final float WGT_WORK = 1.2f;
    private static final float WGT_REMARK = 0.8f;
    private static final float WGT_AGENT_NAME = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_so_other_work_report);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null) {
            titleBar.setText("SO Other Work Report");
        }

        try {
            initControls();
            fetchSOOtherWorkList(AppControl.getmEmployeeId());

//            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private void initListener() {

//        mSpnSO.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initControls() {
        lstSoWork = new ArrayList<>();
        lstSortSO = new ArrayList<>();
        mTblHead = findViewById(R.id.so_other_work_tbl_header);
//        mSpnSO= findViewById(R.id.so_other_work_spn);

        mOtherWorkData = findViewById(R.id.so_other_work_rcv_lst);
        mOtherWorkData.setLayoutManager(new LinearLayoutManager(SOOtherWorkReportActivity.this));


    }

    private void initData() {
        loadGridHeader();
//        loadSpinner(mSpnSO,lstSoWork);
    }

    private void fetchSOOtherWorkList(int soId) {
        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<SOOtherWork>> query = apiService.getSOOtherWorkReport(soId);

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<List<SOOtherWork>>() {
                @Override
                public void onResponse(Call<List<SOOtherWork>> call, Response<List<SOOtherWork>> response) {
                    if (response.isSuccessful()) {
                        List<SOOtherWork> reports = response.body();
                        lstSoWork.clear();
                        lstSoWork.addAll(reports);
                        loadSOList(lstSoWork);
                        initData();
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
                public void onFailure(Call<List<SOOtherWork>> call, Throwable t) {
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

    private void loadSpinner(Spinner spn, List<SOOtherWork> lstSoWork) {

        ArrayList<SOOtherWork> objList = new ArrayList<>();

        for (SOOtherWork so : lstSoWork) {
            SOOtherWork s = new SOOtherWork();
            s.setSoId(so.getSoId());
            s.setSoName(so.getSoName());
            if (!objList.contains(s)) {
                objList.add(s);
            } else {
                objList.remove(s);
            }
        }

        objList.add(0, new SOOtherWork(0, "All"));

        Collections.sort(objList, new Comparator<SOOtherWork>() {
            @Override
            public int compare(SOOtherWork o1, SOOtherWork o2) {
                return ((String.valueOf(o1.getSoId())).compareTo(String.valueOf(o2.getSoId())));
            }
        });

        ArrayAdapter<SOOtherWork> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp.notifyDataSetChanged();
        spn.setAdapter(adp);

    }


    private void loadSOList(List<SOOtherWork> lstSortSO) {
        adapter = new SOOtherWorkAdapter(SOOtherWorkReportActivity.this, lstSortSO);
        adapter.notifyDataSetChanged();
        mOtherWorkData.setAdapter(adapter);
    }

    private List<SOOtherWork> insertDummyList() {
        List<SOOtherWork> lst = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                lst.add(i, new SOOtherWork(i, "All", "Remark" + i, "Agent Name"));
            } else {
                lst.add(i, new SOOtherWork(i, "SO " + i, "Remark" + i, "Agent Name"));
            }
        }
        return lst;
    }

    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        if (mTblHead.getChildCount() == 0) {
            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);
//

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
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Work");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_WORK));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            tr.addView(tv);

           /* tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Remark");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_REMARK));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);*/

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Agent Name");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_AGENT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tv.setAllCaps(true);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblHead.addView(tr);
        }
    }

  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch(parent.getId()){
                case R.id.so_other_work_spn :
                    lstSortSO.clear();
                    employee = (SOOtherWork) mSpnSO.getSelectedItem();

                    if(employee != null)
                    {
                       \
                    }
                    loadSOList(lstSortSO);
                    break;
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
*/

    private class SOOtherWorkAdapter extends RecyclerView.Adapter<SOOtherWorkAdapter.ViewHolder> {

        Context context;
        List<SOOtherWork> lstSortSO;

        SOOtherWorkAdapter(Context context, List<SOOtherWork> lstSortSO) {
            this.context = context;
            this.lstSortSO = lstSortSO;
        }

        @NonNull
        @Override
        public SOOtherWorkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_so_other_work, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SOOtherWorkAdapter.ViewHolder holder, int position) {
            SOOtherWork work = lstSortSO.get(position);
            loadSoData(holder, work);
        }

        private void loadSoData(ViewHolder holder, SOOtherWork work) {
            TableRow tr;
            TextView tv;
            int headingColor = Color.BLACK;
            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

//            if(mTblHead.getChildCount()==0) {
            tr = new TableRow(context);
            tr.setPadding(5, 2, 5, 2);
//
            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(work.getSoName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText(work.getRemark());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_WORK));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

//                tv = new TextView(context);
//                tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
//                tv.setText(work.getSoRemark());
//                tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_REMARK));
//                tv.setTextSize(FONT_SIZE);
//                tv.setBackgroundResource(R.drawable.table_cell_bg);
//                tv.setGravity(Gravity.LEFT);
//                tv.setTypeface(tf);
//                tv.setTextColor(headingColor);
//                tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText(work.getAgentName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_AGENT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            holder.tblSoData.removeAllViews();
            tr.setBackgroundResource(R.drawable.table_row_header);
            holder.tblSoData.addView(tr);
//            }
        }

        @Override
        public int getItemCount() {
            return lstSortSO.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TableLayout tblSoData;

            public ViewHolder(View itemView) {
                super(itemView);

                tblSoData = itemView.findViewById(R.id.item_so_work);
            }
        }
    }
}