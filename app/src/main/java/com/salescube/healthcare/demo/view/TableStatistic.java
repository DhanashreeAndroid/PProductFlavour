package com.salescube.healthcare.demo.view;

import java.io.Serializable;

/**
 * Created by user on 21/09/2016.
 */
public class TableStatistic implements Serializable {

    private String tableName;
    private long records;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
