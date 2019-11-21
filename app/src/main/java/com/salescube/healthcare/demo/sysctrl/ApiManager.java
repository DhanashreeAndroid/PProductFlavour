package com.salescube.healthcare.demo.sysctrl;

import com.salescube.healthcare.demo.func.DateFunc;

import java.util.Date;

/**
 * Created by user on 12/09/2016.
 */

public class ApiManager {

    //public final static String apiMainPath = "https://demo-b2c-ws.pitambaridigitalcare.com/api/";
   // public final static String apiMainPath = "https://maharashtratea-ws.pitambaridigitalcare.in/api/";
    //public final static String apiMainPath = "https://phondaghat-ws.pitambaridigitalcare.in/api/";
    //public final static String apiMainPath = "https://nyasafood-ws.pitambaridigitalcare.in/api/";
   // public final static String apiMainPath = "https://frolicfood-ws.pitambaridigitalcare.in/api/";
   // public final static String apiMainPath = "https://kawre-ws.pitambaridigitalcare.in/api/";
    public final static String apiMainPath = "http://app.pitambari.com:379/demo-fmcg-ws/api/";

    public static class Employee {
        private static final String mController = "Employee";

        public static String getSOByManagerId(int mgrId) {
            String apiUrl = BuildQry(mController, "getSoByEmployee", new Object[]{
                    "mgrId=" + String.valueOf(mgrId)
            });
            return apiUrl;
        }
    }

    public static class User {
        private static final String mController = "User";

        public static String login(String userName, String password) {

            String url = BuildQry(mController, "login", new Object[]{
                    "userName=" + userName,
                    "password=" + password,
                    "imeiNo=" + ""
            });

            return url;
        }

