package com.salescube.healthcare.demo.data.model;

import java.util.List;

public class SoAttendance {

    private int soId;
    private String soName;
    private String date;
    private String action;
    private String time;
    private String lat;
    private String lng;
    private String soRole;

    public String getSoRole() {
        return soRole;
    }

    public void setSoRole(String soRole) {
        this.soRole = soRole;
    }

    private List<SoAttendance> detail;

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<SoAttendance> getDetail() {
        return detail;
    }

    public void setDetail(List<SoAttendance> detail) {
        this.detail = detail;
    }


    public String getStartTime() {
        return time;
    }

    public void setStartTime(String time) {
        this.time = time;
    }


    public String getStatus() {
        return action;
    }

    public void setStatus(String action) {
        this.action = action;
    }



}
