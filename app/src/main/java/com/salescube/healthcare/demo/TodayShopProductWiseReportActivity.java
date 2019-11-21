package com.salescube.healthcare.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.AppEvent;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vShopWiseOrder;
import com.salescube.healthcare.demo.view.vTodayOrders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodayShopProductWiseReportActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spnShop;
    private RecyclerView mShopProduct;
    private TableLayout mTblHead;
    private LinearLayout mTblGrand;
    private Button resetReport;

    private TextView tvQtyTotal;
    private TextView tvDiscTotal;
    private TextView tvValueTotal;


    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float WGT_PRODUCT_NAME = 2.2f;
    private static final float WGT_QTY_COL = 0.6f;
    private static final float WGT_VALUE_COL = 0.8f;
    private static final float FONT_SIZE = 18;
    private static final float TEXT_FONT_SIZE = 16;

    private List<vTodayOrders> orderList, shpLists;
    private ShopProductWiseOrderAdapter adapter;
    private double totalValue = 0;
    private Context mContext;
    ArrayAdapter<vTodayOrders> adpSpnShop;


    private final int ORDER_REQUEST = 02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product_report);
        title("Shop Product Wise Report");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Shop Product Wise Report");
//            actionBar.setIcon(R.drawable.icon_company);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }


        orderList = new ArrayList<>();
        shpLists = new ArrayList<>();

        try {
            initControls();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }


    private void initControls() {
        mContext = TodayShopProductWiseReportActivity.this;

//        resetReport = (Button) findViewById(R.id.shp_prod_btn_report);
        mTblHead = (TableLayout) findViewById(R.id.shp_prod_tbl_header);
        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);

        tvQtyTotal = findViewById(R.id.shop_prod_tv_total_qty);
        tvDiscTotal = findViewById(R.id.shop_prod_tv_total_disc);
        tvValueTotal = findViewById(R.id.shop_prod_tv_total_value);

        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);
        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);

        spnShop = findViewById(R.id.shp_prod_spn_shop);
        mShopProduct = findViewById(R.id.shp_prod_rcv_lst);
        mShopProduct.setLayoutManager(new LinearLayoutManager(TodayShopProductWiseReportActivity.this));
    }

    private void initData() {
        SpinnerShopList(spnShop);
        loadGridHeader();
        orderList = new SalesOrderRepo().getToadyShopProductSummary(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        showList(orderList);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        orderList.clear();
//        orderList=new SalesOrderRepo().getToadyShopProductSummary(AppControl.getmEmployeeId(),AppControl.getTodayDate());
//        showList(orderList);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ORDER_REQUEST) {
            boolean success = data.getBooleanExtra("HasSuccess", false);
            if (success) {

                try {
                    doManualUpload("", AppEvent.EVENT_AUTO);

//                    refreshActivity();
                    orderList.clear();
                    orderList = new SalesOrderRepo().getToadyShopProductSummary(AppControl.getmEmployeeId(), AppControl.getTodayDate());
                    showList(orderList);
                } catch (Exception e) {

                }
            }
        }

    }

    private void refreshActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    private void SpinnerShopList(Spinner spnShop) {
        shpLists = new SalesOrderRepo().getShopWiseTodayOrders(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        shpLists.add(0, new vTodayOrders("0", "All "));
        adpSpnShop = new ArrayAdapter<vTodayOrders>(this.spnShop.getContext(), android.R.layout.simple_spinner_item, shpLists);
        adpSpnShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpSpnShop.notifyDataSetChanged();
        spnShop.setAdapter(adpSpnShop);
        spnShop.setOnItemSelectedListener(this);
    }

    private void initListner() {
        spnShop.setOnItemSelectedListener(this);
    }

    private void showList(List<vTodayOrders> orderList) {

        adapter = null;

        totalValue = 0;

        adapter = new ShopProductWiseOrderAdapter(TodayShopProductWiseReportActivity.this, orderList);
        if (adapter.getItemCount() == 0) {
            Toast.makeText(TodayShopProductWiseReportActivity.this, "No Sales Order Found", Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        mShopProduct.setAdapter(adapter);
        if(orderList != null && orderList.size() >0){
            for(int i = 0; i < orderList.size(); i++){
                totalValue += orderList.get(i).getOrderValue();
            }
        }
        addTotal(totalValue);
    }

    private void addTotal(double totalValue) {

        TableRow tr;
        TextView tv;// add product list

        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tvQtyTotal.setText("");
        tvDiscTotal.setText("");
        int tot = (int) Math.round(totalValue);
        tvValueTotal.setText(String.valueOf(tot));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int i = parent.getId();
        if (i == R.id.shp_prod_spn_shop) {
            vTodayOrders orders = (vTodayOrders) spnShop.getSelectedItem();
            List<vTodayOrders> selectedOrder = new ArrayList<>();

            if (orders != null) {
                if (!orders.getAppShopId().equals("0")) {
                    for (vTodayOrders todayOrders : orderList) {
                        if (todayOrders.getShopName().equals(orders.toString())) {
                            if (!selectedOrder.contains(todayOrders)) {
                                selectedOrder.add(todayOrders);
                            } else {
                                selectedOrder.remove(orders);
                            }
                        }
                    }
                } else {
                    selectedOrder.addAll(orderList);
                }
                showList(selectedOrder);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        tv.setTextSize(TEXT_FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
        tv.setTextSize(TEXT_FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Disc");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
        tv.setTextSize(TEXT_FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE_COL));
        tv.setTextSize(TEXT_FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        mTblHead.addView(tr);
    }

    private class ShopProductWiseOrderAdapter extends RecyclerView.Adapter<ShopProductWiseOrderAdapter.ViewHolder> {

        Context context;
        List<vTodayOrders> shopWiseOrders;
        List<vTodayOrders> lstProductOrder = new ArrayList<>();
        vShopWiseOrder employee;

        ShopProductWiseOrderAdapter(Context context, List<vTodayOrders> shopWiseOrders) {
            this.context = context;
            this.shopWiseOrders = shopWiseOrders;
        }


        @NonNull
        @Override
        public ShopProductWiseOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shop_product_report, parent, false);
            return new ShopProductWiseOrderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopProductWiseOrderAdapter.ViewHolder holder, int position) {
            final vTodayOrders order = shopWiseOrders.get(position);

            holder.mProductData.removeAllViews();

            TextView tv;

            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);
            int headingColor = Color.BLACK;

            LinearLayout mShop = new LinearLayout(context);
            mShop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f));
            mShop.setOrientation(LinearLayout.HORIZONTAL);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, 0, 0);
            tv.setText(" # " + order.getShopName());
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 2.6f));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setBackgroundResource(R.drawable.table_cell_grey);
            tv.setTextColor(headingColor);
            mShop.addView(tv);

            tv = new TextView(context);
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 0.4f));
            tv.setPadding(0, 0, RIGHT_PADDING, 0);
            SpannableString content = new SpannableString("EDIT");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv.setText(content);
            tv.setTextColor(Color.BLUE);
            tv.setTypeface(tf);
            tv.setBackgroundResource(R.drawable.table_cell_grey);
            tv.setGravity(Gravity.CENTER);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat objDf = new SimpleDateFormat("dd/MM/yyyy");

                    Intent orderIntent = new Intent(context, SalesOrderEntryActivity.class);

                    orderIntent.putExtra(Constant.ORDER_DATE, objDf.format(order.getOrderDate()));
                    orderIntent.putExtra(Constant.SHOP_APP_ID, order.getAppShopId());
                    orderIntent.putExtra(Constant.SHOP_NAME, order.getShopName());
                    orderIntent.putExtra(Constant.AREA_ID, order.getAreaId());
                    orderIntent.putExtra(Constant.AGENT_ID, order.getAgentId());
                    orderIntent.putExtra(Constant.IS_NEW, false);

                    startActivityForResult(orderIntent, ORDER_REQUEST);

                }
            });
            mShop.addView(tv);
