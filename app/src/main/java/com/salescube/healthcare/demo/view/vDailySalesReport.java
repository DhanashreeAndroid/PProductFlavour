package com.salescube.healthcare.demo.view;

public class vDailySalesReport {


    private int Id;
    private int soId;
    private String soName;
    private int productId;
    private String productName;
    private double targetAmount;
    private double achAmount;
    private double totalValue;

    public vDailySalesReport(int soId, String soName) {
        this.soId = soId;
        this.soName = soName;
    }


    public vDailySalesReport(int soId, String soName, int productId, String productName, double targetAmount, double achAmount, double totalValue) {
        this.soId = soId;
        this.soName = soName;
        this.productId = productId;
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.achAmount = achAmount;
        this.totalValue = totalValue;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public double getTgt() {
        return targetAmount;
    }

    public void setTgt(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getAch() {
        return achAmount;
    }

    public void setAch(double achAmount) {
        this.achAmount = achAmount;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return getSoName();
    }

}
