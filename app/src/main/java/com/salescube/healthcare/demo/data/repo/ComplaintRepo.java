package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Complaint;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 25/01/2018.
 */

public class ComplaintRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Complaint.TABLE + "("
                + Complaint.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Complaint.COL_SO_ID + " INT ,"
                + Complaint.COL_REPORTED_BY + " TEXT ,"

                + Complaint.COL_AGENT_SS_ID + " INT ,"
                + Complaint.COL_AREA_ID + " INT ,"
                + Complaint.COL_ROUTE_ID + " INT ,"
                + Complaint.COL_LOCALITY_ID + " INT ,"
                + Complaint.COL_SHOP_ID + " INT ,"

                + Complaint.COL_CUSTOMER_NAME + " TEXT ,"
                + Complaint.COL_CONTACT_NO + " TEXT ,"
                + Complaint.COL_PLACE + " TEXT ,"

                + Complaint.COL_COMPLAINT_ABOUT + " TEXT ,"
                + Complaint.COL_PRODUCT + " TEXT ,"
                + Complaint.COL_PRODUCT_SKU + " TEXT ,"
                + Complaint.COL_CRITERIA + " TEXT ,"
                + Complaint.COL_COMPLAINT + " TEXT ,"
                + Complaint.COL_COMPLAINT_OTHER + " TEXT ,"

                + Complaint.COL_BATCH_NO + " TEXT ,"
                + Complaint.COL_QTY + " TEXT ,"

                + Complaint.COL_REMARK + " TEXT ,"
                + Complaint.COL_IMAGE_NAME + " TEXT ,"
                + Complaint.COL_IMAGE_PATH + " TEXT ,"
                + Complaint.COL_CREATED_DATE_TIME + " DATETIME ,"
                + Complaint.COL_IS_POSTED + " BOOLEAN "
                + ")";

        return qry;
    }

    private void insert(Complaint obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Complaint.COL_SO_ID, obj.getSoId());
        values.put(Complaint.COL_REPORTED_BY, obj.getReportedBy());

        values.put(Complaint.COL_AGENT_SS_ID, obj.getAgentSSId());
        values.put(Complaint.COL_ROUTE_ID, obj.getRouteId());
        values.put(Complaint.COL_LOCALITY_ID, obj.getLocalityId());
        values.put(Complaint.COL_SHOP_ID, obj.getShopId());

        values.put(Complaint.COL_CUSTOMER_NAME, obj.getCustomerName());
        values.put(Complaint.COL_CONTACT_NO, obj.getContactNo());
        values.put(Complaint.COL_PLACE, obj.getPlace());

        values.put(Complaint.COL_COMPLAINT_ABOUT, obj.getComplaintAbout());
        values.put(Complaint.COL_PRODUCT, obj.getProduct());
        values.put(Complaint.COL_PRODUCT_SKU, obj.getProductSku());
        values.put(Complaint.COL_CRITERIA, obj.getCriteria());
        values.put(Complaint.COL_COMPLAINT, obj.getComplaint());
        values.put(Complaint.COL_COMPLAINT_OTHER, obj.getComplaintOther());

        values.put(Complaint.COL_BATCH_NO, obj.getBatchNo());
        values.put(Complaint.COL_QTY, obj.getQty());

        values.put(Complaint.COL_REMARK, obj.getRemark());
        values.put(Complaint.COL_IMAGE_NAME, obj.getImageName());
        values.put(Complaint.COL_IMAGE_PATH, obj.getImagePath());
        values.put(Complaint.COL_CREATED_DATE_TIME, DateFunc.getDateTimeStr());
        values.put(Complaint.COL_IS_POSTED, false);

        try {
            db.insert(Complaint.TABLE, null, values);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }

    }

    private void insert(Complaint[] objList, SQLiteDatabase db) {

        for (Complaint productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + Complaint.TABLE + "");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Complaint objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Complaint[] objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void setPosted() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Complaint.COL_IS_POSTED, 1);
        db.update(Complaint.TABLE, values, null, null);

        DatabaseManager.getInstance().closeDatabase();
    }

    public void markPosted(Complaint img) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " UPDATE " + Complaint.TABLE + "";
        sql += " SET is_posted='1'";
        sql += " WHERE is_posted='0'";
        sql += " AND id=" + img.getId() + "";
        db.execSQL(sql);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Complaint> getComplaintsForUpload(int soId) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Complaint.TABLE + "";
        sql += " WHERE is_posted=0";
        sql += " AND so_id = " + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }


        int ix_id = c.getColumnIndexOrThrow(Complaint.COL_ID);
        int ix_so_id = c.getColumnIndexOrThrow(Complaint.COL_SO_ID);

        int ix_agent_ss = c.getColumnIndexOrThrow(Complaint.COL_AGENT_SS_ID );
        int ix_route_id = c.getColumnIndexOrThrow(Complaint.COL_ROUTE_ID);
        int ix_locality_id = c.getColumnIndexOrThrow(Complaint.COL_LOCALITY_ID);
        int ix_shop_id = c.getColumnIndexOrThrow(Complaint.COL_SHOP_ID);

        int ix_reported_by = c.getColumnIndexOrThrow(Complaint.COL_REPORTED_BY);
        int ix_customer_name = c.getColumnIndexOrThrow(Complaint.COL_CUSTOMER_NAME);
        int ix_contact_no = c.getColumnIndexOrThrow(Complaint.COL_CONTACT_NO);
        int ix_place = c.getColumnIndexOrThrow(Complaint.COL_PLACE);

        int ix_complaint_about = c.getColumnIndexOrThrow(Complaint.COL_COMPLAINT_ABOUT);
        int ix_product = c.getColumnIndexOrThrow(Complaint.COL_PRODUCT);
        int ix_product_sku = c.getColumnIndexOrThrow(Complaint.COL_PRODUCT_SKU);
        int ix_criteria = c.getColumnIndexOrThrow(Complaint.COL_CRITERIA);
        int ix_complaint = c.getColumnIndexOrThrow(Complaint.COL_COMPLAINT);
        int ix_complaint_other = c.getColumnIndexOrThrow(Complaint.COL_COMPLAINT_OTHER);
        int ix_batch_no = c.getColumnIndexOrThrow(Complaint.COL_BATCH_NO);
        int ix_qty = c.getColumnIndexOrThrow(Complaint.COL_QTY);

        int ix_image_name = c.getColumnIndexOrThrow(Complaint.COL_IMAGE_NAME);
        int ix_image_path = c.getColumnIndexOrThrow(Complaint.COL_IMAGE_PATH);
        int ix_remark = c.getColumnIndexOrThrow(Complaint.COL_REMARK);
        int ix_created_date_time = c.getColumnIndexOrThrow(Complaint.COL_CREATED_DATE_TIME);

        Complaint complaint;
        List<Complaint> complaints = new ArrayList<>();

        while (c.moveToNext()) {

            complaint = new Complaint();
            complaint.setId(c.getInt(ix_id));
            complaint.setSoId(c.getInt(ix_so_id));
            complaint.setReportedBy(c.getString(ix_reported_by));

            complaint.setAgentSSId(c.getInt(ix_agent_ss));
            complaint.setRouteId(c.getInt(ix_route_id));
            complaint.setLocalityId(c.getInt(ix_locality_id));
            complaint.setShopId(c.getInt(ix_shop_id));

            complaint.setCustomerName(c.getString(ix_customer_name));
            complaint.setContactNo(c.getString(ix_contact_no));
            complaint.setPlace(c.getString(ix_place));

            complaint.setComplaintAbout(c.getString(ix_complaint_about));
            complaint.setProduct(c.getInt(ix_product));
            complaint.setProductSku(c.getInt(ix_product_sku));
            complaint.setCriteria(c.getString(ix_criteria));
            complaint.setComplaint(c.getString(ix_complaint));
            complaint.setComplaintOther(c.getString(ix_complaint_other));
            complaint.setBatchNo(c.getString(ix_batch_no));
            complaint.setQty(c.getString(ix_qty));

            complaint.setImageName(c.getString(ix_image_name));
            complaint.setImagePath(c.getString(ix_image_path));
            complaint.setRemark(c.getString(ix_remark));
            complaint.setCreatedDateTime(DateFunc.getDateTime(c.getString(ix_created_date_time)));
            complaints.add(complaint);
        }

        return complaints;
    }

