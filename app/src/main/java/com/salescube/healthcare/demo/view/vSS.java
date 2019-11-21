package com.salescube.healthcare.demo.view;

/**
 * Created by user on 13/06/2017.
 */

public class vSS {

    private int ssId;
    private String ssName;
    private String role;

    public vSS() {

    }

    public vSS(int _ssId, String _ssName) {
        this.ssId = _ssId;
        this.ssName = _ssName;
    }

    public int getSsId() {
        return ssId;
    }

    public void setSsId(int ssId) {
        this.ssId = ssId;
    }

    public String getSsName() {
        return ssName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSsName(String ssName) {
        this.ssName = ssName;
    }

    @Override
    public String toString() {
        return ssName;
    }
}
