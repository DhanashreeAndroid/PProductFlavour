package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 20/10/2016.
 */

public class POPShopRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + POPShop.TABLE + "("
                + POPShop.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + POPShop.COL_SO_ID + " INT ,"
                + POPShop.COL_TR_DATE + " DATE ,"
                + POPShop.COL_APP_SHOP_ID + " TEXT ,"
                + POPShop.COL_POP_ID + " TEXT ,"
                + POPShop.COL_POP_QTY + " INT ,"
                + POPShop.COL_AGENT_ID + " INT ,"
                + POPShop.COL_IMAGE_NAME + " TEXT ,"
                + POPShop.COL_IMAGE_PATH + " TEXT ,"
                + POPShop.COL_CREATED_DATE_TIME + " DATETIME, "
                + POPShop.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(POPShop obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(POPShop.COL_SO_ID, obj.getSoId());
        values.put(POPShop.COL_TR_DATE, DateFunc.getDateStr(obj.getTrDate()));
        values.put(POPShop.COL_APP_SHOP_ID, obj.getAppShopId());
        values.put(POPShop.COL_POP_ID, obj.getPopId());
        values.put(POPShop.COL_POP_QTY, obj.getPopQty());
        values.put(POPShop.COL_AGENT_ID, obj.getAgentId());
        values.put(POPShop.COL_IMAGE_NAME, obj.getImageName());
        values.put(POPShop.COL_IMAGE_PATH, obj.getImagePath());
        values.put(POPShop.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(POPShop.COL_IS_POSTED, false);

        try {
            db.insert(POPShop.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(POPShop[] objList, SQLiteDatabase db) {

        for (POPShop productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + POPShop.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete(POPShop img){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + POPShop.TABLE + " WHERE id=" + img.getId() + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(POPShop objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(POPShop[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public  void markPosted(int soId){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + POPShop.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public  void markPosted(){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + POPShop.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }


    public List<POPShop> getPOPEntrys(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1: 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + POPShop.TABLE + "";
        sql += " WHERE is_posted='"+ is_posted +"'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return  fillOrders(c);
    }

    public List<POPShop> getPOPEntriesNotPosted(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + POPShop.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return  fillOrders(c);
    }

//    public List<POPShop> getPOPEntrysNotPosted() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + POPShop.TABLE + "";
//        sql += " WHERE is_posted='0'";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//
//            return null;
//        }
//
//        return  fillOrders(c);
//    }

    private List<POPShop> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(POPShop.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(POPShop.COL_SO_ID);
        int ix_tr_date = c.getColumnIndexOrThrow(POPShop.COL_TR_DATE);
        int ix_app_shop_id = c.getColumnIndexOrThrow(POPShop.COL_APP_SHOP_ID);
        int ix_pop_id = c.getColumnIndexOrThrow(POPShop.COL_POP_ID);
        int ix_pop_qty = c.getColumnIndexOrThrow(POPShop.COL_POP_QTY);
        int ix_agent_id = c.getColumnIndexOrThrow(POPShop.COL_AGENT_ID);
        int ix_image_name = c.getColumnIndexOrThrow(POPShop.COL_IMAGE_NAME);
        int ix_image_path = c.getColumnIndexOrThrow(POPShop.COL_IMAGE_PATH);
        int ix_created_date_time = c.getColumnIndexOrThrow(POPShop.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(POPShop.COL_IS_POSTED);

        POPShop coldCall;
        List<POPShop> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new POPShop();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setTrDate(DateFunc.getDate(c.getString(ix_tr_date)));
            coldCall.setAppShopId(c.getString(ix_app_shop_id));
            coldCall.setPopId(c.getInt(ix_pop_id));
            coldCall.setPopQty(c.getInt(ix_pop_qty));
            coldCall.setAgentId(c.getInt(ix_agent_id));
            coldCall.setImageName(c.getString(ix_image_name));
            coldCall.setImagePath(c.getString(ix_image_path));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }
}
