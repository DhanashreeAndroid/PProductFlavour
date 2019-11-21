package com.salescube.healthcare.demo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class vTourPlan implements Serializable {
    private String id;
    private int soId;
    private String tourDate;
    private String setName;
    private int isSync;
    private int createdBy;
    private String createdOn;
    private int updatedBy;
    private String updatedOn;
    private List<vTourPlanDetail> detail = new ArrayList<>();

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
        return setName;
    }

    public void setSetName(String status) {
        this.setName = status;
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

    public List<vTourPlanDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<vTourPlanDetail> detail) {
        this.detail = detail;
    }
}
