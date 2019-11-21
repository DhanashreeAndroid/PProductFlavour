package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ProductRate;

/**
 * Created by user on 10/10/2016.
 */

public class ProductRateRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + ProductRate.TABLE + "("
                + ProductRate.COL_ID + " PRIMARY KEY ,"
                + ProductRate.COL_SO_ID + " INT ,"
                + ProductRate.COL_PRICE_CODE + " TEXT ,"
                + ProductRate.COL_AGENT_ID + " INT ,"
                + ProductRate.COL_RL_PRODUCT_SKU_ID + " INT ,"
                + ProductRate.COL_RATE + " DOUBLE "
                + ")";

        return qry;
    }

    public void  deleteAll(int soId){

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + ProductRate.TABLE + "";
        sql += " WHERE so_id=" + soId + "";

        db.execSQL(sql);
        DatabaseManager.getInstance().closeDatabase();

    }

    private void insert(ProductRate productRate, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(ProductRate.COL_SO_ID, productRate.getSoId());
        values.put(ProductRate.COL_PRICE_CODE, productRate.getPriceCode());
        values.put(ProductRate.COL_AGENT_ID, productRate.getAgentId());
        values.put(ProductRate.COL_RL_PRODUCT_SKU_ID, productRate.getRlProductSKUId());
        values.put(ProductRate.COL_RATE, productRate.getRate());

        try {
            db.insert(ProductRate.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(ProductRate[] productRates, SQLiteDatabase db) {

        for (ProductRate productRate : productRates) {
            insert(productRate, db);
        }
    }

    public void insert(ProductRate[] products) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

}
