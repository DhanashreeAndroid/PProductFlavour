package com.salescube.healthcare.demo.data.model;

public class ProductTarget {

    public static final String TABLE = "txn_product_target";

    public static final String COL_SO_ID = "so_id";
    public static final String COL_SHOP_ID = "shop_id";
    public static final String COL_PRODUCT_SKU_ID = "product_sku_id";
    public static final String COL_RL_PRODUCT_SKU_ID = "rl_product_sku_id";
    public static final String COL_TARGET_QTY = "target_qty";
    public static final String COL_TARGET_VALUE = "target_value";

    private int soId;
    private int shopId;
    private int productSkuId;
    private int rlProductSkuId;
    private int targetQty;
    private double targetValue;

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public int getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(int targetQty) {
        this.targetQty = targetQty;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }
}
