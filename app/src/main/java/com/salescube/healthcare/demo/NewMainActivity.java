package com.salescube.healthcare.demo;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.activity.LatestAttendanceActivity;
import com.salescube.healthcare.demo.ctrl.MyViewPager;
import com.salescube.healthcare.demo.data.model.Dashboard;
import com.salescube.healthcare.demo.data.model.MyMenu;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.data.repo.NoteRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderPreviousRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.data.repo.TargetRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanDetailRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.SharePref;
import com.salescube.healthcare.demo.func.PreferenceUtils;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.Downloader;
import com.salescube.healthcare.demo.sysctrl.MyLocation;
import com.salescube.healthcare.demo.sysctrl.Updater;
import com.salescube.healthcare.demo.sysctrl.XProgressBar;
import com.salescube.healthcare.demo.view.vNote;
import com.salescube.healthcare.demo.view.vTarget;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_AUTO;
import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_MASTER_DOWNLOAD;
import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_REPORT_CHECK;

public class NewMainActivity extends BaseTransactionActivity implements View.OnClickListener {

    private LinearLayout tbl;
    //Navigation Menu
    private LinearLayout ll_Admin;
    private LinearLayout ll_DailyUpdate;
    private LinearLayout ll_MasterUpdate;
    private LinearLayout ll_PendingRecords;
    private LinearLayout ll_UpgradeRecords;
    private LinearLayout ll_AboutApp;
    private LinearLayout ll_Logout;
    private LinearLayout ll_dayClose;

    private Context context;
    NavigationView navigationView;
    DrawerLayout drawer;

    private TextView mEmployeeName;
    private TextView mTodayDate;
    private TextView mVersionCode;


    MyViewPager myAdapter;
    ViewPager viewPager;
    public List<MyMenu> menuList, showMenu;
    int pos, col0, row0, row, col, totalScreen;

    private final int ADMIN = 0;
    private final int SALES_ORDER = 1;
    private final int OTHER_WORK = 2;
    private final int LEAVE = 3;
    private final int TOUR_PLAN = 4;
    private final int MY_PLACE = 12;
    private final int DAILY_UPDATE = 5;
    private final int MASTER_UPDATE = 6;
    private final int ALL_ORDERS_REPORT = 7;
    private final int ORDER_SUMMARY = 8;
    private final int VERSION_UPDATE = 9;
    private final int PENDING_UPLOAD = 10;
    private final int COMPLAINTS = 11;
    //private final int ABOUT_APP = 12;


    private final int ATTENDANCE = 13;
    private final int SO_TODAYS_REPORT = 14;
    private final int SO_MONTH_REPORT = 15;
    private final int OPENING_STOCK_ENTRY = 16;
    private final int SO_ANALYTICS = 17;
    private final int ATTENDENCE_ACTION_TIME = 18;
    private final int SHOP_ORDER = 19;
    private final int PRODUCT_REPORT = 20;
    private final int DAY_ACHIEVEMENTS = 21;
    private final int MY_REPORT = 22;
    private final int MY_TEAM_REPORT = 23;
    private final int DAILY_SALES_REPORT = 24;
    private final int ATTENDENCE_REPORT = 25;
    private final int OTHER_WORK_REPORT = 26;
    private final int SHOP_PRODUCT_WISE_REPORT = 27;

    private final int MY_GPS = 101;

    private Double Lat;
    private Double longitute;


    private TextView lblNote;

    private TextView lblEmployeeName;
    private TextView lblTodayDate;
    private   SharePref pref;
    private String currentVersion, latestVersion;

    BarChart chart;
    ConstraintLayout mChartContainer;
    TableLayout mPlanContainer;