//            }
            holder.mShopName.removeAllViews();
            if(!TextUtils.isEmpty(order.getShopName())) {
                if (position > 0) {
                    if (!order.getShopName().equals(shopWiseOrders.get(position - 1).getShopName())) {
                        holder.mShopName.addView(mShop);
                        loadProduct(holder, order, order.getShopName());
                    } else {
                        holder.mShopName.removeView(mShop);
                        loadProduct(holder, order, order.getShopName());
                    }
                } else {
                    holder.mShopName.addView(mShop);
                    loadProduct(holder, order, order.getShopName());
                }
            }

        }

        // add product list
        private void loadProduct(@NonNull ViewHolder holder, vTodayOrders order, String shopName) {

            TableRow tr;
            TextView tv;// add product list

            Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

            tr = new TableRow(context);
            tr.setPadding(5, 4, 5, 4);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getProductName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT_NAME));
            tv.setTextSize(TEXT_FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int) order.getOrderQty()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(TEXT_FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tr.addView(tv);
//            qtyOrder+=order.getOrderQty();

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int amt = (int) Math.round(Parse.toDbl(order.getDiscount()));
            tv.setText(Parse.toStr(amt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY_COL));
            tv.setTextSize(TEXT_FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tr.addView(tv);

            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int tot = (int) Math.round(order.getOrderValue());
            tv.setText(String.valueOf(tot));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE_COL));
            tv.setTextSize(TEXT_FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            holder.mProductData.addView(tr);

            totalValue += order.getOrderValue();
          //  addTotal(totalValue);
        }

        @Override
        public int getItemCount() {
            return shopWiseOrders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout mShopName, mShop;
            private TableLayout mProductData;
            private TextView tvEditItem;

            ViewHolder(View itemView) {
                super(itemView);
//                mCardView=itemView.findViewById(R.id.item_ll_shop_prod_card);
                mShopName = itemView.findViewById(R.id.item_ll_shop_prod_order);
//                mShop=itemView.findViewById(R.id.item_ll_shop_name);
                mProductData = itemView.findViewById(R.id.item_tbl_prod_order_data);
//                mProductTotal=itemView.findViewById(R.id.item_ll_prod_order_total);
//                tvEditItem=itemView.findViewById(R.id.item_tv_shop_product);
//                SpannableString content = new SpannableString(tvEditItem.getText().toString());
//                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//                tvEditItem.setText(content);
            }

        }

    }
}
