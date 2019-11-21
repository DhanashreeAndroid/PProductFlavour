package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vLeaveReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 03/11/2016.
 */

public class LeaveRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Leave.TABLE + "("
                + Leave.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Leave.COL_SO_ID + " INT ,"
                + Leave.COL_LEAVE_TYPE + " TEXT ,"
                + Leave.COL_DURATION + " TEXT ,"
                + Leave.COL_ORDER_DATE + " DATE ,"
                + Leave.COL_FROM_DATE + " DATE ,"
                + Leave.COL_TO_DATE + " DATE ,"
                + Leave.COL_CO_FOR + " DATE ,"
                + Leave.COL_CREATED_DATE_TIME + " DATETIME, "
                + Leave.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(Leave obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Leave.COL_SO_ID, obj.getSoId());
        values.put(Leave.COL_LEAVE_TYPE, obj.getLeaveType());
        values.put(Leave.COL_DURATION, obj.getDuration());
        values.put(Leave.COL_ORDER_DATE, DateFunc.getDateStr(obj.getOrderDate()));
        values.put(Leave.COL_FROM_DATE, DateFunc.getDateStr(obj.getFromDate()));
        values.put(Leave.COL_TO_DATE, DateFunc.getDateStr(obj.getToDate()));
        if (obj.getLeaveType().equals("CO")) {
            values.put(Leave.COL_CO_FOR, DateFunc.getDateStr(obj.getCoFor()));
        }
        values.put(Leave.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(Leave.COL_IS_POSTED, false);

        try {
            db.insert(Leave.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(Leave[] objList, SQLiteDatabase db) {

        for (Leave productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + Leave.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Leave objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Leave[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(List<Leave> list) {


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values;

        db.beginTransaction();

        for(Leave item: list) {

            values = new ContentValues();
            values.put(Leave.COL_IS_POSTED, true);
            db.update(Leave.TABLE,values,"id=?",new String[] { String.valueOf(item.getId()) });
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Leave> getLeave(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1 : 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Leave.TABLE + "";
        sql += " WHERE is_posted='" + is_posted + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillOrders(c);
    }

    public List<Leave> getLeaveNotPosted(int soId) {

        Cursor c;
        String sql;
        List<Leave> leaves = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Leave.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND so_id=" + soId + "";

        c = db.rawQuery(sql, null);
        leaves = fillOrders(c);

        return leaves;
    }

//    public List<Leave> getLeaveNotPosted() {
//
//        Cursor c;
//        String sql;
//        List<Leave> leaves = new ArrayList<>();
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + Leave.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        c = db.rawQuery(sql, null);
//        leaves = fillOrders(c);
//
//        return leaves;
//    }

    public List<vLeaveReport> getLeaveReport(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT id, from_date,to_date, leave_type, duration, co_for ";
        sql += " FROM " + Leave.TABLE + "";
        sql += " WHERE so_id = "+ userId+"";
        sql += " ORDER BY created_date_time desc ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_from_date = c.getColumnIndexOrThrow("from_date");
        int ix_to_date = c.getColumnIndexOrThrow("to_date");
        int ix_leave_type = c.getColumnIndexOrThrow("leave_type");
        int ix_duration = c.getColumnIndexOrThrow("duration");
        int ix_co_for = c.getColumnIndexOrThrow("co_for");

        vLeaveReport work;
        List<vLeaveReport> report = new ArrayList<>();

        while (c.moveToNext()){

            work = new vLeaveReport();
            work.setFromDate(DateFunc.getDate(c.getString(ix_from_date)));
            work.setToDate(DateFunc.getDate(c.getString(ix_to_date)));
            work.setLeaveType(c.getString(ix_leave_type));
            work.setDuration(c.getString(ix_duration));
            work.setCoFor(DateFunc.getDate(c.getString(ix_co_for)));
            report.add(work);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();

        return report;
    }

    private List<Leave> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(Leave.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(Leave.COL_SO_ID);
        int ix_leave_type = c.getColumnIndexOrThrow(Leave.COL_LEAVE_TYPE);
        int ix_duration = c.getColumnIndexOrThrow(Leave.COL_DURATION);
        int ix_order_date = c.getColumnIndexOrThrow(Leave.COL_ORDER_DATE);
        int ix_from_date = c.getColumnIndexOrThrow(Leave.COL_FROM_DATE);
        int ix_to_date = c.getColumnIndexOrThrow(Leave.COL_TO_DATE);
        int ix_co_for = c.getColumnIndexOrThrow(Leave.COL_CO_FOR);
        int ix_created_date_time = c.getColumnIndexOrThrow(Leave.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(Leave.COL_IS_POSTED);

        Leave coldCall;
        List<Leave> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new Leave();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setLeaveType(c.getString(ix_leave_type));
            coldCall.setDuration(c.getString(ix_duration));
            coldCall.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            coldCall.setFromDate(DateFunc.getDate(c.getString(ix_from_date)));
            coldCall.setToDate(DateFunc.getDate(c.getString(ix_to_date)));

            if (coldCall.getLeaveType().equals("CO")){
                coldCall.setCoFor(DateFunc.getDate(c.getString(ix_co_for)));
            }

            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }

}
