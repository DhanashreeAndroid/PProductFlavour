package com.salescube.healthcare.demo.data.model;

public class AreaAgent {

    public final static String TABLE = "rl_area_agent";

    public final static String COL_AREA_ID = "area_id";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_SO_ID = "so_id";

    private int areaId;
    private int agentId;
    private int soId;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }
}
