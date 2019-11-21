package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.activity.LatestAttendanceActivity;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.repo.LeaveRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.SharePref;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_LEAVE;

public class LeaveActivity extends BaseTransactionActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private Spinner spnDuraton;
    private Spinner spnLeaveType;
    private Button btnSubmit;
    private Button btnCancel;

    private TextView edtFromDate;
    private TextView edtToDate;

    private TextView lblCOFor;
    private TextView edtCOFor;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private DatePickerDialog coForDatePicker;
    private Button btnReport;
    private SharePref sharePref;
    private SysDate sysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        title("Leave");

        try {
            initControl();
            initListners();
            initData();

            Bundle i = getIntent().getExtras();
            sysDate = (SysDate) i.getSerializable("SysData");



        } catch (Exception e) {
            errMsg("While loading Leave", e);
        }

    }

    private void initControl() {

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        edtFromDate = (TextView) findViewById(R.id.leave_date_from_date);
        edtToDate = (TextView) findViewById(R.id.leave_date_to_date);

        spnDuraton = (Spinner) findViewById(R.id.leave_spn_leave_duration);
        spnLeaveType = (Spinner) findViewById(R.id.leave_spn_leave_type);

        btnSubmit = (Button) findViewById(R.id.leave_btn_submit);
        btnCancel = (Button) findViewById(R.id.leave_btn_cancel);

        lblCOFor = (TextView) findViewById(R.id.leave_lbl_co_for);
        edtCOFor = (TextView) findViewById(R.id.leave_date_co_for);

        btnReport = (Button) findViewById(R.id.leave_btn_report);

        spnLeaveType.setOnItemSelectedListener(this);
        sharePref = new SharePref(getApplicationContext());
    }

    private void initListners() {

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean enableGPS = UtilityFunc.isGPSEnabled(true, LeaveActivity.this);
                if (!enableGPS) {
                    return;
                }

                try {
                    save();
                } catch (Exception e) {
                    errMsg("While submiting leave!", e);
                }

            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReport.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(LeaveActivity.this, LeaveReportActivity.class);
                    intent.putExtra("SysData",sysDate);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    Alert("Error!", e.getMessage());
                }

            }
        });
    }

    private void initData() {

        SpinnerHelper.FillLeaveDuration(spnDuraton);
        SpinnerHelper.FillLeaveType(spnLeaveType);

        lblCOFor.setVisibility(View.GONE);
        edtCOFor.setVisibility(View.GONE);

        setDateTimeField();
        edtFromDate.setInputType(InputType.TYPE_NULL);
        edtToDate.setInputType(InputType.TYPE_NULL);
        edtCOFor.setInputType(InputType.TYPE_NULL);

        edtFromDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));
        edtToDate.setText(DateFunc.getDateStrSimple(AppControl.getTodayDate()));

    }

    private boolean save() {

        if (!validate()) {
            return false;
        }

        // validate
        Date fromDate;
        Date toDate;
        Date coFor;
        String leaveType;
        String leaveDuration;

        leaveType = spnLeaveType.getSelectedItem().toString();
        leaveDuration = spnDuraton.getSelectedItem().toString();
        fromDate = DateFunc.getDate(edtFromDate.getText().toString(), dateFormatter);
        toDate = DateFunc.getDate(edtToDate.getText().toString(), dateFormatter);

        Leave objLeave;
        LeaveRepo objLeaveRepo = new LeaveRepo();

        objLeave = new Leave();
        objLeave.setSoId(AppControl.getmEmployeeId());
        objLeave.setOrderDate(AppControl.getTodayDate());
        objLeave.setFromDate(fromDate);
        objLeave.setToDate(toDate);
        objLeave.setLeaveType(leaveType);
        if (leaveType == "CO") {
            coFor = DateFunc.getDate(edtCOFor.getText().toString(), dateFormatter);
            objLeave.setCoFor(coFor);
        }
        objLeave.setDuration(leaveDuration);
        objLeaveRepo.insert(objLeave);

        msgShort("Leave application submitted!");

        UploadData(sysDate);

        doManualUpload("",EVENT_LEAVE);

        Intent homeIntent = new Intent(LeaveActivity.this, NewMainActivity.class);
        startActivity(homeIntent);
        finish();


        return true;
    }

    private boolean validate() {

        Date fromDate;
        Date toDate;

        fromDate = DateFunc.getDate(edtFromDate.getText().toString(), dateFormatter);
        toDate = DateFunc.getDate(edtToDate.getText().toString(), dateFormatter);

        if (fromDate.after(toDate)) {
            Alert("Invalid date!", "Leave To date should be greater than or equal to From date!");
            return false;
        }


        String leaveType;
        String leaveDuration;

        leaveType = spnLeaveType.getSelectedItem().toString();
        leaveDuration = spnDuraton.getSelectedItem().toString();

        if (leaveDuration == Constant.LeaveDuration.FULL_DAY) {
            if (fromDate.compareTo(AppControl.getTodayDate()) == 0) {
                boolean hasTodayOrders = new SalesOrderRepo().hasOrders(AppControl.getmEmployeeId(), AppControl.getTodayDate());
                if (hasTodayOrders) {
                    Alert("Not allowed!", "Orders already exist, leave not allowed!");
                    return false;
                }
            }
        }

        if (leaveDuration != Constant.LeaveDuration.FULL_DAY){
            if (fromDate.compareTo(toDate) != 0){
                Alert("Not allowed!", "Please select Full Day, half day not allowed for long leave");
                return false;
            }
        }

        return true;
    }

    private void setDateTimeField() {

        edtFromDate.setOnClickListener(this);
        edtToDate.setOnClickListener(this);
        edtCOFor.setOnClickListener(this);

        final Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        coForDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtCOFor.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH) -1 );

        Date lockDate = AppControl.getTodayDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(lockDate);

        coForDatePicker.getDatePicker().setMaxDate(lockDate.getTime());

    }

    @Override
    public void onClick(View v) {
        if (v == edtFromDate) {
            fromDatePickerDialog.show();
        } else if (v == edtToDate) {
            toDatePickerDialog.show();
        } else if (v == edtCOFor) {
            coForDatePicker.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String leaveType = spnLeaveType.getSelectedItem().toString();
        if (leaveType == "CO") {
            lblCOFor.setVisibility(View.VISIBLE);
            edtCOFor.setVisibility(View.VISIBLE);
        } else {
            int visbilty = lblCOFor.getVisibility();
            if (visbilty == View.VISIBLE) {
                lblCOFor.setVisibility(View.GONE);
                edtCOFor.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

     /*   Intent homeIntent = new Intent(LeaveActivity.this, NewMainActivity.class);
        startActivity(homeIntent);
        finish();*/

    }

    private  void  UploadData(SysDate sysDat ){

        final String TAG = "saveAttendance";

        ApiService apiService =  ApiClient.getClient().create(ApiService.class);
        Call<Void> dataList = apiService.saveDayInDayOut(sysDat);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    try {

                            sharePref.setDayIn();

                    } catch (Exception ex) {
                        Log.d(TAG ,""+ex);
                    }
                } else {
                    Log.d(TAG , response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }
}
