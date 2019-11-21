package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.ShopStock;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vProduct;
import com.salescube.healthcare.demo.view.vProductSKU;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class StockEntryActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public final static float WGT_PRODUCT = 3f;
    public final static float WGT_QTY = 1;
    private final static int FONT_SIZE = 18;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private String mShopId;

    private TableLayout tblHead;
    private TableLayout tblData;
    private TextView txtDate;

    private Button btnAdd;
    private Button btnSubmit;
    private ScrollView scrMain;

    private String mShopAppId;
    private boolean editable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_entry);
        title("Stock Entry");


        Intent intent = getIntent();
        mShopId = intent.getExtras().getString(Constant.SHOP_ID);
        mShopAppId = intent.getStringExtra(Constant.SHOP_APP_ID);

        try {
            initControls();
            initListener();

            initData();
            loadData();

            // loadData(mShopId);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {

        txtDate = findViewById(R.id.stock_entry_date);
        tblHead = findViewById(R.id.stock_entry_tbl_head);
        tblData = findViewById(R.id.stock_entry_tbl_data);

        btnSubmit = findViewById(R.id.stock_entry_btn_submit);

        scrMain = findViewById(R.id.stock_entry_scroll);
    }

    private void initData() {


//        stock.setShopId(Integer.parseInt(mShopId));

        // SpinnerHelper.FillStockMonthYear(spnYearMonth);
        //ProductRepo repProduct = new ProductRepo();

//        lstProduct = repProduct.getProductsAll(AppControl.getmEmployeeId());
//        lstProductSku = repProduct.getProductSkuAll(AppControl.getmEmployeeId(), 0, mShopAppId, "");
//
//        lstProduct.add(0, new vProduct(0, "--SELECT PRODUCT--"));
//
//        adpProduct = new ArrayAdapter<>(StockEntryActivity.this, R.layout.small_spinner_item, lstProduct);
//        adpProduct.setDropDownViewResource(R.layout.small_spinner_dropdown_item);


        assignDateTextBox(txtDate, false, false, true);


        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog followupDateDlg = new DatePickerDialog(StockEntryActivity.this, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                txtDate.setText(Constant.DATE_FORMAT_2.format(newDate.getTime()));

                loadData();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        Date lockDate = DateFunc.getTodayMin();
        String dt = DateFunc.getDateTimeStr(lockDate);

        followupDateDlg.getDatePicker().setMaxDate(lockDate.getTime());

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followupDateDlg.show();
            }
        });


        txtDate.setText(DateFunc.getDateStr("dd/MM/yyyy"));

        loadGridHeader();
        // loadSavedStock();
    }

    private void initListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!editable) {
                    Alert("Not allowed","Backdated stock modification not allowed!");
                    return;
                }

                try {
                    submitData();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

    }

    private void loadGridHeader() {

        int row1BackColor = ContextCompat.getColor(StockEntryActivity.this, R.color.holo_blue_dark);

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        int textColor = ContextCompat.getColor(this, R.color.colorWhite);

        tr = new TableRow(this);
        tr.setPadding(0, 5, 5, 5);

        tv = new TextView(this);
        tv.setText("PRODUCT");
        tv.setPadding(20, 5, 10, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
        tv.setTextSize(FONT_SIZE);
        tv.setMinLines(2);
        tv.setTypeface(tf);
        tv.setGravity(Gravity.CENTER | Gravity.LEFT);
        tv.setTextColor(textColor);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tr.addView(tv);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, -1, WGT_QTY);
        //params.setMargins(5,0,0,0);

        tv = new TextView(this);
        tv.setText("QTY");
        tv.setPadding(18, 5, 10, 5);
        tv.setLayoutParams(params);
        tv.setTextSize(FONT_SIZE);
        tv.setMinLines(2);
        tv.setTypeface(tf);
        tv.setGravity(Gravity.CENTER | Gravity.CENTER);
        tv.setTextColor(textColor);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tr.addView(tv);


//        tr.setBackgroundResource(R.drawable.table_cell_header);
        tblHead.addView(tr);


    }

    private void loadData() {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        int empId = AppControl.getmEmployeeId();

        String dateStr = DateFunc.getDateStr(DateFunc.getDate(txtDate.getText().toString(), "dd/MM/yyyy"), "yyyy-MM-dd");

        Call<ShopStock[]> query = apiService.getShopStock(empId, mShopAppId, dateStr);

        query.enqueue(new Callback<ShopStock[]>() {
            @Override
            public void onResponse(Call<ShopStock[]> call, retrofit2.Response<ShopStock[]> response) {


                if (response.isSuccessful()) {

                    // save credentials
                    // show home screen

                    try {
                        ShopStock[] objUser = response.body();
                        if (objUser.length == 0) {
                            Alert("No data!", "No previous orders products found!");
                        }else {
                            loadData(objUser);
                        }
                    } catch (Exception e) {
                        UtilityFunc.dismissDialog();
                        //togger.e(e);
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }


                } else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                        message = e.getMessage();
                        // togger.e(e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }
                    Alert("Error!", message);

                }

                UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<ShopStock[]> call, Throwable t) {

                String message;

                if (t instanceof SocketTimeoutException) {
                    message = getString(R.string.connection_timeout);
                } else if (t instanceof ConnectException) {
                    message = getString(R.string.no_connection);
                } else {
                    message = getString(R.string.unknown_error);
                }

                UtilityFunc.dismissDialog();

                // Alert("Error!", message);

                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(StockEntryActivity.this)
                        .setTitle("Error!")
                        .setMessage(message);

                alert.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData();
                    }
                });

                alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         finish();
                    }
                });

                alert.create();
                alert.show();

            }
        });
    }

    protected void Alert(String title, String msg) {

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(StockEntryActivity.this)
                .setTitle(title)
                .setMessage(msg);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        alert.create();
        alert.show();
    }

    private void loadData(ShopStock[] data) {

        int row1BackColor = ContextCompat.getColor(StockEntryActivity.this, R.color.holo_orange_light);

        tblData.removeAllViews();

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        EditText ed;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        ArrayAdapter<vProductSKU> adpSkuTemp = new ArrayAdapter<vProductSKU>(StockEntryActivity.this, R.layout.small_spinner_item, new ArrayList<vProductSKU>());
        adpSkuTemp.setDropDownViewResource(R.layout.small_spinner_dropdown_item);

        TextView spinner;

        for (ShopStock stock : data) {

            tr = new TableRow(this);
            tr.setPadding(0, 1, 5, 1);

            spinner = new TextView(this);
            spinner.setPadding(20, 5, 0, 5);
            spinner.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRODUCT));
            spinner.setGravity(Gravity.CENTER | Gravity.LEFT);
            spinner.setText(stock.getProductName());
            spinner.setTextSize(FONT_SIZE);
            spinner.setTextColor(headingColor);
            spinner.setTag(stock.getRlProductSkuId());
            spinner.setBackgroundResource(R.drawable.table_cell_main);
            tr.addView(spinner);


            ed = new EditText(this);
            ed.setPadding(10, 0, 20, 0);
            if (stock.getQty() != 0) {
                ed.setText(String.valueOf(stock.getQty()));
            }
            ed.setHint("0");
            ed.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_QTY));
            ed.setTextSize(FONT_SIZE + 10);
            ed.setBackgroundResource(R.drawable.table_cell_edit);
            ed.setGravity(Gravity.CENTER | Gravity.RIGHT);
            ed.setTypeface(tf);
            //ed.getLayoutParams().height = 60;
            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
            ed.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            ed.setTextColor(headingColor);
            ed.setBackgroundColor(Color.WHITE);
            ed.setHintTextColor(Color.GRAY);

            if (!stock.isEditable()) {
                editable = false;
                disableEditText(ed);
            }

            tr.addView(ed);

            //tr.setBackgroundColor(row1BackColor);
