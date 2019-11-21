package com.salescube.healthcare.demo;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.salescube.healthcare.demo.ctrl.TSAdapter;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.PreferenceUtils;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SearchableSpinner;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vProduct;
import com.salescube.healthcare.demo.view.vProductSKU;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_ORDER;

public class SalesOrderEntryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, SearchableSpinner.SearchableSpinnerTouchListener {


    //region Private Variables

    private TableLayout tblMain;
    private TableLayout tblHeader;

    private List<vProduct> lstProduct;
    private List<vProductSKU> lstProductSku;

    private ArrayAdapter<vProduct> adpProduct;
    private int rowUniqueId;

    private Button btnAddNewPrd;
    private Button btnSumbit;
    private ScrollView srlOrderScroll;

    private Date mOrderDate;
    private String mShopAppId;
    private int mAreaId;
    private int mAgentId;
    private Drawable textEditDrawble;
    private TextView tvOrderTotal;

    private vAgent _agent;
    private boolean isDirty = false;

    //endregion


    final float PRODUCT_WGT = 3f;
    final float SKU_WGT = 3f;
    final float QTY_WGT = 1.5f;
    final float DIS_RATE_WGT = 1.5f;
    final float DIS_AMT_WGT = 1.5f;
    final float ADD_DIS_WGT = 1.5f;
    final float TOTAL_AMT_WGT = 2f;
    final float DEL_BTN_WGT = 1f;

    final int LEFT_PADDING = 8;
    final int RIGHT_PADDING = 8;

