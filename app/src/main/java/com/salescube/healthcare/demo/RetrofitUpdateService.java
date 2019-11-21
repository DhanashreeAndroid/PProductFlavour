package com.salescube.healthcare.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RetrofitUpdateService extends Service {

//    public final static String TAG = "UPDATE_SERVICE";
//
//    public final static int NOTIFIER_ID = 49001;
//
//    private Notification.Builder mNotificationBuilder;
//    private NotificationManager mNotificationManager;
//
//    private boolean mIsUpdateRunning = false;
//    private String mIMEI = "";
//    private ApiService apiService;

    public RetrofitUpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // REFERENCE : https://stackoverflow.com/questions/13562011/breakpoint-in-service-not-working
//        if(BuildConfig.DEBUG){
//            android.os.Debug.waitForDebugger();
//        }

//        Reference : https://stackoverflow.com/questions/4008081/debugging-a-service
//        android.os.Debug.isDebuggerConnected();

//        apiService = ApiClient.getClient().create(ApiService.class);
//
//        IntentFilter autoUpdateFilter = new IntentFilter(Constant.ACTION_AUTO_UPDATE);
//        getBaseContext().registerReceiver(updaterListner, autoUpdateFilter);
//
//        IntentFilter locationLogFilter = new IntentFilter(Constant.ACTION_AUTO_LOCATION_UPDATE);
//        getBaseContext().registerReceiver(locationLogListner, locationLogFilter);
//
//        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(internetListener, intentFilter);
//
//        IntentFilter screenStateFilter = new IntentFilter();
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
//        registerReceiver(screenStateListener,screenStateFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        mIsUpdateRunning = false;
//
//        showToast("RETROFIT-SERVICE-START-COMMAND");
//
//        createNotifier();
//        mIMEI = getIMEI();
//
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

//        getBaseContext().unregisterReceiver(updaterListner);
//        getBaseContext().unregisterReceiver(locationLogListner);
//        unregisterReceiver(internetListener);
//        unregisterReceiver(screenStateListener);

        super.onDestroy();
    }

