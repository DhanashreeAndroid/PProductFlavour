package com.salescube.healthcare.demo.sysctrl;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user on 06/10/2016.
 */
public class Constant {

    public static final String ACTION_AUTO_UPDATE = "com.salescube.healthcare.nyaasafoods.auto_upload";
    public static final String ACTION_AUTO_LOCATION_UPDATE = "com.salescube.healthcare.nyaasafoods.location_update";

    public static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public static final int ACTION_ID_LOCATION_LOG_ALARM = 20001;
    public static final int ACTION_ID_AUTO_UPDATE_ALARM= 20002;

    public static final String SHOP_APP_ID = "app_shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String ORDER_DATE = "order_date";
    public static final String AGENT_ID = "agent_id";
    public static final String ROUTE_ID = "route_id";
    public static final String IS_NEW = "is_new";
    public static final String LOCALITY_ID = "locality_id";

    public static final String RETURN_DATE = "return_date";

    public static final String AREA_ID = "area_id";
    public static final String IDENTITY = "identity";
    public static final String SHOP_ID = "shop_id";

    public static final String PREF_IS_REFRESH_DASHBOARD = "pref_key_is_refresh_dashboard";
    public static final String PREF_DAY_COLOR = "pref_key_day_color";
    public static final String PREF_MONTH_COLOR= "pref_key_month_color";

    public static class ComplaintBy {
        public final static String AGENT = "Agent";
        public final static String SS = "SS";
        public final static String SHOP = "Shop";
        public final static String CUSTOMER = "Customer";
    }

    public static class PlaceType {
        public final static String AGENT = "Agent";
        public final static String SS = "SS";
        public final static String HOME = "Home";
    }

    public static class Activity {
        public final static String GPS = "GPS";
        public final static String DEVICE = "DEVICE";
        public final static String INTERNET = "INTERNET";
    }

    public final static class UpdateType {

        public final static String EMPLOYEE = "EMPLOYEE";
        public final static String AGENT = "AGENT";
        public final static String SHOP = "SHOP";
        public final static String PRODUCT = "PRODUCT";
        public final static String POP = "POP";
        public final static String OTHER_WORK_REASON = "OTHER_WORK_REASON";
        public final static String SHOP_TYPE = "SHOP_TYPE";
        public final static String AGENT_NEW = "AGENT_NEW";
        public final static String PRICE_LIST = "PRICE_LIST";
        public final static String LAST_ORDERS = "LAST_ORDERS";
        public final static String SS = "SS";
        public final static String COMPLAINT_TYPE = "COMPLAINT_TYPE";

        public final static String AREA = "AREA";
        public final static String ROUTE = "ROUTE";
        public final static String LOCALITY = "LOCALITY";
        public final static String AREA_AGENT = "AREA_AGENT";

    }

    public static class AppEvent{
        public static String POP = "POP";
        public static String NO_ORDER = "NO_ORDER";
        public static String ORDER = "ORDER";
        public static String COMPETITOR = "COMPETITOR";
    }

    public static class ShopStatus {
        public static String LIVE = "Live";
        public static String CLOSE = "Close";
        public static String NAME_CHANGE = "Name Change";
        public static String DUPLICATE = "Duplicate";
    }

    public static class MessageType {
        public static final int Toast = 0;
        public static final int Alert = 1;
    }

    public static  class  LeaveDuration {
        public static final String FULL_DAY = "Full Day";
        public static final String FIRST_HALF = "First Half";
        public static final String SECOND_HALF = "Second Half";
    }

    public static  class  LeaveType {
        public static final String CL = "CL";
        public static final String PL = "PL";
        public static final String SL = "SL";
        public static final String CO = "CO";
        public static final String WP = "WP";
    }

    public static  class  WorkType {
        public static final String RETAILING = "Retailing";
        public static final String OTHER = "Other";
        public static final String LEAVE = "Leave";
    }
    public static  class  ShopRank {
        public static final String CLASS_A = "Class A";
        public static final String CLASS_B = "Class B";
        public static final String CLASS_C = "Class C";
        public static final String SELECT = "--Select Rank--";
    }

    public static  class  WhatMsg {
        public static final int SUCCESS = 1;
        public static final int ABORT = 2;
        public static final int ERROR = 3;
    }

    public static  class  TourPlanSets {
        public static final String WORKING = "Working";
        public static final String OTHER = "Other";
        public static final String LEAVE = "Leave";
        public static final String HOLIDAY = "Holiday/Weekly Off";
    }

}