        public static String getSysDate(Integer soId) {

            String apiUrl = BuildQry(mController, "getSysDates", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }

        public static String getHasMasterUpdate(Integer soId) {

            String apiUrl = BuildQry(mController, "MasterUpdate", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }

        public static String lockMasterUpdate(Integer soId) {

            String apiUrl = BuildQry(mController, "LockMasterUpdate", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }
    }

    public static class SOReport {
        private static final String mController = "ManagerReport";

        public static String getDayReport(int mgrId) {
            String apiUrl = BuildQry(mController, "getSODayReport", new Object[]{
                    "mgrId=" + String.valueOf(mgrId)
            });

            return apiUrl;
        }

        public static String getMonthReport(int mgrId, String fromDate, String toDate) {
            String apiUrl = BuildQry(mController, "getSOMonthReport", new Object[]{
                    "mgrId=" + String.valueOf(mgrId),
                    "fromDate=" + fromDate,
                    "toDate=" + toDate
            });

            return apiUrl;
        }
    }

    public static class Area {
        private static final String mController = "Area";

        public static String getAreas(Integer agentId) {

            String apiUrl = BuildQry(mController, "getAreaList", new Object[]{
                    "SoId=" + agentId.toString()
            });

            return apiUrl;
        }
    }

    public static class Route {
        private static final String mController = "Route";

        public static String getRoute(Integer soId) {

            String apiUrl = BuildQry(mController, "getRoutes", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }
    }

    public static class Locality {
        private static final String mController = "Locality";

        public static String getLocality(Integer soId) {

            String apiUrl = BuildQry(mController, "getLocalality", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }
    }


    public static class AreaAgent {
        private static final String mController = "AreaAgent";

        public static String getRLAreaAgent(Integer soId) {

            String apiUrl = BuildQry(mController, "getAreaAgent", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }
    }


    public static class Agent {
        private static final String mController = "Agent";

        public static String agentsBySo(Integer agentId) {

            String apiUrl = BuildQry(mController, "getAll", new Object[]{
                    "soid=" + agentId.toString()
            });

            return apiUrl;
        }

        public static String closedAgent(Integer soId) {

            String apiUrl = BuildQry(mController, "getClosedAgent", new Object[]{
                    "soid=" + soId.toString()
            });

            return apiUrl;
        }

        public static String updateCloseAgentStatus(Integer soId) {

            String apiUrl = BuildQry(mController, "UpdateCloseAgentStatus", new Object[]{
                    "soid=" + soId.toString()
            });

            return apiUrl;
        }

        public static String allList(Integer soId) {
            String apiUrl = BuildQry(mController, "getAllAgents", new Object[]{
                    "soId=" + soId.toString()
            });
            return apiUrl;
        }

    }

    public static class Order {
        private static final String mController = "Order";

        public static String postOrders() {
            String apiUrl = BuildQry(mController, "postOrders", null);
            return apiUrl;
        }

        public static String postColdCalls() {
            String apiUrl = BuildQry(mController, "postColdCalls", null);
            return apiUrl;
        }

        public static String getLastOrdersBySo(int soId) {

            String apiUrl = BuildQry(mController, "getLastOrdersBySo", new Object[]{
                    "soId=" + soId
            });

            return apiUrl;
        }

        public static String postSalesReturn() {
            String apiUrl = BuildQry(mController, "postSalesReturn", null);
            return apiUrl;
        }

        public static String getTodayOrderSO(int soId) {
            String apiUrl = BuildQry(mController, "getTodayOrderSO", new Object[]{
                    "soID=" + String.valueOf(soId)
            });
            return apiUrl;
        }

    }

    public static class Shop {
        private static final String mController = "Shop";

        public static String shopsBySo(Integer soOd) {

            String apiUrl = BuildQry(mController, "getAll", new Object[]{
                    "soid=" + soOd.toString()
            });
            return apiUrl;
        }

        public static String getShopUpdates(Integer soId) {

            String apiUrl = BuildQry(mController, "GetUpdates", new Object[]{
                    "soId=" + soId.toString()
            });
            return apiUrl;
        }

        public static String postShops() {

            String apiUrl = BuildQry(mController, "update", null);
            return apiUrl;
        }

        public static String updateShopChangeStatus(Integer soId) {

            String apiUrl = BuildQry(mController, "UpdateShopChangeStatus", new Object[]{
                    "soId=" + soId.toString()
            });
            return apiUrl;
        }

    }

    public static class POP {
        private static final String mController = "POP";

        public static String getPOP(String divisionIds) {

            String apiUrl = BuildQry(mController, "getPOPList", new Object[]{
                    "divisionIds=" + divisionIds
            });
            return apiUrl;
        }

        public static String getPOPBySO(int soId) {

            String apiUrl = BuildQry(mController, "getPOPListBySo", new Object[]{
                    "soId=" + soId
            });
            return apiUrl;
        }

        public static String postPOP() {

            String apiUrl = BuildQry(mController, "PostPOP", null);
            return apiUrl;
        }

        public static String postPOPWithImage() {

            String apiUrl = BuildQry(mController, "PostPOPWithImage", null);
            return apiUrl;
        }

    }

    public static class Target {
        private final static String mController = "Target";

        public static String targetAndFocus(Integer soId, Date date) {

            String apiUrl = BuildQry(mController, "getTarget", new Object[]{
                    "soId=" + soId.toString(),
                    "orderDate=" + DateFunc.getDateStr(date)
            });
            return apiUrl;
        }

    }

    public static class OtherWork {
        private final static String mController = "OtherWork";

        public static String postOtherWork() {

            String apiUrl = BuildQry(mController, "otherWork", null);
            return apiUrl;
        }

        public static String getOtherWorkReason() {

            String apiUrl = BuildQry(mController, "getWorkReasonList", null);
            return apiUrl;
        }
    }

    public static class Note {
        private static final String mController = "Note";

        public static String getNotes(Integer soId) {

            String apiUrl = BuildQry(mController, "getNotes", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }
    }

    public static class Location {
        private static final String mController = "Location";

        public static String postLocationLog() {

            String apiUrl = BuildQry(mController, "log", null);
            return apiUrl;
        }

    }

    public static class ActivityLog {
        private static final String mController = "ActivityLog";

        public static String postActivityLog() {

            String apiUrl = BuildQry(mController, "log", null);
            return apiUrl;
        }
    }

    public static class Leave {
        private static final String mController = "Leave";

        public static String apply() {

            String apiUrl = BuildQry(mController, "apply", null);
            return apiUrl;
        }
    }

    public static class CompetitorInfo {
        private static final String mController = "CompetitorInfo";

        public static String postInfo() {

            String apiUrl = BuildQry(mController, "PostInfo", null);
            return apiUrl;
        }

        public static String postWithImage() {

            String apiUrl = BuildQry(mController, "PostInfoWithImage", null);
            return apiUrl;
        }

    }

    public static class ShopType {
        private static final String mController = "ShopType";

        public static String getAll() {
            String apiUrl = BuildQry(mController, "GetAllShopType", null);
            return apiUrl;
        }
    }

    public static class Attendance {
        private static final String mController = "Attendance";

        public static String mark() {
            String apiUrl = BuildQry(mController, "MarkAttendance", null);
            return apiUrl;
        }
    }

    public static class Update {

        public static String getDownloadCount(Integer soId, String divisionIds) {

            String apiUrl = BuildQry("Update", "getDownloadCount", new Object[]{
                    "soId=" + soId.toString(),
                    "divisionIds=" + divisionIds
            });
            return apiUrl;
        }

        public static String getDownloadDetails(Integer soId, String divisionIds) {

            String apiUrl = BuildQry("Update", "getDownloadDetails", new Object[]{
                    "soId=" + soId.toString(),
                    "divisionIds=" + divisionIds
            });
            return apiUrl;
        }

    }

    public static String getVersionUpdate(Integer soId, String versionCode) {

        String apiUrl = BuildQry("System", "HasVersionUpdate", new Object[]{
                "soId=" + soId.toString(),
                "versionCode=" + versionCode
        });

        return apiUrl;
    }

    public static String getCheckNewVersion(Integer soId, String versionCode) {

        String apiUrl = BuildQry("System", "CheckNewVersion", new Object[]{
                "soId=" + soId.toString(),
                "versionCode=" + versionCode
        });

        return apiUrl;
    }

    public static String lockDailyUpdate(Integer soId) {

        String apiUrl = BuildQry("Update", "LockDailyUpdate", new Object[]{
                "soId=" + soId.toString()
        });

        return apiUrl;
    }

    private static String BuildQry(String controller, String action, Object[] paras) {

        String api = apiMainPath;
        String paraString = "";

        if (paras != null) {
            for (Object para : paras) {
                if (paraString != "") {
                    paraString += "&";
                }
                paraString += para.toString();
            }
        }

//        String paraString = Arrays.toString(paras);
//        paraString = paraString.replace("[", "");
//        paraString = paraString.replace("]", "");F
//        paraString = paraString.replace(" ", "");
//        paraString = paraString.replace(",", "&");

        String url = String.format("%s%s/%s?%s", api, controller, action, paraString);
        return url;
    }

    public static class Product {
        private static final String mController = "Product";

        public static String getAll(String divisionId) {

            String apiUrl = BuildQry(mController, "getProducts", new Object[]{
                    "divisionId=" + divisionId
            });

            return apiUrl;
        }

        public static String priceList(Integer soId) {

            String apiUrl = BuildQry(mController, "getPriceList", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }

        public static String productBySo(Integer soId) {

            String apiUrl = BuildQry(mController, "getProductsBySo", new Object[]{
                    "soId=" + soId.toString()
            });

            return apiUrl;
        }


    }

    public static class Stock {
        private static final String mController = "Stock";

        public static String getStock(Integer ssId) {

            String apiUrl = BuildQry(mController, "getStock", new Object[]{
                    "shopId=" + ssId.toString()
            });
            return apiUrl;
        }

        public static String postStock() {
            String apiUrl = BuildQry(mController, "postStock", null);
            return apiUrl;
        }
    }

    public static class MyPlace {
        private static final String mController = "MyPlace";

        public static String postMyPlace() {
            String apiUrl = BuildQry(mController, "saveMyPlace", null);
            return apiUrl;
        }
    }

    public static class SS {
        private static final String mController = "SS";

        public static String getSS(int mgrId) {
            String apiUrl = BuildQry(mController, "getSS", new Object[]{
                    "mgrId=" + String.valueOf(mgrId)
            });
            return apiUrl;
        }

    }

    public static class ProductTarget {
        private static final String mController = "ShopProductTarget";

        public static String getShopTarget(int soId) {
            String apiUrl = BuildQry(mController, "getShopTarget", new Object[]{
                    "soId=" + String.valueOf(soId)
            });
            return apiUrl;
        }
    }

    public static class Complaint {
        private static final String mController = "complaint";

        public static String getComplaintType(int soId) {
            String apiUrl = BuildQry(mController, "getComplaintType", new Object[]{
                    "soId=" + String.valueOf(soId)
            });
            return apiUrl;
        }

        public static String postInfoWithImage() {
            String apiUrl = BuildQry(mController, "PostInfoWithImage", null);
            return apiUrl;
        }

        public static String postInfo() {
            String apiUrl = BuildQry(mController, "PostInfo", null);
            return apiUrl;
        }
    }


}
