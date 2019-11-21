package com.salescube.healthcare.demo.sysctrl;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.salescube.healthcare.demo.ctrl.TSAdapter;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.EmployeeRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkReasonRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.SSRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.ShopTypeRepo;
import com.salescube.healthcare.demo.data.repo.ShopWiseOrderRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vEmployee;
import com.salescube.healthcare.demo.view.vLookup;
import com.salescube.healthcare.demo.view.vMonthYear;
import com.salescube.healthcare.demo.view.vOtherWorkReason;
import com.salescube.healthcare.demo.view.vProduct;
import com.salescube.healthcare.demo.view.vSS;
import com.salescube.healthcare.demo.view.vShop;
import com.salescube.healthcare.demo.view.vShopType;
import com.salescube.healthcare.demo.view.vShopWiseOrder;
import com.salescube.healthcare.demo.view.vSysDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 03/11/2016.
 */

public class SpinnerHelper {

    public static void FillAgents(Spinner spn, int soId, String defaultValue){

//        AgentLocalityRepo repo = new AgentLocalityRepo();
//        List<vAgent> lstAgent = repo.getAgentAll(soId) ;

        AgentRepo repo = new AgentRepo();
        List<vAgent> lstAgent = repo.getAgentAll(soId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            lstAgent.add(0, new vAgent(0,defaultValue));
        }

        ArrayAdapter<vAgent> adp = new ArrayAdapter<vAgent>(spn.getContext(), android.R.layout.simple_spinner_item, lstAgent);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }



    public static void FillShopWiseSO(Spinner spn){

        ShopWiseOrderRepo objRepo = new ShopWiseOrderRepo();
        List<vShopWiseOrder> objList = objRepo.getSONames();

        ArrayAdapter<vShopWiseOrder> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }


    public static void FillSS(Spinner spn, int useId, String defaultValue){

        SSRepo objRepo = new SSRepo();
        List<vSS> objList = objRepo.getSSList(useId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            objList.add(0, new vSS(0, defaultValue));
        }

        ArrayAdapter<vSS> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }

