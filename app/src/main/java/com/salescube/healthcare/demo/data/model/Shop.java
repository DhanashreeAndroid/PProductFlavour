package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 19/09/2016.
 */

public class Shop {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "ms_shop";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_SHOP_ID = "shop_id";
    public static final String COL_APP_SHOP_ID = "app_shop_id";
    public static final String COL_SHOP_NAME = "shop_name";
    public static final String COL_LOCALITY_ID = "locality_id";
    public static final String COL_REG_NO = "reg_no";
    public static final String COL_GST_NO = "gst_no";
    public static final String COL_MOBILE_NO = "mobile_no";
    public static final String COL_CONTACT_NO = "contact_no";
    public static final String COL_OWNER_NAME = "owner_name";
    public static final String COL_AUTO_SMS = "auto_sms";
    public static final String COL_SHOP_TYPE_ID = "shop_type_id";
    public static final String COL_SHOP_LOCATION = "shop_location";
    public static final String COL_STATUS = "shop_status";
    public static final String COL_RANK = "shop_rank";
    public static final String COL_REPLACE_WITH = "replaced_with_shop_id";
    public static final String COL_UPDATABLE = "updatable";
    public static final String COL_CREATED_DATE_TIME = "created_date_time";
    public static final String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private int shopId;
    private String appShopId;
    private String shopName;
    private int localityId;
    private String regNo;
    private String gstNo;
    private String mobileNo;
    private String contactNo;
    private String ownerName;
    private boolean autoSMS;
    private int shopTypeId;
    private String shopLocation;
    private String shopStatus;
    private String shopRank;
    private String replacedWith;
    private int updatedBy;
    private Date updatedOn;
    private boolean updatable;
    private Date createdDateTime;
    private boolean isPosted;

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
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

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean getAutoSMS() {
        return autoSMS;
    }

    public void setAutoSMS(boolean autoSMS) {
        this.autoSMS = autoSMS;
    }

    public int getShopTypeId() {
        return shopTypeId;
    }

    public void setShopTypeId(int shopTypeId) {
        this.shopTypeId = shopTypeId;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getShopRank() {
        return shopRank;
    }

    public void setShopRank(String shopRank) {
        this.shopRank = shopRank;
    }

    public String getReplacedWith() {

        return replacedWith;
    }

    public void setReplacedWith(String replacedWith) {
        this.replacedWith = replacedWith;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedDate) {
        this.updatedOn = updatedDate;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
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
