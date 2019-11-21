package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Attendance;
import com.salescube.healthcare.demo.data.model.Employee;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 07/04/2017.
 */

public class AttendanceRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Attendance.TABLE + "("
                + Attendance.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Attendance.COL_USER_ID + " INT ,"
                + Attendance.COL_TXN_DATE + " DATE ,"
                + Attendance.COL_EMP_ID + " INT ,"
                + Attendance.COL_MGR_ID + " INT ,"
                + Attendance.COL_WORK_TYPE + " TEXT, "
                + Attendance.COL_REMARK + " TEXT, "
                + Attendance.COL_CREATED_DATE_TIME + " DATETIME, "
                + Attendance.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(Attendance obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Attendance.COL_USER_ID, obj.getUserId());
        values.put(Attendance.COL_TXN_DATE, DateFunc.getDateStr(obj.getTxnDate()));
        values.put(Attendance.COL_EMP_ID, obj.getEmpId());
        values.put(Attendance.COL_MGR_ID, obj.getMgrId());
        values.put(Attendance.COL_WORK_TYPE, obj.getWorkType());
        values.put(Attendance.COL_REMARK, obj.getRemark());
        values.put(Attendance.COL_CREATED_DATE_TIME, DateFunc.getDateStr());
        values.put(Attendance.COL_IS_POSTED, false);

        try {
            db.insert(Attendance.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    public void update(Attendance obj, SQLiteDatabase db ){

        if (obj.getId() == 0){
            return;
        }

        ContentValues values = new ContentValues();

        values.put(Attendance.COL_USER_ID, Parse.toStr(obj.getUserId()));
        values.put(Attendance.COL_EMP_ID, Parse.toStr(obj.getEmpId()));
        values.put(Attendance.COL_MGR_ID, Parse.toStr(obj.getMgrId()));
        values.put(Attendance.COL_WORK_TYPE, Parse.toStr(obj.getWorkType()));
        values.put(Attendance.COL_REMARK, Parse.toStr(obj.getRemark()));

        try {
            db.update(Attendance.TABLE,values,"Id=?",new String[]{String.valueOf(obj.getId())});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }
    }

    private void insert(List<Attendance> objList, SQLiteDatabase db) {

        for (Attendance obj : objList) {
            insert(obj, db);
        }
    }

    public void deleteAll(int userId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(Attendance.TABLE,"user_id=?",new String[]{String.valueOf(userId)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int userId, Date txnDate) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(Attendance.TABLE,"user_id=? AND txn_date=? ",new String[]{String.valueOf(userId), DateFunc.getDateStr(txnDate)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int userId, int mgrId, Date txnDate) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(Attendance.TABLE,"user_id=? AND mgr_id=? AND txn_date=? ",new String[]{String.valueOf(userId), String.valueOf(mgrId), DateFunc.getDateStr(txnDate)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Attendance objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();

    }

    public void insert(List<Attendance> objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Attendance> getAttNotPosted(int soId) {

        Cursor c;
        String sql;
        List<Attendance> leaves = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Attendance.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND user_id=" + soId + "";

        c = db.rawQuery(sql, null);
        leaves = fillList(c);

        return leaves;
    }

    public void  markPosted(int userId) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        values.put(Attendance.COL_IS_POSTED, true);

        try {
            db.update(Attendance.TABLE, values, "user_id=?",new String[]{String.valueOf(userId)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Attendance> getAttendance(int userId, Date txnDate, int mgrId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT B.id, a.user_id, a.mgr_id,  a.emp_id, b.work_type, b.remark ";
        sql += " FROM " + Employee.TABLE + " A";
        sql += " LEFT JOIN " + Attendance.TABLE + " B";
        sql += "    ON(A.user_id=B.user_id AND a.emp_id=b.emp_id AND b.txn_date='"+ DateFunc.getDateStr(txnDate) +"')";
        sql += " WHERE A.user_id=" + userId + "";
        sql += " AND  (A.mgr_id=" + mgrId + " or a.emp_id=" + mgrId + ")";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
           Logger.e(e.getMessage());
           return new ArrayList<>();
        }

        int ix_user_id = c.getColumnIndexOrThrow(Attendance.COL_ID);
        int ix_emp_id = c.getColumnIndexOrThrow(Employee.COL_EMP_ID);
        int ix_mgr_id = c.getColumnIndexOrThrow(Employee.COL_MGR_ID);
        int ix_work_type = c.getColumnIndexOrThrow(Attendance.COL_WORK_TYPE);
        int ix_remark = c.getColumnIndexOrThrow(Attendance.COL_REMARK);

        Attendance obj;
        List<Attendance> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new Attendance();
            obj.setUserId(c.getInt(ix_user_id));
            obj.setTxnDate(txnDate);
            obj.setEmpId(c.getInt(ix_emp_id));
            obj.setMgrId(c.getInt(ix_mgr_id));
            obj.setWorkType(c.getString(ix_work_type));
            obj.setRemark(c.getString(ix_remark));

            objList.add(obj);
        }

        return objList;
    }

    private List<Attendance> fillList(Cursor c) {

        int ix_user_id = c.getColumnIndexOrThrow(Attendance.COL_USER_ID);
        int ix_emp_id = c.getColumnIndexOrThrow(Attendance.COL_EMP_ID);
        int ix_mgr_id = c.getColumnIndexOrThrow(Attendance.COL_MGR_ID);
        int ix_trn_date = c.getColumnIndexOrThrow(Attendance.COL_TXN_DATE);
        int ix_work_type = c.getColumnIndexOrThrow(Attendance.COL_WORK_TYPE);
        int ix_remark = c.getColumnIndexOrThrow(Attendance.COL_REMARK);
        int ix_created_date_time = c.getColumnIndexOrThrow(Attendance.COL_CREATED_DATE_TIME);

        Attendance obj;
        List<Attendance> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new Attendance();
            obj.setUserId(c.getInt(ix_user_id));
            obj.setTxnDate(DateFunc.getDate(c.getString(ix_trn_date)));
            obj.setEmpId(c.getInt(ix_emp_id));
            obj.setMgrId(c.getInt(ix_mgr_id));
            obj.setWorkType(c.getString(ix_work_type));
            obj.setRemark(c.getString(ix_remark));

            try {
                obj.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }

            objList.add(obj);

        }

        return  objList;
    }

    public List<Attendance> insertDummyList() {
        List<Attendance> attendances=new ArrayList<>();
        attendances.add(0,new Attendance("ABC","On Field",DateFunc.getDateTime("2019-01-28 11:19:21.927")));
        attendances.add(1,new Attendance("PQR","Training",DateFunc.getDateTime("2019-01-28 10:19:21.927")));
        attendances.add(2,new Attendance("XYZ","Meeting",DateFunc.getDateTime("2019-01-28 08:19:21.927")));

        return attendances;
    }
}
