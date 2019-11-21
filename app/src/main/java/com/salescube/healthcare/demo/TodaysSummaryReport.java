package com.salescube.healthcare.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vTodaySummary;

import java.util.Date;
import java.util.List;

public class TodaysSummaryReport extends BaseAppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private TableLayout tblHead;
    private TableLayout tblData;
    private TextView txtTC;
    private TextView txtPC;

    private final static float WGT_SHOP = 3;
    private final static float WGT_PRODUCT = 4;
    private final static float WGT_QTY = 1.5f;
    private final static float WGT_RATE = 2;
    private final static float WGT_VALUE = 3;
    private final static float WGT_EDIT_BTN = 1;
    private final static int FONT_SIZE = 14;
    private final static int LEFT_PADDING = 2;
    private final static int RIGHT_PADDING = 5;

    private Date mOrderDate;
    private Spinner spnOrderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_summary_report);

        try {
            initControls();
            addHeader();
            showOrders(AppControl.getTodayDate());
        } catch (Exception e) {
            errMsg("While loading todays summary", e);
            finish();
        }

    }

    private void initControls() {

        tblHead = (TableLayout) findViewById(R.id.todays_summary_report_tbl_head);
        tblData = (TableLayout) findViewById(R.id.todays_summary_report_tbl_data);
        spnOrderDate = (Spinner) findViewById(R.id.today_summary_spn_order_date);

        txtTC = (TextView) findViewById(R.id.today_summary_txt_tc);
        txtPC = (TextView) findViewById(R.id.today_summary_txt_pc);

        SpinnerHelper.FillSysDates(spnOrderDate, AppControl.getmEmployeeId());
        spnOrderDate.setOnItemSelectedListener(this);
    }

    private void addHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Product Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Rate");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_RATE));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
        tv.setTextSize(FONT_SIZE);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

//        tv = new TextView(this);
//        tv.setPadding(10, 0, 10, 0);
//        tv.setText("Edtit");
//        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_EDIT_BTN));
//        tv.setTextSize(14);
//        tv.setGravity(Gravity.RIGHT);
//        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
//        tr.addView(tv);


        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

    }

    private void showOrders(Date ordrDate) {

        SalesOrderRepo repo = new SalesOrderRepo();
        List<vTodaySummary> orders = repo.getTodaySummary(AppControl.getmEmployeeId(), ordrDate);
        tblData.removeAllViews();

        if (orders != null) {
            loadData(orders);
        }
    }

    private void loadData(List<vTodaySummary> orders) {

        TableRow tr;
        TextView tv;
        double totalValue = 0;
        int dataCellColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        tblData.removeAllViews();

        for (vTodaySummary order : orders) {

            tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getProductName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(order.getOrderQty()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(order.getRate()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_RATE));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf((int) order.getOrderValue()));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblData.addView(tr);

            totalValue += order.getOrderValue();
        }

        // TOTAL

        TableRow.LayoutParams paras = new TableRow.LayoutParams(0, -1, WGT_PRODUCT + WGT_QTY + WGT_RATE);

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        paras.span = 3;
        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Total Orders:");
        tv.setLayoutParams(paras);
        tv.setTextSize(FONT_SIZE);
        tv.setGravity(Gravity.RIGHT);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setTextColor(Color.BLACK);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText(String.valueOf((int) totalValue));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
        tv.setTextSize(FONT_SIZE);
        tv.setGravity(Gravity.RIGHT);
        tv.setTextColor(Color.BLACK);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblData.addView(tr);
    }

    @Override
    public void onClick(View view) {

        if (view instanceof Button) {
            String tag = ((Button) view).getText().toString();
            TableRow tr = (TableRow) view.getParent();

            int agentId = (int) tr.getTag();
            final String appShopId = tr.getChildAt(0).getTag().toString();
            String orderDateStr = tr.getChildAt(1).getTag().toString();
            final Date orderDate = DateFunc.getDate(orderDateStr, "dd/MM/yyyy");

            if (tag == "Edit") {
                Intent orderIntent = new Intent(TodaysSummaryReport.this, SalesOrderEntryActivity.class);

                orderIntent.putExtra(Constant.ORDER_DATE, orderDateStr);
                orderIntent.putExtra(Constant.SHOP_APP_ID, appShopId);
                orderIntent.putExtra(Constant.AGENT_ID, agentId);
                orderIntent.putExtra(Constant.IS_NEW, true);
                startActivity(orderIntent);
            }

            if (tag == "Delete") {

                AlertDialog.Builder alert = new AlertDialog.Builder(TodaysSummaryReport.this);
                alert.setTitle("Confirm!");
                alert.setMessage("Are you sure you want to cancel order?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SalesOrderRepo().cancelShopOrders(appShopId, orderDate);

                        // refresh orders
                        showOrders(orderDate);
                    }
                });

                alert.create();
                alert.show();


            }
        }

    }

    private void refreshTCPC(Date orderDate) {
        int[] tcPc = new SalesOrderRepo().getTCPC(AppControl.getmEmployeeId(), orderDate);

        txtTC.setText("TC: " + String.valueOf(tcPc[0]));
        txtPC.setText("PC: " + String.valueOf(tcPc[1]));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object selectedDate = spnOrderDate.getSelectedItem();
        if (selectedDate == null) {
            return;
        }

        Date orderDate = DateFunc.getDate(selectedDate.toString(), "dd/MM/yyyy");
        if (orderDate != null) {
            refreshTCPC(orderDate);
            showOrders(orderDate);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
