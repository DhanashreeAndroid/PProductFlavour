package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Area;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.data.model.Route;
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vRoute;

import java.util.ArrayList;
import java.util.List;

public class RouteRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + Route.TABLE + "("
                + Route.COL_ROUTE_ID + " INT ,"
                + Route.COL_ROUTE_NAME+ " TEXT ,"
                + Route.COL_AREA_ID + " INT, "
                + Route.COL_SO_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(Route obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Route.COL_ROUTE_ID, obj.getRouteId());
        values.put(Route.COL_ROUTE_NAME, obj.getRouteName());
        values.put(Route.COL_AREA_ID, obj.getAreaId());
        values.put(Route.COL_SO_ID, obj.getSoId());

        try {
            db.insert(Route.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    private void insert(Route[] objList, SQLiteDatabase db) {
        for (Route productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(Route objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Route[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + Route.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vRoute> getAllRoutes(int soId) {

        List<vRoute> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT route_id, route_name, area_id";
        sql += " FROM " + Route.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY route_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vAgents;
        }

        if (c.getCount() == 0) {
            return vAgents;
        }

        int ix_id = c.getColumnIndexOrThrow("route_id");
        int ix_route_name = c.getColumnIndexOrThrow("route_name");
        int ix_area_id = c.getColumnIndexOrThrow("area_id");

        vRoute vAgent;

        while (c.moveToNext()) {

            vAgent = new vRoute();
            vAgent.setRouteId(c.getInt(ix_id));
            vAgent.setRouteName(c.getString(ix_route_name));
            vAgent.setAreaId(c.getInt(ix_area_id));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public List<vBaseSpinner> getAllRoutes1(int soId) {

        List<vBaseSpinner> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT route_id id, route_name name";
        sql += " FROM " + Route.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY route_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vAgents;
        }

        if (c.getCount() == 0) {
            return vAgents;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_route_name = c.getColumnIndexOrThrow("name");

        vBaseSpinner vAgent;

        while (c.moveToNext()) {

            vAgent = new vBaseSpinner();
            vAgent.setId(c.getInt(ix_id));
            vAgent.setName(c.getString(ix_route_name));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public List<vBaseSpinner> getAllRoutesByArea(int soId, int area_id) {

        List<vBaseSpinner> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT route_id id, route_name name";
        sql += " FROM " + Route.TABLE;
        sql += " inner join " + Area.TABLE;
        sql += " on " + Area.TABLE + "." + Area.COL_AREA_ID + "=" + Route.TABLE + "." + AreaAgent.COL_AREA_ID;
        sql += " and ms_area.so_id = ms_route.so_id " ;
        sql += " WHERE " + Area.TABLE + "." + " so_Id=" + soId + "";
        sql += " and " + Area.TABLE + "." + Area.COL_AREA_ID + "=" + area_id + "";
        sql += " ORDER BY  " + Route.TABLE + "." + " route_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vAgents;
        }

        if (c.getCount() == 0) {
            return vAgents;
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_route_name = c.getColumnIndexOrThrow("name");

        vBaseSpinner vAgent;

        while (c.moveToNext()) {

            vAgent = new vBaseSpinner();
            vAgent.setId(c.getInt(ix_id));
            vAgent.setName(c.getString(ix_route_name));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public String getSelected(int id) {

        String name = "";

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT  route_name";
        sql += " FROM " + Route.TABLE;
        sql += " WHERE route_id=" + id + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return "";
        }

        if (c.getCount() == 0) {
            return "";
        }

        int ix_route_name = c.getColumnIndexOrThrow("route_name");

        vRoute vAgent;

        while (c.moveToNext()) {

            name = c.getString(ix_route_name);
        }

        c.close();
        return name;
    }


}
