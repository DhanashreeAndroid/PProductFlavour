package com.salescube.healthcare.demo.view;

/**
 * Created by user on 19/09/2016.
 */

public class vAgent {

    private int agentId;
    private String agentName;
    private int routeId;
    private String priceCode;

    public vAgent(){

    }

    public vAgent(int _AgentId, String _AgentName){
        agentId = _AgentId;
        agentName = _AgentName;
    }

    public int  getAgentId() {
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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    @Override
    public String toString() {
        return agentName;
    }
}
