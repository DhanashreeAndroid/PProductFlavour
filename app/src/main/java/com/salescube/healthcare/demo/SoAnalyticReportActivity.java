package com.salescube.healthcare.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.salescube.healthcare.demo.adapter.ViewPagerAdapter;
import com.salescube.healthcare.demo.ctrl.AutoHeightViewPager;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.util.Togger;
import com.salescube.healthcare.demo.view.ProductSecondary;
import com.salescube.healthcare.demo.view.SoAnalytics;
import com.salescube.healthcare.demo.view.vMonthData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoAnalyticReportActivity extends BaseTransactionActivity {

    private CardView cvSbtPrevious;
    private CardView cvSbtCurrent;

    private CardView cvHighPerformance;
    private CardView cvNewShops;
    private CardView cvProductWiseSale;

    private TextView txtPreviousMonthRank;
    private TextView txtPreviousMonth;
    private TextView txtCurrentMonthRank;
    private TextView txtCurrentMonth;
    private TextView txtBestSecondaryAmount;
    private TextView txtBestSecondaryDate;
    private TextView txtShopShopCount;
    private TextView txtShopShopText;

    private TableLayout tblProductSec;
    private Togger togger = Togger.getLogger();

    Animation slideDown;
    Animation animRotate;

    private PieChart shiningSecPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_analytic_report);
        title("My Performance");

        try {
            initData();
            loadData();
        } catch (Exception e) {
            togger.e(e);
            msgShort("Error while downloading data! Please try again later.");
        }


        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);


        cvSbtPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvSbtPrevious.startAnimation(slideDown);
            }
        });

        cvSbtCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvSbtCurrent.startAnimation(slideDown);
            }
        });

        cvHighPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvHighPerformance.startAnimation(slideDown);
            }
        });

        cvNewShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvNewShops.startAnimation(slideDown);
            }
        });

        cvProductWiseSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvProductWiseSale.startAnimation(slideDown);
            }
        });


    }

    //₹

    private void initData() {

        cvSbtPrevious = findViewById(R.id.cv_sbt_previous);
        cvSbtCurrent = findViewById(R.id.cv_sbt_current);

        cvHighPerformance = findViewById(R.id.cv_high_performance);
        cvNewShops = findViewById(R.id.cv_new_shops);
        cvProductWiseSale = findViewById(R.id.cv_product_wise_sale);

        txtPreviousMonthRank = findViewById(R.id.lbl_previous_month_rank);
        txtPreviousMonth = findViewById(R.id.lbl_previous_month);
        txtCurrentMonthRank = findViewById(R.id.lbl_current_month_rank);
        txtCurrentMonth = findViewById(R.id.lbl_current_month);
        txtBestSecondaryAmount = findViewById(R.id.lbl_best_secondary_amount);
        txtBestSecondaryDate = findViewById(R.id.lbl_best_secondary_date);
        txtShopShopCount = findViewById(R.id.lbl_new_shops);
        txtShopShopText = findViewById(R.id.lbl_new_shops_text);

        tblProductSec = findViewById(R.id.tbl_product_secondary);

    }

    private void setWebData(SoAnalytics data) {

        if (data == null) {
            Alert("Not Found!", "Data not found! Please try again later.");
            return;
        }

        loadPieChart(data);

        if (data.getBestDay() != null) {
            String bestDay = DateFunc.getDateStr(data.getBestDay().getDay(), "dd/MM/yyyy");
            txtBestSecondaryAmount.setText(Parse.ruppe(data.getBestDay().getAchValue(), true));
            txtBestSecondaryDate.setText("Best Performance\n" + bestDay);
        }

        txtShopShopCount.setText(String.valueOf(data.getShopCount()));

        boolean extraOrdinary = false;
        int sbt = 0;

        if (data.getSbtPrevious() != null) {
            // last month sbt
            sbt = data.getSbtPrevious().getAchPercent();
            //sbt = 99;

            extraOrdinary = false;
            if (sbt > 100) {
                sbt = 100;
                extraOrdinary = true;
            }

            sbt = 101 - sbt;

            txtPreviousMonthRank.setText("#" + String.valueOf(data.getSbtPrevious().getRank()));
//        if (extraOrdinary) {
//            txtPreviousMonthRank.setText("#" + String.valueOf(sbt) + "+");
//        }
            txtPreviousMonth.setText(data.getSbtPrevious().getPeriod() + "\n--" + data.getSbtPrevious().getCategory() + "--");

        }
        // current month sbt

        if (data.getSbtCurrent() != null) {
            sbt = data.getSbtCurrent().getAchPercent();
            //sbt = 110;

            extraOrdinary = false;
            if (sbt > 100) {
                sbt = 100;
                extraOrdinary = true;
            }

            sbt = 101 - sbt;

            txtCurrentMonthRank.setText("#" + String.valueOf(data.getSbtCurrent().getRank()));
//        if (extraOrdinary) {
//            txtCurrentMonthRank.setText("#" + String.valueOf(sbt) + "+");
//        }
            txtCurrentMonth.setText(data.getSbtCurrent().getPeriod() + "\n--" + data.getSbtCurrent().getCategory() + "--");

        }
//        if (sbt < 70) {
//            txtSbtStatus.setBackgroundColor(Color.RED);
//            txtSbtStatus.setText("BAL");
//        } else if (sbt >= 70 && sbt <= 90) {
//            txtSbtStatus.setBackgroundColor(Color.YELLOW);
//            txtSbtStatus.setText("SHISHU");
//        } else {
//            txtSbtStatus.setBackgroundColor(Color.GREEN);
//            txtSbtStatus.setText("TARUN");
//        }

        if (data.getProductDaySecondary() == null || data.getProductWeekSecondary() == null ||
                data.getProductMonthSecondary() == null) {
            return;
        }

        AutoHeightViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        setupViewPager(viewPager, data);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager, SoAnalytics data) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bndle = new Bundle();
        ArrayList<ProductSecondary> list;
        ProductSecondary[] dataArray;

        // day

        list = data.getProductDaySecondary();
        dataArray = list.toArray(new ProductSecondary[list.size()]);
        bndle = new Bundle();
        bndle.putParcelableArray("data", dataArray);

        BrandWiseTargetAchFragment dayFrag = new BrandWiseTargetAchFragment();
        dayFrag.setArguments(bndle);

        adapter.addFragment(dayFrag, "DAY");

        // week

        list = data.getProductWeekSecondary();
        dataArray = list.toArray(new ProductSecondary[list.size()]);
        bndle = new Bundle();
        bndle.putParcelableArray("data", dataArray);

        BrandWiseTargetAchFragment weekFrag = new BrandWiseTargetAchFragment();
        weekFrag.setArguments(bndle);

        adapter.addFragment(weekFrag, "WEEK");

        // month

        list = data.getProductMonthSecondary();
        dataArray = list.toArray(new ProductSecondary[list.size()]);
        bndle = new Bundle();
        bndle.putParcelableArray("data", dataArray);

        BrandWiseTargetAchFragment monthFrag = new BrandWiseTargetAchFragment();
        monthFrag.setArguments(bndle);

        adapter.addFragment(monthFrag, "MONTH");

        // fill adapter

        viewPager.setAdapter(adapter);
    }

    public class DecimalRemover extends PercentFormatter {

        protected DecimalFormat mFormat;

        public DecimalRemover(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "  " + mFormat.format(value) + "%";
        }
    }

    private void loadPieChart(SoAnalytics analytics) {

        shiningSecPie = findViewById(R.id.product_ach_pie);

        TextView lblProduct1 = findViewById(R.id.lblProduct1);
        TextView lblProduct2 = findViewById(R.id.lblProduct2);
        TextView lblProduct3 = findViewById(R.id.lblProduct3);

        vMonthData data = analytics.getMonthData();

        boolean exelant = false;

        int p1;
        int p2;

        exelant = false;
        p1 = data.getProduct1AchPercent();
        if (p1 > 100) {
            p1 = 100;
            exelant = true;
        }

        p2 = 100 - p1;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(p1, ""));
        entries.add(new PieEntry(p2, ""));

        lblProduct1.setText("Shining [" + String.valueOf(p1) + "%]");

        PieDataSet dataSet = new PieDataSet(entries, "data");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF4136"));
        colors.add(Color.parseColor("#ffd9d6"));

        dataSet.setColors(colors);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);

        pieData.setValueFormatter(new DecimalRemover(new DecimalFormat("###")));
        pieData.setValueTextColor(Color.BLACK);
        pieData.setDrawValues(false);
        pieData.setValueTextSize(14);


        shiningSecPie.setTransparentCircleRadius(0f);
        shiningSecPie.setHoleRadius(70f);

        shiningSecPie.setTouchEnabled(false);
        shiningSecPie.getLegend().setEnabled(false);
        shiningSecPie.setDescription(null);

        shiningSecPie.setData(pieData);
        shiningSecPie.invalidate();

        //shiningSecPie.animateY(3000, Easing.EasingOption.EaseInOutQuad);

        exelant = false;
        p1 = data.getProduct2AchPercent();

        if (p1 > 100) {
            p1 = 100;
            exelant = true;
        }

        p2 = 100 - p1;

        PieChart otherSecPie = findViewById(R.id.product_ach_pie_2);

        entries = new ArrayList<>();
        entries.add(new PieEntry(p1, ""));
        entries.add(new PieEntry(p2, ""));

        lblProduct2.setText("Other [" + String.valueOf(p1) + "%]");

        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.parseColor("#ccccff"));

        dataSet = new PieDataSet(entries, "data2");
        dataSet.setColors(colors);
        pieData = new PieData(dataSet);
        pieData.setValueFormatter(new DecimalRemover(new DecimalFormat("###")));
        pieData.setValueTextColor(Color.WHITE);
        pieData.setDrawValues(false);
        pieData.setValueTextSize(14);

        otherSecPie.setHoleRadius(65f);
        otherSecPie.setTouchEnabled(false);
        otherSecPie.setCenterTextSize(14);
        otherSecPie.getLegend().setEnabled(false);
        otherSecPie.setDescription(null);
        otherSecPie.setData(pieData);

        otherSecPie.invalidate();

        PieChart tcPcPie = findViewById(R.id.product_ach_pie_3);

        exelant = false;
        p1 = data.getPc();
        if (p1 > 100) {
            p1 = 100;
            exelant = true;
        }

        p2 = 100 - p1;

        entries = new ArrayList<>();
        entries.add(new PieEntry(p1, ""));
        entries.add(new PieEntry(p2, ""));

        lblProduct3.setText("TC/PC [" + String.valueOf(p1) + "%]");

        colors = new ArrayList<>();
        colors.add(Color.parseColor("#2ECC40"));
        colors.add(Color.parseColor("#d5f4d8"));

        dataSet = new PieDataSet(entries, "data2");
        dataSet.setColors(colors);
        pieData = new PieData(dataSet);
        pieData.setValueFormatter(new DecimalRemover(new DecimalFormat("###")));
        pieData.setValueTextColor(Color.WHITE);
        pieData.setDrawValues(false);
        pieData.setValueTextSize(14);

        tcPcPie.setHoleRadius(50f);
        tcPcPie.setTouchEnabled(false);
        tcPcPie.setCenterTextSize(14);
        tcPcPie.getLegend().setEnabled(false);
        tcPcPie.setDescription(null);
        tcPcPie.setData(pieData);

        tcPcPie.invalidate();

        //SqareFrameLayout pieFrame = findViewById(R.id.pie_frame);


