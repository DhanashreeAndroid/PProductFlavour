package com.salescube.healthcare.demo.view;

/**
 * Created by user on 07/06/2017.
 */

public class vMonthReport {

    private int soId;
    private String soName;
    private double monthTargetAmount;
    private double cumulativeTargetAmount;
    private double cumulativeAchAmount;
    private double achPer;
    private double tc;
    private double pc;
    private double totalTC;
    private String date;
    private String soRole;
    private int MgrId;

    public int getMgrId() {
        return MgrId;
    }

    public void setMgrId(int mgrId) {
        MgrId = mgrId;
    }

    public String getSoRole() {
        return soRole;
    }

    public void setSoRole(String soRole) {
        this.soRole = soRole;
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

    public double getMonthTargetAmount() {
        return monthTargetAmount;
    }

    public void setMonthTargetAmount(double monthTargetAmount) {
        this.monthTargetAmount = monthTargetAmount;
    }

    public double getCumulativeTargetAmount() {
        return cumulativeTargetAmount;
    }

    public void setCumulativeTargetAmount(double cumulativeTargetAmount) {
        this.cumulativeTargetAmount = cumulativeTargetAmount;
    }

    public double getCumulativeAchAmount() {
        return cumulativeAchAmount;
    }

    public void setCumulativeAchAmount(double cumulativeAchAmount) {
        this.cumulativeAchAmount = cumulativeAchAmount;
    }

    public double getAchPer() {
        return achPer;
    }

    public void setAchPer(double achPer) {
        this.achPer = achPer;
    }

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getPc() {
        return pc;
    }

    public void setPc(double pc) {
        this.pc = pc;
    }

    public double getTotalTC() {
        return totalTC;
    }

    public void setTotalTC(double totalTC) {
        this.totalTC = totalTC;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
