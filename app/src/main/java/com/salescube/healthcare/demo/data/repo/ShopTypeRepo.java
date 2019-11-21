package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ShopType;
import com.salescube.healthcare.demo.view.vShopType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17/11/2016.
 */

public class ShopTypeRepo {
    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + ShopType.TABLE + "("
                + ShopType.COL_ID + " INT ,"
                + ShopType.COL_SHOP_TYPE_ID + " INT ,"
                + ShopType.COL_SHOP_TYPE_NAME + " TEXT "
                + ")";

        return qry;
    }

    private void insert(ShopType obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(ShopType.COL_SHOP_TYPE_ID, obj.getShopTypeId());
        values.put(ShopType.COL_SHOP_TYPE_NAME, obj.getShopType());

        try {
            db.insert(ShopType.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(ShopType[] objList, SQLiteDatabase db) {

        for (ShopType productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + ShopType.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ShopType objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ShopType[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vShopType> getReasonList() {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + ShopType.TABLE + "";
        sql += " ORDER BY shop_type_name " ;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return fillOrders(c);
    }


    private List<vShopType> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(ShopType.COL_ID);
        int ix_shop_type_id = c.getColumnIndexOrThrow(ShopType.COL_SHOP_TYPE_ID);
        int ix_shop_type_name = c.getColumnIndexOrThrow(ShopType.COL_SHOP_TYPE_NAME);

        vShopType pop;
        List<vShopType> popList = new ArrayList<vShopType>();

        while (c.moveToNext()) {

            pop = new vShopType();
            pop.setShopTypeId(c.getInt(ix_shop_type_id));
            pop.setShopTypeName(c.getString(ix_shop_type_name));
            popList.add(pop);
        }

        return popList;
    }
}
