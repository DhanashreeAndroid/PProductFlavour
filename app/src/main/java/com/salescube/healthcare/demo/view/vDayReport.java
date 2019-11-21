package com.salescube.healthcare.demo.view;

import java.util.Date;

/**
 * Created by user on 05/06/2017.
 */

public class vDayReport {

    public final static String TABLE = "vDayReport";

    public final static String COL_SO_ID = "so_id";
    public final static String COL_SO_MGR_ID  = "so_mgr_id";
    public final static String COL_SO_NAME = "so_name";
    public final static String COL_TARGET_AMOUNT= "target_amount";
    public final static String COL_CUMULATIVE_TARGET_AMOUNT= "cumulative_target_amount";
    public final static String COL_ACH_AMOUNT = "ach_amount";
    public final static String COL_ACH_PER = "total_ach_per";
    public final static String COL_TC = "tc";
    public final static String COL_PC = "pc";
    public final static String COL_TOTAL_TC = "total_tc";
    public final static String COL_SO_ROLE = "so_role";

    private int soId;
    private String soName;
    private double targetAmount;
    private double achAmount;
    private double achPer;
    private double tc;
    private double pc;
    private Date day;
    private double totalTC;
    private String soRole;
    private int mgrId;
    private double cumulative_target_amt;



    public vDayReport(){

    }

    public vDayReport(Date day, double achPer, double tc, double pc) {
        this.day = day;
        this.achPer = achPer;
        this.tc = tc;
        this.pc = pc;
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

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getAchAmount() {
        return achAmount;
    }

    public void setAchAmount(double achAmount) {
        this.achAmount = achAmount;
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }


    public double getTotalTc() {
        return totalTC;
    }

    public void setTotalTc(double totalTC) {
        this.totalTC = totalTC;
    }

    public int getMgrId() {
        return mgrId;
    }

    public void setMgrId(int mgrId) {
        this.mgrId = mgrId;
    }

    public double getCumulative_target_amt() {
        return cumulative_target_amt;
    }

    public void setCumulative_target_amt(double cumulative_target_amt) {
        this.cumulative_target_amt = cumulative_target_amt;
    }


}
