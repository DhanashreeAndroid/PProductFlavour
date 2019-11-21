package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13/06/2017.
 */

public class MyPlaceRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + MyPlace.TABLE + "("
                + MyPlace.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + MyPlace.COL_USER_ID + " INT ,"
                + MyPlace.COL_PLACE_TYPE + " TEXT ,"
                + MyPlace.COL_PLACE_ID + " INT ,"
                + MyPlace.COL_LAT + " TEXT ,"
                + MyPlace.COL_LNG + " TEXT ,"
                + MyPlace.COL_ADD + " TEXT ,"
                + MyPlace.COL_CREATED_DATE_TIME + " DATETIME, "
                + MyPlace.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(MyPlace obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(MyPlace.COL_USER_ID, obj.getUserId());
        values.put(MyPlace.COL_PLACE_TYPE, obj.getPlaceType());
        values.put(MyPlace.COL_PLACE_ID, obj.getPlaceId());
        values.put(MyPlace.COL_LAT, obj.getLat());
        values.put(MyPlace.COL_LNG, obj.getLng());
        values.put(MyPlace.COL_ADD, obj.getAdd());
        values.put(MyPlace.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(MyPlace.COL_IS_POSTED, false);

        try {
            db.insert(MyPlace.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    private void insert(MyPlace[] objList, SQLiteDatabase db) {

        for (MyPlace productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + MyPlace.TABLE + " WHERE user_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(MyPlace objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(MyPlace[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public  void markPosted(int soId){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + MyPlace.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE user_id=" + soId + "";
        sql += " AND is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public  void markPosted(){

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + MyPlace.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<MyPlace> getMyPlace(int soId, boolean isPosted) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int is_posted = isPosted ? 1: 0;

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + MyPlace.TABLE + "";
        sql += " WHERE is_posted='"+ is_posted +"'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return  fillOrders(c);
    }

    public List<MyPlace> getMyPlacesNotPosted(int userId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + MyPlace.TABLE + "";
        sql += " WHERE  is_posted='0'";
        sql += " AND    user_id=" + userId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return  fillOrders(c);
    }


    private List<MyPlace> fillOrders(Cursor c) {

        int ix_id = c.getColumnIndexOrThrow(MyPlace.COL_ID);
        int ix_user_id = c.getColumnIndexOrThrow(MyPlace.COL_USER_ID);
        int ix_place_type = c.getColumnIndexOrThrow(MyPlace.COL_PLACE_TYPE);
        int ix_place_id = c.getColumnIndexOrThrow(MyPlace.COL_PLACE_ID);
        int ix_lat = c.getColumnIndexOrThrow(MyPlace.COL_LAT);
        int ix_lng = c.getColumnIndexOrThrow(MyPlace.COL_LNG);
        int ix_add = c.getColumnIndexOrThrow(MyPlace.COL_ADD);
        int ix_created_date_time = c.getColumnIndexOrThrow(MyPlace.COL_CREATED_DATE_TIME);
        int ix_is_posted = c.getColumnIndexOrThrow(MyPlace.COL_IS_POSTED);

        MyPlace coldCall;
        List<MyPlace> coldCallList = new ArrayList<>();

        while (c.moveToNext()) {

            coldCall = new MyPlace();
            coldCall.setId(c.getInt(ix_id));
            coldCall.setUserId(c.getInt(ix_user_id));
            coldCall.setPlaceType(c.getString(ix_place_type));
            coldCall.setPlaceId(c.getInt(ix_place_id));
            coldCall.setLat(c.getString(ix_lat));
            coldCall.setLng(c.getString(ix_lng));
            coldCall.setAdd(c.getString(ix_add));
            coldCall.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            coldCall.setPosted(c.getInt(ix_is_posted) == 1);
            coldCallList.add(coldCall);
        }

        return coldCallList;
    }
}
