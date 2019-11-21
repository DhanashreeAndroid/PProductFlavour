package com.salescube.healthcare.demo.view;

import java.util.Date;

/**
 * Created by user on 12/11/2016.
 */

public class vTarget {

    private int soId;
    private Date orderDate;
    private double monthTargetValue;
    private double monthAchievementValue;
    private double monthBalanceTarget;
    private double dayTargetValue;
    private double dayAchievementValue;
    private double dayBalanceTarget;

    private String focusProducts;


    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getMonthTargetValue() {
        return monthTargetValue;
    }

    public void setMonthTargetValue(double monthTargetValue) {
        this.monthTargetValue = monthTargetValue;
    }

    public double getMonthAchievementValue() {
        return monthAchievementValue;
    }

    public void setMonthAchievementValue(double monthAchievementValue) {
        this.monthAchievementValue = monthAchievementValue;
    }

    public double getMonthBalanceTarget() {
        return monthBalanceTarget;
    }

    public void setMonthBalanceTarget(double monthBalanceTarget) {
        this.monthBalanceTarget = monthBalanceTarget;
    }

    public double getDayTargetValue() {
        return dayTargetValue;
    }

    public void setDayTargetValue(double dayTargetValue) {
        this.dayTargetValue = dayTargetValue;
    }

    public double getDayAchievementValue() {
        return dayAchievementValue;
    }

    public void setDayAchievementValue(double dayAchievementValue) {
        this.dayAchievementValue = dayAchievementValue;
    }

    public double getDayBalanceTarget() {
        return dayBalanceTarget;
    }

    public void setDayBalanceTarget(double dayBalanceTarget) {
        this.dayBalanceTarget = dayBalanceTarget;
    }

    public String getFocusProducts() {
        return focusProducts;
    }

    public void setFocusProducts(String focusProducts) {
        this.focusProducts = focusProducts;
    }
}
