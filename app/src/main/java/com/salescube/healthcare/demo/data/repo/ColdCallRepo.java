package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 20/10/2016.
 */

public class ColdCallRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + NoOrder.TABLE + "("
                + NoOrder.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + NoOrder.COL_SO_ID + " INT ,"
                + NoOrder.COL_ORDER_DATE + " DATE ,"
                + NoOrder.COL_APP_SHOP_ID + " TEXT ,"
                + NoOrder.COL_REASON + " TEXT ,"
                + NoOrder.COL_AGENT_ID + " INT ,"
                + NoOrder.COL_CREATED_DATE_TIME + " DATETIME, "
                + NoOrder.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(NoOrder obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(NoOrder.COL_SO_ID, obj.getSoId());
        values.put(NoOrder.COL_ORDER_DATE, DateFunc.getDateStr(obj.getOrderDate()));
        values.put(NoOrder.COL_APP_SHOP_ID, obj.getAppShopId());
        values.put(NoOrder.COL_REASON, obj.getReason());
        values.put(NoOrder.COL_AGENT_ID, obj.getAgentId());
        values.put(NoOrder.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(NoOrder.COL_IS_POSTED, false);

        try {
            db.insert(NoOrder.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(NoOrder[] objList, SQLiteDatabase db) {

        for (NoOrder productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + NoOrder.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(NoOrder objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(NoOrder[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(List<NoOrder> list) {


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values;

        db.beginTransaction();

        for(NoOrder item: list) {

            values = new ContentValues();
            values.put(NoOrder.COL_IS_POSTED, true);
            db.update(NoOrder.TABLE,values,"id=?",new String[] { String.valueOf(item.getId()) });
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<NoOrder> getNoOrdersNotPosted(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + NoOrder.TABLE + "";
        sql += " WHERE is_posted='0'";
        sql += " AND so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillOrders(c);
    }

    private List<NoOrder> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(NoOrder.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(NoOrder.COL_SO_ID);
        int ix_order_date = c.getColumnIndexOrThrow(NoOrder.COL_ORDER_DATE);
        int ix_app_shop_id = c.getColumnIndexOrThrow(NoOrder.COL_APP_SHOP_ID);
        int ix_reason = c.getColumnIndexOrThrow(NoOrder.COL_REASON);
        int ix_agent_id = c.getColumnIndexOrThrow(NoOrder.COL_AGENT_ID);
        int ix_created_date_time = c.getColumnIndexOrThrow(NoOrder.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(NoOrder.COL_IS_POSTED);

        NoOrder coldCall;
        List<NoOrder> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new NoOrder();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setSoId(c.getInt(ix_so_id));
            coldCall.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));
            coldCall.setAppShopId(c.getString(ix_app_shop_id));
            coldCall.setReason(c.getString(ix_reason));
            coldCall.setAgentId(c.getInt(ix_agent_id));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }

}
