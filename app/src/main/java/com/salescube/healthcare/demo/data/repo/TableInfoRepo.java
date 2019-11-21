package com.salescube.healthcare.demo.data.repo;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.AgentLocality;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductRate;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.view.TableStatistic;
import com.salescube.healthcare.demo.view.vHoReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21/09/2016.
 */

public class TableInfoRepo extends RepoBase {

    private final static String TAG = "TableInfoRepo";

    public boolean isEligibleForOrder() {

        boolean result = true;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String[] tables = new String[]{
                AgentLocality.TABLE,
                Shop.TABLE,
        };

        long rows = 0;
        for (String table : tables) {
            rows = DatabaseUtils.queryNumEntries(db, table);
            if (rows == 0) {
                result = false;
                break;
            }
        }

        return result;
    }

    public int getRecordCount(String _tableName, int _soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT count(*) count";
        sql += " FROM " + _tableName;
        sql += " WHERE so_Id=" + _soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return 0;
        }

        if (c.moveToNext()) {
            return c.getInt(0);
        }

        closeCursor(c);
        return 0;
    }

    public int getRecordCount(String _tableName) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT count(*) count";
        sql += " FROM " + _tableName;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return 0;
        }

        if (c.moveToNext()) {
            return c.getInt(0);
        }

        closeCursor(c);
        return 0;
    }

    public boolean isEligibleForOrder(int soId) {

        boolean result = true;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String[] tables = new String[]{
                Agent.TABLE
        };


        Cursor c;
        String sql;

        long rows = 0;
        for (String table : tables) {

            sql = "";
            sql += " SELECT Count(*) ";
            sql += " FROM " + table + "";
            sql += " WHERE so_id=" + soId + "";

            try {
                c = db.rawQuery(sql, null);
            } catch (SQLException e) {
                Logger.e(e.getMessage());
                return false;
            }


            rows = 0;
            while (c.moveToNext()) {
                rows = c.getInt(0);
                break;
            }

            if (rows == 0) {
                result = false;
                break;
            }

        }

        DatabaseManager.getInstance().closeDatabase();
        return result;
    }

    public boolean isUploadPending(int soId) {
        int count = 0;

        count += new SalesOrderRepo().getOrdersForUpload(soId).size();
        count += new ColdCallRepo().getNoOrdersNotPosted(soId).size();
        count += new ShopRepo().getShopForUpload(soId).size();
        count += new POPShopRepo().getPOPEntriesNotPosted(soId).size();
        count += new LocationLogRepo().getLogsNotPosted().size();
        count += new OtherWorkRepo().getOtherWorkNotPosted(soId).size();
        count += new LeaveRepo().getLeaveNotPosted(soId).size();
        count += new CompetitorInfoRepo().getInfoNotPosted(soId).size();
        count += new SysDateRepo().getDatesForUpload(soId).size();
        count += new ActivityLogRepo().getLogsNotPosted().size();
        count += new MyPlaceRepo().getMyPlacesNotPosted(soId).size();
        count += new ComplaintRepo().getComplaintsForUpload(soId).size();
        count += new SalesReturnRepo().getOrdersForUpload(soId).size();

        return count > 0;
    }

