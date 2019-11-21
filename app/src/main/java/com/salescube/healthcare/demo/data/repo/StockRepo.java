package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Stock;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

public class StockRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Stock.TABLE + "("
                + Stock.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Stock.COL_MONTH_YEAR + " INT ,"
                + Stock.COL_STOCK_DATE + " DATE ,"
                + Stock.COL_SS_ID + " INT ,"
                + Stock.COL_RL_PRODUCT_SKU_ID + " INT ,"
                + Stock.COL_QTY + " INT ,"
                + Stock.COL_QTY_IN_TRANSIT + " INT ,"
                + Stock.COL_IS_EDITABLE + " BOOLEAN ,"
                + Stock.COL_CREATED_DATE_TIME + " DATETIME ,"
                + Stock.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(Stock obj, SQLiteDatabase db) {

        // LOGIC
        if (obj.getRlProductSkuId() == 0) {
            return;
        }

        if ((obj.getQty() + obj.getQtyInTransit()) == 0) {
            return;
        }
        // END LOGIC

        ContentValues values = new ContentValues();

        values.put(Stock.COL_SS_ID, obj.getShopId());
        values.put(Stock.COL_MONTH_YEAR, obj.getYearMonth());
        values.put(Stock.COL_STOCK_DATE, DateFunc.getDateStr(obj.getStockDate()));
        values.put(Stock.COL_RL_PRODUCT_SKU_ID, obj.getRlProductSkuId());
        values.put(Stock.COL_QTY, obj.getQty());
        values.put(Stock.COL_QTY_IN_TRANSIT, obj.getQtyInTransit());
        values.put(Stock.COL_IS_EDITABLE, obj.isEditable());
        values.put(Stock.COL_CREATED_DATE_TIME, DateFunc.getDateStr());
        values.put(Stock.COL_IS_POSTED, false);

        try {
            db.insert(Stock.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    public void update(Stock obj, SQLiteDatabase db) {

        if (obj.getId() == 0) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(Stock.COL_MONTH_YEAR, obj.getYearMonth());
        values.put(Stock.COL_SS_ID, obj.getShopId());
        values.put(Stock.COL_RL_PRODUCT_SKU_ID, obj.getRlProductSkuId());
        values.put(Stock.COL_QTY, obj.getQty());
        values.put(Stock.COL_QTY_IN_TRANSIT, obj.getQtyInTransit());
        values.put(Stock.COL_IS_POSTED, false);

        try {
            db.update(Stock.TABLE, values, "Id=?", new String[]{String.valueOf(obj.getId())});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }
    }

    private void insert(List<Stock> objList, SQLiteDatabase db) {

        for (Stock obj : objList) {
            insert(obj, db);
        }
    }

    private void insert(Stock[] objList, SQLiteDatabase db) {

        for (Stock obj : objList) {
            insert(obj, db);
        }
    }
    public void deleteAll(int userId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(Stock.TABLE, "user_id=?", new String[]{String.valueOf(userId)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Stock.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

//    public void deleteAll(int userId, int yearMonth) {
//
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        try {
//            db.delete(Stock.TABLE, "user_id=? AND year_month=? ", new String[]{String.valueOf(userId), String.valueOf(yearMonth)});
//        } catch (Exception ex) {
//            Logger.e(ex.getMessage());
//        }
//
//        DatabaseManager.getInstance().closeDatabase();
//    }

    public void deleteAll(int ssId, int yearMonth) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        StringList whr = new StringList();
        whr.add(ssId);
        whr.add(yearMonth);

        try {
            db.delete(Stock.TABLE, "ss_id=? AND year_month=?", whr.getItems());
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public class StringList {
        private  List<String> list = new ArrayList<String>();
        public void add(Object obj) {
            list.add(String.valueOf(obj));
        }

        public String[] getItems() {
            return list.toArray(new String[list.size()]);
        }
    }

    public void insert(Stock objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();

    }

    public void insert(Stock[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(List<Stock> objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Stock> getStockNotPosted(int ssId) {

        Cursor c;
        String sql;
        List<Stock> leaves;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Stock.TABLE + "";

        c = db.rawQuery(sql, null);
        leaves = fillList(c);

        return leaves;
    }

    public List<Stock> getAgentStock() {

        Cursor c;
        String sql;
        List<Stock> objList = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Stock.TABLE + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return objList;
        }

        objList = fillList(c);
        return objList;
    }

    public void markPosted(int userId) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        values.put(Stock.COL_IS_POSTED, true);

        try {
            db.update(Stock.TABLE, values, "user_id=?", new String[]{String.valueOf(userId)});
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    private List<Stock> fillList(Cursor c) {

        int ix_year_month = c.getColumnIndexOrThrow(Stock.COL_MONTH_YEAR);
        int ix_agent_id = c.getColumnIndexOrThrow(Stock.COL_SS_ID);
        int ix_stock_date = c.getColumnIndexOrThrow(Stock.COL_STOCK_DATE);
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow(Stock.COL_RL_PRODUCT_SKU_ID);
        int ix_qty = c.getColumnIndexOrThrow(Stock.COL_QTY);
        int ix_qty_in_transit = c.getColumnIndexOrThrow(Stock.COL_QTY_IN_TRANSIT);
        int ix_created_date_time = c.getColumnIndexOrThrow(Stock.COL_CREATED_DATE_TIME);

        Stock obj;
        List<Stock> objList = new ArrayList<>();

        while (c.moveToNext()) {

            obj = new Stock();
            obj.setShopId(c.getInt(ix_agent_id));
            obj.setYearMonth(c.getInt(ix_year_month));
            obj.setStockDate(DateFunc.getDate(c.getString(ix_year_month)));
            obj.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            obj.setQty(c.getInt(ix_qty));
            obj.setQtyInTransit(c.getInt(ix_qty_in_transit));

            try {
                obj.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }

            objList.add(obj);
        }

        return objList;
    }
}