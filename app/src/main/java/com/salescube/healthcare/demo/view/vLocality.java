package com.salescube.healthcare.demo.view;

/**
 * Created by user on 01/10/2016.
 */

public class vLocality {
    private int localityId;
    private String localityName;
    private int routeId;
    private int soId;

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

    @Override
    public String toString() {
        return localityName;
    }
}
