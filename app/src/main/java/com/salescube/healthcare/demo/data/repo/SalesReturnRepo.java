package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.SalesReturn;
import com.salescube.healthcare.demo.func.DateFunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesReturnRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + SalesReturn.TABLE + "("
                + SalesReturn.COL_ID + " PRIMARY KEY ,"
                + SalesReturn.COL_SO_ID + " INT ,"
                + SalesReturn.COL_RETURN_DATE + " DATE ,"
                + SalesReturn.COL_APP_SHOP_ID + " TEXT ,"
                + SalesReturn.COL_SET_NO + " TEXT ,"
                + SalesReturn.COL_RL_PRODUCT_SKUID + " INT ,"
                + SalesReturn.COL_RETURN_QTY + " INT ,"
                + SalesReturn.COL_AGENT_ID + " INT ,"
                + SalesReturn.COL_CREATED_DATE_TIME + " DATETIME, "
                + SalesReturn.COL_IS_CANCELLED + " BOOLEAN,"
                + SalesReturn.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }


    private void insert(SalesReturn product, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(SalesReturn.COL_SO_ID, product.getSoId());
        values.put(SalesReturn.COL_RETURN_DATE, DateFunc.getDateStr(product.getReturnDate()));
        values.put(SalesReturn.COL_APP_SHOP_ID, product.getAppShopId());
        values.put(SalesReturn.COL_SET_NO, product.getSetNo());
        values.put(SalesReturn.COL_AGENT_ID, product.getAgentId());
        values.put(SalesReturn.COL_RL_PRODUCT_SKUID, product.getRlProductSkuId());
        values.put(SalesReturn.COL_RETURN_QTY, product.getReturnQty());
        values.put(SalesReturn.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr(product.getCreatedDateTime()));
        values.put(SalesReturn.COL_IS_CANCELLED, false);
        values.put(SalesReturn.COL_IS_POSTED, false);

        db.insert(SalesReturn.TABLE, null, values);

    }

    private void insert(List<SalesReturn> products, SQLiteDatabase db) {

        for (SalesReturn product : products) {
            insert(product, db);
        }
    }

    public void insert(List<SalesReturn> products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(products, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void cancelShopOrders(String appShopId, Date orderDate) {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SalesReturn.TABLE + "";
        sql += " SET is_cancelled='1', is_posted='0'";
        sql += " WHERE app_shop_id ='" + appShopId + "'";
        sql += " AND return_date ='" + DateFunc.getDateStr(orderDate) + "'";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(int soId) {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SalesReturn.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted() {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SalesReturn.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<SalesReturn> getOrdersForUpload(int soId) {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SalesReturn.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND   so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        return fillOrders(c);
    }

    private List<SalesReturn> fillOrders(Cursor c) {

        List<SalesReturn> orders = new ArrayList<>();

        SalesReturn objProduct;

        int ix_id = c.getColumnIndexOrThrow(SalesReturn.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(SalesReturn.COL_SO_ID);
        int ix_order_date = c.getColumnIndexOrThrow(SalesReturn.COL_RETURN_DATE);
        int ix_app_shop_id = c.getColumnIndexOrThrow(SalesReturn.COL_APP_SHOP_ID);
        int ix_set_no = c.getColumnIndexOrThrow(SalesReturn.COL_SET_NO);
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow(SalesReturn.COL_RL_PRODUCT_SKUID);
        int ix_order_qty = c.getColumnIndexOrThrow(SalesReturn.COL_RETURN_QTY);
        int ix_agent_id = c.getColumnIndexOrThrow(SalesReturn.COL_AGENT_ID);
        int ix_created_date_time = c.getColumnIndexOrThrow(SalesReturn.COL_CREATED_DATE_TIME);
        int ix_cancelled = c.getColumnIndexOrThrow(SalesReturn.COL_IS_CANCELLED);

        while (c.moveToNext()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            objProduct = new SalesReturn();
            objProduct.setId(c.getInt(ix_id));
            objProduct.setSoId(c.getInt(ix_so_id));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setSetNo(c.getString(ix_set_no));

            try {
                objProduct.setReturnDate(sdf.parse(c.getString(ix_order_date)));
            } catch (ParseException e) {

            }

            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setReturnQty(c.getInt(ix_order_qty));
            objProduct.setAgentId(c.getInt(ix_agent_id));
            objProduct.setCancelled(c.getInt(ix_cancelled) != 0);

            try {
                objProduct.setCreatedDateTime(dateTime.parse(c.getString(ix_created_date_time)));
            } catch (ParseException e) {

            }

            orders.add(objProduct);
        }

        return orders;

    }

    public List<SalesReturn> getShopOrders(int soid, String appShopId, Date orderDate) {

        List<SalesReturn> orders = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SalesReturn.TABLE + "";
        sql += " WHERE so_id=" + soid + "";
        sql += " AND app_shop_id ='" + appShopId + "'";
        sql += " AND return_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND is_cancelled='0'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return orders;
        }

        orders = fillOrders(c);
        return orders;
    }
}
