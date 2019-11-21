package com.salescube.healthcare.demo.data.model;

public class Employee {

    public final static String TABLE = "ms_employee";
    public final static String COL_ID = "id";
    public final static String COL_USER_ID = "user_id";
    public final static String COL_EMP_ID = "emp_id";
    public final static String COL_EMP_NAME = "emp_name";
    public final static String COL_APP_ROLE = "app_role";
    public final static String COL_MGR_ID = "mgr_id";

    private int id;
    private int userId;
    private int empId;
    private String empName;
    private String appRole;
    private int mgrId;

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

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAppRole() {
        return appRole;
    }

    public void setAppRole(String appRole) {
        this.appRole = appRole;
    }

    public int getMgrId() {
        return mgrId;
    }

    public void setMgrId(int mgrId) {
        this.mgrId = mgrId;
    }
}
