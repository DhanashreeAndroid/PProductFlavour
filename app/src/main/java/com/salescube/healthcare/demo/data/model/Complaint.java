package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 25/01/2018.
 */

public class Complaint {
    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "txn_complaint";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_REPORTED_BY = "reported_by";

    public final static String COL_AGENT_SS_ID = "agent_ss_id";
    public final static String COL_AREA_ID = "area_id";
    public final static String COL_ROUTE_ID = "route_id";
    public final static String COL_LOCALITY_ID = "locality_id";
    public final static String COL_SHOP_ID = "shop_id";

    public final static String COL_CUSTOMER_NAME = "customer_name";
    public final static String COL_CONTACT_NO = "contact_no";
    public final static String COL_PLACE = "place";

    public final static String COL_COMPLAINT_ABOUT = "complaint_about";
    public final static String COL_PRODUCT = "product";
    public final static String COL_PRODUCT_SKU = "product_sku";
    public final static String COL_CRITERIA = "criteria";
    public final static String COL_COMPLAINT = "complaint";
    public final static String COL_COMPLAINT_OTHER = "complaint_other";

    public final static String COL_BATCH_NO = "batch_no";
    public final static String COL_QTY = "qty";


    public final static String COL_REMARK = "remark";
    public final static String COL_IMAGE_NAME = "image_name";
    public final static String COL_IMAGE_PATH = "image_path";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private String reportedBy;

    private int agentSSId;
    private int areaId;
    private int routeId;
    private int localityId;
    private int shopId;

    private String customerName;
    private String contactNo;
    private String place;

    private String complaintAbout;
    private int product;
    private int productSku;
    private String criteria;
    private String complaint;
    private String complaintOther;

    private String batchNo;
    private String qty;

    private String imageName;
    private String imagePath;
    private String remark;
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

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public int getAgentSSId() {
        return agentSSId;
    }

    public void setAgentSSId(int agentSSId) {
        this.agentSSId = agentSSId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComplaintAbout() {
        return complaintAbout;
    }

    public void setComplaintAbout(String complaintAbout) {
        this.complaintAbout = complaintAbout;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getProductSku() {
        return productSku;
    }

    public void setProductSku(int productSku) {
        this.productSku = productSku;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getComplaintOther() {
        return complaintOther;
    }

    public void setComplaintOther(String complaintOther) {
        this.complaintOther = complaintOther;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
