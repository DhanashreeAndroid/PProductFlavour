package com.salescube.healthcare.demo.view;

import java.io.Serializable;
import java.util.Date;

public class BesPerformanceDay implements Serializable {

    private Date day;
    private double achValue;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public double getAchValue() {
        return achValue;
    }

    public void setAchValue(double achValue) {
        this.achValue = achValue;
    }
}