    private static boolean hasInited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_sales_order_entry);

        this.setTitle("Order Entry");

        try {
            initExtra();
            initControls();
            initListners();
        } catch (Exception e) {
            errMsg("While loading Order screen", e);
            finish();
        }

        hasInited = true;

    }

    private void initExtra() {

        Intent intent = getIntent();

        String strData = intent.getStringExtra(Constant.ORDER_DATE);
        mOrderDate = DateFunc.getDate(strData, "dd/MM/yyyy");

        mShopAppId = intent.getStringExtra(Constant.SHOP_APP_ID);
        mAreaId = intent.getIntExtra(Constant.AREA_ID, 0);
        mAgentId = intent.getIntExtra(Constant.AGENT_ID, 0);

        setAgent(mAgentId);

        if (mAreaId == 0) {
            msgShort("Invalid Area");
            finish();
        }

    }

    private void initControls() {

        tvOrderTotal = findViewById(R.id.orderEntry_tv_total);

        tblMain = (TableLayout) findViewById(R.id.newOrder_tbl_main);
        tblHeader = (TableLayout) findViewById(R.id.orderEntry_tbl_header);

        btnAddNewPrd = (Button) findViewById(R.id.orderEntry_btn_addProduct);
        btnSumbit = (Button) findViewById(R.id.orderEntry_btn_submit);

        srlOrderScroll = (ScrollView) findViewById(R.id.orderEntry_srl_orderScroll);

    }

    private void initListners() {

        btnAddNewPrd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getCurrentFocus();

                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                insertProduct(false, 0, 0);
            }
        });

        btnSumbit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {

                    saveData();


//                    if (mAgentId != 0) {
//                        setAgent(mAgentId);
//                    } else {
//                        setAgent(0);
//                    }
                }
            }
        });

    }

    private void setAgent(final int mAgentId) {

        final Dialog dialog = new Dialog(SalesOrderEntryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.agent_list);
        dialog.setCanceledOnTouchOutside(false);

        final Spinner spnAgent = dialog.findViewById(R.id.spn_agent);

        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        AgentRepo agentRepo = new AgentRepo();
        List<vAgent> agentList = agentRepo.getAgentByArea(AppControl.getmEmployeeId(), mAreaId);
        agentList.add(0, new vAgent(0, "-----Select Agent-----"));

        TSAdapter adpReason = new TSAdapter(dialog.getContext(), android.R.layout.simple_spinner_item, agentList);
        adpReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAgent.setAdapter(adpReason);

        int index = 1;

        if (agentList.size() == 2) {
            spnAgent.setSelection(index);
        } else {
            index = getAgentIndex(spnAgent, mAgentId);
            spnAgent.setSelection(index);
        }

        dialog.show();

        spnAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(mAgentId != 0){
//                    int pos = (int) parent.getItemAtPosition(mAgentId);
//                    spnReason.setSelection(mAgentId);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                vAgent agent = (vAgent) spnAgent.getSelectedItem();

                if (agent.getAgentId() == 0) {
                    Alert("Required!", "Please select agent.");
                    return;
                }

                _agent = agent;

                loadData();

                insertHeader();
                loadOrders();

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void saveData() {

        if (!validate()) {
            return;
        }

        if (_agent.getAgentId() == 0) {
            Alert("Invalid Agent", "Please select Agent!");
            return;
        }

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, SalesOrderEntryActivity.this);
        if (!enableGPS) {
            return;
        }

        try {
            submitOrders(_agent.getAgentId());
        } catch (Exception ex) {
            Alert("Order Entry!", ex.getMessage());
        }
    }

    private void loadData() {

        ProductRepo repProduct = new ProductRepo();

        String priceCode = _agent.getPriceCode();

        lstProduct = repProduct.getProductsAll(AppControl.getmEmployeeId());
        lstProductSku = repProduct.getProductSkuAll(AppControl.getmEmployeeId(), 0, mShopAppId, priceCode);

        lstProduct.add(0, new vProduct(0, "--Select Product--"));

//        adpProduct = new ArrayAdapter<vProduct>(SalesOrderEntryActivity.this, R.layout.small_spinner_item, lstProduct);
//        adpProduct.setDropDownViewResource(R.layout.small_spinner_dropdown_item);

        adpProduct = new ArrayAdapter<vProduct>(SalesOrderEntryActivity.this, R.layout.multiline_spinner_row, lstProduct);
        adpProduct.setDropDownViewResource(R.layout.small_spinner_dropdown_item);


    }


    private void insertHeader() {

        TableRow tr;
        TextView tv;

        int TEXT_SIZE = 14;
        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        int textColor = Color.WHITE;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        boolean textCaps = true;

        // tr.setBackgroundResource(R.drawable.table_row_header);

        // Product

        tv = new TextView(this);
        tv.setText("Product");
        tv.setPadding(18, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, PRODUCT_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        // Product SKU

        tv = new TextView(this);
        tv.setText("Product SKU");
        tv.setPadding(18, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, SKU_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        // Order Quantity

        tv = new TextView(this);
        tv.setText("Order\nQty");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, QTY_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        // Free Quantity

        tv = new TextView(this);
        tv.setText("Free\nQty");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, QTY_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setVisibility(View.GONE);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setText("Dis.\nRate");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, DIS_RATE_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setText("Dis.\nAmt");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, DIS_AMT_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setText("Add.\nDis");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, ADD_DIS_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setTextColor(textColor);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setAllCaps(textCaps);
        tr.addView(tv);


        tv = new TextView(this);
        tv.setText("Total\nAmount");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, TOTAL_AMT_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setTextColor(textColor);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setAllCaps(textCaps);
        tr.addView(tv);


        // Scheme Add

//        tv = new TextView(this);
//        tv.setText("Add \nSch.");
//        tv.setPadding(10, 5, 10, 5);
//        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.1f));
//        tv.setTextSize(14);
//        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
//        tv.setBackgroundResource(R.drawable.table_cell_header);
//        tv.setGravity(Gravity.CENTER);
//        tr.addView(tv);

        // Product Remove

        tv = new TextView(this);
        tv.setText("Del");
        tv.setPadding(LEFT_PADDING, 5, RIGHT_PADDING, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, DEL_BTN_WGT));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tf);
        tv.setTextColor(textColor);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setAllCaps(textCaps);
        tr.addView(tv);

        tblHeader.addView(tr);

    }

    private void loadOrders() {

        SalesOrderRepo orderRepo = new SalesOrderRepo();
        List<SalesOrder> orders = orderRepo.getShopOrders(AppControl.getmEmployeeId(), mShopAppId, mOrderDate);

        if (orders.size() == 0) {
            insertProduct(false, 0, 0);
            return;
        }

        TableRow tr;
        TableRow childRow;

        Spinner spnProductId;
       Spinner spnRlProduct;
        EditText edtOrderQty;
        EditText edtFreeQty;

        EditText ediDiscountRate;
        EditText edtDiscount;
        EditText edtAddDiscount;
        TextView txtTotalAmount;


        int index;
        int productId;
        double totalAmount = 0;

        for (SalesOrder order : orders) {
            if (order.getIsScheme()) {
                continue;
            }

            tr = insertProduct(false, 0, 0);

            spnProductId = (Spinner) tr.getChildAt(0);
            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);
            edtFreeQty = (EditText) tr.getChildAt(3);
            ediDiscountRate = (EditText) tr.getChildAt(4);
            edtDiscount = (EditText) tr.getChildAt(5);
            edtAddDiscount = (EditText) tr.getChildAt(6);
            txtTotalAmount = (TextView) tr.getChildAt(7);


            productId = getProductId(order.getRlProductSkuId());
            index = getProductIndex(spnProductId, productId);
            spnProductId.setSelection(index);

            setSkuAdapter(spnRlProduct, productId);

            index = getProductSKUIndex(spnRlProduct, order.getRlProductSkuId());
            spnRlProduct.setSelection(index);

            edtOrderQty.setText(String.valueOf (order.getOrderQty()));
            edtFreeQty.setText(String.valueOf(order.getFreeQty()));

            if (order.getDiscountRate() != 0) {
                ediDiscountRate.setText(Parse.toStr(order.getDiscountRate()));
            }

            if (order.getDiscount() != 0) {
                edtDiscount.setText(Parse.toStr(order.getDiscount()));
            }

            if (order.getAdditionalDiscount() != 0) {
                edtAddDiscount.setText(Parse.toStr(order.getAdditionalDiscount()));
            }

            txtTotalAmount.setText(Parse.toStr(order.getTotalAmount()));

            totalAmount += order.getTotalAmount();

            for (SalesOrder scheme : orders) {
                if (order.getRlProductSkuId() == scheme.getRlProductSkuId()) {
                    if (scheme.getScheme_rlProductSkuId() == 0) {
                        continue;
                    }

                    childRow = insertScheme(tr);

                    spnProductId = (SearchableSpinner) childRow.getChildAt(0);
                    spnRlProduct = (SearchableSpinner) childRow.getChildAt(1);
                    edtFreeQty = (EditText) childRow.getChildAt(3);

                    productId = getProductId(scheme.getScheme_rlProductSkuId());
                    index = getProductIndex(spnProductId, productId);
                    spnProductId.setSelection(index);

                    setSkuAdapter(spnRlProduct, productId);

                    index = getProductSKUIndex(spnRlProduct, scheme.getScheme_rlProductSkuId());
                    spnRlProduct.setSelection(index);

                    edtFreeQty.setText(Integer.toString(scheme.getScheme_qty()));

                }
            }

        }
        tvOrderTotal.setText("Total: " + decimalStr(totalAmount) + "/-");
    }

    private void setSkuAdapter(Spinner spnSKU, final int productId) {

        ArrayAdapter<vProductSKU> adpTemp = (ArrayAdapter<vProductSKU>) spnSKU.getAdapter();

        List<vProductSKU> tmpRoutes = Lists.newArrayList(Collections2.filter(lstProductSku, new Predicate<vProductSKU>() {
            @Override
            public boolean apply(vProductSKU input) {
                return input.getProductId() == productId;
            }
        }));

        tmpRoutes.add(0, new vProductSKU(0, "-- Select Sku --"));

        adpTemp.clear();
        adpTemp.addAll(tmpRoutes);
        adpTemp.notifyDataSetChanged();
    }

    private int getProductId(int rlProductId) {

        int productId = 0;
        for (vProductSKU sku : lstProductSku) {
            if (sku.getRlProductSkuId() == rlProductId) {
                return sku.getProductId();
            }
        }
        return productId;
    }

    private int getAgentIndex(Spinner spinner, int agentId) {

        int index = 0;
        vAgent agent;

        for (int i = 0; i < spinner.getCount(); i++) {
            agent = (vAgent) spinner.getItemAtPosition(i);
            if (agent.getAgentId() == agentId) {
                index = i;
                break;

            }
        }

        return index;
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

    private TableRow insertProduct(boolean isSchemeProduct, int index, int parentId) {

        TableRow tr;
       Spinner spn;

        Button btn;
        EditText editTxt;

        int bgResorce;
        if (!isSchemeProduct) {
            bgResorce = R.drawable.table_cell_main;
        } else {
            bgResorce = R.drawable.table_cell_child;
        }

        ArrayAdapter<vProductSKU> adpSkuTemp = new ArrayAdapter<vProductSKU>(SalesOrderEntryActivity.this, R.layout.multiline_spinner_row, new ArrayList<vProductSKU>());
        adpSkuTemp.setDropDownViewResource(R.layout.small_spinner_dropdown_item);

        rowUniqueId += 1;

        RowData cr = new RowData();
        cr.setOwnId(rowUniqueId);
        cr.setParentId(parentId);

        tr = new TableRow(this);
        //tr.setBackgroundResource(R.drawable.table_row_bg);
        tr.setPadding(5, 0, 5, 2);
        tr.setTag(cr);

        // Product

        spn = new Spinner(this);
        spn.setPadding(10, 0, 10, 0);
        spn.setLayoutParams(new TableRow.LayoutParams(0, -1, PRODUCT_WGT));
        spn.setAdapter(adpProduct);
        spn.setOnItemSelectedListener(this);
        spn.setTag("Product");
        spn.setBackgroundResource(bgResorce);
     /*   spn.addOnTouchListener(this);*/
        tr.addView(spn);

        // Product SKU

        spn = new Spinner(this);
        spn.setPadding(10, 0, 10, 0);
        spn.setLayoutParams(new TableRow.LayoutParams(0, -1, SKU_WGT));
        spn.setAdapter(adpSkuTemp);
        spn.setOnItemSelectedListener(this);
        spn.setTag("SKU");
        spn.setBackgroundResource(bgResorce);
       /* spn.addOnTouchListener(this);*/
        tr.addView(spn);

        // Order Quantity

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, QTY_WGT));
        editTxt.setTextSize(14);
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTxt.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
        editTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        if (isSchemeProduct) {
            editTxt.setVisibility(View.INVISIBLE);
        }
        editTxt.addTextChangedListener(setTextChangeListner(editTxt));
        tr.addView(editTxt);

        // Free Quantity

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, QTY_WGT));
        editTxt.setTextSize(14);
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTxt.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        editTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
        editTxt.setVisibility(View.GONE);
        tr.addView(editTxt);

