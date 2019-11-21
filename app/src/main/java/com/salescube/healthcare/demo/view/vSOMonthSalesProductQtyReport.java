package com.salescube.healthcare.demo.view;

import com.salescube.healthcare.demo.data.model.SOMonthProductSales;

import java.util.ArrayList;
import java.util.List;

public class vSOMonthSalesProductQtyReport {

    public List<SOMonthProductSales> data ;
    public List<vSO> soList;

    public vSOMonthSalesProductQtyReport() {
        data = new ArrayList<>();
        soList = new ArrayList<>();
    }

    public List<SOMonthProductSales> getData() {
        return data;
    }

    public void setData(List<SOMonthProductSales> data) {
        this.data = data;
    }

    public List<vSO> getSoList() {
        return soList;
    }

    public void setSoList(List<vSO> soList) {
        this.soList = soList;
    }
}
