package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.SalesOrderPrevious;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vLastOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by user on 17/10/2016.
 */

public class SalesOrderPreviousRepo extends RepoBase {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + SalesOrderPrevious.TABLE + "("
                + SalesOrderPrevious.COL_ID + " PRIMARY KEY ,"
                + SalesOrderPrevious.COL_SO_ID + " INT ,"
                + SalesOrderPrevious.COL_ORDER_DATE + " DATE ,"
                + SalesOrderPrevious.COL_APP_SHOP_ID + " TEXT ,"
                + SalesOrderPrevious.COL_SET_NO + " TEXT ,"
                + SalesOrderPrevious.COL_PRODUCT_NAME + " TEXT ,"
                + SalesOrderPrevious.COL_RL_PRODUCT_SKUID + " INT ,"
                + SalesOrderPrevious.COL_ORDER_QTY + " INT ,"
                + SalesOrderPrevious.COL_RATE + " DOUBLE(5,2) ,"
                + SalesOrderPrevious.COL_FREE_QTY + " INT ,"
                + SalesOrderPrevious.COL_DISCOUNT_RATE + " DOUBLE(10,2) ,"
                + SalesOrderPrevious.COL_DISCOUNT + " DOUBLE(10,2) ,"
                + SalesOrderPrevious.COL_ADDITIONAL_DISCOUNT + " DOUBLE(10,2) ,"
                + SalesOrderPrevious.COL_TOTAL_AMOUNT + " DOUBLE(12,2) ,"
                + SalesOrderPrevious.COL_SCHEME_RL_PRODUCT_SKUID + " INT ,"
                + SalesOrderPrevious.COL_SCHEME_QTY + " INT ,"
                + SalesOrderPrevious.COL_IS_SCHEME + " BOOLEAN ,"
                + SalesOrderPrevious.COL_AGENT_ID + " INT ,"
                + SalesOrderPrevious.COL_CREATED_DATE_TIME + " DATETIME "
                + ")";

