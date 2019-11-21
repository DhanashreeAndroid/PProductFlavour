package com.salescube.healthcare.demo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.data.repo.SalesOrderPreviousRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vLastOrder;

import java.util.Date;
import java.util.List;

public class SalesOrderViewActivity extends BaseAppCompatActivity {

    private TextView txtOrderDate;
    private TextView txtShopName;
    private TableLayout tblMain;
    private TableLayout tblHeader;

    private String mAppShopId;
    private String mOrderDate;
    private String mShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_view);

        initExtra();
        initControls();

        showOrders();
        addHeader();
    }

    private void initExtra(){

        Intent intent = getIntent();
        mOrderDate = intent.getStringExtra(Constant.ORDER_DATE);
        mShopName = intent.getStringExtra(Constant.SHOP_NAME);
        mAppShopId = intent.getStringExtra(Constant.SHOP_APP_ID);

    }

    private void initControls() {

        txtOrderDate = (TextView) findViewById(R.id.orderView_txt_orderDate);
        txtShopName = (TextView) findViewById(R.id.orderView_txt_shopName);

        tblMain = (TableLayout) findViewById(R.id.order_view_tbl);
        tblHeader = (TableLayout) findViewById(R.id.order_view_tbl_header);

        // txtOrderDate.setText(mOrderDate);
        txtShopName.setText(mShopName);

    }

    private void addHeader() {

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Product Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 8f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHeader.addView(tr);

    }

    private  void showOrders(){

        SalesOrderPreviousRepo repo = new SalesOrderPreviousRepo();
        List<vLastOrder> orders = repo.getOrders(AppControl.getmEmployeeId(), mAppShopId);

        if (orders != null) {
            loadData(orders);
        }else{
            msgShort("Last orders not found!");
        }
    }

    private void loadData(List<vLastOrder> orders) {

        TableRow tr;
        TextView tv;
        double totalValue = 0;
        Date lastOrderDate = null;

        for (vLastOrder order : orders) {

            tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);

            tv = new TextView(this);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(order.getProductName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 8f));
            tv.setTextSize(14);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(String.valueOf(order.getOrderQty()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
            tv.setTextSize(14);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(String.valueOf((int) order.getOrderValue()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
            tv.setTextSize(14);
            tv.setGravity(Gravity.RIGHT);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblMain.addView(tr);

            totalValue += order.getOrderValue();
            lastOrderDate = order.getOrderDate();
        }


        // TOTAL

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("TOTAL:");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 8f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf(""));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf((int) totalValue));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblMain.addView(tr);

        txtOrderDate.setText(DateFunc.getDateStr(lastOrderDate,"dd/MM/yyyy"));
    }

}
