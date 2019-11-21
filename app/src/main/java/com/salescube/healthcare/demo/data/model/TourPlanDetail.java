package com.salescube.healthcare.demo.data.model;

public class TourPlanDetail {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "txn_tour_plan_detail";

    public final static String COL_TOUR_PLAN_ID = "tour_plan_id";
    public final static String COL_TITLE = "title";
    public final static String COL_VALUE = "value";

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
