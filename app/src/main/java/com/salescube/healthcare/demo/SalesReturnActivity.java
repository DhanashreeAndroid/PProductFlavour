package com.salescube.healthcare.demo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.salescube.healthcare.demo.data.model.SalesReturn;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.SalesReturnRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
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

public class SalesReturnActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


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
    private int mAgentId;
    private Drawable textEditDrawble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);

        try {
            initExtra();
            initControls();
            initListners();
            loadData();

            insertHeader();
            loadOrders();
        } catch (Exception e) {
            errMsg("While loading Order screen", e);
            finish();
        }

    }

    private void initExtra() {

        Intent intent = getIntent();

        String strData = intent.getStringExtra(Constant.RETURN_DATE);
        mOrderDate = DateFunc.getDate(strData, "dd/MM/yyyy");

        mShopAppId = intent.getStringExtra(Constant.SHOP_APP_ID);
        mAgentId = intent.getIntExtra(Constant.AGENT_ID, 0);

//        if (mAgentId == 0) {
//            msgShort("Invalid agent id");
//            finish();
//        }

    }

    private void initControls() {

        tblMain = findViewById(R.id.newOrder_tbl_main);
        tblHeader = findViewById(R.id.orderEntry_tbl_header);

        btnAddNewPrd = findViewById(R.id.orderEntry_btn_addProduct);
        btnSumbit = findViewById(R.id.orderEntry_btn_submit);

        srlOrderScroll = findViewById(R.id.orderEntry_srl_orderScroll);

    }

    private void initListners() {

        btnAddNewPrd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProduct(false, 0, 0);

            }
        });

        btnSumbit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    boolean enableGPS = UtilityFunc.isGPSEnabled(true, SalesReturnActivity.this);
                    if (!enableGPS) {
                        return;
                    }

                    try {

                         setAgent();
//                        submitOrders();
                        // new AsyncSubmit().execute(tblMain, null, null);


//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                submitOrders();
//                            }
//                        });

                    } catch (Exception e) {
                        errMsg("While submitting order!", e);
                    }

                }
            }
        });

    }

    private void setAgent() {

        final Dialog dialog = new Dialog(SalesReturnActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.agent_list);

        final Spinner spnReason = dialog.findViewById(R.id.spn_agent);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);

        AgentRepo agentRepo = new AgentRepo();
        List<vAgent> agentList=agentRepo.getAgentAll(AppControl.getmEmployeeId());
        agentList.add(0,new vAgent(0,"-----Select Agent-----"));

        TSAdapter adpReason = new TSAdapter(dialog.getContext(), android.R.layout.simple_spinner_item, agentList);
        adpReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReason.setAdapter(adpReason);

        dialog.show();

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                vAgent agent = (vAgent) spnReason.getSelectedItem();
                Log.e("", agent.getAgentName());

                if (agent.getAgentId() == 0) {
                    Alert("Invalid Agent", "Please select Agent!");
                    return;
                }

                boolean enableGPS = UtilityFunc.isGPSEnabled(true, SalesReturnActivity.this);
                if (!enableGPS) {
                    return;
                }

                try {
                    submitOrders(agent.getAgentId());
                } catch (Exception ex) {
                    Alert("Order Entry!", ex.getMessage());
                }

                dialog.dismiss();
            }
        });

