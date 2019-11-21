package com.salescube.healthcare.demo.view;

/**
 * Created by user on 03/11/2016.
 */

public class vOtherWorkReason {
    private int id;
    private String reason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return reason;
    }

}
