package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.view.vSysDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 17/10/2016.
 */

public class SysDateRepo extends RepoBase {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + SysDate.TABLE + "("
                + SysDate.COL_ID + " PRIMARY KEY ,"
                + SysDate.COL_SO_ID + " INT, "
                + SysDate.COL_IMEI + " TEXT, "
                + SysDate.COL_TR_DATE + " DATE, "
                + SysDate.COL_CREATED_DATE_TIME + " DATETIME, "
                + SysDate.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(SysDate date, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        values.put(SysDate.COL_SO_ID, date.getSoId());
        values.put(SysDate.COL_IMEI, MobileInfo.getIMEI());
        values.put(SysDate.COL_TR_DATE, dateFormat.format(date.getTrDate()));
        values.put(SysDate.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr(date.getCreatedDateTime()));
        values.put(SysDate.COL_IS_POSTED,0);

        db.insert(SysDate.TABLE, null, values);

    }

    private void insert(SysDate[] products, SQLiteDatabase db) {

        for (SysDate productRate : products) {
            insert(productRate, db);
        }
    }

    public void  deleteAll(int soId, Date todayDate){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql += " DELETE FROM " + SysDate.TABLE + "";
        sql += " WHERE  so_id=" + soId + " ";
        sql += " AND    tr_date <> '" + DateFunc.getDateStr(todayDate) + "'";

        db.execSQL(sql);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(SysDate products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(products, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(SysDate[] products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(products, db);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void insert(int soId, Date sysDate){

        SysDate obj = new SysDate();

        obj.setSoId(soId);
        obj.setTrDate(sysDate);
        obj.setCreatedDateTime(DateFunc.getTodaysDateTime());

        insert(obj);
    }

    public Date getMaxDate(int soId){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT max(tr_date) ";
        sql += " FROM " + SysDate.TABLE;
        sql += " WHERE so_id =" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        Date todayDate = null;
        while (c.moveToNext()){
            todayDate  = DateFunc.getDate(c.getString(0));
        }

        closeCursor(c);
        return todayDate;
    }

    public void markPosted() {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SysDate.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<SysDate> getDatesForUpload(int soId) {

        List<SysDate> objList = new ArrayList<SysDate>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT so_id, tr_date, imei, created_date_time, is_posted";
        sql += " FROM " + SysDate.TABLE;
        sql += " WHERE  is_posted ='0'";
        sql += " AND so_id=" + soId + "";
        sql += " ORDER BY tr_date desc ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objList;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return objList;
        }

        int ix_so_id = c.getColumnIndexOrThrow("so_id");
        int ix_imei = c.getColumnIndexOrThrow("imei");
        int ix_tr_Date = c.getColumnIndexOrThrow("tr_date");
        int ix_created_date_time = c.getColumnIndexOrThrow("created_date_time");
        int ix_isposted = c.getColumnIndexOrThrow("is_posted");

        SysDate obj;
        while (c.moveToNext()) {

            obj = new SysDate();

            obj.setSoId(c.getInt(ix_so_id));
            obj.setImei(c.getString(ix_imei));
            obj.setTrDate(DateFunc.getDate(c.getString(ix_tr_Date)));
            obj.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            objList.add(obj);
        }

        closeCursor(c);
        return objList;
    }

//    public List<SysDate> getDatesForUpload() {
//
//        List<SysDate> objList = new ArrayList<SysDate>();
//
//        String sql = "";
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        Cursor c;
//
//        sql = "";
//        sql += " SELECT so_id, tr_date, imei, created_date_time, is_posted";
//        sql += " FROM " + SysDate.TABLE;
//        sql += " WHERE  is_posted ='0'";
//        sql += " ORDER BY tr_date desc ";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception ex) {
//            return objList;
//        }
//
//        if (c.getCount() == 0) {
//            return objList;
//        }
//
//        int ix_so_id = c.getColumnIndexOrThrow("so_id");
//        int ix_imei = c.getColumnIndexOrThrow("imei");
//        int ix_tr_Date = c.getColumnIndexOrThrow("tr_date");
//        int ix_created_date_time = c.getColumnIndexOrThrow("created_date_time");
//        int ix_isposted = c.getColumnIndexOrThrow("is_posted");
//
//        SysDate obj;
//        while (c.moveToNext()) {
//
//            obj = new SysDate();
//
//            obj.setSoId(c.getInt(ix_so_id));
//            obj.setImei(c.getString(ix_imei));
//            obj.setTrDate(DateFunc.getDate(c.getString(ix_tr_Date)));
//            obj.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
//            objList.add(obj);
//        }
//
//        return objList;
//    }

    public List<vSysDate> getDates(int soid) {

        List<vSysDate> objList = new ArrayList<vSysDate>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT so_id, tr_date, created_date_time, is_posted";
        sql += " FROM " + SysDate.TABLE;
        sql += " WHERE so_id =" + soid + "";
        sql += " ORDER BY tr_date desc ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objList;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return objList;
        }

        int ix_so_id = c.getColumnIndexOrThrow("so_id");
        int ix_tr_Date = c.getColumnIndexOrThrow("tr_date");
        int ix_created_date_time = c.getColumnIndexOrThrow("created_date_time");
        int ix_isposted = c.getColumnIndexOrThrow("is_posted");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        vSysDate obj;
        while (c.moveToNext()) {

            obj = new vSysDate();
            try {
                obj.setTrDate(sdf.parse(c.getString(ix_tr_Date)));
            } catch (ParseException e) {

            }
            objList.add(obj);
        }

        closeCursor(c);
        return objList;
    }
}
