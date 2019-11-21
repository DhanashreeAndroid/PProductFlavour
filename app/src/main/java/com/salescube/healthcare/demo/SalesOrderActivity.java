package com.salescube.healthcare.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.data.repo.AreaRepo;
import com.salescube.healthcare.demo.data.repo.ColdCallRepo;
import com.salescube.healthcare.demo.data.repo.LocalityRepo;
import com.salescube.healthcare.demo.data.repo.RouteRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.TargetRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.TextFunc;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SearchableSpinner;
import com.salescube.healthcare.demo.view.vArea;
import com.salescube.healthcare.demo.view.vLocality;
import com.salescube.healthcare.demo.view.vRoute;
import com.salescube.healthcare.demo.view.vShop;
import com.salescube.healthcare.demo.view.vSysDate;
import com.salescube.healthcare.demo.view.vTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_NO_ORDER;

public class SalesOrderActivity extends BaseTransactionActivity implements AdapterView.OnItemSelectedListener,
        SearchableSpinner.SearchableSpinnerTouchListener {

    private TextView txtCity;
    private SpinnerDialog<vArea> spinnerCity;
    private SpinnerDialog<vRoute> spinnerRoute;
    private SpinnerDialog<vLocality> spinnerLocality;
    private SpinnerDialog<vShop> spinnerShop;
    private TextView txtRoute;
    private TextView txtLocality;
    private TextView txtShop;
    private Spinner spnOrderDate;
    private Button btnNewOrder;
    private Button btnNoOrder;
    private Button btnComplaint;
    private Button btnLastOrders;
    private Button btnPOPEntry;
    private Button btnCompetitorInfo;
    private Button btnSalesReturn;
    private Button btnShopAdd;
    private Button btnShopEdit;
    private Button btnShopStock;


    private TableLayout tblTargetHead;
    private TableLayout tblTargetData;

    private List<vArea> lstAgent;
    private List<vRoute> lstRoutes;
    private List<vLocality> lstLocalities;
    private List<vShop> lstShops;

    private final int SHOP_ADD_REQUEST = 01;
    private final int ORDER_REQUEST = 02;
    private final int SALES_RETURN_REQUEST = 03;

    private int employeeId;

    private final String MY_PREF = "LAST_SALES_ORDER";
    private final String KEY_CITY = "KEY_CITY";
    private final String KEY_ROUTE = "KEY_ROUTE";
    private final String KEY_LOCALITY = "KEY_LOCALITY";
    private final String KEY_SHOP = "KEY_SHOP";

    final String SELECT_CITY = "------- SELECT CITY -------";
    final String SELECT_ROUTE = "------- SELECT ROUTE -------";
    final String SELECT_LOCALITY = "------- SELECT LOCALITY -------";
    final String SELECT_SHOP = "------- SELECT SHOP -------";

    private boolean isDirty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sales_order);
        title("Sales Order");

        employeeId = AppControl.getmEmployeeId();

        try {
            initControls();
            initListners();
            loadData();
            loadTarget();
        } catch (Exception e) {
            errMsg("While loading Order", e);
            finish();
        }

    }


    private void initControls() {

        txtCity = findViewById(R.id.ord_spn_agent);
        txtRoute = findViewById(R.id.ord_spn_route);

        txtLocality = findViewById(R.id.ord_spn_locality);
        txtShop = findViewById(R.id.ord_spn_shop);

        btnNewOrder = (Button) findViewById(R.id.ord_btn_new_order);

        spnOrderDate = (Spinner) findViewById(R.id.order_spn_orderDate);
        btnNoOrder = (Button) findViewById(R.id.ord_btn_no_order);
        btnComplaint = (Button) findViewById(R.id.ord_btn_complaint);
        btnLastOrders = (Button) findViewById(R.id.order_btn_lastOrder);
        btnPOPEntry = (Button) findViewById(R.id.order_btn_pop);
        btnCompetitorInfo = (Button) findViewById(R.id.order_btn_competitor);
        btnSalesReturn = findViewById(R.id.ord_btn_return);

        btnShopAdd = (Button) findViewById(R.id.order_btn_shop_add);
        btnShopEdit = (Button) findViewById(R.id.order_btn_edit_shop);
        btnShopStock = (Button) findViewById(R.id.ord_btn_stock_order);

        tblTargetHead = (TableLayout) findViewById(R.id.order_tbl_target_head);
        tblTargetData = (TableLayout) findViewById(R.id.order_tbl_target_data);
    }


    private void setSpinnerSetting(SpinnerDialog spinnerDialog) {

        spinnerDialog.setTitleColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchIconColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchTextColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemDividerColor(getResources().getColor(R.color.colorLightGray));
        spinnerDialog.setCloseColor(getResources().getColor(R.color.colorAccent));

        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);
    }

    private void initListners() {

        btnNewOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateForNewOrder()) {
                    return;
                }

                boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
                if (!isGPSOn) {
                    return;
                }


                DateFormat objDf = new SimpleDateFormat("dd/MM/yyyy");
                vShop shop = getShop();

                String shopAppId = shop.getAppShopId();
                String shopName = shop.getShopName();
                int areaId = Parse.toInt(txtCity.getTag());

                Intent orderIntent = new Intent(SalesOrderActivity.this, SalesOrderEntryActivity.class);

                orderIntent.putExtra(Constant.ORDER_DATE, objDf.format(getSelectedDate().getTrDate()));
                orderIntent.putExtra(Constant.SHOP_APP_ID, shopAppId);
                orderIntent.putExtra(Constant.SHOP_NAME, shopName);
                orderIntent.putExtra(Constant.AREA_ID, areaId);
                orderIntent.putExtra(Constant.IS_NEW, true);

                startActivityForResult(orderIntent, ORDER_REQUEST);

            }
        });

        btnLastOrders.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForNewOrder()) {
                    return;
                }
                DateFormat objDf = new SimpleDateFormat("dd/MM/yyyy");

                Intent inNewOrder = new Intent(SalesOrderActivity.this, SalesOrderViewActivity.class);
                inNewOrder.putExtra(Constant.ORDER_DATE, objDf.format(getSelectedDate().getTrDate()));
                inNewOrder.putExtra(Constant.SHOP_NAME, getShop().getShopName());
                inNewOrder.putExtra(Constant.SHOP_APP_ID, getShop().getAppShopId());
                startActivity(inNewOrder);

            }
        });


        btnNoOrder.setOnClickListener(btnNoOrder_OnClick);
        btnPOPEntry.setOnClickListener(btnPOPEntry_OnClick);

        btnCompetitorInfo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForNewOrder()) {
                    return;
                }

                boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
                if (!isGPSOn) {
                    return;
                }

                Intent popIntent = new Intent(SalesOrderActivity.this, CompititorActivity.class);
                popIntent.putExtra(Constant.ORDER_DATE, DateFunc.getDateStr(getSelectedDate().getTrDate()));
                popIntent.putExtra(Constant.AGENT_ID, Parse.toInt(txtCity.getTag()));
                popIntent.putExtra(Constant.SHOP_APP_ID, getShop().getAppShopId());
                startActivity(popIntent);

            }
        });

        btnShopAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtLocality.getTag() != null && Parse.toInt(txtLocality.getTag()) == 0) {
                    Alert("Required!", "Please select locality to add new shop.");
                    return;
                }

                Intent popIntent = new Intent(SalesOrderActivity.this, ShopAddActivity.class);
                popIntent.putExtra(Constant.LOCALITY_ID, Parse.toInt(txtLocality.getTag()));
                startActivityForResult(popIntent, SHOP_ADD_REQUEST);

            }
        });

        btnShopEdit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                String appShopId = getShop().getAppShopId();
                if (TextFunc.isEmpty(appShopId)) {
                    Alert("Required!", "Please select shop to edit.");
                    return;
                }

                Intent popIntent = new Intent(SalesOrderActivity.this, ShopAddActivity.class);
                popIntent.putExtra(Constant.LOCALITY_ID, Parse.toInt(txtLocality.getTag()));
                popIntent.putExtra(Constant.IDENTITY, getShop().getId());
                popIntent.putExtra(Constant.SHOP_APP_ID, getShop().getAppShopId());
                popIntent.putExtra(Constant.SHOP_ID, getShop().getShopId());

                startActivityForResult(popIntent, SHOP_ADD_REQUEST);
            }
        });


        btnComplaint.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateForNewOrder()) {
                    return;
                }

                Intent inNewOrder = new Intent(SalesOrderActivity.this, ComplaintActivity.class);

                inNewOrder.putExtra("SOURCE", "ORDER");

                inNewOrder.putExtra(Constant.AREA_ID, Parse.toInt(txtCity.getTag()));
                inNewOrder.putExtra(Constant.ROUTE_ID, Parse.toInt(txtRoute.getTag()));
                inNewOrder.putExtra(Constant.LOCALITY_ID, Parse.toInt(txtLocality.getTag()));
                inNewOrder.putExtra(Constant.SHOP_ID, getShop().getShopId());

                startActivity(inNewOrder);

            }
        });

        btnSalesReturn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForNewOrder()) {
                    return;
                }

                boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
                if (!isGPSOn) {
                    return;
                }

                DateFormat objDf = new SimpleDateFormat("dd/MM/yyyy");
                vShop shop = getShop();

                String shopAppId = shop.getAppShopId();
                String shopName = shop.getShopName();
