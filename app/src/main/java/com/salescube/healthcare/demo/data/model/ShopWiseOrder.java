package com.salescube.healthcare.demo.data.model;

import java.util.Date;

public class ShopWiseOrder {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "ms_shop_wise_order";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_SO_NAME= "so_name";
    public final static String COL_ORDER_DATE = "order_date";
    public static final String COL_SHOP_ID = "shop_id";
    public static final String COL_SHOP_NAME = "shop_name";

    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public final static String COL_ORDER_QTY = "order_qty";
    public final static String COL_TOTAL_AMOUNT = "total_amount";

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

    public ShopWiseOrder(String shopName, String productName, int orderQty, int totalAmount) {
        this.shopName=shopName;
        this.productName=productName;
        this.orderQty=orderQty;
        this.totalAmount=totalAmount;
    }

    public ShopWiseOrder(Date orderDate, String shopName, double totalAmount) {
        this.orderDate = orderDate;
        this.shopName = shopName;
        this.totalAmount = totalAmount;
    }

    public ShopWiseOrder(String shopName, double totalAmount) {
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
