package com.salescube.healthcare.demo.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.ApiClient;
import com.salescube.healthcare.demo.ApiService;
import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.model.Complaint;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.SalesReturn;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.ActivityLogRepo;
import com.salescube.healthcare.demo.data.repo.ColdCallRepo;
import com.salescube.healthcare.demo.data.repo.CompetitorInfoRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintRepo;
import com.salescube.healthcare.demo.data.repo.LeaveRepo;
import com.salescube.healthcare.demo.data.repo.LocationLogRepo;
import com.salescube.healthcare.demo.data.repo.MyPlaceRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkRepo;
import com.salescube.healthcare.demo.data.repo.POPShopRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.SalesReturnRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.Parse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.salescube.healthcare.demo.func.MobileInfo.getIMEI;

public class DataJobIntentService extends JobIntentService {

    public final static String TAG = "DataService";
    private int mEmployeeId;
    private ApiService apiService;

    public DataJobIntentService() {
        super();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Log.i(TAG, "service started");

        final User objUser = new UserRepo().getOwnerUser();

        if (objUser == null) {
            return;
        }

        try {
            startUpdating();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        Log.i(TAG, "work done");
    }

    public static void enqueueWork(Context context) {

        if (isServiceIsRunning(context, DataJobIntentService.class)) {
            Log.d(TAG, "service already running");
            return;
        }

        if (!IsConnected(context)) {
            Log.i(TAG, "no internet connection");
            return;
        }

        Intent intent = new Intent(context, DataJobIntentService.class);
        enqueueWork(context, DataJobIntentService.class, 1244, intent);
    }

    private static boolean isServiceIsRunning(Context context, Class serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "service destroyed");
        super.onDestroy();
    }

    public static boolean IsConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            return true;
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void startUpdating() {

        apiService = ApiClient.getClient().create(ApiService.class);

        String imei = getIMEI();
        List<User> objUsers = new UserRepo().getUsers();

        Log.i(TAG, "getting users");

        for (User objUser : objUsers) {

            Log.i(TAG, "checking pending upload " + objUser.getEmployeeName());
            boolean isUploadPending = new TableInfoRepo().isUploadPending(objUser.getEmployeeId());

            if (!isUploadPending) {
                Log.i(TAG, "no upload pending for " + objUser.getEmployeeName());
                continue;
            }

            mEmployeeId = objUser.getEmployeeId();
            Log.i(TAG, "uploading data " + objUser.getEmployeeName());

            startUpload();
        }


    }

    private void startUpload() {


        try {
           //close this method after merge all development module - attendance mark After Login click
          uploadAttendance();

        } catch (Exception e) {
            Log.i(TAG, "");
        }

        try {
            uploadShops();
        } catch (Exception e) {
            Log.i(TAG, "");
        }

        try {
            uploadOrders();
        } catch (Exception e) {
            Log.i(TAG, "");
        }

        try {
            uploadNoOrders();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadOtherWork();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadLeave();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadLocationLogs();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadActivityLog();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadPOP();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadCompetitor();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadMyPlace();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadComplaints();
        } catch (Exception e) {
            Log.i(TAG, "");
        }


        try {
            uploadSalesReturn();
        } catch (Exception e) {
            Log.i(TAG, "");
        }

    }

    private int uploadOrders() {

        final String TAG2 = "uploadOrders";
        int result = 0;

        final SalesOrderRepo objSalesOrderData = new SalesOrderRepo();
        final List<SalesOrder> orders = objSalesOrderData.getOrdersForUpload(mEmployeeId);
        result = orders.size();

        if (orders.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.postOrders(orders);

        dataList.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        objSalesOrderData.markPosted(orders);
                        objSalesOrderData.deleteCancelledShopOrders();
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });

        return result;
    }

    private int uploadNoOrders() {

        final String TAG2 = "uploadNoOrders";

        final ColdCallRepo objData = new ColdCallRepo();
        final List<NoOrder> orders = objData.getNoOrdersNotPosted(mEmployeeId);

        if (orders.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.postColdCalls(orders);

        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        objData.markPosted(orders);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });

        return orders.size();
    }

    private int uploadLocationLogs() {

        final String TAG2 = "uploadLocationLogs";

        final LocationLogRepo objLocation = new LocationLogRepo();
        final List<LocationLog> orders = objLocation.getLogsNotPosted();

        if (orders.size() == 0) {
            return 0;
        }

        Double lat, lng;

        Geocoder geoCoder;
        List<Address> addresss;
        String formatedAddress;

        geoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);

        for (LocationLog locLog : orders) {


            if (!Parse.toStr(locLog.getAddress()).equals("")) {
                continue;
            }

            if (locLog.getLatitude() == null || locLog.getLongitude() == null) {
                continue;
            }

            lat = Parse.toDbl(locLog.getLatitude());
            lng = Parse.toDbl(locLog.getLongitude());

            if (!lat.equals(0) || !lng.equals(0)) {

                try {
                    addresss = geoCoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    continue;
                }

                if (addresss != null) {
                    formatedAddress = "";
                    if (addresss.size() > 0) {
                        locLog.setAddress(addresss.get(0).getAddressLine(0));
                        int maxLines = addresss.get(0).getMaxAddressLineIndex();
                        if (maxLines > 0) {
                            for (int i = 0; i < maxLines; i++) {
                                formatedAddress += addresss.get(0).getAddressLine(i) + ", ";
                            }
                            locLog.setAddress(formatedAddress);
                        }
                    }
                    objLocation.updateAddress(locLog, formatedAddress);
                }

            } else {
                objLocation.updateAddress(locLog, "NO-GPS CONNECTIVITY");
            }

        }

        Call<Void> dataList = apiService.postLocationLog(orders);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        objLocation.deleteAll(orders);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });

        return orders.size();
    }

    private int uploadPOP() {

        final String TAG2 = "uploadPOP";

        final POPShopRepo objImageData = new POPShopRepo();
        List<POPShop> images = objImageData.getPOPEntriesNotPosted(mEmployeeId);

        if (images.size() == 0) {
            return 0;
        }

        File file = null;
        String path;

        for (final POPShop img : images) {

            file = null;
            path = img.getImagePath();

            if (!TextUtils.isEmpty(path)) {

                try {
                    file = new File(path);
                } catch (Exception e) {

                }

                if (!file.exists()) {
                    file = null;
                }
            }

            if (file != null) {
                ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                // Create a request body with file and image media type
                RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);

                // Create MultipartBody.Part using file request-body,file name and part name
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
                MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(), fileReqBody);

                //Create request body with text description and text media type
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

                Call<Void> dataList = imgApi.postPOPWithImage(part, description, img);

                final File finalFile = file;

                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            if (finalFile != null) {
                                try {
                                    finalFile.delete();
                                } catch (Exception ex) {
                                    log2(TAG2, ex.getMessage());
                                }
                            }
                            objImageData.delete(img);
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            } else {
                Call<Void> dataList = apiService.postPOP(img);
                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            try {
                                objImageData.delete(img);
                            } catch (Exception ex) {
                                log2(TAG2, ex.getMessage());
                            }
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log2(TAG2, t.getMessage());
                    }
                });
            }

        }

        return images.size();
    }

    private int uploadCompetitor() {

        final String TAG2 = "uploadCompetitor";

        final CompetitorInfoRepo objImageData = new CompetitorInfoRepo();
        List<CompetitorInfo> images = objImageData.getInfoNotPosted(mEmployeeId);

        if (images.size() == 0) {
            return 0;
        }

        File file = null;
        String path;

        for (final CompetitorInfo img : images) {

            file = null;
            path = img.getImagePath();

            if (!TextUtils.isEmpty(path)) {

                try {
                    file = new File(path);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

                if (!file.exists()) {
                    file = null;
                }
            }

            if (file != null) {
                ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                // Create a request body with file and image media type
                RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);

                // Create MultipartBody.Part using file request-body,file name and part name
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
                MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(), fileReqBody);

                //Create request body with text description and text media type
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

                Call<Void> dataList = imgApi.postCompetitorWithImage(part, description, img);

                final File finalFile = file;

                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            if (finalFile != null) {
                                try {
                                    finalFile.delete();
                                } catch (Exception ex) {
                                    log2(TAG2, ex.getMessage());
                                }
                            }
//                                objImageData.delete(img);
                            objImageData.markPosted(img);
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log2(TAG2, t.getMessage());
                    }
                });


            } else {


                Call<Void> dataList = apiService.postInfo(img);
                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            try {
                                objImageData.markPosted(img);
                            } catch (Exception ex) {
                                log2(TAG2, ex.getMessage());
                            }
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log2(TAG2, t.getMessage());
                    }
                });
            }

        }
        return images.size();
    }

    private int uploadOtherWork() {

        final String TAG2 = "uploadOtherWork";

        final OtherWorkRepo objOtherWork = new OtherWorkRepo();
        final List<OtherWork> data = objOtherWork.getOtherWorkNotPosted(mEmployeeId);

        if (data.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.postOtherWork(data);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        // Mark Posted
                        objOtherWork.markPosted(data);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }

        });

        return data.size();
    }

    private int uploadLeave() {

        final String TAG2 = "uploadLeave";

        final LeaveRepo leaveRepo = new LeaveRepo();
        final List<Leave> leaves = leaveRepo.getLeaveNotPosted(mEmployeeId);

        if (leaves.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.apply(leaves);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        leaveRepo.markPosted(leaves);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });
        return leaves.size();
    }

    private int uploadShops() {

        final String TAG2 = "uploadShops";

        final ShopRepo leaveRepo = new ShopRepo();
        List<Shop> leaves = leaveRepo.getShopForUpload(mEmployeeId);

        if (leaves.size() == 0) {
            return 0;
        }

        // todo

        Call<Void> dataList = apiService.getShopUpdate(leaves);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        leaveRepo.markPosted();
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });

        return leaves.size();
    }

    private void log2(String tag2, String message) {
        Log.d(TAG + "-" + tag2, message);
    }

    private String filterResponseErr(Response response) {

        String message;
        try {
            message = response.errorBody().string();
            if (message.equals("")) {
                message = response.raw().message();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        return message;
    }

    private int uploadAttendance() {

        final String TAG2 = "uploadAttendance";

        final SysDateRepo attendance = new SysDateRepo();

        List<SysDate> dateList = attendance.getDatesForUpload(mEmployeeId);

        if (dateList.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.getAttendenceMark(dateList);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        attendance.markPosted();
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });
        return dateList.size();
    }

    private int uploadActivityLog() {

        final String TAG2 = "uploadActivityLog";

        final ActivityLogRepo objOtherWork = new ActivityLogRepo();
        List<ActivityLog> orders = objOtherWork.getLogsNotPosted();

        if (orders.size() == 0) {
            return 0;
        }


        Call<Void> dataList = apiService.postActivityLog(orders);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        objOtherWork.deleteAll();
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {

                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });
        return orders.size();
    }

    private int uploadMyPlace() {

        final String TAG2 = "uploadMyPlace";

        final MyPlaceRepo repo = new MyPlaceRepo();
        List<MyPlace> orders = repo.getMyPlacesNotPosted(mEmployeeId);

        if (orders.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.postMyPlace(orders);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        repo.markPosted(mEmployeeId);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });

        return orders.size();
    }

    private int uploadSalesReturn() {

        final String TAG2 = "uploadSalesReturn";


        final SalesReturnRepo repo = new SalesReturnRepo();
        List<SalesReturn> orders = repo.getOrdersForUpload(mEmployeeId);

        if (orders.size() == 0) {
            return 0;
        }

        Call<Void> dataList = apiService.postSalesReturn(orders);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        repo.markPosted(mEmployeeId);
                    } catch (Exception ex) {
                        log2(TAG2, ex.getMessage());
                    }
                } else {
                    String error = filterResponseErr(response);
                    log2(TAG2, error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log2(TAG2, t.getMessage());
            }
        });


        return orders.size();
    }

    private int uploadComplaints() {

        final String TAG2 = "uploadComplaints";

        final ComplaintRepo objImageData = new ComplaintRepo();
        List<Complaint> images = objImageData.getComplaintsForUpload(mEmployeeId);

        if (images.size() == 0) {
            return 0;
        }

        File file = null;
        String path;

        for (final Complaint img : images) {

            file = null;
            path = img.getImagePath();

            if (!TextUtils.isEmpty(path)) {

                try {
                    file = new File(path);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

                if (!file.exists()) {
                    file = null;
                }
            }

            if (file != null) {
                ApiService imgApi = ApiClient.getRetrofitImage(img).create(ApiService.class);

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                // Create a request body with file and image media type
                RequestBody fileReqBody = RequestBody.create(MEDIA_TYPE_PNG, file);

                // Create MultipartBody.Part using file request-body,file name and part name
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
                MultipartBody.Part part = MultipartBody.Part.createFormData("title", file.getName(), fileReqBody);

                //Create request body with text description and text media type
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

                Call<Void> dataList = imgApi.postComplaintWithImage(part, description, img);

                final File finalFile = file;

                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            if (finalFile != null) {
                                try {
                                    finalFile.delete();
                                } catch (Exception ex) {
                                    log2(TAG2, ex.getMessage());
                                }
                            }
//                                objImageData.delete(img);
                            objImageData.markPosted(img);
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log2(TAG2, t.getMessage());
                    }
                });
            } else {
                Call<Void> dataList = apiService.postComplaint(img);
                dataList.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            try {
                                objImageData.markPosted(img);
                            } catch (Exception ex) {
                                log2(TAG2, ex.getMessage());
                            }
                        } else {
                            String error = filterResponseErr(response);
                            log2(TAG2, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log2(TAG2, t.getMessage());
                    }
                });
            }

        }

        return images.size();

    }
}
