package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 03/11/2016.
 */

public class OtherWork {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_other_work";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_OTHER_WORK = "other_work";
    public final static String COL_ORDER_DATE = "order_date";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private int agentId;
    private String orherWork;
    private Date orderDate;
    private Date createdDateTime;
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

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getOrherWork() {
        return orherWork;
    }

    public void setOrherWork(String orherWork) {
        this.orherWork = orherWork;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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
