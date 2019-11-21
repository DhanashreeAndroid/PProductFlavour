package com.salescube.healthcare.demo.data.model;

import com.salescube.healthcare.demo.view.vSOAttendenceTotal;

import java.util.List;

public class SOMonthAttendance {

    private int soId;
    private String soName;
    private String date;
    private String action;
    private String startTime;

    private List<vSOAttendenceTotal> soTotalList;

    public SOMonthAttendance(int soId, String date, String action, String startTime) {
        this.soId = soId;
        this.soName = soName;
        this.date = date;
        this.action = action;
        this.startTime = startTime;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return startTime;
    }

    public void setTime(String startTime) {
        this.startTime = startTime;
    }

    public List<vSOAttendenceTotal> getSoTotalList() {
        return soTotalList;
    }

    public void setSoTotalList(List<vSOAttendenceTotal> soTotalList) {
        this.soTotalList = soTotalList;
    }

}
