package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ShopWiseOrder;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vShopWiseOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopWiseOrderRepo extends RepoBase{

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + ShopWiseOrder.TABLE + "("
//                + ShopWiseOrder.COL_ID + " PRIMARY KEY ,"
                + ShopWiseOrder.COL_SO_ID + " INT ,"
                + ShopWiseOrder.COL_SO_NAME + " INT ,"
                + ShopWiseOrder.COL_ORDER_DATE + " DATE ,"
                + ShopWiseOrder.COL_SHOP_ID + " INT ,"
                + ShopWiseOrder.COL_SHOP_NAME + " TEXT ,"
                + ShopWiseOrder.COL_PRODUCT_ID + " INT ,"
                + ShopWiseOrder.COL_PRODUCT_NAME + " TEXT ,"
                + ShopWiseOrder.COL_ORDER_QTY + " INT ,"
                + ShopWiseOrder.COL_TOTAL_AMOUNT + " INT "
                + ")";
        return qry;
    }

    private void insert(ShopWiseOrder product, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

//        values.put(ShopWiseOrder.COL_ID, product.getId());
        values.put(ShopWiseOrder.COL_SO_ID, product.getSoId());
        values.put(ShopWiseOrder.COL_SO_NAME, product.getSoName());
        values.put(ShopWiseOrder.COL_ORDER_DATE, DateFunc.getDateStr(product.getOrderDate()));
        values.put(ShopWiseOrder.COL_SHOP_ID, product.getShopId());
        values.put(ShopWiseOrder.COL_SHOP_NAME, product.getShopName());
        values.put(ShopWiseOrder.COL_PRODUCT_ID, product.getProductId());
        values.put(ShopWiseOrder.COL_PRODUCT_NAME, product.getProductName());
        values.put(ShopWiseOrder.COL_ORDER_QTY, product.getOrderQty());
        values.put(ShopWiseOrder.COL_TOTAL_AMOUNT, product.getTotalAmount());

        try {
            db.insert(ShopWiseOrder.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(List<ShopWiseOrder> products, SQLiteDatabase db) {

        for (ShopWiseOrder product : products) {
            insert(product, db);
        }
    }
    private void insert(ShopWiseOrder[] objList, SQLiteDatabase db) {

        for (ShopWiseOrder obj : objList) {
            insert(obj, db);
        }
    }

    public void insert(ShopWiseOrder[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(List<ShopWiseOrder> products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vShopWiseOrder> getSONames() {

//        List<vShopWiseOrder> objList = new ArrayList<>();
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT DISTINCT so_id,so_name ";
        sql += " FROM " + ShopWiseOrder.TABLE + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        int ix_so_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SO_ID);
        int ix_so_name = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SO_NAME);

//        vShopWiseOrder order=new vShopWiseOrder();
//        order.setSoId(c.getInt(ix_so_id));
//        order.setSoName(c.getString(ix_so_name));

        List<vShopWiseOrder> objList = new ArrayList<>();
//        orders=fillOrders(c);
        if (c.getCount() == 0) {
            closeCursor(c);
            return objList;
        }

        vShopWiseOrder obj;
        while (c.moveToNext()) {

            obj = new vShopWiseOrder();
            obj.setSoId(c.getInt(ix_so_id));
            obj.setSoName(c.getString(ix_so_name));
            objList.add(obj);
        }
//        closeCursor(c);
        return objList;
    }
    public List<vShopWiseOrder> getShopNames() {

        List<vShopWiseOrder> orders = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT shop_id,shop_name ,total_amount";
        sql += " FROM " + ShopWiseOrder.TABLE + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return orders;
        }
        int ix_shop_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SHOP_ID);
        int ix_shop_name = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SHOP_NAME);
        int ix_total_amount = c.getColumnIndexOrThrow(ShopWiseOrder.COL_TOTAL_AMOUNT);

//        vShopWiseOrder order=new vShopWiseOrder();
//        order.setSoId(c.getInt(ix_so_id));
//        order.setSoName(c.getString(ix_so_name));

        List<vShopWiseOrder> objList = new ArrayList<>();
//        orders=fillOrders(c);
        if (c.getCount() == 0) {
            closeCursor(c);
            return objList;
        }

        vShopWiseOrder obj;
        while (c.moveToNext()) {

            obj = new vShopWiseOrder();
            obj.setShopId(c.getString(ix_shop_id));
            obj.setShopName(c.getString(ix_shop_name));
            obj.setTotalAmount(c.getDouble(ix_total_amount));
            objList.add(obj);
        }
//        closeCursor(c);
        return objList;
    }

    public int[] getTCPC(int soId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;
        int[] tcPc = new int[2];

        tcPc[0] = 0;
        tcPc[1] = 0;

        sql = "";
        sql += " SELECT shop_id, 'CC' CallType";
        sql += " FROM  trn_no_order ";
        sql += " WHERE shop_id NOT IN(SELECT shop_id FROM ms_shop_wise_order WHERE order_date = '" + DateFunc.getDateStr(orderDate) + "' AND so_id=" + soId + ")";
        sql += " AND order_date = '" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND so_id=" + soId + "";
        sql += " UNION ";
        sql += " SELECT shop_id, 'PC' CallType";
        sql += " FROM ms_shop_wise_order";
        sql += " WHERE order_date = '" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return tcPc;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return tcPc;
        }

        int ix_call_type = c.getColumnIndex("CallType");
        String callType = "";
        int pc_calls = 0;
        int tc_calls = c.getCount();

        while (c.moveToNext()) {
            callType = c.getString(ix_call_type);
            if (callType.equals("PC")) {
                pc_calls +=1;
            }
        }

        tcPc[0] = tc_calls;
        tcPc[1] = pc_calls;

        return tcPc;
    }

    private List<ShopWiseOrder> fillOrders(Cursor c) {

        List<ShopWiseOrder> orders = new ArrayList<>();

        ShopWiseOrder objProduct;

//        int ix_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SO_ID);
        int ix_so_name = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SO_NAME);
        int ix_order_date = c.getColumnIndexOrThrow(ShopWiseOrder.COL_ORDER_DATE);
        int ix_shop_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SHOP_ID);
        int ix_shop_name = c.getColumnIndexOrThrow(ShopWiseOrder.COL_SHOP_NAME);
        int ix_product_id = c.getColumnIndexOrThrow(ShopWiseOrder.COL_PRODUCT_ID);
        int ix_product_name = c.getColumnIndexOrThrow(ShopWiseOrder.COL_PRODUCT_NAME);
        int ix_order_qty = c.getColumnIndexOrThrow(ShopWiseOrder.COL_ORDER_QTY);
        int ix_total_amt = c.getColumnIndexOrThrow(ShopWiseOrder.COL_TOTAL_AMOUNT);

        while (c.moveToNext()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            objProduct = new ShopWiseOrder("ABC", "XYZ", 90, 278);
//            objProduct.setId(c.getInt(ix_id));
            objProduct.setSoId(c.getInt(ix_so_id));
            objProduct.setSoName(c.getString(ix_so_name));


            try {
                objProduct.setOrderDate(dateTime.parse(c.getString(ix_order_date)));
            } catch (ParseException e) {

            }

            objProduct.setShopId(c.getString(ix_shop_id));
            objProduct.setShopName(c.getString(ix_shop_name));
            objProduct.setProductId(c.getInt(ix_product_id));
            objProduct.setProductName(c.getString(ix_product_name));
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setTotalAmount(c.getDouble(ix_total_amt));


            orders.add(objProduct);
        }

        return orders;

    }


    public void deleteAll() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + ShopWiseOrder.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }
}
