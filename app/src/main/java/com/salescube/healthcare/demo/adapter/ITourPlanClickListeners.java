package com.salescube.healthcare.demo.adapter;


import com.salescube.healthcare.demo.view.vTourDates;
import com.salescube.healthcare.demo.view.vTourPlan;

public interface ITourPlanClickListeners {
    void onDayClick(vTourDates dates);

    void onAddMoreClick(vTourPlan vo);

    void onCloseClick(vTourPlan vo, int position);
}
