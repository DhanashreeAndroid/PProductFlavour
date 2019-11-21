package com.salescube.healthcare.demo.sysctrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.BuildConfig;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.Parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 07/02/2017.
 */

public class ApkInstaller extends AsyncTask<String,String,String> {

    private Context mContext;
    private Handler mHandler;

    private PowerManager.WakeLock mWakeLock;
    ProgressDialog mProgressDialog;
    private Gson gson;
    private OkHttpClient client;
    private boolean mDownloadComplet;

    public void setContext(Context _context){
        mContext = _context;
    }

    public void setHandler(Handler _handler){
        mHandler = _handler;
    }

    public ApkInstaller() {

    }

    String destination;

    @Override
    protected String doInBackground(String... params) {
        mDownloadComplet = false;

        try {
            startDownload(params);
        } catch (Exception e) {
            errorFound("", e, "Error", e.getMessage());
        }

        return null;
    }

    private String startDownload(String... params2) {

        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        String applink = hasUpdates();
        if (applink.isEmpty()) {
            if (!isCancelled()){
                sendAlert("No Update!", "Latest version already installed!");
            }
            return "";
        }

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        final String fileName = "PMA.apk";
        destination += fileName;

        try {
            URL url = new URL(applink);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();


            File file = new File(destination);
            if (file.exists()) {
                file.delete();
            }

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress(String.valueOf((int) (total * 100 / fileLength)));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            sendAlert("Download Error!", e.toString());
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {

            }

            if (connection != null)
                connection.disconnect();
        }

        mDownloadComplet = true;
        return null;
    }

    private String hasUpdates()   {

        String url;

        publishProgress("Cheking for new updates...");
        url = ApiManager.getCheckNewVersion(AppControl.getmEmployeeId(), BuildConfig.VERSION_NAME);
        String appLink = getRequestString(url);
        appLink = appLink.replace("\"","");

        return appLink;
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
                    .addHeader("IMEI", MobileInfo.getIMEI(mContext))
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
        }catch (UnknownHostException ex){
            errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Failed to connect server, please check your internet connection and try again");
        } catch (Exception ex) {
            errorFound("GET_REQUEST", ex, "Unknown Error!", "Failed to retrieve data from server.");
            return response;
        }

        return response;

    }

    // DO / CHECK CAREFULLY
    // JSON & STRING IS DIFFERENT
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
                return responseStr;
            }else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", String.valueOf(response.code()) + ":" + response.message() );
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
            }else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                errorFound("DOWNLOADER", null, "Unauthorised!", responseStr);
            } else {
                errorFound("DOWNLOADER", null, "Unknown Error!", "Error Code: " + String.valueOf(response.code()) + " - " + response.message() );
            }
        }

        return false;
    }

    private void errorFound(String tag, Throwable e, String title, String customMsg) {
        this.cancel(true);
        String excepMsg= "";

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

        mHandler.sendMessage(msg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Please wait, download in process...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
            }
        });

        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mWakeLock.release();
        mProgressDialog.dismiss();

        if (mDownloadComplet) {

            try {
                Toast.makeText(mContext, "File downloaded, installing new version.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(destination)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                mContext.startActivity(intent);
            } catch (Exception e) {
                errorFound("",e, "Failed to install!","");
            }

        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(Parse.toInt(values[0]));

    }

    @Override
    protected void onCancelled(String aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dismissDialog();
    }

    private void dismissDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
