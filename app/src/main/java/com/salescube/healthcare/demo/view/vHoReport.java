package com.salescube.healthcare.demo.view;

/**
 * Created by user on 18/03/2017.
 */

public class vHoReport {

    private String report;
    private String status;
    private int recordCount;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public vHoReport(String _reportName, String _status, int _recordCount){
        this.report = _reportName;
        this.status = _status;
        this.recordCount = _recordCount;
    }
}
