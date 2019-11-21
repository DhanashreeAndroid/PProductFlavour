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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.data.model.ProductWiseReport;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vTodaySummary;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayProductWiseAchievementActivity extends BaseTransactionActivity{

    TableLayout tblHead;

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float FONT_SIZE = 16;
    private static final float WGT_PRODUCT_NAME = 2;
    private static final float WGT_QTY = 1;
    private static final float WGT_VALUE = 0.7f;
    private static final float WGT_COL=0.3f;
    private static final float WGT_DATE_COL = 1;

    List<ProductWiseReport> lstProductReport;
    MonthProductWiseDataAdapter dataAdapter;
    private RecyclerView mProductData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wise_achievement);
        title("Product Wise Achievement");

        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product Wise Achievement");
            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/

        try {
            initControls();
            addHeader();
            fetchProductList(AppControl.getmEmployeeId());

        } catch (Exception e) {
            errMsg("While loading todays orders",e);
            finish();
        }

    }

    private void loadProductList(List<ProductWiseReport> lstProductReport) {
        dataAdapter = new MonthProductWiseDataAdapter(this, lstProductReport);
        dataAdapter.notifyDataSetChanged();
        mProductData.setAdapter(dataAdapter);
    }

    private void fetchProductList(int soId) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ProductWiseReport>> query = apiService.getTodayProductWiseAchievementReport(soId);

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
                        loadProductList(lstProductReport);
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





    private void initControls() {
        lstProductReport = new ArrayList<>();
        tblHead = (TableLayout) findViewById(R.id.product_wise_tbl_head);
        mProductData = findViewById(R.id.month_prod_rcv_lst);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TodayProductWiseAchievementActivity.this);
        mProductData.setLayoutManager(layoutManager);

    }

    private void addHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        if (tblHead.getChildCount() == 0) {
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
            tv.setText("Tgt\nQty\n(Pcs)");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_DATE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Ach\nQty\n(Pcs)");
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
            tblHead.addView(tr);
        }

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
