package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vBaseSpinner;

import java.util.ArrayList;
import java.util.List;

public class AgentRepo {

    public static String createTable() {
        String qry;

        qry = "CREATE TABLE " + Agent.TABLE + "("
                + Agent.COL_AGENT_ID + " INT ,"
                + Agent.COL_AGENT_NAME+ " TEXT ,"
                + Agent.COL_SO_ID + " TEXT, "
                + Agent.COL_PRICE_CODE + " TEXT "
                + ")";

        return qry;
    }

    private void insert(Agent obj, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Agent.COL_AGENT_ID, obj.getAgentId());
        values.put(Agent.COL_AGENT_NAME, obj.getAgentName());
        values.put(Agent.COL_SO_ID, obj.getSoId());
        values.put(Agent.COL_PRICE_CODE, obj.getPriceCode());

        try {
            db.insert(Agent.TABLE, null, values);
        } catch (Exception ex) {

        }

    }

    private void insert(Agent[] objList, SQLiteDatabase db) {

        for (Agent productRate : objList) {
            insert(productRate, db);
        }
    }

    public void insert(Agent objList) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(objList, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Agent[] objList) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        insert(objList, db);

        db.setTransactionSuccessful();
        db.endTransaction();

        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll(int soId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + Agent.TABLE + " WHERE so_id=" + soId + "");
        DatabaseManager.getInstance().closeDatabase();
    }


    public List<vAgent> getAgentAll(int soId) {

        List<vAgent> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT agent_id, agent_name, price_code ";
        sql += " FROM " + Agent.TABLE;
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
        int ix_price_code = c.getColumnIndexOrThrow("price_code");

        vAgent vAgent;

        while (c.moveToNext()) {

            vAgent = new vAgent();
            vAgent.setAgentId(c.getInt(ix_id));
            vAgent.setAgentName(c.getString(ix_agentName));
            vAgent.setPriceCode(c.getString(ix_price_code));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public List<vAgent> getAgentByArea(int soId, int areaId) {

        List<vAgent> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT A.agent_id, A.agent_name, A.price_code ";
        sql += " FROM " + Agent.TABLE + " A ";
        sql += " Join " + AreaAgent.TABLE  + " AA on A." + Agent.COL_AGENT_ID + " = AA." + AreaAgent.COL_AGENT_ID ;
        sql += " WHERE A.so_Id=" + soId + "";
        sql += " And AA.area_id = " + areaId;
        sql += " And AA.so_id = " + soId;
        sql += " ORDER BY A.agent_name";

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
        int ix_price_code = c.getColumnIndexOrThrow("price_code");

        vAgent vAgent;

        while (c.moveToNext()) {

            vAgent = new vAgent();
            vAgent.setAgentId(c.getInt(ix_id));
            vAgent.setAgentName(c.getString(ix_agentName));
            vAgent.setPriceCode(c.getString(ix_price_code));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public List<vBaseSpinner> getAgentAll1(int soId) {

        List<vBaseSpinner> vAgents = new ArrayList<>();

        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT agent_id id, agent_name name";
        sql += " FROM " + Agent.TABLE;
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

        int ix_id = c.getColumnIndexOrThrow("id");
        int ix_agentName = c.getColumnIndexOrThrow("name");

        vBaseSpinner vAgent;

        while (c.moveToNext()) {

            vAgent = new vBaseSpinner();
            vAgent.setId(c.getInt(ix_id));
            vAgent.setName(c.getString(ix_agentName));
            vAgents.add(vAgent);
        }

        c.close();
        return vAgents;
    }

    public String getSelected(int id) {

        String name = "";
        String sql = "";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c;

        sql += " SELECT agent_name ";
        sql += " FROM " + Agent.TABLE;
        sql += " WHERE agent_id=" + id + "";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            return "";
        }

        if (c.getCount() == 0) {
            return "";
        }

        int ix_agentName = c.getColumnIndexOrThrow("agent_name");

        vAgent vAgent;

        while (c.moveToNext()) {
            name = c.getString(ix_agentName);
        }

        c.close();
        return name;
    }
}