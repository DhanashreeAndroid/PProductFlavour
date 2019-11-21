package com.salescube.healthcare.demo.view;

import java.io.Serializable;

public class vTourPlanDetail implements Serializable {
    private String tourPlanId;
    private String title;
    private String value;

    public String getTourPlanId() {
        return tourPlanId;
    }

    public void setTourPlanId(String tourPlanId) {
        this.tourPlanId = tourPlanId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
