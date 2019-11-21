package com.salescube.healthcare.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.vAchStatus;
import com.salescube.healthcare.demo.view.vTodayOrders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayShopWiseAchievementActivity extends BaseTransactionActivity{

    private TableLayout tblHead;
    private TableLayout tblData;
    private TableLayout tblTotal;
    private TextView txtTC;
    private TextView txtPC;
    private TextView txtDate;

    double totalValue ;

    private List<vTodayOrders> orders;

    private final static float WGT_SHOP = 3.2f;
    private final static float WGT_VALUE = 1.8f;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 2;
    private final static int RIGHT_PADDING = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_wise_achievement);
        title("Shop Wise Report");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Shop Wise Report");
//            actionBar.setIcon(R.drawable.icon_company);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }

        try {
            initControls();
//            initListners();

            addHeader();
            showOrders(AppControl.getTodayDate());
            addTotal();
        } catch (Exception e) {
            errMsg("While loading todays orders",e);
            finish();
        }

    }



    private void initControls() {
        txtDate = (TextView) findViewById(R.id.shop_wise_txt_date);
        txtTC = (TextView) findViewById(R.id.shop_wise_txt_tc);
        txtPC = (TextView) findViewById(R.id.shop_wise_txt_pc);
        tblHead = (TableLayout) findViewById(R.id.shop_wise_tbl_head);
        tblData = (TableLayout) findViewById(R.id.shop_wise_tbl_data);
        tblTotal = (TableLayout) findViewById(R.id.shop_wise_tbl_total);
    }

    private void showOrders(Date ordrDate) {

        SalesOrderRepo repo = new SalesOrderRepo();
        int[] tcPc = repo.getTCPC(AppControl.getmEmployeeId(), ordrDate);

        txtDate.setText("DATE: " + DateFunc.getDateStrSimple(AppControl.getTodayDate()));
        txtTC.setText("TC: " + String.valueOf(tcPc[0]));
        txtPC.setText("PC: " + String.valueOf(tcPc[1]));

        orders = repo.getShopWiseTodayOrders(AppControl.getmEmployeeId(),AppControl.getTodayDate());
//        List<ShopWiseOrder> orders = insertDummyList();
        tblData.removeAllViews();

        List<vTodayOrders> ordersList=new ArrayList<>();
        if (orders != null) {
//
            loadData(orders);
        }
    }

//    private List<vTodayOrders> insertDummyList() {
//        List<vTodayOrders> orders=new ArrayList<>();
//        for(int i=0;i<=15;i++){
//        orders.add(i,new vShopWiseOrder("ABC "+i,(i*10)));
//        }
//        return orders;
//    }

    private void loadData(List<vTodayOrders> orders) {

        TableRow tr;
        TextView tv;
        totalValue=0;
        int dataCellColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        tblData.removeAllViews();

//        for (ShopWiseOrder order : orders) {
        for (int i=0 ; i < orders.size() ; i++) {

            tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(""+(i+1)+". "+orders.get(i).getShopName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            int amnt=(int)Math.round(orders.get(i).getOrderValue());
            tv.setText(String.valueOf(amnt));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(dataCellColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblData.addView(tr);

            totalValue += orders.get(i).getOrderValue();
        }
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
        tv.setText("Shop Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Value");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
        tv.setTextSize(FONT_SIZE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        //tv.setAllCaps(true);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHead.addView(tr);

    }

    private void addTotal() {

        vAchStatus achStatus = new SalesOrderRepo().getAchStatus(AppControl.getmEmployeeId(),AppControl.getTodayDate());

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);


        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);


        if(achStatus!=null) {
            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Total :");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setTextColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.ruppe(achStatus.getTotalAch(), true) + "/-");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.RIGHT);
            tv.setTextColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            tblTotal.addView(tr);

            tr = new TableRow(this);
            tr.setPadding(5, 4, 5, 10);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Received at HO");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(Color.parseColor("#008000"));
            //tv.setAllCaps(true);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.ruppe(achStatus.getPosted(),true) + "/-");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(Color.parseColor("#008000"));
            //tv.setAllCaps(true);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            tblTotal.addView(tr);

            tr = new TableRow(this);
            tr.setPadding(5, 4, 5, 10);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Not Received (Offline)");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(Color.parseColor("#DC143C"));
            //tv.setAllCaps(true);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(Parse.ruppe(achStatus.getNotPosted(), true)+ "/-");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(Color.parseColor("#DC143C"));
            //tv.setAllCaps(true);
            tr.addView(tv);
        }

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblTotal.addView(tr);
    }
    /*public String notReceived(){
       SalesOrderRepo repo=new SalesOrderRepo();
       repo.cancelShopOrders();
        return "aa";
    }*/
}
