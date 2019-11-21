package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class SalesReturn {

    public  static  final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_sales_return";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_RETURN_DATE = "return_date";
    public final static String COL_APP_SHOP_ID = "app_shop_id";
    public final static String COL_SET_NO = "set_no";
    public final static String COL_RL_PRODUCT_SKUID = "rl_product_sku_id";
    public final static String COL_RETURN_QTY = "order_qty";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_CANCELLED =  "is_cancelled";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private Date returnDate;
    private String appShopId;
    private String setNo;
    private int rlProductSkuId;
    private int returnQty;
    private int agentId;
    private Date createdDateTime;
    private boolean isCancelled;
    private boolean isPosted;

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

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
    }

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isPosted() {
        return isPosted;
    }

    public void setPosted(boolean posted) {
        isPosted = posted;
    }
}
