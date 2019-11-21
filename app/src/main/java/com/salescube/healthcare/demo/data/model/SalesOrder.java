package com.salescube.healthcare.demo.data.model;


import java.util.Date;

/**
 * Created by user on 06/10/2016.
 */

public class SalesOrder {

    public  static  final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_sales_order";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_iD";
    public final static String COL_ORDER_DATE = "order_date";
    public final static String COL_APP_SHOP_ID = "app_shop_id";
    public final static String COL_SET_NO = "set_no";
    public final static String COL_RL_PRODUCT_SKUID = "rl_product_sku_id";
    public final static String COL_TARGET_QTY = "target_qty";
    public final static String COL_ORDER_QTY = "order_qty";
    public final static String COL_RATE = "rate";
    public final static String COL_FREE_QTY = "free_qty";

// REFERENCE from K-pra App
    public final static String COL_DISCOUNT_RATE = "discount_rate";
    public final static String COL_DISCOUNT = "discount";
    public final static String COL_ADDITIONAL_DISCOUNT = "additional_discount";
    public final static String COL_TOTAL_AMOUNT = "total_amount";

    public final static String COL_SCHEME_RL_PRODUCT_SKUID = "scheme_rl_product_sku_id";
    public final static String COL_SCHEME_QTY = "scheme_qty";
    public final static String COL_IS_SCHEME ="is_scheme";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";
    public final static String COL_IS_CANCELLED = "is_cancelled";

    private int id;
    private int soId;
    private int agentId;
    private String appShopId;
    private String setNo;
    private Date orderDate;
    private int rlProductSkuId;
    private int targetQty;
    private int orderQty;
    private double rate;
    private int freeQty;
    private int scheme_rlProductSkuId;
    private int scheme_qty;
    private boolean isScheme;
    private Date createdDateTime;
    private boolean isPosted;
    private boolean isCancelled;

//    REFERENCE FROM KPra

    private double discountRate;
    private double discount;
    private double additionalDiscount;
    private double totalAmount;


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

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public String getSetNo() {
        return setNo;
    }

    public void setSetNo(String setNo) {
        this.setNo = setNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public int getScheme_rlProductSkuId() {
        return scheme_rlProductSkuId;
    }

    public void setScheme_rlProductSkuId(int scheme_rlProductSkuId) {
        this.scheme_rlProductSkuId = scheme_rlProductSkuId;
    }

    public int getScheme_qty() {
        return scheme_qty;
    }

    public void setScheme_qty(int scheme_qty) {
        this.scheme_qty = scheme_qty;
    }

    public boolean getIsScheme() {
        return isScheme;
    }

    public void setIsScheme(boolean scheme) {
        isScheme = scheme;
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

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

//REFERENCE FROM K-Pra
    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAdditionalDiscount() {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(double additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


}
