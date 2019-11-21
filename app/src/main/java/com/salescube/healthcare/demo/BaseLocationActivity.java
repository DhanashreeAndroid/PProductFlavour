package com.salescube.healthcare.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by user on 26/10/2016.
 */

abstract class BaseLocationActivity extends BaseActivity {

    protected String[] mLocation= new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int recsultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (recsultCode == RESULT_OK){
                mLocation = intent.getStringArrayExtra("location");
            }
        }
    };

//    protected void captureLocation(String appShopId, String event) {
//
//        Intent intent = new Intent(Constant.ACTION_AUTO_LOCATION_UPDATE);
//
//        LocationLog objLoc = new LocationLog();
//        objLoc.setSoId(AppControl.getmEmployeeId());
//        objLoc.setImei(AppControl.getImei());
//        objLoc.setAppShopId(appShopId);
//        objLoc.setEvent(event);
//
//        intent.putExtra("LOG", objLoc);
//        getBaseContext().sendBroadcast(intent);
//
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



}
