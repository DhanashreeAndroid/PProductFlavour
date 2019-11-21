package com.salescube.healthcare.demo.view;

/**
 * Created by user on 29/01/2018.
 */

public class vIssueSubType  {

    private String issue;
    private String subIssue;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSubIssue() {
        return subIssue;
    }

    public void setSubIssue(String subIssue) {
        this.subIssue = subIssue;
    }

    @Override
    public String toString() {
        return subIssue;
    }
}
