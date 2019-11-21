package com.salescube.healthcare.demo.data.model;

public class SOOtherWork {

    private int soId;
    private String soName;
    private String soWork;
    private String remark;
    private String agentName;

    public SOOtherWork() {
    }

    public SOOtherWork(int soId, String soName) {
        this.soId = soId;
        this.soName = soName;
    }

    public SOOtherWork(int soId, String soName,  String remark, String agentName) {
        this.soId = soId;
        this.soName = soName;
        this.remark = remark;
        agentName = agentName;
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

    public String getSoWork() {
        return soWork;
    }

    public void setSoWork(String soWork) {
        this.soWork = soWork;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        agentName = agentName;
    }

    @Override
    public String toString() {
        return getSoName();
    }


}
