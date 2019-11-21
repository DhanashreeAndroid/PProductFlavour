package com.salescube.healthcare.demo.view;

import com.salescube.healthcare.demo.data.model.SOMonthAttendance;

import java.util.ArrayList;
import java.util.List;

public class vSOMonthAttendenceReport {

    public List<SOMonthAttendance> data;
    public List<vSO> soList ;

    public int presentDays ;
    public int otherWorkDays ;
    public int leavesDays ;

    public vSOMonthAttendenceReport() {
        data = new ArrayList<>();
        soList = new ArrayList<>();
    }

    public List<SOMonthAttendance> getData() {
        return data;
    }

    public void setData(List<SOMonthAttendance> data) {
        this.data = data;
    }

    public List<vSO> getSoList() {
        return soList;
    }

    public void setSoList(List<vSO> soList) {
        this.soList = soList;
    }

    public int getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(int presentDays) {
        this.presentDays = presentDays;
    }

    public int getOtherWorkDays() {
        return otherWorkDays;
    }

    public void setOtherWorkDays(int otherWorkDays) {
        this.otherWorkDays = otherWorkDays;
    }

    public int getLeavesDays() {
        return leavesDays;
    }

    public void setLeavesDays(int leavesDays) {
        this.leavesDays = leavesDays;
    }
}
