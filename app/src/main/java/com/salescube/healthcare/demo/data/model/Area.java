package com.salescube.healthcare.demo.data.model;

public class Area {

    public final static String TABLE = "ms_area";

    public final static String COL_ID = "id";
    public final static String COL_AREA_ID = "area_id";
    public final static String COL_AREA_NAME = "area_name";
    public final static String COL_ZONE_ID = "zone_id";
    public final static String COL_SO_ID = "so_id";

    private  int id;
    private  int areaId;
    private  String areaName;
    private  int zoneId;
    private  int soId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }
}
