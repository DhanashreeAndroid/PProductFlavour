package com.salescube.healthcare.demo.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 17/10/2016.
 */

public class SysDate implements Serializable {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "trn_sys_date";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_IMEI = "imei";
    public static final String COL_TR_DATE = "tr_date";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private String imei;
    private Date trDate;
    private Date createdDateTime;
    private boolean isPosted;
    private  String attType;
    private String latitude;
    private String longitude;

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getTrDate() {
        return trDate;
    }

    public void setTrDate(Date trDate) {
        this.trDate = trDate;
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