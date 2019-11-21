package com.salescube.healthcare.demo.view;

import com.salescube.healthcare.demo.data.model.SOMonthPerformance;

import java.util.List;

public class vSOMonthPerformance {

    public List<SOMonthPerformance> data;
    public List<vSO> soList ;

    public vSOMonthPerformance(List<SOMonthPerformance> data, List<vSO> soList) {
        this.data = data;
        this.soList = soList;
    }

    public List<SOMonthPerformance> getData() {
        return data;
    }

    public void setData(List<SOMonthPerformance> data) {
        this.data = data;
    }

    public List<vSO> getSoList() {
        return soList;
    }

    public void setSoList(List<vSO> soList) {
        this.soList = soList;
    }
}
