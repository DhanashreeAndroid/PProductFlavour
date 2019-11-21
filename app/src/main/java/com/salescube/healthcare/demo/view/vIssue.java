package com.salescube.healthcare.demo.view;

/**
 * Created by user on 29/01/2018.
 */

public class vIssue  {

    private String issuefFor;
    private String issue;

    public String getIssuefFor() {
        return issuefFor;
    }

    public void setIssuefFor(String issuefFor) {
        this.issuefFor = issuefFor;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return issue;
    }

}
