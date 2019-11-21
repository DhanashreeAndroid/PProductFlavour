package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class Stock {

    public static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "trn_stock";

    public static final String COL_ID = "id";
    public static final String COL_MONTH_YEAR = "year_month";
    public static final String COL_STOCK_DATE = "stock_date";
    public static final String COL_SS_ID = "ss_id";
    public static final String COL_RL_PRODUCT_SKU_ID = "rl_product_sku_id";
    public static final String COL_QTY = "qty";
    public static final String COL_QTY_IN_TRANSIT = "qty_in_transit";
    public static final String COL_IS_EDITABLE = "is_editable";
    public static final String COL_CREATED_DATE_TIME = "created_date_time";
    public static final String COL_IS_POSTED = "is_posted";

    private int id;
    private int yearMonth;
    private Date stockDate ;
    private int shopId;
    private int rlProductSkuId;
    private int qty;
    private int qtyInTransit;
    private boolean isEditable;
    private Date createdDateTime;
    private boolean isPosted;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(int yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Date getStockDate() {
        return stockDate;
    }

    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getQtyInTransit() {
        return qtyInTransit;
    }

    public void setQtyInTransit(int qtyInTransit) {
        this.qtyInTransit = qtyInTransit;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public boolean isPosted() {
        return isPosted;
    }

    public void setPosted(boolean posted) {
        isPosted = posted;
    }
}