//        btnCancel.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

    }

    private void loadData() {

        ProductRepo repProduct = new ProductRepo();

        lstProduct = repProduct.getProductsAll(AppControl.getmEmployeeId());
        lstProductSku = repProduct.getProductSkuAll(AppControl.getmEmployeeId(), mAgentId, mShopAppId, "");

        lstProduct.add(0, new vProduct(0, "--Select Product--"));

        adpProduct = new ArrayAdapter<vProduct>(SalesReturnActivity.this, R.layout.small_spinner_item, lstProduct);
        adpProduct.setDropDownViewResource(R.layout.small_spinner_dropdown_item);


    }

    private void insertHeader() {

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);
        // tr.setBackgroundResource(R.drawable.table_row_header);
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        int textColor = ContextCompat.getColor(this, R.color.colorWhite);

        // Product

        tv = new TextView(this);
        tv.setText("Product");
        tv.setPadding(18, 5, 10, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 3f));
        tv.setTextSize(14);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tr.addView(tv);

        // Product SKU

        tv = new TextView(this);
        tv.setText("Product SKU");
        tv.setPadding(18, 5, 10, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 4f));
        tv.setTextSize(14);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(textColor);
        tr.addView(tv);

        // Order Quantity

        tv = new TextView(this);
        tv.setText("Return\nQty");
        tv.setPadding(10, 5, 10, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(14);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setTextColor(textColor);
        tr.addView(tv);

        // Product Remove

        tv = new TextView(this);
        tv.setText("Del.");
        tv.setPadding(10, 5, 10, 5);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(14);
        tv.setTypeface(tf);
        tv.setBackgroundResource(R.drawable.table_cell_header);
        tv.setTextColor(textColor);
        tr.addView(tv);

        tblHeader.addView(tr);

    }

    private void loadOrders() {

        SalesReturnRepo orderRepo = new SalesReturnRepo();
        List<SalesReturn> orders = orderRepo.getShopOrders(AppControl.getmEmployeeId(), mShopAppId, mOrderDate);

        if (orders.size() == 0) {
            insertProduct(false, 0, 0);
            return;
        }

        TableRow tr;
        TableRow childRow;

        Spinner spnProductId;
        Spinner spnRlProduct;
        EditText edtOrderQty;

        int index;
        int productId;

        for (SalesReturn order : orders) {

            tr = insertProduct(false, 0, 0);

            spnProductId = (Spinner) tr.getChildAt(0);
            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);

            productId = getProductId(order.getRlProductSkuId());
            index = getProductIndex(spnProductId, productId);
            spnProductId.setSelection(index);

            setSkuAdapter(spnRlProduct, productId);

            index = getProductSKUIndex(spnRlProduct, order.getRlProductSkuId());
            spnRlProduct.setSelection(index);

            edtOrderQty.setText(Parse.toStr(order.getReturnQty()));

//            for (SalesReturn scheme : orders) {
//                if (order.getRlProductSkuId() == scheme.getRlProductSkuId()) {
//
//                    childRow = insertScheme(tr);
//
//                    spnProductId = (Spinner) childRow.getChildAt(0);
//                    spnRlProduct = (Spinner) childRow.getChildAt(1);
//
//                    productId = getProductId(scheme.getScheme_rlProductSkuId());
//                    index = getProductIndex(spnProductId, productId);
//                    spnProductId.setSelection(index);
//
//                    setSkuAdapter(spnRlProduct, productId);
//
//                    index = getProductSKUIndex(spnRlProduct, scheme.getScheme_rlProductSkuId());
//                    spnRlProduct.setSelection(index);
//                }
//            }

        }
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
        TextView tv;

        int bgResorce;
        if (!isSchemeProduct) {
            bgResorce = R.drawable.table_cell_main;
        } else {
            bgResorce = R.drawable.table_cell_child;
        }

        ArrayAdapter<vProductSKU> adpSkuTemp = new ArrayAdapter<vProductSKU>(SalesReturnActivity.this, R.layout.small_spinner_item, new ArrayList<vProductSKU>());
        adpSkuTemp.setDropDownViewResource(R.layout.small_spinner_dropdown_item);

        rowUniqueId += 1;

        RowData cr = new RowData();
        cr.setOwnId(rowUniqueId);
        cr.setParentId(parentId);

        tr = new TableRow(this);
        //tr.setBackgroundResource(R.drawable.table_row_bg);
        tr.setPadding(5, 2, 5, 2);
        tr.setTag(cr);

        // Product

        spn = new Spinner(this, Spinner.MODE_DIALOG);
        spn.setPadding(10, 0, 10, 0);
        spn.setLayoutParams(new TableRow.LayoutParams(0, -1, 3f));
        spn.setAdapter(adpProduct);
        spn.setOnItemSelectedListener(this);
        spn.setTag("Product");
        spn.setBackgroundResource(bgResorce);
        tr.addView(spn);

        // Product SKU

        spn = new Spinner(this, Spinner.MODE_DIALOG);
        spn.setPadding(10, 0, 10, 0);
        spn.setLayoutParams(new TableRow.LayoutParams(0, -1, 4f));
        spn.setAdapter(adpSkuTemp);
        spn.setOnItemSelectedListener(this);
        spn.setTag("SKU");
        spn.setBackgroundResource(bgResorce);
        tr.addView(spn);

        // Order Quantity

        editTxt = new EditText(this);
        editTxt.setText("");
        editTxt.setPadding(12, 0, 10, 0);
        editTxt.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
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

        // Product Remove

        btn = new Button(this);
        btn.setText("X");
        btn.setPadding(10, 0, 10, 0);
        btn.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        btn.setOnClickListener(this);
        btn.setTextSize(14);
        btn.setTag("REMOVE");
        btn.getLayoutParams().height = 80;
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
            cr = (SalesReturnActivity.RowData) tmpRow.getTag();

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
                    cr = (SalesReturnActivity.RowData) childRow.getTag();

                    if (ownId == cr.getParentId()) {
                        tblMain.removeView(childRow);
                    }
                }
            }
        }

        tblMain.removeView(mainRow);
    }

    private void submitOrders(int agentId) {

        TableRow tr;
        RowData rd;

        int rowCount = tblMain.getChildCount();

        SalesReturnRepo orderRepo;
        SalesReturn objOrder;
        List<SalesReturn> objOrders;

        int rlProductSkuId;
        int orderQty;

        List<RowData> lstRows = new ArrayList<RowData>();

        Spinner spnRlProduct;
        EditText edtOrderQty;

        // Complex to Simplex
        for (int i = 0; i < rowCount; i++) {

            tr = (TableRow) tblMain.getChildAt(i);
            rd = (RowData) tr.getTag();

            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);

            vProductSKU sku = (vProductSKU) spnRlProduct.getSelectedItem();

            rlProductSkuId = sku.getRlProductSkuId();
            orderQty = Parse.toInt(edtOrderQty.getText().toString());

            if (rlProductSkuId == 0) {
                continue;
            }

            if (orderQty == 0) {
                continue;
            }

            rd.setRlProductSkuId(rlProductSkuId);
            rd.setOrderQty(orderQty);

            lstRows.add(rd);
        }


        boolean hasSchemes;
        objOrders = new ArrayList<>();
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

                    objOrder = new SalesReturn();
                    objOrder.setSoId(AppControl.getmEmployeeId());
                    objOrder.setReturnDate(mOrderDate);
                    objOrder.setSetNo(setNo);
                    objOrder.setAppShopId(mShopAppId);
                    objOrder.setRlProductSkuId(product.getRlProductSkuId());
                    objOrder.setReturnQty(product.getOrderQty());
                    objOrder.setAgentId(agentId);
                    objOrder.setCreatedDateTime(dateNow);
                    objOrders.add(objOrder);

                    hasSchemes = true;
                }
            }

            if (!hasSchemes) {

                // Main Product
                objOrder = new SalesReturn();
                objOrder.setSoId(AppControl.getmEmployeeId());
                objOrder.setReturnDate(mOrderDate);
                objOrder.setSetNo(setNo);
                objOrder.setAppShopId(mShopAppId);
                objOrder.setRlProductSkuId(product.getRlProductSkuId());
                objOrder.setReturnQty(product.getOrderQty());

                objOrder.setAgentId(agentId);
                objOrder.setCreatedDateTime(dateNow);
                objOrders.add(objOrder);

            }
        }

        // Update into database
        orderRepo = new SalesReturnRepo();

        orderRepo.cancelShopOrders(mShopAppId, mOrderDate);
        orderRepo.insert(objOrders);

        // Close Activity
        msgShort("Sales Return Successful!");

        Intent intent = new Intent();
        intent.putExtra("HasSuccess", true);
        setResult(RESULT_OK, intent);

        doManualUpload (mShopAppId, EVENT_ORDER);
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

                    AlertDialog.Builder alert = new AlertDialog.Builder(SalesReturnActivity.this);
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
        double totalAmount;

        Spinner spnRlProduct;
        EditText edtOrderQty;

        Hashtable hsKey = new Hashtable();

        EditText r = new EditText(this);
        boolean duplicateFound = false;

        for (int i = 0; i < rowCount; i++) {

            tr = (TableRow) tblMain.getChildAt(i);
            rd = (RowData) tr.getTag();

            spnRlProduct = (Spinner) tr.getChildAt(1);
            edtOrderQty = (EditText) tr.getChildAt(2);

            vProductSKU sku = (vProductSKU) spnRlProduct.getSelectedItem();

            rlProductSkuId = sku.getRlProductSkuId();
            orderQty = Parse.toInt(edtOrderQty.getText().toString());

            if (orderQty > 0) {
                if (rlProductSkuId == 0) {
                    Alert("Invalid Product", "Please select Product / Product SKU!");
                    return false;
                }
            }

            if (orderQty < 0) {
                Alert("Invalid Qty", "Negative quantity not allowed!");
                return false;
            }

//            if (rlProductSkuId == 0) {
//                continue;
//            }

            if (rlProductSkuId == 0) {
                Alert("Invalid Product", "Please select Product / Product SKU!");
                return false;
            }

            if(!rd.isChildRow()){

                if (orderQty == 0) {
                    Alert("Order Qty", "Required! Please enter order quantity and continue.");
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

//    private int getInt(String str) {
//        if (str == null) {
//            return 0;
//        }
//
//        if (str.length() == 0) {
//            return 0;
//        }
//
//        return Integer.parseInt(str);
//    }

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

                //spnSKU.setSelection(0);
                spnProduct.requestFocus();

                //bingo(spnSKU.getTag().toString());
            }

            if (spnProduct.getTag() == "SKU") {

//                TableRow tr = (TableRow) spnProduct.getParent();
//                final  EditText orderQty = (EditText)tr.getChildAt(2);
//                orderQty.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        orderQty.requestFocus();
//                    }
//                });

            }

            TableRow tr = (TableRow) spnProduct.getParent();
            final EditText orderQty = (EditText) tr.getChildAt(2);
            orderQty.post(new Runnable() {
                @Override
                public void run() {
                    orderQty.requestFocus();
                }
            });

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
            AlertDialog.Builder alert = new AlertDialog.Builder(SalesReturnActivity.this);
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

    private boolean isRunning = false;

    private XTextWatcher setTextChangeListner(final EditText editText) {

        return new XTextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.beforeTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
            }

            // https://stackoverflow.com/questions/9385081/how-can-i-change-the-edittext-text-without-triggering-the-text-watcher

        };
    }

    public Double roundOff(double value, int decimals) {

        BigDecimal a = new BigDecimal(value);
        BigDecimal roundOff = a.setScale(decimals, BigDecimal.ROUND_HALF_EVEN);
        //System.out.println(roundOff);

        return Parse.toDbl(roundOff);
    }

    public Double roundOff2(double value) {

        DecimalFormat df2 = new DecimalFormat("##.00");
        double dd2dec = new Double(df2.format(value)).doubleValue();
        return dd2dec;
    }


//    @Override
//    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable editable) {
//
//        TableRow tr = (TableRow) editable. .getParent();
//        Spinner spnSKU = (Spinner) tr.getChildAt(1);
//
//        msgShort(editable.toString());
//    }

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

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

    }

}
