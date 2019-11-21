package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 10/10/2016.
 */

public class Target {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "trn_target";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_MONTH_TARGET_VALUE = "month_target_value";
    public static final String COL_MONTH_ACHIEVEMENT_VALUE = "month_achievement_value";
    public static final String COL_DAY_TARGET_VALUE = "day_target_value";
    public static final String COL_DAY_ACHIEVEMENT_VALUE = "day_achievement_value";

    public static final String COL_FOCUS_PRODUCT = "focus_product";

    private int soId;
    private Date orderDate;
    private double monthTargetValue;
    private double monthAchievementValue;
    private double dayTargetValue;
    private double dayAchievementValue;
    private String focusProducts;

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getMonthTargetValue() {
        return monthTargetValue;
    }

    public void setMonthTargetValue(double monthTargetValue) {
        this.monthTargetValue = monthTargetValue;
    }

    public double getMonthAchievementValue() {
        return monthAchievementValue;
    }

    public void setMonthAchievementValue(double monthAchievementValue) {
        this.monthAchievementValue = monthAchievementValue;
    }

    public double getDayTargetValue() {
        return dayTargetValue;
    }

    public void setDayTargetValue(double dayTargetValue) {
        this.dayTargetValue = dayTargetValue;
    }

    public double getDayAchievementValue() {
        return dayAchievementValue;
    }

    public void setDayAchievementValue(double dayAchievementValue) {
        this.dayAchievementValue = dayAchievementValue;
    }

    public String getFocusProducts() {
        return focusProducts;
    }

    public void setFocusProducts(String focusProducts) {
        this.focusProducts = focusProducts;
    }
}
