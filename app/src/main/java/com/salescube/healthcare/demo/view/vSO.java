package com.salescube.healthcare.demo.view;

public class vSO {
    private int soId;
    private String soName;

    public vSO() {
    }

    public vSO(int soId, String soName) {
        this.soId = soId;
        this.soName = soName;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    @Override
    public String toString() {
        return getSoName();
    }
}
