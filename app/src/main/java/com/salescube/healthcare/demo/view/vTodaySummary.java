package com.salescube.healthcare.demo.view;

/**
 * Created by user on 25/11/2016.
 */

public class vTodaySummary {

    private  String productName;
    private  int targetQty;
    private  int orderQty;
    private  int freeQty;
    private  double rate;
    private  double orderValue;

//  for SO Name

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(int targetQty) {
        this.targetQty = targetQty;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }
}
