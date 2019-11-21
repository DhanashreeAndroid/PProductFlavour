package com.salescube.healthcare.demo.func;

import java.text.DecimalFormat;

/**
 * Created by user on 21/10/2016.
 */

public class Parse {

    public static int toInt(Object object){

        int result;
        try {
            result = Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
            result =0 ;

        }

        return  result;
    }

    public static double toDbl(Object object){

        double result;
        try {
            result = Double.parseDouble(object.toString());
        } catch (NumberFormatException e) {
            result =0 ;

        }

        return  result;
    }

    public static String toStr(Object object){

        if (object == null){
            return "";
        }

        if (object.toString() == null){
            return "";
        }

        return  object.toString();
    }

    public  static String ruppe(double amount, boolean addSymbol) {
        if (addSymbol) {
            return  "₹" +new DecimalFormat("##,##,##0").format(amount);
        }else {
            return  new DecimalFormat("##,##,##0").format(amount);
        }

    }

    public  static String ruppe(double amount) {
        return  ruppe(amount, false);
    }

}
