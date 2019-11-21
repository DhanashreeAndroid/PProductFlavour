package com.salescube.healthcare.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.salescube.healthcare.demo.service.DataJobIntentService;
import com.salescube.healthcare.demo.service.JobWorkScheduler;

/**
 * Created by user on 07/11/2016.
 */

public class BootStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        JobWorkScheduler.data();
        JobWorkScheduler.location();

        // fire pending log
        DataJobIntentService.enqueueWork(context);

        // TODO: LOCATION LOG REMOVED
        // fire location log
//        LocationLog objLoc = new LocationLog();
//        objLoc.setSoId(AppControl.getmEmployeeId());
//        objLoc.setImei(AppControl.getImei());
//        objLoc.setAppShopId("");
//        objLoc.setEvent(AppEvent.EVENT_AUTO);
//
//        LocationJobIntentService.enqueueWork(context, objLoc);


    }
}
