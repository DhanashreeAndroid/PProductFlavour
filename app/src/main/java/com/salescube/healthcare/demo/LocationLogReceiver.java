package com.salescube.healthcare.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class LocationLogReceiver extends BroadcastReceiver {
    public LocationLogReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // String curtime = (String) android.text.format.DateFormat.format("kk:mm:ss", new java.util.Date());
        // Toast.makeText(context, "Location log broadcast " + curtime, Toast.LENGTH_LONG).show();

        Log.i("AUTO_LOG_TEST","auto_log_received");

        if (!isValidTiming()) {
            Log.i("SMARTSALES","LOCATION_AUTO_LOG_NOT_VALID_TIMING");
            return;
        }

        try {

//            Intent mIntent = new Intent(context, AutoLocationService.class);
//            LocationLog objLoc = new LocationLog();
//
//            objLoc.setSoId(0);
//            objLoc.setImei("");
//            objLoc.setAppShopId("");
//            objLoc.setEvent("AUTO_LOG");
//
//            mIntent.putExtra("LOG", objLoc);
//            AutoLocationService.enqueueWork(context, mIntent);

        } catch (Exception e) {
            Log.e("AUTO_LOG_TEST", e.getMessage());
        }
    }

    private boolean isValidTiming() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int ampm = calendar.get(Calendar.AM_PM);

        boolean correct = false;
        if ((hour >= 9 && ampm == 0) || (hour <= 6 && ampm == 1)) {
            correct = true;
        }

        return correct;
    }

}
