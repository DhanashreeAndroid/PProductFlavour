package com.salescube.healthcare.demo.data.model;

import java.util.List;

public class AttendanceReport {

    private int soId;
    private String soName;
    private String date;
    private String action;
    private String time;
    private String lat;
    private String lng;

    private List<AttendanceReport> detail;

    public AttendanceReport(String soName,String date,String action, String time) {
        this.soName = soName;
        this.date = date;
        this.action = action;
        this.time = time;
    }

    public AttendanceReport(int soId, String soName, String action, String time, String lat, String lng) {
        this.soId = soId;
        this.soName = soName;
        this.action = action;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
    }

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

    public String getStatus() {
        return action;
    }

    public void setStatus(String action) {
        this.action = action;
    }

    public String getStartTime() {
        return time;
    }

    public void setStartTime(String time) {
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

    public List<AttendanceReport> getDetail() {
        return detail;
    }

    public void setDetail(List<AttendanceReport> detail) {
        this.detail = detail;
    }
}