//                int agentId = getAgent().getAgentId();
                int agentId = 0;
//                int areaId = getArea().getAreaId();

                Intent orderIntent = new Intent(SalesOrderActivity.this, SalesReturnActivity.class);

                orderIntent.putExtra(Constant.RETURN_DATE, objDf.format(getSelectedDate().getTrDate()));
                orderIntent.putExtra(Constant.SHOP_APP_ID, shopAppId);
                orderIntent.putExtra(Constant.SHOP_NAME, shopName);
                orderIntent.putExtra(Constant.AGENT_ID, agentId);
//                orderIntent.putExtra(Constant.AREA_ID, areaId);
                orderIntent.putExtra(Constant.IS_NEW, true);

                startActivityForResult(orderIntent, SALES_RETURN_REQUEST);

            }
        });

        btnShopStock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SalesOrderActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();


                if (!validateForNewOrder()) {
                    return;
                }

                vShop shop = getShop();
                String shopAppId = shop.getAppShopId();
                String shopId = String.valueOf(shop.getShopId());

                try {
                    Intent toolsIntent = new Intent(SalesOrderActivity.this, StockEntryActivity.class);
                    toolsIntent.putExtra(Constant.SHOP_ID, shopId);
                    toolsIntent.putExtra(Constant.SHOP_APP_ID, shopAppId);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    errMsg("Stock Entry!", ex);
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SHOP_ADD_REQUEST) {
            String update = data.getStringExtra("Update");
            if (update.equals("Y")) {
                updateShops(data.getStringExtra("AppShopId"));
            }
        }

        if (requestCode == ORDER_REQUEST) {
            boolean success = data.getBooleanExtra("HasSuccess", false);
            if (success) {

                try {
                    loadTarget();
                } catch (Exception e) {

                }
            }
        }

    }

    private Button.OnClickListener btnPOPEntry_OnClick = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!validateForNewOrder()) {
                return;
            }

            boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
            if (!isGPSOn) {
                return;
            }

            Intent popIntent = new Intent(SalesOrderActivity.this, POPActivity.class);
            popIntent.putExtra(Constant.ORDER_DATE, DateFunc.getDateStr(getSelectedDate().getTrDate()));
            popIntent.putExtra(Constant.AGENT_ID, Parse.toInt(txtCity.getTag()));
            popIntent.putExtra(Constant.SHOP_APP_ID, getShop().getAppShopId());
            startActivity(popIntent);

        }
    };

    Button.OnClickListener btnNoOrder_OnClick = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!validateForNewOrder()) {
                return;
            }

            Date orderDate = getSelectedDate().getTrDate();
            boolean hasOrders = new SalesOrderRepo().hasOrders(AppControl.getmEmployeeId(), getShop().getAppShopId(), orderDate);

            if (hasOrders) {
                Alert("Restricted!", "Sales order already exist for this shop, you can not enter no-order");
                return;
            }

            boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
            if (!isGPSOn) {
                return;
            }

            final Dialog dialog = new Dialog(SalesOrderActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_no_order);

            final Spinner spnReason = (Spinner) dialog.findViewById(R.id.no_order_spn_reason);
            final TextView txtExtraInfo = (TextView) dialog.findViewById(R.id.no_order_txt_msg);
            Button btnSubmit = (Button) dialog.findViewById(R.id.no_order_btn_submit);
            Button btnCancel = (Button) dialog.findViewById(R.id.no_order_btn_cancel);

            ArrayAdapter<String> adpReason = new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_spinner_item, getReasons());
            adpReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnReason.setAdapter(adpReason);

            dialog.show();

            btnSubmit.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String reason = spnReason.getSelectedItem().toString();
                    String extraInfo = txtExtraInfo.getText().toString().trim();

                    if (reason.equals("Competitor Product")) {
                        if (extraInfo.length() == 0) {
                            Toast.makeText(dialog.getContext(), "Please Enter Competitor Product", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    boolean isGPSOn = UtilityFunc.isGPSEnabled(true, SalesOrderActivity.this);
                    if (!isGPSOn) {
                        return;
                    }

                    if (extraInfo.length() != 0) {
                        reason += " - " + extraInfo;
                    }

                    ColdCallRepo objColCallData = new ColdCallRepo();

                    NoOrder objColdCall = new NoOrder();
                    objColdCall.setAgentId((Integer) txtCity.getTag());
                    objColdCall.setSoId(AppControl.getmEmployeeId());
                    objColdCall.setOrderDate(getSelectedDate().getTrDate());
                    objColdCall.setAppShopId(getShop().getAppShopId());
                    objColdCall.setReason(reason);
                    objColCallData.insert(objColdCall);

                    dialog.dismiss();

                    doManualUpload(objColdCall.getAppShopId(), EVENT_NO_ORDER);

                    msgShort("NO-Order Submited!");
                }
            });

            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    };

    private void loadData() {

        ShopRepo shopRepo = new ShopRepo();
        SysDateRepo dateRepo = new SysDateRepo();

        lstAgent = new AreaRepo().getAreaAll(employeeId);
        lstRoutes = new RouteRepo().getAllRoutes(employeeId);
        lstLocalities = new LocalityRepo().getAllLocalities((employeeId));
        lstShops = shopRepo.getShopsAll(AppControl.getmEmployeeId());

        // --------- CITY ---------------------------------------------------

        int lastIndex = getLastCityIndex();
        if (lastIndex < lstAgent.size()) {
            txtCity.setText(lstAgent.get(lastIndex).getAreaName());
            txtCity.setTag(lstAgent.get(lastIndex).getAreaId());
        } else {
            txtCity.setText(SELECT_CITY);
            txtCity.setTag(0);
        }

        spinnerCity = new SpinnerDialog<>(SalesOrderActivity.this, lstAgent, "CITY");
        setSpinnerSetting(spinnerCity);

        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCity.showSpinerDialog();
            }
        });

        spinnerCity.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position, Object object) {
                //Toast.makeText(MainActivity.this, item + "  " + position + "", Toast.LENGTH_SHORT).show();

                vArea city = (vArea) object;
                txtCity.setText(city.getAreaName());
                txtCity.setTag(city.getAreaId());
                setLastCityIndex(position);

                List<vRoute> tmpRoutes = Lists.newArrayList(Collections2.filter(lstRoutes, new Predicate<vRoute>() {
                    @Override
                    public boolean apply(vRoute input) {
                        return input.getAreaId() == city.getAreaId();
                    }
                }));

                vRoute objvRoute = new vRoute();
                objvRoute.setRouteId(0);
                objvRoute.setRouteName(SELECT_ROUTE);
                tmpRoutes.add(0, objvRoute);

                spinnerRoute.refreshData(tmpRoutes);

                txtRoute.setTag(0);
                txtRoute.setText(SELECT_ROUTE);
                setLastRouteIndex(0);

                txtLocality.setTag(0);
                txtLocality.setText(SELECT_LOCALITY);
                setLastLocalityIndex(0);

                spinnerLocality.refreshData(new ArrayList<>());

                txtShop.setTag(0);
                txtShop.setText(SELECT_SHOP);
                setLastShopIndex(0);

                spinnerShop.refreshData(new ArrayList<>());

            }
        });

        //--------- ROUTE ---------------------------------------------

        final int agentId = Parse.toInt(txtCity.getTag());

        List<vRoute> tmpRoutes = Lists.newArrayList(Collections2.filter(lstRoutes, new Predicate<vRoute>() {
            @Override
            public boolean apply(vRoute input) {
                return input.getAreaId() == agentId;
            }
        }));

        vRoute objvRoute = new vRoute();
        objvRoute.setRouteId(0);
        objvRoute.setRouteName(SELECT_ROUTE);
        tmpRoutes.add(0, objvRoute);

        int lastRoute = getRouteIndex();
        if (lastRoute < tmpRoutes.size()) {
            txtRoute.setText(tmpRoutes.get(lastRoute).getRouteName());
            txtRoute.setTag(tmpRoutes.get(lastRoute).getRouteId());
        } else {
            txtRoute.setText(SELECT_ROUTE);
            txtRoute.setTag(0);
        }

        spinnerRoute = new SpinnerDialog<>(SalesOrderActivity.this, tmpRoutes, "ROUTE");
        setSpinnerSetting(spinnerRoute);


        txtRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerRoute.showSpinerDialog();
            }
        });

        spinnerRoute.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position, Object object) {

                vRoute route = (vRoute) object;
                txtRoute.setText(route.getRouteName());
                txtRoute.setTag(route.getRouteId());
                setLastRouteIndex(position);

                txtLocality.setText(SELECT_LOCALITY);
                txtLocality.setTag(0);

                List<vLocality> tmpLocality = Lists.newArrayList(Collections2.filter(lstLocalities, new Predicate<vLocality>() {
                    @Override
                    public boolean apply(vLocality input) {
                        return input.getRouteId() == Parse.toInt(txtRoute.getTag());
                    }
                }));

                vLocality objvLocality = new vLocality();
                objvLocality.setLocalityId(0);
                objvLocality.setLocalityName(SELECT_LOCALITY);
                tmpLocality.add(0, objvLocality);

                spinnerLocality.refreshData(tmpLocality);

                txtLocality.setTag(0);
                txtLocality.setText(SELECT_LOCALITY);
                setLastLocalityIndex(0);

                txtShop.setTag(0);
                txtShop.setText(SELECT_SHOP);
                setLastShopIndex(0);

                spinnerShop.refreshData(new ArrayList<>());
            }
        });

        //-------- LOCALITY ------------------------------------------------------

        final int routeId = Parse.toInt(txtRoute.getTag());

        List<vLocality> tmpLocality = Lists.newArrayList(Collections2.filter(lstLocalities, new Predicate<vLocality>() {
            @Override
            public boolean apply(vLocality input) {
                return input.getRouteId() == routeId;
            }
        }));

        vLocality objvLocality = new vLocality();
        objvLocality.setLocalityId(0);
        objvLocality.setLocalityName(SELECT_LOCALITY);
        tmpLocality.add(0, objvLocality);

        int lastLocality = getLastLocalityIndex();
        if (lastLocality < tmpLocality.size()) {
            txtLocality.setText(tmpLocality.get(lastLocality).getLocalityName());
            txtLocality.setTag(tmpLocality.get(lastLocality).getLocalityId());
        } else {
            txtLocality.setText(SELECT_LOCALITY);
            txtLocality.setTag(0);
        }


        spinnerLocality = new SpinnerDialog<>(SalesOrderActivity.this, tmpLocality, "LOCALITY");
        setSpinnerSetting(spinnerLocality);

        txtLocality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerLocality.showSpinerDialog();
            }
        });

        spinnerLocality.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position, Object object) {

                vLocality route = (vLocality) object;
                txtLocality.setText(route.getLocalityName());
                txtLocality.setTag(route.getLocalityId());
                setLastLocalityIndex(position);

                List<vShop> tmpShop = Lists.newArrayList(Collections2.filter(lstShops, new Predicate<vShop>() {
                    @Override
                    public boolean apply(vShop input) {
                        return input.getLocalityId() == route.getLocalityId();
                    }
                }));

                vShop objvShop = new vShop();
                objvShop.setShopId(0);
                objvShop.setShopName(SELECT_SHOP);
                tmpShop.add(0, objvShop);

                spinnerShop.refreshData(tmpShop);

                txtShop.setTag(0);
                txtShop.setText(SELECT_SHOP);
                setLastShopIndex(0);

            }
        });

        //------- SHOP -----------------------------------------------------------

        final int localityId = Parse.toInt(txtLocality.getTag());
        List<vShop> tmpShop = Lists.newArrayList(Collections2.filter(lstShops, new Predicate<vShop>() {
            @Override
            public boolean apply(vShop input) {
                return input.getLocalityId() == localityId;
            }
        }));
        vShop objvShop = new vShop();
        objvShop.setShopId(0);
        objvShop.setShopName(SELECT_SHOP);
        tmpShop.add(0, objvShop);

        int lastShop = getLastShopIndex();
        if (lastShop < tmpShop.size()) {
            txtShop.setText(tmpShop.get(lastShop).getShopName());
            txtShop.setTag(tmpShop.get(lastShop).getAppShopId());
        } else {
            txtShop.setText(SELECT_SHOP);
            txtShop.setTag(0);
        }


        spinnerShop = new SpinnerDialog<>(SalesOrderActivity.this, tmpShop, "SHOP");
        setSpinnerSetting(spinnerShop);

        txtShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerShop.showSpinerDialog();
            }
        });

        spinnerShop.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position, Object object) {

                vShop route = (vShop) object;
                txtShop.setText(route.getShopName());
                txtShop.setTag(route.getAppShopId());
                setLastShopIndex(position);
            }
        });

        List<vSysDate> lstSysDate = dateRepo.getDates(AppControl.getmEmployeeId());
        ArrayAdapter<vSysDate> adpSysDate = new ArrayAdapter<vSysDate>(this, android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOrderDate.setAdapter(adpSysDate);

    }

    private void loadTarget() {

        tblTargetHead.removeAllViews();
        tblTargetData.removeAllViews();

        final int TEXT_SIZE = 14;
        TargetRepo targetRepo = new TargetRepo();
        vTarget target = targetRepo.getTodaysTarget(AppControl.getmEmployeeId(), AppControl.getTodayDate());

        addTargetHeader("", "Target", "Ach.", "Balance", true);
        addTargetValue("Month", (long) target.getMonthTargetValue(), (long) target.getMonthAchievementValue(), (long) target.getMonthBalanceTarget());
        addTargetValue("Today", (long) target.getDayTargetValue(), (long) target.getDayAchievementValue(), (long) target.getDayBalanceTarget());

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Focus Products");
        tv.setTextColor(Color.BLUE);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        tv.setGravity(Gravity.CENTER);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblTargetData.addView(tr);


        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(target.getFocusProducts());
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);

        tv.setGravity(Gravity.CENTER);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblTargetData.addView(tr);

    }

    private void addTargetHeader(String p1, String p2, String p3, String p4, boolean isHeading) {

        final int TEXT_SIZE = 14;

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(p1);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLUE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(p2);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLUE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(p3);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLUE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(p4);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLUE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        if (isHeading) {
            tr.setBackgroundResource(R.drawable.table_row_header);
            tblTargetHead.addView(tr);
        } else {
            tr.setBackgroundResource(R.drawable.table_row_bg);
            tblTargetData.addView(tr);
        }

    }

    private void addTargetValue(String p1, long p2, long p3, long p4) {

        final int TEXT_SIZE = 14;

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(p1);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf(p2));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf(p3));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf(p4));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1f));
        tv.setTextSize(TEXT_SIZE);

        if (p4 < 0) {
            tv.setTextColor(Color.RED);
        } else if (p4 > 0) {
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(Color.YELLOW);
        }

        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblTargetData.addView(tr);

    }

    private List<String> getReasons() {
        List<String> reasons = new ArrayList<String>();

        reasons.add("Stock Available");
        reasons.add("Feeding by Wholesale");
        reasons.add("Black listed By Agent");
        reasons.add("Duplicators Problem");
        reasons.add("Shop Close");
        reasons.add("Cash Problem (Ready Stock)");

        return reasons;
    }

    private boolean validateForNewOrder() {

        if (TextUtils.isEmpty(getArea())) {
            showAlert("Select City!", "Please select city.");
            txtCity.requestFocus();
            return false;
        }

        if (getShop().getAppShopId() == null) {
            showAlert("Select Shop!", "Please select a shop to make new order.");
            txtShop.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {

        AlertDialog.Builder alert = new AlertDialog.Builder(SalesOrderActivity.this);

        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }

    private vSysDate getSelectedDate() {

        vSysDate date = new vSysDate();
        if (spnOrderDate.getCount() > 0) {
            date = (vSysDate) spnOrderDate.getSelectedItem();
        }
        return date;
    }

    private vShop getShop() {
        vShop shop = new vShop();

        if (txtShop.getTag() != null) {
            String appShopId = Parse.toStr(txtShop.getTag());
            if (!appShopId.equalsIgnoreCase("0") && !TextUtils.isEmpty(appShopId)) {
                shop.setAppShopId(appShopId);
                shop.setShopName(txtShop.getText().toString());
            }
        }
        return shop;
    }

    private String getArea() {
        String area = "";
        if (!TextUtils.isEmpty(txtCity.getText().toString())) {
            area = txtCity.getText().toString();
        }
        return area;
    }

    private String getRoute() {
        String route = "";
        if (!TextUtils.isEmpty(txtRoute.getText().toString())) {
            route = txtRoute.getText().toString();
        }
        return route;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // http://stackoverflow.com/questions/6416706/easy-way-to-change-iterable-into-collection
        // http://stackoverflow.com/questions/9146224/arraylist-filter


        vRoute objvRoute;
        vLocality objvLocality;
        vShop objvShop;

        int i1 = adapterView.getId();
        if (i1 == R.id.ord_spn_agent) {
            final int agentId = Parse.toInt(txtCity.getTag());

            List<vRoute> tmpRoutes = Lists.newArrayList(Collections2.filter(lstRoutes, new Predicate<vRoute>() {
                @Override
                public boolean apply(vRoute input) {
                    return input.getAreaId() == agentId;
                }
            }));

            objvRoute = new vRoute();
            objvRoute.setRouteId(0);
            objvRoute.setRouteName("------SELECT ROUTE-------");

            tmpRoutes.add(0, objvRoute);

            setLastCityIndex(i);


        } else if (i1 == R.id.ord_spn_route) {
            final int routeId = Parse.toInt(txtRoute.getTag());

            List<vLocality> tmpLocality = Lists.newArrayList(Collections2.filter(lstLocalities, new Predicate<vLocality>() {
                @Override
                public boolean apply(vLocality input) {
                    return input.getRouteId() == routeId;
                }
            }));

            objvLocality = new vLocality();
            objvLocality.setLocalityId(0);
            objvLocality.setLocalityName("------SELECT LOCALITY------");

            tmpLocality.add(0, objvLocality);


        } else if (i1 == R.id.ord_spn_locality) {
            final int localityId = Parse.toInt(txtLocality.getTag());

            List<vShop> tmpShop = Lists.newArrayList(Collections2.filter(lstShops, new Predicate<vShop>() {
                @Override
                public boolean apply(vShop input) {
                    return input.getLocalityId() == localityId;
                }
            }));

            objvShop = new vShop();
            objvShop.setShopId(0);
            objvShop.setShopName("------SELECT SHOP------");

            tmpShop.add(0, objvShop);


        } else if (i1 == R.id.ord_spn_shop) {/* List<vProductTarget> target = new ProductTargetRepo().getAll(AppControl.getmEmployeeId(), shopAppId);
                if (target.size() != 0) {
                    Intent orderIntent = new Intent(SalesOrderActivity.this, ProductTargetActivity.class);

                    orderIntent.putExtra(Constant.SHOP_APP_ID, shopAppId);
                    orderIntent.putExtra(Constant.SHOP_NAME, shopName);
                    startActivity(orderIntent);
                }*/


        } else {
        }

    }

    private void updateShops(String appShopId) {

        if (txtLocality.getTag() == null) {
            return;
        }

        lstShops = new ShopRepo().getShopsAll(AppControl.getmEmployeeId());

        final int localityId = Parse.toInt(txtLocality.getTag());
        List<vShop> tmpShop = Lists.newArrayList(Collections2.filter(lstShops, new Predicate<vShop>() {
            @Override
            public boolean apply(vShop input) {
                return input.getLocalityId() == localityId;
            }
        }));
        vShop objvShop = new vShop();
        objvShop.setShopId(0);
        objvShop.setShopName(SELECT_SHOP);
        tmpShop.add(0, objvShop);

        spinnerShop.refreshData(tmpShop);

        txtShop.setText(SELECT_SHOP);
        txtShop.setTag(0);

        if (!TextUtils.isEmpty(appShopId)) {
            for (int i = 0; i < tmpShop.size(); i++) {
                if(appShopId.equalsIgnoreCase(tmpShop.get(i).getAppShopId())) {
                    txtShop.setText(tmpShop.get(i).getShopName());
                    txtShop.setTag(tmpShop.get(i).getAppShopId());
                    break;
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setLastCityIndex(int index) {

        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        //Gson gson = new Gson();

        ed.putInt(KEY_CITY, index);
        ed.apply();
    }

    private int getLastCityIndex() {
        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getInt(KEY_CITY, 0);
    }

    private void setLastRouteIndex(int index) {

        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putInt(KEY_ROUTE, index);
        ed.apply();
    }

    private int getRouteIndex() {
        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getInt(KEY_ROUTE, 0);
    }

    private void setLastLocalityIndex(int index) {

        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putInt(KEY_LOCALITY, index);
        ed.apply();
    }

    private int getLastLocalityIndex() {
        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getInt(KEY_LOCALITY, 0);
    }

    private void setLastShopIndex(int index) {

        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putInt(KEY_SHOP, index);
        ed.apply();
    }

    private int getLastShopIndex() {
        SharedPreferences sf = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getInt(KEY_SHOP, 0);
    }

    @Override
    public void onTouch() {
        isDirty = true;
    }

}
