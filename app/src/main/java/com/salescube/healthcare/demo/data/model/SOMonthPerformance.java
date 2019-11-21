package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class SOMonthPerformance {

    public int soId ;
    public Date orderDate ;
    public double targetAmount ;
    public double achievement ;
    public double totalAmount ;
    public int PC ;
    public int CC ;
    public int TC ;
    public double totalTC ;

    public SOMonthPerformance() {
    }

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

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getAchievement() {
        return achievement;
    }

    public void setAchievement(double achievement) {
        this.achievement = achievement;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getCC() {
        return CC;
    }

    public void setCC(int CC) {
        this.CC = CC;
    }

    public int getTC() {
        return TC;
    }

    public void setTC(int TC) {
        this.TC = TC;
    }

    public double getTotalTC() {
        return totalTC;
    }

    public void setTotalTC(double totalTC) {
        this.totalTC = totalTC;
    }
}