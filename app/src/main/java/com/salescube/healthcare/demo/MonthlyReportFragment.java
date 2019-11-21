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

public class MonthlyReportFragment extends BaseFragment implements View.OnClickListener  {

    LinearLayout llMainMenu;
    LinearLayout row;
    LinearLayout.LayoutParams params;
    Context context;
    String reportType;

    private final int MONTH_SHOPWISE_ACHIEVEMENT = 1;
    private final int MONTH_PRODUCT_WISE_ACHIEVEMENT = 2;
    private final int MONTH_DAY_WISE_ACHIEVEMENT = 3;
    private final int SO_MONTH_REPORT = 4;
    private final int PRODUCT_REPORT = 5;
    private final int SO_MONTH_ATTENDENCE = 6;
    private final int SO_MONTH_PERFORMANCE =7;
    private final int SO_MONTH_PRODUCT_WISE =8;

    public static MonthlyReportFragment newInstance(String reportType) {

        Bundle args = new Bundle();
        args.putString("reportType",reportType);
        MonthlyReportFragment fragment = new MonthlyReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportType = getArguments().getString("reportType");

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

        return view;
    }

    private void fillMyTeamReport() {
        addButton(llMainMenu, SO_MONTH_ATTENDENCE, "Attendence Report", "attendence_report", true);
        addButton(llMainMenu, SO_MONTH_PERFORMANCE, "SO Wise Performance", "so_wise_performence", true);
        addButton(llMainMenu, SO_MONTH_PRODUCT_WISE, "Product Wise Performance", "product_wise_achievement", true);
        addButton(llMainMenu, SO_MONTH_REPORT, "SO Wise Monthly Achievement", "so_wise_performance", true);
    }

    private void fillMySalesReport() {
        addButton(llMainMenu,MONTH_SHOPWISE_ACHIEVEMENT,"Shop Wise Achievement","shop_wise_achievement",true);
        addButton(llMainMenu,MONTH_PRODUCT_WISE_ACHIEVEMENT,"Product Wise Achievement","product_wise_achievement",true);
        addButton(llMainMenu,MONTH_DAY_WISE_ACHIEVEMENT,"Day Wise Target / Achievement","shop_product_wise_achievement",true);

//        addButton(llMainMenu, PRODUCT_REPORT, "Product Wise Report", "icon_report", false);
    }

    private void initControls(View view) {
        context = getActivity();
        llMainMenu = view.findViewById(R.id.ll_today_menu_report);
    }

    private void addButton(LinearLayout row, int id, String name, String icon, boolean visible) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0 );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lp);
        params.weight = 1f;

        Button btn = new Button(context);
        btn.setLayoutParams(params);
        btn.setBackground(getResources().getDrawable(R.drawable.sales_button));
        if(!icon.equals("")){
            btn.setCompoundDrawablesWithIntrinsicBounds( getDrawable(context, icon),null, null, null);
            btn.setCompoundDrawablePadding(5);
        }
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setId(id);
        btn.setTextAppearance(context, R.style.MyLabel);
        btn.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
        btn.setCompoundDrawablePadding(10);
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
            case MONTH_SHOPWISE_ACHIEVEMENT:
//                            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, MonthShopWiseAchievementActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }
//                            }
                break;
            case MONTH_PRODUCT_WISE_ACHIEVEMENT:
//               if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, MonthProductWiseAchievementActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }
//              }
                break;

            case MONTH_DAY_WISE_ACHIEVEMENT:
//                            if (isGPSMasterEnabled()) {
                try {
                    Intent orderIntent = new Intent(context, MonthDayWiseReportActivity.class);
                    startActivity(orderIntent);
                } catch (Exception ex) {
                    Logger.e("While sales order!", ex);
                }
//                            }
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

            case SO_MONTH_ATTENDENCE:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, SoMonthAttendenceReportActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                break;


            case SO_MONTH_PERFORMANCE:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, SoMonthPerformanceReportActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                break;


            case SO_MONTH_PRODUCT_WISE:
                if (isGPSEnabled()) {
                    try {
                        Intent intent = new Intent(context, SoMonthProductReportActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
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
