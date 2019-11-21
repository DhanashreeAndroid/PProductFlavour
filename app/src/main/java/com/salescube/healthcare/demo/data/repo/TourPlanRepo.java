package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Holiday;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vTourPlan;
import com.salescube.healthcare.demo.view.vTourPlanDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourPlanRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + TourPlan.TABLE + "("
                + TourPlan.COL_ID + " TEXT ,"
                + TourPlan.COL_SO_ID + " TEXT ,"
                + TourPlan.COL_TOUR_DATE + " TEXT , "
                + TourPlan.COL_SET + " TEXT , "
                + TourPlan.COL_IS_SYNC + " INT , "
                + TourPlan.COL_CREATED_BY + " INT , "
                + TourPlan.COL_CREATED_ON + " TEXT , "
                + TourPlan.COL_UPDATED_BY + " INT , "
                + TourPlan.COL_UPDATED_ON + " TEXT , "
                + TourPlan.COL_IS_CANCELLED + " INT DEFAULT 0  "
                + ")";

        return qry;
    }

    private void insert(TourPlan obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(TourPlan.COL_ID, obj.getId());
        values.put(TourPlan.COL_SO_ID, obj.getSoId());
        values.put(TourPlan.COL_TOUR_DATE, obj.getTourDate());
        values.put(TourPlan.COL_SET, obj.getSetName());
        values.put(TourPlan.COL_IS_SYNC, obj.getIsSync());
        values.put(TourPlan.COL_CREATED_BY, obj.getCreatedBy());
        values.put(TourPlan.COL_CREATED_ON, obj.getCreatedOn());
        try {
            db.insert(TourPlan.TABLE, null, values);
        } catch (Exception ex) {
        }

    }

    private void update(TourPlan obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(TourPlan.COL_ID, obj.getId());
        values.put(TourPlan.COL_SO_ID, obj.getSoId());
        values.put(TourPlan.COL_TOUR_DATE, obj.getTourDate());
        values.put(TourPlan.COL_SET, obj.getSetName());
        values.put(TourPlan.COL_IS_SYNC, obj.getIsSync());
        values.put(TourPlan.COL_UPDATED_BY, obj.getUpdatedBy());
        values.put(TourPlan.COL_UPDATED_ON, obj.getUpdatedOn());

        try {
            db.update(TourPlan.TABLE, values, TourPlan.COL_ID + " = '" + obj.getId() + "'", null);
        } catch (Exception ex) {
        }
    }

    public void update(TourPlan obj) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        update(obj, db);
        DatabaseManager.getInstance().closeDatabase();
    }


    private void insert(List<TourPlan> objList, SQLiteDatabase db) {

        for (TourPlan productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(TourPlan objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(List<TourPlan> objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insertUpdate(TourPlan objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (isExistTourPlan(objList.getId())) {
            update(objList, db);
            new TourPlanDetailRepo().deletePlan(objList.getId());
        } else {
            insert(objList, db);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + TourPlan.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteByDate(int soId, String tourDate) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sql = "DELETE FROM " + TourPlan.TABLE + " WHERE so_id=" + soId + " AND tour_date = '" + tourDate + "'";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("TourPlan", e.getMessage());
        }

        DatabaseManager.getInstance().closeDatabase();

    }

    public void deletePlan(int soId, String tourPlanId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(TourPlan.COL_IS_CANCELLED, 1);
        values.put(TourPlan.COL_IS_SYNC, 0);
        try {
            db.update(TourPlan.TABLE, values, TourPlan.COL_ID + " = '" + tourPlanId + "' AND " +
                    TourPlan.COL_SO_ID + " = '" + soId + "'", null);
        } catch (Exception ex) {
        }

        // db.execSQL("DELETE FROM " + TourPlan.TABLE + " WHERE so_id=" + soId + " And id = '" +tourPlanId+ "'" );
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vTourPlan> getTourPlan(int soId, String date, boolean isAddDefault, ArrayList<Holiday> holidayList) {

        List<vTourPlan> vTours = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        //check details available for selected date
        sql += " SELECT DISTINCT id,tour_date, set_name, so_id ";
        sql += " FROM " + TourPlan.TABLE;
        sql += " inner join txn_tour_plan_detail on ";
        sql += " txn_tour_plan.id = txn_tour_plan_detail.tour_plan_id";
        sql += " WHERE so_Id=" + soId + "";
        sql += " AND tour_date='" + date + "'";
        sql += " AND is_cancelled = 0";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return getDefaultPlan(soId, date, isAddDefault, holidayList);
        }

        if (c != null) {
            if (c.getCount() == 0) {
                //if not then default tourplan is added
                return getDefaultPlan(soId, date, isAddDefault, holidayList);
            } else {
                //if tourplan available in details , then shows only those tourplan
                int ix_id = c.getColumnIndexOrThrow("id");
                int ix_status = c.getColumnIndexOrThrow("set_name");
                int ix_so_id = c.getColumnIndexOrThrow("so_id");
                int ix_date = c.getColumnIndexOrThrow("tour_date");

                vTourPlan vTourPlan;

                while (c.moveToNext()) {
                    vTourPlan = new vTourPlan();
                    vTourPlan.setId(c.getString(ix_id));
                    vTourPlan.setSetName(c.getString(ix_status));
                    vTourPlan.setSoId(c.getInt(ix_so_id));
                    vTourPlan.setTourDate(c.getString(ix_date));
                    vTours.add(vTourPlan);
                }
            }
        }
        c.close();
        return vTours;
    }

    public List<vTourPlan> insertDefaultPlan(int soId, String date, ArrayList<Holiday> holidayList) {
        List<vTourPlan> vTours = new ArrayList<>();
        boolean isHoliday = false;
        int position = 0;
        if (holidayList.size() > 0) {
            for (int i = 0; i < holidayList.size(); i++) {
                if (holidayList.get(i).getHoliday().equalsIgnoreCase(date)) {
                    isHoliday = true;
                    position = i;
                    break;
                }
            }
        }

        if (isHoliday) {
            //return list
            vTourPlan tourPlan = new vTourPlan();
            tourPlan.setId(UtilityFunc.getTourPlanId(soId));
            tourPlan.setSetName(Constant.TourPlanSets.HOLIDAY);
            tourPlan.setSoId(soId);
            tourPlan.setTourDate(date);
            tourPlan.setIsSync(0);
            tourPlan.setCreatedOn(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
            tourPlan.setCreatedBy(soId);

            ArrayList<vTourPlanDetail> detail = new ArrayList<>();
            vTourPlanDetail det = new vTourPlanDetail();
            det.setTourPlanId(tourPlan.getId());
            det.setTitle("Remark");
            det.setValue(holidayList.get(position).getRemark());
            detail.add(det);
            tourPlan.setDetail(detail);

            vTours.add(tourPlan);

        } else {
            TourPlan tour = new TourPlan();
            tour.setId(UtilityFunc.getTourPlanId(soId));
            tour.setSetName(Constant.TourPlanSets.WORKING);
            tour.setSoId(soId);
            tour.setTourDate(date);
            tour.setIsSync(0);
            tour.setCreatedOn(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
            tour.setCreatedBy(soId);
            insert(tour);

            //return list
            vTourPlan tourPlan = new vTourPlan();
            tourPlan.setId(tour.getId());
            tourPlan.setSetName(tour.getSetName());
            tourPlan.setSoId(tour.getSoId());
            tourPlan.setTourDate(tour.getTourDate());
            tourPlan.setIsSync(tour.getIsSync());
            tourPlan.setCreatedOn(tour.getCreatedOn());
            tourPlan.setCreatedBy(tour.getCreatedBy());
            vTours.add(tourPlan);
        }

        return vTours;
    }

    public List<vTourPlan> getDefaultPlan(int soId, String date, boolean isAddDefault, ArrayList<Holiday> holidayList) {

        List<vTourPlan> vTours = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT id,tour_date, set_name, so_id ";
        sql += " FROM " + TourPlan.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " AND tour_date='" + date + "'";
        sql += " AND is_cancelled = 0";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            if (isAddDefault) {
                return insertDefaultPlan(soId, date, holidayList);
            }
        }

        if (c != null) {
            if (c.getCount() == 0) {
                if (isAddDefault) {
                    return insertDefaultPlan(soId, date, holidayList);
                }
            }
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_status = c.getColumnIndexOrThrow("set_name");
        int ix_so_id = c.getColumnIndexOrThrow("so_id");
        int ix_date = c.getColumnIndexOrThrow("tour_date");

        vTourPlan vTourPlan;

        while (c.moveToNext()) {
            vTourPlan = new vTourPlan();
            vTourPlan.setId(c.getString(ix_id));
            vTourPlan.setSetName(c.getString(ix_status));
            vTourPlan.setSoId(c.getInt(ix_so_id));
            vTourPlan.setTourDate(c.getString(ix_date));
            vTours.add(vTourPlan);
        }
        c.close();
        return vTours;
    }


    public boolean isExistTourPlan(String tourPlanId) {
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT id ";
        sql += " FROM " + TourPlan.TABLE;
        sql += " WHERE id='" + tourPlanId + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
        }
        if (c == null) {
            return false;
        } else {
            if (c.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isExistTouPlanDetail(String id) {
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT t.id ";
        sql += " FROM " + TourPlan.TABLE + " AS t";
        sql += " INNER JOIN " + TourPlanDetail.TABLE + " AS d";
        sql += " ON t.id = d.tour_plan_id ";
        sql += " WHERE t.id='" + id + "'";
        sql += " AND is_sync = 1";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
        }
        if (c == null) {
            return false;
        } else {
            if (c.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public List<TourPlan> getTourPlanForSync(int soId) {

        List<TourPlan> list = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        sql += " SELECT  DISTINCT id,tour_date, set_name, so_id , created_by, created_on, updated_by, updated_on, is_cancelled";
        sql += " FROM " + TourPlan.TABLE;
        sql += " inner join txn_tour_plan_detail on ";
        sql += " txn_tour_plan.id = txn_tour_plan_detail.tour_plan_id";
        sql += " WHERE so_Id=" + soId + "";
        sql += " And is_sync = 0";
        sql += " order by tour_date ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return list;
        }

        if (c != null) {
            if (c.getCount() == 0) {
                return list;
            }
        }

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_status = c.getColumnIndexOrThrow("set_name");
        int ix_so_id = c.getColumnIndexOrThrow("so_id");
        int ix_date = c.getColumnIndexOrThrow("tour_date");
        int ix_created_by = c.getColumnIndexOrThrow("created_by");
        int ix_created_on = c.getColumnIndexOrThrow("created_on");
        int ix_updated_by = c.getColumnIndexOrThrow("updated_by");
        int ix_updated_on = c.getColumnIndexOrThrow("updated_on");
        int ix_is_cancelled = c.getColumnIndexOrThrow("is_cancelled");

        TourPlan tourPlan;

        while (c.moveToNext()) {
            tourPlan = new TourPlan();
            tourPlan.setId(c.getString(ix_id));
            tourPlan.setSetName(c.getString(ix_status));
            tourPlan.setSoId(c.getInt(ix_so_id));
            tourPlan.setTourDate(c.getString(ix_date));
            tourPlan.setCreatedBy(c.getInt(ix_created_by));
            tourPlan.setCreatedOn(c.getString(ix_created_on));
            tourPlan.setUpdatedBy(c.getInt(ix_updated_by));
            tourPlan.setUpdatedOn(c.getString(ix_updated_on));
            tourPlan.setCancelled(c.getInt(ix_is_cancelled));
            tourPlan.setDetail(new TourPlanDetailRepo().getTourPlanDetail(tourPlan.getId()));
            list.add(tourPlan);
        }
        c.close();
        return list;
    }

    public List<vTourPlan> getOverAllTourPlan(int soId, String date, boolean isAddDefault, ArrayList<Holiday> holidayList) {

        List<vTourPlan> vTours = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = null;

        //check details available for selected date
        sql += " SELECT DISTINCT id,tour_date, set_name, so_id ";
        sql += " FROM " + TourPlan.TABLE;
        sql += " Left join txn_tour_plan_detail on ";
        sql += " txn_tour_plan.id = txn_tour_plan_detail.tour_plan_id";
        sql += " WHERE so_Id=" + soId + "";
        sql += " AND tour_date='" + date + "'";
        sql += " AND is_cancelled = 0";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return getDefaultPlan(soId, date, isAddDefault, holidayList);
        }

        if (c != null) {
            if (c.getCount() == 0) {
                //if not then default tourplan is added
                return getDefaultPlan(soId, date, isAddDefault, holidayList);
            } else {
                //if tourplan available in details , then shows only those tourplan
                int ix_id = c.getColumnIndexOrThrow("id");
                int ix_status = c.getColumnIndexOrThrow("set_name");
                int ix_so_id = c.getColumnIndexOrThrow("so_id");
                int ix_date = c.getColumnIndexOrThrow("tour_date");

                vTourPlan vTourPlan;

                while (c.moveToNext()) {
                    vTourPlan = new vTourPlan();
                    vTourPlan.setId(c.getString(ix_id));
                    vTourPlan.setSetName(c.getString(ix_status));
                    vTourPlan.setSoId(c.getInt(ix_so_id));
                    vTourPlan.setTourDate(c.getString(ix_date));
                    vTours.add(vTourPlan);
                }
            }
        }
        c.close();
        return vTours;
    }


}