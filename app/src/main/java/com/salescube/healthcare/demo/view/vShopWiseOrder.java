package com.salescube.healthcare.demo.view;

import java.util.Date;

public class vShopWiseOrder {
    private int Id;
    private int soId;
    private String soName;
    private Date orderDate;
    private String shopId;
    private String shopName;
    private int productId;
    private String productName;
    private double orderQty;
    private double totalAmount;

    public vShopWiseOrder() {
    }
    public vShopWiseOrder(int soId, String soName) {
        this.soId=soId;
        this.soName=soName;
    }

    public vShopWiseOrder(String shopName, double totalAmount) {
        this.shopName = shopName;
        this.totalAmount = totalAmount;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return getSoName();
    }
}
