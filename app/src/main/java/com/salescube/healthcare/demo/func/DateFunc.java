package com.salescube.healthcare.demo.func;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 20/10/2016.
 */

public class DateFunc {

    public static String getDateTimeStr() {
        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(date);
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateTimeFormat.format(date);
    }

    public static String getDayAndDateStr() {
        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy");
        return dateTimeFormat.format(date);
    }

    public static String getTimeStr() {
        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("h:mm a");
        return dateTimeFormat.format(date);
    }

    public static String getDateTimeStr(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(date);
    }

    public static Date getTodaysDate() {

        Date date = new Date();
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = dateTimeFormat.parse(dateTimeFormat.format(date));
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    public static Date getTodaysDateTime() {

        Date date = new Date();
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = dateTimeFormat.parse(dateTimeFormat.format(date));
        } catch (ParseException e) {
            return null;
        }

        return date;
    }


    public static String getDateStr(Date date, String format) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(format);
        return dateTimeFormat.format(date);
    }

    public static String getDateStr(String format) {
        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(format);
        return dateTimeFormat.format(date);
    }

    public static String getDateStrSimple(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateTimeFormat.format(date);
    }

    public static String getDateStr() {
        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(date);
    }

    public static Date getDateTime(String dateStr) {

        Date date = null;
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = dateTimeFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }

        return date;
    }

    public static Date getDate(String dateStr) {

        if (dateStr == null) {
            return null;
        }

        Date date = null;
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = dateTimeFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }

        return date;
    }

    public static Date getDate(String dateStr, SimpleDateFormat dateTimeFormat) {

        Date date = null;

        try {
            date = dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    public static Date getDate(String dateStr, String sourceFormat) {

        Date date = null;

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(sourceFormat);

        try {
            date = dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    public static Date getDate() {

        Date date = new Date();
        return date;
    }

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }
    public static Date prevMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, (months));
        return c.getTime();
    }

    public static boolean isSameDate(Date date1, Date date2) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        try {
            date1 = df.parse(df.format(date1));
            date2 = df.parse(df.format(date2));
        } catch (ParseException e) {
            return false;
        }

        return date1.equals(date2);

    }

    public static Date FO_Month(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);

        return cal.getTime();
    }

    public static String FO_MonthStr(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(cal.getTime());
    }

    public static Date EO_Month(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    public static String EO_MonthStr(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(cal.getTime());
    }

    public static Date setDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        return cal.getTime();
    }


    public static Date getTodayMin() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(getTodaysDate());
        
        return cal.getTime();
    }
}
