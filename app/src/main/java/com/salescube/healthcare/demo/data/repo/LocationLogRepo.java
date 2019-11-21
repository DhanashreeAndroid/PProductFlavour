package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.salescube.healthcare.demo.BuildConfig;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.UtilityFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26/10/2016.
 */

public class LocationLogRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + LocationLog.TABLE + "("
                + LocationLog.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LocationLog.COL_SO_ID + " INT ,"
                + LocationLog.COL_IMEI + " TEXT ,"
                + LocationLog.COL_APP_SHOP_ID + " TEXT ,"
                + LocationLog.COL_TXN_DATE + " DATE ,"
                + LocationLog.COL_EVENT_TAG + " TEXT ,"
                + LocationLog.COL_LATITUDE + " TEXT ,"
                + LocationLog.COL_LONGITUDE + " TEXT ,"
                + LocationLog.COL_ADDRESS + " TEXT, "
                + LocationLog.COL_EXTRA_INFO + " TEXT, "
                + LocationLog.COL_NETWORK + " TEXT, "
                + LocationLog.COL_BATTERY + " TEXT, "
                + LocationLog.COL_DEVICE_MODEL + " TEXT, "
                + LocationLog.COL_OS_VERSION + " TEXT, "
                + LocationLog.COL_APP_VERSION + " TEXT, "
                + LocationLog.COL_CREATED_DATE_TIME + " DATE, "
                + LocationLog.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(LocationLog obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(LocationLog.COL_SO_ID, obj.getSoId());
        values.put(LocationLog.COL_IMEI, obj.getImei());
        values.put(LocationLog.COL_APP_SHOP_ID, obj.getAppShopId());
        values.put(LocationLog.COL_TXN_DATE, DateFunc.getDateStr(obj.getTxnDate()));
        values.put(LocationLog.COL_EVENT_TAG, obj.getEventTag());
        values.put(LocationLog.COL_LATITUDE, obj.getLatitude());
        values.put(LocationLog.COL_LONGITUDE, obj.getLongitude());
        values.put(LocationLog.COL_ADDRESS, obj.getAddress());
        values.put(LocationLog.COL_EXTRA_INFO, obj.getExtraInfo());
        values.put(LocationLog.COL_NETWORK, obj.getNetwork());
        values.put(LocationLog.COL_BATTERY, obj.getBattery());
        values.put(LocationLog.COL_DEVICE_MODEL, MobileInfo.getDeviceName());
        values.put(LocationLog.COL_OS_VERSION, Build.VERSION.RELEASE);
        values.put(LocationLog.COL_APP_VERSION, obj.getAppVersion());
        values.put(LocationLog.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(LocationLog.COL_IS_POSTED, false);

        try {
            db.insert(LocationLog.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void updateAddress(LocationLog log, String address){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationLog.COL_ADDRESS,address);
        db.update(LocationLog.TABLE,values,"id=?",new String[] { String.valueOf(log.getId()) });
        DatabaseManager.getInstance().closeDatabase();
    }

    private void insert(LocationLog[] objList, SQLiteDatabase db) {

        for (LocationLog productRate : objList) {
            insert(productRate, db);
        }
    }

//    public void deleteAll(int soId) {
//
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        db.execSQL("DELETE FROM " + LocationLog.TABLE + " WHERE so_id=" + soId + "");
//        DatabaseManager.getInstance().closeDatabase();
//    }

    public void deleteAll(List<LocationLog> logs) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        for(LocationLog log: logs) {
            db.execSQL("DELETE FROM " + LocationLog.TABLE + " WHERE id=" + log.getId() + "");
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public static void log(LocationLog log){
        new LocationLogRepo().insert(log);
    }

    public void insert(LocationLog objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(LocationLog[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

//    public void markPosted(int soId) {
//
//        String sql = "";
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " UPDATE " + LocationLog.TABLE + "";
//        sql += " SET is_posted='1'";
//        sql += " WHERE so_id=" + soId + "";
//        sql += " AND is_posted='0'";
//        db.execSQL(sql);
//
//        DatabaseManager.getInstance().closeDatabase();
//    }
//
//    public void markPosted() {
//
//        String sql = "";
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " UPDATE " + LocationLog.TABLE + "";
//        sql += " SET is_posted='1'";
//        sql += " WHERE is_posted='0'";
//        db.execSQL(sql);
//
//        DatabaseManager.getInstance().closeDatabase();
//    }

//    public List<LocationLog> getLogs(int soId, boolean isPosted) {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        int is_posted = isPosted ? 1 : 0;
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + LocationLog.TABLE + "";
//        sql += " WHERE is_posted='" + is_posted + "'";
//        sql += " AND so_id=" + soId + "";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            return null;
//        }
//
//        return fillOrders(c);
//    }

    public List<LocationLog> getLogsNotPosted() {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();


        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + LocationLog.TABLE + "";
        sql += " WHERE is_posted='0'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }

    private List<LocationLog> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(LocationLog.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(LocationLog.COL_SO_ID);
        int ix_imei = c.getColumnIndexOrThrow(LocationLog.COL_IMEI);
        int ix_app_shop_id = c.getColumnIndexOrThrow(LocationLog.COL_APP_SHOP_ID);
        int ix_txn_date = c.getColumnIndexOrThrow(LocationLog.COL_TXN_DATE);
        int ix_event = c.getColumnIndexOrThrow(LocationLog.COL_EVENT_TAG);
        int ix_latitude = c.getColumnIndexOrThrow(LocationLog.COL_LATITUDE);
        int ix_longitude = c.getColumnIndexOrThrow(LocationLog.COL_LONGITUDE);
        int ix_address = c.getColumnIndexOrThrow(LocationLog.COL_ADDRESS);
        int ix_extra_info = c.getColumnIndexOrThrow(LocationLog.COL_EXTRA_INFO);
        int ix_network = c.getColumnIndexOrThrow(LocationLog.COL_NETWORK);
        int ix_battery = c.getColumnIndexOrThrow(LocationLog.COL_BATTERY);
        int ix_device_model = c.getColumnIndexOrThrow(LocationLog.COL_DEVICE_MODEL);
        int ix_os_version = c.getColumnIndexOrThrow(LocationLog.COL_OS_VERSION);
        int ix_app_version = c.getColumnIndexOrThrow(LocationLog.COL_APP_VERSION);
        int ix_created_date_time = c.getColumnIndexOrThrow(LocationLog.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(LocationLog.COL_IS_POSTED);

        LocationLog coldCall;
        List<LocationLog> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new LocationLog();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setImei(c.getString(ix_imei));
            coldCall.setAppShopId(c.getString(ix_app_shop_id));
            coldCall.setTxnDate(DateFunc.getDate(c.getString(ix_txn_date)));
            coldCall.setEvent(c.getString(ix_event));
            coldCall.setLatitude(c.getString(ix_latitude));
            coldCall.setLongitude(c.getString(ix_longitude));
            coldCall.setAddress(c.getString(ix_address));
            coldCall.setExtraInfo(c.getString(ix_extra_info));
            coldCall.setNetwork(c.getString(ix_network));
            coldCall.setBattery(c.getString(ix_battery));
            coldCall.setDeviceModel(c.getString(ix_device_model));
            coldCall.setOsVersion(c.getString(ix_os_version));
            coldCall.setAppVersion(c.getString(ix_app_version));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }

}
