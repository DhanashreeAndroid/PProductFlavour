package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 27/02/2017.
 */

public class ActivityLogRepo {
    public static String createTable() {

        String qry;

        qry = "CREATE TABLE " + ActivityLog.TABLE + "("
                + ActivityLog.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ActivityLog.COL_SO_ID + " INT ,"
                + ActivityLog.COL_IMEI + " TEXT ,"
                + ActivityLog.COL_ACTIVITY + " TEXT ,"
                + ActivityLog.COL_STATUS + " DATE ,"
                + ActivityLog.COL_CREATED_DATE_TIME + " DATE, "
                + ActivityLog.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(ActivityLog obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(ActivityLog.COL_SO_ID, obj.getSoId());
        values.put(ActivityLog.COL_IMEI, obj.getImei());
        values.put(ActivityLog.COL_ACTIVITY, obj.getActivity());
        values.put(ActivityLog.COL_STATUS, obj.getStatus());
        values.put(ActivityLog.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(ActivityLog.COL_IS_POSTED, false);

        try {
            db.insert(ActivityLog.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(ActivityLog[] objList, SQLiteDatabase db) {

        for (ActivityLog productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + ActivityLog.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();

    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + ActivityLog.TABLE + " ");
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void Log(ActivityLog log){
        new ActivityLogRepo().insert(log);
    }

    public void insert(ActivityLog objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ActivityLog[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(int soId) {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + ActivityLog.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();

    }

    public void markPosted() {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + ActivityLog.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<ActivityLog> getLogs(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1 : 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + ActivityLog.TABLE + "";
        sql += " WHERE is_posted='" + is_posted + "'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);

    }

    public List<ActivityLog> getLogsNotPosted() {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();


        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + ActivityLog.TABLE + "";
        sql += " WHERE is_posted='0'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }

//    public List<ActivityLog> getLogsNotPosted() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + ActivityLog.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            return null;
//        }
//
//        return fillOrders(c);
//
//    }

    private List<ActivityLog> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(ActivityLog.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(ActivityLog.COL_SO_ID);
        int ix_imei = c.getColumnIndexOrThrow(ActivityLog.COL_IMEI);
        int ix_activity = c.getColumnIndexOrThrow(ActivityLog.COL_ACTIVITY);
        int ix_status = c.getColumnIndexOrThrow(ActivityLog.COL_STATUS);
        int ix_created_date_time = c.getColumnIndexOrThrow(ActivityLog.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(ActivityLog.COL_IS_POSTED);

        ActivityLog coldCall;
        List<ActivityLog> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new ActivityLog();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setImei(c.getString(ix_imei));
            coldCall.setActivity(c.getString(ix_activity));
            coldCall.setStatus(c.getString(ix_status));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }
}
