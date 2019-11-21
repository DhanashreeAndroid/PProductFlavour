package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Target;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vTarget;

import java.util.Date;

/**
 * Created by user on 10/10/2016.
 */

public class TargetRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Target.TABLE + "("
                + Target.COL_ID + " PRIMARY KEY ,"
                + Target.COL_SO_ID + " INT ,"
                + Target.COL_ORDER_DATE + " DATE ,"
                + Target.COL_MONTH_TARGET_VALUE + " DOUBLE ,"
                + Target.COL_MONTH_ACHIEVEMENT_VALUE + " DOUBLE ,"
                + Target.COL_DAY_TARGET_VALUE+ " DOUBLE ,"
                + Target.COL_DAY_ACHIEVEMENT_VALUE + " DOUBLE ,"
                + Target.COL_FOCUS_PRODUCT + " TEXT "
                + ")";

        return qry;
    }


    private void insert(Target target, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Target.COL_SO_ID, target.getSoId());
        values.put(Target.COL_ORDER_DATE, DateFunc.getDateStr(target.getOrderDate()));
        values.put(Target.COL_MONTH_TARGET_VALUE, target.getMonthTargetValue());
        values.put(Target.COL_MONTH_ACHIEVEMENT_VALUE, target.getMonthAchievementValue());
        values.put(Target.COL_DAY_TARGET_VALUE, target.getDayTargetValue());
        values.put(Target.COL_DAY_ACHIEVEMENT_VALUE, target.getDayAchievementValue());
        values.put(Target.COL_FOCUS_PRODUCT, target.getFocusProducts());

        try {
            db.insert(Target.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(Target[] products, SQLiteDatabase db) {

        for (Target productRate : products) {
            insert(productRate, db);
        }
    }

    public void  deleteAll(int soId){

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + Target.TABLE + " WHERE so_id=" + soId + "") ;
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Target[] products) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Target products) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(products, db);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void copyYesterdayAch(int soId, Date fromDate, Date toDate){

        Target target = new Target();
        vTarget lastTar = getTodaysTarget(soId,fromDate);

        double monthTargetAmt = 0;
        double monthAchAmt = 0;

        if (lastTar != null){
            monthTargetAmt = lastTar.getMonthTargetValue();
            monthAchAmt = lastTar.getMonthAchievementValue();
        }

        target.setSoId(soId);
        target.setOrderDate(toDate);
        target.setMonthTargetValue(monthTargetAmt);
        target.setMonthAchievementValue(monthAchAmt);
        target.setDayTargetValue(0);
        target.setDayAchievementValue(0);
        target.setFocusProducts("");

        insert(target);
    }

    public vTarget getTodaysTarget(int soId, Date targetDate) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Target.TABLE + "";
        sql += " WHERE  so_id =" + soId + "";
        sql += " AND    order_date ='" + DateFunc.getDateStr(targetDate) + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillObject(c);
    }


    private vTarget fillObject(Cursor c) {

        int ix_so_id = c.getColumnIndexOrThrow(Target.COL_SO_ID);
        int ix_order_date = c.getColumnIndexOrThrow(Target.COL_ORDER_DATE);
        int ix_month_target_value = c.getColumnIndexOrThrow(Target.COL_MONTH_TARGET_VALUE);
        int ix_month_achievment_value = c.getColumnIndexOrThrow(Target.COL_MONTH_ACHIEVEMENT_VALUE);
        int ix_day_target_value = c.getColumnIndexOrThrow(Target.COL_DAY_TARGET_VALUE);
        int ix_day_achievment_value = c.getColumnIndexOrThrow(Target.COL_DAY_ACHIEVEMENT_VALUE);
        int ix_focus_product = c.getColumnIndexOrThrow(Target.COL_FOCUS_PRODUCT);

        vTarget target = new vTarget();

        while (c.moveToNext()) {

            double todaySales = 0;

            target.setSoId(c.getInt(ix_so_id));
            target.setOrderDate(DateFunc.getDate(c.getString(ix_order_date)));

            try {
                todaySales = new SalesOrderRepo().getTodaySalesValue(target.getSoId(),target.getOrderDate());
            } catch (Exception e) {
            }

            target.setMonthTargetValue(c.getDouble(ix_month_target_value));
            target.setMonthAchievementValue(c.getDouble(ix_month_achievment_value) + todaySales);

            target.setDayTargetValue(c.getDouble(ix_day_target_value));
            target.setDayAchievementValue(c.getDouble(ix_day_achievment_value) + todaySales);

            target.setFocusProducts(c.getString(ix_focus_product));

            double monthBalanceTargetValue = target.getMonthAchievementValue()  - target.getMonthTargetValue() ;
            double dayBalanceTargetValue = target.getDayAchievementValue() - target.getDayTargetValue();

            target.setMonthBalanceTarget(monthBalanceTargetValue);
            target.setDayBalanceTarget(dayBalanceTargetValue);
        }

        return target;
    }
}
