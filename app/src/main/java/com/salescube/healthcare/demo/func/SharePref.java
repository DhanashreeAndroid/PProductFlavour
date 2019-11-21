package com.salescube.healthcare.demo.func;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SharePref {
    private final String TOKEN = "token";
    private final String tokenStatus = "status";
    private final String MY_PREF = "share_pref";

    private final String MOBILE_VERSION  = "mobileVersion";
    private final String SOID  = "soId";
    private final String APP_VERSION  = "appVersion";
    private final String CURRENT_DATE_TIME  = "date_time";
    private final String TYPE  = "type";
    private final String SYNC_STATUS = "status";
    private final String DAY_IN = "day_in";
    private final String DAY_OUT = "day_out";

    private Context context;

    public SharePref(Context _context) {
        context = _context;
    }

    public String getSYNC_STATUS() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(SYNC_STATUS, "");
    }

    public void setSYNC_STATUS(String value) {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(SYNC_STATUS, value);
        ed.apply();
    }

    public String getToken() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(TOKEN, "");
    }


    public void setToken(String value) {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(TOKEN, value);
        ed.apply();
    }

    public String getTokenStatus() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(tokenStatus, "");
    }

    public  void  setTokenStatus(String value){
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(tokenStatus, value);
        ed.apply();
    }


    public void SaveData(String mobileVersion, int SoId, String AppVer , String DateTime , String type){
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(MOBILE_VERSION, mobileVersion);
        ed.putInt(SOID, SoId);
        ed.putString(APP_VERSION, AppVer);
        ed.putString(CURRENT_DATE_TIME, DateTime);
        ed.putString(TYPE, type);
        ed.apply();

    }

    public String getMOBILE_VERSION() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(MOBILE_VERSION, "0");
    }

    public int getSOID() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getInt(SOID, 0);
    }

    public String getAPP_VERSION() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(APP_VERSION, "0");
    }

    public String getCURRENT_DATE_TIME() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(CURRENT_DATE_TIME, "0");
    }

    public String getTYPE() {
        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sf.getString(TYPE, "0");
    }

    public void setDayIn() {
        String today = DateFunc.getDateStr("dd/MM/yyyy");

        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(DAY_IN, today);
        ed.commit();
    }

    public boolean isDayInDone() {

        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        String dateStr = sf.getString(DAY_IN, "");
        if (dateStr == "") {
            return false;
        }

        Date attDate = DateFunc.getDate(dateStr, "dd/MM/yyyy");
        Date todayDate = DateFunc.getDate();

        if (DateFunc.isSameDate(attDate, todayDate)) {
            return true;
        }

        return false;
    }

    public void setDayOut() {
        String today = DateFunc.getDateStr("dd/MM/yyyy");

        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sf.edit();
        ed.putString(DAY_OUT, today);
        ed.commit();
    }

    public boolean isDayOutDone() {

        SharedPreferences sf = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        String dateStr = sf.getString(DAY_OUT, "");
        if (dateStr == "") {
            return false;
        }

        Date attDate = DateFunc.getDate(dateStr, "dd/MM/yyyy");
        Date todayDate = DateFunc.getDate();

        if (DateFunc.isSameDate(attDate, todayDate)) {
            return true;
        }

        return false;
    }
}
