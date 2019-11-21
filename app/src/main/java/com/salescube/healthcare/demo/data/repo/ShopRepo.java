package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vShop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 19/09/2016.
 */

public class ShopRepo extends RepoBase {
    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Shop.TABLE + "("
                + Shop.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Shop.COL_SO_ID + " INT ,"
                + Shop.COL_SHOP_ID + " INT ,"
                + Shop.COL_APP_SHOP_ID + " INT ,"
                + Shop.COL_SHOP_NAME + " TEXT ,"
                + Shop.COL_LOCALITY_ID + " INT ,"
                + Shop.COL_REG_NO + " TEXT ,"
                + Shop.COL_GST_NO + " TEXT ,"
                + Shop.COL_CONTACT_NO + " TEXT ,"
                + Shop.COL_MOBILE_NO + " TEXT ,"
                + Shop.COL_OWNER_NAME + " TEXT ,"
                + Shop.COL_AUTO_SMS + " BOOLEAN ,"
                + Shop.COL_SHOP_TYPE_ID + " INT ,"
                + Shop.COL_SHOP_LOCATION + " TEXT ,"
                + Shop.COL_STATUS + " TEXT ,"
                + Shop.COL_RANK + " TEXT ,"
                + Shop.COL_REPLACE_WITH + " TEXT ,"
                + Shop.COL_UPDATABLE + " BOOLEAN, "
                + Shop.COL_CREATED_DATE_TIME + " DATETIME, "
                + Shop.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }


    private void insert(Shop objShop, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Shop.COL_SO_ID, objShop.getSoId());
        values.put(Shop.COL_SHOP_ID, objShop.getShopId());
        values.put(Shop.COL_APP_SHOP_ID, objShop.getAppShopId());
        values.put(Shop.COL_SHOP_NAME, objShop.getShopName());
        values.put(Shop.COL_LOCALITY_ID, objShop.getLocalityId());
        values.put(Shop.COL_REG_NO, objShop.getRegNo());
        values.put(Shop.COL_GST_NO, objShop.getGstNo());
        values.put(Shop.COL_CONTACT_NO, objShop.getContactNo());
        values.put(Shop.COL_MOBILE_NO, objShop.getMobileNo());
        values.put(Shop.COL_OWNER_NAME, objShop.getOwnerName());
        values.put(Shop.COL_AUTO_SMS, objShop.getAutoSMS());
        values.put(Shop.COL_SHOP_TYPE_ID, objShop.getShopTypeId());
        values.put(Shop.COL_SHOP_LOCATION, objShop.getShopLocation());
        values.put(Shop.COL_STATUS, objShop.getShopStatus());
        values.put(Shop.COL_RANK, objShop.getShopRank());
        values.put(Shop.COL_REPLACE_WITH, objShop.getReplacedWith());
        values.put(Shop.COL_UPDATABLE, objShop.isUpdatable());
        values.put(Shop.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(Shop.COL_IS_POSTED, false);

        try {
            db.insert(Shop.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(Shop[] objShopList, SQLiteDatabase db) {

        for (Shop objShop : objShopList) {
            insert(objShop, db);
        }
    }

//    public void  deleteAll(int soId){
//
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " DELETE FROM " + Shop.TABLE + "";
//        sql += " WHERE locality_id IN ( ";
//        sql += "                    SELECT DISTINCT locality_id ";
//        sql += "                    FROM " + Agent_Locality.TABLE + "";
//        sql += "                    WHERE so_id=" + soId + "";
//        sql += " )";
//
//        db.execSQL(sql);
//        DatabaseManager.getInstance().closeDatabase();
//    }

    public void deleteAll(int soId) {


        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + Shop.TABLE + "";
        sql += " WHERE so_id=" + soId + "";

        db.execSQL(sql);
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean isExist(String appShopId, String shopName, int localityId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT shop_name ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE upper(trim(shop_name))= upper(trim('" + shopName + "'))";
        sql += " AND locality_id=" + localityId + "";
        sql += " AND upper(trim(app_shop_id)) != upper(trim('" + appShopId + "'))";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return false;
        }

        boolean result = false;

        if (c.getCount() > 0) {
            result = true;
        }

        closeCursor(c);
        return result;
    }

    public void markPosted(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + Shop.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE so_id=" + soId + "";
        sql += " AND is_posted='0'";
        sql += " AND updatable='1'";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }


    public void markPosted() {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + Shop.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        sql += " AND updatable='1'";

        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Shop[] objShopList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(objShopList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Shop objShopList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objShopList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateShops(Shop[] shops) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values;
        String sql = "";

        for (Shop objShop : shops) {

            values = new ContentValues();
            values.put(Shop.COL_SHOP_ID, Parse.toStr(objShop.getShopId()));
            values.put(Shop.COL_APP_SHOP_ID, Parse.toStr(objShop.getAppShopId()));
            values.put(Shop.COL_SHOP_NAME, Parse.toStr(objShop.getShopName()));
            values.put(Shop.COL_REG_NO, Parse.toStr(objShop.getRegNo()));
            values.put(Shop.COL_GST_NO, Parse.toStr(objShop.getGstNo()));
            values.put(Shop.COL_CONTACT_NO, Parse.toStr(objShop.getContactNo()));
            values.put(Shop.COL_MOBILE_NO, Parse.toStr(objShop.getMobileNo()));
            values.put(Shop.COL_OWNER_NAME, Parse.toStr(objShop.getOwnerName()));
            values.put(Shop.COL_AUTO_SMS, objShop.getAutoSMS());
            values.put(Shop.COL_SHOP_TYPE_ID, Parse.toInt(objShop.getShopTypeId()));
            values.put(Shop.COL_STATUS, Parse.toStr(objShop.getShopStatus()));
            values.put(Shop.COL_REPLACE_WITH, objShop.getReplacedWith());

            // TODO: Testing Pending 13/04/2017 03:29
            // issue Shop Name not updating correctly

            values.put(Shop.COL_UPDATABLE, false);
            values.put(Shop.COL_IS_POSTED, true);

            try {
                db.update(Shop.TABLE, values, "app_shop_id=?", new String[]{String.valueOf(objShop.getAppShopId())});
            } catch (Exception ex) {
                Logger.log(Logger.ERROR, "SHOP_UPDATE", ex.getMessage(), ex);
            }

            sql = "";
            sql += " UPDATE " + Shop.TABLE + "";
            sql += " SET app_shop_id='" + objShop.getShopId() + "'";
            sql += " WHERE app_shop_id='" + objShop.getAppShopId() + "'";

            try {
                db.execSQL(sql);
            } catch (Exception ex) {
                Logger.log(Logger.ERROR, "SHOP_UPDATE", ex.getMessage(), ex);
                throw ex;
            }
        }

        DatabaseManager.getInstance().closeDatabase();

    }

    public void changeNewShopStatus() {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql += " UPDATE " + Shop.TABLE;
        sql += " SET updatable ='1', is_posted='0' ";
        sql += " WHERE app_shop_id like 'N%'";

        try {
            db.execSQL(sql);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();

    }

    public int getAllCount(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT count(*) count";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE so_Id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return 0;
        }

        if (c.moveToNext()) {
            return c.getInt(0);
        }

        closeCursor(c);
        return 0;
    }

    public void update(Shop objShop) {

        if (TextUtils.isEmpty(objShop.getAppShopId())) {
            return;
        }
        if (objShop.getAppShopId().equalsIgnoreCase("0")) {
            return;
        }

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(Shop.COL_SHOP_NAME, Parse.toStr(objShop.getShopName()));
        values.put(Shop.COL_REG_NO, Parse.toStr(objShop.getRegNo()));
        values.put(Shop.COL_GST_NO, Parse.toStr(objShop.getGstNo()));
        values.put(Shop.COL_CONTACT_NO, Parse.toStr(objShop.getContactNo()));
        values.put(Shop.COL_MOBILE_NO, Parse.toStr(objShop.getMobileNo()));
        values.put(Shop.COL_OWNER_NAME, Parse.toStr(objShop.getOwnerName()));
        values.put(Shop.COL_AUTO_SMS, objShop.getAutoSMS());
        values.put(Shop.COL_SHOP_TYPE_ID, Parse.toInt(objShop.getShopTypeId()));
        values.put(Shop.COL_SHOP_LOCATION, Parse.toStr(objShop.getShopLocation()));
        values.put(Shop.COL_STATUS, Parse.toStr(objShop.getShopStatus()));
        values.put(Shop.COL_RANK, Parse.toStr(objShop.getShopRank()));
        values.put(Shop.COL_REPLACE_WITH, objShop.getReplacedWith());
        values.put(Shop.COL_UPDATABLE, true);
        values.put(Shop.COL_IS_POSTED, false);

        try {
            db.update(Shop.TABLE, values, "app_shop_id=?", new String[]{String.valueOf(objShop.getAppShopId())});
        } catch (Exception ex) {
            throw ex;
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public Shop getShopById(int id) {

        Shop objShops = new Shop();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT * ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE shop_id=" + id + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            return objShops;
        }

        List<Shop> shops = fillShops(c);

        if (shops.size() > 0) {
            return shops.get(0);
        } else {
            return new Shop();
        }

    }

    public Shop getShopById(String appShopid) {

        Shop objShops = new Shop();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT * ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE app_shop_id='" + appShopid + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            return objShops;
        }

        List<Shop> shops = fillShops(c);

        if (shops.size() > 0) {
            return shops.get(0);
        } else {
            return null;
        }

    }

    public boolean isShopUploadPending() {
        int records = 0;

        try {
            records = getShopForUpload().size();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

        return records > 0;
    }

    public List<Shop> getShopForUpload(int soId) {

        List<Shop> objShops = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT * ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE is_posted='0'";
        sql += " AND updatable='1'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            return objShops;
        }

        objShops = fillShops(c);
        return objShops;
    }

    public List<Shop> getShopForUpload() {

        List<Shop> objShops = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT * ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE is_posted='0'";
        sql += " AND updatable='1'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            return objShops;
        }

        objShops = fillShops(c);
        return objShops;
    }

    private List<Shop> fillShops(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(Shop.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(Shop.COL_SO_ID);
        int ix_shop_id = c.getColumnIndexOrThrow(Shop.COL_SHOP_ID);
        int ix_app_shop_id = c.getColumnIndexOrThrow(Shop.COL_APP_SHOP_ID);
        int ix_shop_name = c.getColumnIndexOrThrow(Shop.COL_SHOP_NAME);
        int ix_locality_id = c.getColumnIndexOrThrow(Shop.COL_LOCALITY_ID);
        int ix_reg_no_id = c.getColumnIndexOrThrow(Shop.COL_REG_NO);
        int ix_gst_no_id = c.getColumnIndexOrThrow(Shop.COL_GST_NO);
        int ix_mobile_no = c.getColumnIndexOrThrow(Shop.COL_MOBILE_NO);
        int ix_contact_no = c.getColumnIndexOrThrow(Shop.COL_CONTACT_NO);
        int ix_owner_name = c.getColumnIndexOrThrow(Shop.COL_OWNER_NAME);
        int ix_auto_sms = c.getColumnIndexOrThrow(Shop.COL_AUTO_SMS);
        int ix_shop_type = c.getColumnIndexOrThrow(Shop.COL_SHOP_TYPE_ID);
        int ix_shop_location = c.getColumnIndexOrThrow(Shop.COL_SHOP_LOCATION);
        int ix_shop_status = c.getColumnIndexOrThrow(Shop.COL_STATUS);
        int ix_shop_rank = c.getColumnIndexOrThrow(Shop.COL_RANK);
        int ix_replace_with = c.getColumnIndexOrThrow(Shop.COL_REPLACE_WITH);
        int ix_updatable = c.getColumnIndexOrThrow(Shop.COL_UPDATABLE);
        int ix_is_posted = c.getColumnIndexOrThrow(Shop.COL_IS_POSTED);
        int ix_created_date_time = c.getColumnIndexOrThrow(Shop.COL_CREATED_DATE_TIME);

        Shop objAgent;
        List<Shop> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            objAgent = new Shop();
            objAgent.setId(c.getInt(ix_id));
            objAgent.setSoId(c.getInt(ix_so_id));
            objAgent.setShopId(c.getInt(ix_shop_id));
            objAgent.setAppShopId(c.getString(ix_app_shop_id));
            objAgent.setShopName(c.getString(ix_shop_name));
            objAgent.setLocalityId(c.getInt(ix_locality_id));
            objAgent.setRegNo(c.getString(ix_reg_no_id));
            objAgent.setGstNo(c.getString(ix_gst_no_id));
            objAgent.setMobileNo(c.getString(ix_mobile_no));
            objAgent.setContactNo(c.getString(ix_contact_no));
            objAgent.setOwnerName(c.getString(ix_owner_name));
            objAgent.setAutoSMS(c.getString(ix_auto_sms).equals("1"));
            objAgent.setShopTypeId(c.getInt(ix_shop_type));
            objAgent.setShopLocation(c.getString(ix_shop_location));
            objAgent.setShopStatus(c.getString(ix_shop_status));
            objAgent.setShopRank(c.getString(ix_shop_rank));
            objAgent.setReplacedWith(c.getString(ix_replace_with));
            objAgent.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            objAgent.setUpdatable(c.getString(ix_updatable).equals("1"));
            objAgent.setPosted(c.getString(ix_is_posted).equals("1"));

            coldCallList.add(objAgent);
        }

        return coldCallList;
    }

    public List<vShop> getShopsAll(int soId) {

        List<vShop> objShops = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

//        sql += " SELECT DISTINCT shop_id, shop_name, app_shop_id, locality_id ";
//        sql += " FROM " + Shop.TABLE;
//        sql += " WHERE so_Id=" + soId + "";
//        sql += " ORDER BY shop_name ";

        sql += " SELECT id, shop_id, shop_name, app_shop_id, locality_id, shop_status ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " AND shop_status='" + Constant.ShopStatus.LIVE + "'";
        sql += " ORDER BY shop_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return objShops;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_shop_id = c.getColumnIndexOrThrow("shop_id");
        int ix_shop_name = c.getColumnIndexOrThrow("shop_name");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_locality_id = c.getColumnIndexOrThrow("locality_id");
        int ix_shop_status = c.getColumnIndexOrThrow(("shop_status"));

        vShop objAgent;
        while (c.moveToNext()) {

            objAgent = new vShop();
            objAgent.setId(c.getInt(ix_id));
            objAgent.setShopId(c.getInt(ix_shop_id));
            objAgent.setShopName(c.getString(ix_shop_name));
            objAgent.setAppShopId(c.getString(ix_app_shop_id));
            objAgent.setLocalityId(c.getInt(ix_locality_id));
            objAgent.setShopStatus(c.getString(ix_shop_status));
            objShops.add(objAgent);
        }

        closeCursor(c);
        return objShops;
    }

    public List<vShop> getShopsAll(int soId, int localityId) {

        List<vShop> objShops = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

//        sql += " SELECT DISTINCT shop_id, shop_name, app_shop_id, locality_id ";
//        sql += " FROM " + Shop.TABLE;
//        sql += " WHERE so_Id=" + soId + "";
//        sql += " ORDER BY shop_name ";

        sql += " SELECT id, shop_id, shop_name, app_shop_id, locality_id, shop_status ";
        sql += " FROM " + Shop.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " AND locality_id=" + localityId + "";
        sql += " AND shop_status='" + Constant.ShopStatus.LIVE + "'";
        sql += " ORDER BY shop_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return objShops;
        }

        if (c.getCount() == 0) {
            closeCursor(c);
            return objShops;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_shop_id = c.getColumnIndexOrThrow("shop_id");
        int ix_shop_name = c.getColumnIndexOrThrow("shop_name");
        int ix_app_shop_id = c.getColumnIndexOrThrow("app_shop_id");
        int ix_locality_id = c.getColumnIndexOrThrow("locality_id");
        int ix_shop_status = c.getColumnIndexOrThrow(("shop_status"));

        vShop objAgent;
        while (c.moveToNext()) {

            objAgent = new vShop();
            objAgent.setId(c.getInt(ix_id));
            objAgent.setShopId(c.getInt(ix_shop_id));
            objAgent.setShopName(c.getString(ix_shop_name));
            objAgent.setAppShopId(c.getString(ix_app_shop_id));
            objAgent.setLocalityId(c.getInt(ix_locality_id));
            objAgent.setShopStatus(c.getString(ix_shop_status));
            objShops.add(objAgent);
        }

        closeCursor(c);
        return objShops;
    }

}

