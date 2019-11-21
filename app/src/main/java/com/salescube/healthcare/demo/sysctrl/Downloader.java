package com.salescube.healthcare.demo.sysctrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.BuildConfig;
import com.salescube.healthcare.demo.app.App;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.AgentLocality;
import com.salescube.healthcare.demo.data.model.Area;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.data.model.ComplaintType;
import com.salescube.healthcare.demo.data.model.Employee;
import com.salescube.healthcare.demo.data.model.Locality;
import com.salescube.healthcare.demo.data.model.Note;
import com.salescube.healthcare.demo.data.model.OtherWorkReason;
import com.salescube.healthcare.demo.data.model.POP;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductRate;
import com.salescube.healthcare.demo.data.model.Route;
import com.salescube.healthcare.demo.data.model.SS;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.SalesOrderPrevious;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.ShopType;
import com.salescube.healthcare.demo.data.model.Target;
import com.salescube.healthcare.demo.data.model.UpdateRequest;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaAgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintTypeRepo;
import com.salescube.healthcare.demo.data.repo.EmployeeRepo;
import com.salescube.healthcare.demo.data.repo.LocalityRepo;
import com.salescube.healthcare.demo.data.repo.NoteRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkReasonRepo;
import com.salescube.healthcare.demo.data.repo.POPRepo;
import com.salescube.healthcare.demo.data.repo.ProductRateRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.RouteRepo;
import com.salescube.healthcare.demo.data.repo.SSRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderPreviousRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.ShopTypeRepo;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.data.repo.TargetRepo;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.PreferenceUtils;
import com.salescube.healthcare.demo.func.UtilityFunc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 10/09/2016.
 */

// msgHandler to send message to caller class work like delegate
// http://stackoverflow.com/questions/22519541/how-can-i-send-value-from-onpostexecute-to-activity

// Android Asynchronous Http Client
// http://loopj.com/android-async-http/

public class Downloader extends AsyncTask<Object, String, Boolean> {

    private Handler handler = null;

    private Context context;
    private ProgressDialog dialog;

    private Gson gson;
    private OkHttpClient client;
    private boolean mFullDownload;
    private int mRequestCount = 0;
    private int mResponseCount = 0;

    private int employeeId;

    public Downloader(Context context, Handler pHandler, boolean _fullDownload) {
        this.context = context;
        this.handler = pHandler;
        this.mFullDownload = _fullDownload;
    }

    public boolean isJSONValid(String test) {

        if (test.length() <= 4) {
            return false;
        }

        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {


        try {
            return startDownload();
        } catch (Exception e) {
            errorFound("Downloader", e, "Unknown Error!", "");
        }

        return false;

    }

    private boolean startDownload() {

        employeeId = AppControl.getmEmployeeId();

        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        try {

            if (BuildConfig.DEBUG == false) {
                if (!hasUpdates()) {
                    if (!isCancelled()) {
                        sendAlert("No Downloads!", "Please call head office for download request!");
                    }
                    return false;
                }
            }

            downloadDetails();

            downloadShops();
            downloadProducts();
            downloadPOP();
            downloadOtherWorkReason();
            downloadShopTypes();

            downloadPriceList();

            downloadPreviousOrders();

            downloadTargetAndFocus();
            downloadNotes();

            downloadSS();
            downloadComplaintType();

            downloadAgentsMain();
            downloadArea();
            downloadRoute();
            downloadLocality();

            downloadAreaAgent();

            // ASM

            downloadSO();

            downloadTodaysOrderSO();

            if (mResponseCount >= mRequestCount) {
                lockMasterUpdate();
            }/* else {
                sendAlert("Incomplete!", "Incomplete download, please download again.");
            }*/


        } catch (SocketTimeoutException e) {
            Logger.log(Logger.ERROR, "PSS", e.getMessage(), e);
            sendMessage("Connection timeout! Server is busy or not available");
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "PSS", e.getMessage(), e);
            sendMessage(e.getMessage());
            return false;
        }

        return true;
    }



