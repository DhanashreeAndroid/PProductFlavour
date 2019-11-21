package com.salescube.healthcare.demo.sysctrl;

public class MsgBundle {

    public static final int SUCCESS = 1;
    public static final int ABORT = 2;
    public static final int ERROR = 3;
    public static final int CONNECTION_ERROR = 4;

    public static final int COMPLETED = 4;

    private String title;
    private String message;
    private boolean showAlert;
    private boolean silent;

    public MsgBundle(String title, String message) {
        this.title = title;
        this.message = message;
        this.silent = false;
        this.showAlert = true;
    }

    public MsgBundle(String title, String message, boolean showAlert) {
        this.title = title;
        this.message = message;
        this.silent = false;
        this.showAlert = showAlert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
