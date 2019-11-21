package com.salescube.healthcare.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.MyMenu;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuScreenFragment extends BaseFragment {

    LinearLayout layoutParams;
    List<MyMenu> menuList, showMenu;
    List<String> menuType;
    int pos, col0, row0, row, col, totalScreen;
    TableRow tr;
    View view;
    LinearLayout ll_main;
    private Context context;

    private final int ADMIN = 0;
    private final int SALES_ORDER = 1;
    private final int OTHER_WORK = 2;
    private final int LEAVE = 3;
    private final int MY_PLACE = 4;
    private final int DAILY_UPDATE = 5;
    private final int MASTER_UPDATE = 6;
    private final int ALL_ORDERS_REPORT = 7;
    private final int ORDER_SUMMARY = 8;
    private final int VERSION_UPDATE = 9;
    private final int PENDING_UPLOAD = 10;
    private final int COMPLAINTS = 11;
    private final int ABOUT_APP = 12;


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

    public static MenuScreenFragment newInstance(List<MyMenu> menuList, int position, int totalScreen, List<String> menuType) {

        Bundle args = new Bundle();
        args.putSerializable("menuList", (Serializable) menuList);
        args.putSerializable("menuType", (Serializable) menuType);
        args.putInt("position", position);
        args.putInt("totalScreen", totalScreen);
        MenuScreenFragment fragment = new MenuScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState=getArguments();

        context=getActivity();
        if((savedInstanceState!=null))
            menuList = (List<MyMenu>) savedInstanceState.getSerializable("menuList");
        menuType= (List<String>) savedInstanceState.getSerializable("menuType");
//            pos = savedInstanceState.getInt("position");
//            totalScreen = savedInstanceState.getInt("totalScreen");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_screen, container, false);
        ll_main = view.findViewById(R.id.ll_main);
//        ll_main.setBackgroundColor(Color.GRAY);

        menuList=new ArrayList<>();
        savedInstanceState=getArguments();

        if((savedInstanceState!=null))
        menuList = (List<MyMenu>) savedInstanceState.getSerializable("menuList");
        menuType = (List<String>) savedInstanceState.getSerializable("menuType");
        pos = savedInstanceState.getInt("position");
        totalScreen = savedInstanceState.getInt("totalScreen");

//        if (savedInstanceState != null) {
//            savedInstanceState.remove("menuList");
////            getArguments().remove("position");
////            getArguments().remove("totalScreen");
//        }

        showMenu = new ArrayList<>();

//      screenwise seggregate item from list
       /* for(int i=0;i<menuList.size();i++){
            if (menuList.get(i).getScreenNo()==(pos + 1) || menuType.get(i).equals(menuList.get(i).getMenuType())) {
                showMenu.add(menuList.get(i));
            }
        }*/
        for (MyMenu menu : menuList) {
            if (menu.getScreenNo() == (pos + 1)) {
                showMenu.add(menu);
            }
        }
        /*for (String type : menuType){
            if(!showMenu.contains(type)){
                show
            }
        }*/

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
        Display display = ((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int w= display.getWidth();
        int width =(int)Math.round ((w)/3) ;
        int height = display.getHeight();
        Log.d("sizeee","width :"+(width)+"\tHeight : "+(height));
//        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1,-1,1.0f);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(0, -1);
        ll.weight = 1f;

        //For Rows
        for (int r = 1; r <= row0; r++) {
//          To Create Row Dynamically
            layoutParams = new LinearLayout(getActivity());
            LinearLayout.LayoutParams ll_row=new LinearLayout.LayoutParams(-1,-1);
            layoutParams.setLayoutParams(ll_row);
            layoutParams.setGravity(Gravity.CENTER);
            //For Column
            for (int c = 1; c <= col0; c++) {

                btn = new Button(context);
                btn.setLayoutParams(ll);
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) btn.getLayoutParams();
                params.width=width;
                params.height=width;
                params.setMargins(2, 2, 2, 2);
                btn.setTextColor(Color.BLACK);
                btn.setBackground(getActivity().getResources().getDrawable(R.drawable.button_white));
//                btn.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
                btn.setVisibility(View.INVISIBLE);
                btn.setLayoutParams(params);
//                btn.setOnClickListener(Fragment1.this);

                for(int m = 0 ;m < showMenu.size(); m++){
                    int n=showMenu.get(m).getRowNo();
                    int cm=showMenu.get(m).getColumnNo();
                    if (r == n && c == cm)
                    {
                        btn.setGravity(Gravity.CENTER);
                        btn.setVisibility(View.VISIBLE);
                        btn.setId(showMenu.get(m).getMenuId());
                        btn.setText(showMenu.get(m).getMenuName());
//                        btn.setPadding(0,20,0,20);
                        btn.setCompoundDrawablesWithIntrinsicBounds(null,getDrwable(getActivity(),showMenu.get(m).getIconPath()), null, null);
                        btn.setTextAppearance(context, R.style.my_text);
                        btn.setGravity(Gravity.CENTER);
                        btn.setCompoundDrawablePadding(5);
                        btn.setPadding(8,20,8,20);

                        if(!showMenu.get(m).isVisiable()){
                            btn.setVisibility(View.INVISIBLE);
                        }
                        if(showMenu.get(m).isEnable()){
//                            btn.setBackgroundColor(getActivity().getResources().getColor(R.color.isEnabled));
                        }
                        if(showMenu.get(m).isVisiable() && showMenu.get(m).isEnable()){
//                            btn.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
                        }

                        final Button finalBtn = btn;
                        final int finalM = m;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              btnClick(v.getId());
                            }
                        });
                    }
                }

                layoutParams.addView(btn);
            }// For Column
            ll_main.addView(layoutParams);
//                }
        }//For Row
        return view;
    }

    private void btnClick(int id) {
        switch(id){
            case SHOP_PRODUCT_WISE_REPORT:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context, TodayShopProductWiseReportActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While sales order!", ex);
                    }
                }
                break;

            case ADMIN:
                try {
                    Intent orderIntent = new Intent(context, AdminToolsActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }
                break;
            case SALES_ORDER:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context, SalesOrderActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While sales order!", ex);
                    }
                }
                break;
            case OTHER_WORK:
                if (isGPSEnabled()) {
                    try {
                        Intent toolsIntent = new Intent(context, OtherWorkActivity.class);
                        startActivity(toolsIntent);
                    } catch (Exception ex) {
                        Logger.e("Other work", ex);
                    }
                }
                break;
            case LEAVE:
                if (isGPSEnabled()) {
                    try {
                        Intent toolsIntent = new Intent(context, LeaveActivity.class);
                        startActivity(toolsIntent);
                    } catch (Exception ex) {
                        Logger.e("Other work", ex);
                    }
                }
                break;
            case MY_PLACE:
                if (isGPSEnabled()) {
                    try {
                        Intent toolsIntent = new Intent(context, MyPlaceActivity.class);
                        startActivity(toolsIntent);
                    } catch (Exception ex) {
                        Logger.e("Leave", ex);
                    }
                }
                break;

            case ALL_ORDERS_REPORT:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent toolsIntent = new Intent(context, TodaysOrderActivity.class);
                        startActivity(toolsIntent);
                    } catch (Exception ex) {
                        Logger.e("Report todays order", ex);
                    }
                }
                break;
            case ORDER_SUMMARY:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent toolsIntent = new Intent(context, TodaysSummaryReport.class);
                        startActivity(toolsIntent);
                    } catch (Exception ex) {
                        Logger.e("Report todays summary", ex);
                    }
                }
                break;
            case COMPLAINTS:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent intent = new Intent(context, ComplaintActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                break;
            case ATTENDANCE:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, AttendanceActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }

                break;
            case SO_TODAYS_REPORT:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, SoDayReportActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                break;
            case SO_MONTH_REPORT:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, SoMonthReportActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                break;
            case SO_ANALYTICS:
                try {
                    Intent intent = new Intent(context, SoAnalyticReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                break;

            case ATTENDENCE_ACTION_TIME:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, AttendanceActivity2.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }

                break;
            case SHOP_ORDER :
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context, ShopWiseOrderActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While shop wise sales order!", ex);
                    }
                }
                break;

            case PRODUCT_REPORT :
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context,  ProductWiseOrderActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While product wise sales order!", ex);
                    }
                }
                break;

            case DAY_ACHIEVEMENTS :
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context,  DayWiseAchievementActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While day wise sales order!", ex);
                    }
                }
                break;


            case MY_REPORT :