//        otherSecPie.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //CIRCLE :      (x-a)^2 + (y-b)^2 = r^2
//                float centerX, centerY, touchX, touchY, radius;
//                centerX = v.getWidth() / 2;
//                centerY = v.getHeight() / 2;
//                touchX = event.getX();
//                touchY = event.getY();
//                radius = centerX;
//                System.out.println("centerX = "+centerX+", centerY = "+centerY);
//                System.out.println("touchX = "+touchX+", touchY = "+touchY);
//                System.out.println("radius = "+radius);
//                if (Math.pow(touchX - centerX, 2)
//                        + Math.pow(touchY - centerY, 2) < Math.pow(radius, 2)) {
//                    System.out.println("Inside Circle");
//                    return false;
//                } else {
//                    System.out.println("Outside Circle");
//                    return true;
//                }
//            }
//        });

    }

    @Override
    protected void onStop() {
       UtilityFunc.dismissDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
       UtilityFunc.dismissDialog();
        super.onDestroy();
    }



    private void loadData() {
        UtilityFunc.showDialog(this, "","");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        int empId = AppControl.getmEmployeeId();

        Call<SoAnalytics> query = apiService.getAnalyticsData(empId);

        query.enqueue(new Callback<SoAnalytics>() {
            @Override
            public void onResponse(Call<SoAnalytics> call, Response<SoAnalytics> response) {


                if (response.isSuccessful()) {
                    // save credentials
                    // show home screen

                    try {
                        SoAnalytics objUser = response.body();
                        setWebData(objUser);
                    } catch (Exception e) {
                       UtilityFunc.dismissDialog();
                        togger.e(e);
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }

                    startAnimation();

                } else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                       UtilityFunc.dismissDialog();
                        message = e.getMessage();
                        togger.e(e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }
                    Alert("Error!", message);

                }

               UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<SoAnalytics> call, Throwable t) {

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

                togger.e(t);
            }
        });
    }

    protected void Alert(String title, String msg) {

        AlertDialog.Builder alert = new AlertDialog.Builder(SoAnalyticReportActivity.this)
                .setTitle(title)
                .setMessage(msg);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.create();
        alert.show();
    }

    private void startAnimation() {

        cvSbtPrevious.startAnimation(slideDown);
        cvSbtCurrent.startAnimation(slideDown);
        cvHighPerformance.startAnimation(slideDown);
        cvNewShops.startAnimation(slideDown);
        cvProductWiseSale.startAnimation(slideDown);

    }

}