    public static void FillShops(Spinner spn, int soId, String defaultValue){

        ShopRepo repo = new ShopRepo();
        List<vShop> lstAgent = repo.getShopsAll(soId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            lstAgent.add(0, new vShop(0,defaultValue));
        }

        ArrayAdapter<vShop> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, lstAgent);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }

    public static void FillShopsExlude(Spinner spn, int soId, String defaultValue, int localityId, int exludeShopId){

        ShopRepo repo = new ShopRepo();
        List<vShop> lstAgent = repo.getShopsAll(soId,localityId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            lstAgent.add(0, new vShop(0,defaultValue));
        }

        if (exludeShopId != 0) {
            int removeIndex = 0;
            for (vShop shop : lstAgent) {
                if (shop.getShopId() == exludeShopId) {
                    break;
                }
                removeIndex += 1;
            }
            if (removeIndex < lstAgent.size() ) {
                lstAgent.remove(removeIndex);
            }
        }

        ArrayAdapter<vShop> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, lstAgent);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }

    public static void FillSysDates(Spinner spn, int soId){
        SysDateRepo repo = new SysDateRepo();

        List<vSysDate> lstSysDate = repo.getDates(soId);
        ArrayAdapter<vSysDate> adpSysDate = new ArrayAdapter<vSysDate>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillOrderDates(Spinner spn, int soId){
        SalesOrderRepo repo = new SalesOrderRepo();

        List<Date> lstSysDate = repo.getOrderDates(soId);
        ArrayAdapter<Date> adpSysDate = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillOtherWorkReason(Spinner spn){
        OtherWorkReasonRepo repo = new OtherWorkReasonRepo();

        List<vOtherWorkReason> lstSysDate = repo.getReasonList();
        ArrayAdapter<vOtherWorkReason> adpSysDate = new ArrayAdapter<vOtherWorkReason>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillProducts(Spinner spn){
        ProductRepo repo = new ProductRepo();

        List<vProduct> lstSysDate = repo.getProductsAll(AppControl.getmEmployeeId());

        ArrayAdapter<vProduct> adpSysDate = new ArrayAdapter<vProduct>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillLeaveDuration(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.LeaveDuration.FULL_DAY);
        lstSysDate.add(Constant.LeaveDuration.FIRST_HALF);
        lstSysDate.add(Constant.LeaveDuration.SECOND_HALF);

        ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillPlaceType(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.PlaceType.AGENT);
        lstSysDate.add(Constant.PlaceType.SS);
        //lstSysDate.add(Constant.PlaceType.HOME);

        ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillLeaveType(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.LeaveType.CL);
        lstSysDate.add(Constant.LeaveType.PL);
        lstSysDate.add(Constant.LeaveType.SL);
        lstSysDate.add(Constant.LeaveType.CO);
        lstSysDate.add(Constant.LeaveType.WP);

        ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

//    public static void FillMonthYear(Spinner spn){
//
//        List<vLookup> lstSysDate = new ArrayList<vLookup>();
//
//        Date date1 = DateFunc.getDate();
////        Date date2 = DateFunc.addMonths(date1, -1 );
////        Date date3 = DateFunc.addMonths(date2,- 1 );
//
////        lstSysDate.add(new vLookup(DateFunc.getDateStr(date3, "yyyyMM"),DateFunc.getDateStr(date3,"MMMM,yyyy") ));
////        lstSysDate.add(new vLookup(DateFunc.getDateStr(date2, "yyyyMM"),DateFunc.getDateStr(date2,"MMMM,yyyy") ));
//        lstSysDate.add(new vLookup(DateFunc.getDateStr(date1, "yyyyMM"),DateFunc.getDateStr(date1,"MMMM,yyyy") ));
//
//        ArrayAdapter<vLookup> adpSysDate = new ArrayAdapter<vLookup>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
//        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn.setAdapter(adpSysDate);
//    }

    public static void FillShopStatus(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.ShopStatus.LIVE);
        lstSysDate.add(Constant.ShopStatus.CLOSE);
        lstSysDate.add(Constant.ShopStatus.NAME_CHANGE);
        lstSysDate.add(Constant.ShopStatus.DUPLICATE);

        ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillReportedBy(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.ComplaintBy.CUSTOMER);
        lstSysDate.add(Constant.ComplaintBy.SHOP);
        lstSysDate.add(Constant.ComplaintBy.SS);
        lstSysDate.add(Constant.ComplaintBy.AGENT);

        TSAdapter adpSysDate = new TSAdapter(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }


    public static void FillShopType(Spinner spn){
        ShopTypeRepo repo = new ShopTypeRepo();

        List<vShopType> lstSysDate = repo.getReasonList();

        ArrayAdapter<vShopType> adpSysDate = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillShopRank(Spinner spn){

        List<String> lstSysDate = new ArrayList<String>();
        lstSysDate.add(Constant.ShopRank.SELECT);
        lstSysDate.add(Constant.ShopRank.CLASS_A);
        lstSysDate.add(Constant.ShopRank.CLASS_B);
        lstSysDate.add(Constant.ShopRank.CLASS_C);

        ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillWorkType(Spinner spn){

            List<String> lstSysDate = new ArrayList<String>();
            lstSysDate.add(Constant.WorkType.RETAILING);
            lstSysDate.add(Constant.WorkType.OTHER);
            lstSysDate.add(Constant.WorkType.LEAVE);

            ArrayAdapter<String> adpSysDate = new ArrayAdapter<String>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
            adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn.setAdapter(adpSysDate);
        }

    public static void FillASM(Spinner spn, int useId, String defaultValue) {

        EmployeeRepo objRepo = new EmployeeRepo();
        List<vEmployee> objList = objRepo.getASM(useId);

        if (!TextUtils.isEmpty(defaultValue)) {
            objList.add(0, new vEmployee(0, defaultValue));
        }

        ArrayAdapter<vEmployee> adp = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adp);
    }

    public static void FillStockMonthYear(Spinner spn){

        List<vLookup> lstSysDate = new ArrayList<vLookup>();

        Date date1 = DateFunc.getDate();
//        Date date2 = DateFunc.addMonths(date1, -1 );
//        Date date3 = DateFunc.addMonths(date2,- 1 );

//        lstSysDate.add(new vLookup(DateFunc.getDateStr(date3, "yyyyMM"),DateFunc.getDateStr(date3,"MMMM,yyyy") ));
//        lstSysDate.add(new vLookup(DateFunc.getDateStr(date2, "yyyyMM"),DateFunc.getDateStr(date2,"MMMM,yyyy") ));
        lstSysDate.add(new vLookup(DateFunc.getDateStr(date1, "yyyyMM"),DateFunc.getDateStr(date1,"MMMM,yyyy") ));

        ArrayAdapter<vLookup> adpSysDate = new ArrayAdapter<vLookup>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

    public static void FillMonthYear(Spinner spn,int next, int prev){

        SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy");

        List<vMonthYear> lstSysDate = new ArrayList<>();
        Date tmpDate = DateFunc.getDate();

        // next 2
        // MAY, APRIL
        // prev 3
        // MAR,FEB,JAN

        for (int i = 0 ; i < next  ; i++) {
            tmpDate =  DateFunc.addMonths(tmpDate,1);
            lstSysDate.add(new vMonthYear(format.format(tmpDate), DateFunc.FO_MonthStr(tmpDate),DateFunc.EO_MonthStr(tmpDate)));
        }

        tmpDate = DateFunc.getDate();
        lstSysDate.add(new vMonthYear(format.format(tmpDate), DateFunc.FO_MonthStr(tmpDate),DateFunc.EO_MonthStr(tmpDate)));

        for(int i = prev ; i > 0 ; i--){
            tmpDate =  DateFunc.prevMonths(tmpDate,-1);
            lstSysDate.add(new vMonthYear(format.format(tmpDate), DateFunc.FO_MonthStr(tmpDate),DateFunc.EO_MonthStr(tmpDate)));
        }


        ArrayAdapter adpSysDate = new ArrayAdapter<>(spn.getContext(), android.R.layout.simple_spinner_item, lstSysDate);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adpSysDate);
    }

}
