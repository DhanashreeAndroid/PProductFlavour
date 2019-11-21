package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.SS;
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vSS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13/06/2017.
 */

public class SSRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + SS.TABLE + "("
                + SS.COL_ID + " INT, "
                + SS.COL_SO_ID + " INT, "
                + SS.COL_SS_ID + " INT, "
                + SS.COL_SS_NAME + " TEXT, "
                + SS.COL_ROLE + " TEXT "
                + ")";

        return qry;
    }

    private void insert(SS obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(SS.COL_SO_ID, obj.getSoId());
        values.put(SS.COL_SS_ID, obj.getSsId());
        values.put(SS.COL_SS_NAME, obj.getSsName());
        values.put(SS.COL_ROLE, obj.getRole());

        try {
            db.insert(SS.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    private void insert(SS[] objList, SQLiteDatabase db) {

        for (SS productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + SS.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(SS objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(SS[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vSS> getSSList(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SS.TABLE + "";
        sql += " WHERE so_id=" + soId + "";
        sql += " ORDER BY ss_name " ;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }

    private List<vSS> fillOrders(Cursor c) {

        int ix_ss_id = c.getColumnIndexOrThrow(SS.COL_SS_ID);
        int ix_ss_name = c.getColumnIndexOrThrow(SS.COL_SS_NAME);
        int ix_role = c.getColumnIndexOrThrow(SS.COL_ROLE);

        vSS pop;
        List<vSS> popList = new ArrayList<>();

        while (c.moveToNext()) {

            pop = new vSS();
            pop.setSsId(c.getInt(ix_ss_id));
            pop.setSsName(c.getString(ix_ss_name));
            pop.setRole(c.getString(ix_role));
            popList.add(pop);
        }

        return popList;
    }


    public List<vBaseSpinner> getSSList1(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SS.TABLE + "";
        sql += " WHERE so_id=" + soId + "";
        sql += " ORDER BY ss_name " ;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_ss_id = c.getColumnIndexOrThrow(SS.COL_SS_ID);
        int ix_ss_name = c.getColumnIndexOrThrow(SS.COL_SS_NAME);
        vBaseSpinner pop;
        List<vBaseSpinner> popList = new ArrayList<>();

        while (c.moveToNext()) {

            pop = new vBaseSpinner();
            pop.setId(c.getInt(ix_ss_id));
            pop.setName(c.getString(ix_ss_name));
            popList.add(pop);
        }

        return popList;
    }


    public String getSelected(int id) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String name = "";

        sql = "";
        sql += " SELECT  ss_name";
        sql += " FROM " + SS.TABLE + "";
        sql += " WHERE ss_id=" + id + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        int ix_ss_name = c.getColumnIndexOrThrow(SS.COL_SS_NAME);
        vBaseSpinner pop;
        List<vBaseSpinner> popList = new ArrayList<>();

        while (c.moveToNext()) {

            name = c.getString(ix_ss_name);
        }

        return name;
    }


}
