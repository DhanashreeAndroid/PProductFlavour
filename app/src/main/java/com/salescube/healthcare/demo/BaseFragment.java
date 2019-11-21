package com.salescube.healthcare.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class BaseFragment extends Fragment {

    protected Context context;

//    protected void assignDateTextBox(final EditText txt) {
//
//        assignDateTextBox(txt, true);
//    }
//
//    protected void assignDateTextBox(final EditText txt, Boolean setMinLock) {
//        assignDateTextBox(txt, setMinLock, false, false);
//    }
//
//    protected void assignDateTextBox(final EditText txt, Boolean setMinLock, boolean clearButton) {
//        assignDateTextBox(txt, setMinLock, clearButton, false);
//    }

//    protected void assignDateTextBox(final EditText txt, Boolean setMinLock, boolean clearButton, boolean setMaxLock) {
//
//        final Calendar newCalendar = Calendar.getInstance();
//        final DatePickerDialog followupDateDlg = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                txt.setText(Constant.DATE_FORMAT_2.format(newDate.getTime()));
//            }
//
//        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        if (setMinLock) {
//            Date lockDate = DateFunc.getTodayMin();
//            followupDateDlg.getDatePicker().setMinDate(lockDate.getTime());
//        }
//
//        if (setMaxLock) {
//            Date lockDate = DateFunc.getTodayMin();
//            followupDateDlg.getDatePicker().setMaxDate(lockDate.getTime());
//        }
//
//
//        if (clearButton) {
//            followupDateDlg.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    txt.setText(null);
//                }
//            });
//        }
//
//        txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                followupDateDlg.show();
//            }
//        });
//
//    }

    protected void assignTimeTextBox(final EditText txt) {

        final Calendar newCalendar = Calendar.getInstance();
        final TimePickerDialog followupDateDlg = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.context=  context;
//    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context=  context;
    }


    protected void Alert(String title, String msg) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.create();
        alert.show();
    }

    protected void disableEditText(EditText txt) {
        txt.setFocusable(false);
        txt.setFocusableInTouchMode(false);
        txt.setLongClickable(false);
        txt.setClickable(false);
    }

    public void msgShort(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void msgLong(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected int getColor(int id){
        return ContextCompat.getColor(context,id);
    }

    public int getItemIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

//    protected void logLocation(String formId, String event) {
//        Intent intent = new Intent(Constant.ACTION_AUTO_LOCATION_UPDATE);
//
//        LocationLog objLoc = new LocationLog();
//        objLoc.setCreatedBy(AppControl.getmEmployeeId());
//        objLoc.setFormId(formId);
//        objLoc.setLogEvent(event);
//
//        intent.putExtra("LOG", objLoc);
//        getActivity().getBaseContext().sendBroadcast(intent);
//    }



}