//    public List<vComplaint> getComplaints() {
//
//        Cursor c;
//        String sql;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//
//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + Complaint.TABLE + "";
//
//        try {
//            c = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            return null;
//        }
//
//        return fillOrders(c);
//    }
//
//    private List<vComplaint> fillOrders(Cursor c) {
//
//        int ix_customer_type = c.getColumnIndexOrThrow(Complaint.COL_CUSTOMER_TYPE);
//        int ix_customer_name = c.getColumnIndexOrThrow(Complaint.COL_CUSTOMER_NAME);
//        int ix_category_1 = c.getColumnIndexOrThrow(Complaint.COL_CATEGORY_1);
//        int ix_category_2 = c.getColumnIndexOrThrow(Complaint.COL_CATEGORY_2);
//        int ix_category_3 = c.getColumnIndexOrThrow(Complaint.COL_CATEGORY_3);
//
//        vComplaint complaint;
//        List<vComplaint> complaints = new ArrayList<>();
//
//        while (c.moveToNext()) {
//
//            complaint = new vComplaint();
//            complaint.setCustomerType(c.getString(ix_customer_type));
//            complaint.setCustomerName(c.getString(ix_customer_name));
//            complaint.setCategory1(c.getString(ix_category_1));
//            complaint.setCategory2(c.getString(ix_category_2));
//            complaint.setCategory3(c.getString(ix_category_3));
//            complaints.add(complaint);
//        }
//
//        return complaints;
//    }
}
