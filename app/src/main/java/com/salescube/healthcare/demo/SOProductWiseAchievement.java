package com.salescube.healthcare.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vDailySalesReport;
import com.salescube.healthcare.demo.view.vProductWiseSaleQty;
import com.salescube.healthcare.demo.view.vSO;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SOProductWiseAchievement extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {

    TableLayout mTblHead;
    RecyclerView mProductData;
    Spinner mSpnSO;

    private List<vDailySalesReport> lstProduct, lstEmployeeProduct;

    private vSO employee;


    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float WGT_PRODUCT_NAME = 2;
    private static final float WGT_QTY_COL = 1.4f;
    private static final float WGT_VALUE_COL = 0.8f;
    private static final float FONT_SIZE = 16;
    private DaliySalesReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sales_report);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product Wise Sales (In Qty)");
        }

        try {
            initControls();
            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initControls() {
        lstProduct = new ArrayList<>();
        lstEmployeeProduct = new ArrayList<>();

        mTblHead = (TableLayout) findViewById(R.id.daily_sales_tbl_header);
        mSpnSO = (Spinner) findViewById(R.id.daily_sales_so_spn);
        mProductData = (RecyclerView) findViewById(R.id.daily_sales_prod_rcv_lst);
        mProductData.setLayoutManager(new LinearLayoutManager(SOProductWiseAchievement.this));

    }

    private void initData() {
        loadGridHeader();

        fetchSOProductList(AppControl.getmEmployeeId());
//        loadSpinner(lstProduct, mSpnSO);

//        loadEmployeeProductList(insertDummyList());
    }

    private static void loadSpinner(List<vSO> lstProduct, Spinner mSpnSO) {
//

        ArrayAdapter adp = new ArrayAdapter<>(mSpnSO.getContext(), android.R.layout.simple_spinner_item, lstProduct);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSO.setAdapter(adp);
    }

    private void fetchSOProductList(int soId) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<vProductWiseSaleQty> query = apiService.getSOTodayProductSalesQTY(soId);

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<vProductWiseSaleQty>() {
                @Override
                public void onResponse(Call<vProductWiseSaleQty> call, Response<vProductWiseSaleQty> response) {
                    if (response.isSuccessful()) {

                        List<vDailySalesReport> reports = response.body().getData();
                        List<vSO> soList = response.body().getSoList();

                        lstProduct.clear();
                        lstProduct.addAll(reports);
                        loadEmployeeProductList(reports);

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
                public void onFailure(Call<vProductWiseSaleQty> call, Throwable t) {
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

    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 8, 5, 8);

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
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Tgt");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Ach");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("%");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE_COL));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);


        tr.setBackgroundResource(R.drawable.table_row_header);
        mTblHead.addView(tr);
    }

    private void initListener() {
        mSpnSO.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int i = parent.getId();
        if (i == R.id.daily_sales_so_spn) {
            employee = (vSO) mSpnSO.getSelectedItem();
            lstEmployeeProduct.clear();

            if (employee != null) {
                if (employee.getSoId() == 0) {
                    lstEmployeeProduct.addAll(lstProduct);
                } else {
                    for (vDailySalesReport emp : lstProduct) {
                        if (employee.getSoId() == emp.getSoId()) {

                            lstEmployeeProduct.add(emp);
                        }
                    }
                }
                loadEmployeeProductList(lstEmployeeProduct);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void loadEmployeeProductList(List<vDailySalesReport> lstEmployeeProduct) {
        List<vDailySalesReport> lstProduct = new ArrayList<>();
        lstProduct.clear();
        lstProduct.addAll(lstEmployeeProduct);
        adapter = new DaliySalesReportAdapter(SOProductWiseAchievement.this, lstProduct);
        if (adapter.getItemCount() == 0) {
//            Toast.makeText(SOProductWiseAchievement.this,"Data not found...!!",Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        mProductData.setAdapter(adapter);
    }

    private class DaliySalesReportAdapter extends RecyclerView.Adapter<DaliySalesReportAdapter.ViewHolder> {

        Context context;
        List<vDailySalesReport> lstProduct;

        public DaliySalesReportAdapter(Context context, List<vDailySalesReport> lstProduct) {
            this.context = context;
            this.lstProduct = lstProduct;
        }


        @NonNull
        @Override
        public DaliySalesReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
            return new DaliySalesReportAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DaliySalesReportAdapter.ViewHolder holder, int position) {
            vDailySalesReport product = lstProduct.get(position);
            loadProductItem(holder, product);

        }

        private void loadProductItem(ViewHolder holder, vDailySalesReport product) {
            TextView tv;
            TableRow tr;

            tr = new TableRow(context);
            tr.setPadding(5, 8, 5, 8);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(product.getProductName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tgt = (int) Math.round(product.getTgt());
            tv.setText(Parse.toStr(tgt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int ach = (int) Math.round(product.getAch());
            tv.setText(Parse.toStr(ach));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tot = (int) Math.round(product.getTotalValue());

            if (tgt == 0 && ach > 0) {
                tot = 100;
            } else {
                if (tgt > 0) {
                    tot = ((ach * 100 / tgt));
                } else {
                    tot = 0;
                }
            }

            tv.setText(Parse.toStr(tot) + "%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            holder.ll_product.removeAllViews();

            holder.ll_product.setBackgroundResource(R.drawable.table_row_header);
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
                ll_product = itemView.findViewById(R.id.item_ll_prod);
            }
        }
    }
}
