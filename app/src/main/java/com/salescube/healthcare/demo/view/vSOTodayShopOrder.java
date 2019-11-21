package com.salescube.healthcare.demo.view;

import com.salescube.healthcare.demo.data.model.ShopWiseOrder;

import java.util.List;

public class vSOTodayShopOrder {

    public List<ShopWiseOrder> data;
    public List<vSO> soList;

    public List<ShopWiseOrder> getData() {
        return data;
    }

    public void setData(List<ShopWiseOrder> data) {
        this.data = data;
    }

    public List<vSO> getSoList() {
        return soList;
    }

    public void setSoList(List<vSO> soList) {
        this.soList = soList;
    }
}
