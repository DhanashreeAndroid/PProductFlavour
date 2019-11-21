package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 13/06/2017.
 */

public class SS {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "mst_ss";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_SS_ID = "ss_id";
    public final static String COL_SS_NAME = "ss_name";
    public  final static String COL_ROLE = "role";

    private int id;
    private int soId;
    private int ssId;
    private String ssName;
    private String role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getSsId() {
        return ssId;
    }

    public void setSsId(int ssId) {
        this.ssId = ssId;
    }

    public String getSsName() {
        return ssName;
    }

    public void setSsName(String ssName) {
        this.ssName = ssName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
