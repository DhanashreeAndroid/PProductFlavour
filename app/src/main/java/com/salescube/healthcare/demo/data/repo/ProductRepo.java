package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductRate;
import com.salescube.healthcare.demo.data.model.ProductTarget;
import com.salescube.healthcare.demo.view.vProduct;
import com.salescube.healthcare.demo.view.vProductSKU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 01/10/2016.
 */

public class ProductRepo extends RepoBase {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Product.TABLE + "("
                + Product.COL_ID + " PRIMARY KEY ,"
                + Product.COL_SO_ID + " INT ,"
                + Product.COL_PRODUCT_ID + " INT ,"
                + Product.COL_PRODUCT_NAME + " TEXT ,"
                + Product.COL_PRODUCT_SKU_ID + " INT ,"
                + Product.COL_PRODUCT_SKU + " TEXT ,"
                + Product.COL_RL_PRODUCT_SKU_ID + " INT ,"
                + Product.COL_MRP + " DECIMAL(10,2) ,"
                + Product.COL_SORT_KEY + " INT ,"
                + Product.COL_DIVISION_ID + " INT "
                + ")";

        return qry;
    }

    public void  deleteAll(int soId){

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " DELETE FROM " + Product.TABLE + "";
        sql += " WHERE so_id = " + soId + "";

        db.execSQL(sql);
        DatabaseManager.getInstance().closeDatabase();

    }

    private void insert(Product product, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Product.COL_SO_ID, product.getSoId());
        values.put(Product.COL_PRODUCT_ID, product.getProductId());
        values.put(Product.COL_PRODUCT_NAME, product.getProductName());
        values.put(Product.COL_PRODUCT_SKU_ID, product.getProductSkuId());
        values.put(Product.COL_PRODUCT_SKU, product.getProductSku());
        values.put(Product.COL_RL_PRODUCT_SKU_ID, product.getRlProductSkuId());
        values.put(Product.COL_MRP, product.getMrp());
        values.put(Product.COL_SORT_KEY, product.getSortKey());
        values.put(Product.COL_DIVISION_ID, product.getDivisionId());

        try {
            db.insert(Product.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(Product[] products, SQLiteDatabase db) {

        for (Product product : products) {
            insert(product, db);
        }
    }

    public void insert(Product[] products) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        insert(products, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vProduct> getProductsAll(int soId) {

        List<vProduct> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT DISTINCT product_id, upper(product_name) as product_name ";
        sql += " FROM " + Product.TABLE;
        sql += " WHERE so_id = " + soId + " order by product_name ASC";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vProducts;
        }

        if (c.getCount() == 0) {
            return vProducts;
        }

        int ix_product_id = c.getColumnIndexOrThrow("product_id");
        int ix_product_name = c.getColumnIndexOrThrow("product_name");

        vProduct objProduct;
        while (c.moveToNext()) {

            objProduct = new vProduct();
            objProduct.setProductId(c.getInt(ix_product_id));
            objProduct.setProductName(c.getString(ix_product_name));
            vProducts.add(objProduct);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
        return vProducts;
    }

    public List<vProductSKU> getProductSkuAll(int soId, int agentId, String appShopId, String priceCode) {

        List<vProductSKU> vProducts = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT A.product_id, A.rl_product_sku_id, A.product_sku ,C.target_qty , B.rate ";
        sql += " FROM ( SELECT DISTINCT product_id, rl_product_sku_id, product_sku ";
        sql += "        FROM " + Product.TABLE + " ";
        sql += "        WHERE so_id =" + soId + " ";
        sql += " ) As A ";
        sql += " LEFT JOIN " + ProductRate.TABLE + " As B ";
        sql += "            On(A.rl_product_sku_Id=B.rl_product_sku_Id AND b.so_id="+ soId +" AND B.agent_id=" + agentId +" AND B.price_code='"+ priceCode + "')";
        sql += " LEFT JOIN " + ProductTarget.TABLE + " As C";
        sql += "            ON(A.rl_product_sku_id=C.rl_product_sku_id AND C.shop_id='" + appShopId + "')" ;

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex .getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_product_id = c.getColumnIndexOrThrow("product_id");
        int ix_product_sku = c.getColumnIndexOrThrow("product_sku");
        int ix_rl_product_sku_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_rate = c.getColumnIndexOrThrow("rate");
        int ix_target_qty = c.getColumnIndexOrThrow("target_qty");

        String skuName, targetQty, skuWithTarget;
        vProductSKU objProduct;
        while (c.moveToNext()) {

            skuName= c.getString(ix_product_sku);
            targetQty = c.getString(ix_target_qty);

            if (targetQty != null) {
                skuWithTarget = skuName + "   [TGT-" + targetQty + " Qty]";
            }else {
                skuWithTarget = skuName;
            }


            objProduct = new vProductSKU();
            objProduct.setProductId(c.getInt(ix_product_id));
            objProduct.setProductSku(skuWithTarget);
            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_sku_id));
            objProduct.setRate(c.getDouble(ix_rate));

            vProducts.add(objProduct);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
        return vProducts;
    }

    public List<vProductSKU> getProductSku(int soId) {

        List<vProductSKU> vProducts = new ArrayList<>();

        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql = "";
        sql += " SELECT DISTINCT product_id, rl_product_sku_id, product_sku ";
        sql += " FROM " + Product.TABLE + " ";
        sql += " WHERE so_id =" + soId + " ";


        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex .getMessage());
            return null;
        }

        if (c.getCount() == 0) {
            return null;
        }

        int ix_product_id = c.getColumnIndexOrThrow("product_id");
        int ix_rl_product_id = c.getColumnIndexOrThrow("rl_product_sku_id");
        int ix_product_sku = c.getColumnIndexOrThrow("product_sku");

        vProductSKU objProduct;
        while (c.moveToNext()) {

            objProduct = new vProductSKU();
            objProduct.setProductId(c.getInt(ix_product_id));
            objProduct.setRlProductSkuId(c.getInt(ix_rl_product_id));
            objProduct.setProductSku(c.getString(ix_product_sku));

            vProducts.add(objProduct);
        }

        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
        return vProducts;
    }
}
