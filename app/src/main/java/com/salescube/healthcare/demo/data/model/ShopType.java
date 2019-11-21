package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 17/11/2016.
 */

public class ShopType {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "ms_shop_type";

    public final static String COL_ID = "id";
    public final static String COL_SHOP_TYPE_ID = "shop_type_id";
    public final static String COL_SHOP_TYPE_NAME = "shop_type_name";

    private int id;
    private int shopTypeId;
    private String shopType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopTypeId() {
        return shopTypeId;
    }

    public void setShopTypeId(int shopTypeId) {
        this.shopTypeId = shopTypeId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
}
