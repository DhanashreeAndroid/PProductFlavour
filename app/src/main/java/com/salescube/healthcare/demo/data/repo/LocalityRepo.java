package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Locality;
import com.salescube.healthcare.demo.view.vLocality;

import java.util.ArrayList;
import java.util.List;

public class LocalityRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + Locality.TABLE + "("
                + Locality.COL_LOCALITY_ID + " INT ,"
                + Locality.COL_LOCALITY_NAME+ " TEXT ,"
                + Locality.COL_ROUTE_ID + " INT, "
                + Locality.COL_SO_ID + " INT "
                + ")";

        return qry;
    }

    private void insert(Locality obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Locality.COL_LOCALITY_ID, obj.getLocalityId());
        values.put(Locality.COL_LOCALITY_NAME, obj.getLocalityName());
        values.put(Locality.COL_ROUTE_ID, obj.getRouteId());
        values.put(Locality.COL_SO_ID, obj.getSoId());

        try {
            db.insert(Locality.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    private void insert(Locality[] objList, SQLiteDatabase db) {
        for (Locality productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(Locality objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Locality[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + Locality.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vLocality> getAllLocalities(int soId) {

        List<vLocality> localityList = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT locality_id, locality_name, route_id";
        sql += " FROM " + Locality.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY locality_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return localityList;
        }

        if (c.getCount() == 0) {
            return localityList;
        }

        int ix_id = c.getColumnIndexOrThrow("locality_id");
        int ix_locality_name = c.getColumnIndexOrThrow("locality_name");
        int ix_route_id = c.getColumnIndexOrThrow("route_id");

        vLocality locality;

        while (c.moveToNext()) {

            locality = new vLocality();
            locality.setLocalityId(c.getInt(ix_id));
            locality.setLocalityName(c.getString(ix_locality_name));
            locality.setRouteId(c.getInt(ix_route_id));
            localityList.add(locality);
        }

        c.close();
        return localityList;
    }
}
