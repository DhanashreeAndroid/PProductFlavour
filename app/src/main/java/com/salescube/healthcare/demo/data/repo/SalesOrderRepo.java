package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.view.vAchStatus;
import com.salescube.healthcare.demo.view.vTodayOrders;
import com.salescube.healthcare.demo.view.vTodaySummary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 06/10/2016.
 */

public class SalesOrderRepo extends RepoBase {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + SalesOrder.TABLE + "("
                + SalesOrder.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + SalesOrder.COL_SO_ID + " INT ,"
                + SalesOrder.COL_ORDER_DATE + " DATE ,"
                + SalesOrder.COL_APP_SHOP_ID + " TEXT ,"
                + SalesOrder.COL_SET_NO + " TEXT ,"
                + SalesOrder.COL_RL_PRODUCT_SKUID + " INT ,"
                + SalesOrder.COL_ORDER_QTY + " INT ,"
                + SalesOrder.COL_RATE + " DOUBLE(5,2) ,"
                + SalesOrder.COL_FREE_QTY + " INT ,"
                + SalesOrder.COL_DISCOUNT_RATE + " DOUBLE(10,2) ,"
                + SalesOrder.COL_DISCOUNT + " DOUBLE(10,2) ,"
                + SalesOrder.COL_ADDITIONAL_DISCOUNT + " DOUBLE(10,2) ,"
                + SalesOrder.COL_TOTAL_AMOUNT + " DOUBLE(12,2) ,"
                + SalesOrder.COL_SCHEME_RL_PRODUCT_SKUID + " INT ,"
                + SalesOrder.COL_SCHEME_QTY + " INT ,"
                + SalesOrder.COL_IS_SCHEME + " BOOLEAN ,"
                + SalesOrder.COL_AGENT_ID + " INT ,"
                + SalesOrder.COL_CREATED_DATE_TIME + " DATETIME, "
                + SalesOrder.COL_IS_POSTED + " BOOLEAN, "
                + SalesOrder.COL_IS_CANCELLED + " BOOLEAN "
                + ")";