//        Similar to Kpra
        // Discount Rate

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, DIS_RATE_WGT));
        editTxt.setTextSize(14);
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTxt.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        editTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
        editTxt.addTextChangedListener(setTextChangeListner(editTxt));
        editTxt.setTag("DIS_RATE");
//        editTxt.setVisibility(View.GONE);
        tr.addView(editTxt);

        // Discount

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, DIS_AMT_WGT));
        editTxt.setTextSize(14);
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTxt.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        editTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
        editTxt.addTextChangedListener(setTextChangeListner(editTxt));
        editTxt.setTag("DIS_AMT");
        if (isSchemeProduct) {
            editTxt.setVisibility(View.INVISIBLE);
        }
//        editTxt.setVisibility(View.GONE);
        tr.addView(editTxt);


        // Additional Discount

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, ADD_DIS_WGT));
        editTxt.setTextSize(14);
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTxt.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        editTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
//        editTxt.setVisibility(View.GONE);
        editTxt.addTextChangedListener(setTextChangeListner(editTxt));
        tr.addView(editTxt);

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, TOTAL_AMT_WGT));
        editTxt.setTextSize(14);
        editTxt.setBackgroundResource(R.drawable.table_cell_edit);
        editTxt.setEnabled(false);
        editTxt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        editTxt.setTextColor(Color.BLACK);
