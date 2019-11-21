package com.salescube.healthcare.demo.view;

/**
 * Created by user on 07/04/2017.
 */

public class vEmployee {

    @Override
    public String toString() {
        return this.empName;
    }

    private int empId;
    private String empName;
    private String appRole;
    private int mgrId;

    public vEmployee(){
    }

    public vEmployee(int _empId, String _empName) {
        this.empId = _empId;
        this.empName = _empName;
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
