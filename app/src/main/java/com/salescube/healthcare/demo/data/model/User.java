package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 19/09/2016.
 */

public class User {

    public  static  final String TAG = User.class.getSimpleName();
    public static final String TABLE = "ms_user";

    public static final String COL_ID = "id";
    public static final String COL_EMPLOYEE_ID = "employee_id";
    public static final String COL_EMPLOYEE_NAME = "employee_name";
    public static final String COL_DIVISION_ID = "division_id";
    public static final String COL_IMEI_NO = "imei_no";

    public static final String COL_USERNAME = "user_name";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE_ID = "role_id";
    public static final String COL_ROLE_NAME = "role_name";
    public static final String COL_APP_ROLE = "app_role";
    public static final String COL_MGR_ID = "mgr_id";
    public static final String COL_LAST_LOGIN_DATE = "last_login_date";


    private int employeeId;
    private String employeeName;
    private String divisionId;
    private String imeiNo;
    private String userName;
    private String password;
    private int roleId;
    private String roleName;
    private String appRole;
    private int mgrId;
    private Date lastLoginDate;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
