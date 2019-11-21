package com.salescube.healthcare.demo.data.model;

import java.util.Date;
import java.util.List;

/**
 * Created by user on 07/04/2017.
 */

public class Attendance {

    public final static String TABLE = "trn_attendance";
    public final static String COL_ID = "id";
    public final static String COL_USER_ID = "user_id";
    public final static String COL_TXN_DATE = "txn_date";
    public final static String COL_EMP_ID = "emp_id";
    public final static String COL_MGR_ID = "mgr_id";
    public final static String COL_WORK_TYPE = "work_type";
    public final static String COL_REMARK = "remark";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int userId;
    private Date txnDate;
    private int empId;
    private int mgrId;
    private String workType;
    private String remark;
    private Date createdDateTime;
    private boolean isPosted;

    private String soName;
    private String action;
    private String time;
    List<Attendance> attendanceList;

    public Attendance(String soName, String action, Date dateTime) {
        this.soName=soName;
        this.workType=action;
        this.createdDateTime=dateTime;
    }

    public Attendance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getMgrId() {
        return mgrId;
    }

    public void setMgrId(int mgrId) {
        this.mgrId = mgrId;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
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

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
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

}
