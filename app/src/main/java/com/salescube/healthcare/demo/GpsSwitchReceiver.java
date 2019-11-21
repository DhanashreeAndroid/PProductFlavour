package com.salescube.healthcare.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.repo.ActivityLogRepo;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.Constant;

public class GpsSwitchReceiver extends BroadcastReceiver {

    private static boolean mLastEnabled;

    public GpsSwitchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isEnabled = UtilityFunc.isGPSEnabled(context);

        if (isEnabled == mLastEnabled){
            return;
        }

        mLastEnabled = isEnabled;

        String imei = MobileInfo.getIMEI(context);
        ActivityLog log = new ActivityLog();
        log.setSoId(0);
        log.setImei(imei);
        log.setActivity(Constant.Activity.GPS);

        if (isEnabled){
            Log.i("SMARTSALES","GPS-ON");
            log.setStatus("ON");
            Toast.makeText(context,"GPS SWITCHED ON",Toast.LENGTH_SHORT).show();
        }else {
            Log.i("SMARTSALES","GPS-OFF");
            log.setStatus("OFF");
            Toast.makeText(context,"GPS SWITCHED OFF",Toast.LENGTH_SHORT).show();
        }

        try {
            new ActivityLogRepo().insert(log);
        } catch (Exception e) {
            Toast.makeText(context,"Error while logging gps activity!",Toast.LENGTH_SHORT).show();
        }
    }
}