        return qry;
    }

    public void updateLastOrders(List<SalesOrderPrevious> orders) {

        Hashtable hs = new Hashtable();
        String appShopId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sql;

        for (SalesOrderPrevious order : orders) {

            appShopId = order.getAppShopId();
            if (hs.containsKey(appShopId)) {
                continue;
            }
            hs.put(appShopId, appShopId);


            sql = "";
            sql += " DELETE FROM " + SalesOrderPrevious.TABLE + "";
            sql += " WHERE app_shop_id=" + appShopId + "";
            db.execSQL(sql);

        }

        insert(orders, db);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void copyOrders(int soId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT DISTINCT app_shop_id";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE order_date='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND is_cancelled='0'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return;
        }

        String appShopId = "";

        if (c.getCount() == 0) {
            return;
        }

        while (c.moveToNext()) {

            appShopId = c.getString(0);

            sql = "";
            sql += " DELETE FROM " + SalesOrderPrevious.TABLE + "";
            sql += " WHERE app_shop_id='" + appShopId + "'";
            sql += " AND so_id=" + soId + "";

            try {
                db.execSQL(sql);
            } catch (SQLException e) {
                Logger.e(e.getMessage());
                return;
            }

        }

        sql = "";
        sql += " INSERT INTO " + SalesOrderPrevious.TABLE + "";
        sql += " SELECT A.id, A.so_id, A.order_date, A.app_shop_id, A.set_no, B.product_name, A.rl_product_sku_id,  A.order_qty, A.rate, ";
        sql += "        A.free_qty, A.discount_rate," +
                " A.discount," +
                "A.additional_discount," +
                "A.total_" +
                "amount,  A.scheme_rl_product_sku_id, A.scheme_qty, A.is_scheme, A.agent_id, A.created_date_time";
        sql += " FROM  " + SalesOrder.TABLE + " As A ";
        sql += " LEFT JOIN (    SELECT DISTINCT rl_product_sku_id,  product_name || ' ' || product_sku As product_name " + "";
        sql += "                FROM " + Product.TABLE + " ";
        sql += " ) As B ";
        sql += "    ON(A.rl_product_sku_id=B.rl_product_sku_id)";
        sql += " WHERE  A.order_date='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND    A.so_id=" + soId + "";

        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    private void insert(SalesOrderPrevious   product, SQLiteDatabase db) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues values = new ContentValues();

        values.put(SalesOrderPrevious.COL_ORDER_DATE, dateFormat.format(product.getOrderDate()));
        values.put(SalesOrderPrevious.COL_SO_ID, product.getSoId());
        values.put(SalesOrderPrevious.COL_APP_SHOP_ID, product.getAppShopId());
        values.put(SalesOrderPrevious.COL_AGENT_ID, product.getAgentId());
        values.put(SalesOrderPrevious.COL_PRODUCT_NAME, product.getProductName());
        values.put(SalesOrderPrevious.COL_RL_PRODUCT_SKUID, product.getRlProductSkuId());
        values.put(SalesOrderPrevious.COL_ORDER_QTY, product.getOrderQty());
        values.put(SalesOrderPrevious.COL_RATE, product.getRate());
        values.put(SalesOrderPrevious.COL_FREE_QTY, product.getFreeQty());
        values.put(SalesOrderPrevious.COL_DISCOUNT_RATE, product.getDiscountRate());
        values.put(SalesOrderPrevious.COL_DISCOUNT, product.getDiscount());
        values.put(SalesOrderPrevious.COL_ADDITIONAL_DISCOUNT, product.getAdditionalDiscount());
        values.put(SalesOrderPrevious.COL_TOTAL_AMOUNT, product.getTotalAmount());
        values.put(SalesOrderPrevious.COL_SCHEME_RL_PRODUCT_SKUID, product.getScheme_rlProductSkuId());
        values.put(SalesOrderPrevious.COL_SCHEME_QTY, product.getScheme_qty());
        values.put(SalesOrderPrevious.COL_IS_SCHEME, product.getIsScheme());
        values.put(SalesOrderPrevious.COL_CREATED_DATE_TIME, dateTimeFormat.format(product.getCreatedDateTime()));

        try {
            db.insert(SalesOrderPrevious.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(List<SalesOrderPrevious> products, SQLiteDatabase db) {

        for (SalesOrderPrevious product : products) {
            insert(product, db);
        }
    }

    public void insert(List<SalesOrderPrevious> products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + SalesOrderPrevious.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vLastOrder> getOrders(int soId, String shopAppId) {

        List<vLastOrder> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT Max(order_date) ";
        sql += " FROM " + SalesOrderPrevious.TABLE + "";
        sql += " WHERE app_shop_id='" + shopAppId + "'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        String lastOrdrDate = "";
        while (c.moveToNext()) {
            lastOrdrDate = c.getString(0);
        }


        sql = "";
        sql += " SELECT order_date, app_shop_id, set_no, rl_product_sku_id, product_name , order_qty , rate,  round(total_amount) As order_value";
        sql += " FROM " + SalesOrderPrevious.TABLE + " ";
        sql += " WHERE  so_id = " + soId + "";
        sql += " AND    app_shop_id='" + shopAppId + "'";
        sql += " AND    order_date ='" + lastOrdrDate + "'";
        sql += " AND    is_scheme='0'  ";

//        sql = "";
//        sql += " SELECT A.order_date, A.app_shop_id, A.set_no, A.rl_product_sku_id, B.product_name || ' ' || B.product_sku As product_name, A.order_qty As order_qty , A.rate,  round(A.rate * A.order_qty) As order_value";
//        sql += " FROM " + SalesOrderPrevious.TABLE + " As A";
//        sql += " LEFT JOIN (    SELECT DISTINCT product_id, product_name, rl_product_sku_id, product_sku " + "";
//        sql += "                FROM " + Product.TABLE + " ";
//        sql += " ) As B ";
//        sql += "        ON(A.rl_product_sku_id=B.rl_product_sku_id)";
//        sql += " WHERE A.app_shop_id='" + shopAppId + "'";
//        sql += " AND A.order_date ='" + lastOrdrDate + "'";
//        sql += " AND A.is_scheme='0'  ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vLastOrder objProduct;

        int ix_order_date = c.getColumnIndexOrThrow(SalesOrderPrevious.COL_ORDER_DATE);
        int ix_product_name = c.getColumnIndexOrThrow("product_name");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_order_value = c.getColumnIndexOrThrow("order_value");
        int ix_rate = c.getColumnIndexOrThrow("rate");

        while (c.moveToNext()) {

            objProduct = new vLastOrder();
            objProduct.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            objProduct.setProductName(c.getString(ix_product_name));
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }


}
