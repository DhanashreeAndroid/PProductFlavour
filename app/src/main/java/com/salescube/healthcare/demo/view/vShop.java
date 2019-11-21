package com.salescube.healthcare.demo.view;

/**
 * Created by user on 04/10/2016.
 */
public class vShop {

    private int id;
    private int shopId;
    private String appShopId;
    private String shopName;
    private int localityId;
    private String shopStatus;

    public vShop() {

    }

    public vShop(int _shopId, String _shopName) {
        shopId = _shopId;
        shopName = _shopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }

    @Override
    public String toString() {
        return shopName;
    }
}
