package com.salescube.healthcare.demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.adapter.DaysAdapter;
import com.salescube.healthcare.demo.adapter.ITourPlanClickListeners;
import com.salescube.healthcare.demo.adapter.TourPlanAdapter;
import com.salescube.healthcare.demo.data.model.Holiday;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.data.repo.TourPlanDetailRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.view.TourPlanResponse;
import com.salescube.healthcare.demo.view.vTourDates;
import com.salescube.healthcare.demo.view.vTourPlan;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourPlanActivity extends BaseAppCompatActivity implements View.OnClickListener,
        ITourPlanClickListeners {

    int mSelectedMonth = 0;
    int mSelectedYear;
    TextView mTvMothYear, mTvNoData;
    RecyclerView mDayRecycler;
    DaysAdapter mDayAdapter;
    RecyclerView mDataRecycler;
    TourPlanAdapter mDataAdapter;
    String mSelectedDate;
    ArrayList<vTourPlan> mTourPlanList;
    String mSelectedDay;
    LinearLayoutManager layoutManager1;
    Button mBtnSubmit;
    private ProgressDialog mProgressDialog;
    ApiService apiService;
    boolean isSuccess = false;
    ArrayList<Holiday> mHolidayList = new ArrayList<>();
    String mSelectedErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_plan);
        title("Tour Plan");
        initialization();
        setListener();
        loadTouPlan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initialization() {

        apiService = ApiClient.getClient().create(ApiService.class);
        mTvMothYear = findViewById(R.id.tvMonthYear);
        Calendar c = Calendar.getInstance();
        mSelectedYear = c.get(Calendar.YEAR);
        mSelectedMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(c.getTime());
        mTvMothYear.setText(month_name + " " + mSelectedYear);

        mTvNoData = findViewById(R.id.tvNoData);
        mBtnSubmit = findViewById(R.id.btnSubmit);

        mDayRecycler = findViewById(R.id.daysRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDayRecycler.setLayoutManager(layoutManager);
        mDayAdapter = new DaysAdapter(this, this);
        mDayRecycler.setAdapter(mDayAdapter);


        mDataRecycler = findViewById(R.id.dataRecycler);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDataRecycler.setLayoutManager(layoutManager1);
        mDataAdapter = new TourPlanAdapter(this, this);
        mDataRecycler.setAdapter(mDataAdapter);

        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Progress dialog title
        mProgressDialog.setTitle("Please wait....");
        // Progress dialog message
        mProgressDialog.setMessage("Tour Plan is submiting.....");

    }

    private ArrayList<vTourDates> getDaysList(int year, int month) {
        ArrayList<vTourDates> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.print(df.format(cal.getTime()));
        for (int i = 1; i < maxDay + 1; i++) {
            vTourDates dates = new vTourDates();
            cal.set(Calendar.DAY_OF_MONTH, i);
            dates.setDate(df.format(cal.getTime()));
            dates.setError(false);
            if (i == 1) {
                if (TextUtils.isEmpty(mSelectedDate)) {
                    mSelectedDate = df.format(cal.getTime());
                }
            }
            list.add(dates);
        }
        return list;
    }

    private ArrayList<vTourPlan> getTourPlanList(String date) {
        if (mTourPlanList != null) {
            mTourPlanList.clear();
        }

        mTourPlanList = (ArrayList<vTourPlan>) new TourPlanRepo().getTourPlan(
                AppControl.getmEmployeeId(),
                date,
                UtilityFunc.isAllowedTourPlan(this, date), mHolidayList);

        if (mTourPlanList.size() != 0) {
            mDataRecycler.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);
        } else {
            mDataRecycler.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.VISIBLE);
        }

        return mTourPlanList;
    }

    private void setListener() {
        mTvMothYear.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mTvMothYear) {
            mTvMothYear.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            showMonthYearPicker();
        } else if (v == mBtnSubmit) {
            if (UtilityFunc.isNetworkConnected(this)) {
                pushDataToServer();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(TourPlanActivity.this)
                        .setTitle("No Connection")
                        .setMessage("Please connect to internet then try again.");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.create();
                alert.show();
            }
        }
    }

    @Override
    public void onDayClick(vTourDates dates) {
        mSelectedDay = dates.getDate().substring(dates.getDate().length() - 2);
        mSelectedDate = dates.getDate();
        mDataAdapter.setData(getTourPlanList(dates.getDate()), dates.getErrorMessage());
    }

    @Override
    public void onAddMoreClick(vTourPlan vo) {
        new TourPlanRepo().insertDefaultPlan(vo.getSoId(), vo.getTourDate(), mHolidayList);
        if (mTourPlanList != null) {
            mTourPlanList.clear();
        }
        mTourPlanList = (ArrayList<vTourPlan>) new TourPlanRepo().getOverAllTourPlan(
                AppControl.getmEmployeeId(),
                vo.getTourDate(),
                UtilityFunc.isAllowedTourPlan(this, vo.getTourDate()), mHolidayList);
        mDataAdapter.setData(mTourPlanList, "");

        mDataRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataRecycler.smoothScrollToPosition(mDataAdapter.getItemCount() - 1);
            }
        }, 300);

    }

    private void setDataToList() {
        ArrayList<vTourDates> list = getDaysList(mSelectedYear, mSelectedMonth);
        mDayAdapter.setData(list);

        mTourPlanList = getTourPlanList(mSelectedDate);
        mDataAdapter.setData(mTourPlanList, "");
    }

    @Override
    public void onCloseClick(final vTourPlan vo, int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(vo.getSetName());
        alert.setCancelable(false);
        alert.setMessage("Are you sure, you want to delete tour plan?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new TourPlanRepo().deletePlan(vo.getSoId(), vo.getId());
                mTourPlanList = getTourPlanList(vo.getTourDate());
                mDataAdapter.setData(mTourPlanList, "");
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }

    public void loadTouPlan() {

        if (!UtilityFunc.isNetworkConnected(this)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(TourPlanActivity.this)
                    .setTitle("No Connection")
                    .setMessage("Please connect to internet then try again.");

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.create();
            alert.show();
            return;
        }

        UtilityFunc.showDialog(this, "", "");
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<TourPlan>> query = apiService.getTourPlan(AppControl.getmEmployeeId());

        query.enqueue(new Callback<List<TourPlan>>() {
            @Override
            public void onResponse(Call<List<TourPlan>> call, Response<List<TourPlan>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<TourPlan> objList = response.body();
                        TourPlanRepo repo = new TourPlanRepo();
                        for (TourPlan plan : objList) {
                            plan.setIsSync(1);
                            repo.insertUpdate(plan);
                            List<TourPlanDetail> objDetailList = plan.getDetail();
                            TourPlanDetailRepo repoDetail = new TourPlanDetailRepo();
                            for (TourPlanDetail detail : objDetailList) {
                                repoDetail.insertUpdate(detail);
                            }
                        }
                        UtilityFunc.dismissDialog();
                        setDataToList();
                        loadHoliday();
                    } catch (Exception e) {
                        UtilityFunc.dismissDialog();
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }
                } else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                        message = e.getMessage();
                        errMsg("While downloading", e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }
                    Alert("Error!", message);

                }
                UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<TourPlan>> call, Throwable t) {
                String message;

                if (t instanceof IOException) {
                    message = t.toString();
                }
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
    protected void onDestroy() {
        UtilityFunc.dismissDialog();
        super.onDestroy();
    }

    private void loadHoliday() {
        ApiService apiService1 = ApiClient.getClient().create(ApiService.class);

        Call<List<Holiday>> queryHoliday = apiService1.getMonthlyHolidays(mSelectedDate);

        queryHoliday.enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                if (response.isSuccessful()) {
                    mHolidayList = (ArrayList<Holiday>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Holiday>> call, Throwable t) {
            }
        });
    }

    private void pushDataToServer() {
        try {
            uploadTask task = new uploadTask();
            task.execute();

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private class uploadTask extends AsyncTask<String, Integer, Boolean> {
        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected Boolean doInBackground(String... tasks) {

            final TourPlanRepo objTourPlan = new TourPlanRepo();
            List<TourPlan> list = objTourPlan.getTourPlanForSync(AppControl.getmEmployeeId());

            if (list.size() == 0) {
                return false;
            }

            return submitTourPlan(list);
        }

        // After each task done
        protected void onProgressUpdate(Integer... progress) {
            // Update the progress bar on dialog
            mProgressDialog.setProgress(progress[0]);
        }

        // When all async task done
        protected void onPostExecute(Boolean success) {
            // Hide the progress dialog
            mProgressDialog.dismiss();

        }
    }

    private String isValidate(List<TourPlan> list) {
        StringBuilder strWorking = new StringBuilder();
        if (list != null && list.size() != 0) {
            for (TourPlan plan : list) {
                if (plan.getSetName().equalsIgnoreCase("working")) {
                    Gson gson = new Gson();
                    String arrayData = gson.toJson(plan);
                    if (arrayData.contains("Distributor") && arrayData.contains("City") && arrayData.contains("Route")) {
                    } else {
                        strWorking.append(DateFunc.getDateStrSimple(DateFunc.getDate(plan.getTourDate())));
                        strWorking.append(",");
                    }
                }
            }
        }
        return strWorking.toString();
    }

    private boolean submitTourPlan(final List<TourPlan> tourPlanList) {

        final String strWorking = isValidate(tourPlanList);
        if (!TextUtils.isEmpty(strWorking)) {
            TourPlanActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    ErrorAlert("Incomplete information", "Distributor, City, Route should be compulsory. \n\nPlease check below dates having incomplete information. \n\n" + strWorking.replace(",", "\n"), TourPlanActivity.this);
                }
            });
            return false;
        }


        final int employeeId = AppControl.getmEmployeeId();

        Call<TourPlanResponse> dataList = apiService.postTourPlan(tourPlanList);

        dataList.enqueue(new Callback<TourPlanResponse>() {
            @Override
            public void onResponse(Call<TourPlanResponse> call, Response<TourPlanResponse> response) {
                if (response.isSuccessful()) {
                    TourPlanResponse objList = response.body();
                    if (objList != null) {
                        if (objList.getDates().size() > 0) {
                            isSuccess = true;
                            for (int j = 0; j < objList.getDates().size(); j++) {
                                if (tourPlanList.size() > 0) {
                                    for (int i = 0; i < tourPlanList.size(); i++) {
                                        if(tourPlanList.get(i).getTourDate().equals(objList.getDates().get(j).date)){
                                            if(objList.getDates().get(j).message.equalsIgnoreCase("success")){
                                                tourPlanList.get(i).setIsSync(1);
                                                new TourPlanRepo().update(tourPlanList.get(i));
                                            }else{
                                                isSuccess = false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (isSuccess) {
                            SuccessAlert("Done", "Tour Plan submitted successfully", TourPlanActivity.this);
                            loadTouPlan();
                        } else {
                            ErrorAlert("Issue occurred", "Please look into red colored dates, to see problem.", TourPlanActivity.this);
                            ArrayList<vTourDates> list = getDaysListWithError(mSelectedYear, mSelectedMonth, objList);
                            mDayAdapter.setData(list);

                            mDataAdapter.setData(getTourPlanList(mSelectedDate), getSelectedErrorMessage(objList));
                        }
                    }
                } else {
                    isSuccess = false;
                    AlertDialog.Builder alert = new AlertDialog.Builder(TourPlanActivity.this)
                            .setTitle("Try Again")
                            .setMessage("Problem occurred, " + response.message());

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<TourPlanResponse> call, Throwable t) {
                isSuccess = false;
                AlertDialog.Builder alert = new AlertDialog.Builder(TourPlanActivity.this)
                        .setTitle("Try Again")
                        .setMessage("Problem occurred, " + t.getMessage());

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.create();
                alert.show();
            }
        });
        return isSuccess;
    }

    private String getSelectedErrorMessage(TourPlanResponse response) {
        String error = "";
        for (int j = 0; j < response.getDates().size(); j++) {
            if (response.getDates().get(j).date.equals(mSelectedDate)) {
                if( response.getDates().get(j).message.equalsIgnoreCase("Success")){
                    return error;
                }else {
                    return response.getDates().get(j).message;
                }
            }
        }
        return error;
    }

    private ArrayList<vTourDates> getDaysListWithError(int year, int month, TourPlanResponse response) {
        ArrayList<vTourDates> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.print(df.format(cal.getTime()));
        for (int i = 1; i < maxDay + 1; i++) {
            vTourDates dates = new vTourDates();
            cal.set(Calendar.DAY_OF_MONTH, i);
            String date = df.format(cal.getTime());
            for (int j = 0; j < response.getDates().size(); j++) {
                if (response.getDates().get(j).date.equals(date)) {
                    if(response.getDates().get(j).message.equalsIgnoreCase("success")) {
                        dates.setError(false);
                        dates.setErrorMessage("");
                    }else{
                        dates.setError(true);
                        dates.setErrorMessage(response.getDates().get(j).message);
                    }
                    break;
                }
            }
            dates.setDate(date);
            if (i == 1) {
                if (TextUtils.isEmpty(mSelectedDate)) {
                    mSelectedDate = df.format(cal.getTime());
                }
            }
            list.add(dates);
        }
        return list;
    }

    private void showMonthYearPicker() {
        final Dialog dialogAlert = new Dialog(this, R.style.CustomDialogTheme);
        dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlert.setContentView(R.layout.month_year_picker);
        dialogAlert.setCanceledOnTouchOutside(false);
        dialogAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
        dialogAlert.show();
        final Button btnOk = dialogAlert.findViewById(R.id.btn_ok);
        final Button btnCancel = dialogAlert.findViewById(R.id.btn_cancel);
        final TextView tvSelection = dialogAlert.findViewById(R.id.txt_selection);
        final TextView tvPrevious = dialogAlert.findViewById(R.id.btn_previous);
        final TextView tvNext = dialogAlert.findViewById(R.id.btn_next);
        final TextView tvYear = dialogAlert.findViewById(R.id.txt_year);
        final TextView tvJan = dialogAlert.findViewById(R.id.txt_jan);
        final TextView tvFeb = dialogAlert.findViewById(R.id.txt_feb);
        final TextView tvMar = dialogAlert.findViewById(R.id.txt_mar);
        final TextView tvApr = dialogAlert.findViewById(R.id.txt_apr);
        final TextView tvMay = dialogAlert.findViewById(R.id.txt_may);
        final TextView tvJun = dialogAlert.findViewById(R.id.txt_jun);
        final TextView tvJul = dialogAlert.findViewById(R.id.txt_jul);
        final TextView tvAug = dialogAlert.findViewById(R.id.txt_aug);
        final TextView tvSep = dialogAlert.findViewById(R.id.txt_sep);
        final TextView tvOct = dialogAlert.findViewById(R.id.txt_oct);
        final TextView tvNov = dialogAlert.findViewById(R.id.txt_nov);
        final TextView tvDec = dialogAlert.findViewById(R.id.txt_dec);
        final TextView[] arrTvMonths = {tvJan, tvFeb, tvMar, tvApr, tvMay, tvJun, tvJul, tvAug, tvSep, tvOct, tvNov, tvDec};
        Calendar c = Calendar.getInstance();
        final int maxYear = c.get(Calendar.YEAR);
        final int minYear = 2015;

        final LinearLayout container = dialogAlert.findViewById(R.id.picker_container);
        container.setBackground(UtilityFunc.getRectangleBorder(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), 0, 0));
        UtilityFunc.setSelectorRoundedCorner(this, btnOk, 0, R.color.transparent_bg, R.color.all_selection_color, R.color.month_year_picker_color, R.color.all_selection_color, 0);
        UtilityFunc.setSelectorRoundedCorner(this, btnCancel, 0, R.color.transparent_bg, R.color.all_selection_color, R.color.month_year_picker_color, R.color.all_selection_color, 0);
        UtilityFunc.setSelectorRoundedCorner(this, tvPrevious, 0, R.color.transparent_bg, R.color.all_selection_color, R.color.transparent_bg, R.color.all_selection_color, 0);
        UtilityFunc.setSelectorRoundedCorner(this, tvNext, 0, R.color.transparent_bg, R.color.all_selection_color, R.color.transparent_bg, R.color.all_selection_color, 0);

        tvYear.setText("" + mSelectedYear);
        arrTvMonths[mSelectedMonth - 1].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
        arrTvMonths[mSelectedMonth - 1].setTextColor(Color.WHITE);
        tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));

        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedYear > minYear) {
                    mSelectedYear--;
                    tvYear.setText("" + mSelectedYear);
                    tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                }
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedYear < maxYear) {
                    mSelectedYear++;
                    tvYear.setText("" + mSelectedYear);
                    tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.dismiss();
                mTvMothYear.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border));
                mTvMothYear.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + " " + mSelectedYear);
                mSelectedDate = "";
                ArrayList<vTourDates> list = getDaysList(mSelectedYear, mSelectedMonth);
                mDayAdapter.setData(list);
                mDayAdapter.setSelectedPosition(0);

                if (!UtilityFunc.isNetworkConnected(TourPlanActivity.this)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TourPlanActivity.this)
                            .setTitle("No Connection")
                            .setMessage("Please connect to internet then try again.");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.create();
                    alert.show();
                    return;
                }

                Call<List<Holiday>> queryHoliday = apiService.getMonthlyHolidays(mSelectedDate);

                queryHoliday.enqueue(new Callback<List<Holiday>>() {
                    @Override
                    public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                        if (response.isSuccessful()) {
                            mHolidayList = (ArrayList<Holiday>) response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Holiday>> call, Throwable t) {
                    }
                });

                mTourPlanList = getTourPlanList(mSelectedDate);
                mDataAdapter.setData(mTourPlanList, "");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.dismiss();
                mTvMothYear.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border));
            }
        });

        tvJan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvJan.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvFeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvFeb.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvMar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvMar.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvApr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvApr.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvMay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvMay.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvJun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvJun.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvJul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvJul.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvAug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvAug.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvSep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvSep.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvOct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvOct.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

        tvNov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvNov.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });
        tvDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedMonth = Integer.parseInt(tvDec.getTag().toString());
                tvSelection.setText(arrTvMonths[mSelectedMonth - 1].getText().toString() + ", " + String.valueOf(mSelectedYear));
                for (int i = 0; i < arrTvMonths.length; i++) {
                    if (mSelectedMonth == i + 1) {
                        arrTvMonths[i].setBackground(UtilityFunc.getCircularBorder(getResources().getColor(
                                R.color.month_year_picker_color), Color.TRANSPARENT, 0));
                        arrTvMonths[i].setTextColor(Color.WHITE);
                    } else {
                        arrTvMonths[i].setBackground(null);
                        arrTvMonths[i].setTextColor(Color.BLACK);
                    }
                }
            }
        });

    }

}

