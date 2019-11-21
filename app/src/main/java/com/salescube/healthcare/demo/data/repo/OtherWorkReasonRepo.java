package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.OtherWorkReason;
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vOtherWorkReason;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 03/11/2016.
 */

public class OtherWorkReasonRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + OtherWorkReason.TABLE + "("
                + OtherWorkReason.COL_ID + " INT ,"
                + OtherWorkReason.COL_REASON + " INT, "
                + OtherWorkReason.COL_SEQ + " INT "
                + ")";

        return qry;
    }

    public void insert(OtherWorkReason obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(OtherWorkReason.COL_ID, obj.getId());
        values.put(OtherWorkReason.COL_REASON, obj.getReason());
        values.put(OtherWorkReason.COL_SEQ, obj.getSeq());

        try {
            db.insert( OtherWorkReason.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex .getMessage());
        }

    }

    private void insert(OtherWorkReason[] objList, SQLiteDatabase db) {

        for (OtherWorkReason productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + OtherWorkReason.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(OtherWorkReason objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(OtherWorkReason[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vOtherWorkReason> getReasonList() {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        List<vOtherWorkReason> list = new ArrayList<>();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + OtherWorkReason.TABLE + "";
        sql += " ORDER BY seq";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return list;
        }

        DatabaseManager.getInstance().closeDatabase();
        list = fillOrders(c);

        return list;
    }

    public List<vBaseSpinner> getReasonList1() {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        List<vBaseSpinner> list = new ArrayList<>();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + OtherWorkReason.TABLE + "";
        sql += " ORDER BY seq";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return list;
        }


        int ix_id = c.getColumnIndexOrThrow(OtherWorkReason.COL_ID);
        int ix_reason = c.getColumnIndexOrThrow(OtherWorkReason.COL_REASON);

        vBaseSpinner pop;

        while (c.moveToNext()) {

            pop = new vBaseSpinner();
            pop.setId(c.getInt(ix_id));
            pop.setName(c.getString(ix_reason));
            list.add(pop);
        }
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }


    private void insertDefault() {

        if (getReasonList().size() != 0){
            return;
        }

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(new OtherWorkReason(1, "Other"), db);
        insert(new OtherWorkReason(2, "Training"), db);
        insert(new OtherWorkReason(3, "Launching"), db);
        insert(new OtherWorkReason(4, "Exhibition"), db);
        insert(new OtherWorkReason(5, "Weekly Meeting"), db);
        insert(new OtherWorkReason(6, "H.O.Meeting"), db);
        insert(new OtherWorkReason(7, "Agent Visit"), db);
        insert(new OtherWorkReason(8, "SS Visit"), db);
        insert(new OtherWorkReason(9, "Joint Work with SO"), db);

        DatabaseManager.getInstance().closeDatabase();

    }

    private List<vOtherWorkReason> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(OtherWorkReason.COL_ID);
        int ix_reason = c.getColumnIndexOrThrow(OtherWorkReason.COL_REASON);

        vOtherWorkReason pop;
        List<vOtherWorkReason> popList = new ArrayList<>();

        while (c.moveToNext()) {

            pop = new vOtherWorkReason();
            pop.setId(c.getInt(ix_id));
            pop.setReason(c.getString(ix_reason));
            popList.add(pop);
        }

        return popList;
    }

}