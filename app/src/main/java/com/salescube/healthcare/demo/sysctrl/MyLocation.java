package com.salescube.healthcare.demo.sysctrl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 14/02/2017.
 */


public class MyLocation {

    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private Context mContext;

    public boolean getLocation(Context context, LocationResult result) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        //I use LocationResult callback class to pass location value from MyLocation to user code.
        mContext = context;
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {
            locationResult.gotLocation("", null);
            return false;
        }

        if (gps_enabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps, Looper.getMainLooper());
        }

        if (network_enabled) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork, Looper.getMainLooper());
        }

        // Last Location Disabled
        int timeout = 60000 * 3;
        timer1 = new Timer();
        timer1.schedule(new LocationTimeOut(), timeout);

        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            timer1.cancel();
            locationResult.gotLocation("GPS", location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            timer1.cancel();
            locationResult.gotLocation("Network", location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class LocationTimeOut extends TimerTask {
        @Override
        public void run() {

            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            locationResult.gotLocation("", null);
        }
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationResult.gotLocation("", null);
                return;
            }

            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled) {
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (network_enabled) {
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime()) {
                    locationResult.gotLocation("GPS-Last", gps_loc);
                } else {
                    locationResult.gotLocation("Network-Last", net_loc);
                }
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation("GPS-Last", gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation("Network-Last", net_loc);
                return;
            }
            locationResult.gotLocation("", null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(String provider, Location location);
    }

}
