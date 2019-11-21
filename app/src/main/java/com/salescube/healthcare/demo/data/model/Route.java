package com.salescube.healthcare.demo.data.model;

public class Route {

    public final static String TABLE = "ms_route";

    public final static String COL_ID = "id";
    public final static String COL_ROUTE_ID = "route_id";
    public final static String COL_ROUTE_NAME = "route_name";
    public final static String COL_AREA_ID = "area_id";
    public final static String COL_SO_ID = "so_id";

    private int id;
    private int routeId;
    private String routeName;
    private int areaId;
    private int soId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
