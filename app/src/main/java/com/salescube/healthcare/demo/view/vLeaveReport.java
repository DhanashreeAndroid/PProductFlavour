package com.salescube.healthcare.demo.view;

import java.util.Date;

/**
 * Created by user on 22/04/2017.
 */

public class vLeaveReport {

    private Date fromDate;
    private Date toDate;
    private String leaveType;
    private String Duration;
    private Date coFor;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public Date getCoFor() {
        return coFor;
    }

    public void setCoFor(Date coFor) {
        this.coFor = coFor;
    }
}
