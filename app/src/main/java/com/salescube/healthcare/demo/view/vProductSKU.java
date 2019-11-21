package com.salescube.healthcare.demo.view;

/**
 * Created by user on 01/10/2016.
 */

public class vProductSKU {

    private int productId;
    private int rlProductSkuId;
    private String productSku;
    private double rate;

    public vProductSKU() {
    }

    public vProductSKU(int rlProductSkuId, String productSku) {
        this.rlProductSkuId = rlProductSkuId;
        this.productSku = productSku;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return productSku;
    }
}
