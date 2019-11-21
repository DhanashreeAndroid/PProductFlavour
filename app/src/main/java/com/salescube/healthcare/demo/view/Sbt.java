package com.salescube.healthcare.demo.view;

import java.io.Serializable;

public class Sbt implements Serializable {

    private String period;
    private Double targetValue ;
    private Double achValue ;
    private int achPercent ;

    private int rank;
    private String category;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Double getAchValue() {
        return achValue;
    }

    public void setAchValue(Double achValue) {
        this.achValue = achValue;
    }

    public int getAchPercent() {
        return achPercent;
    }

    public void setAchPercent(int achPercent) {
        this.achPercent = achPercent;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
