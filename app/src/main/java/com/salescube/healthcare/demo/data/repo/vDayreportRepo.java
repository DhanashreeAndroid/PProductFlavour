package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.Area;
import com.salescube.healthcare.demo.view.vDayReport;
import com.salescube.healthcare.demo.view.vMonthReport;

import java.util.ArrayList;
import java.util.List;

public class vDayreportRepo {



    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + vDayReport.TABLE + "("
                + vDayReport.COL_SO_ID + " INT ,"
                + vDayReport.COL_SO_MGR_ID + " INT ,"
                + vDayReport.COL_SO_NAME+ " TEXT ,"
                + vDayReport.COL_TARGET_AMOUNT+ " TEXT ,"
                + vDayReport.COL_CUMULATIVE_TARGET_AMOUNT+ " TEXT ,"
                + vDayReport.COL_ACH_AMOUNT+ " TEXT ,"
                + vDayReport.COL_ACH_PER+ " TEXT ,"
                + vDayReport.COL_TC+ " TEXT ,"
                + vDayReport.COL_PC+ " TEXT ,"
                + vDayReport.COL_TOTAL_TC+ " TEXT ,"
                + vDayReport.COL_SO_ROLE + " TEXT "
                + ")";

        return qry;
    }


    public void insert(vDayReport[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        for (vDayReport dayReport : objList) {
            insert(dayReport, db);
        }
    }

    public void insert(vMonthReport[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        for (vMonthReport dayReport : objList) {
            insert(dayReport, db);
        }
    }

    public void insert(vMonthReport obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(vDayReport.COL_SO_ID, obj.getSoId());
        values.put(vDayReport.COL_SO_MGR_ID,obj.getMgrId());
        values.put(vDayReport.COL_SO_NAME, obj.getSoName());
        values.put(vDayReport.COL_ACH_AMOUNT, obj.getCumulativeAchAmount());
        values.put(vDayReport.COL_ACH_PER, obj.getAchPer());
        values.put(vDayReport.COL_TARGET_AMOUNT, obj.getMonthTargetAmount());
        values.put(vDayReport.COL_CUMULATIVE_TARGET_AMOUNT, obj.getCumulativeTargetAmount());
        values.put(vDayReport.COL_TC, obj.getTc());
        values.put(vDayReport.COL_PC, obj.getPc());
        values.put(vDayReport.COL_TOTAL_TC, obj.getTotalTC());
        values.put(vDayReport.COL_SO_ROLE, obj.getSoRole());


        try {
            db.insert(vDayReport.TABLE, null, values);
        } catch (Exception ex) {

        }

    }


    public void insert(vDayReport obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(vDayReport.COL_SO_ID, obj.getSoId());
        values.put(vDayReport.COL_SO_MGR_ID,obj.getMgrId());
        values.put(vDayReport.COL_SO_NAME, obj.getSoName());
        values.put(vDayReport.COL_ACH_AMOUNT, obj.getAchAmount());
        values.put(vDayReport.COL_ACH_PER, obj.getAchPer());
        values.put(vDayReport.COL_TARGET_AMOUNT, obj.getTargetAmount());
        values.put(vDayReport.COL_CUMULATIVE_TARGET_AMOUNT, obj.getCumulative_target_amt());
        values.put(vDayReport.COL_TC, obj.getTc());
        values.put(vDayReport.COL_PC, obj.getPc());
        values.put(vDayReport.COL_TOTAL_TC, obj.getTotalTc());
        values.put(vDayReport.COL_SO_ROLE, obj.getSoRole());


        try {
            db.insert(vDayReport.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    public void deleteAll() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(vDayReport.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }


    public List<vDayReport> getList(String appRole) {

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT *";
        sql += " FROM " + vDayReport.TABLE + "";
        sql += " WHERE so_role='" + appRole +"'";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        List<vDayReport> list =  fillList(c);
        return list;

    }


    public List<vMonthReport> getList1(String appRole) {

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT *";
        sql += " FROM " + vDayReport.TABLE + "";
        sql += " WHERE so_role='" + appRole +"'";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        List<vMonthReport> list =  fillList1(c);
        return list;

    }

    public List<vMonthReport> getListonId1(int  id) {

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT *";
        sql += " FROM " + vDayReport.TABLE + "";
        sql += " WHERE so_mgr_id=" + id +"";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        List<vMonthReport> list =  fillList1(c);
        return list;

    }



    public List<vDayReport> getListonId(int  id) {

        String sql;
        Cursor c;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT *";
        sql += " FROM " + vDayReport.TABLE + "";
        sql += " WHERE so_mgr_id=" + id +"";
        c = db.rawQuery(sql,null);


        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        List<vDayReport> list =  fillList(c);
        return list;

    }


    private List<vDayReport> fillList(Cursor c) {

        List<vDayReport> list = new ArrayList<>();

        int ix_so_id = c.getColumnIndex(vDayReport.COL_SO_ID);
        int ix_so_mgr_id = c.getColumnIndex("so_mgr_id");
        int ix_so_name = c.getColumnIndex("so_name");
        int ix_target_amount = c.getColumnIndex("target_amount");
        int ix_ach_amount = c.getColumnIndex("ach_amount");
        int ix_total_ach_per = c.getColumnIndex("total_ach_per");
        int ix_tc = c.getColumnIndex("tc");
        int ix_pc = c.getColumnIndex("pc");
        int ix_totalTc = c.getColumnIndex("total_tc");
        int ix_soRolee = c.getColumnIndex("so_role");
        int ix_cumulative_target = c.getColumnIndex("cumulative_target_amount");


        vDayReport view;
        while (c.moveToNext()) {

            view = new vDayReport();
            view.setSoId(c.getInt(ix_so_id));
            view.setMgrId(ix_so_mgr_id);
            view.setSoName(c.getString(ix_so_name));

            view.setTargetAmount(c.getDouble(ix_target_amount));
            view.setAchAmount(c.getDouble(ix_ach_amount));
            view.setAchPer(c.getDouble(ix_total_ach_per));
            view.setTc(c.getDouble(ix_tc));
            view.setPc(c.getDouble(ix_pc));
            view.setTotalTc(c.getDouble(ix_totalTc));
            view.setSoRole(c.getString(ix_soRolee));
            view.setCumulative_target_amt(c.getDouble(ix_cumulative_target));


            list.add(view);
        }

        return list;
    }

    private List<vMonthReport> fillList1(Cursor c) {

        List<vMonthReport> list = new ArrayList<>();

        int ix_so_id = c.getColumnIndex(vDayReport.COL_SO_ID);
        int ix_so_mgr_id = c.getColumnIndex("so_mgr_id");
        int ix_so_name = c.getColumnIndex("so_name");
        int ix_target_amount = c.getColumnIndex("target_amount");
        int ix_ach_amount = c.getColumnIndex("ach_amount");
        int ix_total_ach_per = c.getColumnIndex("total_ach_per");
        int ix_tc = c.getColumnIndex("tc");
        int ix_pc = c.getColumnIndex("pc");
        int ix_totalTc = c.getColumnIndex("total_tc");
        int ix_soRolee = c.getColumnIndex("so_role");
        int ix_cumulative_target = c.getColumnIndex("cumulative_target_amount");


        vMonthReport view;
        while (c.moveToNext()) {

            view = new vMonthReport();
            view.setSoId(c.getInt(ix_so_id));
            view.setMgrId(ix_so_mgr_id);
            view.setSoName(c.getString(ix_so_name));

            view.setMonthTargetAmount(c.getDouble(ix_target_amount));
            view.setCumulativeAchAmount(c.getDouble(ix_ach_amount));
            view.setAchPer(c.getDouble(ix_total_ach_per));
            view.setTc(c.getDouble(ix_tc));
            view.setPc(c.getDouble(ix_pc));
            view.setTotalTC(c.getDouble(ix_totalTc));
            view.setSoRole(c.getString(ix_soRolee));
            view.setCumulativeTargetAmount(c.getDouble(ix_cumulative_target));


            list.add(view);
        }

        return list;
    }


    /*public List<vDayReport> getList(){

    }*/

}
