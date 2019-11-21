package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.view.vTourPlanDetail;

import java.util.ArrayList;
import java.util.List;

public class TourPlanDetailRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + TourPlanDetail.TABLE + "("
                + TourPlanDetail.COL_TOUR_PLAN_ID + " TEXT ,"
                + TourPlanDetail.COL_TITLE + " TEXT ,"
                + TourPlanDetail.COL_VALUE + " TEXT "
                + ")";

        return qry;
    }

    private void insert(TourPlanDetail obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(TourPlanDetail.COL_TOUR_PLAN_ID, obj.getTourPlanId());
        values.put(TourPlanDetail.COL_TITLE, obj.getTitle());
        values.put(TourPlanDetail.COL_VALUE, obj.getValue());

        try {
            db.insert(TourPlanDetail.TABLE, null, values);
        } catch (Exception ex) {

        }

    }
    private void update(TourPlanDetail obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(TourPlanDetail.COL_TOUR_PLAN_ID, obj.getTourPlanId());
        values.put(TourPlanDetail.COL_TITLE, obj.getTitle());
        values.put(TourPlanDetail.COL_VALUE, obj.getValue());

        try {
            db.update(TourPlanDetail.TABLE, values, TourPlanDetail.COL_TOUR_PLAN_ID
                    + " = '" + obj.getTourPlanId() + "' And " +
                    TourPlanDetail.COL_TITLE
                    + " = '" + obj.getTitle() + "' ", null);
        } catch (Exception ex) {
        }
    }

    public void update(TourPlanDetail obj) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        update(obj, db);
        DatabaseManager.getInstance().closeDatabase();
    }


    private void insert(TourPlanDetail[] objList, SQLiteDatabase db) {

        for (TourPlanDetail productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(TourPlanDetail objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(TourPlanDetail[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + TourPlanDetail.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }
    public void deletePlan(String tourPlanId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + TourPlanDetail.TABLE + " WHERE tour_plan_id = '" +tourPlanId+ "'" );
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insertUpdate(TourPlanDetail obj) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if(isExistTourPlan(obj.getTourPlanId(), obj.getTitle())){
            update(obj, db);
        }else{
            insert(obj, db);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean isExistTourPlan(String tourPlanId, String title){
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT tour_plan_id ";
        sql += " FROM " + TourPlanDetail.TABLE;
        sql += " WHERE tour_plan_id='" + tourPlanId + "'";
        sql += " And title='" + title + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
        }

        if(c == null){
            return false;
        }else {
            if (c.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }


    public List<vTourPlanDetail> getTourPlanDetailAll(String tourPlanId) {

        List<vTourPlanDetail> vTourPlanDetails = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT tour_plan_id, title, value ";
        sql += " FROM " + TourPlanDetail.TABLE;
        sql += " WHERE tour_plan_id='" + tourPlanId + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vTourPlanDetails;
        }

        if (c.getCount() == 0) {
            return vTourPlanDetails;
        }

        int ix_id = c.getColumnIndexOrThrow("tour_plan_id");
        int ix_title = c.getColumnIndexOrThrow("title");
        int ix_value = c.getColumnIndexOrThrow("value");

        vTourPlanDetail vTourPlanDetail;

        while (c.moveToNext()) {
            vTourPlanDetail = new vTourPlanDetail();
            vTourPlanDetail.setTourPlanId(c.getString(ix_id));
            vTourPlanDetail.setTitle(c.getString(ix_title));
            vTourPlanDetail.setValue(c.getString(ix_value));
            vTourPlanDetails.add(vTourPlanDetail);
        }
        c.close();
        return vTourPlanDetails;
    }


    public List<TourPlanDetail> getTourPlanDetail(String tourPlanId) {

        List<TourPlanDetail> vTourPlanDetails = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT tour_plan_id, title, value ";
        sql += " FROM " + TourPlanDetail.TABLE;
        sql += " WHERE tour_plan_id='" + tourPlanId + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vTourPlanDetails;
        }

        if (c.getCount() == 0) {
            return vTourPlanDetails;
        }

        int ix_id = c.getColumnIndexOrThrow("tour_plan_id");
        int ix_title = c.getColumnIndexOrThrow("title");
        int ix_value = c.getColumnIndexOrThrow("value");

        TourPlanDetail vTourPlanDetail;

        while (c.moveToNext()) {
            vTourPlanDetail = new TourPlanDetail();
            vTourPlanDetail.setTourPlanId(c.getString(ix_id));
            vTourPlanDetail.setTitle(c.getString(ix_title));
            vTourPlanDetail.setValue(c.getString(ix_value));
            vTourPlanDetails.add(vTourPlanDetail);
        }
        c.close();
        return vTourPlanDetails;
    }


    public String getInputValue(String tourPlanId, String title){
       String value = "";
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT value ";
        sql += " FROM " + TourPlanDetail.TABLE;
        sql += " WHERE tour_plan_id='" + tourPlanId + "'";
        sql += " And title='" + title + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
        }

        if(c == null){
            value = "";
        }else {
            if (c.getCount() == 0) {
                value = "";
            } else {
                c.moveToFirst();
                value = c.getString(0);
            }
        }
        return  value;
    }
}