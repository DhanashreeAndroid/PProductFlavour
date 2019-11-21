package com.salescube.healthcare.demo.view;

/**
 * Created by user on 17/11/2016.
 */

public class vShopType {

    private int shopTypeId;
    private String shopTypeName;

    public int getShopTypeId() {
        return shopTypeId;
    }

    public void setShopTypeId(int shopTypeId) {
        this.shopTypeId = shopTypeId;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    @Override
    public String toString() {
        return shopTypeName;
    }
}
