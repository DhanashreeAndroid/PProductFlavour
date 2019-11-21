package com.salescube.healthcare.demo.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 17/10/2016.
 */

public class vSysDate {

    private Date trDate;

    public Date getTrDate() {
        return trDate;
    }

    public void setTrDate(Date trDate) {
        this.trDate = trDate;
    }

    @Override
    public String toString() {

        DateFormat objDf = new SimpleDateFormat("dd/MM/yyyy");
        return objDf.format(trDate);
    }
}
