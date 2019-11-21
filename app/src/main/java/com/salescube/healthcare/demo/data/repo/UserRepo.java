package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 19/09/2016.
 */
public class UserRepo extends RepoBase {

    public UserRepo() {

    }

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + User.TABLE + "("
                + User.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + User.COL_EMPLOYEE_ID + " INT ,"
                + User.COL_EMPLOYEE_NAME + " TEXT ,"
                + User.COL_DIVISION_ID + " INT ,"
                + User.COL_IMEI_NO + " INT ,"
                + User.COL_USERNAME + " TEXT ,"
                + User.COL_PASSWORD + " TEXT ,"
                + User.COL_ROLE_ID + " INT ,"
                + User.COL_ROLE_NAME + " TEXT ,"
                + User.COL_APP_ROLE + " TEXT, "
                + User.COL_LAST_LOGIN_DATE + " DATE "
                + ")";

        return qry;
    }

    public long insert(User objUser) {

        if (objUser.getPassword() == null) {
            return 0;
        }

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(User.COL_EMPLOYEE_ID, objUser.getEmployeeId());
        values.put(User.COL_EMPLOYEE_NAME, objUser.getEmployeeName());
        values.put(User.COL_DIVISION_ID, objUser.getDivisionId());
        values.put(User.COL_IMEI_NO, objUser.getImeiNo());
        values.put(User.COL_USERNAME, objUser.getUserName());
        values.put(User.COL_PASSWORD, objUser.getPassword());
        values.put(User.COL_ROLE_ID, objUser.getRoleId());
        values.put(User.COL_ROLE_NAME, objUser.getRoleName());
        values.put(User.COL_APP_ROLE, objUser.getAppRole());
        values.put(User.COL_LAST_LOGIN_DATE, DateFunc.getDateTimeStr());

        long newRowId = db.insert(User.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return newRowId;
    }

    public  void  updateLoginTimeStamp(int userId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COL_LAST_LOGIN_DATE, DateFunc.getDateTimeStr());

        db.update(User.TABLE, values,"employee_id=?",new String[] {String.valueOf(userId)});

        DatabaseManager.getInstance().closeDatabase();
    }

    public User getDefaultUser() {

        User objUser = new User();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT employee_id, employee_name, user_name, password ";
        sql += " FROM " + User.TABLE;
        sql += " ORDER BY last_login_date desc";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Log.e("USER_DATA","Error while pulling user", ex);
            return objUser;
        }

        if (c.getCount() == 0) {
            return objUser;
        }

        c.moveToNext();

        objUser.setEmployeeId(c.getInt(0));
        objUser.setEmployeeName(c.getString(1));
        objUser.setUserName(c.getString(2));
        objUser.setPassword(c.getString(3));

        closeCursor(c);
        return objUser;
    }

    public User getOwnerUser(){

        User objUser = new User();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT id, employee_id, employee_name, division_id, imei_no, user_name, role_name, app_role ";
        sql += " FROM " + User.TABLE;
        sql += " ORDER BY last_login_date desc";
        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_employee_id = c.getColumnIndexOrThrow("employee_id");
        int ix_employee_name = c.getColumnIndexOrThrow("employee_name");
        int ix_division_id = c.getColumnIndexOrThrow("division_id");
        int ix_imei_no = c.getColumnIndexOrThrow("imei_no");
        int ix_user_name = c.getColumnIndexOrThrow("user_name");
        int ix_role_name = c.getColumnIndexOrThrow("role_name");
        int ix_app_role = c.getColumnIndexOrThrow("app_role");

        c.moveToNext();

        objUser.setEmployeeId(c.getInt(ix_employee_id));
        objUser.setEmployeeName(c.getString(ix_employee_name));
        objUser.setDivisionId(c.getString(ix_division_id));
        objUser.setImeiNo(c.getString(ix_imei_no));
        objUser.setUserName(c.getString(ix_user_name));
        objUser.setRoleName(c.getString(ix_role_name));
        objUser.setAppRole(c.getString(ix_app_role));

        closeCursor(c);
        return objUser;

    }

    public User getUser(String loginName, String pass) {

        User objUser = new User();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT id, employee_id, employee_name, division_id, imei_no, user_name, password, role_name, app_role ";
        sql += " FROM " + User.TABLE;
        sql += " WHERE user_name ='" + loginName + "'";
        //sql += " AND password='" + pass + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_employee_id = c.getColumnIndexOrThrow("employee_id");
        int ix_employee_name = c.getColumnIndexOrThrow("employee_name");
        int ix_division_id = c.getColumnIndexOrThrow("division_id");
        int ix_imei_no = c.getColumnIndexOrThrow("imei_no");
        int ix_user_name = c.getColumnIndexOrThrow("user_name");
        int ix_password = c.getColumnIndexOrThrow("password");
        int ix_role_name = c.getColumnIndexOrThrow("role_name");
        int ix_app_role = c.getColumnIndexOrThrow("app_role");

        c.moveToNext();

        objUser.setEmployeeId(c.getInt(ix_employee_id));
        objUser.setEmployeeName(c.getString(ix_employee_name));
        objUser.setDivisionId(c.getString(ix_division_id));
        objUser.setImeiNo(c.getString(ix_imei_no));
        objUser.setUserName(c.getString(ix_user_name));
        objUser.setPassword(c.getString(ix_password));
        objUser.setRoleName(c.getString(ix_role_name));
        objUser.setAppRole(c.getString(ix_app_role));

        closeCursor(c);
        return objUser;
    }


    public User getUser(int employeeId){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT id, employee_id, employee_name, division_id, imei_no, user_name, password, role_name, app_role ";
        sql += " FROM " + User.TABLE;
        sql += " WHERE employee_id=" + employeeId + "";
        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Log.e("UserRepo", ex.getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_employee_id = c.getColumnIndexOrThrow("employee_id");
        int ix_employee_name = c.getColumnIndexOrThrow("employee_name");
        int ix_division_id = c.getColumnIndexOrThrow("division_id");
        int ix_imei_no = c.getColumnIndexOrThrow("imei_no");
        int ix_user_name = c.getColumnIndexOrThrow("user_name");
        int ix_password = c.getColumnIndexOrThrow("password");
        int ix_role_name = c.getColumnIndexOrThrow("role_name");
        int ix_app_role = c.getColumnIndexOrThrow("app_role");

        User objUser = new User();

        while (c.moveToNext()) {

            objUser.setEmployeeId(c.getInt(ix_employee_id));
            objUser.setEmployeeName(c.getString(ix_employee_name));
            objUser.setDivisionId(c.getString(ix_division_id));
            objUser.setImeiNo(c.getString(ix_imei_no));
            objUser.setUserName(c.getString(ix_user_name));
            objUser.setPassword(c.getString(ix_password));
            objUser.setRoleName(c.getString(ix_role_name));
            objUser.setAppRole(c.getString(ix_app_role));

            break;
        }

        closeCursor(c);
        return objUser;
    }



    public List<User> getUsers(){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT id, employee_id, employee_name, division_id, imei_no, user_name, password, role_name, app_role ";
        sql += " FROM " + User.TABLE;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_employee_id = c.getColumnIndexOrThrow("employee_id");
        int ix_employee_name = c.getColumnIndexOrThrow("employee_name");
        int ix_division_id = c.getColumnIndexOrThrow("division_id");
        int ix_imei_no = c.getColumnIndexOrThrow("imei_no");
        int ix_user_name = c.getColumnIndexOrThrow("user_name");
        int ix_password = c.getColumnIndexOrThrow("password");
        int ix_role_name = c.getColumnIndexOrThrow("role_name");
        int ix_app_role = c.getColumnIndexOrThrow("app_role");

        User objUser = new User();
        List<User> objList = new ArrayList<>();

        while (c.moveToNext()) {

            objUser = new User();
            objUser.setEmployeeId(c.getInt(ix_employee_id));
            objUser.setEmployeeName(c.getString(ix_employee_name));
            objUser.setDivisionId(c.getString(ix_division_id));
            objUser.setImeiNo(c.getString(ix_imei_no));
            objUser.setUserName(c.getString(ix_user_name));
            objUser.setPassword(c.getString(ix_password));
            objUser.setRoleName(c.getString(ix_role_name));
            objUser.setAppRole(c.getString(ix_app_role));
            objList.add(objUser);

        }

        closeCursor(c);
        return objList;
    }

//
//    public User getUser(String loginName, String pass) {
//
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        String[] projection = {
//                User.COL_ID,
//                User.COL_EMPLOYEE_ID,
//                User.COL_EMPLOYEE_NAME,
//                User.COL_DIVISION_ID,
//                User.COL_IMEI_NO,
//                User.COL_USERNAME,
//                User.COL_ROLE_NAME,
//                User.COL_APP_ROLE
//        };
//
//        String selecttion = "" +
//                User.COL_USERNAME + "= ? AND " +
//                User.COL_PASSWORD + "= ?";
//
//        String[] selectionArgs = {loginName, pass};
//
//        String sortOrder =
//                User.COL_USERNAME;
//
//        Cursor c = db.query(
//                User.TABLE,
//                projection,
//                selecttion,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//
//        User objUser = new User();
//        if (c.getCount() == 0) {
//            return null;
//        }
//
//        c.moveToFirst();
//
//        int col_ix_id = c.getColumnIndexOrThrow(User.COL_ID);
//        int col_ix_employeeId = c.getColumnIndexOrThrow(User.COL_EMPLOYEE_ID);
//        int col_ix_employeeName = c.getColumnIndexOrThrow(User.COL_EMPLOYEE_NAME);
//        int col_ix_divisionId = c.getColumnIndexOrThrow(User.COL_DIVISION_ID);
//        int col_ix_imeiNo = c.getColumnIndexOrThrow(User.COL_IMEI_NO);
//        int col_ix_userName = c.getColumnIndexOrThrow(User.COL_USERNAME);
//        int col_ix_rolName = c.getColumnIndexOrThrow(User.COL_ROLE_NAME);
//        int col_ix_appRole = c.getColumnIndexOrThrow(User.COL_APP_ROLE);
//
//        objUser.setEmployeeId(c.getInt(col_ix_employeeId));
//        objUser.setEmployeeName(c.getString(col_ix_employeeName));
//        objUser.setDivisionId(c.getString(col_ix_divisionId));
//        objUser.setImeiNo(c.getString(col_ix_imeiNo));
//        objUser.setUserName(c.getString(col_ix_userName));
//        objUser.setRoleName(c.getString(col_ix_rolName));
//        objUser.setAppRole(c.getString(col_ix_appRole));
//
//        return objUser;
//    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

}
