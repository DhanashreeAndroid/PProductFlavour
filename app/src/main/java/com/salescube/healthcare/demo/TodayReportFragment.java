package com.salescube.healthcare.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;

public class TodayReportFragment extends BaseFragment implements View.OnClickListener {

    LinearLayout llMainMenu;
    LinearLayout row;
    LinearLayout.LayoutParams params;
    Context context;
    String reportType;


    private final int SHOPWISE_ACHIEVEMENT = 1;
    private final int PRODUCT_WISE_ACHIEVEMENT = 2;
    private final int SHOP_PRODUCT_WISE_REPORT = 3;
    private final int DISTRIBUTOR_PRODUCT_WISE_REPORT = 10;
    private final int DAILY_SALES_REPORT = 4;
    private final int ATTENDENCE_REPORT = 5;
    private final int OTHER_WORK_REPORT = 6;
    private final int SHOP_ORDER = 7;
    private final int DAY_ACHIEVEMENTS = 8;
    private final int SO_TODAYS_REPORT = 9;

    public static TodayReportFragment newInstance(String reportType) {

        Bundle args = new Bundle();
        args.putString("reportType",reportType);
        TodayReportFragment fragment = new TodayReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportType = getArguments().getString("reportType");

    }

    private void fillMyTeamReport() {
        addButton(llMainMenu, ATTENDENCE_REPORT, "Attendence Report", "attendence_report", true);
        addButton(llMainMenu, SO_TODAYS_REPORT, "Daily Sales Report (In value)", "daily_sales_report", true);
        addButton(llMainMenu, DAILY_SALES_REPORT, "Product Wise Sales (In Qty)", "product_wise_achievement", true);
        addButton(llMainMenu, OTHER_WORK_REPORT, "Other Work Report ", "other_work_report", true);
        addButton(llMainMenu, SHOP_ORDER, "Shop wise Order","shop_wise_achievement",true);
    }

    private void fillMySalesReport() {

        addButton(llMainMenu,SHOPWISE_ACHIEVEMENT,"Shop Wise Achievement","shop_wise_achievement",true);
        addButton(llMainMenu,PRODUCT_WISE_ACHIEVEMENT,"Product Wise Achievement","product_wise_achievement",true);
        addButton(llMainMenu,SHOP_PRODUCT_WISE_REPORT,"Shop & Product Wise Achievement","shop_product_wise_achievement",true);
        addButton(llMainMenu,DISTRIBUTOR_PRODUCT_WISE_REPORT,"Distributor & Product Wise Achievement","distributor_report",true);

        addButton(llMainMenu, DAY_ACHIEVEMENTS, "Day Wise Achievement ", "icon_report", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_todays_report,container,false);
        try {
            initControls(view);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

        if(reportType.equals("MySales"))
        {
            fillMySalesReport();
        }else{
            fillMyTeamReport();
        }
//
        return view;
    }

    private void initControls(View view) {
        context = getActivity();
        llMainMenu = view.findViewById(R.id.ll_today_menu_report);
    }

    private void addButton(LinearLayout row, int id, String name, String icon, boolean visible) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lp);
        params.weight = 1f;

        Button btn = new Button(context);
        btn.setLayoutParams(params);
        btn.setBackground(getResources().getDrawable(R.drawable.sales_button));
        if(!icon.equals("")){
            btn.setCompoundDrawablesWithIntrinsicBounds(getDrawable(context, icon),null,  null, null);
            btn.setCompoundDrawablePadding(5);
        }
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setId(id);
        btn.setTextAppearance(context, R.style.MyLabel);
        btn.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
        btn.setCompoundDrawablePadding(5);

        btn.setPadding(20, 20, 0, 20);
        btn.setOnClickListener(this);
        if (visible) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
        row.addView(btn);
    }

    private Drawable getDrawable(Context context, String iconPath) {
        int resourceId = context.getResources().getIdentifier(iconPath, "drawable", context.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case SHOPWISE_ACHIEVEMENT:
                        if (isGPSMasterEnabled()) {
                            try {
                                Intent orderIntent = new Intent(context, TodayShopWiseAchievementActivity.class);
                                startActivity(orderIntent);
                            } catch (Exception ex) {
                                Logger.e("While sales order!", ex);
                            }
                        }
                break;
            case PRODUCT_WISE_ACHIEVEMENT:
                            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, TodayProductWiseAchievementActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }
                            }
                break;

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
            case DISTRIBUTOR_PRODUCT_WISE_REPORT:
                if (isGPSMasterEnabled()) {
                    try {
                        Intent orderIntent = new Intent(context, TodayAgentProductWiseReportActivity.class);
                        startActivity(orderIntent);
                    } catch (Exception ex) {
                        Logger.e("While sales order!", ex);
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
//                }```
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
        }
    }

    private boolean isGPSEnabled() {
        boolean enableGPS = UtilityFunc.isGPSEnabled(true, context);
        if (!enableGPS) {
            return false;
        }
        return true;
    }

    private boolean isGPSMasterEnabled() {

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, getActivity());
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
}
