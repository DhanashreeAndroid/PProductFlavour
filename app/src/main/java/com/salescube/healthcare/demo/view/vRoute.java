package com.salescube.healthcare.demo.view;

/**
 * Created by user on 19/09/2016.
 */

public class vRoute {

    private int routeId;
    private String routeName;
    private int areaId;
    private int soId;


    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    @Override
    public String toString() {
        return routeName;
    }

    public static vRoute getDefault(){
        vRoute obj = new vRoute();
        obj.setRouteId(0);
        obj.setRouteName("--SELECT--");
        return obj;
    }
}
