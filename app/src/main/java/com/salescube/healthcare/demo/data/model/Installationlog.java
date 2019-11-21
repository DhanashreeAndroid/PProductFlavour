package com.salescube.healthcare.demo.data.model;

public class Installationlog {

    private String mobileVersion;
    private int  soId;
    private String appVersion;
    private String DateTime;
    private String type;

    public String getMobileVersion() {
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
