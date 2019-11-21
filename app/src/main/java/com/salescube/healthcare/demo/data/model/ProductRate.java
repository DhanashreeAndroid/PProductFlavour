package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 10/10/2016.
 */

public class ProductRate {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "ms_product_rate";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_PRICE_CODE = "price_code";
    public static final String COL_AGENT_ID = "agent_id";
    public static final String COL_RL_PRODUCT_SKU_ID = "rl_product_sku_id";
    public static final String COL_RATE = "rate";

    public int soId;
    public String priceCode;
    public int agentId;
    public int rlProductSKUId;
    public double rate;

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getRlProductSKUId() {
        return rlProductSKUId;
    }

    public void setRlProductSKUId(int rlProductSKUId) {
        this.rlProductSKUId = rlProductSKUId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
