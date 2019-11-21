package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 03/11/2016.
 */

public class Leave {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_leave";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_LEAVE_TYPE = "leave_type";
    public final static String COL_DURATION = "duration";
    public final static String COL_ORDER_DATE = "order_date";
    public final static String COL_FROM_DATE = "from_date";
    public final static String COL_TO_DATE = "to_date";
    public final static String COL_CO_FOR = "co_for";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private String leaveType;
    private String duration;
    private Date orderDate;
    private Date fromDate;
    private Date toDate;
    private Date coFor;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCoFor() {
        return coFor;
    }

    public void setCoFor(Date coFor) {
        this.coFor = coFor;
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