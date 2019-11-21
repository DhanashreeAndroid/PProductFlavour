package com.salescube.healthcare.demo.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.ApiClient;
import com.salescube.healthcare.demo.ApiService;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.LocationLogRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.MyLocation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.salescube.healthcare.demo.func.MobileInfo.getIMEI;

public class LocationJobIntentService extends JobIntentService {

    public final static String TAG = "LocationService";

    public LocationJobIntentService() {
        super();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Log.i(TAG, "service started");

        final User objUser = new UserRepo().getOwnerUser();

        if (objUser == null) {
            return;
        }


        try {
            fireLocation(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public static void enqueueWork(Context context, LocationLog locationLog) {

        Intent intent = new Intent(context, LocationJobIntentService.class);
        intent.putExtra("LOG", locationLog);
        enqueueWork(context, LocationJobIntentService.class, 1299, intent);
    }


    @Override
    public void onDestroy() {

        Log.i(TAG, "service destroyed");
        super.onDestroy();
    }

    public static boolean IsConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            return false;
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private static boolean isServiceIsRunning(Context context, Class serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fireLocation(Intent intent) {

        LocationLog objLoc = intent.getParcelableExtra("LOG");
        if (objLoc == null) {
            Log.e(TAG, "location intent data empty");
            return;
        }

        try {
            captureLocation(objLoc);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    protected void captureLocation(final LocationLog log) {

        if (!UtilityFunc.isGPSEnabled(LocationJobIntentService.this)) {

            Log.i(TAG, "GPS SWITCHED OFF BY USER");

            log.setLatitude("0");
            log.setLongitude("0");
            log.setExtraInfo("GPS SWITCHED OFF BY USER");

            try {
                saveLocation(log);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return;
        }

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

            @Override
            public void gotLocation(String provider, Location location) {
                double[] latLong = new double[2];

                latLong[0] = 0;
                latLong[1] = 0;

                if (location != null) {
                    latLong[0] = location.getLatitude();
                    latLong[1] = location.getLongitude();
                } else {
                    log.setExtraInfo("GPS NOT AVAILABLE");
                }

                log.setLatitude(String.valueOf(latLong[0]));
                log.setLongitude(String.valueOf(latLong[1]));

                try {
                    saveLocation(log);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getBaseContext(), locationResult);
    }

    protected void saveLocation(LocationLog locLog) {

        String networkStatus = "NOT_CONNECTED";
        String imei = getIMEI();

        try {
            boolean connected = IsConnected(LocationJobIntentService.this);
            if (connected) {
                networkStatus = "CONNECTED";
            }
        } catch (Exception e) {
        }

        float battryLevel = 0;

        try {
            battryLevel = getBatteryLevel();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

        Date todayDate = null;

        try {
            todayDate = Calendar.getInstance().getTime();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        locLog.setImei(imei);
        locLog.setTxnDate(todayDate);
        locLog.setAddress("");
        locLog.setNetwork(networkStatus);
        locLog.setBattery(String.valueOf(battryLevel));
        locLog.setAppVersion(UtilityFunc.getAppVersion(getApplicationContext()));

        LocationLogRepo.log(locLog);

        uploadLocationLogs();

    }

    private void uploadLocationLogs() {

        if (!IsConnected(LocationJobIntentService.this)) {
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        final String TAG2 = "uploadLocationLogs";

        final LocationLogRepo objLocation = new LocationLogRepo();
        final List<LocationLog> orders = objLocation.getLogsNotPosted();

        Log.i(TAG, "uploading location log count " + String.valueOf(orders.size()));

        if (orders.size() == 0) {
            return;
        }

        Double lat, lng;

        Geocoder geoCoder;
        List<Address> addresss;
        String formatedAddress;

        geoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);

        for (LocationLog locLog : orders) {


            if (!Parse.toStr(locLog.getAddress()).equals("")) {
                continue;
            }

            if (locLog.getLatitude() == null || locLog.getLongitude() == null) {
                continue;
            }

            lat = Parse.toDbl(locLog.getLatitude());
            lng = Parse.toDbl(locLog.getLongitude());

            if (!lat.equals(0) || !lng.equals(0)) {

                try {
                    addresss = geoCoder.getFromLocation(lat, lng, 1);
                    Log.i(TAG, "uploading location done");
                } catch (IOException e) {
                    continue;
                }

                if (addresss != null) {
                    formatedAddress = "";
                    if (addresss.size() > 0) {
                        locLog.setAddress(addresss.get(0).getAddressLine(0));
                        int maxLines = addresss.get(0).getMaxAddressLineIndex();
                        if (maxLines > 0) {
                            for (int i = 0; i < maxLines; i++) {
                                formatedAddress += addresss.get(0).getAddressLine(i) + ", ";
                            }
                            locLog.setAddress(formatedAddress);
                        }
                    }
                    objLocation.updateAddress(locLog, formatedAddress);
                }

            } else {
                objLocation.updateAddress(locLog, "NO-GPS CONNECTIVITY");
            }

        }

        Call<Void> dataList = apiService.postLocationLog(orders);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        objLocation.deleteAll(orders);
                    } catch (Exception ex) {
                        Log.i(TAG, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    Log.i(TAG, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

    }

    private String filterResponseErr(Response response) {

        String message;
        try {
            message = response.errorBody().string();
            if (message.equals("")) {
                message = response.raw().message();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        return message;
    }

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
