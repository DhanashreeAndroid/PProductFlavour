package com.salescube.healthcare.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.repo.ActivityLogRepo;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.sysctrl.Constant;

public class DeviceShutdownReceiver extends BroadcastReceiver {
    public DeviceShutdownReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String imei = MobileInfo.getIMEI(context);
        ActivityLog log = new ActivityLog();
        log.setSoId(0);
        log.setImei(imei);
        log.setActivity(Constant.Activity.DEVICE);
        log.setStatus("SWITCH_OFF");

        try {
            new ActivityLogRepo().insert(log);
        } catch (Exception e) {
            Toast.makeText(context,"Error while logging shutdown activity!",Toast.LENGTH_SHORT).show();
        }

    }
}
