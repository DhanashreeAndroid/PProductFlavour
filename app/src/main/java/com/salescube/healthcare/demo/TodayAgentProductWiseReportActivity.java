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
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.AppEvent;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vShopWiseOrder;
import com.salescube.healthcare.demo.view.vTodayOrders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodayAgentProductWiseReportActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spnAgent;
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

    private List<vAgent> agentList;

    private List<vTodayOrders> orderList;
    private AgentProductWiseOrderAdapter adapter;
    private double totalValue = 0;
    private Context mContext;
    ArrayAdapter<vAgent> adpAgent;


    private final int ORDER_REQUEST = 02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_product_report);
        title("Distributor Product Wise Report");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Shop Product Wise Report");
//            actionBar.setIcon(R.drawable.icon_company);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }


        orderList = new ArrayList<>();
        agentList = new ArrayList<>();

        try {
            initControls();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }


    private void initControls() {
        mContext = TodayAgentProductWiseReportActivity.this;

//        resetReport = (Button) findViewById(R.id.shp_prod_btn_report);
        mTblHead = (TableLayout) findViewById(R.id.shp_prod_tbl_header);
        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);

        tvQtyTotal = findViewById(R.id.shop_prod_tv_total_qty);
        tvDiscTotal = findViewById(R.id.shop_prod_tv_total_disc);
        tvValueTotal = findViewById(R.id.shop_prod_tv_total_value);

        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);
        mTblGrand = (LinearLayout) findViewById(R.id.shop_prod_ll_grand_total);

        spnAgent = findViewById(R.id.shp_prod_spn_shop);
        mShopProduct = findViewById(R.id.shp_prod_rcv_lst);
        mShopProduct.setLayoutManager(new LinearLayoutManager(TodayAgentProductWiseReportActivity.this));
    }

    private void initData() {
        SpinnerAgentList(spnAgent);
        loadGridHeader();
        orderList = new SalesOrderRepo().getToadyAgentProductSummary(AppControl.getmEmployeeId(), AppControl.getTodayDate());
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
                    orderList = new SalesOrderRepo().getToadyAgentProductSummary(AppControl.getmEmployeeId(), AppControl.getTodayDate());
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


    private void SpinnerAgentList(Spinner spnShop) {
        AgentRepo repo = new AgentRepo();
        agentList = repo.getAgentAll( AppControl.getmEmployeeId()) ;
        agentList.add(0, new vAgent(0,"All"));
        adpAgent = new ArrayAdapter<vAgent>(this.spnAgent.getContext(), android.R.layout.simple_spinner_item, agentList);
        adpAgent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpAgent.notifyDataSetChanged();
        spnShop.setAdapter(adpAgent);
        spnShop.setOnItemSelectedListener(this);
    }

    private void initListner() {
        spnAgent.setOnItemSelectedListener(this);
    }

    private void showList(List<vTodayOrders> orderList) {

        adapter = null;

        totalValue = 0;

        adapter = new AgentProductWiseOrderAdapter(TodayAgentProductWiseReportActivity.this, orderList);
        if (adapter.getItemCount() == 0) {
            Toast.makeText(TodayAgentProductWiseReportActivity.this, "No Sales Order Found", Toast.LENGTH_LONG).show();
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
            vAgent agent = (vAgent) spnAgent.getSelectedItem();
            List<vTodayOrders> selectedOrder = new ArrayList<>();

            if (agent != null) {
                if (agent.getAgentId() != 0) {
                    for (vTodayOrders todayOrders : orderList) {
                        if (todayOrders.getAgentId() == agent.getAgentId()) {
                            if (!selectedOrder.contains(todayOrders)) {
                                selectedOrder.add(todayOrders);
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

    private class AgentProductWiseOrderAdapter extends RecyclerView.Adapter<AgentProductWiseOrderAdapter.ViewHolder> {

        Context context;
        List<vTodayOrders> shopWiseOrders;
        List<vTodayOrders> lstProductOrder = new ArrayList<>();
        vShopWiseOrder employee;

        AgentProductWiseOrderAdapter(Context context, List<vTodayOrders> shopWiseOrders) {
            this.context = context;
            this.shopWiseOrders = shopWiseOrders;
        }


        @NonNull
        @Override
        public AgentProductWiseOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shop_product_report, parent, false);
            return new AgentProductWiseOrderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AgentProductWiseOrderAdapter.ViewHolder holder, int position) {
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
            tv.setText(" # " + order.getAgentName());
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 2.6f));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setBackgroundResource(R.drawable.table_cell_grey);
            tv.setTextColor(headingColor);
            mShop.addView(tv);

            holder.mShopName.removeAllViews();
            if(!TextUtils.isEmpty(order.getAgentName())) {
                if (position > 0) {
                    if (!order.getAgentName().equals(shopWiseOrders.get(position - 1).getAgentName())) {
                        holder.mShopName.addView(mShop);
                        loadProduct(holder, order, order.getAgentName());
                    } else {
                        holder.mShopName.removeView(mShop);
                        loadProduct(holder, order, order.getAgentName());
                    }
                } else {
                    holder.mShopName.addView(mShop);
                    loadProduct(holder, order, order.getAgentName());
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