    private Boolean downloadTodaysOrderSO() {

        SalesOrderRepo objObjData = new SalesOrderRepo();
        if(objObjData.hasTodaysOrders()){
            return true;
        }

        String url;
        String responseStr = "";
        int count = 0;

        publishProgress(String.format("%s (%s)...", "Downloading Todays Order List...", count));

        url = ApiManager.Order.getTodayOrderSO(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            SalesOrder[] objList = gson.fromJson(responseStr, SalesOrder[].class);

            publishProgress(String.format("%s (%s)...", "Updating Todays Order List...", count));
            objObjData.insert(objList);
        }
        return true;
    }

    private Boolean downloadSO() throws Exception {

        String url;
        String responseStr = "";
        int count = 0;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.PRICE_LIST);

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading SO List...", count));

        url = ApiManager.Employee.getSOByManagerId(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Employee[] objList = gson.fromJson(responseStr, Employee[].class);
            EmployeeRepo objObjData = new EmployeeRepo();

            publishProgress(String.format("%s (%s)...", "Updating SO List...", count));

            objObjData.deleteAll(employeeId);
            for (Employee employee : objList) {
                employee.setUserId(employeeId);
            }

            objObjData.insert(objList);
        }

        return true;
    }

    private Boolean hasUpdates() throws Exception {

        String url;

        publishProgress("Cheking for updates...");
        url = ApiManager.User.getHasMasterUpdate(AppControl.getmEmployeeId());
        boolean result = getRequestBoolean(url);

        return result;
    }



    private void lockMasterUpdate() throws Exception {

        String url;

        publishProgress("Setting update status...");
        url = ApiManager.User.lockMasterUpdate(AppControl.getmEmployeeId());
        getRequest(url);
    }

    private UpdateRequest[] mDownloadDetails;

    private Hashtable<String, UpdateRequest> mUpdateDetails;

    private Boolean downloadDetails() throws Exception {

        String url;
        String responseStr = "";
        TableInfoRepo objTableInfo = new TableInfoRepo();

        publishProgress("Downloading Details...");
        url = ApiManager.Update.getDownloadDetails(AppControl.getmEmployeeId(), AppControl.getDivisionId());
        responseStr = getRequestString(url);

        mUpdateDetails = new Hashtable<>();
        int localCount = 0;
        int soId = AppControl.getmEmployeeId();

        if (responseStr != "") {

            mDownloadDetails = gson.fromJson(responseStr, UpdateRequest[].class);
            for (UpdateRequest requests : mDownloadDetails) {

                localCount = 0;
                switch (requests.getUpdateType()) {
                    case Constant.UpdateType.AGENT:
                        localCount = objTableInfo.getRecordCount(AgentLocality.TABLE, soId);
                        break;
                    case Constant.UpdateType.SHOP:
                        localCount = objTableInfo.getRecordCount(Shop.TABLE, soId);
                        break;
                    case Constant.UpdateType.PRODUCT:
                        localCount = objTableInfo.getRecordCount(Product.TABLE, soId);
                        break;
                    case Constant.UpdateType.POP:
                        localCount = objTableInfo.getRecordCount(POP.TABLE, soId);
                        break;
                    case Constant.UpdateType.OTHER_WORK_REASON:
                        localCount = objTableInfo.getRecordCount(OtherWorkReason.TABLE);
                        break;
                    case Constant.UpdateType.SHOP_TYPE:
                        localCount = objTableInfo.getRecordCount(ShopType.TABLE);
                        break;
                    case Constant.UpdateType.PRICE_LIST:
                        localCount = objTableInfo.getRecordCount(ProductRate.TABLE, soId);
                        break;
                    case Constant.UpdateType.LAST_ORDERS:
                        localCount = objTableInfo.getRecordCount(SalesOrderPrevious.TABLE, soId);
                        break;
                    case Constant.UpdateType.SS:
                        localCount = objTableInfo.getRecordCount(SS.TABLE, soId);
                        break;
                    default:
                        break;
                }

                requests.setUpdateRequired(true);
                if ((localCount == requests.getRecordCount()) && requests.isForceUpdate() == false && mFullDownload == false) {
                    requests.setUpdateRequired(false);
                    requests.setRecordCount(0);
                }

                mUpdateDetails.put(requests.getUpdateType(), requests);
                mRequestCount += requests.getRecordCount();
            }
        }

        return true;
    }

