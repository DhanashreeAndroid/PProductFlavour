package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Employee;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.view.vEmployee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 06/04/2017.
 */

public class EmployeeRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Employee.TABLE + "("
                + Employee.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Employee.COL_USER_ID + " INT ,"
                + Employee.COL_EMP_ID + " TEXT ,"
                + Employee.COL_EMP_NAME + " TEXT, "
                + Employee.COL_APP_ROLE + " TEXT, "
                + Employee.COL_MGR_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(Employee obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Employee.COL_USER_ID, obj.getUserId());
        values.put(Employee.COL_EMP_ID, obj.getEmpId());
        values.put(Employee.COL_EMP_NAME, obj.getEmpName());
        values.put(Employee.COL_APP_ROLE, obj.getAppRole());
        values.put(Employee.COL_MGR_ID, obj.getMgrId());

        try {
            db.insert(Employee.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }
    }

    public void update(Employee obj, SQLiteDatabase db ){

        if (obj.getId() == 0){
            return;
        }

        ContentValues values = new ContentValues();

        values.put(Employee.COL_USER_ID, Parse.toStr(obj.getUserId()));
        values.put(Employee.COL_EMP_ID, Parse.toStr(obj.getEmpId()));
        values.put(Employee.COL_EMP_NAME, Parse.toStr(obj.getEmpName()));
        values.put(Employee.COL_APP_ROLE, Parse.toStr(obj.getAppRole()));
        values.put(Employee.COL_MGR_ID, Parse.toStr(obj.getMgrId()));

        try {
            db.update(Employee.TABLE,values,"Id=?",new String[]{String.valueOf(obj.getId())});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }
    }

    private void insert(Employee[] objList, SQLiteDatabase db) {

        for (Employee obj : objList) {
            insert(obj, db);
        }
    }

    public void deleteAll(int userId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(Employee.TABLE,"user_id=?",new String[]{String.valueOf(userId)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Employee objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();

    }

    public void insert(Employee[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();
        DatabaseManager.getInstance().closeDatabase();
    }


    public List<vEmployee> getEmployee(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Employee.TABLE + "";
        sql += " WHERE user_id=" + userId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_emp_id = c.getColumnIndexOrThrow(Employee.COL_EMP_ID);
        int ix_emp_name = c.getColumnIndexOrThrow(Employee.COL_EMP_NAME);
        int ix_app_role = c.getColumnIndexOrThrow(Employee.COL_APP_ROLE);
        int ix_mgr_id = c.getColumnIndexOrThrow(Employee.COL_MGR_ID);

        vEmployee obj;
        List<vEmployee> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new vEmployee();
            obj.setEmpId(c.getInt(ix_emp_id));
            obj.setEmpName(c.getString(ix_emp_name));
            obj.setAppRole(c.getString(ix_app_role));
            obj.setMgrId(c.getInt(ix_mgr_id));
            objList.add(obj);
        }

        return objList;
    }

    public List<vEmployee> getEmployeeByASM(int userId, int mgrId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Employee.TABLE + "";
        sql += " WHERE  user_id=" + userId + "";
        sql += " AND    mgr_id=" + mgrId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_emp_id = c.getColumnIndexOrThrow(Employee.COL_EMP_ID);
        int ix_emp_name = c.getColumnIndexOrThrow(Employee.COL_EMP_NAME);
        int ix_app_role = c.getColumnIndexOrThrow(Employee.COL_APP_ROLE);
        int ix_mgr_id = c.getColumnIndexOrThrow(Employee.COL_MGR_ID);

        vEmployee obj;
        List<vEmployee> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new vEmployee();
            obj.setEmpId(c.getInt(ix_emp_id));
            obj.setEmpName(c.getString(ix_emp_name));
            obj.setAppRole(c.getString(ix_app_role));
            obj.setMgrId(c.getInt(ix_mgr_id));
            objList.add(obj);
        }

        return objList;
    }

    public List<vEmployee> getASM(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Employee.TABLE + "";
        sql += " WHERE user_id=" + userId + "";
        sql += " AND app_role='ASM' ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_emp_id = c.getColumnIndexOrThrow(Employee.COL_EMP_ID);
        int ix_emp_name = c.getColumnIndexOrThrow(Employee.COL_EMP_NAME);
        int ix_app_role = c.getColumnIndexOrThrow(Employee.COL_APP_ROLE);
        int ix_mgr_id = c.getColumnIndexOrThrow(Employee.COL_MGR_ID);

        vEmployee obj;
        List<vEmployee> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new vEmployee();
            obj.setEmpId(c.getInt(ix_emp_id));
            obj.setEmpName(c.getString(ix_emp_name));
            obj.setAppRole(c.getString(ix_app_role));
            obj.setMgrId(c.getInt(ix_mgr_id));
            objList.add(obj);
        }

        return objList;
    }

}
