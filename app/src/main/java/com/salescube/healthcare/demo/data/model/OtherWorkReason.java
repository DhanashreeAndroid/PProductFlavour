package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 03/11/2016.
 */

public class OtherWorkReason {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "ms_work_reason";

    public final static String COL_ID = "id";
    public final static String COL_REASON = "reason";
    public final static String COL_SEQ = "seq";

    private int id;
    private String reason;
    private int seq;

    public OtherWorkReason() {}

    public OtherWorkReason(int _id, String _reason) {
        this.id = _id;
        this.reason = _reason;
    }

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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}