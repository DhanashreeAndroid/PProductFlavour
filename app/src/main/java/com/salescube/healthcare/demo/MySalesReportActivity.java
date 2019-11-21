package com.salescube.healthcare.demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salescube.healthcare.demo.adapter.ViewPagerAdapter;

public class MySalesReportActivity extends BaseTransactionActivity{

    private LinearLayout ll_main;
    private FrameLayout myFragment;
    private TextView tvToday;
    private TextView tvMonth;
    private String reportType;
//    MyViewpagerAdapter myViewPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);


        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setIcon(R.drawable.icon_menu_72x72);
            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/

        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            if(!(bundle.getString("reportType")).equals("MyTeam")){
                reportType= "MySales";
                title("My Report");
            }else{
                reportType= "MyTeam";
                title("My Team Report");
            }
        }

//        if(reportType.equals("MySales"))
//        {
//            actionBar.setTitle("My Sales Report");
//        }else{
//            actionBar.setTitle("My Team Sales Report");
//        }

        try {
            initControls();
        } catch (Exception e) {
            errMsg("While loading sales orders",e);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initControls() {

        tabLayout=findViewById(R.id.sales_report_tab_layout);
        viewPager=findViewById(R.id.sales_report_viewpager);
        setupViewPager(viewPager);
//
        tabLayout.setupWithViewPager(viewPager);
    }

//    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        try{
         ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            if(reportType.equals("MySales"))
            {
                adapter.addFragment(new TodayReportFragment().newInstance(reportType),"Today");
                adapter.addFragment(new MonthlyReportFragment().newInstance(reportType),"Month");
            }else{
                adapter.addFragment(new TodayReportFragment().newInstance(reportType),"Today");
                adapter.addFragment(new MonthlyReportFragment().newInstance(reportType),"Month");
            }


         viewPager.setAdapter(adapter);
        }catch (Exception ex){
            ex.getMessage();
        }
    }
}
