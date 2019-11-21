package com.salescube.healthcare.demo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.sysctrl.Constant;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by user on 22/10/2016.
 */

abstract class BaseTransactionActivity extends BaseAppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setIcon(R.drawable.icon_menu_72x72);
//            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    protected void logLocation(final String appShopId, final String event) {
//
//        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
//            int count = 0;
//
//            @Override
//            public void gotLocation(String provider, Location location) {
//                double[] latLong = new double[2];
//
//                if (location == null) {
//                    latLong[0] = 0;
//                    latLong[1] = 0;
//                }else{
//                    latLong[0] = location.getLatitude();
//                    latLong[1] = location.getLongitude();
//                }
//
//                logLocation(appShopId,event, latLong);
//            }
//        };
//
//        MyLocation myLocation = new MyLocation();
//        myLocation.getLocation(getApplicationContext(), locationResult);
//    }

//    protected void logLocation(String appShopId, String event, double[] latLong) {
//
//        String networkStatus = "NOT_CONNECTED";
//
//        try {
//            boolean connected = UtilityFunc.IsConnected();
//            if (connected) {
//                networkStatus = "CONNECTED";
//            }
//        } catch (Exception e) {
//            Logger.log(Logger.ERROR, "NetworkCheque", e.getMessage(), e);
//        }
//
//        float battryLevel = 0;
//
//        try {
//            battryLevel = getBatteryLevel();
//        } catch (Exception e) {
//            Logger.log(Logger.ERROR, "BatteryLevel", e.getMessage(), e);
//        }
//
//        LocationLog locLog = new LocationLog();
//        locLog.setSoId(AppControl.getmEmployeeId());
//        locLog.setImei(MobileInfo.getIMEI());
//        locLog.setAppShopId(appShopId);
//        locLog.setTxnDate(AppControl.getTodayDate());
//        locLog.setLatitude(String.valueOf(latLong[0]));
//        locLog.setLongitude(String.valueOf(latLong[1]));
//        locLog.setAddress("");
//        locLog.setEvent(event);
//        locLog.setNetwork(networkStatus);
//        locLog.setBattery(String.valueOf(battryLevel));
//
//        try {
//            LocationLogRepo.log(locLog);
//        } catch (Exception e) {
//            Logger.log(Logger.ERROR, "LocationLog", e.getMessage(), e);
//        }
//
//    }

    public float getBatteryLevel() {

        Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }



    protected void assignDateTextBox(final TextView txt) {

        assignDateTextBox(txt, true);
    }

    protected void assignDateTextBox(final TextView txt, Boolean setMinLock) {
        assignDateTextBox(txt, setMinLock, false, false);
    }

    protected void assignDateTextBox(final TextView txt, Boolean setMinLock, boolean clearButton) {
        assignDateTextBox(txt, setMinLock, clearButton, false);
    }

    protected void assignDateTextBox(final TextView txt, Boolean setMinLock, boolean clearButton, boolean setMaxLock) {

        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog followupDateDlg = new DatePickerDialog(BaseTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {




            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txt.setText(Constant.DATE_FORMAT_2.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        if (setMinLock) {
            Date lockDate = DateFunc.getTodayMin();
            followupDateDlg.getDatePicker().setMinDate(lockDate.getTime());
        }

        if (setMaxLock) {
            Date lockDate = DateFunc.getTodayMin();
            followupDateDlg.getDatePicker().setMaxDate(lockDate.getTime());
        }


        if (clearButton) {
            followupDateDlg.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt.setText(null);
                }
            });
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followupDateDlg.show();
            }
        });

    }

    protected void assignTimeTextBox(final EditText txt) {

        final Calendar newCalendar = Calendar.getInstance();
        final TimePickerDialog followupDateDlg = new TimePickerDialog(BaseTransactionActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String ampm;
                if (hourOfDay < 12) {
                    ampm = "AM";
                }else {
                    ampm = "PM";
                }
                hourOfDay = (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12;
                String minutsStr = String.format("%02d", minute);

                txt.setText(hourOfDay + ":" + minutsStr + " " + ampm);
            }

        },7,0, false);


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followupDateDlg.show();
            }
        });

    }

    protected void disableEditText(EditText txt) {
        txt.setFocusable(false);
        txt.setFocusableInTouchMode(false);
        txt.setLongClickable(false);
        txt.setClickable(false);
    }




}
