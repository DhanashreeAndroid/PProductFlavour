package com.salescube.healthcare.demo.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DataJobWorker extends Worker {

    private static final  String TAG = "DataJobWorker";

    public DataJobWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        Log.d(TAG, "running data job service");
        DataJobIntentService.enqueueWork(context);

        return Result.success();
    }


}
