package com.salescube.healthcare.demo.view;

import java.util.Date;

/**
 * Created by user on 22/04/2017.
 */

public class vOtherWorkReport {

    private Date txnDate;
    private String agent;
    private String work;

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

}
