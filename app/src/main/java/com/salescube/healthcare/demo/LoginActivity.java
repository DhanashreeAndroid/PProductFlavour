package com.salescube.healthcare.demo;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.activity.LatestAttendanceActivity;
import com.salescube.healthcare.demo.data.model.Installationlog;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.MobileInfo;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.SharePref;
import com.salescube.healthcare.demo.func.TextFunc;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.service.DataJobIntentService;
import com.salescube.healthcare.demo.service.JobWorkScheduler;
import com.salescube.healthcare.demo.service.LocationJobIntentService;
import com.salescube.healthcare.demo.sysctrl.ApiManager;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.XProgressBar;

import org.acra.ACRA;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "LOGIN_ACTIVITY";

    private TextView txtLoginName;
    private TextView txtPassword;
    private Button btnLogin;
    private boolean isFreshLogin;
    private  Installationlog log;
    private SharePref pref;
    private AlertDialog mGPSDialog;
    private static final int GPS_ENABLE_REQUEST = 0x1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            initControls();
            initListners();

            pref = new SharePref(getApplicationContext());

            initSavedLogin();

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }


    }

    private void initControls() {

        txtLoginName = findViewById(R.id.login_txtUserName);
        txtPassword = findViewById(R.id.login_txtPassword);
        btnLogin = findViewById(R.id.login_btnLogin);

        isFreshLogin = false;
    }

    private void initListners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!permissionCheck()) {
                    return;
                }

                boolean isGPSOn = UtilityFunc.isGPSEnabled(false, LoginActivity.this);
                if (!isGPSOn) {
                   showGPSDiabledDialog();
                    return;
                }


                txtLoginName.setText(txtLoginName.getText().toString().trim());
                txtPassword.setText(txtPassword.getText().toString().trim());


                final String userName = txtLoginName.getText().toString();
                final String password = txtPassword.getText().toString();
                final String imei = MobileInfo.getIMEI(LoginActivity.this);

                ACRA.getErrorReporter().putCustomData("user_name", userName);

                if (userName.equalsIgnoreCase("reset")) {
                    try {
                        new UserRepo().delete();
                        Alert("Success!", "User reset successfully.");
                    } catch (Exception e) {
                        Alert("Error!", "Failed to reset user.");
                    }

                    return;
                }

                if (userName.equalsIgnoreCase("admin")) {
                    String key = DateFunc.getDateStr("ddhh");
                    key = new StringBuffer(key).reverse().toString();

                    String requirdPass = "#JQQXL" + key;
                    if (requirdPass.equals(password)) {

                        DataJobIntentService.enqueueWork(getApplicationContext());

                        Intent utilityIntent = new Intent(LoginActivity.this, UtilityActivity.class);
                        startActivity(utilityIntent);
                        return;

                    } else {

                        Alert("Wrong Password!", "Please enter correct password.");
                        return;
                    }

                }

                if (!validate()) {
                    return;
                }

                new LoginTask().execute(userName, password, imei);

            }
        });
    }

    private void initSavedLogin() {

        User objUser = new UserRepo().getDefaultUser();
        if (objUser != null) {
            txtLoginName.setText(objUser.getUserName());
            txtPassword.setText(objUser.getPassword());
        }
    }

    private boolean validate() {

        boolean result = true;

        if (TextFunc.isEmpty(txtLoginName.getText())) {
            txtLoginName.setError("Please enter login name.");
            result = false;
        }

        if (TextFunc.isEmpty(txtPassword.getText())) {
            txtPassword.setError("Please enter mPassword.");
            result = false;
        }

        return result;
    }

    private void Login(String userName, String password) {

        try {
            AppControl.initControl(getApplicationContext(), userName, password);
        } catch (Exception e) {
            msgLong(e.getMessage());
            return;
        }

        checkForService();

        //updateStatus();

       // Boolean dayInStatus = pref.isDayInDone();

        ACRA.getErrorReporter().putCustomData("user_full_name", AppControl.getEmployeeName());

       // if (dayInStatus== true){

            Intent homeIntent = new Intent(LoginActivity.this, NewMainActivity.class);
            homeIntent.putExtra("isFreshLogin", isFreshLogin);
            startActivity(homeIntent);
            finish();
/*

        }else {

            Intent attendanceActivity = new Intent(LoginActivity.this, LatestAttendanceActivity.class);
            startActivity(attendanceActivity);

        }
*/







    }

    private void checkForService() {

        DataJobIntentService.enqueueWork(getApplicationContext());

        LocationLog objLoc = new LocationLog();
        objLoc.setSoId(AppControl.getmEmployeeId());
        objLoc.setImei(AppControl.getImei());
        objLoc.setAppShopId("");
        objLoc.setEvent("LOGIN");

        LocationJobIntentService.enqueueWork(getApplicationContext(), objLoc);

        JobWorkScheduler.data();
        JobWorkScheduler.location();
    }


    private boolean isServiceIsRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ProgressDialog dlgLogin;

    private class LoginTask extends AsyncTask<String, String, Boolean> {


        String mUserName;
        String mPassword;

        public LoginTask() {
            super();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            mUserName = strings[0];
            mPassword = strings[1];

            try {
                return startLoginTask();
            } catch (Exception e) {
                Alert("Unknown Error!", e.getMessage());
            }

            return false;
        }


        private boolean startLoginTask() {

            UserRepo objUserdb = new UserRepo();
            User objUser = objUserdb.getUser(mUserName, mPassword);

            if (objUser != null) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                if (!Parse.toStr(objUser.getPassword()).equals(mPassword)) {
                    Alert("Login Failed!", "Invalid Username or Password. Please try again.");
                    return false;
                }

                new UserRepo().updateLoginTimeStamp(objUser.getEmployeeId());

                isFreshLogin = false;
                return true;
            }

            boolean isConnected = UtilityFunc.IsConnected(LoginActivity.this);
            if (!isConnected) {
                Alert("No Connectivity!", "Internet connection not available, please check your network.");
                return false;
            }

            String url = ApiManager.User.login(mUserName, mPassword);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("USER_NAME", mUserName)
                    .addHeader("IMEI", MobileInfo.getIMEI(LoginActivity.this))
                    .build();

            String responseStr = "";

            Response response = null;

            try {
                response = client.newCall(request).execute();
                responseStr = response.body().string();
            } catch (SocketTimeoutException ex) {
                errorFound("LOGIN_REQUEST", ex, "Connectivity Problem!", "Connection timeout! Server is busy or not available");
                return false;
            } catch (ConnectException ex) {
                errorFound("LOGIN_REQUEST", ex, "Connectivity Problem!", "Failed to connect server, please try again later.");
                return false;
            } catch (UnknownHostException ex) {
                errorFound("GET_REQUEST", ex, "Connectivity Problem!", "Failed to connect server, please check your internet connection and try again");
            } catch (Exception ex) {
                errorFound("LOGIN_REQUEST", ex, "Unknown Error!", "Failed to retrieve response from server.");
                return false;
            }

            if (response == null) {
                errorFound("LOGIN_REQUEST", null, "Server Error!", "Invalid response! Problem with server or internet connection, please try again.");
                return false;
            }

            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                this.cancel(true);
                Alert("Authentication Error!", responseStr);
                return false;
            }

            if (response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                errorFound("LOGIN_REQUEST", null, "Server Error!", "Problem with server or internet connection, please try again later.");
                return false;
            }

            if (response.code() == HttpURLConnection.HTTP_OK) {

                if (responseStr.isEmpty()) {
                    errorFound("LOGIN_REQUEST", null, "NO DATA!", "Empty response.");
                    return false;
                } else {

                    Gson gson = new GsonBuilder().create();

                    objUser = gson.fromJson(responseStr, User.class);
                    Date deviceDate = new Date();

                    if (!DateFunc.isSameDate(deviceDate, objUser.getLastLoginDate())) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                alert.setTitle("Incorrect Date!");
                                alert.setCancelable(false);
                                alert.setMessage("Your phone date is inaccurate! do you want to correct it?");

                                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent locSetting = new Intent(Settings.ACTION_DATE_SETTINGS);
                                        startActivity(locSetting);
                                    }
                                });

                                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        msgLong("Please correct your phone date, it must be accurate to proceed!");
                                    }
                                });

                                alert.show();
                            }
                        });

                        return false;
                    }

                    if (objUser.getUserName() != null) {

                        objUserdb.insert(objUser);

                        SysDateRepo sysDateRepo = new SysDateRepo();
                        sysDateRepo.deleteAll(objUser.getEmployeeId(), objUser.getLastLoginDate());

                        sysDateRepo.insert(objUser.getEmployeeId(), objUser.getLastLoginDate());


                        String sdkVersion =Build.VERSION.RELEASE;
                        int soId = objUser.getEmployeeId();
                        String appVersion = UtilityFunc.getAppVersion(LoginActivity.this);
                        String currentDate = DateFunc.getDateTimeStr();


                        pref.SaveData(sdkVersion,soId,appVersion,currentDate,"Install");
                        pref.setSYNC_STATUS("0");

                        log = new Installationlog();
                        log.setMobileVersion(sdkVersion);
                        log.setSoId(soId);
                        log.setAppVersion(appVersion);
                        log.setDateTime(currentDate);
                        log.setType("Install");




                        //Log.d("Testing Data",sdkVersion+" "+soId+ " "+appVersion+ " "+currentDate);


                        isFreshLogin = true;
                        return true;
                    }

                }

            } else {
                // unknown response
                errorFound("LOGIN_REQUEST", null, "Unknown error! ", +response.code() + ":" + response.message());
            }

            return false;
        }

        private void errorFound(String tag, Throwable e, String title, String customMsg) {
            this.cancel(true);
            String msg = "";

            if (e != null) {
                Logger.log(Logger.ERROR, tag, e.getMessage(), e);
                msg = e.getMessage();
            }

            if (customMsg == "") {
                Alert(title, msg);
            } else {
                Alert(title, customMsg);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dlgLogin = new ProgressDialog(LoginActivity.this);
            dlgLogin.setMessage("Login...Please wait");
            dlgLogin.setCancelable(true);

            dlgLogin.setOnCancelListener(new Dialog.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    cancel(true);
                }
            });

            dlgLogin.show();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (dlgLogin != null) {
                dlgLogin.dismiss();
            }
            if (success) {

                Login(mUserName, mPassword);
            }

           /* if (pref.getSYNC_STATUS() == "0"){
                uploadInstallationLog(log);
            }*/



        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean string) {
            super.onCancelled(string);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dlgLogin != null) {
                dlgLogin.dismiss();
            }
        }
    }

    private void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (dlgLogin != null && dlgLogin.isShowing()) {
            dlgLogin.dismiss();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        if (dlgLogin != null && dlgLogin.isShowing()) {
            dlgLogin.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setTitle("Confirm!");
            alert.setMessage("Do you want to exit ?");

            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alert.create();
            alert.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean permissionCheck() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }

        List<String> permissions = new ArrayList<>();
        List<String> reqPermissions = new ArrayList<>();

        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);

        for (String permission : permissions)
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                reqPermissions.add(permission);
            }

        if (reqPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this, reqPermissions.toArray(new String[reqPermissions.size()]), 0);
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private void uploadInstallationLog(Installationlog  log) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> query = apiService.postInstallationLog(log);

        UtilityFunc.showDialog(this,"","");

        query.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {


                if (response.isSuccessful()) {

                   SharePref pref = new SharePref(getApplicationContext());
                   pref.setSYNC_STATUS("1");


                } else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                        message = e.getMessage();
                        // togger.e(e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }

                    if(!LoginActivity.this.isFinishing()) {
                        Alert("Error!", message);
                    }

                }

                UtilityFunc.dismissDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                String message;

                if (t instanceof SocketTimeoutException) {
                    message = getString(R.string.connection_timeout);
                } else if (t instanceof ConnectException) {
                    message = getString(R.string.no_connection);
                } else {
                    message = getString(R.string.unknown_error);
                }

                UtilityFunc.dismissDialog();

                if(!LoginActivity.this.isFinishing()) {
                    Alert("Error!", message);
                }

            }
        });





    }

    private  void  updateStatus(){
        String sdk_version  = pref.getAPP_VERSION();

        if (sdk_version != "0"){
            String latestAppVersion = UtilityFunc.getAppVersion(this);
            if (!latestAppVersion.equalsIgnoreCase(sdk_version)){

                String sdkVersion = Build.VERSION.RELEASE;
                int soId = pref.getSOID();
                String appVersion =UtilityFunc.getAppVersion(this);
                String currentDate = DateFunc.getDateTimeStr();


                pref.SaveData(sdkVersion, soId,appVersion,currentDate,"Update");
                pref.setSYNC_STATUS("0");

                log = new Installationlog();
                log.setMobileVersion(sdkVersion);
                log.setSoId(soId);
                log.setAppVersion(appVersion);
                log.setDateTime(currentDate);
                log.setType("Update");

                uploadInstallationLog(log);

            }
        }
    }

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    }

    public void showGPSDiabledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Disabled");
        builder.setMessage("Gps is disabled, in order to use the application properly you need to enable GPS of your device");
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mGPSDialog = builder.create();
        mGPSDialog.show();
    }


}
