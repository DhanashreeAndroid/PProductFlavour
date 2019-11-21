package com.salescube.healthcare.demo.func;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.salescube.healthcare.demo.app.App;

/**
 * Created by user on 26/08/2016.
 */
public final class MobileInfo {

    // bydefault imei number is for primary sim
    // getDeviceId(slot) implemented in API level 23

    public static String getDeviceName(){
        String manufacture = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacture)){
            return capitalize(model);
        }else{
            return capitalize(manufacture) + " " + model;
        }
    }

    private static String capitalize(String value){
        if (value == null || value.length() == 0){
            return "";
        }

        char first = value.charAt(0);
        if (Character.isUpperCase(first)){
            return value;
        }else {
            return Character.toUpperCase(first) + value.substring(2);
        }
    }

   // Source: http://stackoverflow.com/questions/1972381/how-to-get-the-devices-imei-esn-programmatically-in-android
   public static  String getIMEI(){


       String imei = "";

       try {
           imei = "";
           TelephonyManager tm = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
           if (tm.getDeviceId() != null){
               imei = tm.getDeviceId();
           }
       } catch (Exception e) {

       }

       return  imei;

    }

    public static  String getIMEI(Context _context){


        String imei = "";

        try {
            imei = "";
            TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getDeviceId() != null){
                imei = tm.getDeviceId();
            }
        } catch (Exception e) {

        }

        return  imei;

    }

    // We can find through telephoneyManager
    // SIM Serial Number
    // Operator Name
    // Operator ID
    // SIM Status

    public static  String getSimSerialNo(Activity activity){

        String serialNo = "";
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        serialNo =  tm.getSimSerialNumber();
        return  serialNo;
    }
}
