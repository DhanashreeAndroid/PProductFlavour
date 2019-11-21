package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.AreaAgent;

public class AreaAgentRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + AreaAgent.TABLE + "("
                + AreaAgent.COL_AREA_ID + " INT ,"
                + AreaAgent.COL_AGENT_ID+ " TEXT ,"
                + AreaAgent.COL_SO_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(AreaAgent obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(AreaAgent.COL_AREA_ID, obj.getAreaId());
        values.put(AreaAgent.COL_AGENT_ID, obj.getAgentId());
        values.put(AreaAgent.COL_SO_ID, obj.getSoId());

        try {
            db.insert(AreaAgent.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    private void insert(AreaAgent[] objList, SQLiteDatabase db) {
        for (AreaAgent productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(AreaAgent objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(AreaAgent[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + AreaAgent.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }
}
