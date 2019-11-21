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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.ProductWiseReport;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthProductWiseAchievementActivity extends BaseTransactionActivity {


    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float FONT_SIZE = 16;
    private static final float WGT_PRODUCT_NAME = 2;
    private static final float WGT_DATE_COL = 1;
//    private static final float WGT_COL=1.2f;

    List<ProductWiseReport> lstProductReport, fetchSortedList;
    MonthProductWiseDataAdapter dataAdapter;

    private TableLayout mTblHead;

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
        setContentView(R.layout.activity_month_product_report);
        title("Product Wise Achievement");

       /* ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product Wise Report");
            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/

        try {
            initControls();
            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private void initControls() {
        lstProductReport = new ArrayList<>();
        fetchSortedList = new ArrayList<>();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cnvrtFormatter = new SimpleDateFormat("yyyy-MM-dd");
//
        viewReport = findViewById(R.id.month_prod_btn_report);
        mFromDate = findViewById(R.id.month_prod_tv_from_date);
        mToDate = findViewById(R.id.month_prod_tv_to_date);
        mTblHead = findViewById(R.id.month_prod_tbl_header);
        mProductData = findViewById(R.id.month_prod_rcv_lst);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MonthProductWiseAchievementActivity.this);
        mProductData.setLayoutManager(layoutManager);
    }

    private void initListener() {
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSortedList = new ArrayList<>();
                fDate = DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy");
                tDate = DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy");

//                loadProductList(fDate,tDate,lstProductReport);
                fetchProductList(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(fDate), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(tDate), cnvrtFormatter));
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
                    Toast.makeText(MonthProductWiseAchievementActivity.this, "Please select before To Date", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MonthProductWiseAchievementActivity.this, "Please select after From Date", Toast.LENGTH_SHORT).show();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void initData() {
        mFromDate.setText(DateFunc.getDateStrSimple(getFirstDateOfCurrentMonth()));
        mToDate.setText(DateFunc.getDateStrSimple(CurrentDate()));
        setDateField();
        loadGridHeader();
        fetchProductList(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(getFirstDateOfCurrentMonth()), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(CurrentDate()), cnvrtFormatter));
//
    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private Date CurrentDate() {
        return DateFunc.getDate();
    }

    private void fetchProductList(int soId, final Date fromDate, final Date toDate) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ProductWiseReport>> query = apiService.getMonthProductWiseAchievementReport(soId, DateFunc.getDateStr(fromDate, "yyyy-MM-dd"), DateFunc.getDateStr(toDate, "yyyy-MM-dd"));

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<List<ProductWiseReport>>() {
                @Override
                public void onResponse(Call<List<ProductWiseReport>> call, Response<List<ProductWiseReport>> response) {
                    if (response.isSuccessful()) {
                        List<ProductWiseReport> reports = response.body();
                        lstProductReport.clear();
                        lstProductReport.addAll(reports);
                        loadProductList(fromDate, toDate, lstProductReport);
//                        loadProductList(lstProductReport);
                        UtilityFunc.dismissDialog();
                    } else {
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
                public void onFailure(Call<List<ProductWiseReport>> call, Throwable t) {
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
            tv.setText("Product Name");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Tgt\nQty");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Ach\nQty");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
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

    private void loadProductList(Date fDate, Date tDate, List<ProductWiseReport> lstProductReport) {
        dataAdapter = new MonthProductWiseDataAdapter(MonthProductWiseAchievementActivity.this, lstProductReport);
        dataAdapter.notifyDataSetChanged();
        mProductData.setAdapter(dataAdapter);
    }

    private class MonthProductWiseDataAdapter extends RecyclerView.Adapter<MonthProductWiseDataAdapter.ViewHolder> {
        Context mContext;
        List<ProductWiseReport> lstProductReport;

        public MonthProductWiseDataAdapter(Context mContext, List<ProductWiseReport> lstProductReport) {
            this.mContext = mContext;
            this.lstProductReport = lstProductReport;
        }

        @NonNull
        @Override
        public MonthProductWiseDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_order, parent, false);
            return new MonthProductWiseDataAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthProductWiseDataAdapter.ViewHolder holder, int position) {
            ProductWiseReport report = lstProductReport.get(position);
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

        private void loadProductItem(@NonNull MonthProductWiseDataAdapter.ViewHolder holder, ProductWiseReport report) {

            TableRow tr;
            TextView tv;


            tr = new TableRow(mContext);
            tr.setPadding(5, 3, 5, 3);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(report.getProductName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int achv = (int) Math.round(report.getTargetQty());
            tv.setText(Parse.toStr(achv));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);


            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int qty = (int) Math.round(report.getAchQty());
            tv.setText(Parse.toStr(qty));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(mContext);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);

            int tgtVal = (int) report.getTargetQty();
            int totAch = (int) report.getAchQty();

            if (report.getTargetQty() == 0 && report.getAchQty() > 0) {
                report.setTotalAmount(100);
            } else {
                if (tgtVal > 0) {
                    report.setTotalAmount((totAch * 100 / tgtVal));
                } else {
                    report.setTotalAmount(0);
                }

            }

            tv.setText(String.valueOf((int) report.getTotalAmount()) + "%");

            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
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
            return lstProductReport.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TableLayout ll_product;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_product = itemView.findViewById(R.id.item_ll_prod);
            }
        }
    }
}
