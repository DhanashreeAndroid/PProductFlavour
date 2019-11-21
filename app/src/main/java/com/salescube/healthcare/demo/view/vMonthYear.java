package com.salescube.healthcare.demo.view;

public class vMonthYear {

    private String monthyear;
    private String fromDate;
    private String toDate;

    public vMonthYear() {

    }

    public vMonthYear(String _monthyear,String _fromDate,String _toDate) {
        this.monthyear = _monthyear;
        this.fromDate = _fromDate;
        this.toDate = _toDate;
    }

    public String getMonthyear() {
        return monthyear;
    }

    public void setMonthyear(String monthyear) {
        this.monthyear = monthyear;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return getMonthyear();
    }
}
