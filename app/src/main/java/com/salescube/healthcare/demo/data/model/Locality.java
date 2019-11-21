package com.salescube.healthcare.demo.data.model;

public class Locality {

    public final static String TABLE = "ms_locality";

    public final static String COL_ID = "id";
    public final static String COL_LOCALITY_ID = "locality_id";
    public final static String COL_LOCALITY_NAME = "locality_name";
    public final static String COL_ROUTE_ID = "route_id";
    public final static String COL_SO_ID = "so_id";

    private  int id;
    private  int localityId;
    private  String localityName;
    private  int routeId;
    private  int soId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }
}
