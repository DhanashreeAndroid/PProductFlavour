package com.salescube.healthcare.demo.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.AppEvent;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LocationJobWorker extends Worker {

    private static final  String TAG = "LocationJobWorker";

    public LocationJobWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();


        Log.d(TAG, "running location job service");

        LocationLog objLoc = new LocationLog();
        objLoc.setSoId(AppControl.getmEmployeeId());
        objLoc.setImei(AppControl.getImei());
        objLoc.setAppShopId("");
        objLoc.setEvent(AppEvent.EVENT_AUTO);

        LocationJobIntentService.enqueueWork(context,  objLoc);

        return Result.success();
    }

}