//                if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context,  MySalesReportActivity.class);
                    orderIntent.putExtra("reportType","MyReport");
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
//                }
                break;


            case MY_TEAM_REPORT :
//                if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context,  MySalesReportActivity.class);
                    orderIntent.putExtra("reportType","MyTeam");
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
//                }
                break;


            case DAILY_SALES_REPORT :
//                if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context,  SOProductWiseAchievement.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
//                }
                break;


            case ATTENDENCE_REPORT :
//                if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context,  AttendenceReportActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
//                }
                break;


            case OTHER_WORK_REPORT :
//                if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context,  SOOtherWorkReportActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While day wise sales order!", ex);
                }
//                }
                break;


            case ABOUT_APP :
                Alert("Version Info", "You are using version " + UtilityFunc.getAppVersion(getActivity()));
                break;

            default:
                break;

        }
    }

    private Drawable getDrwable(Context context, String iconPath) {
        int resourceId = context.getResources().getIdentifier(iconPath, "drawable", context.getPackageName());
        return  context.getResources().getDrawable(resourceId);
    }

    private boolean isGPSEnabled() {
        boolean enableGPS = UtilityFunc.isGPSEnabled(true, context);
        if (!enableGPS) {
            return false;
        }
        return true;
    }

    private boolean isGPSMasterEnabled() {

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, context);
        if (!enableGPS) {
            return false;
        }

        if (!hasMasterFound()) {
            return false;
        }
        return true;

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

    //    Load Dialog
    /*private void loadDialog(String menuName) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(menuName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.sample_dialog);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context,"Leave Submited",Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

    }*/


}
