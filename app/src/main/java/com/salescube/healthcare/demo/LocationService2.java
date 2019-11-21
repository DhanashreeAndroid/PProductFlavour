package com.salescube.healthcare.demo;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

public class LocationService2 extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

//    public final static String ACTION = "com.pitambari.smartsales.LocationService";
//
//    private final static String TAG = "LocationService";
//    private GoogleApiClient mApiClient;
//    private static final int LOCATION_INTERVAL = 60000 * 2;
//    private String[] mLocationStr = new String[3];
//
//    private static final TimeUnit mBrodcastTimeUnit = TimeUnit.SECONDS;
//    private static final int mBrodcastInitDelay = 5;
//    private static final int mBrodcastIntervalDelay = 5;
//
//    private static final TimeUnit mLocationLogTimeUnit = TimeUnit.MINUTES;
//    private static final int mLocationLogInitDelay = 1;
//    private static final int mLocationLogIntervalDelay = 15;
//    private Geocoder geocoder;

    public LocationService2() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        mApiClient.connect();
//        return START_STICKY;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        mApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new broadcastLocation(), mBrodcastInitDelay, mBrodcastIntervalDelay, mBrodcastTimeUnit);
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new logLocation(), mLocationLogInitDelay, mLocationLogIntervalDelay, mLocationLogTimeUnit);
//
//        geocoder = new Geocoder(this, Locale.ENGLISH);
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        requestUpdates();
//    }
//
//    private void requestUpdates() {
//
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(LOCATION_INTERVAL);
//        locationRequest.setFastestInterval(LOCATION_INTERVAL / 2);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        try {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, this);
//        } catch (Exception e) {
//            Logger.log(Logger.ERROR, TAG, e.getMessage(), e);
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        mLocationStr[0] = String.valueOf(location.getLatitude());
//        mLocationStr[1] = String.valueOf(location.getLongitude());
//
//        if (location != null) {
//            List<Address> address;
//
//            try {
//                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            } catch (IOException e) {
//                return;
//            }
//
//            if (address != null) {
//                mLocationStr[2] = "";
//                if (address.size() > 0) {
//                    int maxLines = address.get(0).getMaxAddressLineIndex();
//                    if (maxLines > 0) {
//                        for (int i = 0; i < maxLines; i++) {
//                            mLocationStr[2] += address.get(0).getAddressLine(i) + ", ";
//                        }
//                    }
//                }
//            }
//
//        }
//
//    }
//
//
//    private class broadcastLocation implements Runnable {
//
//        @Override
//        public void run() {
//
//            Intent locationIntent = new Intent(ACTION);
//            locationIntent.putExtra("resultCode", Activity.RESULT_OK);
//            locationIntent.putExtra("location", mLocationStr);
//
////            try {
////                LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(locationIntent);
////            } catch (Exception e) {
////                Logger.log(Logger.ERROR, TAG, e.getMessage(), e);
////            }
//
//        }
//    }
//
//    private class logLocation implements Runnable {
//
//        private boolean isValidTiming() {
//
//            Calendar calendar = Calendar.getInstance();
//            int hour = calendar.get(Calendar.HOUR);
//            int ampm = calendar.get(Calendar.AM_PM);
//
//            boolean correct = false;
//            if ((hour >= 9 && ampm == 0) || (hour <= 6 && ampm == 1)) {
//                correct = true;
//            }
//
//            return correct;
//        }
//
//        @Override
//        public void run() {
//
//            if (!isValidTiming()) {
//                return;
//            }
//
//            User user = null;
//            int soId = 0;
//
//            try {
//                user = new UserRepo().getOwnerUser();
//                if (user == null) {
//                    return;
//                }
//            } catch (Exception e) {
//                Logger.log(Logger.ERROR, TAG, e.getMessage(), e);
//                return;
//            }
//
//
//            soId = user.getEmployeeId();
//            // check network connection
//
//            String networkStatus = "NOT_CONNECTED";
//
//            try {
//                boolean connected = UtilityFunc.IsConnected();
//                if (connected) {
//                    networkStatus = "CONNECTED";
//                }
//            } catch (Exception e) {
//                Logger.log(Logger.ERROR, "NetworkCheck", e.getMessage(), e);
//            }
//
//            //
//
//            float battryLevel = 0;
//
//            try {
//                battryLevel = getBatteryLevel();
//            } catch (Exception e) {
//                Logger.log(Logger.ERROR, "BatteryLevel", e.getMessage(), e);
//            }
//
//
//            LocationLog loc = new LocationLog();
//            loc.setSoId(soId);
//            loc.setImei(MobileInfo.getIMEI());
//            loc.setAppShopId("AUTO");
//            loc.setEvent("AUTO_LOG");
//            loc.setLatitude(mLocationStr[0]);
//            loc.setLongitude(mLocationStr[1]);
//            loc.setAddress(mLocationStr[2]);
//            loc.setNetwork(networkStatus);
//            loc.setBattery(String.valueOf(battryLevel));
//            loc.setTxnDate(DateFunc.getDate());
//
//            try {
//                LocationLogRepo.log(loc);
//            } catch (Exception e) {
//                Logger.log(Logger.ERROR, TAG, e.getMessage(), e);
//            }
//
//        }
//
//        public float getBatteryLevel() {
//
//            Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//            // Error checking that probably isn't needed but I added just in case.
//            if (level == -1 || scale == -1) {
//                return 50.0f;
//            }
//
//            return ((float) level / (float) scale) * 100.0f;
//        }
//    }


}
