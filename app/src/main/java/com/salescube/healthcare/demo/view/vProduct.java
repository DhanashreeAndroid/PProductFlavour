package com.salescube.healthcare.demo.view;

/**
 * Created by user on 01/10/2016.
 */
public class vProduct {

    private int productId;
    private String productName;
    private int divisionId;

    public vProduct() {
    }

    public vProduct(int _productId, String _productName){
        productId = _productId;
        productName = _productName;
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

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    @Override
    public String toString() {
        return productName;
    }
}