//        tr.setBackgroundResource(R.drawable.table_row_header);
            tblData.addView(tr);
        }


        // set focus on last row added
//        scrMain.post(new Runnable() {
//            @Override
//            public void run() {
//                scrMain.fullScroll(View.FOCUS_DOWN);
//            }
//        });

    }


    private void disableSpinner(final Spinner spinner) {

        spinner.post(new Runnable() {
            @Override
            public void run() {
                ((Spinner) spinner).getSelectedView().setEnabled(false);
                spinner.setEnabled(false);

                View view = ((Spinner) spinner).getSelectedView();
                ((TextView) view).setTextColor(Color.BLACK);
            }
        });
    }

    private void submitData() {

        int rows = tblData.getChildCount();
        TableRow tr;

        TextView spnRlProductSku;
        EditText edtQty;

        int rlProductSkuId;
        int qty;

        Date dateStr = DateFunc.getDate(txtDate.getText().toString(), "dd/MM/yyyy");
        int soId = AppControl.getmEmployeeId();

        ShopStock att;
        List<ShopStock> stockList = new ArrayList<>();

        for (int i = 0; i < rows; i++) {

            tr = (TableRow) tblData.getChildAt(i);

            spnRlProductSku = (TextView) tr.getChildAt(0);
            edtQty = (EditText) tr.getChildAt(1);

            rlProductSkuId = Parse.toInt(spnRlProductSku.getTag());
            qty = Parse.toInt(edtQty.getText().toString());

            att = new ShopStock();
            att.setSoId(soId);
            att.setEntryDate(dateStr);
            att.setRlProductSkuId(rlProductSkuId);
            att.setAppShopId(mShopAppId);
            att.setQty(qty);
            stockList.add(att);
        }


        postData(stockList);
    }

    private void postData(List<ShopStock> data) {

        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> query = apiService.postShopStock(data);

        query.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {


                if (response.isSuccessful()) {

                    Alert("Done", "Stock update successful");

                } else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                        message = e.getMessage();
                        // togger.e(e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }
                    Alert("Error!", message);

                }
                UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                String message;

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


    private int getProductIndex(Spinner spinner, int productId) {

        int index = 0;
        vProduct product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vProduct) spinner.getItemAtPosition(i);
            if (product.getProductId() == productId) {
                index = i;
                break;

            }
        }

        return index;
    }

    private int getProductSKUIndex(Spinner spinner, int rlProductSkuId) {

        int index = 0;
        vProductSKU product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vProductSKU) spinner.getItemAtPosition(i);
            if (product.getRlProductSkuId() == rlProductSkuId) {
                index = i;
                break;

            }
        }
        return index;
    }

    @Override
    public void onClick(View v) {

    }

    private ScrollMode mScrollMode;

    private enum ScrollMode {
        AgentChange,
        AddProduct
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