//    public boolean isUploadPendding() {
//
//        boolean result = true;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        String[] tables = new String[]{
//                SalesOrder.TABLE,
//                NoOrder.TABLE,
//                Shop.TABLE,
//                POPShop.TABLE,
//                LocationLog.TABLE,
//                OtherWork.TABLE,
//                Leave.TABLE,
//                CompetitorInfo.TABLE,
//                SysDate.TABLE,
//                ActivityLog.TABLE
//        };
//
//
//        Cursor c;
//        String sql;
//
//        long rows = 0;
//        for (String table : tables) {
//
//            if (table == Shop.TABLE){
//                sql = "";
//                sql += " SELECT Count(*) ";
//                sql += " FROM " + table + "";
//                sql += " WHERE is_posted = '0' ";
//                sql += " AND updatable = '1'";
//
//            }else{
//                sql = "";
//                sql += " SELECT Count(*) ";
//                sql += " FROM " + table + "";
//                sql += " WHERE is_posted = '0' ";
//            }
//
//            try {
//                c = db.rawQuery(sql, null);
//            } catch (SQLException e) {
//                Logger.e(e.getMessage());
//                continue;
//            }
//
//            while (c.moveToNext()) {
//                rows += c.getInt(0);
//            }
//        }
//
//        DatabaseManager.getInstance().closeDatabase();
//
//        return rows > 0;
//    }

    public List<vHoReport> getHOReport() {

        List<vHoReport> report = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String[] tables = new String[]{
                SalesOrder.TABLE,
                NoOrder.TABLE,
                Shop.TABLE,
                POPShop.TABLE,
                LocationLog.TABLE,
                OtherWork.TABLE,
                Leave.TABLE,
                CompetitorInfo.TABLE,
                SysDate.TABLE,
                ActivityLog.TABLE,
                MyPlace.TABLE
        };

        Cursor c;
        String sql;

        int rows = 0;
        for (String table : tables) {

            if (table == Shop.TABLE){
                sql = "";
                sql += " SELECT Count(*) ";
                sql += " FROM " + table + "";
                sql += " WHERE is_posted = '0' ";
                sql += " AND updatable = '1'";

            }else{
                sql = "";
                sql += " SELECT Count(*) ";
                sql += " FROM " + table + "";
                sql += " WHERE is_posted = '0' ";
            }


            try {
                c = db.rawQuery(sql, null);
            } catch (SQLException e) {
                Logger.e(e.getMessage());
                continue;
            }

            if (c.moveToNext()) {
                rows = c.getInt(0);
                if (rows != 0) {
                    report.add(new vHoReport(getTableName(table), String.format("Pending(%s)", rows), rows));
                }else{
                    // report.add(new vHoReport(getTableName(table),"Done",0));
                }
            }
        }

        DatabaseManager.getInstance().closeDatabase();
        return report;
    }

    private String getTableName (String _tableName){

        switch (_tableName) {
            case SalesOrder.TABLE:
                return "Sales Order";
            case NoOrder.TABLE:
                return  "No-Order";
            case Shop.TABLE:
                return "Shop Updates";
            case POPShop.TABLE:
                return  "POP";
            case LocationLog.TABLE:
                return  "Log";
            case OtherWork.TABLE:
                return "Other Work";
            case Leave.TABLE:
                return "Leave Application";
            case CompetitorInfo.TABLE:
                return "Competitor Info";
            case SysDate.TABLE:
                return "Attendance";
            case ActivityLog.TABLE:
                return "Activity Log";
            case MyPlace.TABLE:
                return "My Place";
            default:
                return _tableName;
        }

    }

    // only called on dayOpen procedure
    // all SO posted data must be deleted

    public void deletePostedData() {

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String[] tables = new String[]{
                SalesOrder.TABLE,
                NoOrder.TABLE,
                POPShop.TABLE,
                LocationLog.TABLE,
                OtherWork.TABLE,
                Leave.TABLE,
                CompetitorInfo.TABLE,
                ActivityLog.TABLE,
                MyPlace.TABLE
        };

        for (String table : tables) {

            sql = "";
            sql += " DELETE ";
            sql += " FROM " + table + "";
            sql += " WHERE is_posted = '1' ";

            try {
                db.execSQL(sql);
            } catch (SQLException e) {
                Logger.e(e.getMessage());
            }
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean hasDayOpen(int soId) {

        Cursor c;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String sql;
        long rows = 0;

        sql = "";
        sql += " SELECT Count(*) ";
        sql += " FROM " + SysDate.TABLE + "";
        sql += " WHERE so_id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (SQLException e) {
            Logger.e(e.getMessage());
            return false;
        }


        rows = 0;
        while (c.moveToNext()) {
            rows = c.getInt(0);
            break;
        }

        closeCursor(c);
        return rows > 0;
    }

    public boolean hasDataExist(int soId) {

        String sql;
        Cursor c;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String[] tables = new String[]{
                AgentLocality.TABLE,
                Shop.TABLE,
                Product.TABLE,
                ProductRate.TABLE
        };

        long rows = 0;
        for (String table : tables) {


            sql = "";
            sql += " SELECT Count(*) ";
            sql += " FROM " + table + "";
            sql += " WHERE so_id=" + soId + "";

            try {
                c = db.rawQuery(sql, null);
            } catch (SQLException e) {
                Logger.e(e.getMessage());
                return false;
            }

            rows = 0;
            while (c.moveToNext()) {
                rows = c.getInt(0);
                break;
            }

            if (rows > 0) {
                break;
            }
        }

        return rows > 0;
    }

    public List<TableStatistic> getTables() {

        List<TableStatistic> objTables = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT name ";
        sql += " FROM sqlite_master ";
        sql += " WHERE type='table' ";
        sql += " AND name !='android_metadata'";
        sql += " ORDER BY name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return objTables;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int col_ix_name = c.getColumnIndexOrThrow("name");

        TableStatistic objTable;
        String tableName = "";
        long rows = 0;

        while (c.moveToNext()) {

            tableName = c.getString(col_ix_name);
            rows = DatabaseUtils.queryNumEntries(db, tableName);

            objTable = new TableStatistic();
            objTable.setTableName(tableName);
            objTable.setRecords(rows);
            objTables.add(objTable);
        }

        return objTables;
    }
}