        return qry;
    }


    private void insert(SalesOrder product, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(SalesOrder.COL_ORDER_DATE, DateFunc.getDateStr(product.getOrderDate()));
        values.put(SalesOrder.COL_SO_ID, product.getSoId());
        values.put(SalesOrder.COL_APP_SHOP_ID, product.getAppShopId());
        values.put(SalesOrder.COL_SET_NO, product.getSetNo());
        values.put(SalesOrder.COL_AGENT_ID, product.getAgentId());
        values.put(SalesOrder.COL_RL_PRODUCT_SKUID, product.getRlProductSkuId());
        values.put(SalesOrder.COL_ORDER_QTY, product.getOrderQty());
        values.put(SalesOrder.COL_RATE, product.getRate());
        values.put(SalesOrder.COL_FREE_QTY, product.getFreeQty());
        values.put(SalesOrder.COL_DISCOUNT_RATE, product.getDiscountRate());
        values.put(SalesOrder.COL_DISCOUNT, product.getDiscount());
        values.put(SalesOrder.COL_ADDITIONAL_DISCOUNT, product.getAdditionalDiscount());
        values.put(SalesOrder.COL_TOTAL_AMOUNT, product.getTotalAmount());
        values.put(SalesOrder.COL_SCHEME_RL_PRODUCT_SKUID, product.getScheme_rlProductSkuId());
        values.put(SalesOrder.COL_SCHEME_QTY, product.getScheme_qty());
        values.put(SalesOrder.COL_IS_SCHEME, product.getIsScheme());
        values.put(SalesOrder.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr(product.getCreatedDateTime()));
        values.put(SalesOrder.COL_IS_POSTED, product.isPosted());
        values.put(SalesOrder.COL_IS_CANCELLED, product.isCancelled());

        try {
            db.insert(SalesOrder.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(List<SalesOrder> products, SQLiteDatabase db) {

        for (SalesOrder product : products) {
            insert(product, db);
        }
    }

    public void insert(SalesOrder[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();
        DatabaseManager.getInstance().closeDatabase();
    }

    private void insert(SalesOrder[] objList, SQLiteDatabase db) {

        for (SalesOrder obj : objList) {
            insert(obj, db);
        }
    }


    public void insert(List<SalesOrder> products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    // Used for delete old orders from order
    public void deleteShopOrders(String appShopId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + SalesOrder.TABLE + "";
        sql += " WHERE app_shop_id ='" + appShopId + "'";
        sql += " AND order_date ='" + DateFunc.getDateStr(orderDate) + "'";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }


    public void cancelShopOrders(String appShopId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SalesOrder.TABLE + "";
        sql += " SET is_cancelled='1', is_posted='0'";
        sql += " WHERE app_shop_id ='" + appShopId + "'";
        sql += " AND order_date ='" + DateFunc.getDateStr(orderDate) + "'";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateOrders(List<SalesOrder> orders) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        for (SalesOrder order : orders) {

            sql = "";
            sql += " UPDATE " + SalesOrder.TABLE + "";
            sql += " SET order_qty=" + order.getOrderQty() + ",is_posted='0' ";
            sql += " WHERE  app_shop_id ='" + order.getAppShopId() + "'";
            sql += " AND    order_date ='" + DateFunc.getDateStr(order.getOrderDate()) + "'";
            sql += " AND    rl_product_sku_id=" + order.getRlProductSkuId() + "";
            sql += " AND    is_cancelled='0'";

            db.execSQL(sql);
        }

        DatabaseManager.getInstance().closeDatabase();

    }

    public void cancelShopOrders(Date orderDate, String appShopId, int rlProductSkuId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + SalesOrder.TABLE + "";
        sql += " SET is_cancelled='1', is_posted='0'";
        sql += " WHERE app_shop_id ='" + appShopId + "'";
        sql += " AND order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND rl_product_sku_id=" + rlProductSkuId + "";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deletePostedShopOrders(String appShopId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + SalesOrder.TABLE + "";
        sql += " WHERE app_shop_id ='" + appShopId + "'";
        sql += " AND order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND is_posted='1'";

        try {
            db.execSQL(sql);
        } catch (Exception ex) {
            return;
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteCancelledShopOrders() {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + SalesOrder.TABLE + "";
        sql += " WHERE  is_cancelled='1'";
        sql += " AND    is_posted='1'";

        try {
            db.execSQL(sql);
        } catch (Exception ex) {
            return;
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<SalesOrder> getShopOrders(int soid, String appShopId, Date orderDate) {

        List<SalesOrder> orders = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE so_id=" + soid + "";
        sql += " AND app_shop_id ='" + appShopId + "'";
        sql += " AND order_date ='" + DateFunc.getDateStr(orderDate) + "'";
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

    public vAchStatus getAchStatus(int soId, Date orderDate) {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql = "";
        sql += " SELECT (total_amount) as order_amt, is_posted ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE is_cancelled='0'";
        sql += " AND  so_id=" + soId + "";
        sql += " AND  order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND  is_scheme='0' ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return new vAchStatus();
        }

        boolean isPosted;
        double orderAmt;
        double totalAch;
        double posted;
        double notPosted;

        posted = 0;
        notPosted = 0;
        totalAch = 0;

        while (c.moveToNext()){

            orderAmt = c.getDouble(0);
            isPosted = c.getInt(1) == 1;

            if (isPosted) {
                posted += orderAmt;
            }else {
                notPosted += orderAmt;
            }
        }

        if (c != null) {
            c.close();
        }
//        DM.close();

        totalAch = posted+notPosted;

        vAchStatus status = new vAchStatus();
        status.setPosted(posted);
        status.setNotPosted(notPosted);
        status.setTotalAch(totalAch);

        return status;

    }

    public List<Date> getOrderDates(int soId) {

        List<Date> orders = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT order_date ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE so_id =" + soId + "";
        sql += " GROUP BY order_date";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return orders;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return orders;
        }

        Date date;
        while (c.moveToNext()) {
            date = DateFunc.getDate(c.getString(0));
            orders.add(date);
        }

        closeCursor(c);
        return orders;
    }

    public void markPosted(List<SalesOrder> orders) {


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values;

        db.beginTransaction();

        for(SalesOrder order: orders) {

            values = new ContentValues();
            values.put(SalesOrder.COL_IS_POSTED, true);
            db.update(SalesOrder.TABLE,values,"id=?",new String[] { String.valueOf(order.getId()) });
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }


    public List<SalesOrder> getOrders(int soId, boolean isPosted) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        int is_posted = isPosted ? 1 : 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='" + is_posted + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return new ArrayList<SalesOrder>();
        }

        return fillOrders(c);
    }

//    public List<SalesOrder> getOrdersForUpload() {
//
//        String sql = "";
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        Cursor c;
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + SalesOrder.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception ex) {
//            return null;
//        }
//
//        if (c.getCount() == 0) {
//            return new ArrayList<SalesOrder>();
//        }
//
//        return fillOrders(c);
//    }

    public List<SalesOrder> getOrdersForUpload(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND   so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            return new ArrayList<SalesOrder>();
        }

        return fillOrders(c);
    }

    public boolean hasOrders(int soId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT order_date ";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE  so_id=" + soId + "";
        sql += " AND    is_cancelled='0'";
        sql += " AND    order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return false;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return false;
        }

        closeCursor(c);
        return true;
    }

    private List<SalesOrder> fillOrders(Cursor c) {

        List<SalesOrder> orders = new ArrayList<>();

        SalesOrder objProduct;

        int ix_id = c.getColumnIndexOrThrow(SalesOrder.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(SalesOrder.COL_SO_ID);
        int ix_order_date = c.getColumnIndexOrThrow(SalesOrder.COL_ORDER_DATE);
        int ix_app_shop_id = c.getColumnIndexOrThrow(SalesOrder.COL_APP_SHOP_ID);
        int ix_set_no = c.getColumnIndexOrThrow(SalesOrder.COL_SET_NO);
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow(SalesOrder.COL_RL_PRODUCT_SKUID);
        int ix_order_qty = c.getColumnIndexOrThrow(SalesOrder.COL_ORDER_QTY);
        int ix_rate = c.getColumnIndexOrThrow(SalesOrder.COL_RATE);
        int ix_free_qty = c.getColumnIndexOrThrow(SalesOrder.COL_FREE_QTY);
        int ix_discount_rate = c.getColumnIndexOrThrow(SalesOrder.COL_DISCOUNT_RATE);
        int ix_discount  = c.getColumnIndexOrThrow(SalesOrder.COL_DISCOUNT);
        int ix_additional_discount  = c.getColumnIndexOrThrow(SalesOrder.COL_ADDITIONAL_DISCOUNT);
        int ix_total_amount  = c.getColumnIndexOrThrow(SalesOrder.COL_TOTAL_AMOUNT);
        int ix_scheme_rl_product_sku_id = c.getColumnIndexOrThrow(SalesOrder.COL_SCHEME_RL_PRODUCT_SKUID);
        int ix_scheme_qty = c.getColumnIndexOrThrow(SalesOrder.COL_SCHEME_QTY);
        int ix_is_scheme = c.getColumnIndexOrThrow(SalesOrder.COL_IS_SCHEME);
        int ix_agent_id = c.getColumnIndexOrThrow(SalesOrder.COL_AGENT_ID);
        int ix_created_date_time = c.getColumnIndexOrThrow(SalesOrder.COL_CREATED_DATE_TIME);
        int ix_cancelled = c.getColumnIndexOrThrow(SalesOrder.COL_IS_CANCELLED);

        while (c.moveToNext()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            objProduct = new SalesOrder();
            objProduct.setId(c.getInt(ix_id));
            objProduct.setSoId(c.getInt(ix_so_id));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setSetNo(c.getString(ix_set_no));

            try {
                objProduct.setOrderDate(sdf.parse(c.getString(ix_order_date)));
            } catch (ParseException e) {

            }

            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setRate(c.getDouble(ix_rate));
            objProduct.setFreeQty(c.getInt(ix_free_qty));
            objProduct.setDiscountRate(c.getDouble(ix_discount_rate));
            objProduct.setDiscount(c.getDouble(ix_discount));
            objProduct.setAdditionalDiscount(c.getDouble(ix_additional_discount));
            objProduct.setTotalAmount(c.getDouble(ix_total_amount));
            objProduct.setScheme_rlProductSkuId(c.getInt(ix_scheme_rl_product_sku_id));
            objProduct.setScheme_qty(c.getInt(ix_scheme_qty));
            objProduct.setIsScheme(c.getInt(ix_is_scheme) != 0);
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

    public boolean hasOrders(int soId, String appShopId, Date orderDate) {

        List<SalesOrder> orders = getShopOrders(soId, appShopId, orderDate);
        return orders.size() > 0;

    }

    public void updateShopId(Shop[] shops) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        for (Shop shop : shops) {

            sql = "";
            sql += " UPDATE " + SalesOrder.TABLE + "";
            sql += " SET app_shop_id='" + shop.getShopId() + "'";
            sql += " WHERE app_shop_id='" + shop.getAppShopId() + "'";

            try {
                db.execSQL(sql);
            } catch (SQLException e) {
                Logger.log(Logger.ERROR, "ORDER_SHOP_ID_UPDATE", e.getMessage(), e);
                return;
            }
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public double getTodaySalesValue(int soId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT sum(total_amount) As total_amount";
        sql += " FROM " + SalesOrder.TABLE + "";
        sql += " WHERE  is_cancelled='0'";
        sql += " AND    so_id=" + soId + "";
        sql += " AND    order_date ='" + DateFunc.getDateStr(orderDate) + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return 0;
        }

        double totalValue = 0;

        while (c.moveToNext()) {
            totalValue = c.getDouble(c.getColumnIndexOrThrow("total_amount"));
        }

        closeCursor(c);
        return totalValue;
    }

    public List<vTodayOrders> getTodayOrders(int soId, Date orderDate) {

        List<vTodayOrders> vProducts = new ArrayList<vTodayOrders>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;


        sql = "";
        sql += " SELECT A.order_date, A.app_shop_id, A.agent_id,L.area_id, C.shop_name, A.set_no, A.rl_product_sku_id, B.product_name || ' ' || B.product_sku As product_name, A.order_qty As order_qty , A.rate,  round(A.total_amount) As order_value";
        sql += " FROM " + SalesOrder.TABLE + " As A";
        sql += " LEFT JOIN (    SELECT DISTINCT product_id, product_name, rl_product_sku_id, product_sku ";
        sql += "                FROM " + Product.TABLE + " ";
        sql += "                WHERE so_id=" + soId + "";
        sql += " ) As B ";
        sql += "        ON(A.rl_product_sku_id=B.rl_product_sku_id)";
        sql += " LEFT JOIN " + AreaAgent.TABLE + " As L ";
        sql += "        ON(A.agent_id = L.agentid AND A.So_Id=L.so_id) ";
        sql += " LEFT JOIN " + Shop.TABLE + " As C ";
        sql += "        ON(A.app_shop_id=C.app_shop_id AND A.So_Id=C.so_id) ";
        sql += " WHERE  A.so_id =" + soId + "";
        sql += " AND    A.is_cancelled='0'";
        sql += " AND    A.order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND    A.is_scheme='0'  ";
        sql += " ORDER BY A.app_shop_id ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodayOrders objProduct;

        int ix_order_date = c.getColumnIndexOrThrow("order_date");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_agent_id = c.getColumnIndexOrThrow("agent_id");
        int ix_area_id = c.getColumnIndexOrThrow("area_id");
        int ix_shop_name = c.getColumnIndexOrThrow("shop_name");
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_product_name = c.getColumnIndexOrThrow("product_name");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_order_value = c.getColumnIndexOrThrow("order_value");
        int ix_rate = c.getColumnIndexOrThrow("rate");

        while (c.moveToNext()) {

            String productName = c.getString(ix_product_name);
            int length = productName.indexOf("-");
            if (length > 0) {
                productName = productName.substring(0, length);
            }

            objProduct = new vTodayOrders();
            objProduct.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setAgentId(c.getInt(ix_agent_id));
            objProduct.setAreaId(c.getInt(ix_area_id));
            objProduct.setShopName(c.getString(ix_shop_name));
            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setProductName(productName);
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setRate(c.getDouble(ix_rate));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public List<vTodayOrders> getShopWiseTodayOrders(int soId, Date orderDate) {

        List<vTodayOrders> vProducts = new ArrayList<vTodayOrders>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;


        sql = "";
        sql += " SELECT DISTINCT A.app_shop_id, A.order_date, C.shop_name,sum(A.total_amount) As order_value";
        sql += " FROM " + SalesOrder.TABLE + " As A";
        sql += " LEFT JOIN " + Shop.TABLE + " As C ";
        sql += "        ON(A.app_shop_id=C.app_shop_id AND A.So_Id=C.so_id) ";
        sql += " WHERE  A.so_id =" + soId + "";
        sql += " AND    A.is_cancelled='0'";
        sql += " AND    A.order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND    A.is_scheme='0' group by A.app_shop_id,  C.shop_name ";
        sql += " ORDER BY C.shop_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodayOrders objProduct;

        int ix_order_date = c.getColumnIndexOrThrow("order_date");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_shop_name = c.getColumnIndexOrThrow("shop_name");
        int ix_order_value = c.getColumnIndexOrThrow("order_value");

        while (c.moveToNext()) {

            objProduct = new vTodayOrders();
            objProduct.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setShopName(c.getString(ix_shop_name));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public int[] getTCPC(int soId, Date orderDate) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;
        int[] tcPc = new int[2];

        tcPc[0] = 0;
        tcPc[1] = 0;

        sql = "";
        sql += " SELECT app_shop_id, 'CC' CallType";
        sql += " FROM  trn_no_order ";
        sql += " WHERE app_shop_id NOT IN(SELECT app_shop_id FROM trn_sales_order WHERE order_date = '" + DateFunc.getDateStr(orderDate) + "' AND so_id=" + soId + ")";
        sql += " AND order_date = '" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND so_id=" + soId + "";
        sql += " UNION ";
        sql += " SELECT app_shop_id, 'PC' CallType";
        sql += " FROM trn_sales_order";
        sql += " WHERE order_date = '" + DateFunc.getDateStr(orderDate) + "'";
        sql += " AND  is_cancelled='0' ";
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

    public List<vTodaySummary> getTodaySummary(int soId, Date orderDate) {

        List<vTodaySummary> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;


        sql = "";
        sql += " SELECT A.rl_product_sku_id, B.product_name || ' ' || B.product_sku As product_name, A.order_qty As order_qty , A.rate,  (A.total_amount) As order_value";
        sql += " FROM ( SELECT rl_product_sku_id, sum(order_qty) As order_qty , rate ,total_amount";
        sql += "        FROM " + SalesOrder.TABLE + " ";
        sql += "        WHERE  so_id =" + soId + "";
        sql += "        AND    is_cancelled='0'";
        sql += "        AND    order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += "        AND    is_scheme='0'  ";
        sql += "        GROUP BY rl_product_sku_id, rate,total_amount";
        sql += " ) As A ";
        sql += " LEFT JOIN (    SELECT DISTINCT product_id, product_name, rl_product_sku_id, product_sku " + "";
        sql += "                FROM " + Product.TABLE + " ";
        sql += "                WHERE so_id=" + soId + "";
        sql += " ) As B ";
        sql += "        ON(A.rl_product_sku_id=B.rl_product_sku_id)";
        sql += " ORDER BY B.product_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodaySummary objProduct;

        int ix_product_name = c.getColumnIndexOrThrow("product_name");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_order_value = c.getColumnIndexOrThrow("order_value");
        int ix_rate = c.getColumnIndexOrThrow("rate");

        String productName = "";
        while (c.moveToNext()) {

            productName = c.getString(ix_product_name);
            int length = productName.indexOf("-");
            if (length > 0) {
                productName = productName.substring(0, length);
            }

            objProduct = new vTodaySummary();
            objProduct.setProductName(productName);
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setRate(c.getDouble(ix_rate));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public boolean hasTodaysOrders() {
        boolean empty = true;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+ SalesOrder.TABLE , null);

        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) != 0);
        }

        closeCursor(cur);

        return empty;
    }

    public List<vTodaySummary> getTodaySummarySKU(int soId, Date orderDate) {

        List<vTodaySummary> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;


        sql = "";
        sql += " SELECT A.rl_product_sku_id,B.product_name || ' ' || B.product_sku As product_name , 0 As target_qty , A.order_qty As order_qty , A.rate , '' As order_value";
        sql += " FROM ( SELECT rl_product_sku_id, sum(order_qty) As order_qty , rate ";
        sql += "        FROM " + SalesOrder.TABLE + " ";
        sql += "        WHERE  so_id =" + soId + "";
        sql += "        AND    is_cancelled='0'";
        sql += "        AND    order_date ='" + DateFunc.getDateStr(orderDate) + "'";
        sql += "        AND    is_scheme='0'  ";
        sql += "        GROUP BY rl_product_sku_id, rate";
        sql += " ) As A ";
        sql += " LEFT JOIN (    SELECT DISTINCT product_id, product_name, rl_product_sku_id, product_sku " + "";
        sql += "                FROM " + Product.TABLE + " ";
        sql += "                WHERE so_id=" + soId + "";
        sql += "                ORDER BY product_sku";
        sql += " ) As B ";
        sql += "        ON(A.rl_product_sku_id=B.rl_product_sku_id)";


        sql += " ORDER BY B.product_name, B.product_sku ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodaySummary objProduct;

        int ix_product_name = c.getColumnIndexOrThrow("product_name");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_order_value = c.getColumnIndexOrThrow("order_value");
        int ix_rate = c.getColumnIndexOrThrow("rate");

        String productName = "";
        while (c.moveToNext()) {

            productName = Parse.toStr(c.getString(ix_product_name));

            int length = productName.indexOf("-");
            if (length > 0) {
                productName = productName.substring(0, length);
            }

            objProduct = new vTodaySummary();
            objProduct.setProductName(productName);
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setRate(c.getDouble(ix_rate));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public List<vTodayOrders> getToadyShopProductSummary(int soId,Date orderDate){
        List<vTodayOrders> vProducts = new ArrayList<vTodayOrders>();
        vProducts.clear();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        // salesorder - group by app_shop_id, rl_product_sku_id,
        // -> left product ->
        // -> left shopname ->


        //changes in 18-09-2019 : adding locality and route join : because one agent having multiple area
        sql = "  select B.app_shop_id, B.agent_id,T.area_id, B.order_date,  S.shop_name, D.rl_product_sku_id, D.product_name || ' ' || D.product_sku As ProductName,   B.order_qty, total_discount  As discount_rate, " +
                " B.total_amount As OrderValue  " +
                " from " +
                " ( " +
                " select order_date, agent_id, app_shop_id, rl_product_sku_id, SUM(order_qty) order_qty , SUM(discount + additional_discount) total_discount, SUM(total_amount) total_amount " +
                " from trn_sales_order " +
                " where so_id = " + soId +
                " and order_date = '"+ DateFunc.getDateStr(orderDate) +"' " +
                " and is_cancelled = '0' " +
                " group by order_date, agent_id, app_shop_id, rl_product_sku_id " +
                " ) B " +
                " LEFT JOIN ms_product D ON(B.rl_product_sku_id=D.rl_product_sku_id AND D.so_id = " + soId + ") " +
                " LEFT JOIN ms_shop As S  ON (B.app_shop_id = S.app_shop_id AND S.so_id = " + soId + ")  " +
                " LEFT JOIN (SELECT DISTINCT a.locality_id, area_id" +
                "  FROM ms_locality A" +
                "  LEFT JOIN ms_route B ON(A.route_id=B.route_id)) T ON(S.locality_id=T.locality_id) " +
                " Order by S.shop_name ";

       /* sql = "";
        sql += " SELECT B.app_shop_id,B.so_iD,B.agent_id,L.area_id, B.order_date,  S.shop_name, ";
        sql += " D.rl_product_sku_id, D.product_name || ' ' || D.product_sku As ProductName, ";
        sql += " B.order_qty, (B.discount + B.additional_discount) As discount_rate, sum(B.total_amount) As OrderValue";
        sql += " FROM " + SalesOrder.TABLE + " As B";
        sql += " LEFT JOIN " + Shop.TABLE + " As S ";
        sql += " 	  ON (B.app_shop_id = S.app_shop_id)";
        sql += " LEFT JOIN "+Product.TABLE + " As D ";
        sql += "		  ON(B.rl_product_sku_id=D.rl_product_sku_id AND D.so_id = " + soId +")";
        sql += " LEFT JOIN " + AreaAgent.TABLE + " As L ";
        sql += " 	  ON (B.agent_id = L.agent_id)";
        sql += " WHERE B.so_iD = " + soId + "";
        sql += " AND B.order_date = '" + DateFunc.getDateStr(orderDate)+"'";
        sql += " AND B.is_cancelled = '0'";
        sql += " GROUP BY B.app_shop_id, S.shop_name,D.rl_product_sku_id,D.product_name,B.order_qty";*/


        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodayOrders objProduct;

        int ix_agent_id = c.getColumnIndexOrThrow("agent_id");
        int ix_area_id = c.getColumnIndexOrThrow("area_id");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_shop_name = c.getColumnIndexOrThrow("shop_name");
        int ix_product_name = c.getColumnIndexOrThrow("ProductName");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_discount_rate = c.getColumnIndexOrThrow("discount_rate");
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_order_date = c.getColumnIndexOrThrow("order_date");
        int ix_order_value = c.getColumnIndexOrThrow("OrderValue");

        String productName = "";
        while (c.moveToNext()) {

            productName = c.getString(ix_product_name);
            int length = productName.indexOf("-");
            if (length > 0) {
                productName = productName.substring(0, length);
            }


            objProduct = new vTodayOrders();
            objProduct.setAgentId(c.getInt(ix_agent_id));
            objProduct.setAreaId(c.getInt(ix_area_id));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setShopName(c.getString(ix_shop_name));
            objProduct.setProductName(productName);
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setDiscount(c.getString(ix_discount_rate));
            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public List<vTodayOrders> getToadyAgentProductSummary(int soId,Date orderDate){
        List<vTodayOrders> vProducts = new ArrayList<vTodayOrders>();
        vProducts.clear();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        // salesorder - group by app_shop_id, rl_product_sku_id,
        // -> left product ->
        // -> left shopname ->


        //changes in 18-09-2019 : adding locality and route join : because one agent having multiple area
        sql = "  select B.app_shop_id, B.agent_id,rl.area_id, B.order_date,  S.agent_name, D.rl_product_sku_id, D.product_name || ' ' || D.product_sku As ProductName,   B.order_qty, total_discount  As discount_rate, " +
                " B.total_amount As OrderValue  " +
                " from " +
                " ( " +
                " select order_date, agent_id, app_shop_id, rl_product_sku_id, SUM(order_qty) order_qty , SUM(discount + additional_discount) total_discount, SUM(total_amount) total_amount " +
                " from trn_sales_order " +
                " where so_id = " + soId +
                " and order_date = '"+ DateFunc.getDateStr(orderDate) +"' " +
                " and is_cancelled = '0' " +
                " group by order_date, agent_id, app_shop_id, rl_product_sku_id " +
                " ) B " +
                " LEFT JOIN ms_product D ON(B.rl_product_sku_id=D.rl_product_sku_id AND D.so_id = " + soId + ") " +
                " LEFT JOIN ms_agent As S  ON (B.agent_id = S.agent_id AND S.so_id = " + soId + ")  " +
                " LEFT JOIN rl_area_agent As rl  ON (B.agent_id = rl.agent_id AND rl.so_id = " + soId + ")  " +
                " Order by S.agent_name ";


        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return null;
        }

        vTodayOrders objProduct;

        int ix_agent_id = c.getColumnIndexOrThrow("agent_id");
        int ix_area_id = c.getColumnIndexOrThrow("area_id");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_agent_name = c.getColumnIndexOrThrow("agent_name");
        int ix_product_name = c.getColumnIndexOrThrow("ProductName");
        int ix_order_qty = c.getColumnIndexOrThrow("order_qty");
        int ix_discount_rate = c.getColumnIndexOrThrow("discount_rate");
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_order_date = c.getColumnIndexOrThrow("order_date");
        int ix_order_value = c.getColumnIndexOrThrow("OrderValue");

        String productName = "";
        while (c.moveToNext()) {

            productName = c.getString(ix_product_name);
            int length = productName.indexOf("-");
            if (length > 0) {
                productName = productName.substring(0, length);
            }


            objProduct = new vTodayOrders();
            objProduct.setAgentId(c.getInt(ix_agent_id));
            objProduct.setAreaId(c.getInt(ix_area_id));
            objProduct.setAppShopId(c.getString(ix_app_shop_id));
            objProduct.setAgentName(c.getString(ix_agent_name));
            objProduct.setProductName(productName);
            objProduct.setOrderQty(c.getInt(ix_order_qty));
            objProduct.setDiscount(c.getString(ix_discount_rate));
            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            objProduct.setOrderValue(c.getDouble(ix_order_value));
            vProducts.add(objProduct);
        }

        closeCursor(c);
        return vProducts;
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + SalesOrder.TABLE);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + SalesOrder.TABLE + " WHERE so_id=" + soId + "");

        DatabaseManager.getInstance().closeDatabase();
    }



}
