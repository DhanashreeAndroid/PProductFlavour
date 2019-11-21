package com.salescube.healthcare.demo.service;

import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class JobWorkScheduler {

    private  static  final String TAG = "JobWorkScheduler";

    private  static  final String DATA = "data001";
    private  static  final String LOCATION = "location001";

    public static void data() {

        if (isWorkScheduled(DATA)) {
            return;
        }

        Log.i(TAG,"data upload scheduled");

        PeriodicWorkRequest.Builder requestBuilder = new PeriodicWorkRequest.Builder(DataJobWorker.class, 30, TimeUnit.MINUTES);
        PeriodicWorkRequest request = requestBuilder.build();

        WorkManager instance = WorkManager.getInstance();
        instance.enqueueUniquePeriodicWork(DATA, ExistingPeriodicWorkPolicy.KEEP , request);
    }

    public static void location() {

        // TODO:       location scheduler removed for hang issue

//        if (isWorkScheduled(LOCATION)) {
//            return;
//        }
//
//        Log.i(TAG,"location upload scheduled");
//
//        PeriodicWorkRequest.Builder requestBuilder = new PeriodicWorkRequest.Builder(LocationJobWorker.class, 15, TimeUnit.MILLISECONDS);
//        PeriodicWorkRequest request = requestBuilder.build();
//
//        WorkManager instance = WorkManager.getInstance();
//        instance.enqueueUniquePeriodicWork(LOCATION, ExistingPeriodicWorkPolicy.KEEP , request);


    }


    private static boolean isWorkScheduled(String tag) {

        WorkManager instance = WorkManager.getInstance();
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);

        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}
