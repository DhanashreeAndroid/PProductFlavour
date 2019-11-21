package com.salescube.healthcare.demo;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.service.DataJobIntentService;
import com.salescube.healthcare.demo.service.LocationJobIntentService;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.widget.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 17/11/2016.
 */

abstract class BaseAppCompatActivity extends AppCompatActivity {

    TextView textCartItemCount;
    int mCartItemCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setIcon(R.drawable.icon_menu_72x72);
//            actionBar.setIcon(R.drawable.icon_company);
            actionBar.setDisplayShowHomeEnabled(true);
            String appName = getResources().getString(R.string.app_name);

//            actionBar.setTitle(String.format("%s (%s)", appName,BuildConfig.VERSION_NAME));
//            actionBar.setTitle(String.format("%s", appName));
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.notifications) {
            msgShort("test");

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    public void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    protected void title(final String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
//        setTitle(title);
    }

    @Override
    protected void onResume() {

        final Date appDate = AppControl.getTodayDate();
        final Date phoneDate = DateFunc.getTodaysDate();

        if (appDate != null && phoneDate != null) {
            if (appDate.compareTo(phoneDate) != 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BaseAppCompatActivity.this)
                        .setTitle("Date Incorrect")
                        .setMessage("Device date error! Invalid system date!");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(BaseAppCompatActivity.this, LoginActivity.class);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected void msgShort(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseAppCompatActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void msgLong(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseAppCompatActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void errMsg(final String msg, final Throwable ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseAppCompatActivity.this, "Error! " + msg, Toast.LENGTH_LONG).show();
                Logger.log(Logger.ERROR, "PSS", ex.getMessage(), ex);
            }
        });
    }

    protected void Alert(String title, String msg) {

        AlertDialog.Builder alert = new AlertDialog.Builder(BaseAppCompatActivity.this)
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

        LocationJobIntentService.enqueueWork(getApplicationContext(), objLoc);
    }

    protected Handler msgHandler = new Handler() {
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

    protected void SuccessAlert1(String title, String msg) {

        new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        onBackPressed();
                    }
                })
                .show();

    }

    protected void SuccessAlert(String title, String msg, Context context) {
        try {

            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(msg)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();


                        }
                    })
                    .show();
        } catch (Exception e) {
            Log.d("SuccessAlert", "" + e.getMessage());
        }

    }

    protected void ErrorAlert(String title, String msg, Context context) {
        try {

            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title)
                    .setContentText(msg)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        } catch (Exception e) {
            Log.d("SuccessAlert", "" + e.getMessage());
        }

    }

}
