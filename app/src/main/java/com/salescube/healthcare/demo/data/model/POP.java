package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 20/10/2016.
 */

public class POP {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "ms_pop";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_POP_ID = "pop_id";
    public final static String COL_POP_NAME = "pop_name";
    public final static String COL_DIVISION_ID = "division_id";


    private int soId;
    private int popId;
    private String popName;
    private int divisionId;

    public POP(){

    }

    public POP(int _popId, String _popName){
        popId = _popId;
        popName = _popName;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getPopId() {
        return popId;
    }

    public void setPopId(int popId) {
        this.popId = popId;
    }

    public String getPopName() {
        return popName;
    }

    public void setPopName(String popName) {
        this.popName = popName;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }


    @Override
    public String toString() {
        return popName;
    }
}
