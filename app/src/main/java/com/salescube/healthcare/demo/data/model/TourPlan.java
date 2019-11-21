package com.salescube.healthcare.demo.data.model;

import java.util.ArrayList;
import java.util.List;

public class TourPlan {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "txn_tour_plan";

    public final static String COL_ID = "id";
    public final static String COL_TOUR_DATE = "tour_date";
    public final static String COL_SET = "set_name";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_IS_SYNC = "is_sync";
    public final static String COL_CREATED_BY = "created_by";
    public final static String COL_CREATED_ON = "created_on";
    public final static String COL_UPDATED_BY = "updated_by";
    public final static String COL_UPDATED_ON = "updated_on";
    public final static String COL_IS_CANCELLED = "is_cancelled";

    private String id;
    private String tourDate;
    private String workType;
    private int soId;
    private int isSync;
    private int createdBy;
    private String createdOn;
    private int updatedBy;
    private String updatedOn;
    private List<TourPlanDetail> detail = new ArrayList<>();
    private int isCancelled;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public String getSetName() {
        return workType;
    }

    public void setSetName(String status) {
        this.workType = status;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<TourPlanDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<TourPlanDetail> detail) {
        this.detail = detail;
    }

    public int isCancelled() {
        return isCancelled;
    }

    public void setCancelled(int cancelled) {
        isCancelled = cancelled;
    }
}