//    private Boolean downloadAgents() throws Exception {
//
//        String url;
//        String responseStr = "";
//
//        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.AGENT);
//        int count = 0;
//
//        if (serverDetail != null) {
//            count = serverDetail.getRecordCount();
//            if (!serverDetail.isUpdateRequired()) {
//                return true;
//            }
//        }
//
//        publishProgress(String.format("%s (%s)...", "Downloading Agents", count));
//
//        url = ApiManager.Agent.agentsBySo(AppControl.getmEmployeeId());
//        responseStr = getRequestString(url);
//
//        if (responseStr != "") {
//
//            AgentLocalityRepo objAgentData = new AgentLocalityRepo();
//            AgentLocality[] tempAgents = gson.fromJson(responseStr, AgentLocality[].class);
//
//            mResponseCount += tempAgents.length;
//            publishProgress(String.format("%s (%s)...", "Updating Agents", count));
//
//            objAgentData.deleteAll(AppControl.getmEmployeeId());
//            objAgentData.insert(tempAgents);
//        }
//
//        return true;
//    }

    private Boolean downloadShops() throws Exception {

        String url;
        String responseStr = "";

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.SHOP);
        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Shops", count));

        url = ApiManager.Shop.shopsBySo(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {

            ShopRepo objShopData = new ShopRepo();
            Shop[] tempShops = gson.fromJson(responseStr, Shop[].class);

            mResponseCount += tempShops.length;

            publishProgress(String.format("%s (%s)...", "Updating Shops", count));

            objShopData.deleteAll(AppControl.getmEmployeeId());
            objShopData.insert(tempShops);
        }

        return true;
    }

    private Boolean downloadProducts() throws Exception {

        String url;
        String responseStr = "";

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.PRODUCT);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Products", count));

        url = ApiManager.Product.productBySo(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ProductRepo objProductData = new ProductRepo();
            Product[] products = gson.fromJson(responseStr, Product[].class);

            mResponseCount += products.length;
            publishProgress(String.format("%s (%s)...", "Updating Products", count));

            objProductData.deleteAll(AppControl.getmEmployeeId());
            objProductData.insert(products);
        }

        return true;
    }

    private Boolean downloadPriceList() throws Exception {

        String url;
        String responseStr = "";
        int count = 0;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.PRICE_LIST);

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Price List", count));

        url = ApiManager.Product.priceList(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ProductRate[] rates = gson.fromJson(responseStr, ProductRate[].class);
            ProductRateRepo objPriceList = new ProductRateRepo();

            mResponseCount += rates.length;

            publishProgress(String.format("%s (%s)...", "Updating Price List", count));
            objPriceList.deleteAll(AppControl.getmEmployeeId());
            objPriceList.insert(rates);
        }

        return true;
    }

    private Boolean downloadTargetAndFocus() throws Exception {

        String url;
        String responseStr = "";

        publishProgress("Downloading Target & Focus Product...");
        url = ApiManager.Target.targetAndFocus(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Target targets = gson.fromJson(responseStr, Target.class);
            TargetRepo objTargetData = new TargetRepo();

            publishProgress("Updating Target & Focus Product...");
            objTargetData.deleteAll(AppControl.getmEmployeeId());
            objTargetData.insert(targets);
            PreferenceUtils.putBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, true, context);
        }

        return true;
    }

    private Boolean downloadNotes() throws Exception {

        String url;
        String responseStr = "";

        publishProgress("Downloading Notes...");
        url = ApiManager.Note.getNotes(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Note[] targets = gson.fromJson(responseStr, Note[].class);
            NoteRepo objTargetData = new NoteRepo();

            publishProgress("Updating Notes...");
            objTargetData.deleteAll(AppControl.getmEmployeeId());
            objTargetData.insert(targets);
        }

        return true;
    }

    private Boolean downloadPreviousOrders() throws Exception {

        String url;
        String responseStr = "";

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.LAST_ORDERS);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Previous Orders", count));

        url = ApiManager.Order.getLastOrdersBySo(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            List<SalesOrderPrevious> orders = Arrays.asList(gson.fromJson(responseStr, SalesOrderPrevious[].class));
            SalesOrderPreviousRepo objData = new SalesOrderPreviousRepo();

            mResponseCount += orders.size();

            publishProgress(String.format("%s (%s)...", "Updating Previous Orders", count));
            objData.deleteAll(AppControl.getmEmployeeId());
            objData.insert(orders);
        }

        return true;
    }

    private Boolean downloadPOP() throws Exception {

        String url;
        String responseStr = "";
        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.POP);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading POP", count));

        url = ApiManager.POP.getPOPBySO(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            POP[] list = gson.fromJson(responseStr, POP[].class);
            POPRepo objData = new POPRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating POP", count));
            objData.deleteAll(AppControl.getmEmployeeId());
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadShopTypes() throws Exception {

        String url;
        String responseStr = "";

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.SHOP_TYPE);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Shop Types", count));

        url = ApiManager.ShopType.getAll();
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ShopType[] list = gson.fromJson(responseStr, ShopType[].class);
            ShopTypeRepo objData = new ShopTypeRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Shop Types", count));

            objData.deleteAll();
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadOtherWorkReason() throws Exception {

        String url;
        String responseStr = "";
        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.OTHER_WORK_REASON);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Work Reason", count));

        url = ApiManager.OtherWork.getOtherWorkReason();
        responseStr = getRequestString(url);

        if (responseStr != "") {
            OtherWorkReason[] list = gson.fromJson(responseStr, OtherWorkReason[].class);
            OtherWorkReasonRepo objData = new OtherWorkReasonRepo();

            mResponseCount += list.length;
            publishProgress(String.format("%s (%s)...", "Updating Work Reason", count));

            objData.deleteAll();
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadSS() throws Exception {

        String url;
        String responseStr = "";
        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.SS);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading SS List", count));

        url = ApiManager.SS.getSS(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            SS[] list = gson.fromJson(responseStr, SS[].class);
            SSRepo objData = new SSRepo();

            mResponseCount += list.length;
            publishProgress(String.format("%s (%s)...", "Updating SS List", count));

            objData.deleteAll();
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadComplaintType() {

        String url;
        String responseStr;
        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.COMPLAINT_TYPE);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress("Downloading complaint types");

        url = ApiManager.Complaint.getComplaintType(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ComplaintType[] list = gson.fromJson(responseStr, ComplaintType[].class);
            ComplaintTypeRepo objData = new ComplaintTypeRepo();

            mResponseCount += list.length;
            publishProgress("Updating complaint types ");

            objData.deleteAll();
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadAgentsMain() {

        String url;
        String responseStr;
        int count = 0;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.AGENT_NEW);



        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Agents New", count));

        url = ApiManager.Agent.allList(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Agent[] list = gson.fromJson(responseStr, Agent[].class);
            AgentRepo objData = new AgentRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Agents New", count));

            objData.deleteAll(employeeId);
            objData.insert(list);
        }

        return true;
    }


    private Boolean downloadArea() {

        String url;
        String responseStr;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.AREA);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Areas", count));

        url = ApiManager.Area.getAreas(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Area[] list = gson.fromJson(responseStr, Area[].class);
            AreaRepo objData = new AreaRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Areas", count));

            objData.deleteAll(employeeId);
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadRoute() throws Exception {

        String url;
        String responseStr;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.ROUTE);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Route", count));

        url = ApiManager.Route.getRoute(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Route[] list = gson.fromJson(responseStr, Route[].class);
            RouteRepo objData = new RouteRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Routes", count));

            objData.deleteAll(employeeId);
            objData.insert(list);
        }

        return true;
    }

    private Boolean downloadLocality() throws Exception {

        String url;
        String responseStr;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.LOCALITY);

        int count = 0;

        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Locality", count));

        url = ApiManager.Locality.getLocality(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Locality[] list = gson.fromJson(responseStr, Locality[].class);
            LocalityRepo objData = new LocalityRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Localities", count));

            objData.deleteAll(employeeId);
            objData.insert(list);
        }

        return true;
    }


    private Boolean downloadAreaAgent() throws Exception {

        String url;
        String responseStr;
        int count = 0;

        UpdateRequest serverDetail = mUpdateDetails.get(Constant.UpdateType.AREA_AGENT);



        if (serverDetail != null) {
            count = serverDetail.getRecordCount();
            if (!serverDetail.isUpdateRequired()) {
                return true;
            }
        }

        publishProgress(String.format("%s (%s)...", "Downloading Area to Agent", count));

        url = ApiManager.AreaAgent.getRLAreaAgent(employeeId);
        responseStr = getRequestString(url);

        if (responseStr != "") {
            AreaAgent[] list = gson.fromJson(responseStr, AreaAgent[].class);
            AreaAgentRepo objData = new AreaAgentRepo();

            mResponseCount += list.length;

            publishProgress(String.format("%s (%s)...", "Updating Area to Agent", count));

            objData.deleteAll(employeeId);
            objData.insert(list);
        }

        return true;
    }

    private Response getRequest(String url) {

        if (isCancelled()) {
            return null;
        }

        Response response = null;
        Request request;

        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("USER_NAME", AppControl.getUserName())
                    .addHeader("IMEI", MobileInfo.getIMEI(App.getContext()))
                    .build();
        } catch (Exception ex) {
            errorFound("GET_REQUEST", ex, "Error!", "Error while building request");
            return response;
        }

        try {
            response = client.newCall(request).execute();
        } catch (SocketTimeoutException ex) {
            errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Connection timeout! Server is busy or not available, please try again.");
            return response;
        } catch (UnknownHostException ex) {
            errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Failed to connect server, please check your internet connection and try again");
        } catch (Exception ex) {
            errorFound("GET_REQUEST", ex, "Unknwon Error!", "");
            return response;
        }

        return response;

    }

    private String getRequestString(String url) {

        if (isCancelled()) {
            return "";
        }

        String responseStr = "";
        Response response = getRequest(url);

        if (response != null) {

            try {
                responseStr = response.body().string();
            } catch (Exception e) {
                Logger.log(Logger.ERROR, "GET_REQUEST", e.getMessage(), e);
                return "";
            }

            if (response.code() == HttpURLConnection.HTTP_OK) {

                if (isJSONValid(responseStr)) {
                    return responseStr;
                }
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", response.message());
            }
        }

        return "";
    }

    private boolean getRequestBoolean(String url) {

        if (isCancelled()) {
            return false;
        }

        String responseStr = "";
        Response response = getRequest(url);
        boolean result = false;

        if (response != null) {

            try {
                responseStr = response.body().string();
            } catch (Exception e) {
                errorFound("GET_REQUEST", e, "Response Error!", "");
                return false;
            }

            if (response.code() == HttpURLConnection.HTTP_OK) {

                try {
                    result = Boolean.parseBoolean(responseStr);
                } catch (Exception e) {
                    errorFound("GET_REQUEST", e, "Parse Error!", "");
                    return false;
                }
                return result;
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", responseStr);
            }
        }

        return false;
    }

    private int getRequestInt(String url) {

        if (isCancelled()) {
            return 0;
        }

        String responseStr = "";
        Response response = getRequest(url);
        int result = 0;

        if (response != null) {

            try {
                responseStr = response.body().string();
            } catch (Exception e) {
                errorFound("GET_REQUEST", e, "Response Error!", "");
                return 0;
            }

            if (response.code() == HttpURLConnection.HTTP_OK) {

                try {
                    result = Integer.parseInt(responseStr);
                } catch (Exception e) {
                    errorFound("GET_REQUEST", e, "Parse Error!", "");
                    return 0;
                }
                return result;

            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", responseStr);
            }
        }

        return 0;
    }

    private void sendMessage(String message) {
        Message msg = new Message();
        msg.obj = message;
        msg.arg1 = Constant.MessageType.Toast;

        handler.sendMessage(msg);
    }

    private void errorFound(String tag, Throwable e, String title, String customMsg) {
        this.cancel(true);
        String excepMsg = "";

        if (e != null) {
            Logger.log(Logger.ERROR, tag, e.getMessage(), e);
            excepMsg = e.getMessage();
        }

        if (customMsg == "") {
            sendAlert(title, excepMsg);
        } else {
            sendAlert(title, customMsg);
        }
    }

    private void sendAlert(String title, String message) {

        Message msg = new Message();

        List<String> appMsg = new ArrayList<>();
        appMsg.add(title);
        appMsg.add(message);
        msg.obj = appMsg;

        msg.arg1 = Constant.MessageType.Alert;

        handler.sendMessage(msg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!UtilityFunc.IsConnected(App.getContext())) {
            sendAlert("No Connectivity!", "Internet connection not available, please check your network.");
            cancel(true);
            return;
        }

        dialog = new ProgressDialog(context);
        dialog.setMessage("Download started...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        dismissDialog();

        if (success) {
            sendMessage("Download completed successfully!");
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dismissDialog();
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}

