package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.AgentLocality;
import com.salescube.healthcare.demo.data.model.ClosedAgent;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vLocality;
import com.salescube.healthcare.demo.view.vRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agent_Locality on 19/09/2016.
 */

public class AgentLocalityRepo extends RepoBase {

    public AgentLocalityRepo() {

    }

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + AgentLocality.TABLE + "("
                + AgentLocality.COL_ID + " PRIMARY KEY ,"
                + AgentLocality.COL_SoID + " INT ,"
                + AgentLocality.COL_AgentID + " INT ,"
                + AgentLocality.COL_AgentName + " TEXT ,"
                + AgentLocality.COL_RouteID + " INT ,"
                + AgentLocality.COL_RouteName + " TEXT ,"
                + AgentLocality.COL_RouteSeq + " INT ,"
                + AgentLocality.COL_LocalityID + " INT ,"
                + AgentLocality.COL_LocalityName + " TEXT ,"
                + AgentLocality.COL_LocalitySeq + " INT "
                + ")";

        return qry;
    }

//    public void insert(AgentLocality objAgent_Locality){
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        insert(objAgent_Locality);
//        DatabaseManager.getInstance().closeDatabase();
//    }

    private long insert(AgentLocality objAgent_Locality, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(AgentLocality.COL_SoID, objAgent_Locality.getSoId());
        values.put(AgentLocality.COL_AgentID, objAgent_Locality.getAgentId());
        values.put(AgentLocality.COL_AgentName, objAgent_Locality.getAgentName());
        values.put(AgentLocality.COL_RouteID, objAgent_Locality.getRouteId());
        values.put(AgentLocality.COL_RouteName, objAgent_Locality.getRouteName());
        values.put(AgentLocality.COL_RouteSeq, objAgent_Locality.getRouteSeq());
        values.put(AgentLocality.COL_LocalityID, objAgent_Locality.getLocalityId());
        values.put(AgentLocality.COL_LocalityName, objAgent_Locality.getLocalityName());
        values.put(AgentLocality.COL_LocalitySeq, objAgent_Locality.getLocalitySeq());

        long newRowId = db.insert(AgentLocality.TABLE, null, values);

        return newRowId;
    }

    public void  deleteAll(int soId){

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + AgentLocality.TABLE + " WHERE so_id=" + soId + "");

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(AgentLocality[] objAgent_LocalityList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.beginTransaction();
        for (AgentLocality objAgent_Locality : objAgent_LocalityList) {
            insert(objAgent_Locality, db);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public int getAllCount(int soId) {

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT count(*) count";
        sql += " FROM " + AgentLocality.TABLE;
        sql += " WHERE so_Id=" + soId + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return 0;
        }

        if (c.moveToNext()){
            return  c.getInt(0);
        }

        c.close();
        return 0;
    }

    public List<vAgent> getAgentAll(int soId) {

        List<vAgent> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT DISTINCT agent_id, agent_name ";
        sql += " FROM " + AgentLocality.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY agent_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vAgents;
        }

        if (c.getCount() == 0) {
            return vAgents;
        }

        int ix_id = c.getColumnIndexOrThrow("agent_id");
        int ix_agentName = c.getColumnIndexOrThrow("agent_name");

        vAgent vAgent;

        while (c.moveToNext()) {

            vAgent = new vAgent();
            vAgent.setAgentId(c.getInt(ix_id));
            vAgent.setAgentName(c.getString(ix_agentName));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public void updateClosedAgent(ClosedAgent[] agentList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        for(ClosedAgent agent : agentList) {

            db.execSQL("DELETE FROM " + AgentLocality.TABLE + " WHERE agent_id=" + agent.getAgentId() + "");
        }
        DatabaseManager.getInstance().closeDatabase();

    }

    public List<vRoute> getRouteAll(int soId) {

        List<vRoute> vRoutes = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT DISTINCT route_id, route_name, agent_id, so_id ";
        sql += " FROM " + AgentLocality.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY route_name";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return vRoutes;
        }

        if (c.getCount() == 0) {
            return vRoutes;
        }


        int ix_route_id = c.getColumnIndexOrThrow("route_id");
        int ix_route_name = c.getColumnIndexOrThrow("route_name");
        int ix_agent_id = c.getColumnIndexOrThrow("agent_id");
        int ix_so_id = c.getColumnIndexOrThrow("so_id");

        vRoute vRoute;
        while (c.moveToNext()) {

            vRoute = new vRoute();
            vRoute.setRouteId(c.getInt(ix_route_id));
            vRoute.setRouteName(c.getString(ix_route_name));
            vRoute.setAreaId(c.getInt(ix_agent_id));
            vRoute.setSoId(c.getInt(ix_so_id));
            vRoutes.add(vRoute);
        }

        c.close();
        return vRoutes;
    }

    public List<vLocality> getLocalityAll(int soId) {

        List<vLocality> localities = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT DISTINCT locality_id, locality_name, route_id, so_id ";
        sql += " FROM " + AgentLocality.TABLE;
        sql += " WHERE so_Id=" + soId + "";
        sql += " ORDER BY locality_name ";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return localities;
        }

        if (c.getCount() == 0) {
            return localities;
        }

        int ix_locality_id = c.getColumnIndexOrThrow("locality_id");
        int ix_locality_name = c.getColumnIndexOrThrow("locality_name");
        int ix_route_id = c.getColumnIndexOrThrow("route_id");
        int ix_so_id = c.getColumnIndexOrThrow("so_id");

        vLocality vLocality;
        while (c.moveToNext()) {

            vLocality = new vLocality();
            vLocality.setLocalityId(c.getInt(ix_locality_id));
            vLocality.setLocalityName(c.getString(ix_locality_name));
            vLocality.setRouteId(c.getInt(ix_route_id));
            vLocality.setSoId(c.getInt(ix_so_id));
            localities.add(vLocality);

        }

        c.close();
        return localities;
    }
}
