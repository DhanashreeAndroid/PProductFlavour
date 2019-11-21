package com.salescube.healthcare.demo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vTodayOrders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodaysOrderActivity extends BaseAppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private TableLayout tblHead;
    private TableLayout tblData;
    private Button btnUpdate;

    private final static float WGT_SHOP = 2.5f;
    private final static float WGT_PRODUCT = 3f;
    private final static float WGT_QTY = 1.2f;
    private final static float WGT_RATE = 2;
    private final static float WGT_VALUE = 2;
    private final static float WGT_EDIT_BTN = 1f;
    private final static int FONT_SIZE = 14;
    private final static int LEFT_PADDING = 4;
    private final static int RIGHT_PADDING = 8;

    private Date mOrderDate;
    private Spinner spnOrderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_order);
        title("Todays Order");

        try {
            initControls();
            initListners();
            addHeader();
            showOrders(AppControl.getTodayDate());
        } catch (Exception e) {
           errMsg("While loading todays orders",e);
            finish();
        }

    }

    private void initControls() {

        tblHead = (TableLayout) findViewById(R.id.todayOrder_tbl_head);
        tblData = (TableLayout) findViewById(R.id.todayOrder_tbl_data);
        spnOrderDate = (Spinner)findViewById(R.id.today_order_spn_order_date);
        btnUpdate = (Button)findViewById(R.id.today_order_btn_update);

        SpinnerHelper.FillSysDates(spnOrderDate, AppControl.getmEmployeeId());
        spnOrderDate.setOnItemSelectedListener(this);
    }

    private void initListners(){

        btnUpdate.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    updateData();
                    msgShort("Order updated successfully!");
                } catch (Exception e) {
                    Logger.log(Logger.ERROR,"TodaysOrder",e.getMessage(),e);
                    msgShort("Failed to update data!");
                }
            }
        });
    }

    private void addHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setText("Shop Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setText("Product Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, -1, WGT_EDIT_BTN);
        params.setMargins(5,0,0,0);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setText("");
        tv.setLayoutParams(params);
        tv.setTextSize(FONT_SIZE);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

    }

    private void showOrders(Date ordrDate) {

        SalesOrderRepo repo = new SalesOrderRepo();
        List<vTodayOrders> orders = repo.getTodayOrders(AppControl.getmEmployeeId(), ordrDate);
        tblData.removeAllViews();

        if (orders != null) {
            loadData(orders);
        }
    }

    private void loadData(List<vTodayOrders> orders) {

        Button btn;
        TableRow tr;
        TextView tv;
        EditText ed;
        int cellColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        tblData.removeAllViews();

        for (vTodayOrders order : orders) {

            tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);

            tr.setTag(DateFunc.getDateStr(order.getOrderDate(),"dd/MM/yyyy"));

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getShopName());
            tv.setTag(order.getAppShopId());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
            tv.setTextSize(FONT_SIZE);
            tv.setTypeface(tf);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(cellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getProductName());
            tv.setTag(order.getRlProductSkuId());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
            tv.setTextSize(FONT_SIZE);
            tv.setTypeface(tf);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(cellColor);
            tr.addView(tv);

            TableRow.LayoutParams params = new TableRow.LayoutParams(0, 80, WGT_QTY);
            params.setMargins(0,2,0,0);


            ed = new EditText(this);
            ed.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            ed.setText(String.valueOf(order.getOrderQty()));
            ed.setTag(String.valueOf(order.getOrderQty()));
            ed.setLayoutParams(params);
            ed.setTextSize(FONT_SIZE);
            tv.setTypeface(tf);
            ed.setBackgroundResource(R.drawable.table_cell_blue);
            ed.setGravity(Gravity.CENTER);
            ed.setTextColor(cellColor);
            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
            ed.setImeOptions(EditorInfo.IME_ACTION_DONE);
            ed.setSelection(ed.getText().length());
            tr.addView(ed);

            params = new TableRow.LayoutParams(0, 80, WGT_EDIT_BTN);
            params.setMargins(5,0,0,0);

            btn = new Button(this);
            btn.setClickable(true);
            btn.setPadding(0 , 0, 0, 8);
            btn.setText("X");
            btn.setLayoutParams(params);
            btn.setTextSize(15);
            btn.setTypeface(tf);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundResource(R.drawable.button_danger);
            btn.setTypeface(tv.getTypeface(), Typeface.BOLD);
            btn.setOnClickListener(this);
            tr.addView(btn);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblData.addView(tr);
        }

    }

    private void updateData(){

        int count = tblData.getChildCount();
        TableRow tr;
        TextView vw;
        Date orderDate;
        String appShopId;
        int rlProductSkuId;
        int orignalQty;
        int changedQty;

        SalesOrder order;
        List<SalesOrder> orderList = new ArrayList<>();

        for (int i = 0;i < count;i++){
            tr = (TableRow)tblData.getChildAt(i);
            orderDate = DateFunc.getDate(tr.getTag().toString(),"dd/MM/yyyy");

            vw = (TextView)tr.getChildAt(0);
            appShopId = vw.getTag().toString();

            vw = (TextView)tr.getChildAt(1);
            rlProductSkuId = Parse.toInt(vw.getTag());

            vw = (TextView)tr.getChildAt(2);
            orignalQty = Parse.toInt(vw.getTag());
            changedQty = Parse.toInt(vw.getText());

            if (orignalQty == changedQty){
                continue;
            }

            order = new SalesOrder();
            order.setOrderDate(orderDate);
            order.setAppShopId(appShopId);
            order.setRlProductSkuId(rlProductSkuId);
            order.setOrderQty(changedQty);
            orderList.add(order);
        }

        new SalesOrderRepo().updateOrders(orderList);

    }

    @Override
    public void onClick(View view) {

        if (view instanceof Button) {
            String tag = ((Button) view).getText().toString();
            TableRow tr = (TableRow) view.getParent();

            String orderDateStr = spnOrderDate.getSelectedItem().toString();
            final String appShopId = tr.getChildAt(0).getTag().toString();
            final int rlProductSkuId = (int)tr.getChildAt(1).getTag();
            final Date orderDate  = DateFunc.getDate(orderDateStr,"dd/MM/yyyy");

            if(tag == "X"){

                AlertDialog.Builder alert = new AlertDialog.Builder(TodaysOrderActivity.this);
                alert.setTitle("Confirm!");
                alert.setMessage("Are you sure you want to cancel order?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            new SalesOrderRepo().cancelShopOrders(orderDate,appShopId,rlProductSkuId);
                        } catch (Exception e) {
                            Logger.log(Logger.ERROR,"TodaysOrder",e.getMessage(),e);
                        }
                        // refresh orders
                        showOrders(orderDate);
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.create();
                alert.show();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object selectedDate = spnOrderDate.getSelectedItem();
        if (selectedDate == null){
            return;
        }

        Date orderDate = DateFunc.getDate(selectedDate.toString(), "dd/MM/yyyy");
        if (orderDate != null){
            showOrders(orderDate);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
