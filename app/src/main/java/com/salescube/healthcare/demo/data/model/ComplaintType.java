package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 25/01/2018.
 */

public class ComplaintType {
    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "mst_complaint_type";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_CUSTOMER_TYPE = "customer_type";
    public final static String COL_COMPLAINT_ABOUT = "complaint_about";
    public final static String COL_PRODUCT = "product";
    public final static String COL_CRITERIA = "criteria";
    public final static String COL_REASON = "reason";


    private int id ;
    private int soId ;
    private String customerType;
    private String complaintAbout;
    private String product;
    private String criteria;
    private String reason;

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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getComplaintAbout() {
        return complaintAbout;
    }

    public void setComplaintAbout(String complaintAbout) {
        this.complaintAbout = complaintAbout;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
