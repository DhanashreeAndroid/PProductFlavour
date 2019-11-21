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
import com.salescube.healthcare.demo.data.model.SOMonthProductSales;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vMonthYear;
import com.salescube.healthcare.demo.view.vSO;
import com.salescube.healthcare.demo.view.vSOMonthSalesProductQtyReport;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoMonthProductReportActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {

    private TableLayout TblSpn;
    private Spinner mSpn;
    private Spinner mSpnSO;

    private TableLayout TblHead;

    private RecyclerView mProductData;

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float WGT_PRODUCT_NAME = 2;
    private static final float WGT_QTY_COL = 1.4f;
    private static final float WGT_VALUE_COL = 0.8f;
    private static final float FONT_SIZE = 16;

    private List<SOMonthProductSales> lstProductList;
    SOMonthProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_month_product_report);
        title("Product Wise Performance ");


//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Product Wise Performance ");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initListener() {
        mSpn.setOnItemSelectedListener(this);
        mSpnSO.setOnItemSelectedListener(this);
    }

    private void initControls() {
        lstProductList=new ArrayList<>();

        TblSpn=findViewById(R.id.so_month_product_report_tbl_spinner);
        TblSpn.setVisibility(View.VISIBLE);

        mSpn=(Spinner)findViewById(R.id.so_month_product_spn_month_year) ;
        mSpnSO=(Spinner)findViewById(R.id.so_month_product_spn_so) ;

        TblHead=findViewById(R.id.so_month_product_report_tbl_head);
        mProductData=findViewById(R.id.so_month_product_report_rcv_lst);
        mProductData.setLayoutManager(new LinearLayoutManager(SoMonthProductReportActivity.this));
    }

    private void initData() {
        SpinnerHelper.FillMonthYear(mSpn,0,3);
        loadGridHeader();

        Date today = DateFunc.getDate();

        String fromDate = DateFunc.FO_MonthStr(today);
        String toDate = DateFunc.EO_MonthStr(today);
        fetchSOProductList(AppControl.getmEmployeeId(),fromDate,toDate);
    }

    /*private List<SOMonthProductSales> insertDumm1List() {
        List<SOMonthProductSales> lst = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                lst.add(i, new SOMonthProductSales(i, "All", i, "Product" + i, (i * 5000.01), (i * 5000.01), ((i + 1) * 20)));
            } else {
                lst.add(i, new SOMonthProductSales(i, "SO  " + i, i, "Product" + i, (i * 5000.01), (i * 5000.01), ((i + 1) * 20)));
            }
        }
        return lst;
    }*/

    private void fetchSOProductList(int soId, String fromDate ,String toDate) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<vSOMonthSalesProductQtyReport> query = apiService.getSOMonthProductSalesQTY(soId,fromDate,toDate);

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<vSOMonthSalesProductQtyReport>() {
                @Override
                public void onResponse(Call<vSOMonthSalesProductQtyReport> call, Response<vSOMonthSalesProductQtyReport> response) {
                    if (response.isSuccessful()) {

                        List<SOMonthProductSales> reports = response.body().getData();
                        List<vSO> soList = response.body().getSoList();

                        lstProductList.clear();
                        lstProductList.addAll(reports);

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
                public void onFailure(Call<vSOMonthSalesProductQtyReport> call, Throwable t) {
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

    private static void loadSpinner(List<vSO> lstProduct, Spinner mSpnSO) {

        lstProduct.add(0,new vSO(0,"SELECT"));
        ArrayAdapter adp = new ArrayAdapter<>(mSpnSO.getContext(), android.R.layout.simple_spinner_item, lstProduct);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSO.setAdapter(adp);
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
        TblHead.addView(tr);
    }

    private void loadEmployeeProductList(List<SOMonthProductSales> lstEmployeeProduct) {
        List<SOMonthProductSales> lstProduct=new ArrayList<>();
        lstProduct.clear();
        lstProduct.addAll(lstEmployeeProduct);
        adapter = new SOMonthProductAdapter(SoMonthProductReportActivity.this, lstProduct);
        if(adapter.getItemCount()== 0){
//            Toast.makeText(SoMonthProductReportActivity.this,"Data not found...!!",Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        mProductData.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vMonthYear date = new vMonthYear();
        int i = parent.getId();
        if (i == R.id.so_month_product_spn_month_year) {
            date = (vMonthYear) mSpn.getSelectedItem();
            if (date != null) {
                fetchSOProductList(AppControl.getmEmployeeId(), date.getFromDate(), date.getToDate());
            }

        } else if (i == R.id.so_month_product_spn_so) {
            vSO so = (vSO) mSpnSO.getSelectedItem();

            List<SOMonthProductSales> lst = new ArrayList<>();

            if (so != null) {
//                    if(so.getSoId()== 0){
////                        lst.addAll(lstProductList);
////                    }else{
                for (SOMonthProductSales s : lstProductList) {
                    if (so.getSoId() == s.getSoId()) {
                        lst.add(s);
                    }
                }
//                    }
            }
//                        fetchSOProductList(AppControl.getmEmployeeId(),date.getFromDate(), date.getToDate());
            loadEmployeeProductList(lst);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class SOMonthProductAdapter extends RecyclerView.Adapter<SOMonthProductAdapter.ViewHolder> {

        Context context;
        List<SOMonthProductSales> lstProduct;
        List<SOMonthProductSales> lstProduct2;

        public SOMonthProductAdapter(Context context, List<SOMonthProductSales> lstProduct) {
            this.context = context;
            this.lstProduct = lstProduct;
            this.lstProduct2 = lstProduct;
        }


        @NonNull
        @Override
        public SOMonthProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
            return new SOMonthProductAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SOMonthProductAdapter.ViewHolder holder, int position) {
            SOMonthProductSales product = lstProduct.get(position);
            loadProductItem(holder, product,position);
        }

        private void loadProductItem(ViewHolder holder, SOMonthProductSales product, int position) {
            TextView tv;
            TableRow tr;
            int ach,ach1,totAch = 0;

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
            int tgt= (int) Math.round(product.getTargetAmount());
            tv.setText(Parse.toStr(tgt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            totAch =(int) Math.round(product.getAchAmount());
            tv.setText(Parse.toStr(totAch));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tot = (int) Math.round(product.getTotalValue());

            if (tgt == 0 && totAch > 0) {
                tot = 100;
            } else {
                if (tgt > 0) {
                    tot = ((totAch * 100 / tgt));
                } else {
                    tot = 0;
                }
            }

            tv.setText(Parse.toStr(tot)+ "%");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE_COL));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tr.addView(tv);

            holder.ll_product.setBackgroundResource(R.drawable.table_row_header);
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
                ll_product = itemView.findViewById(R.id.item_ll_prod);
            }
        }
    }

}

