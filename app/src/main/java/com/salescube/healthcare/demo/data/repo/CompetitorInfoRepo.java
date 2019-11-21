package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vCompetitorReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 04/11/2016.
 */

public class CompetitorInfoRepo {
    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + CompetitorInfo.TABLE + "("
                + CompetitorInfo.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + CompetitorInfo.COL_TR_DATE + " DATE, "
                + CompetitorInfo.COL_SO_ID + " INT ,"
                + CompetitorInfo.COL_AGENT_ID + " INT ,"
                + CompetitorInfo.COL_APP_SHOP_ID + " TEXT ,"
                + CompetitorInfo.COL_PRODUCT_ID + " INT ,"
                + CompetitorInfo.COL_COMPETITOR_PRODUCT + " TEXT ,"
                + CompetitorInfo.COL_GMS + " TEXT ,"
                + CompetitorInfo.COL_RETAILER_NET_RATE + " DOUBLE(5,2) ,"
                + CompetitorInfo.COL_SCHEME + " TEXT ,"
                + CompetitorInfo.COL_MRP + " DOUBLE(5,2) ,"
                + CompetitorInfo.COL_IMAGE_NAME + " TEXT ,"
                + CompetitorInfo.COL_IMAGE_PATH + " TEXT ,"
                + CompetitorInfo.COL_STOCK + " TEXT ,"
                + CompetitorInfo.COL_CREATED_DATE_TIME + " DATETIME, "
                + CompetitorInfo.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(CompetitorInfo obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();


        values.put(CompetitorInfo.COL_TR_DATE, DateFunc.getDateStr(obj.getTrDate()));
        values.put(CompetitorInfo.COL_SO_ID, obj.getSoId());
        values.put(CompetitorInfo.COL_AGENT_ID, obj.getAgentId());
        values.put(CompetitorInfo.COL_APP_SHOP_ID, obj.getAppShopId());
        values.put(CompetitorInfo.COL_PRODUCT_ID, obj.getProductId());
        values.put(CompetitorInfo.COL_COMPETITOR_PRODUCT, obj.getCompetitorProduct());
        values.put(CompetitorInfo.COL_GMS, obj.getGms());
        values.put(CompetitorInfo.COL_RETAILER_NET_RATE, obj.getRetailerRate());
        values.put(CompetitorInfo.COL_SCHEME, obj.getScheme());
        values.put(CompetitorInfo.COL_MRP, obj.getMrp());
        values.put(CompetitorInfo.COL_IMAGE_NAME, obj.getImageName());
        values.put(CompetitorInfo.COL_IMAGE_PATH, obj.getImagePath());
        values.put(CompetitorInfo.COL_STOCK, obj.getStock());
        values.put(CompetitorInfo.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(CompetitorInfo.COL_IS_POSTED, false);

        try {
            db.insert(CompetitorInfo.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(CompetitorInfo[] objList, SQLiteDatabase db) {

        for (CompetitorInfo productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + CompetitorInfo.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(CompetitorInfo objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(CompetitorInfo[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + CompetitorInfo.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void testDM(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + CompetitorInfo.TABLE + "";
        sql += " SET gms='', competitor_product=''";
        sql += " WHERE id IN(2,3)";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted() {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + CompetitorInfo.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(CompetitorInfo img) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + CompetitorInfo.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        sql += " AND id=" + img.getId() + "";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<CompetitorInfo> getInfo(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1 : 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + CompetitorInfo.TABLE + "";
        sql += " WHERE is_posted='" + is_posted + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }

    public void delete(CompetitorInfo img){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + CompetitorInfo.TABLE + " WHERE id=" + img.getId() + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vCompetitorReport> getCompetitorReport(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT A.id, B.product_name, A.competitor_product, A.gms, A.retailer_net_rate, A.scheme, A.stock, a.mrp ";
        sql += " FROM "+ CompetitorInfo.TABLE +" A";
        sql += " LEFT JOIN ( SELECT DISTINCT product_id, product_name ";
        sql += "             FROM "+ Product.TABLE +" ";
        sql += " ) B ON(A.product=B.product_id)";
        sql += " WHERE A.so_id = " + userId +"";
        sql += " ORDER BY created_date_time desc ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_target_product = c.getColumnIndexOrThrow("product_name");
        int ix_product = c.getColumnIndexOrThrow("competitor_product");
        int ix_gms = c.getColumnIndexOrThrow("gms");
        int ix_ret_rate = c.getColumnIndexOrThrow("retailer_net_rate");
        int ix_scheme = c.getColumnIndexOrThrow("scheme");
        int ix_stock = c.getColumnIndexOrThrow("stock");
        int ix_mrp = c.getColumnIndexOrThrow("mrp");

        vCompetitorReport work;
        List<vCompetitorReport> report = new ArrayList<>();

        while (c.moveToNext()) {

            work = new vCompetitorReport();
            work.setId(c.getInt(ix_id));
            work.setTargetProduct(c.getString(ix_target_product));
            work.setProduct(c.getString(ix_product));
            work.setGrams(c.getString(ix_gms));
            work.setRetailerRate(c.getDouble(ix_ret_rate));
            work.setScheme(c.getString(ix_scheme));
            work.setStock(c.getString(ix_stock));
            work.setMrp(c.getDouble(ix_mrp));
            report.add(work);

        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();

        return report;
    }

    public List<CompetitorInfo> getInfoNotPosted(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();


        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + CompetitorInfo.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }

//    public List<CompetitorInfo> getInfoNotPosted() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + CompetitorInfo.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//
//            return null;
//        }
//
//        return fillOrders(c);
//    }


    private List<CompetitorInfo> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_ID);
        int ix_tr_date = c.getColumnIndexOrThrow(CompetitorInfo.COL_TR_DATE);
        int ix_so_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_SO_ID);
        int ix_agent_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_AGENT_ID);
        int ix_shop_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_APP_SHOP_ID);
        int ix_product_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_PRODUCT_ID);
        int ix_competitor_product_id = c.getColumnIndexOrThrow(CompetitorInfo.COL_COMPETITOR_PRODUCT);
        int ix_gms = c.getColumnIndexOrThrow(CompetitorInfo.COL_GMS);
        int ix_retailer_net_rate = c.getColumnIndexOrThrow(CompetitorInfo.COL_RETAILER_NET_RATE);
        int ix_scheme = c.getColumnIndexOrThrow(CompetitorInfo.COL_SCHEME);
        int ix_mrp = c.getColumnIndexOrThrow(CompetitorInfo.COL_MRP);
        int ix_image_name = c.getColumnIndexOrThrow(CompetitorInfo.COL_IMAGE_NAME);
        int ix_image_path = c.getColumnIndexOrThrow(CompetitorInfo.COL_IMAGE_PATH);
        int ix_stock = c.getColumnIndexOrThrow(CompetitorInfo.COL_STOCK);
        int ix_created_date_time = c.getColumnIndexOrThrow(CompetitorInfo.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(CompetitorInfo.COL_IS_POSTED);

        CompetitorInfo coldCall;
        List<CompetitorInfo> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new CompetitorInfo();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setTrDate(DateFunc.getDate(c.getString(ix_tr_date)));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setAgentId(c.getInt(ix_agent_id));
            coldCall.setAppShopId(c.getString(ix_shop_id));
            coldCall.setProductId(c.getInt(ix_product_id));
            coldCall.setCompetitorProduct(c.getString(ix_competitor_product_id));
            coldCall.setGms(c.getString(ix_gms));
            coldCall.setRetailerRate(c.getDouble(ix_retailer_net_rate));
            coldCall.setScheme(c.getString(ix_scheme));
            coldCall.setMrp(c.getDouble(ix_mrp));
            coldCall.setImageName(c.getString(ix_image_name));
            coldCall.setImagePath(c.getString(ix_image_path));
            coldCall.setStock(c.getString(ix_stock));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }
}
