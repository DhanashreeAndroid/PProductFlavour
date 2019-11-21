package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 01/10/2016.
 */

public class Product {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "ms_product";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_PRODUCT_SKU_ID = "product_sku_id";
    public static final String COL_PRODUCT_SKU = "product_sku";
    public static final String COL_RL_PRODUCT_SKU_ID = "rl_product_sku_id";
    public static final String COL_MRP = "mrp";
    public static final String COL_SORT_KEY = "sort_key";
    public static final String COL_DIVISION_ID = "division_id";

    private int id;
    private int soId;
    private int productId;
    private String productName;
    private String productSku;
    private int productSkuId;
    private int rlProductSkuId;
    private double mrp;
    private int sortKey;
    private int divisionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public int getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(int productSkuId) {
        this.productSkuId = productSkuId;
    }

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
