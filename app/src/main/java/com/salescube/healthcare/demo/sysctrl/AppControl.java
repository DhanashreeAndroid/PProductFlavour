package com.salescube.healthcare.demo.sysctrl;

import android.content.Context;
import android.widget.Toast;

import com.salescube.healthcare.demo.app.App;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.MobileInfo;

import java.util.Date;

/**
 * Created by user on 26/08/2016.
 */

public class AppControl {

    private static String userName;
    private static int employeeId;
    private static String divisionId;
    private static Date todayDate;
    private static String employeeName;
    private static String imei;
    private static String appRole;
    private static int mgrId;


    public AppControl() {

    }

    private static AppControl instance;

    public static String getUserName() {
        return userName;
    }

    public static int getmEmployeeId() {
        return employeeId;
    }

    public static String getEmployeeName() {
        return employeeName;
    }

    public static String getDivisionId() {
        return divisionId;
    }

    public static Date getTodayDate() {
        return todayDate;
    }

    public static String getImei(){
        return imei;
    }
    public static String getAppRole() {
        return appRole;
    }

    public static int getMgrId() {
        return mgrId;
    }

    public static void initControl(Context context, String pUserName, String pPassword) {


        UserRepo objUserData = new UserRepo();
        User objUser = objUserData.getUser(pUserName, pPassword);

        if (objUser == null) {
            return;
        }

        userName = objUser.getUserName();
        employeeId = objUser.getEmployeeId();
        divisionId = objUser.getDivisionId();
        employeeName = objUser.getEmployeeName();
        imei = MobileInfo.getIMEI();
        appRole = objUser.getAppRole();
        mgrId = objUser.getMgrId();


        refreshDate();
    }

    private static void initUser(User objUser) {


    }

    public static void refreshDate() {
        SysDateRepo objSysDate = new SysDateRepo();

        try {
            todayDate = objSysDate.getMaxDate(employeeId);
        } catch (Exception e) {
            //new AuthUpdate(App.getContext(), null).execute();
            //refreshDate();
            Toast.makeText(App.getContext(),"Error! Problem with app date, please contact to Pitambari IT Department.",Toast.LENGTH_SHORT).show();
        }
    }

}
