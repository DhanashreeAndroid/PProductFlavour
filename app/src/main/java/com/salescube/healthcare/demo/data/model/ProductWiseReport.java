package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class ProductWiseReport {

    private int soId;
    private Date fromDate;
    private Date toDate;
    private int productId;
    private String productName;
    private Date orderDate;
    public int rlProductSKUId;
    private int achQty;
    private int totalQty;
    private double totalAmount;
    private double targetQty;

    public ProductWiseReport(Date fromDate, Date toDate, String productName, int totalQty, double totalAmount) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.productName = productName;
        this.totalQty = totalQty;
        this.totalAmount = totalAmount;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getRlProductSKUId() {
        return rlProductSKUId;
    }

    public void setRlProductSKUId(int rlProductSKUId) {
        this.rlProductSKUId = rlProductSKUId;
    }

    public int getAchQty() {
        return achQty;
    }

    public void setAchQty(int achQty) {
        this.achQty = achQty;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(double targetQty) {
        this.targetQty = targetQty;
    }
}