    String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        context = NewMainActivity.this;
        packageName = getApplicationContext().getPackageName();
        try {
            forceUpdate(false);
            initControl();
            initData();
            initListner();
            markAttendance(false);
            initMyChart();
           /* pref = new SharePref(getApplicationContext());
            String tokenStatus = pref.getTokenStatus();
            String regId1 = pref.getToken();

            if (tokenStatus.equalsIgnoreCase("1")){
                String regId = pref.getToken();

                if (!TextUtils.isEmpty(regId)){
                    updateFirebaseRegistrationId(regId);
                }

            }*/

        } catch (Exception e) {
            errMsg("While loading! " + e.getMessage(), e);
        }

    }

    private void updateFirebaseRegistrationId(String regId) {

        int empId = AppControl.getmEmployeeId();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> query = apiService.postFcmToken(empId,regId);

        UtilityFunc.showDialog(this, "", "");

        query.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {


                if (response.isSuccessful()) {

                    pref.setTokenStatus("2");


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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.notifications);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);


        setupBadge(mCartItemCount);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        menuItem.setVisible(false);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i1 = item.getItemId();
        if (i1 == R.id.notifications) {
            Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
            startActivity(i);
            //  msgShort("test");

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    public void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void initListner() {
        navMenuListner();
    }

    private void initControl() {
        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.icon_company);
        String appName = getResources().getString(R.string.app_name_title);
//        toolbar.setTitle(String.format("%s (%s)", appName,BuildConfig.VERSION_NAME));
        toolbar.setTitle(String.format("%s ", appName));
        setSupportActionBar(toolbar);
      //  toolbar.inflateMenu(R.menu.main_menu);

        drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                NewMainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                boolean isRefresh = PreferenceUtils.getBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, false, NewMainActivity.this);
                if(isRefresh){
                    initMyChart();
                    PreferenceUtils.putBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, false, NewMainActivity.this);
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);




        navigationView = findViewById(R.id.nav_view);
        mEmployeeName = navigationView.findViewById(R.id.nav_employee_name);
        mTodayDate = navigationView.findViewById(R.id.nav_today_date);
        mVersionCode = navigationView.findViewById(R.id.nav_version_code);

        lblEmployeeName = findViewById(R.id.home_lbl_employee_name);
        lblTodayDate = findViewById(R.id.home_lbl_today_date);

