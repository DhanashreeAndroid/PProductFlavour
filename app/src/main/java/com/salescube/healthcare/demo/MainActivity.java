package com.salescube.healthcare.demo;

public class MainActivity extends BaseTransactionActivity {

//    private LinearLayout tbl;
//    private Context context;
//
//    private final int ADMIN = 0;
//    private final int SALES_ORDER = 1;
//    private final int OTHER_WORK = 2;
//    private final int LEAVE = 3;
//    private final int MY_PLACE = 4;
//    private final int DAILY_UPDATE = 5;
//    private final int MASTER_UPDATE = 6;
//    private final int ALL_ORDERS_REPORT = 7;
//    private final int ORDER_SUMMARY = 8;
//    private final int VERSION_UPDATE = 9;
//    private final int PENDING_UPLOAD = 10;
//    private final int COMPLAINTS = 11;
//    private final int ABOUT_APP = 12;
//
//
//    private final int ATTENDANCE = 13;
//    private final int SO_TODAYS_REPORT = 14;
//    private final int SO_MONTH_REPORT = 15;
//    private final int OPENING_STOCK_ENTRY = 16;
//    private final int SO_ANALYTICS = 17;
//    private final int ATTENDENCE_ACTION_TIME = 18;
//    private final int SHOP_ORDER = 19;
//    private final int PRODUCT_REPORT = 20;
//    private final int DAY_ACHIEVEMENTS = 21;
//    private final int MY_REPORT = 22;
//    private final int MY_TEAM_REPORT = 23;
//    private final int DAILY_SALES_REPORT = 24;
//    private final int ATTENDENCE_REPORT = 25;
//    private final int OTHER_WORK_REPORT = 26;
//    private final int SHOP_PRODUCT_WISE_REPORT = 27;
//
//    private final int MY_GPS = 101;
//
//
//    private TextView lblNote;
//
//    private TextView lblEmployeeName;
//    private TextView lblTodayDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        context = MainActivity.this;
//
//        try {
//            initControl();
//            initData();
//            markAttendance(false);
//        } catch (Exception e) {
//            errMsg("While loading! " + e.getMessage(), e);
//        }
//
//        if (AppControl.getAppRole().equalsIgnoreCase("ASM")) {
//            fillASMMenu();
//        }else {
//            fillSoMenu();
//        }
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.update_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId())
//        {
//            case R.id.update_action_masterUpdate:
//                            if (isGPSEnabled()) {
//                                doManualUpload("", EVENT_MASTER_DOWNLOAD);
//
//                                try {
//                                    downloadData();
//                                } catch (Exception e) {
//                                    errMsg("While downloading data!", e);
//                                }
//                            }
//                            break;
//
//            case R.id.update_action_pendingLogs:
//
//                if (isGPSEnabled()) {
//                    try {
//
//                        doManualUpload("", EVENT_REPORT_CHECK);
//
//                        Intent intent = new Intent(MainActivity.this, HoReportActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//                break;
//
//            case R.id.update_action_upgradeApp:
//
//                if (isGPSEnabled()) {
//                    try {
//                        ApkInstaller installer = new ApkInstaller();
//                        installer.setContext(MainActivity.this);
//                        installer.setHandler(msgHandler);
//                        installer.execute("");
//                    } catch (Exception e) {
//                        msgShort(e.getMessage());
//                    }
//                }
//                break;
//            case R.id.update_action_dailyUpdate:
//                if (isGPSEnabled()) {
//                    doManualUpload("", EVENT_DAY_UPDATE);
//
//                    try {
//                        new Updater(MainActivity.this, updateHandler).execute("");
//                    } catch (Exception ex) {
//                        errMsg("Update " + ex.getMessage(), ex);
//                    }
//                }
//                break;
//            case R.id.update_action_aboutApp:
//                Alert("Version Info", "You are using version " + BuildConfig.VERSION_NAME);
//                break;
//            case R.id.update_action_admin:
//                try {
//                    Intent orderIntent = new Intent(MainActivity.this, AdminToolsActivity.class);
//                    startActivity(orderIntent);
//                } catch (Exception ex) {
//                    errMsg("While sales order!", ex);
//                }
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem admin = menu.findItem(R.id.update_action_admin);
//        if (BuildConfig.DEBUG) {
//            admin.setVisible(true);
//        }else {
//            admin.setVisible(false);
//        }
//        return true;
//    }
//
//    private void initControl() {
//
//        tbl = findViewById(R.id.menu_layout);
//
//        lblEmployeeName = findViewById(R.id.home_lbl_employee_name);
//        lblTodayDate = findViewById(R.id.home_lbl_today_date);
//
//        lblNote = findViewById(R.id.home_lbl_note);
//    }
//
//    private void fillSoMenu() {
//
//
//        LinearLayout row;
//        LinearLayout.LayoutParams params;
//
//        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        row = new LinearLayout(context);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setLayoutParams(params);
//
//        addButton(row, SALES_ORDER, "Sales Order", "icon_sales_order", true);
//        addButton(row, OTHER_WORK, "Other Work", "icon_group", true);
//        addButton(row, LEAVE, "Leave", "icon_leave", true);
//        addButton(row, MY_PLACE, "SS\\Agent\nPlace", "icon_location", true);
//
//        tbl.addView(row);
//
//        row = new LinearLayout(context);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setLayoutParams(params);
//
//        addButton(row, SO_ANALYTICS, "My\nPerformance", "icon_screen_graph", true);
//        addButton(row, COMPLAINTS, "Complaints", "icon_complaint_box", true);
//        addButton(row, MY_REPORT, "Report ", "icon_report", true);
//        addButton(row, ORDER_SUMMARY, "Orders\nSummary", "icon_screen_graph", false);
//
//        tbl.addView(row);
//
////        row = new LinearLayout(context);
////        row.setOrientation(LinearLayout.HORIZONTAL);
////        row.setLayoutParams(params);
////
////
////
////        addButton(row, SHOP_ORDER, "Shop wise\nOrder","icon_sales_order",false);
////        addButton(row, PRODUCT_REPORT, "Product Wise\nReport", "icon_report", false);
////
////        addButton(row, DAY_ACHIEVEMENTS, "Day Wise\nAchievement ", "icon_report", false);
////
//////
//////        if (BuildConfig.DEBUG) {
//////            addButton(row, ADMIN, "Admin", "icon_sys_info", true);
//////        }else {
//////            addButton(row, ADMIN, "Admin", "icon_sys_info", false);
//////        }
////        addButton(row, ORDER_SUMMARY, "Orders\nSummary", "icon_screen_graph", false);
//
////        tbl.addView(row);
//
//
//    }
//
//    private void fillASMMenu() {
//
//
//        LinearLayout row;
//        LinearLayout.LayoutParams params;
//
//        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        // row 1
//
//        row = new LinearLayout(context);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setLayoutParams(params);
//
////        addButton(row, ATTENDANCE, "Attendance", "icon_men", true);
//        addButton(row, SALES_ORDER, "Sales Order", "icon_sales_order", true);
//        addButton(row, SHOP_PRODUCT_WISE_REPORT, "Modify\nOrder", "icon_sales_order", true);
//        addButton(row, OTHER_WORK, "Other Work", "icon_group", true);
//        addButton(row, LEAVE, "Leave", "icon_leave", true);
//
//
//        tbl.addView(row);
//
//        // row 2
//
//        row = new LinearLayout(context);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setLayoutParams(params);
//
//        addButton(row, ORDER_SUMMARY, "Orders\nSummary", "icon_screen_graph", true);
//        addButton(row, MY_PLACE, "SS\\Agent\nPlace", "icon_location", true);
//        addButton(row, COMPLAINTS, "Complaints", "icon_complaint_box", true);
//        addButton(row, SO_ANALYTICS, "My\nPerformance", "icon_screen_graph", true);
////        addButton(row, SO_TODAYS_REPORT, "SO Report", "icon_report", true);
//        tbl.addView(row);
//
//        // row 3
//
//        row = new LinearLayout(context);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setLayoutParams(params);
//
////        addButton(row, SO_MONTH_REPORT, "SO Month\nReport", "icon_report", true);
//
////        addButton(row, 992, "About App", "icon_sys_info", false);
////        addButton(row, ATTENDENCE_ACTION_TIME, "SO\nAttendance", "icon_men", true);
////        addButton(row, SHOP_ORDER, "Shop wise Order","icon_sales_order",true);
////
////        tbl.addView(row);
//
//        // row 4
////        row = new LinearLayout(context);
////        row.setOrientation(LinearLayout.HORIZONTAL);
////        row.setLayoutParams(params);
//
////        addButton(row, PRODUCT_REPORT, "Product Wise\nReport", "icon_report", true);
////        addButton(row, DAY_ACHIEVEMENTS, "Day Wise\nAchievement ", "icon_report", true);
//        addButton(row, MY_REPORT, "My Report", "icon_report", true);
//        addButton(row, MY_TEAM_REPORT, "My Team\nReport", "icon_report", true);
//
////        addButton(row, MY_GPS, "View GPS", "icon_location", true);
////
////        tbl.addView(row);
////
//////        // row 5
////        row = new LinearLayout(context);
////        row.setOrientation(LinearLayout.HORIZONTAL);
////        row.setLayoutParams(params);
//
//
//        addButton(row, ATTENDENCE_REPORT, "Attendence\nReport", "icon_report", false);
//        addButton(row, OTHER_WORK_REPORT, "Other Work\nReport ", "icon_report", false);
//
//
////        if (BuildConfig.DEBUG) {
////            addButton(row, ADMIN, "Admin", "icon_sys_info", true);
////        }else {
////            addButton(row, ADMIN, "Admin", "icon_sys_info", false);
////        }
////        addButton(row, MY_REPORT, "Report ", "icon_report", false);
//
//        tbl.addView(row);
//    }
//
//    private void addButton(LinearLayout row, int id, String name, String icon, boolean visible) {
//
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lp);
//        params.weight = 1f;
//
//        Button btn = new Button(context);
//        btn.setLayoutParams(params);
//        btn.setBackground(getResources().getDrawable(R.drawable.button_simple));
//        btn.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(context, icon), null, null);
//        btn.setCompoundDrawablePadding(5);
//        btn.setText(name);
//        btn.setAllCaps(false);
//        btn.setId(id);
//        btn.setTextAppearance(context, R.style.my_text);
//        btn.setGravity(Gravity.CENTER);
//        btn.setCompoundDrawablePadding(5);
//        btn.setPadding(0, 20, 0, 20);
//        btn.setOnClickListener(this);
//        if (visible) {
//            btn.setVisibility(View.VISIBLE);
//        } else {
//            btn.setVisibility(View.INVISIBLE);
//        }
//        row.addView(btn);
//    }
//
//    private Drawable getDrawable(Context context, String iconPath) {
//        int resourceId = context.getResources().getIdentifier(iconPath, "drawable", context.getPackageName());
//        return context.getResources().getDrawable(resourceId);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//
//            case SHOP_PRODUCT_WISE_REPORT:
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(context, TodayShopProductWiseReportActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        Logger.e("While sales order!", ex);
//                    }
//                }
//                break;
//
//            case ADMIN:
//                try {
//                    Intent orderIntent = new Intent(MainActivity.this, AdminToolsActivity.class);
//                    startActivity(orderIntent);
//                } catch (Exception ex) {
//                    errMsg("While sales order!", ex);
//                }
//                break;
//            case SALES_ORDER:
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this, SalesOrderActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While sales order!", ex);
//                    }
//                }
//                break;
//            case OTHER_WORK:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent toolsIntent = new Intent(MainActivity.this, OtherWorkActivity.class);
//                        startActivity(toolsIntent);
//                    } catch (Exception ex) {
//                        errMsg("Other work", ex);
//                    }
//                }
//                break;
//            case LEAVE:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent toolsIntent = new Intent(MainActivity.this, LeaveActivity.class);
//                        startActivity(toolsIntent);
//                    } catch (Exception ex) {
//                        errMsg("Other work", ex);
//                    }
//                }
//                break;
//            case MY_PLACE:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent toolsIntent = new Intent(MainActivity.this, MyPlaceActivity.class);
//                        startActivity(toolsIntent);
//                    } catch (Exception ex) {
//                        errMsg("Leave", ex);
//                    }
//                }
//                break;
//
//            case ALL_ORDERS_REPORT:
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent toolsIntent = new Intent(MainActivity.this, TodaysOrderActivity.class);
//                        startActivity(toolsIntent);
//                    } catch (Exception ex) {
//                        errMsg("Report todays order", ex);
//                    }
//                }
//                break;
//            case ORDER_SUMMARY:
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent toolsIntent = new Intent(MainActivity.this, TodaysSummaryReport.class);
//                        startActivity(toolsIntent);
//                    } catch (Exception ex) {
//                        errMsg("Report todays summary", ex);
//                    }
//                }
//                break;
//            case VERSION_UPDATE:
//
//                if (isGPSEnabled()) {
//                    try {
//                        ApkInstaller installer = new ApkInstaller();
//                        installer.setContext(MainActivity.this);
//                        installer.setHandler(msgHandler);
//                        installer.execute("");
//                    } catch (Exception e) {
//                        msgShort(e.getMessage());
//                    }
//                }
//                break;
//            case PENDING_UPLOAD:
//                if (isGPSEnabled()) {
//
//                    try {
//                        doManualUpload("", EVENT_REPORT_CHECK);
//
//                        Intent intent = new Intent(MainActivity.this, HoReportActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//                break;
//            case COMPLAINTS:
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent intent = new Intent(MainActivity.this, ComplaintActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//                break;
//           /* case ABOUT_APP:
//                Alert("Version Info", "You are using version " + BuildConfig.VERSION_NAME);
//                break;*/
//            case ATTENDANCE:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//
//                break;
//            case SO_TODAYS_REPORT:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent intent = new Intent(MainActivity.this, SoDayReportActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//                break;
//            case SO_MONTH_REPORT:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent intent = new Intent(MainActivity.this, SoMonthReportActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//                break;
//            case SO_ANALYTICS:
//                    try {
//                        Intent intent = new Intent(MainActivity.this, SoAnalyticReportActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                break;
//
//            case ATTENDENCE_ACTION_TIME:
//                if (isGPSEnabled()) {
//                    try {
//                        Intent intent = new Intent(MainActivity.this, AttendanceActivity2.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//                }
//
//                break;
//            case SHOP_ORDER :
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this, ShopWiseOrderActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While shop wise sales order!", ex);
//                    }
//                }
//                break;
//
//            case PRODUCT_REPORT :
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  ProductWiseOrderActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While product wise sales order!", ex);
//                    }
//                }
//                break;
//
//            case DAY_ACHIEVEMENTS :
//                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  DayWiseAchievementActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
//                }
//                break;
//
//
//            case MY_REPORT :
////                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  MySalesReportActivity.class);
//                        orderIntent.putExtra("reportType","MyReport");
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
////                }
//                break;
//
//
//            case MY_TEAM_REPORT :
////                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  MySalesReportActivity.class);
//                        orderIntent.putExtra("reportType","MyTeam");
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
////                }
//                break;
//
//
//            case DAILY_SALES_REPORT :
////                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  SOProductWiseAchievement.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
////                }
//                break;
//
//
//            case ATTENDENCE_REPORT :
////                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  AttendenceReportActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
////                }
//                break;
//
//
//            case OTHER_WORK_REPORT :
////                if (isGPSMasterEnabled()) {
//                    try {
//                        Intent orderIntent = new Intent(MainActivity.this,  SOOtherWorkReportActivity.class);
//                        startActivity(orderIntent);
//                    } catch (Exception ex) {
//                        errMsg("While day wise sales order!", ex);
//                    }
////                }
//                break;
//
//            default:
//                break;
//
//        }
//    }
//
//
//
//    private boolean isGPSEnabled() {
//        boolean enableGPS = UtilityFunc.isGPSEnabled(true, MainActivity.this);
//        if (!enableGPS) {
//            return false;
//        }
//        return true;
//    }
//
//    private boolean isGPSMasterEnabled() {
//
//        boolean enableGPS = UtilityFunc.isGPSEnabled(true, MainActivity.this);
//        if (!enableGPS) {
//            return false;
//        }
//
//        if (!hasMasterFound()) {
//            return false;
//        }
//        return true;
//
//    }
//
//
//    private void markAttendance(boolean userInvoke) {
//
//        final Date appDate = AppControl.getTodayDate();
//        final Date phoneDate = DateFunc.getTodaysDate();
//
//        if (DateFunc.isSameDate(appDate, phoneDate)) {
//            if (userInvoke) {
//                msgShort("Thank You! Attendance already done!");
//            }
//            return;
//        }
//
//        if (phoneDate.before(appDate)) {
//            msgShort("Device date error! Invalid system date!");
//            return;
//        }
//
//        addDay(appDate, phoneDate);
//    }
//
//    private void addDay(final Date oldDate, final Date newDate) {
//
//        SysDateRepo sysDateRepo = new SysDateRepo();
//
//        try {
//            dayOpenProc(oldDate, newDate);
//        } catch (Exception e) {
//            msgShort("Failed to update previous orders!");
//        }
//
//        sysDateRepo.deleteAll(AppControl.getmEmployeeId(), newDate);
//        sysDateRepo.insert(AppControl.getmEmployeeId(), newDate);
//
//        AppControl.refreshDate();
//        lblTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));
//
//        new TableInfoRepo().deletePostedData();
//        Toast.makeText(MainActivity.this, "*** Thank You! Have a Great Day ***", Toast.LENGTH_LONG).show();
//
//        try {
//            doManualUpload("", EVENT_ATTENDANCE);
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }
//    }
//
//    private void dayOpenProc(Date orderDate, Date newDate) {
//        new SalesOrderPreviousRepo().copyOrders(AppControl.getmEmployeeId(), orderDate);
//        new TargetRepo().copyYesterdayAch(AppControl.getmEmployeeId(), orderDate, newDate);
//    }
//
//    private void downloadData() {
//
//        TableInfoRepo tableData = new TableInfoRepo();
//
//        // TODO: Check if there is no upload pending
//
//        boolean isUploadPending = new ShopRepo().isShopUploadPending();
//
//        if (isUploadPending) {
//            Alert("Upload Pending!", "Shop not uploaded, please check HO Report & try again later.");
//            return;
//        }
//
//        boolean dataExist = tableData.hasDataExist(AppControl.getmEmployeeId());
//        if (dataExist) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("Confirm Download!");
//            builder.setMessage("Please select download type Complete or Update.");
//
//            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//
//            builder.setNeutralButton("FULL", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//                    try {
//                        new Downloader(MainActivity.this, msgHandler, true).execute("");
//                    } catch (Exception ex) {
//                        errMsg("While downloading", ex);
//                    }
//                }
//            });
//
//            builder.setNegativeButton("PENDING", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//                    try {
//                        new Downloader(MainActivity.this, msgHandler, false).execute("");
//                    } catch (Exception ex) {
//                        errMsg("While downloading", ex);
//                    }
//                }
//            });
//
//            AlertDialog alert = builder.create();
//            alert.show();
//            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
//            alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
//            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
//
//        } else {
//
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//            try {
//                new Downloader(MainActivity.this, msgHandler, true).execute("");
//            } catch (Exception ex) {
//                errMsg("While downloading", ex);
//            }
//        }
//    }
//
//    private void initData() {
//
//        loadNotes();
//        lblEmployeeName.setText(AppControl.getEmployeeName());
//        lblTodayDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));
//
//        // new Updater(updateHandler).execute("");
//
//    }
//
//    private void loadNotes() {
//
//        NoteRepo objNote = new NoteRepo();
//        List<vNote> notes = objNote.getTodayNotes(AppControl.getmEmployeeId(), AppControl.getTodayDate());
//        String notesStr = "Note: ";
//
//        for (vNote note : notes) {
//            notesStr += note.getNoteContent();
//        }
//
//        lblNote.setText(notesStr);
//        lblNote.setMovementMethod(new ScrollingMovementMethod());
//    }
//
//    private boolean hasMasterFound() {
//
//        TableInfoRepo tb = new TableInfoRepo();
//        if (!tb.isEligibleForOrder(AppControl.getmEmployeeId())) {
//            Alert("Data Not Found!", "Please do 'Master Updates' and try again.");
//            return false;
//        }
//
//        if (!tb.hasDayOpen(AppControl.getmEmployeeId())) {
//            Alert("Dates not found!", "Please mark attendance");
//            return false;
//        }
//
//        return true;
//    }
//
//    private void UpgadeVersion() {
//
//        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + " /";
//        final String fileName = "PMA.apk";
//        destination += fileName;
//        final Uri uri = Uri.parse("file://" + destination);
//
//        File file = new File(destination);
//        if (file.exists()) {
//            file.delete();
//        }
//
//        String url = "http://app.pitambari.com/pma.apk";
//
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        request.setDescription("Pitambari Mobile App New Update");
//        request.setTitle("PMA Upgrade");
//        request.setDestinationUri(uri);
//
//        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        final long downloadId = manager.enqueue(request);
//
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
////                Intent install = new Intent(Intent.ACTION_VIEW);
////                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                install.setDataAndType(uri,manager.getMimeTypeForDownloadedFile(downloadId));
////                startActivity(install);
//
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + fileName)), "application/vnd.android.package-archive");
//                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                install.setDataAndType(uri, manager.getMimeTypeForDownloadedFile(downloadId));
//                startActivity(install);
//
//                unregisterReceiver(this);
//                finish();
//            }
//        };
//
//        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//            alert.setTitle("Confirm!");
//            alert.setMessage("Do you want to exit ?");
//
//            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    onBackPressed();
//                }
//            });
//
//            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    return;
//                }
//            });
//
//            alert.create();
//            alert.show();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    protected Handler updateHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (msg.obj == null) {
//                return;
//            }
//
//            if (msg.arg1 == Constant.MessageType.Toast) {
//                msgShort((String) msg.obj);
//            }
//            if (msg.arg1 == Constant.MessageType.Alert) {
//                List<String> appMsg = null;
//
//                try {
//                    appMsg = (ArrayList<String>) msg.obj;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Alert(appMsg.get(0), appMsg.get(1));
//            }
//
//            loadNotes();
//        }
//    };

}
