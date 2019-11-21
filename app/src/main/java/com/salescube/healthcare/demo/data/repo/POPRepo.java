package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.POP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 20/10/2016.
 */

public class POPRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + POP.TABLE + "("
                + POP.COL_ID + " PRIMARY KEY ,"
                + POP.COL_SO_ID + " INT ,"
                + POP.COL_POP_ID + " INT ,"
                + POP.COL_POP_NAME + " TEXT ,"
                + POP.COL_DIVISION_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(POP obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(POP.COL_SO_ID, obj.getSoId());
        values.put(POP.COL_POP_ID, obj.getPopId());
        values.put(POP.COL_POP_NAME, obj.getPopName());
        values.put(POP.COL_DIVISION_ID, obj.getDivisionId());

        try {
            db.insert( POP.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(POP[] objList, SQLiteDatabase db) {

        for (POP productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + POP.TABLE + " WHERE so_id = "+ soId +"");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(POP objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(POP[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }


    public List<POP> getPOPList(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + POP.TABLE + "";
        sql += " WHERE so_id = "+ soId +"";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }


    private List<POP> fillOrders(Cursor c) {

        int ix_so_id = c.getColumnIndexOrThrow(POP.COL_SO_ID);
        int ix_pop_id = c.getColumnIndexOrThrow(POP.COL_POP_ID);
        int ix_pop_name = c.getColumnIndexOrThrow(POP.COL_POP_NAME);
        int ix_division_id = c.getColumnIndexOrThrow(POP.COL_DIVISION_ID);

        POP pop;
        List<POP> popList = new ArrayList<POP>();

        while (c.moveToNext()) {

            pop = new POP();
            pop.setSoId(c.getInt(ix_so_id));
            pop.setPopId(c.getInt(ix_pop_id));
            pop.setPopName(c.getString(ix_pop_name));
            pop.setDivisionId(c.getInt(ix_division_id));
            popList.add(pop);
        }

        return popList;
    }
}
