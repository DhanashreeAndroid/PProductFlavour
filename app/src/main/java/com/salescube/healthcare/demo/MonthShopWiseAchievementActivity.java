package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.ShopWiseOrder;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthShopWiseAchievementActivity extends BaseTransactionActivity {

    private TableLayout mTblHead;
    private LinearLayout mTblTotal;
    List<ShopWiseOrder> lstShop;

    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;
    private static final float FONT_SIZE = 16;
    private static final float WGT_SHOP_NAME = 2f;
    private static final float WGT_VALUE = 1f;

    private SimpleDateFormat dateFormatter;
    private Date fDate, tDate;
    private SimpleDateFormat cnvrtFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    double totalvalue;

    private TextView mFromDate;
    private TextView mToDate;
    private TextView mTotal;
    private RecyclerView mProductData;
    private Button viewReport;

    private RecyclerView mShopList;
    private MonthShopWiseListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_shop_report);
        title("Shop Wise Achievement");

      /*  ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Shop Wise Achievement");
            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/

        try {
            initControls();
            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {

        lstShop = new ArrayList<>();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cnvrtFormatter = new SimpleDateFormat("yyyy-MM-dd");

        viewReport = findViewById(R.id.month_shop_btn_report);
        mTotal = findViewById(R.id.month_shop_tv_total);
        mFromDate = findViewById(R.id.month_shop_tv_from_date);
        mToDate = findViewById(R.id.month_shop_tv_to_date);
        mTblHead = findViewById(R.id.month_shop_tbl_header);
        mTblTotal = findViewById(R.id.month_shop_tbl_total);

        mShopList = (RecyclerView) findViewById(R.id.month_shop_rcv_lst);
        mShopList.setLayoutManager(new LinearLayoutManager(MonthShopWiseAchievementActivity.this));
    }

    private void initData() {
        mFromDate.setText(DateFunc.getDateStrSimple(getFirstDateOfCurrentMonth()));
        mToDate.setText(DateFunc.getDateStrSimple(CurrentDate()));
        setDateField();
        loadGridHeader();
//        lstShop=insertDummyList();
//        loadShopList(DateFunc.getDate(DateFunc.getDateStr(getFirstDateOfCurrentMonth()),cnvrtFormatter),DateFunc.getDate(DateFunc.getDateStr(CurrentDate()),cnvrtFormatter),lstShop);

        fetchShopList(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(getFirstDateOfCurrentMonth()), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(CurrentDate()), cnvrtFormatter));
    }

    private void initListener() {

//        loadDayAchivements(AppControl.getTodayDate(), AppControl.getTodayDate(), lstDayAchivmnt);
        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                fromDatePickerDialog.show();
            }
        });
        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                toDatePickerDialog.show();
            }
        });
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fDate = DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy");
                tDate = DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy");

//                loadShopList(fDate,tDate,lstShop);
                fetchShopList(AppControl.getmEmployeeId(), DateFunc.getDate(DateFunc.getDateStr(fDate), cnvrtFormatter), DateFunc.getDate(DateFunc.getDateStr(tDate), cnvrtFormatter));

            }
        });

    }

    private List<ShopWiseOrder> insertDummyList() {
        List<ShopWiseOrder> lst = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                lst.add(i, new ShopWiseOrder(DateFunc.getDate(DateFunc.getDateStr("0" + (i + 1) + "-02-2019"), "dd-MM-yyyy"), "Shop Name " + i, ((i + 1) * 10)));
            } else {
                lst.add(i, new ShopWiseOrder(DateFunc.getDate(DateFunc.getDateStr("0" + (i + 1) + "-02-2019"), "dd-MM-yyyy"), "Shop Name " + i, ((i + 1) * 10)));
            }
        }
        return lst;
    }

    private void fetchShopList(int soId, final Date fromDate, final Date toDate) {

        UtilityFunc.showDialog(this, "","");
        lstShop = new ArrayList<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ShopWiseOrder>> query = apiService.getMonthShopWiseAchievementReport(soId, DateFunc.getDateStr(fromDate, "yyyy-MM-dd"), DateFunc.getDateStr(toDate, "yyyy-MM-dd"));

        if (query == null) {
            Log.e("", "");
        } else {
            query.enqueue(new Callback<List<ShopWiseOrder>>() {
                @Override
                public void onResponse(Call<List<ShopWiseOrder>> call, Response<List<ShopWiseOrder>> response) {
                    if (response.isSuccessful()) {
                        List<ShopWiseOrder> reports = response.body();
                        lstShop.addAll(reports);
                        loadShopList(fromDate, toDate, lstShop);
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
                public void onFailure(Call<List<ShopWiseOrder>> call, Throwable t) {
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

    private void loadShopList(Date fDate, Date tDate, List<ShopWiseOrder> lstShop) {
        totalvalue = 0;
        List<ShopWiseOrder> lst2 = new ArrayList<>();
        lst2.clear();
        lst2.addAll(lstShop);
        listAdapter = new MonthShopWiseListAdapter(MonthShopWiseAchievementActivity.this, fDate, tDate, lst2);
        listAdapter.notifyDataSetChanged();
        if (listAdapter.getItemCount() == 0) {
            Toast.makeText(MonthShopWiseAchievementActivity.this, "Record Not Found", Toast.LENGTH_SHORT).show();
        }

        mShopList.setAdapter(listAdapter);
        calculateTotal(lstShop);
    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private Date CurrentDate() {
        return DateFunc.getDate();
    }

    private void setDateField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fDate = DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()), "dd-MM-yyyy");
                if (fDate.before(DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy")) || fDate.equals(DateFunc.getDate((mToDate.getText().toString()), "dd-MM-yyyy"))) {
//                    /if(mToDate.getError().equals(true)){
                    mFromDate.setError(null);
                    mFromDate.clearFocus();
//                    }
                    mFromDate.setText(DateFunc.getDateStrSimple(fDate));
                } else {
                    mFromDate.setError("Please select before To Date");
                    Toast.makeText(MonthShopWiseAchievementActivity.this, "Please select before To Date", Toast.LENGTH_SHORT).show();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tDate = DateFunc.getDate(DateFunc.getDateStrSimple(newDate.getTime()), "dd-MM-yyyy");
                if (tDate.after(DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy")) || tDate.equals(DateFunc.getDate((mFromDate.getText().toString()), "dd-MM-yyyy"))) {
//                    /if(mToDate.getError().equals(true)){
                    mToDate.setError(null);
                    mToDate.clearFocus();
//                    }
                    mToDate.setText(DateFunc.getDateStrSimple(tDate));
                } else {
                    mToDate.setError("Please select after From Date");
                    Toast.makeText(MonthShopWiseAchievementActivity.this, "Please select after From Date", Toast.LENGTH_SHORT).show();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void loadGridHeader() {
        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        if (mTblHead.getChildCount() == 0) {
            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);
//

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText("Shop Name");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SHOP_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tv = new TextView(this);
            tv.setPadding(RIGHT_PADDING, 0, LEFT_PADDING, 0);
            tv.setText("Value");
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_VALUE));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblHead.addView(tr);
        }
    }

    private class MonthShopWiseListAdapter extends RecyclerView.Adapter<MonthShopWiseListAdapter.ViewHolder> {

        Context context;
        Date fromDate;
        Date toDate;
        List<ShopWiseOrder> lst2;

        public MonthShopWiseListAdapter(Context context, Date fromDate, Date toDate, List<ShopWiseOrder> lst2) {
            this.context = context;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.lst2 = lst2;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_month_shop, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShopWiseOrder order = lst2.get(position);
            loadShop(holder.shopName, order);
        }

        private void loadShop(LinearLayout holder, ShopWiseOrder order) {
            holder.removeAllViews();

            TextView tv;
            LinearLayout.LayoutParams ll_shop = new LinearLayout.LayoutParams(0, -1, WGT_SHOP_NAME);
            ll_shop.setMargins(4, 1, 0, 1);
            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(order.getShopName());
            tv.setLayoutParams(ll_shop);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            holder.addView(tv);

            LinearLayout.LayoutParams ll_value = new LinearLayout.LayoutParams(0, -1, WGT_VALUE);
            ll_value.setMargins(4, 1, 0, 1);
            tv = new TextView(context);
            tv.setPadding(LEFT_PADDING, 0, LEFT_PADDING, 0);
            int amnt = (int) Math.round(order.getTotalAmount());
            tv.setText("₹ " + Parse.toStr(amnt));
            tv.setLayoutParams(ll_value);
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.RIGHT);
            holder.addView(tv);

            holder.setBackgroundResource(R.drawable.table_row_header);

        }

        @Override
        public int getItemCount() {
            return lst2.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout shopName;

            public ViewHolder(View itemView) {
                super(itemView);
                shopName = itemView.findViewById(R.id.ll_item_month_shop);
            }
        }
    }

    private void calculateTotal(List<ShopWiseOrder> lstShop) {

        if (lstShop != null && lstShop.size() > 0) {
            for (int i = 0; i < lstShop.size(); i++) {
                totalvalue += ((int) Math.round(lstShop.get(i).getTotalAmount()));
            }
        }

        mTblTotal.setVisibility(View.VISIBLE);
        int tot = (int) Math.round(totalvalue);
        mTotal.setText("₹ " + String.valueOf(tot));


    }
}
