package com.salescube.healthcare.demo.data.model;

public class Agent {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "ms_agent";

    public final static String COL_ID = "id";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_AGENT_NAME = "agent_name";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_PRICE_CODE = "price_code";

    private int agentId;
    private String agentName;
    private int soId;
    private String priceCode;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }


    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }
}
