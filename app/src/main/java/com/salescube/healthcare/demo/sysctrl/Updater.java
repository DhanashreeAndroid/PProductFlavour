package com.salescube.healthcare.demo.sysctrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.app.App;
import com.salescube.healthcare.demo.data.model.ClosedAgent;
import com.salescube.healthcare.demo.data.model.Note;
import com.salescube.healthcare.demo.data.model.ProductTarget;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.Target;
import com.salescube.healthcare.demo.data.repo.AgentLocalityRepo;
import com.salescube.healthcare.demo.data.repo.NoteRepo;
import com.salescube.healthcare.demo.data.repo.ProductTargetRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.TargetRepo;
import com.salescube.healthcare.demo.func.PreferenceUtils;
import com.salescube.healthcare.demo.func.UtilityFunc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 05/11/2016.
 */

public class Updater extends AsyncTask<Object, String, Boolean> {

    private Handler mHandler = null;

    private Context mContext;
    private ProgressDialog dialog;
    private boolean isErrorFound;
    private Gson gson2;
    private OkHttpClient client2;

    public Updater(Context _context, Handler _handler) {
        this.mContext = _context;
        this.mHandler = _handler;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {

        try {
            downloadData();
        } catch (Exception e) {
            sendAlert("Error!", e.getMessage());
        }

        return true;
    }

    private void downloadData() throws Exception {

        isErrorFound = false;

        client2 = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();

        gson2 = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();


        downloadTargetAndFocus();
        downloadNotes();
        downloadDates();
        downloadClosedAgent();
        downloadShopChnages();
        // downloadProductTarget();

    }

    private Boolean downloadTargetAndFocus() {

        String url;
        String responseStr = "";

        publishProgress("Updating Target & Focus Product...");
        url = ApiManager.Target.targetAndFocus(AppControl.getmEmployeeId(), AppControl.getTodayDate());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Target targets = gson2.fromJson(responseStr, Target.class);
            TargetRepo objTargetData = new TargetRepo();

            objTargetData.deleteAll(AppControl.getmEmployeeId());
            objTargetData.deleteAll(0);
            objTargetData.insert(targets);
            PreferenceUtils.putBoolean(Constant.PREF_IS_REFRESH_DASHBOARD, true, mContext);
        }

        return true;
    }

    private Boolean downloadNotes() {

        String url;
        String responseStr = "";

        publishProgress("Updating Notes From HO...");
        url = ApiManager.Note.getNotes(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Note[] targets = gson2.fromJson(responseStr, Note[].class);
            NoteRepo objTargetData = new NoteRepo();

            objTargetData.deleteAll(AppControl.getmEmployeeId());
            objTargetData.insert(targets);
        }

        return true;
    }

    private Boolean downloadDates() {

        String url;
        String responseStr = "";

        publishProgress("Updating dates...");
        url = ApiManager.User.getSysDate(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            SysDate[] rates = gson2.fromJson(responseStr, SysDate[].class);
            SysDateRepo objData = new SysDateRepo();

            objData.deleteAll(AppControl.getmEmployeeId(), AppControl.getTodayDate());
            objData.insert(rates);
        }

        return true;
    }

    private Boolean downloadProductTarget() {

        String url;
        String responseStr = "";

        publishProgress("Updating shop target...");
        url = ApiManager.ProductTarget.getShopTarget(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ProductTarget[] rates = gson2.fromJson(responseStr, ProductTarget[].class);
            ProductTargetRepo objData = new ProductTargetRepo();

            objData.deleteAll(AppControl.getmEmployeeId());
            objData.insert(rates);
        }

        return true;
    }

    private Boolean downloadShopChnages() throws Exception {

        String url;
        String responseStr = "";

        publishProgress("Updating shop updates...");
        url = ApiManager.Shop.getShopUpdates(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            Shop[] shops = gson2.fromJson(responseStr, Shop[].class);
            ShopRepo objData = new ShopRepo();

            objData.updateShops(shops);
            new SalesOrderRepo().updateShopId(shops);

            updateShopChangesStatus();
        }

        return true;
    }

    private Boolean downloadClosedAgent() throws Exception {

        String url;
        String responseStr = "";

        publishProgress("Updating closed agent...");
        url = ApiManager.Agent.closedAgent(AppControl.getmEmployeeId());
        responseStr = getRequestString(url);

        if (responseStr != "") {
            ClosedAgent[] shops = gson2.fromJson(responseStr, ClosedAgent[].class);
            AgentLocalityRepo objData = new AgentLocalityRepo();

            objData.updateClosedAgent(shops);

            updateCloseAgentStatus();
        }

        return true;
    }

    private void updateShopChangesStatus() {

        String url;

        publishProgress("Setting shop change status...");
        url = ApiManager.Shop.updateShopChangeStatus(AppControl.getmEmployeeId());
        getRequest(url);

    }

    private void updateCloseAgentStatus() {

        String url;

        publishProgress("Setting close agent status...");
        url = ApiManager.Agent.updateCloseAgentStatus(AppControl.getmEmployeeId());
        getRequest(url);

    }

    private void errorFound(String tag, Throwable e, String title, String customMsg) {

        String excepMsg= "";
        isErrorFound = true;

        if (e != null) {
            Logger.log(Logger.ERROR, tag, e.getMessage(), e);
            excepMsg = e.getMessage();
        }

        if (customMsg == "") {
            sendAlert(title, excepMsg);
        } else {
            sendAlert(title, customMsg);
        }

        this.cancel(true);
    }

    private void sendAlert(String title, String message) {

        Message msg = new Message();

        List<String> appMsg = new ArrayList<>();
        appMsg.add(title);
        appMsg.add(message);
        msg.obj = appMsg;

        msg.arg1 = Constant.MessageType.Alert;

        mHandler.sendMessage(msg);
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
                    .addHeader("IMEI", AppControl.getImei())
                    .build();
        } catch (Exception ex) {
            errorFound("UPDATER", ex, "Build Error!", "Error while building request.");
            return response;
        }

        try {
            response = client2.newCall(request).execute();
        } catch (SocketTimeoutException ex) {
            errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Connection timeout! Server is busy or not available, please try again.");
            return response;
        }catch (UnknownHostException ex){
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
            }else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", "Error Code: " + String.valueOf(response.code()) + " - " + response.message() );
            }
        }

        return "";
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

    private void sendMessage(String message) {
        Message msg = new Message();
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

    protected void onPreExecute() {
        super.onPreExecute();

        if (!UtilityFunc.IsConnected(App.getContext())) {
            sendAlert("No Connectivity!", "Internet connection not available, please check your network.");
            cancel(true);
            return;
        }

        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Update started...");
        dialog.setCancelable(false);

        dialog.show();

    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        dismissDialog();

        if (success) {
            sendMessage("Updates completed successfully!");
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

    private void dismissDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }

}
