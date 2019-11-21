package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductTarget;
import com.salescube.healthcare.demo.view.vProductTarget;

import java.util.ArrayList;
import java.util.List;

public class ProductTargetRepo {

    public static String createTable() {

        String qry;

        qry = "CREATE TABLE " + ProductTarget.TABLE + "("
                + ProductTarget.COL_SO_ID + " INT ,"
                + ProductTarget.COL_SHOP_ID + " INT ,"
                + ProductTarget.COL_PRODUCT_SKU_ID + " INT ,"
                + ProductTarget.COL_RL_PRODUCT_SKU_ID + " INT ,"
                + ProductTarget.COL_TARGET_QTY + " INT, "
                + ProductTarget.COL_TARGET_VALUE + " DOUBLE "
                + ")";

        return qry;
    }

    private void insert(ProductTarget obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(ProductTarget.COL_SO_ID, obj.getSoId());
        values.put(ProductTarget.COL_SHOP_ID, obj.getShopId());
        values.put(ProductTarget.COL_PRODUCT_SKU_ID, obj.getProductSkuId());
        values.put(ProductTarget.COL_RL_PRODUCT_SKU_ID, obj.getRlProductSkuId());
        values.put(ProductTarget.COL_TARGET_QTY, obj.getTargetQty());
        values.put(ProductTarget.COL_TARGET_VALUE, obj.getTargetValue());

        try {
            db.insert(ProductTarget.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(ProductTarget[] objList, SQLiteDatabase db) {
        for (ProductTarget productRate : objList) {
            insert(productRate, db);
        }
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + ProductTarget.TABLE + " WHERE so_id = "+ soId +"");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ProductTarget objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(ProductTarget[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vProductTarget> getAll(int soId, String shopId) {

        List<vProductTarget> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += "";
        sql += " SELECT A.rl_product_sku_id, B.product_name || ' ' || B.product_sku As product_name, A.target_qty, A.target_value ";
        sql += " FROM " + ProductTarget.TABLE + " A";
        sql += " LEFT JOIN ( SELECT DISTINCT rl_product_sku_id, product_name, product_sku ";
        sql += "             FROM " + Product.TABLE;
        sql += "             WHERE so_id = " + soId + "";
        sql += " ) B ON(A.rl_product_sku_id=B.rl_product_sku_id)";
        sql += " WHERE A.so_id=" + soId + "";
        sql += " AND A.shop_id='" + shopId +"'";


        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vProducts;
        }

        if (c.getCount() == 0) {
            return vProducts;
        }

        int ix_rl_product_sku_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_product_name = c.getColumnIndexOrThrow("product_name");
        int ix_target_qty = c.getColumnIndexOrThrow("target_qty");
        int ix_target_value = c.getColumnIndexOrThrow("target_value");

        vProductTarget objProduct;
        while (c.moveToNext()) {

            objProduct = new vProductTarget();
            objProduct.setProductName(c.getString(ix_product_name));
            objProduct.setTargetQty(c.getInt(ix_target_qty));
            objProduct.setTargetValue(c.getDouble(ix_target_value));
            vProducts.add(objProduct);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
        return vProducts;
    }


}