//        lblNote = findViewById(R.id.home_lbl_note);
        tbl = findViewById(R.id.new_menu_list);

        ll_Admin = navigationView.findViewById(R.id.update_action_admin);
        if (BuildConfig.DEBUG) {
            ll_Admin.setVisibility(View.VISIBLE);
        } else {
            ll_Admin.setVisibility(View.INVISIBLE);
        }

        ll_DailyUpdate = navigationView.findViewById(R.id.update_action_dailyUpdate);
        ll_MasterUpdate = navigationView.findViewById(R.id.update_action_masterUpdate);
        ll_PendingRecords = navigationView.findViewById(R.id.update_action_pendingRecords);
        ll_UpgradeRecords = navigationView.findViewById(R.id.update_action_upgradeApp);
        ll_AboutApp = navigationView.findViewById(R.id.update_action_aboutApp);
        ll_Logout = navigationView.findViewById(R.id.logout);
        ll_dayClose = navigationView.findViewById(R.id.day_close);


        menuList = new ArrayList<>();

        if (AppControl.getAppRole().equalsIgnoreCase("ASM")) {
            menuList = loadASMMenu();
        }else if(AppControl.getAppRole().equalsIgnoreCase("RSM")){
            menuList = loadASMMenu();
        }else if(AppControl.getAppRole().equalsIgnoreCase("ZSM")){
            menuList = loadASMMenu();
        }else if(AppControl.getAppRole().equalsIgnoreCase("NSM")){
            menuList = loadASMMenu();
        } else {
            menuList = loadSOMenu();
        }
        setButton(menuList);

        mChartContainer = findViewById(R.id.graph_container);
        mPlanContainer = findViewById(R.id.plane_container);
        chart = findViewById(R.id.my_chart);

    }

    private void setButton(List<MyMenu> showMenu) {

        //Max Count of row And Column from List
        if (showMenu.size() > 0) {
            row0 = showMenu.get(0).getRowNo();
            col0 = showMenu.get(0).getColumnNo();
            for (int s = 0; s < showMenu.size(); s++) {
                row = showMenu.get(s).getRowNo();
                col = showMenu.get(s).getColumnNo();
                if (row > row0) {
                    row0 = row;
                }
                if (col > col0) {
                    col0 = col;
                }
            }
        }


        Button btn = null;
        //Set Button in Square shape
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int w = display.getWidth();
        int width = (int) Math.round((w) / 3);
        int height = display.getHeight();
        Log.d("sizeee", "width :" + (width) + "\tHeight : " + (height));
//        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1,-1,1.0f);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(0, -1);
        ll.weight = 1f;

        //For Rows
        for (int r = 1; r <= row0; r++) {
//          To Create Row Dynamically
            LinearLayout layoutParams = new LinearLayout(context);
            LinearLayout.LayoutParams ll_row = new LinearLayout.LayoutParams(-1, -1);
            layoutParams.setLayoutParams(ll_row);
            layoutParams.setGravity(Gravity.CENTER);
            //For Column
            for (int c = 1; c <= col0; c++) {

                btn = new Button(context);
                btn.setLayoutParams(ll);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
                params.width = width;
                params.height = width;
                params.setMargins(2, 2, 2, 2);
                btn.setTextColor(Color.BLACK);
                btn.setBackground(context.getResources().getDrawable(R.drawable.button_white));
//                btn.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                btn.setVisibility(View.INVISIBLE);
                btn.setLayoutParams(params);
//                btn.setOnClickListener(Fragment1.this);

                for (int m = 0; m < showMenu.size(); m++) {
                    int n = showMenu.get(m).getRowNo();
                    int cm = showMenu.get(m).getColumnNo();
                    if (r == n && c == cm) {
                        btn.setGravity(Gravity.CENTER);
                        btn.setVisibility(View.VISIBLE);
                        Log.d("Menu", "\n" + showMenu.get(m).getMenuName());
                        btn.setId(showMenu.get(m).getMenuId());
                        btn.setText(showMenu.get(m).getMenuName());
//                        btn.setPadding(0,20,0,20);

                        try {
                            btn.setCompoundDrawablesWithIntrinsicBounds(null, getDrwable(context, showMenu.get(m).getIconPath()), null, null);
                        } catch (Exception e) {
                            Log.e("NewMainAcitivty", "icon not found: " + showMenu.get(m).getIconPath(), e);
                        }

                        btn.setTextAppearance(context, R.style.my_text);
                        btn.setGravity(Gravity.CENTER);
                        btn.setCompoundDrawablePadding(5);
                        btn.setPadding(8, 20, 8, 20);

                        if (!showMenu.get(m).isVisiable()) {
                            btn.setVisibility(View.INVISIBLE);
                        }
                        if (showMenu.get(m).isEnable()) {
//                            btn.setBackgroundColor(context.getResources().getColor(R.color.isEnabled));
                        }
                        if (showMenu.get(m).isVisiable() && showMenu.get(m).isEnable()) {
//                            btn.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        }

                        final Button finalBtn = btn;
                        final int finalM = m;
                        btn.setOnClickListener(this);
                    }
                }

                layoutParams.addView(btn);
            }// For Column
            tbl.addView(layoutParams);
//                }
        }//For Row
    }

    private Drawable getDrwable(Context context, String iconPath) {
        int resourceId = context.getResources().getIdentifier(iconPath, "drawable", context.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    private void navMenuListner() {
        ll_AboutApp.setOnClickListener(this);
        ll_DailyUpdate.setOnClickListener(this);
        ll_MasterUpdate.setOnClickListener(this);
        ll_UpgradeRecords.setOnClickListener(this);
        ll_PendingRecords.setOnClickListener(this);
        ll_Admin.setOnClickListener(this);
        ll_Logout.setOnClickListener(this);
        ll_dayClose.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            finish();
//            super.onBackPressed();
        }
    }

    private List<MyMenu> loadSOMenu() {
        MyMenu menu;

        menu = new MyMenu();
        menu.setMenuId(SALES_ORDER);
        menu.setMenuName("Sales\nOrder");
        menu.setIconPath("icon_new_sales_order");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(SHOP_PRODUCT_WISE_REPORT);
        menu.setMenuName("Modify\nOrder");
        menu.setIconPath("icon_sales_order");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(OTHER_WORK);
        menu.setMenuName("Other\nWork");
        menu.setIconPath("new_other_work");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);


        menu = new MyMenu();
        menu.setMenuId(LEAVE);
        menu.setMenuName("Leave");
        menu.setIconPath("new_leave");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(TOUR_PLAN);
        menu.setMenuName("Tour Plan");
        menu.setIconPath("new_location");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(COMPLAINTS);
        menu.setMenuName("Complaints");
        menu.setIconPath("icon_complaint_box");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);


        menu = new MyMenu();

        menu.setMenuId(SO_ANALYTICS);
        menu.setMenuName("My\nPerformance");
        menu.setIconPath("icon_screen_graph");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(MY_REPORT);
        menu.setMenuName("My\nReport");
        menu.setIconPath("new_report");
        menu.setMenuType("Report");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(MY_PLACE);
        menu.setMenuName("SS Agent\nPlace");
        menu.setIconPath("new_location");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        return menuList;
    }

    public List<MyMenu> loadASMMenu() {
        MyMenu menu;

        menu = new MyMenu();
        menu.setMenuId(SALES_ORDER);
        menu.setMenuName("Sales\nOrder");
        menu.setIconPath("icon_new_sales_order");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(SHOP_PRODUCT_WISE_REPORT);
        menu.setMenuName("Modify\nOrder");
        menu.setIconPath("icon_sales_order");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(OTHER_WORK);
        menu.setMenuName("Other\nWork");
        menu.setIconPath("new_other_work");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(1);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);


        menu = new MyMenu();
        menu.setMenuId(LEAVE);
        menu.setMenuName("Leave");
        menu.setIconPath("new_leave");
        menu.setMenuType("Sales");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(false);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(TOUR_PLAN);
        menu.setMenuName("Tour Plan");
        menu.setIconPath("new_location");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(COMPLAINTS);
        menu.setMenuName("Complaints");
        menu.setIconPath("icon_complaint_box");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(2);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(SO_ANALYTICS);
        menu.setMenuName("My\nPerformance");
        menu.setIconPath("icon_screen_graph");
        menu.setMenuType("Leave");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(1);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        menu = new MyMenu();
        menu.setMenuId(MY_REPORT);
        menu.setMenuName("My\nReport");
        menu.setIconPath("new_report");
        menu.setMenuType("Report");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(2);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);


        menu = new MyMenu();
        menu.setMenuId(MY_TEAM_REPORT);
        menu.setMenuName("My Team\nReport");
        menu.setIconPath("new_my_team_report");
        menu.setMenuType("Report");
        menu.setScreenNo(1);
        menu.setRowNo(3);
        menu.setColumnNo(3);
        menu.setVisiable(true);
        menu.setEnable(true);
        menuList.add(menu);

        return menuList;
    }

    private boolean isGPSEnabled() {
        boolean enableGPS = UtilityFunc.isGPSEnabled(true, NewMainActivity.this);
        if (!enableGPS) {
            return false;
        }
        return true;
    }

    private boolean isGPSMasterEnabled() {

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, NewMainActivity.this);
        if (!enableGPS) {
            return false;
        }

        if (!hasMasterFound()) {
            return false;
        }
        return true;

    }

    private void markAttendance(boolean userInvoke) {

        final Date appDate = AppControl.getTodayDate();
        final Date phoneDate = DateFunc.getTodaysDate();

        if (DateFunc.isSameDate(appDate, phoneDate)) {
            if (userInvoke) {
                msgShort("Thank You! Attendance already done!");
            }
            return;
        }

        if (phoneDate.before(appDate)) {
            msgShort("Device date error! Invalid system date!");
            return;
        }

        addDay(appDate, phoneDate);
    }

    private void addDay(final Date oldDate, final Date newDate) {

        SysDateRepo sysDateRepo = new SysDateRepo();

        try {
            dayOpenProc(oldDate, newDate);
        } catch (Exception e) {
            msgShort("Failed to update previous orders!");
        }

        sysDateRepo.deleteAll(AppControl.getmEmployeeId(), newDate);
        sysDateRepo.insert(AppControl.getmEmployeeId(), newDate);

        AppControl.refreshDate();
        lblTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));
        mTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));

        new TableInfoRepo().deletePostedData();
        Toast.makeText(NewMainActivity.this, "*** Thank You! Have a Great Day ***", Toast.LENGTH_LONG).show();

