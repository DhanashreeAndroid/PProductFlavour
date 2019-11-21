package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ComplaintType;
import com.salescube.healthcare.demo.func.Parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 25/01/2018.
 */

public class ComplaintTypeRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + ComplaintType.TABLE + "("
                + ComplaintType.COL_ID + " INT ,"
                + ComplaintType.COL_SO_ID + " INT ,"
                + ComplaintType.COL_CUSTOMER_TYPE + " TEXT ,"
                + ComplaintType.COL_COMPLAINT_ABOUT + " TEXT ,"
                + ComplaintType.COL_PRODUCT + " TEXT ,"
                + ComplaintType.COL_CRITERIA + " TEXT ,"
                + ComplaintType.COL_REASON + " TEXT "
                + ")";

        return qry;
    }

    private void insert(ComplaintType obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(ComplaintType.COL_SO_ID, obj.getSoId());
        values.put(ComplaintType.COL_CUSTOMER_TYPE, obj.getCustomerType());
        values.put(ComplaintType.COL_COMPLAINT_ABOUT, obj.getComplaintAbout());
        values.put(ComplaintType.COL_PRODUCT, obj.getProduct());
        values.put(ComplaintType.COL_CRITERIA, obj.getCriteria());
        values.put(ComplaintType.COL_REASON, obj.getReason());

        try {
            db.insert(ComplaintType.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    private void insert(ComplaintType[] objList, SQLiteDatabase db) {

        for (ComplaintType productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + ComplaintType.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ComplaintType objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ComplaintType[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<String> getComplaintType(String customerType) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        List<String> issueList = new ArrayList<>();

        sql = "";
        sql += " SELECT DISTINCT complaint_about";
        sql += " FROM " + ComplaintType.TABLE + "";
        sql += " WHERE customer_type like '%" + customerType + ";%'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return issueList;
        }

        while (c.moveToNext()) {
            issueList.add(c.getString(0));
        }

        return issueList;
    }

    public List<String> getCriteria(String customerType, String complaintAbout, String product) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        List<String> issueList = new ArrayList<>();

        sql = "";
        sql += " SELECT DISTINCT criteria";
        sql += " FROM " + ComplaintType.TABLE + "";
        sql += " WHERE customer_type like '%" + customerType + ";%'";
        sql += " AND complaint_about='" + complaintAbout + "'";
        if (!product.equals("")) {
            sql += " AND product like '%[" + product + "]%'";
        }
        sql += " AND criteria != ''";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return issueList;
        }

        while (c.moveToNext()) {
            issueList.add(c.getString(0));
        }

        return issueList;
    }

    public List<String> getComplaint(String customerType, String complaintAbout, String product, String criteria ) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        List<String> issueList = new ArrayList<>();

        if (complaintAbout.equals("-----")) {
            complaintAbout = "";
        }

//        if (product.equals("-----") || product.equals("0") || product.equals("") ) {
//            product = "";
//        }else {
//            product = "[" + product + "]";
//        }

        if (criteria.equals("-----")) {
            criteria = "";
        }

        sql = "";
        sql += " SELECT reason";
        sql += " FROM " + ComplaintType.TABLE + "";
        sql += " WHERE customer_type like '%" + customerType + ";%'";
        sql += " AND complaint_about='" + complaintAbout + "'";
        if (Parse.toInt(product) > 0) {
            sql += " AND product like '%[" + product + "]%'";
        }else {
            sql += " AND product = ''";
        }
        sql += " AND criteria == '" + criteria +  "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return issueList;
        }

        while (c.moveToNext()) {
            issueList.add(c.getString(0));
        }

        return issueList;
    }
//
//    public List<vIssueSubType> getIssueSubTypes(String cat1) {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        List<vIssueSubType> issueList = new ArrayList<>();
//
//        sql = "";
//        sql += " SELECT category_2, category_3 ";
//        sql += " FROM " + ComplaintType.TABLE + "";
//        sql += " WHERE category_1='" + cat1 + "'";
//        sql += " GROUP BY category_2, category_3";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            return issueList;
//        }
//
//        int ix_category_2 = c.getColumnIndexOrThrow(ComplaintType.COL_CATEGORY_2);
//        int ix_category_3 = c.getColumnIndexOrThrow(ComplaintType.COL_CATEGORY_3);
//
//        vIssueSubType obj;
//        while (c.moveToNext()) {
//            obj = new vIssueSubType();
//
//            obj.setIssue(c.getString(ix_category_2));
//            obj.setSubIssue(c.getString(ix_category_3));
//            issueList.add(obj);
//        }
//
//        return issueList;
//    }
//
//    public List<vComplaintType> getReasonList() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + ComplaintType.TABLE + "";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            return null;
//        }
//
//        return fillOrders(c);
//    }
//
//
//    private List<vComplaintType> fillOrders(Cursor c) {
//
//        int ix_id = c.getColumnIndexOrThrow(ComplaintType.COL_ID);
//        int ix_category_1 = c.getColumnIndexOrThrow(ComplaintType.COL_CATEGORY_1);
//        int ix_category_2 = c.getColumnIndexOrThrow(ComplaintType.COL_CATEGORY_2);
//        int ix_category_3 = c.getColumnIndexOrThrow(ComplaintType.COL_CATEGORY_3);
//
//        vComplaintType pop;
//        List<vComplaintType> popList = new ArrayList<>();
//
//        while (c.moveToNext()) {
//
//            pop = new vComplaintType();
//            pop.setCategory1(c.getString(ix_category_1));
//            pop.setCategory2(c.getString(ix_category_2));
//            pop.setCategory3(c.getString(ix_category_3));
//            popList.add(pop);
//        }
//
//        return popList;
//    }
}
