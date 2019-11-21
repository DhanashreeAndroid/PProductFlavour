package com.salescube.healthcare.demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.repo.LeaveRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderPreviousRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class AdminToolsActivity extends BaseAppCompatActivity {

    private static final String TAG = "AdminToolsActivity";
    private Button btnClearOrders;
    private Button btnManualUpload;
    private Button btnTableInfo;
    private Button btnTest;
    private Button btnGetLocation;
    private Button btnCopyData;
    private Button btnTableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tools);
        title("Admin");

        try {
            initControls();
            initListners();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            Alert("Error", e.getMessage());
        }

    }

    private void initControls() {

        btnClearOrders = (Button) findViewById(R.id.admin_tools_btn_clear_orders);
        btnManualUpload = (Button) findViewById(R.id.admin_tools_btn_manual_update);
        btnTableInfo = (Button) findViewById(R.id.admin_tools_btn_table_info);
        btnTest = (Button) findViewById(R.id.admin_tools_btn_test);
        btnGetLocation = (Button) findViewById(R.id.admin_btn_get_location);
        btnCopyData = (Button) findViewById(R.id.admin_tools_copy_data);
        btnTableView = (Button) findViewById(R.id.admin_btn_table_view);

    }

    private  void initListners() {

        btnClearOrders.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLeaves();
            }
        });

        btnManualUpload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                doManualUpload("","ADMIN");
            }
        });

        btnTableInfo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableInfo();
            }
        });

        btnTest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new SalesOrderPreviousRepo().copyOrders(AppControl.getTodayDate());

                msgShort(Build.VERSION.RELEASE);
                //getModelNo();
                // alaramTest();
            }
        });

        btnGetLocation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Constant.ACTION_AUTO_LOCATION_UPDATE);

                LocationLog objLoc = new LocationLog();
                objLoc.setSoId(AppControl.getmEmployeeId());
                objLoc.setImei(AppControl.getImei());
                objLoc.setAppShopId("");
                objLoc.setEvent("ADMIN_CALL");

                intent.putExtra("LOG", objLoc);
                getBaseContext().sendBroadcast(intent);

            }
        });

       btnCopyData.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDatabse("PitambariSS.db");
            }
        });

        btnTableView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminToolsActivity.this, TableViewActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getModelNo(){
        msgShort(MobileInfo.getDeviceName());
    }



    private void brodcastTest() {

      /*  Intent locationIntent = new Intent(Constant.ACTION_AUTO_UPDATE);
        locationIntent.putExtra("key", "Test from admin tools");

        try {
            getBaseContext().sendBroadcast(locationIntent);
        } catch (Exception e) {

        }*/
    }

    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;

    private void startLocationScheduler(Context _context){

        Intent alarmIntent = new Intent(_context, LocationLogReceiver.class);
        boolean isLocationAlaramRunning = (PendingIntent.getBroadcast(_context, Constant.ACTION_ID_LOCATION_LOG_ALARM, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (isLocationAlaramRunning) {
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, Constant.ACTION_ID_LOCATION_LOG_ALARM, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)_context.getSystemService(Context.ALARM_SERVICE);
        int interval = (1000 * 60) * 1;

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() , interval, pendingIntent);
    }

    private void alaramTest() {

       startLocationScheduler(this);
    }

    private void clearOrders() {

        new SalesOrderRepo().deleteAll(AppControl.getmEmployeeId());
        new SalesOrderPreviousRepo().deleteAll(AppControl.getmEmployeeId());

        msgShort("Cleared successfully!");
    }

//    private void doManualUpload() {
//
//        Log.i(TAG,"Manual Upload Start");
//
//        Intent intent = new Intent(Constant.ACTION_AUTO_UPDATE);
//        getApplicationContext().sendBroadcast(intent);
//    }

    private void tableInfo() {

        try {
            Intent iStatistics = new Intent(AdminToolsActivity.this, TableStatisticActivity.class);
            startActivity(iStatistics);
        } catch (Exception ex) {
            errMsg("Table Info", ex);
        }
    }

    private void clearLeaves() {
        new LeaveRepo().deleteAll(AppControl.getmEmployeeId());
    }

    private void exportDatabse(String databaseName) {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+ getPackageName()+ "//databases//"+databaseName+"";
                String backupDBPath = "export.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            msgShort("backup successful!");
        } catch (Exception e) {
            msgShort("Error! " + e.getMessage());
        }
    }
}
