package com.salescube.healthcare.demo.view;

import java.util.Date;

/**
 * Created by user on 18/10/2016.
 */

public class vTodayOrders  {

    private Date orderDate;
    private int agentId;
    private int areaId;
    private String appShopId;
    private String shopName;
    private String productName;
    private int rlProductSkuId;
    private int orderQty;
    private int freeQty;
    private double rate;
    private double orderValue;
    private String discount;
    private String agentName;

    public vTodayOrders() {
    }

    public vTodayOrders(String appShopId, String shopName) {
        this.appShopId = appShopId;
        this.shopName = shopName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRlProductSkuId() {
        return rlProductSkuId;
    }

    public void setRlProductSkuId(int rlProductSkuId) {
        this.rlProductSkuId = rlProductSkuId;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return getShopName();
    }
}
