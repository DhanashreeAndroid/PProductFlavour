package com.salescube.healthcare.demo.view;

public class vProductTarget {

    private String productName;
    private int targetQty;
    private Double targetValue;
    private boolean isTotal;

    public vProductTarget() {

    }

    public vProductTarget(Double value) {
        this.targetValue = value;
    }

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

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setTotal(boolean total) {
        isTotal = total;
    }
}