//        try {
//            captureLocation("", EVENT_ATTENDANCE);
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }
    }

    private void dayOpenProc(Date orderDate, Date newDate) {
        new SalesOrderPreviousRepo().copyOrders(AppControl.getmEmployeeId(), orderDate);
        new TargetRepo().copyYesterdayAch(AppControl.getmEmployeeId(), orderDate, newDate);
    }

    private void downloadData() {

        TableInfoRepo tableData = new TableInfoRepo();

        // TODO: Check if there is no upload pending

        boolean isUploadPending = new ShopRepo().isShopUploadPending();

        if (isUploadPending) {
            Alert("Upload Pending!", "Shop not uploaded, please check HO Report & try again later.");
            return;
        }

        boolean dataExist = tableData.hasDataExist(AppControl.getmEmployeeId());
        if (dataExist) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
            builder.setTitle("Confirm Download!");
            builder.setMessage("Please select download type Complete or Update.");

            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setNeutralButton("FULL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    try {
                        new Downloader(NewMainActivity.this, msgHandler, true).execute("");
                    } catch (Exception ex) {
                        errMsg("While downloading", ex);
                    }
                }
            });

            builder.setNegativeButton("PENDING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    try {
                        new Downloader(NewMainActivity.this, msgHandler, false).execute("");
                    } catch (Exception ex) {
                        errMsg("While downloading", ex);
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);

        } else {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            try {
                new Downloader(NewMainActivity.this, msgHandler, true).execute("");
            } catch (Exception ex) {
                errMsg("While downloading", ex);
            }
        }
    }

    private void initData() {

//        loadNotes();
        mVersionCode.setText("Version : " + UtilityFunc.getAppVersion(this));
        mEmployeeName.setText(AppControl.getEmployeeName());
        mTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));

        lblEmployeeName.setText(AppControl.getEmployeeName());
        lblTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));

