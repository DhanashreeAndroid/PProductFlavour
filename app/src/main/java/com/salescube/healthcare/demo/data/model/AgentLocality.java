package com.salescube.healthcare.demo.data.model;

/**
 * Created by user on 19/09/2016.
 */

public class AgentLocality {

    public  static  final String TAG = AgentLocality.class.getSimpleName();
    public static final String TABLE = "ms_agent_locality";

    public static final String COL_ID = "id";
    public static final String COL_SoID = "so_id";
    public static final String COL_AgentID = "agent_id";
    public static final String COL_AgentName = "agent_name";
    public static final String COL_RouteID = "route_id";
    public static final String COL_RouteName = "route_name";
    public static final String COL_RouteSeq = "route_seq";
    public static final String COL_LocalityID = "locality_id";
    public static final String COL_LocalityName = "locality_name";
    public static final String COL_LocalitySeq = "locality_seq";

    private int id;
    private int soId;
    private int agentId;
    private String agentName;
    private int routeId;
    private String routeName;
    private int routeSeq;
    private int localityId;
    private String localityName;
    private int localitySeq;

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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getRouteSeq() {
        return routeSeq;
    }

    public void setRouteSeq(int routeSeq) {
        this.routeSeq = routeSeq;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public int getLocalitySeq() {
        return localitySeq;
    }

    public void setLocalitySeq(int localitySeq) {
        this.localitySeq = localitySeq;
    }


}
