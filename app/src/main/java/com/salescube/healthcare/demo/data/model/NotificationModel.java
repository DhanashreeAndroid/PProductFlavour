package com.salescube.healthcare.demo.data.model;

public class NotificationModel {

    public static final String TABLE = "notifications";

    public static final String COL_ID  = "id";
    public static final String COL_TITLE  = "title";
    public static final String COL_DESCRIPTION  = "description";
    public static final String COL_READ_STATUS  = "read_satus";
    public static final String COL_DATE  = "date";
    public static final String COL_EMP_ID  = "emp_id";


    private int id;
    private String title;
    private String description;
    private int readStatus;
    private int soId;
    private String date;



    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
