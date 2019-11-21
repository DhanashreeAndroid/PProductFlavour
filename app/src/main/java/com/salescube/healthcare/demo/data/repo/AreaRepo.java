package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Area;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.view.vArea;
import com.salescube.healthcare.demo.view.vBaseSpinner;

import java.util.ArrayList;
import java.util.List;

public class AreaRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + Area.TABLE + "("
                + Area.COL_AREA_ID + " INT ,"
                + Area.COL_AREA_NAME+ " TEXT ,"
                + Area.COL_ZONE_ID + " INT, "
                + Area.COL_SO_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(Area obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Area.COL_AREA_ID, obj.getAreaId());
        values.put(Area.COL_AREA_NAME, obj.getAreaName());
        values.put(Area.COL_ZONE_ID, obj.getZoneId());
        values.put(Area.COL_SO_ID, obj.getSoId());

        try {
            db.insert(Area.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    private void insert(Area[] objList, SQLiteDatabase db) {
        for (Area productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(Area objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Area[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + Area.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vArea> getAreaAll(int soId) {

        List<vArea> areaList = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT area_id, area_name ";
        sql += " FROM " + Area.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY area_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return areaList;
        }

        if (c.getCount() == 0) {
            return areaList;
        }

        int ix_id = c.getColumnIndexOrThrow("area_id");
        int ix_agentName = c.getColumnIndexOrThrow("area_name");

        vArea area;

        while (c.moveToNext()) {

            area = new vArea();
            area.setAreaId(c.getInt(ix_id));
            area.setAreaName(c.getString(ix_agentName));
            areaList.add(area);
        }

        c.close();
        return areaList;
    }

    public List<vBaseSpinner> getAreaAll1(int soId) {

        List<vBaseSpinner> areaList = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT area_id id, area_name name ";
        sql += " FROM " + Area.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY area_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return areaList;
        }

        if (c.getCount() == 0) {
            return areaList;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_agentName = c.getColumnIndexOrThrow("name");

        vBaseSpinner area;

        while (c.moveToNext()) {

            area = new vBaseSpinner();
            area.setId(c.getInt(ix_id));
            area.setName(c.getString(ix_agentName));
            areaList.add(area);
        }

        c.close();
        return areaList;
    }


    public List<vBaseSpinner> getAreaByAgent(int soId, int agentId) {

        List<vBaseSpinner> areaList = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;
        sql += " SELECT ms_area.area_id id, area_name name ";
        sql += " FROM " + Area.TABLE;
        sql += " inner join " + AreaAgent.TABLE;
        sql += " on " + Area.TABLE + "." + Area.COL_AREA_ID + "=" + AreaAgent.TABLE + "." + AreaAgent.COL_AREA_ID;
        sql += "  and ms_area.so_id = rl_area_agent.so_id  ";
        sql += " WHERE ms_area.so_Id=" + soId + "";
        sql += " and " + AreaAgent.TABLE + "." + AreaAgent.COL_AGENT_ID + "=" + agentId + "";
        sql += " ORDER BY area_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return areaList;
        }

        if (c.getCount() == 0) {
            return areaList;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_agentName = c.getColumnIndexOrThrow("name");

        vBaseSpinner area;

        while (c.moveToNext()) {

            area = new vBaseSpinner();
            area.setId(c.getInt(ix_id));
            area.setName(c.getString(ix_agentName));
            areaList.add(area);
        }

        c.close();
        return areaList;
    }

    public String getSelected(int id) {

        String name = "";
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT area_name ";
        sql += " FROM " + Area.TABLE;
        sql += " WHERE area_id=" + id + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return "";
        }

        if (c.getCount() == 0) {
            return "";
        }

        int ix_agentName = c.getColumnIndexOrThrow("area_name");

        vArea area;

        while (c.moveToNext()) {

            name = c.getString(ix_agentName);
        }

        c.close();
        return name;
    }


}