//        editTxt.setVisibility(View.GONE);
        tr.addView(editTxt);

//        tv = new TextView(this);
//        tv.setText("");
//        tv.setPadding(12, 0, 10, 0);
//        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
//        tv.setTextSize(14);
//        tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
//        tv.setBackgroundResource(R.drawable.table_cell_edit);
//        tv.setTextColor(Color.BLACK);
//        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        tr.addView(tv);


        // Scheme Add

//        btn = new Button(this);
//        btn.setText("+");
//        btn.setPadding(10, 0, 10, 0);
//        btn.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.1f));
//        btn.setOnClickListener(this);
//        btn.setTextSize(14);
//        btn.setTag("ADD");
//        btn.getLayoutParams().height = 80;
//        if (isSchemeProduct) {
//            btn.setVisibility(View.INVISIBLE);
//        }
//        btn.setBackgroundResource(bgResorce);
//        tr.addView(btn);

        // Product Remove

        btn = new Button(this);
        btn.setText("X");
        btn.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        btn.setLayoutParams(new TableRow.LayoutParams(0, -1, DEL_BTN_WGT));
        btn.setOnClickListener(this);
        btn.setTextSize(14);
        btn.setTag("REMOVE");
        //btn.getLayoutParams().height = 80;
        btn.setTextColor(Color.RED);
        btn.setBackgroundResource(bgResorce);
        tr.addView(btn);

        if (isSchemeProduct) {
            tblMain.addView(tr, index + 1);
        } else {
            tblMain.addView(tr);
        }

        // set focus on last row added
        srlOrderScroll.post(new Runnable() {
            @Override
            public void run() {
                srlOrderScroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        return tr;
    }

    private TableRow insertScheme(TableRow mainRow) {

        int ix = tblMain.indexOfChild(mainRow);
        int rowCount = tblMain.getChildCount();

        RowData cr = (RowData) mainRow.getTag();
        int mainId = cr.getOwnId();

        TableRow tmpRow;
        for (int i = 0; i < rowCount; i++) {

            tmpRow = (TableRow) tblMain.getChildAt(i);
            cr = (RowData) tmpRow.getTag();

            if (cr.getParentId() == mainId) {
                if (tblMain.indexOfChild(tmpRow) > ix) {
                    ix = tblMain.indexOfChild(tmpRow);
                }
            }
        }

        return insertProduct(true, ix, mainId);
    }

    private void removeScheme(View view) {

        TableRow mainRow = (TableRow) view.getParent();
        TableRow childRow;

        RowData cr = (RowData) mainRow.getTag();

        if (cr.getParentId() == 0) {

            int ownId = cr.getOwnId();
            int rowCount = tblMain.getChildCount();

            for (int i = rowCount - 1; i >= 0; i--) {

                if (true) {
                    childRow = (TableRow) tblMain.getChildAt(i);
                    cr = (RowData) childRow.getTag();

                    if (ownId == cr.getParentId()) {
                        tblMain.removeView(childRow);
                    }
                }
            }
        }
        tblMain.removeView(mainRow);
        calculateTotal();
    }

    private void submitOrders(int agentId) {

        TableRow tr;
        RowData rd;

        int rowCount = tblMain.getChildCount();

        SalesOrderRepo orderRepo;
        SalesOrder objOrder;
        List<SalesOrder> objOrders;

        int rlProductSkuId;
        int orderQty;
        int freeQty;
        double rate;
        double disRate;
        double dis;
        double addDis;
        double totalAmount;
        vProductSKU sku;
        vProduct prod;

        List<RowData> lstRows = new ArrayList<RowData>();

        Spinner spnProduct;
        Spinner spnRlProduct;
        EditText edtOrderQty;
        EditText edtFreeQty;

        EditText edtDiscountRate;
        EditText edtDiscount;
        EditText edtAddDiscount;
        TextView txtTotalAmount;

        // Complex to Simplex
        for (int i = 0; i < rowCount; i++) {

            tr = (TableRow) tblMain.getChildAt(i);
            rd = (RowData) tr.getTag();

            spnProduct = (Spinner) tr.getChildAt(0);
            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);
            edtFreeQty = (EditText) tr.getChildAt(3);
            edtDiscountRate = (EditText) tr.getChildAt(4);
            edtDiscount = (EditText) tr.getChildAt(5);
            edtAddDiscount = (EditText) tr.getChildAt(6);
            txtTotalAmount = (TextView) tr.getChildAt(7);

            prod = (vProduct) spnProduct.getSelectedItem();
            sku = (vProductSKU) spnRlProduct.getSelectedItem();

            rlProductSkuId = sku.getRlProductSkuId();
//            orderQty = getInt(edtOrderQty.getText().toString());
            orderQty = Parse.toInt(edtOrderQty.getText().toString());
            disRate = Parse.toDbl(edtDiscountRate.getText().toString());
            dis = Parse.toDbl(edtDiscount.getText().toString());
            addDis = Parse.toDbl(edtAddDiscount.getText().toString());
            totalAmount = Parse.toDbl(txtTotalAmount.getText().toString());

            freeQty = getInt(edtFreeQty.getText().toString());
            rate = sku.getRate();

            if (rlProductSkuId == 0) {
                continue;
            }

            if (rd.isChildRow()) {
                if (freeQty == 0) {
                    continue;
                }
            } else {
                if (orderQty == 0) {
                    continue;
                }
            }

            rd.setRlProductSkuId(rlProductSkuId);
            rd.setOrderQty(orderQty);
            rd.setFreeQty(freeQty);
            rd.setRate(rate);

            rd.setDiscountRate(disRate);
            rd.setDiscount(dis);
            rd.setAddDiscount(addDis);
            rd.setTotalAmount(totalAmount);

            if (totalAmount == 0) {
                Alert("Rate not found!", "Pricelist not found for " + prod.getProductName() + " " +  sku.getProductSku() +".");
                return;
            }
            lstRows.add(rd);
        }


        boolean hasSchemes;
        objOrders = new ArrayList<SalesOrder>();
        Date dateNow = new Date();

        DateFormat df = new SimpleDateFormat("HHmmss");
        String setNo = df.format(dateNow);

        for (RowData product : lstRows) {

            if (product.isChildRow()) {
                continue;
            }

            // Scheme Products

            hasSchemes = false;
            for (RowData scheme : lstRows) {
                if (product.getOwnId() == scheme.getParentId()) {

                    objOrder = new SalesOrder();
                    objOrder.setSoId(AppControl.getmEmployeeId());
                    objOrder.setOrderDate(mOrderDate);
                    objOrder.setSetNo(setNo);
                    objOrder.setAppShopId(mShopAppId);
                    objOrder.setRlProductSkuId(product.getRlProductSkuId());
                    objOrder.setOrderQty(product.getOrderQty());
                    objOrder.setRate(product.getRate());
                    objOrder.setFreeQty(product.getFreeQty());

                    objOrder.setDiscountRate(product.getDiscountRate());
                    objOrder.setDiscount(product.getDiscount());
                    objOrder.setAdditionalDiscount(product.getAddDiscount());
                    objOrder.setTotalAmount(product.getTotalAmount());

                    objOrder.setScheme_rlProductSkuId(scheme.getRlProductSkuId());
                    objOrder.setScheme_qty((scheme.getFreeQty()));
                    objOrder.setIsScheme(hasSchemes);

                    objOrder.setAgentId(agentId);
                    objOrder.setCreatedDateTime(dateNow);
                    objOrders.add(objOrder);

                    hasSchemes = true;
                }
            }

            if (!hasSchemes) {

                // Main Product
                objOrder = new SalesOrder();
                objOrder.setSoId(AppControl.getmEmployeeId());
                objOrder.setOrderDate(mOrderDate);
                objOrder.setSetNo(setNo);
                objOrder.setAppShopId(mShopAppId);
                objOrder.setRlProductSkuId(product.getRlProductSkuId());
                objOrder.setOrderQty(product.getOrderQty());
                objOrder.setRate(product.getRate());
                objOrder.setFreeQty(product.getFreeQty());

                objOrder.setDiscountRate(product.getDiscountRate());
                objOrder.setDiscount(product.getDiscount());
                objOrder.setAdditionalDiscount(product.getAddDiscount());
                objOrder.setTotalAmount(product.getTotalAmount());

                objOrder.setScheme_rlProductSkuId(0);
                objOrder.setScheme_qty(0);
                objOrder.setIsScheme(false);

                objOrder.setAgentId(agentId);
                objOrder.setCreatedDateTime(dateNow);
                objOrder.setPosted(false);
                objOrders.add(objOrder);

            }
        }

        // Update into database
        orderRepo = new SalesOrderRepo();

        orderRepo.cancelShopOrders(mShopAppId, mOrderDate);
        orderRepo.insert(objOrders);

        PreferenceUtils.putBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, true, this);
//        if(mAgentId != 0){
////            orderRepo.updateOrders(objOrders);
////        }else{
////            orderRepo.insert(objOrders);
////        }

        // Close Activity
        msgShort("Order Successful!");
        doManualUpload(mShopAppId, EVENT_ORDER);

        Intent intent = new Intent();
        intent.putExtra("HasSuccess", true);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onClick(final View view) {

        if (view instanceof Button) {
            String tag = view.getTag().toString();

            if (tag == "ADD") {

                TableRow tr = (TableRow) view.getParent();
                insertScheme(tr);
            }

            if (tag == "REMOVE") {

                TableRow mainRow = (TableRow) view.getParent();
                RowData rowData = (RowData) mainRow.getTag();

                if (!rowData.isChildRow()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(SalesOrderEntryActivity.this);
                    alert.setTitle("Confirm Delete!");
                    alert.setMessage("Do you want to delete this product?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeScheme(view);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();

                } else {
                    removeScheme(view);
                }

            }
        }
    }


    private boolean validate() {

        TableRow tr;
        RowData rd;
        int rowCount = tblMain.getChildCount();

        int rlProductSkuId;
        int orderQty;
        int freeQty;
        double total;

        String productName;

        Spinner spnProduct;
        Spinner spnRlProduct;
        EditText edtOrderQty;
        EditText edtFreeQty;
        EditText edtTotal;

        Hashtable hsKey = new Hashtable();

        EditText r = new EditText(this);
        boolean duplicateFound = false;

        for (int i = 0; i < rowCount; i++) {

            tr = (TableRow) tblMain.getChildAt(i);
            rd = (RowData) tr.getTag();

            spnProduct = (Spinner) tr.getChildAt(0);
            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);
            edtFreeQty = (EditText) tr.getChildAt(3);
            edtTotal = (EditText) tr.getChildAt(7);

            vProductSKU sku = (vProductSKU) spnRlProduct.getSelectedItem();

            productName = spnProduct.getSelectedItem().toString();
            rlProductSkuId = sku.getRlProductSkuId();
            orderQty = getInt(edtOrderQty.getText().toString());
            freeQty = getInt(edtFreeQty.getText().toString());
            total = Parse.toDbl(edtTotal.getText().toString());

            productName = productName + "-" + sku.getProductSku();

            if (orderQty > 0 || freeQty > 0) {
                if (rlProductSkuId == 0) {
                    Alert("Invalid Product", "Please select Product!");
                    return false;
                }
            }

            if (orderQty < 0 || freeQty < 0) {
                Alert("Invalid Qty", "[" + productName + "]\nNegative quantity not allowed!");
                return false;
            }

            if (rlProductSkuId == 0) {
                continue;
            }

            if (rd.isChildRow()) {

                if (freeQty == 0) {
                    Alert("Scheme Qty", "[" + productName + "]\nPlease enter free quantity for scheme.");
                    return false;
                }

            } else {

                if (orderQty == 0) {
                    Alert("Order Qty", "[" + productName + "]\nPlease enter order quantity and continue.");
                    return false;
                }

                if (freeQty > orderQty) {
                    Alert("Free Qty", "[" + productName + "]\nFree quantity should not be greater then Order quantity.");
                    return false;
                }


                if (total < 0) {
                    Alert("Invalid Discount", "[" + productName + "]\nDiscount more than 100% not allowed.");
                    return false;
                }

                if (hsKey.containsKey(rlProductSkuId)) {
                    duplicateFound = true;
                    break;
                } else {
                    hsKey.put(rlProductSkuId, rlProductSkuId);
                }
            }

        }

        if (duplicateFound) {
            Alert("Duplicate Found!", "Please remove duplicate product entries and continue.");
            return false;
        }

        if (hsKey.size() == 0) {
            Alert("No records!", "Please add products and continue.");
            return false;
        }

        return true;
    }

    private int getInt(String str) {
        if (str == null) {
            return 0;
        }

        if (str.length() == 0) {
            return 0;
        }

        return Integer.parseInt(str);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        try {

            Spinner spnProduct = (Spinner) adapterView;
            if (spnProduct.getTag() == "Product") {

                TableRow tr = (TableRow) spnProduct.getParent();
                Spinner spnSKU = (Spinner) tr.getChildAt(1);
                ArrayAdapter<vProductSKU> adpTemp = (ArrayAdapter<vProductSKU>) spnSKU.getAdapter();

                vProduct objvAgent = ((vProduct) spnProduct.getSelectedItem());
                final int agentId = objvAgent.getProductId();

                List<vProductSKU> tmpRoutes = Lists.newArrayList(Collections2.filter(lstProductSku, new Predicate<vProductSKU>() {
                    @Override
                    public boolean apply(vProductSKU input) {
                        return input.getProductId() == agentId;
                    }
                }));

                tmpRoutes.add(0, new vProductSKU(0, "-- Select Sku --"));

                adpTemp.clear();
                adpTemp.addAll(tmpRoutes);
                adpTemp.notifyDataSetChanged();

                spnProduct.requestFocus();

                //bingo(spnSKU.getTag().toString());

                if (isDirty) {

                    spnSKU.setSelection(0);

                    EditText edtQty = (EditText) tr.getChildAt(2);
                    EditText edtDisRate = (EditText) tr.getChildAt(4);
                    EditText edtDis = (EditText) tr.getChildAt(5);
                    EditText txtAddDis = (EditText) tr.getChildAt(6);

                    edtQty.setText("");
                    edtDisRate.setText("");
                    edtDis.setText("");
                    txtAddDis.setText("");
                }

            }

            if (isDirty) {

                TableRow tr = (TableRow) spnProduct.getParent();

                EditText edtQty = (EditText) tr.getChildAt(2);
                EditText edtDisRate = (EditText) tr.getChildAt(4);
                EditText edtDis = (EditText) tr.getChildAt(5);
                EditText txtAddDis = (EditText) tr.getChildAt(6);
                TextView txtTotal = (TextView) tr.getChildAt(7);

                edtQty.setText("");
                edtDisRate.setText("");
                edtDis.setText("");
                txtAddDis.setText("");
                txtTotal.setText("");

                calculateTotal();

            }

            if (spnProduct.getTag() == "SKU") {
            }

            TableRow tr = (TableRow) spnProduct.getParent();
            final EditText orderQty = (EditText) tr.getChildAt(2);
            orderQty.post(new Runnable() {
                @Override
                public void run() {
                    orderQty.requestFocus();
                }
            });

            // Calculate Total Amount While Selecting SKU

//            EditText txtQty = (EditText) tr.getChildAt(2);
//            EditText txtDisRate = (EditText) tr.getChildAt(4);
//            EditText txtDis = (EditText) tr.getChildAt(5);
//            EditText txtAddDis = (EditText) tr.getChildAt(6);
//            TextView txtTotal = (TextView) tr.getChildAt(7);
//            if (spnProduct.getTag() == "SKU") {
//
//                vProductSKU sku = (vProductSKU) spnProduct.getSelectedItem();
//                int qty = Parse.toInt(txtQty.getText());
//                double rate = sku.getRate();
//                double disRate = 0; // Parse.toDbl(txtDisRate.getText());
//                double dis = Parse.toDbl(txtDis.getText());
//                double addDis = Parse.toDbl(txtAddDis.getText());
//                double total;
//
//                total = roundOff(qty * rate, 2);
//
//
//                if (Parse.toStr(txtDisRate.getTag().toString()) == "DIS_RATE") {
//                    dis = roundOff((total * disRate) / 100, 2);
//                    txtDis.setText(String.valueOf(dis));
//                }
//
//                if (Parse.toStr(txtAddDis.getTag().toString()) == "DIS_AMT") {
//                    if (dis != 0) {
//                        disRate = roundOff((dis / total) * 100, 2);
//                        txtDisRate.setText(String.valueOf(disRate));
//                    }
//                }
//
//                total = roundOff(total, 2);
//                txtTotal.setText(String.valueOf(total));
//
//            }
//            calculateTotal();


        } catch (Exception e) {
            errMsg("Product selection!", e);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(SalesOrderEntryActivity.this, R.style.AppTheme));
            alert.setTitle("Confirm!");
            alert.setMessage("Close Without Submit?");

            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
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

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private XTextWatcher setTextChangeListner(final EditText editText) {

        return new XTextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.beforeTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                super.onTextChanged(charSequence, i, i1, i2);
                calculateTotal();
            }


            // https://stackoverflow.com/questions/9385081/how-can-i-change-the-edittext-text-without-triggering-the-text-watcher

            @Override
            public void afterTextChanged(Editable editable) {

                if (getCurrentFocus() != editText) {
                    return;
                }

                editText.removeTextChangedListener(this);

                TableRow tr = (TableRow) editText.getParent();
                Spinner spnSku = (Spinner) tr.getChildAt(1);
                final EditText txtQty = (EditText) tr.getChildAt(2);
                EditText txtDisRate = (EditText) tr.getChildAt(4);
                EditText txtDis = (EditText) tr.getChildAt(5);
                EditText txtAddDis = (EditText) tr.getChildAt(6);
                TextView txtTotal = (TextView) tr.getChildAt(7);

                vProductSKU sku = (vProductSKU) spnSku.getSelectedItem();
                int qty = Parse.toInt(txtQty.getText());
                double rate = sku.getRate();
                double disRate = Parse.toDbl(txtDisRate.getText());
                double dis = Parse.toDbl(txtDis.getText());
                double addDis = Parse.toDbl(txtAddDis.getText());
                double total;

                total = roundOff(qty * rate, 2);

                String type = Parse.toStr(editText.getTag());

                if (type == "DIS_RATE") {
                    dis = roundOff((total * disRate) / 100, 2);
                    txtDis.setText(String.valueOf(dis));
                }

                if (type == "DIS_AMT") {
                    disRate = roundOff((dis / total) * 100, 2);
                    txtDisRate.setText(String.valueOf(disRate));
                }

                total = roundOff(total - dis, 2) - addDis;
                txtTotal.setText(String.valueOf(total));


                calculateTotal();

                editText.addTextChangedListener(this);

                super.afterTextChanged(editable);


            }

        };
    }

    private void calculateTotal() {

        int rowCount = tblMain.getChildCount();
        TableRow tr;
        TextView edtTotalAmount;
        double totalAmount = 0;

        for (int i = 0; i < rowCount; i++) {
            tr = (TableRow) tblMain.getChildAt(i);
            edtTotalAmount = (TextView) tr.getChildAt(7);
            totalAmount += Parse.toDbl(edtTotalAmount.getText().toString());
        }

        tvOrderTotal.setText("Total: " + decimalStr(totalAmount) + "/-");
    }

    private String decimalStr(double value) {
        DecimalFormat myFormatter = new DecimalFormat("0.00");
        String output = myFormatter.format(value);
        return output;
    }

    public Double roundOff(double value, int decimals) {
        Double round = Double.valueOf(0);
        try{
            BigDecimal a = new BigDecimal(value);
            BigDecimal roundOff = a.setScale(decimals, BigDecimal.ROUND_HALF_EVEN);
            round = Parse.toDbl(roundOff);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            return round;
        }
        return round;
    }

    @Override
    public void onTouch() {
        isDirty = true;
    }

    private class RowData {

        private int ownId;
        private int parentId;

        private int rlProductSkuId;
        private int orderQty;
        private int freeQty;

        private double discountRate;
        private double discount;
        private double addDiscount;
        private double totalAmount;

        private double rate;

        public int getOwnId() {
            return ownId;
        }

        public void setOwnId(int ownId) {
            this.ownId = ownId;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public boolean isChildRow() {
            return parentId != 0;
        }

        public int getRlProductSkuId() {
            return rlProductSkuId;
        }

        public void setRlProductSkuId(int rlProductSkuId) {
            this.rlProductSkuId = rlProductSkuId;
        }

        public int getOrderQty() {
            return orderQty;
        }

        public void setOrderQty(int orderQty) {
            this.orderQty = orderQty;
        }

        public int getFreeQty() {
            return freeQty;
        }

        public void setFreeQty(int freeQty) {
            this.freeQty = freeQty;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getAddDiscount() {
            return addDiscount;
        }

        public void setAddDiscount(double addDiscount) {
            this.addDiscount = addDiscount;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }


    }

}
