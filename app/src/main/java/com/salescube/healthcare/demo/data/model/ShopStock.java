package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class ShopStock {

    private int soId;
    private String appShopId;
    private Date entryDate ;
    private int rlProductSkuId ;
    private String productName ;
    private int qty ;
    private boolean editable;

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
