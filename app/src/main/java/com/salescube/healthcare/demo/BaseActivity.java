package com.salescube.healthcare.demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.service.DataJobIntentService;
import com.salescube.healthcare.demo.service.LocationJobIntentService;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by user on 10/11/2016.6
 */

abstract class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        final Date appDate = AppControl.getTodayDate();
        final Date phoneDate = DateFunc.getTodaysDate();
        if(appDate != null  && phoneDate != null) {
            if (appDate.compareTo(phoneDate) != 0 ) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this)
                        .setTitle("Date Incorrect")
                        .setMessage("Device date error! Invalid system date!");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(BaseActivity.this, LoginActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                    }
                });

                alert.setCancelable(false);
                alert.create();
                alert.show();
            }
        }
        super.onResume();
    }

    protected void setTitle(String title){
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = (TextView)getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null){
            titleBar.setText(title);
        }
    }

    protected void changeTitle(String title){

        TextView titleBar = (TextView)getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null){
            titleBar.setText(title);
        }
    }

    protected void msgShort(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void msgLong(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void errMsg(final String msg, final Throwable ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "Error!" + msg, Toast.LENGTH_LONG).show();
                Logger.log(Logger.ERROR,"PSS",ex.getMessage(), ex);
            }
        });
    }

//    protected void Alert(final String title,final String msg){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this)
//                        .setTitle(title)
//                        .setMessage(msg);
//
//                alert.setPositiveButton("OK",new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                alert.create();
//                alert.show();
//
//            }
//        });
//    }


    protected void Alert(final String title,final String msg){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(BaseActivity.this, R.style.AppTheme))
                        .setTitle(title)
                        .setMessage(msg);

                alert.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.create();
                alert.show();

            }
        });
    }

    protected void closeCursor(Cursor c) {
        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
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
        final DatePickerDialog followupDateDlg = new DatePickerDialog(BaseActivity.this, new DatePickerDialog.OnDateSetListener() {




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


    protected void doManualUpload(String appShopId, String event) {

        DataJobIntentService.enqueueWork(getApplicationContext());

        captureLocation(appShopId, event);

    }

    private void captureLocation(String appShopId, String event) {

        LocationLog objLoc = new LocationLog();
        objLoc.setSoId(AppControl.getmEmployeeId());
        objLoc.setImei(AppControl.getImei());
        objLoc.setAppShopId(appShopId);
        objLoc.setEvent(event);

        LocationJobIntentService.enqueueWork(getApplicationContext(),objLoc);
    }

    protected void assignTimeTextBox(final EditText txt) {

        final Calendar newCalendar = Calendar.getInstance();
        final TimePickerDialog followupDateDlg = new TimePickerDialog(BaseActivity.this, new TimePickerDialog.OnTimeSetListener() {

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

    public Handler msgHandler = new Handler() {
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
        }
    };

}
