package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vOtherWorkReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 03/11/2016.
 */

public class OtherWorkRepo {
    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + OtherWork.TABLE + "("
                + OtherWork.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + OtherWork.COL_SO_ID + " INT ,"
                + OtherWork.COL_AGENT_ID + " INT ,"
                + OtherWork.COL_ORDER_DATE + " DATE ,"
                + OtherWork.COL_OTHER_WORK + " TEXT ,"
                + OtherWork.COL_CREATED_DATE_TIME + " DATETIME, "
                + OtherWork.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(OtherWork obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(OtherWork.COL_SO_ID, obj.getSoId());
        values.put(OtherWork.COL_AGENT_ID, obj.getAgentId());
        values.put(OtherWork.COL_ORDER_DATE, DateFunc.getDateStr(obj.getOrderDate()));
        values.put(OtherWork.COL_OTHER_WORK, obj.getOrherWork());
        values.put(OtherWork.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(OtherWork.COL_IS_POSTED, false);

        try {
            db.insert(OtherWork.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(OtherWork[] objList, SQLiteDatabase db) {

        for (OtherWork productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + OtherWork.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(OtherWork objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(OtherWork[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(List<OtherWork> list) {


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values;

        db.beginTransaction();

        for(OtherWork item: list) {

            values = new ContentValues();
            values.put(OtherWork.COL_IS_POSTED, true);
            db.update(OtherWork.TABLE,values,"id=?",new String[] { String.valueOf(item.getId()) });
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<OtherWork> getOtherWork(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1 : 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + OtherWork.TABLE + "";
        sql += " WHERE is_posted='" + is_posted + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillOrders(c);
    }

    public List<OtherWork> getOtherWorkNotPosted(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + OtherWork.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillOrders(c);
    }

//    public List<OtherWork> getOtherWorkNotPosted() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + OtherWork.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//
//            return null;
//        }
//
//        return fillOrders(c);
//    }

    public List<vOtherWorkReport> getOtherWorkReport(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

//        sql = "";
//        sql += " SELECT id, order_date, IFNULL(agent_name,'-') agent_name, other_work ";
//        sql += " FROM " + OtherWork.TABLE + " A";
//        sql += " LEFT JOIN (    SELECT DISTINCT agent_id, agent_name ";
//        sql += "                FROM "+ AgentLocality.TABLE +" ";
//        sql += " ) B ON (A.agent_id=B.agent_id)";
//        sql += " WHERE a.so_id = "+ userId+"";
//        sql += " ORDER BY created_date_time desc ";

        sql = "";
        sql += " SELECT id, order_date, other_work ";
        sql += " FROM " + OtherWork.TABLE + " A";
        sql += " WHERE a.so_id = "+ userId+"";
        sql += " ORDER BY created_date_time desc ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_order_date = c.getColumnIndexOrThrow("order_date");
     //   int ix_agent_name = c.getColumnIndexOrThrow("agent_name");
        int ix_other_work = c.getColumnIndexOrThrow("other_work");

        vOtherWorkReport work;
        List<vOtherWorkReport> report = new ArrayList<>();

        while (c.moveToNext()){
            work = new vOtherWorkReport();
            work.setTxnDate(DateFunc.getDate(c.getString(ix_order_date)));
            //work.setAgent(c.getString(ix_agent_name));
            work.setWork(c.getString(ix_other_work));
            report.add(work);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();

        return report;
    }

    private List<OtherWork> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(OtherWork.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(OtherWork.COL_SO_ID);
        int ix_agent_id = c.getColumnIndexOrThrow(OtherWork.COL_AGENT_ID);
        int ix_tr_date = c.getColumnIndexOrThrow(OtherWork.COL_ORDER_DATE);
        int ix_other_work = c.getColumnIndexOrThrow(OtherWork.COL_OTHER_WORK);
        int ix_created_date_time = c.getColumnIndexOrThrow(OtherWork.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(OtherWork.COL_IS_POSTED);

        OtherWork coldCall;
        List<OtherWork> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new OtherWork();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setAgentId(c.getInt(ix_agent_id));
            coldCall.setOrderDate(DateFunc.getDate(c.getString(ix_tr_date)));
            coldCall.setOrherWork(c.getString(ix_other_work));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }
}
