package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + NotificationModel.TABLE + "("
                + NotificationModel.COL_ID + " INT ,"
                + NotificationModel.COL_TITLE + " TEXT, "
                + NotificationModel.COL_DESCRIPTION + " TEXT, "
                + NotificationModel.COL_READ_STATUS + " INT ,"
                + NotificationModel.COL_EMP_ID + " INT ,"
                + NotificationModel.COL_DATE + " DATETIME "
                + ")";

         return qry;
    }


    public  void insert(NotificationModel obj ) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();

        values.put(NotificationModel.COL_ID, obj.getId());
        values.put(NotificationModel.COL_TITLE, obj.getTitle());
        values.put(NotificationModel.COL_DESCRIPTION, obj.getDescription());
        values.put(NotificationModel.COL_READ_STATUS, obj.getReadStatus());
        values.put(NotificationModel.COL_DATE, obj.getDate());
        values.put(NotificationModel.COL_EMP_ID, obj.getSoId());

        db.insert(NotificationModel.TABLE, null, values);
    }


    public List<NotificationModel> getNotificationList() {
        List<NotificationModel> list = new ArrayList<>();

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + NotificationModel.TABLE + "";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return list;
        }


        int ix_id = c.getColumnIndex(NotificationModel.COL_ID);
        int ix_title = c.getColumnIndex(NotificationModel.COL_TITLE);
        int ix_date = c.getColumnIndex(NotificationModel.COL_DATE);
        int ix_desc = c.getColumnIndex(NotificationModel.COL_DESCRIPTION);
        int ix_read_status = c.getColumnIndex(NotificationModel.COL_READ_STATUS);




        NotificationModel view;
        while (c.moveToNext()) {

            view = new NotificationModel();
            view.setId(c.getInt(ix_id));
            view.setTitle(c.getString(ix_title));
            view.setDescription(c.getString(ix_desc));
            view.setReadStatus(c.getInt(ix_read_status));
            view.setDate(c.getString(ix_date));
            list.add(view);
        }

        return list;
    }

    public void insert(NotificationModel[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        for (NotificationModel org : objList) {
            insert(org);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public int getMaxId() {

        int maxid= 0;

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT Max(id) as max";
        sql += " FROM " + NotificationModel.TABLE + "";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return maxid;
        }


        int ix_id = c.getColumnIndex("max");

        while (c.moveToNext()) {

           maxid = c.getInt(ix_id);
        }

        return maxid;
    }



}
