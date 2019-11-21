package com.salescube.healthcare.demo.data.model;

public class Dashboard {
    private Double DayTarget;
    private Double DayAchievement;
    private Double MonthTarget;
    private Double MonthAchievement;
    private String DayTitle;
    private String MonthTitle;
    private String DayColor;
    private String MonthColor;

    public Double getDayTarget() {
        return DayTarget;
    }

    public void setDayTarget(Double dayTarget) {
        DayTarget = dayTarget;
    }

    public Double getDayAchievement() {
        return DayAchievement;
    }

    public void setDayAchievement(Double dayAchievement) {
        DayAchievement = dayAchievement;
    }

    public Double getMonthTarget() {
        return MonthTarget;
    }

    public void setMonthTarget(Double monthTarget) {
        MonthTarget = monthTarget;
    }

    public Double getMonthAchievement() {
        return MonthAchievement;
    }

    public void setMonthAchievement(Double monthAchievement) {
        MonthAchievement = monthAchievement;
    }

    public String getDayTitle() {
        return DayTitle;
    }

    public void setDayTitle(String dayTitle) {
        DayTitle = dayTitle;
    }

    public String getMonthTitle() {
        return MonthTitle;
    }

    public void setMonthTitle(String monthTitle) {
        MonthTitle = monthTitle;
    }

    public String getDayColor() {
        return DayColor;
    }

    public void setDayColor(String dayColor) {
        DayColor = dayColor;
    }

    public String getMonthColor() {
        return MonthColor;
    }

    public void setMonthColor(String monthColor) {
        MonthColor = monthColor;
    }
}
