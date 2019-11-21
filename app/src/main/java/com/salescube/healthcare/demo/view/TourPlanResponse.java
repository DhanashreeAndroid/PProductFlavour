package com.salescube.healthcare.demo.view;

import java.util.List;

public class TourPlanResponse {
    private List<Dates> dates ;

    public class Dates {
        public String date ;
        public String message ;

        public Dates(String _date, String _message) {
            this.date = _date;
            this.message = _message;
        }
    }

    public List<Dates> getDates() {
        return dates;
    }

    public void setDates(List<Dates> dates) {
        this.dates = dates;
    }
}