//         new Updater(updateHandler).execute("");

    }

    private void loadNotes() {

        NoteRepo objNote = new NoteRepo();
        List<vNote> notes = objNote.getTodayNotes(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        String notesStr = "Note: ";

        for (vNote note : notes) {
            notesStr += note.getNoteContent();
        }

//        lblNote.setText(notesStr);
//        lblNote.setMovementMethod(new ScrollingMovementMethod());
    }

    private boolean hasMasterFound() {

        TableInfoRepo tb = new TableInfoRepo();
        if (!tb.isEligibleForOrder(AppControl.getmEmployeeId())) {
            Alert("Data Not Found!", "Please do 'Master Updates' and try again.");
            return false;
        }

        if (!tb.hasDayOpen(AppControl.getmEmployeeId())) {
            Alert("Dates not found!", "Please mark attendance");
            return false;
        }

        return true;
    }

    private void UpgadeVersion() {

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + " /";
        final String fileName = "PMA.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }

        String url = "http://app.pitambari.com/pma.apk";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Pitambari Mobile App New Update");
        request.setTitle("PMA Upgrade");
        request.setDestinationUri(uri);

        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                install.setDataAndType(uri,manager.getMimeTypeForDownloadedFile(downloadId));
//                startActivity(install);

                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + fileName)), "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri, manager.getMimeTypeForDownloadedFile(downloadId));
                startActivity(install);

                unregisterReceiver(this);
                finish();
            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert = new AlertDialog.Builder(NewMainActivity.this);
            alert.setTitle("Confirm!");
            alert.setMessage("Do you want to exit ?");

            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            alert.create();
            alert.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    protected Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.obj == null) {
                return;
            }

            if (msg.arg1 == Constant.MessageType.Toast) {
                msgShort((String) msg.obj);
            }
            if (msg.arg1 == Constant.MessageType.Alert) {
                List<String> appMsg = null;

                try {
                    appMsg = (ArrayList<String>) msg.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Alert(appMsg.get(0), appMsg.get(1));
            }

            loadNotes();
        }
    };


    @Override
    public void onClick(View v) {

        int i1 = v.getId();
        if (i1 == R.id.update_action_masterUpdate) {
            if (isGPSEnabled()) {
                // doManualUpload("", EVENT_MASTER_DOWNLOAD);

                try {
                    downloadData();
                } catch (Exception e) {
                    errMsg("While downloading data!", e);
                }
            }

        } else if (i1 == R.id.update_action_pendingRecords) {
            if (isGPSEnabled()) {

                try {

                    doManualUpload("", EVENT_REPORT_CHECK);

                    Intent intent = new Intent(context, HoReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        } else if (i1 == R.id.update_action_upgradeApp) {
            forceUpdate(true);


        } else if (i1 == R.id.update_action_dailyUpdate) {
            if (isGPSEnabled()) {

                // doManualUpload("", AppEvent.EVENT_DAY_UPDATE);

                try {
                    new Updater(context, updateHandler).execute("");
                } catch (Exception ex) {
                    errMsg("Update " + ex.getMessage(), ex);
                }
            }

        } else if (i1 == R.id.update_action_aboutApp) {
            Alert("Version Info", "You are using version " + UtilityFunc.getAppVersion(this));

        } else if (i1 == R.id.update_action_admin) {
            try {
                Intent orderIntent = new Intent(context, AdminToolsActivity.class);
                startActivity(orderIntent);
            } catch (Exception ex) {
                errMsg("While sales order!", ex);
            }

        } else if (i1 == R.id.logout) {
            moveTaskToBack(true);
            finish();

        } else if (i1 == R.id.day_close) {
            Boolean dayOutDone = new SharePref(getApplicationContext()).isDayOutDone();

            if (dayOutDone == true) {
                msgShort("Day close already marked");
                return;
            }

            Intent attendanceActivity = new Intent(context, LatestAttendanceActivity.class);
            startActivity(attendanceActivity);


        } else if (i1 == SHOP_PRODUCT_WISE_REPORT) {
            if (isGPSMasterEnabled()) {

                try {
                    Intent orderIntent = new Intent(context, TodayShopProductWiseReportActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }

            }

        } else if (i1 == ADMIN) {
            try {
                Intent orderIntent = new Intent(context, AdminToolsActivity.class);
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While sales order!", ex);
            }

        } else if (i1 == SALES_ORDER) {
            if (isGPSMasterEnabled()) {

                //doManualUpload("", EVENT_AUTO);

                try {

                    Intent orderIntent = new Intent(context, SalesOrderActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }

            }


        } else if (i1 == OTHER_WORK) {
            if (isGPSEnabled()) {

                // doManualUpload("", EVENT_AUTO);

                try {
                    Intent toolsIntent = new Intent(context, OtherWorkActivity.class);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    Logger.e("Other work", ex);
                }
            }

        } else if (i1 == LEAVE) {
            if (isGPSEnabled()) {

                // doManualUpload("", EVENT_AUTO);

                try {
                    Intent toolsIntent = new Intent(context, LeaveActivity.class);
                    SysDate sysDate = new SysDate();
                    toolsIntent.putExtra("SysData", sysDate);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    Logger.e("Other work", ex);
                }
            }

        } else if (i1 == MY_PLACE) {
            if (isGPSEnabled()) {
                try {
                    Intent toolsIntent = new Intent(context, MyPlaceActivity.class);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    Logger.e("Leave", ex);
                }
            }

        } else if (i1 == ALL_ORDERS_REPORT) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent toolsIntent = new Intent(context, TodaysOrderActivity.class);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    Logger.e("Report todays order", ex);
                }
            }

        } else if (i1 == ORDER_SUMMARY) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent toolsIntent = new Intent(context, TodaysSummaryReport.class);
                    startActivity(toolsIntent);
                } catch (Exception ex) {
                    Logger.e("Report todays summary", ex);
                }
            }

        } else if (i1 == COMPLAINTS) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent intent = new Intent(context, ComplaintActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        } else if (i1 == ATTENDANCE) {
            if (isGPSEnabled()) {
                try {
                    Intent intent = new Intent(context, ComplaintActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }


        } else if (i1 == SO_TODAYS_REPORT) {
            if (isGPSEnabled()) {
                try {
                    Intent intent = new Intent(context, SoDayReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        } else if (i1 == SO_MONTH_REPORT) {
            if (isGPSEnabled()) {
                try {
                    Intent intent = new Intent(context, SoMonthReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        } else if (i1 == SO_ANALYTICS) {
            try {
                Intent intent = new Intent(context, SoAnalyticReportActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }

        } else if (i1 == ATTENDENCE_ACTION_TIME) {
            if (isGPSEnabled()) {
                try {
                    Intent intent = new Intent(context, AttendanceActivity2.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }


        } else if (i1 == SHOP_ORDER) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, ShopWiseOrderActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While shop wise sales order!", ex);
                }
            }

        } else if (i1 == PRODUCT_REPORT) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, ProductWiseOrderActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While product wise sales order!", ex);
                }
            }

        } else if (i1 == DAY_ACHIEVEMENTS) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, DayWiseAchievementActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
            }

        } else if (i1 == MY_REPORT) {//                if (isGPSMasterEnabled()) {
            try {
                Intent orderIntent = new Intent(context, MySalesReportActivity.class);
                orderIntent.putExtra("reportType", "MyReport");
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While day wise sales order!", ex);
            }
//                }

        } else if (i1 == MY_TEAM_REPORT) {//                if (isGPSMasterEnabled()) {
            try {
                Intent orderIntent = new Intent(context, MySalesReportActivity.class);
                orderIntent.putExtra("reportType", "MyTeam");
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While day wise sales order!", ex);
            }
//                }

        } else if (i1 == DAILY_SALES_REPORT) {//                if (isGPSMasterEnabled()) {
            try {
                Intent orderIntent = new Intent(context, SOProductWiseAchievement.class);
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While day wise sales order!", ex);
            }
//                }

        } else if (i1 == ATTENDENCE_REPORT) {//                if (isGPSMasterEnabled()) {
            try {
                Intent orderIntent = new Intent(context, AttendenceReportActivity.class);
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While day wise sales order!", ex);
            }
//                }

        } else if (i1 == OTHER_WORK_REPORT) {//                if (isGPSMasterEnabled()) {
            try {
                Intent orderIntent = new Intent(context, SOOtherWorkReportActivity.class);
                startActivity(orderIntent);
            } catch (Exception ex) {
                Logger.e("While day wise sales order!", ex);
            }
//                }

        } else if (i1 == TOUR_PLAN) {
            if (isGPSMasterEnabled()) {
                try {
                    Intent i = new Intent(this, TourPlanActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        }
    }


    public void forceUpdate(boolean isShowAlert) {

        if (BuildConfig.DEBUG) {
            return;
        }

        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        if (isShowAlert) {
            new GetLatestVersionOnForceUpgrade().execute();
        } else {
            new GetLatestVersionOnPageLoad().execute();
        }
    }


    private class GetLatestVersionOnPageLoad extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName).get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();

        }


        @Override

        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }
            } else
                // background.start();
                super.onPostExecute(jsonObject);
        }
    }

    private class GetLatestVersionOnForceUpgrade extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName).get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();

        }


        @Override

        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                } else {
                    Alert("No Update!", "Latest version already installed!");
                }
            } else
                // background.start();
                super.onPostExecute(jsonObject);
        }
    }


    private void showUpdateDialog() {
        Alert1("Update Available", "A new version of App is available. Please update to version");
    }


    protected void Alert1(String title, String msg) {

        AlertDialog.Builder alert = new AlertDialog.Builder(NewMainActivity.this)

                .setTitle(title)

                .setMessage(msg);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse

                        ("https://play.google.com/store/apps/details?id=" + packageName)));

            }

        });

        alert.create();
        alert.show();

    }


    private void initMyChart() {

        if (!UtilityFunc.isNetworkConnected(this)) {
            fillBarGraph(getOfflineDashboard());
            return;
        }

        UtilityFunc.showDialog(this, "", "");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Dashboard> query = apiService.getDashboard(AppControl.getmEmployeeId());

        query.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (response.isSuccessful()) {
                    try {
                        Dashboard objList = response.body();
                        mPlanContainer.setVisibility(View.GONE);
                        mChartContainer.setVisibility(View.VISIBLE);
                        objList.setDayColor(null);
                        if(objList != null  && TextUtils.isEmpty(objList.getDayColor())) {
                            objList = getOfflineDashboard();
                        }
                        PreferenceUtils.putString(Constant.PREF_DAY_COLOR, objList.getDayColor(), NewMainActivity.this);
                        PreferenceUtils.putString(Constant.PREF_MONTH_COLOR, objList.getMonthColor(), NewMainActivity.this);
                        fillBarGraph(objList);
                        UtilityFunc.dismissDialog();
                    } catch (Exception e) {
                        fillBarGraph(getOfflineDashboard());
                        UtilityFunc.dismissDialog();
                        return;
                    }
                } else {
                    fillBarGraph(getOfflineDashboard());
                    UtilityFunc.dismissDialog();
                }
                UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                fillBarGraph(getOfflineDashboard());
                UtilityFunc.dismissDialog();
            }
        });

    }

    private Dashboard getOfflineDashboard(){
        mPlanContainer.setVisibility(View.GONE);
        mChartContainer.setVisibility(View.VISIBLE);
        TargetRepo targetRepo = new TargetRepo();
        vTarget target = targetRepo.getTodaysTarget(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        Dashboard dashboard = new Dashboard();
        dashboard.setDayAchievement(target.getDayAchievementValue());
        dashboard.setMonthAchievement(target.getMonthAchievementValue());
        dashboard.setDayTarget(target.getDayTargetValue());
        dashboard.setMonthTarget(target.getMonthTargetValue());

        String dayTitle = "Day Shortfall", monthTitle = "Month Shortfall";
        if(target.getDayTargetValue() - target.getDayAchievementValue() > 0){
            dayTitle = "Day Shortfall";
        }else if(target.getDayTargetValue() - target.getDayAchievementValue() < 0){
            dayTitle = "Day Excess Achievement";
        }

        if(target.getMonthTargetValue() - target.getMonthAchievementValue() > 0){
            monthTitle = "Month Shortfall";
        }else if(target.getMonthTargetValue() - target.getMonthAchievementValue() < 0){
            monthTitle = "Month Excess Achievement";
        }

        dashboard.setDayTitle(dayTitle);
        dashboard.setMonthTitle(monthTitle);

        String daycolor =  PreferenceUtils.getString(Constant.PREF_DAY_COLOR, "#dc961a", NewMainActivity.this);
        String monthcolor =  PreferenceUtils.getString(Constant.PREF_MONTH_COLOR, "#33a1c9", NewMainActivity.this);

        dashboard.setDayColor(daycolor);
        dashboard.setMonthColor(monthcolor);

        return dashboard;
    }

    public void fillBarGraph(Dashboard chartData) {

        LinearLayout dayContainer = findViewById(R.id.ll_title1);
        LinearLayout monthContainer = findViewById(R.id.linearLayout);
        TextView txtDayTitle = findViewById(R.id.txt_day_title);
        TextView txtDayValue = findViewById(R.id.txt_day_value);
        TextView txtMonthTitle = findViewById(R.id.txt_month_title);
        TextView txtMonthValue = findViewById(R.id.txt_month_value);

       /* txtDayTitle.setTextColor(Color.parseColor(chartData.getDayColor()));
        txtDayValue.setTextColor(Color.parseColor(chartData.getDayColor()));
        txtMonthTitle.setTextColor(Color.parseColor(chartData.getMonthColor()));
        txtMonthValue.setTextColor(Color.parseColor(chartData.getMonthColor()));*/
        dayContainer.setBackgroundColor(Color.parseColor(chartData.getDayColor()));
        monthContainer.setBackgroundColor(Color.parseColor(chartData.getMonthColor()));

        int labelCount = 5;
        int maxYAxix = 100;

        txtDayTitle.setText(chartData.getDayTitle());
        txtDayValue.setText("₹ " + String.valueOf(Math.round(Math.abs(chartData.getDayTarget() - chartData.getDayAchievement()))));

        txtMonthTitle.setText(chartData.getMonthTitle());
        txtMonthValue.setText("₹ " + String.valueOf(Math.round(Math.abs(chartData.getMonthTarget() - chartData.getMonthAchievement()))));

        chart.setOnClickListener(null);
        chart.setOnTouchListener(null);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(true);

        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);
        chart.getAxisRight().setEnabled(false);


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Day");
        xAxisLabel.add("Month");

        final XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = Math.round(value);
                return xAxisLabel.get(v);
            }
        });

        xAxis.setLabelCount(2);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(labelCount, true);
        yAxis.setDrawLabels(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawTopYLabelEntry(true);
        yAxis.setCenterAxisLabels(true);
        yAxis.setDrawAxisLine(true);
        yAxis.setAxisMaximum(maxYAxix);
        yAxis.setAxisMinimum(0);

        chart.setExtraBottomOffset(10f);
        chart.setExtraRightOffset(30f);
        chart.getLegend().setEnabled(false);


        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<BarEntry> values1 = new ArrayList<>();

//        values.add(new BarEntry(0, 100));
//        values.add(new BarEntry(1, 100));
        double dayPercent = 100;
        if (chartData.getDayTarget() == 0 && chartData.getDayAchievement() == 0) {
            dayPercent = 0;
        }
        if (chartData.getDayTarget() != 0) {
            dayPercent = (chartData.getDayAchievement() * 100) / chartData.getDayTarget();
            if (dayPercent > 100) {
                dayPercent = 100;
            }
        }

        double monthPercent = 100;
        if (chartData.getMonthTarget() == 0 && chartData.getMonthAchievement() == 0) {
            monthPercent = 0;
        }
        if (chartData.getMonthTarget() != 0) {
            monthPercent = (chartData.getMonthAchievement() * 100) / chartData.getMonthTarget();
            if (monthPercent > 100) {
                monthPercent = 100;
            }
        }
        values1.add(new BarEntry(0, (float) dayPercent));
        values1.add(new BarEntry(1, (float) monthPercent));


        BarDataSet targetDs, achDs;

        int colorShadow = getResources().getColor(R.color.target);

        int[] getColor = new int[]{colorShadow, colorShadow};
        int[] getColor1 = new int[]{Color.parseColor(chartData.getDayColor()), Color.parseColor(chartData.getMonthColor())};

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            targetDs = (BarDataSet) chart.getData().getDataSetByIndex(0);
            targetDs.setValues(values);

            achDs = (BarDataSet) chart.getData().getDataSetByIndex(1);
            achDs.setValues(values1);

            chart.getData().notifyDataChanged();

        } else {

            targetDs = new BarDataSet(values, "");
            targetDs.setColors(getColor);
            targetDs.setDrawIcons(false);
            targetDs.setDrawValues(false);

            achDs = new BarDataSet(values1, "");
            achDs.setColors(getColor1);
            achDs.setDrawIcons(false);

            BarData data = new BarData(targetDs, achDs);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextSize(10f);
            data.setBarWidth(0.50f);
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        chart.animateY(2000);
        chart.notifyDataSetChanged();
        chart.setFitBars(true);
        chart.invalidate();
    }

    private class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###"); // use no decimals
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            // write your logic here
            if (value < 10) return "";

            return mFormat.format(value) + "%"; // in case you want to add percent
        }
        /*@Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value)+"";
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRefresh = PreferenceUtils.getBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, false, this);
        if(isRefresh){
            initMyChart();
            PreferenceUtils.putBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, false, this);
        }
    }








}

