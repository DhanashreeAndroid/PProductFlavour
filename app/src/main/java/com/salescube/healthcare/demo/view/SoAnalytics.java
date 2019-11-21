package com.salescube.healthcare.demo.view;

import java.io.Serializable;
import java.util.ArrayList;

public class SoAnalytics  implements Serializable {

    private int soId ;
    private BesPerformanceDay bestDay ;
    private int shopCount ;
    private ArrayList<ProductSecondary> productDaySecondary;
    private ArrayList<ProductSecondary> productWeekSecondary;
    private ArrayList<ProductSecondary> productMonthSecondary;

    private Sbt sbtPrevious ;
    private Sbt sbtCurrent ;

    private vMonthData monthData;

    public SoAnalytics() {
        productDaySecondary = new ArrayList<>();

    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public BesPerformanceDay getBestDay() {
        return bestDay;
    }

    public void setBestDay(BesPerformanceDay bestDay) {
        this.bestDay = bestDay;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public ArrayList<ProductSecondary> getProductDaySecondary() {
        return productDaySecondary;
    }

    public void setProductDaySecondary(ArrayList<ProductSecondary> productDaySecondary) {
        this.productDaySecondary = productDaySecondary;
    }

    public Sbt getSbtPrevious() {
        return sbtPrevious;
    }

    public void setSbtPrevious(Sbt sbtPrevious) {
        this.sbtPrevious = sbtPrevious;
    }

    public Sbt getSbtCurrent() {
        return sbtCurrent;
    }

    public void setSbtCurrent(Sbt sbtCurrent) {
        this.sbtCurrent = sbtCurrent;
    }

    public ArrayList<ProductSecondary> getProductWeekSecondary() {
        return productWeekSecondary;
    }

    public void setProductWeekSecondary(ArrayList<ProductSecondary> productWeekSecondary) {
        this.productWeekSecondary = productWeekSecondary;
    }

    public ArrayList<ProductSecondary> getProductMonthSecondary() {
        return productMonthSecondary;
    }

    public void setProductMonthSecondary(ArrayList<ProductSecondary> productMonthSecondary) {
        this.productMonthSecondary = productMonthSecondary;
    }

    public vMonthData getMonthData() {
        return monthData;
    }

    public void setMonthData(vMonthData monthData) {
        this.monthData = monthData;
    }
}
