package com.salescube.healthcare.demo;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.ShopWiseOrder;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vSO;
import com.salescube.healthcare.demo.view.vSOTodayShopOrder;
import com.salescube.healthcare.demo.view.vShopWiseOrder;
import com.salescube.healthcare.demo.view.vSysDate;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopWiseOrderActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {


    private TableLayout mTblHead;
//    private TableLayout mTblData;
    private RecyclerView mOrderData;
    private Spinner mSpnOrderDates;
    private Spinner mSpnSO;
    private Button btnSubmit;

    private List<ShopWiseOrder> lstShopWiseOrder,fetchList;

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float WGT_PRODUCT_NAME = 2;
    private static final float WGT_QTY_COL = 1;
    private static final float WGT_VALUE_COL = 1;
    private static final float FONT_SIZE = 16;
    private vSysDate date;
    private vSO employee;
    private ShopWiseOrderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activty_shop_wise_order);
        title("Shop Wise Order");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Shop Wise Order");
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

    private void initControls() {
        fetchList=new ArrayList<>();
        lstShopWiseOrder=new ArrayList<>();
        mTblHead = (TableLayout) findViewById(R.id.shp_tbl_header);
        mOrderData=findViewById(R.id.shp_rcv_lst);
        mOrderData.setLayoutManager(new LinearLayoutManager(this));

        mSpnOrderDates = (Spinner) findViewById(R.id.shp_spn_dates);
        mSpnSO = (Spinner) findViewById(R.id.shp_spn_so);
    }

    private void initData() {

        SpinnerHelper.FillSysDates(mSpnOrderDates, AppControl.getmEmployeeId());
//        SpinnerHelper.FillShopWiseSO(mSpnSO);
        loadGridHeader();
        loadShopWiseOrder();
    }


    private void loadShopWiseOrder() {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService=ApiClient.getClient().create(ApiService.class);
        Call<vSOTodayShopOrder> query = apiService.getSOShopOrder(AppControl.getmEmployeeId());

        if (query == null) {
            Log.e("","");
        }
        query.enqueue(new Callback<vSOTodayShopOrder>() {
            @Override
            public void onResponse(Call<vSOTodayShopOrder> call, Response<vSOTodayShopOrder> response) {
                if(response.isSuccessful())
                {
                    try {
                        List<ShopWiseOrder> reports = response.body().getData();
                        List<vSO> soList = response.body().getSoList();

                        fetchList.clear();
                        fetchList.addAll(reports);

//                        fetchList.addAll(shopWiseOrders);
                        loadShopProductList(fetchList);
                        loadSpinner(mSpnSO,soList);
                        UtilityFunc.dismissDialog();
                    } catch (Exception e) {
                        UtilityFunc.dismissDialog();
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }
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
            public void onFailure(Call<vSOTodayShopOrder> call, Throwable t) {
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


    @Override
    protected void onDestroy() {
        UtilityFunc.dismissDialog();
        super.onDestroy();
    }

    private void loadSpinner(Spinner spn, List<vSO> objList) {

        ArrayAdapter<vSO> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp.notifyDataSetChanged();
        spn.setAdapter(adp);
    }

    private void loadShopProductList(List<ShopWiseOrder> lstShopWiseOrder) {
        List<ShopWiseOrder> lstOrder= new ArrayList<>();
        lstOrder.clear();
        lstOrder.addAll(lstShopWiseOrder);
        adapter = new ShopWiseOrderAdapter(ShopWiseOrderActivity.this, lstOrder);
        if(adapter.getItemCount() == 0){
//            Toast.makeText(ShopWiseOrderActivity.this,"Data not Found...!!",Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        mOrderData.setAdapter(adapter);
    }

    private void initListener() {
        mSpnOrderDates.setOnItemSelectedListener(this);
        mSpnOrderDates.setClickable(false);
        mSpnOrderDates.setFocusable(false);

        mSpnSO.setOnItemSelectedListener(this);
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
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Value");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List<ShopWiseOrder> employeeList=new ArrayList<>();

        int i = parent.getId();
        if (i == R.id.shp_spn_dates) {
            date = (vSysDate) mSpnOrderDates.getSelectedItem();
            employee = (vSO) mSpnSO.getSelectedItem();

            if ((date != null) && (employee != null)) {
//                    loadGridData(date.getTrDate(), employee.getEmpId(),lstShopWiseOrder);

            }

        } else if (i == R.id.shp_spn_so) {
            date = (vSysDate) mSpnOrderDates.getSelectedItem();

            employee = (vSO) mSpnSO.getSelectedItem();

            if ((date != null) && (employee != null)) {
                if (employee.getSoId() == 0) {
                    employeeList.addAll(fetchList);
                } else {
                    for (ShopWiseOrder so : fetchList) {
                        if (employee.getSoId() == so.getSoId()) {
                            employeeList.add(so);
                        } else {
                            employeeList.remove(so);
                        }
                    }
                }
            }
            loadShopProductList(employeeList);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ShopWiseOrderAdapter extends RecyclerView.Adapter<ShopWiseOrderAdapter.ViewHolder> {
        Context context;
        List<ShopWiseOrder> shopWiseOrders;
        List<ShopWiseOrder> lstProductOrder=new ArrayList<>();
        vShopWiseOrder employee;

        ShopWiseOrderAdapter(Context context, List<ShopWiseOrder> shopWiseOrders) {
            this.context=context;
            this.shopWiseOrders=shopWiseOrders;
//            this.lstProductOrder=shopWiseOrders;
        }

        @NonNull
        @Override
        public ShopWiseOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.item_shop_product_order,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopWiseOrderAdapter.ViewHolder holder, int position) {
            ShopWiseOrder order = shopWiseOrders.get(position);

            TextView tv;

            int headingColor = Color.BLACK;

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(" # "+order.getShopName());
            tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            tv.setTextSize(FONT_SIZE);
//            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(null,Typeface.BOLD);
            tv.setBackgroundResource(R.drawable.table_cell_grey);
            tv.setTextColor(headingColor);

            holder.mShopName.removeAllViews();
            if(position>0)
            {
              if(!order.getShopName().equals(shopWiseOrders.get(position-1).getShopName())){

                  holder.mShopName.addView(tv);
                  // add product list
                  loadProduct(holder.mProductData, order);

              }else{
                  holder.mShopName.removeView(tv);
                  loadProduct(holder.mProductData, order);
              }
            }else{

                holder.mShopName.addView(tv);
                loadProduct(holder.mProductData, order);
            }

        }

        // add product list
        private void loadProduct(@NonNull LinearLayout holder, ShopWiseOrder order) {
            TextView tv;// add product list
            holder.removeAllViews();
            LinearLayout.LayoutParams ll_product=new LinearLayout.LayoutParams(0, -1, WGT_PRODUCT_NAME);
            ll_product.setMargins(0,1,0,1);
            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getProductName());
            tv.setLayoutParams(ll_product);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.LEFT);
            holder.addView(tv);

            holder.setBackgroundResource(R.drawable.table_row_header);
            LinearLayout.LayoutParams ll_QTY=new LinearLayout.LayoutParams(0, -1, WGT_QTY_COL);
            ll_product.setMargins(0,1,0,1);
            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int)order.getOrderQty()));
            tv.setLayoutParams(ll_QTY);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.RIGHT);
            holder.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int)order.getTotalAmount()));
            tv.setLayoutParams(ll_QTY);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_row_header);
            tv.setGravity(Gravity.RIGHT);
            holder.addView(tv);

        }

        @Override
        public int getItemCount() {
            return shopWiseOrders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
           private LinearLayout mShopName;
           private LinearLayout mProductData;
            ViewHolder(View itemView) {
                super(itemView);

                mShopName=itemView.findViewById(R.id.item_ll_shop_order);
                mProductData=itemView.findViewById(R.id.item_ll_order_data);
            }
        }

    }
}
