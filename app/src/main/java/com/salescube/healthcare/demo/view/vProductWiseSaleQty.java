package com.salescube.healthcare.demo.view;

import java.util.List;

public class vProductWiseSaleQty {

    private List<vDailySalesReport> data;
    private List<vSO> soList;

    public List<vDailySalesReport> getData() {
        return data;
    }

    public void setData(List<vDailySalesReport> data) {
        this.data = data;
    }

    public List<vSO> getSoList() {
        return soList;
    }

    public void setSoList(List<vSO> soList) {
        this.soList = soList;
    }
}