//    public BroadcastReceiver screenStateListener = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("SMARTSALES","SCREEN_ON_OFF");
//
//            if (!isValidTiming()) {
//                Log.i("SMARTSALES","SCREEN_STATE_LOG_NOT_VALID_TIMING");
//                return;
//            }
//
//            String action = intent.getAction();
//
//            if (action.equals(Intent.ACTION_SCREEN_ON)) {
//                Log.i("SMARTSALES", "SCREEN_ON");
//
//                LocationLog objLoc = new LocationLog();
//                objLoc.setEvent("SCREEN_ON");
//                captureLocation(objLoc);
//            }
//
//            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
//                Log.i("SMARTSALES", "SCREEN_OFF");
//
//                try {
//                    startSync();
//                } catch (Exception e) {
//                    Logger.e(e.getMessage());
//                    mIsUpdateRunning = false;
//                }
//            }
//
//        }
//    };
//
//    public BroadcastReceiver internetListener = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//                try {
//                    startSync();
//                } catch (Exception e) {
//                    Logger.e(e.getMessage());
//                    mIsUpdateRunning = false;
//                }
//        }
//    };
//
//    public BroadcastReceiver updaterListner = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            try {
//                startSync();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//                mIsUpdateRunning = false;
//            }
//        }
//    };
//
//    public BroadcastReceiver locationLogListner = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Log.i("AUTO_LOG_TEST","broadcast_received");
//
//            LocationLog objLoc = intent.getParcelableExtra("LOG");
//            if (objLoc == null) {
//                Logger.e("LOCATION LOG IS EMPTY");
//                return;
//            }
//
//            try {
//                captureLocation(objLoc);
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//        }
//    };
//
//    public String getIMEI() {
//
//        String imei = "";
//
//        try {
//            imei = "";
//            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//            if (tm.getDeviceId() != null) {
//                imei = tm.getDeviceId();
//            }
//        } catch (SecurityException e) {
//            Logger.e(e.getMessage());
//        }
//
//        return imei;
//
//    }
//
//    protected void captureLocation(final LocationLog log) {
//
//        // CHECK IF GPS OFF BY GPS OFF
//        if (!UtilityFunc.isGPSEnabled(RetrofitUpdateService.this)) {
//            Log.i("AUTO_LOG_TEST", "GPS SWITCHED OFF BY USER");
//
//            log.setLatitude("0");
//            log.setLongitude("0");
//            log.setExtraInfo("GPS SWITCHED OFF BY USER");
//
//            try {
//                saveLocation(log);
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            return;
//        }
//
//        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
//            int count = 0;
//
//            @Override
//            public void gotLocation(String provider, Location location) {
//                double[] latLong = new double[2];
//
//                latLong[0] = 0;
//                latLong[1] = 0;
//
//                if (location != null) {
//                    latLong[0] = location.getLatitude();
//                    latLong[1] = location.getLongitude();
//                }else {
//                    log.setExtraInfo("GPS NOT AVAILABLE");
//                }
//
//                log.setLatitude(String.valueOf(latLong[0]));
//                log.setLongitude(String.valueOf(latLong[1]));
//
//                try {
//                    saveLocation(log);
//                } catch (Exception e) {
//                    Logger.e(e.getMessage());
//                }
//
//            }
//        };
//
//        MyLocation myLocation = new MyLocation();
//        myLocation.getLocation(getBaseContext(), locationResult);
//    }
//
//    private boolean isValidTiming() {
//
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR);
//        int ampm = calendar.get(Calendar.AM_PM);
//
//        boolean correct = false;
//        if ((hour >= 9 && ampm == 0) || (hour <= 6 && ampm == 1)) {
//            correct = true;
//        }
//
//        return correct;
//    }
//
//    protected void saveLocation(LocationLog locLog) {
//
//        String networkStatus = "NOT_CONNECTED";
//
//        try {
//            boolean connected = IsConnected();
//            if (connected) {
//                networkStatus = "CONNECTED";
//            }
//        } catch (Exception e) {
//        }
//
//        float battryLevel = 0;
//
//        try {
//            battryLevel = getBatteryLevel();
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }
//
//        Date notDate = null;
//
//        try {
//            notDate = Calendar.getInstance().getTime();
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }
//
//        if (mIMEI.equals("")) {
//            mIMEI = getIMEI();
//        }
//
//        locLog.setImei(mIMEI);
//        locLog.setTxnDate(notDate);
//        locLog.setAddress("");
//        locLog.setNetwork(networkStatus);
//        locLog.setBattery(String.valueOf(battryLevel));
//
//        LocationLogRepo.log(locLog);
//
//    }
//
//    public float getBatteryLevel() {
//
//        Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//        // Error checking that probably isn't needed but I added just in case.
//        if (level == -1 || scale == -1) {
//            return 50.0f;
//        }
//
//        return ((float) level / (float) scale) * 100.0f;
//    }
//
//    private void startSync() {
//
//        if (!IsConnected()) {
//            showToast("[AUTO_UPDATE] " + "No internet access!");
//        }
//
//        if (mIsUpdateRunning) {
//            Logger.i("[AUTO_UPDATE] is already running.");
//            return;
//        }
//
//        try {
//            new Uploader().execute();
//        } catch (Exception e) {
//            mIsUpdateRunning = false;
//            Logger.e(e.getMessage());
//        }
//
//    }
//
//    private void createNotifier() {
//
//        buildNotification("Ready to update...!");
//        startForeground(NOTIFIER_ID, mNotificationBuilder.build());
//    }
//
//    private String CHANNEL_ID = "CH00113";
//
//    private void buildNotification(String text) {
//
//        Intent notificationIntent = new Intent(this, LoginActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        String companyName = getString(R.string.company_name);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "AutoSync Channel", importance);
//            mNotificationManager.createNotificationChannel(channel);
//
//            mNotificationBuilder = new Notification.Builder(this, CHANNEL_ID)
//                    .setContentTitle( companyName + " Order Update")
//                    .setContentText(text)
//                    .setSmallIcon(R.mipmap.ic_logo_3)
//                    .setContentIntent(pendingIntent)
//                    .setTicker("Sales Cube");
//
//        } else {
//
//            mNotificationBuilder = new Notification.Builder(this)
//                    .setContentTitle( companyName + " Order Update")
//                    .setContentText(text)
//                    .setSmallIcon(R.mipmap.ic_logo_3)
//                    .setContentIntent(pendingIntent)
//                    .setTicker("Sales Cube");
//        }
//
//
//
//    }
//
//    public boolean IsConnected() {
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    private void updateNotificattion(String text) {
//        mNotificationBuilder.setContentText(text);
//        mNotificationManager.notify(NOTIFIER_ID, mNotificationBuilder.build());
//    }
//
//    void showToast(final String msg) {
//        if (null != getBaseContext()) {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    public class Uploader extends AsyncTask<Object, String, Boolean> {
//
//        // private final String TAG = "UPLOADER";
//        private Gson gson2;
//        private OkHttpClient client2;
//
//        private String mUserName;
//        private String mIMEI;
//        private int mEmployeeId;
//
//        public Uploader() {
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Object... objects) {
//
//            Log.i(TAG, "Upload started");
//
//            try {
//                startUpdating();
//            } catch (Exception e) {
//                mIsUpdateRunning = false;
//                // showToast(String.format("Failed to auto update! [%s]", mUserName));
//                Logger.e(e.getMessage());
//            }
//
//            return true;
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            Log.i(TAG, "auto update begin");
//
//            if (mIsUpdateRunning) {
//                Log.i(TAG, "auto update already running");
//                cancel(true);
//                return;
//            }
//
//            if (!IsConnected()) {
//                Log.i(TAG, "no internet connection");
//                cancel(true);
//                return;
//            }
//
//            updateNotificattion("Uploading started...");
//            mIsUpdateRunning = true;
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            super.onPostExecute(success);
//
//            Log.i(TAG, "auto update completed");
//
//            mIsUpdateRunning = false;
//
//            String time = getCurrentDateTime();
//            updateNotificattion("Last update succeed on " + time);
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//        }
//
//        public void startUpdating() {
//
//            Log.i(TAG, "Building Client");
//
//            client2 = new OkHttpClient.Builder()
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .build();
//
//            Log.i(TAG, "Building Gson");
//
//            gson2 = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//                    .create();
//
//
//            Log.i(TAG, "Building Gson Completed");
//            String imei = getIMEI();
//            List<User> objUsers = new UserRepo().getUsers();
//
//            Log.i(TAG, "Update Loop user");
//
//            for (User objUser : objUsers) {
//
//                Log.i(TAG, "Checking pending upload " + objUser.getEmployeeName());
//                boolean isUploadPending = new TableInfoRepo().isUploadPending(objUser.getEmployeeId());
//
//                if (!isUploadPending) {
//                    Log.i(TAG, "no upload pending for " + objUser.getEmployeeName());
//                    continue;
//                }
//
//                mUserName = objUser.getUserName();
//                mIMEI = imei;
//                mEmployeeId = objUser.getEmployeeId();
//
//                Log.i(TAG, "Uploading  Data " + objUser.getEmployeeName());
//                uploadData();
//                // new Uploader(objUser.getEmployeeId()).execute(objUser.getUserName(), imei, objUser.getEmployeeId());
//            }
//
//
//        }
//
//        private void uploadData() {
//
//            try {
//                uploadAttendance();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadShops();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadOrders();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadNoOrders();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadOtherWork();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadLeave();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadLocationLogs();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//            try {
//                uploadActivityLog();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadPOP();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadCompetitor();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadMyPlace();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadComplaints();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            try {
//                uploadSalesReturn();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }
//
//            /*try {
//                uploadShopWiseOrder();
//            } catch (Exception e) {
//                Logger.e(e.getMessage());
//            }*/
//        }
//
//
//
//        private int uploadOrders() {
//
//            String url;
//            int result = 0;
//
////            url = ApiManager.Order.postOrders();
//
//            final SalesOrderRepo objSalesOrderData = new SalesOrderRepo();
//            List<SalesOrder> orders = objSalesOrderData.getOrdersForUpload(mEmployeeId);
//            result = orders.size();
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Orders (%s)", orders.size()));
//
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                // error
////                return 0;
////            }
////
////            if (ok) {
////                objSalesOrderData.markPosted();
////                objSalesOrderData.deleteCancelledShopOrders();
////            } else {
////                return 0;
////            }
////
//            Call<Void> dataList = apiService.postOrders(orders);
//
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            objSalesOrderData.markPosted();
//                            objSalesOrderData.deleteCancelledShopOrders();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("shop", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("shop",message);
//                }
//            });
//
//            return result;
//        }
//
//        private int uploadNoOrders() {
//
////            String url;
////            url = ApiManager.Order.postColdCalls();
//
//            final ColdCallRepo objData = new ColdCallRepo();
//            List<NoOrder> orders = objData.getNoOrdersNotPosted(mEmployeeId);
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading No-Orders (%s)", orders.size()));
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                objData.markPosted();
////            } else {
////                return 0;
////            }
////
//            Call<Void> dataList = apiService.postColdCalls(orders);
//
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            objData.markPosted();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("shop", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("shop",message);
//                }
//            });
//
//            return orders.size();
//        }
//
//        private int uploadLocationLogs() {
//
//            String url;
//            url = ApiManager.Location.postLocationLog();
//
//            final LocationLogRepo objLocation = new LocationLogRepo();
//            List<LocationLog> orders = objLocation.getLogsNotPosted();
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            Double lat, lng;
//
//            Geocoder geoCoder;
//            List<Address> addresss = new ArrayList<>();
//            String formatedAddress = "";
//
//            geoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
//
//            for (LocationLog locLog : orders) {
//
//
//                if (!Parse.toStr(locLog.getAddress()).equals("")) {
//                    continue;
//                }
//
//                if (locLog.getLatitude() == null || locLog.getLongitude() == null) {
//                    continue;
//                }
//
//                lat = Parse.toDbl(locLog.getLatitude());
//                lng = Parse.toDbl(locLog.getLongitude());
//
//                if (!lat.equals(0) || !lng.equals(0)) {
//
//                    try {
//                        addresss = geoCoder.getFromLocation(lat, lng, 1);
//                    } catch (IOException e) {
//                        continue;
//                    }
//
//                    if (addresss != null) {
//                        formatedAddress = "";
//                        if (addresss.size() > 0) {
//                            locLog.setAddress(addresss.get(0).getAddressLine(0));
//                            int maxLines = addresss.get(0).getMaxAddressLineIndex();
//                            if (maxLines > 0) {
//                                for (int i = 0; i < maxLines; i++) {
//                                    formatedAddress += addresss.get(0).getAddressLine(i) + ", ";
//                                }
//                                locLog.setAddress(formatedAddress);
//                            }
//                        }
//                        objLocation.updateAddress(locLog, formatedAddress);
//                    }
//
//                } else {
//                    objLocation.updateAddress(locLog, "NO-GPS CONNECTIVITY");
//                }
//
//            }
//
//            updateNotificattion(String.format("Uploading Location Log (%s)", orders.size()));
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                objLocation.deleteAll();
////            } else {
////                return 0;
////            }
//            Call<Void> dataList = apiService.postLocationLog(orders);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            objLocation.deleteAll();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("LocationLog", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("LocationLog",message);
//                }
//            });
//
//            return orders.size();
//        }
//
//        private int uploadPOP() {
//
//            String popApi;
//            String popWithImageApi;
//
//            popApi = ApiManager.POP.postPOP();
//            popWithImageApi = ApiManager.POP.postPOPWithImage();
//
//            final POPShopRepo objImageData = new POPShopRepo();
//            List<POPShop> images1 = objImageData.getPOPEntriesNotPosted(mEmployeeId);
//
//            if (images1.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading POP (%s)", images1.size()));
//
//            File file = null;
//            String path;
//            boolean ok;
//
//            for (final POPShop img : images1) {
//
//                file = null;
//                path = img.getImagePath();
//
//                if (!TextUtils.isEmpty(path)) {
//
//                    try {
//                        file = new File(path);
//                    } catch (Exception e) {
//
//                    }
//
//                    if (!file.exists()) {
//                        file = null;
//                    }
//                }
//
////                ok = false;
////
////                try {
////                    if (file != null) {
////                        ok = postImage(popWithImageApi, file, img);
////                    } else {
////                        ok = postRequest(popApi, img);
////                    }
////                } catch (Exception e) {
////                    return 0;
////                }
////
////                if (ok) {
////                    if (file != null) {
////                        try {
////                            file.delete();
////                        } catch (Exception e) {
////                        }
////                    }
////                    objImageData.delete(img);
////                }
//                if(file!=null){
//                    ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);
//
//                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//                    // Create a request body with file and image media type
//                    RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);
//
//                    // Create MultipartBody.Part using file request-body,file name and part name
////                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(),fileReqBody);
//
//                    //Create request body with text description and text media type
//                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
//
//                    Call<Void> dataList = imgApi.postPOPWithImage(part,description,img);
//
//                    final File finalFile = file;
//
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                if (finalFile != null) {
//                                    try {
//                                    finalFile.delete();
//                                } catch (Exception e) {
//                                }
//                            }
//                            objImageData.delete(img);
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("POPShop", e.getMessage());
////                        togger.e(e);
//                                }
//
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("POPShop",message);
//                        }
//                    });
//                }else{
//                    Call<Void> dataList = apiService.postPOP(img);
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                try{
//                                    objImageData.delete(img);
//                                }catch (Exception ex){
//
//                                }
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("POPShop", e.getMessage());
////                        togger.e(e);
//                                }
//
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("POPShop",message);
//                        }
//                    });
//                }
//
//            }
//
//            return images1.size();
//        }
//
//        private int uploadCompetitor() {
//
//            String popApi;
//            String popWithImageApi;
//
//            popApi = ApiManager.CompetitorInfo.postInfo();
//            popWithImageApi = ApiManager.CompetitorInfo.postWithImage();
//
//            final CompetitorInfoRepo objImageData = new CompetitorInfoRepo();
//            List<CompetitorInfo> images1 = objImageData.getInfoNotPosted(mEmployeeId);
//
//            if (images1.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Competitor Info (%s)", images1.size()));
//
//            File file = null;
//            String path;
//            boolean ok;
//
//            for (final CompetitorInfo img : images1) {
//
//                file = null;
//                path = img.getImagePath();
//
//                if (!TextUtils.isEmpty(path)) {
//
//                    try {
//                        file = new File(path);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//
//                    if (!file.exists()) {
//                        file = null;
//                    }
//                }
//
////                ok = false;
////
////                try {
////                    if (file != null) {
////                        ok = postImage(popWithImageApi, file, img);
////                    } else {
////                        ok = postRequest(popApi, img);
////                    }
////                } catch (Exception e) {
////                    Logger.e(e.getMessage());
////                    continue;
////                }
////
////                if (ok) {
////                    if (file != null) {
////                        file.delete();
////                    }
////                    // objImageData.delete(img);
////                    objImageData.markPosted(img);
////                }
////            }
//
//                if(file!=null){
//                    ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);
//
//                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//                    // Create a request body with file and image media type
//                    RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);
//
//                    // Create MultipartBody.Part using file request-body,file name and part name
////                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(),fileReqBody);
//
//                    //Create request body with text description and text media type
//                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
//
//                    Call<Void> dataList = imgApi.postCompetitorWithImage(part,description,img);
//
//                    final File finalFile = file;
//
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                if (finalFile != null) {
//                                    try {
//                                        finalFile.delete();
//                                    } catch (Exception e) {
//                                    }
//                                }
////                                objImageData.delete(img);
//                                objImageData.markPosted(img);
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("CompetitorInfo", e.getMessage());
////                        togger.e(e);
//                                }
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("CompetitorInfo",message);
//                        }
//                    });
//                }else{
//                    Call<Void> dataList = apiService.postInfo(img);
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                try{
//                                    objImageData.delete(img);
//                                }catch (Exception ex){
//
//                                }
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("CompetitorInfo", e.getMessage());
////                        togger.e(e);
//                                }
//
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("CompetitorInfo",message);
//                        }
//                    });
//                }
//
//            }
//            return images1.size();
//        }
//
//        private int uploadOtherWork() {
//
//            String url;
//
//            url = ApiManager.OtherWork.postOtherWork();
//
//            final OtherWorkRepo objOtherWork = new OtherWorkRepo();
//            List<OtherWork> orders = objOtherWork.getOtherWorkNotPosted(mEmployeeId);
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Other Work (%s)", orders.size()));
//
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                // Mark Posted
////                objOtherWork.markPosted();
////            } else {
////                return 0;
////            }
//
//            Call<Void> dataList = apiService.postOtherWork(orders);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            // Mark Posted
//                            objOtherWork.markPosted();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("otherWork", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("otherWork",message);
//                }
//            });
//
//            return orders.size();
//        }
//
//        private int uploadLeave() {
//
////            String url;
////            url = ApiManager.Leave.apply();
//
//            final LeaveRepo leaveRepo = new LeaveRepo();
//            List<Leave> leaves = leaveRepo.getLeaveNotPosted(mEmployeeId);
//
//            if (leaves.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Leave (%s)", leaves.size()));
//
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, leaves);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                leaveRepo.markPosted();
////            } else {
////                return 0;
////            }
//
//            Call<Void> dataList = apiService.apply(leaves);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            leaveRepo.markPosted();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("leave", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("leave",message);
//                }
//            });
//            return leaves.size();
//        }
//
//        private int uploadShops() {
//
////            String url;
////
////            url = ApiManager.Shop.postShops();
//
//            final ShopRepo leaveRepo = new ShopRepo();
//            List<Shop> leaves = leaveRepo.getShopForUpload(mEmployeeId);
//
//            if (leaves.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Shops (%s)", leaves.size()));
////            boolean ok = false;
//
////            try {
////                ok = postRequest(url, leaves);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                leaveRepo.markPosted();
////            } else {
////                return 0;
////            }
//
//
////            apiService = ApiClient.getClient().create(ApiService.class);
////
//            Call<Void> dataList = apiService.getShopUpdate(leaves);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            leaveRepo.markPosted();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("shop", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("shop",message);
//                }
//            });
//
//            return leaves.size();
//        }
//
//        private int uploadAttendance() {
//
//            final SysDateRepo leaveRepo = new SysDateRepo();
//
//            List<SysDate> dateList = leaveRepo.getDatesForUpload(mEmployeeId);
//
//            if (dateList.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Attendance (%s)", dateList.size()));
//
////            apiService = ApiClient.getClient().create(ApiService.class);
////
//            Call<Void> dataList = apiService.getAttendenceMark(dateList);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                            try{
//                                leaveRepo.markPosted();
//                            }catch (Exception ex){
//
//                            }
//                        }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("attedance", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//                       }
//                    }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                        message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("attedance",message);
//                }
//            });
//            return dateList.size();
//        }
//
//        private int uploadActivityLog() {
//
//            String url;
//
//            url = ApiManager.ActivityLog.postActivityLog();
//
//            final ActivityLogRepo objOtherWork = new ActivityLogRepo();
//            List<ActivityLog> orders = objOtherWork.getLogsNotPosted();
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Activity Log (%s)", orders.size()));
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                objOtherWork.deleteAll();
////            } else {
////                return 0;
////            }
//
//            Call<Void> dataList = apiService.postActivityLog(orders);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            objOtherWork.deleteAll();
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("ActivityLog", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("ActivityLog",message);
//                }
//            });
//            return orders.size();
//        }
//
//        private int uploadMyPlace() {
//
//            String url;
//
//            url = ApiManager.MyPlace.postMyPlace();
//
//            final MyPlaceRepo repo = new MyPlaceRepo();
//            List<MyPlace> orders = repo.getMyPlacesNotPosted(mEmployeeId);
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading My Place (%s)", orders.size()));
////            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                repo.markPosted(mEmployeeId);
////            } else {
////                return 0;
////            }
//
//            Call<Void> dataList = apiService.postMyPlace(orders);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            repo.markPosted(mEmployeeId);
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("SalesReturn", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("SalesReturn",message);
//                }
//            });
//
//            return orders.size();
//        }
//
//        private int uploadSalesReturn() {
//
//            String url;
//
//            url = ApiManager.Order.postSalesReturn();
//
//            final SalesReturnRepo repo = new SalesReturnRepo();
//            List<SalesReturn> orders = repo.getOrdersForUpload(mEmployeeId);
//
//            if (orders.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Sales Return (%s)", orders.size()));
//            boolean ok = false;
////
////            try {
////                ok = postRequest(url, orders);
////            } catch (Exception e) {
////                return 0;
////            }
////
////            if (ok) {
////                repo.markPosted(mEmployeeId);
////            } else {
////                return 0;
////            }
//            Call<Void> dataList = apiService.postSalesReturn(orders);
//            dataList.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if(response.isSuccessful()){
//                        try{
//                            repo.markPosted(mEmployeeId);
//                        }catch (Exception ex){
//
//                        }
//                    }else {
//                        String message;
//                        try {
//                            message = response.errorBody().string();
//                        } catch (Exception e) {
//                            message = e.getMessage();
//                            Log.d("SalesReturn", e.getMessage());
////                        togger.e(e);
//                        }
//
//                        if (message.equals("")) {
//                            message = response.raw().message();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    String message;
//
//                    message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                    Log.d("SalesReturn",message);
//                }
//            });
//
//
//            return orders.size();
//        }
//
//        private int uploadComplaints() {
//
//            String popApi;
//            String popWithImageApi;
//
//            popApi = ApiManager.Complaint.postInfo();
//            popWithImageApi = ApiManager.Complaint.postInfoWithImage();
//
//            final ComplaintRepo objImageData = new ComplaintRepo();
//            List<Complaint> images1 = objImageData.getComplaintsForUpload(mEmployeeId);
//
//            if (images1.size() == 0) {
//                return 0;
//            }
//
//            updateNotificattion(String.format("Uploading Complaints (%s)", images1.size()));
//
//            File file = null;
//            String path;
//            boolean ok;
//
//            for (final Complaint img : images1) {
//
//                file = null;
//                path = img.getImagePath();
//
//                if (!TextUtils.isEmpty(path)) {
//
//                    try {
//                        file = new File(path);
//                    } catch (Exception e) {
//                        Logger.e(e.getMessage());
//                    }
//
//                    if (!file.exists()) {
//                        file = null;
//                    }
//                }
//
////                ok = false;
////
////                try {
////                    if (file != null) {
////                        ok = postImage(popWithImageApi, file, img);
////                    } else {
////                        ok = postRequest(popApi, img);
////                    }
////                } catch (Exception e) {
////                    Logger.e(e.getMessage());
////                    continue;
////                }
////
////                if (ok) {
////                    if (file != null) {
////                        file.delete();
////                    }
////                    // objImageData.delete(img);
////                    objImageData.markPosted(img);
////                }
//                if(file!=null){
//                    ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);
//
//                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//                    // Create a request body with file and image media type
//                    RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);
//
//                    // Create MultipartBody.Part using file request-body,file name and part name
////                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(),fileReqBody);
//
//                    //Create request body with text description and text media type
//                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
//
//                    Call<Void> dataList = imgApi.postComplaintWithImage(part,description,img);
//
//                    final File finalFile = file;
//
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                if (finalFile != null) {
//                                    try {
//                                        finalFile.delete();
//                                    } catch (Exception e) {
//                                    }
//                                }
////                                objImageData.delete(img);
//                                objImageData.markPosted(img);
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("CompetitorInfo", e.getMessage());
////                        togger.e(e);
//                                }
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("CompetitorInfo",message);
//                        }
//                    });
//                } else{
//                    Call<Void> dataList = apiService.postComplaint(img);
//                    dataList.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                            if(response.isSuccessful()){
//                                try{
//                                    objImageData.markPosted(img);
//                                }catch (Exception ex){
//
//                                }
//                            }else {
//                                String message;
//                                try {
//                                    message = response.errorBody().string();
//                                } catch (Exception e) {
//                                    message = e.getMessage();
//                                    Log.d("CompetitorInfo", e.getMessage());
////                        togger.e(e);
//                                }
//
//                                if (message.equals("")) {
//                                    message = response.raw().message();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            String message;
//
//                            message  = RetrofitException.msg(RetrofitUpdateService.this,t);
//
//                            Log.d("CompetitorInfo",message);
//                        }
//                    });
//                }
//
//            }
//
//            return images1.size();
//        }
//
//        private boolean postRequest(String url, Object objList) throws Exception {
//
//            if (isCancelled()) {
//                return false;
//            }
//
//            Response response = null;
//            Request request;
//
//            String jsonStr = gson2.toJson(objList);
//            final okhttp3.MediaType JSON = okhttp3.MediaType.parse("application/json;charset=utf-8");
//
//            RequestBody requestBody = RequestBody.create(JSON, jsonStr);
//            request = new Request.Builder()
//                    .url(url)
//                    .addHeader("USER_NAME", mUserName)
//                    .addHeader("IMEI", mIMEI)
//                    .post(requestBody)
//                    .build();
//
//            try {
//                response = client2.newCall(request).execute();
//            } catch (Exception e) {
//                Logger.e("post_request", e.getMessage());
//                return false;
//            }
//
//            if (response != null) {
//                if (response.code() == HttpURLConnection.HTTP_OK) {
//                    return true;
//                }
//                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                    Log.i(TAG, String.format("Unauthorised User [%s]", mUserName));
//                }
//            }
//
//            return false;
//        }
//
//        private boolean postImage(String url, File file, Object data) throws Exception {
//
//            if (isCancelled()) {
//                return false;
//            }
//
//            RequestBody requestBody;
//            String jsonStr = "";
//
//            Response response = null;
//            Request request;
//
//            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//
//            requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("title", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
//                    .build();
//
//            try {
//                jsonStr = gson2.toJson(data);
//            } catch (Exception e) {
//                Logger.e("toJson " + e.getMessage());
//            }
//
//            request = new Request.Builder()
//                    .url(url)
//                    .addHeader("Data", jsonStr)
//                    .addHeader("USER_NAME", mUserName)
//                    .addHeader("IMEI", mIMEI)
//                    .post(requestBody)
//                    .build();
//
//
//            try {
//                response = client2.newCall(request).execute();
//            } catch (Exception e) {
//                Logger.e("post_request_image", e.getMessage());
//                return false;
//            }
//
//            if (response != null) {
//                if (response.code() == HttpURLConnection.HTTP_OK) {
//                    return true;
//                }
//                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                    //showToast(String.format("Unauthorised User [%s]", mUserName));
//                }
//            }
//
//            return false;
//        }
//
//        private boolean postImage2(String url, File file, Object data) throws Exception {
//
//            if (isCancelled()) {
//                return false;
//            }
//
//            RequestBody requestBody;
//            String jsonStr = "";
//
//            Response response = null;
//            Request request;
//
//            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//            final MediaType MEDIA_TYPE_JSON = okhttp3.MediaType.parse("application/json;charset=utf-8");
//
//            RequestBody requestBodyJson = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
//
//            requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addPart(requestBodyJson)
//                    .addFormDataPart("title", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
//                    .build();
//
////            try {
////                jsonStr = gson2.toJson(data);
////            } catch (Exception e) {
////                Logger.e("toJson " +  e.getMessage());
////            }
//
//            request = new Request.Builder()
//                    .url(url)
//                    .addHeader("USER_NAME", mUserName)
//                    .addHeader("IMEI", mIMEI)
//                    .post(requestBody)
//                    .build();
//
//
//            try {
//                response = client2.newCall(request).execute();
//            } catch (Exception e) {
//                Logger.e("post_request_image", e.getMessage());
//                return false;
//            }
//
//            if (response != null) {
//                if (response.code() == HttpURLConnection.HTTP_OK) {
//                    return true;
//                }
//                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                    // showToast(String.format("Unauthorised User [%s]", mUserName));
//                }
//            }
//
//            return false;
//        }
//
////        private void dailyUpdate(){
////
////            if (!hasDailyUpdates()){
////                return;
////            }
////
////            downloadTargetAndFocus();
////            downloadNotes();
////            downloadDates();
////            downloadShopChnages();
////
////        }
////
////        private Boolean downloadTargetAndFocus() {
////
////            String url;
////            String responseStr = "";
////
////            updateNotificattion("Updating Target & Focus Product...");
////            url = ApiManager.targetAndFocus(mEmployeeId, AppControl.getTodayDate());
////            responseStr = getRequestString(url);
////
////            if (responseStr != "") {
////                Target targets = gson2.fromJson(responseStr, Target.class);
////                TargetRepo objTargetData = new TargetRepo();
////
////                objTargetData.deleteAll(mEmployeeId);
////                objTargetData.insert(targets);
////            }
////
////            return true;
////        }
////
////        private Boolean downloadNotes() {
////
////            String url;
////            String responseStr = "";
////
////            updateNotificattion("Updating Notes From HO...");
////            url = ApiManager.getNotes(mEmployeeId);
////            responseStr = getRequestString(url);
////
////            if (responseStr != "") {
////                Note[] targets = gson2.fromJson(responseStr, Note[].class);
////                NoteRepo objTargetData = new NoteRepo();
////
////                objTargetData.deleteAll(mEmployeeId);
////                objTargetData.insert(targets);
////            }
////
////            return true;
////        }
////
////        private Boolean downloadDates() {
////
////            String url;
////            String responseStr = "";
////
////            updateNotificattion("Updating dates...");
////            url = ApiManager.getSysDate(mEmployeeId);
////            responseStr = getRequestString(url);
////
////            if (responseStr != "") {
////                SysDate[] rates = gson2.fromJson(responseStr, SysDate[].class);
////                SysDateRepo objData = new SysDateRepo();
////
////                objData.deleteAll(mEmployeeId, AppControl.getTodayDate());
////                objData.insert(rates);
////            }
////
////            return true;
////        }
////
////        private Boolean downloadShopChnages() {
////
////            String url;
////            String responseStr = "";
////
////            updateNotificattion("Updating shop updates...");
////            url = ApiManager.getShopUpdates(mEmployeeId);
////            responseStr = getRequestString(url);
////
////            if (responseStr != "") {
////                Shop[] shops = gson2.fromJson(responseStr, Shop[].class);
////                ShopRepo objData = new ShopRepo();
////
////                objData.updateShops(shops);
////                new SalesOrderRepo().updateShopId(shops);
////
////            }
////
////            return true;
////        }
////
////        private void updateShopChangesStatus() {
////
////            String url;
////
////            updateNotificattion("Setting shop change status...");
////            url = ApiManager.updateShopChangeStatus(mEmployeeId);
////            getRequest(url);
////
////        }
////
////        private Boolean hasDailyUpdates()  {
////
////            String url;
////            publishProgress("Checking for daily updates...");
////
////            boolean result = false;
////            try {
////                url = ApiManager.getHasDailyUpdate(mEmployeeId);
////                result = getRequestBoolean(url);
////            } catch (Exception e) {
////                return result;
////            }
////
////            return result;
////        }
//
//        private Response getRequest(String url) {
//
//            if (isCancelled()) {
//                return null;
//            }
//
//            Response response = null;
//            Request request;
//
//            try {
//                request = new Request.Builder()
//                        .url(url)
//                        .addHeader("USER_NAME", mUserName)
//                        .addHeader("IMEI", mIMEI)
//                        .build();
//            } catch (Exception ex) {
//                return response;
//            }
//
//            try {
//                response = client2.newCall(request).execute();
//            } catch (Exception ex) {
//                return response;
//            }
//
//            return response;
//
//        }
//
//        private boolean getRequestBoolean(String url) {
//
//            if (isCancelled()) {
//                return false;
//            }
//
//            String responseStr = "";
//            Response response = getRequest(url);
//            boolean result = false;
//
//            if (response != null) {
//
//                try {
//                    responseStr = response.body().string();
//                } catch (Exception e) {
//                    return false;
//                }
//
//                if (response.code() == HttpURLConnection.HTTP_OK) {
//
//                    try {
//                        result = Boolean.parseBoolean(responseStr);
//                    } catch (Exception e) {
//                        return false;
//                    }
//                    return result;
//                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//
//                } else {
//                }
//            }
//
//            return false;
//        }
//
//        private String getRequestString(String url) {
//
//            if (isCancelled()) {
//                return "";
//            }
//
//            String responseStr = "";
//            Response response = getRequest(url);
//
//            if (response != null) {
//
//                try {
//                    responseStr = response.body().string();
//                } catch (Exception e) {
//                    return "";
//                }
//
//                if (response.code() == HttpURLConnection.HTTP_OK) {
//
//                    if (isJSONValid(responseStr)) {
//                        return responseStr;
//                    }
//                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                } else {
//                }
//            }
//
//            return "";
//        }
//
//        public boolean isJSONValid(String test) {
//
//            if (test.length() <= 4) {
//                return false;
//            }
//
//            try {
//                new JSONObject(test);
//            } catch (JSONException ex) {
//                try {
//                    new JSONArray(test);
//                } catch (JSONException ex1) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//
//        @Override
//        protected void onCancelled(Boolean aBoolean) {
//            super.onCancelled(aBoolean);
//            mIsUpdateRunning = false;
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            Log.i(TAG, "auto update cancelled");
//
//            mIsUpdateRunning = false;
//
//            String time = getCurrentDateTime();
//            updateNotificattion("Last update on " + time);
//        }
//
//        private String getCurrentDateTime() {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm aaa");
//
//            try {
//                return dateFormat.format(new Date());
//            } catch (Exception e) {
//                return "DateTime error";
//            }
//        }
//    }

}

