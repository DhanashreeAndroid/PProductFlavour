package com.salescube.healthcare.demo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.salescube.healthcare.demo.ApiClient;
import com.salescube.healthcare.demo.ApiService;
import com.salescube.healthcare.demo.LeaveActivity;
import com.salescube.healthcare.demo.LoginActivity;
import com.salescube.healthcare.demo.NewMainActivity;
import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.SharePref;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.MyLocation;
import com.salescube.healthcare.demo.sysctrl.XProgressBar;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class LatestAttendanceActivity extends AppCompatActivity {

    ProgressDialog dialog;
    private TextView lbl_att_time;
    private TextView lbl_day_date;
    private TextView lbl_emp_name;
    private ImageView refreshlocation;
    private Button dayIn;
    private String geoAddress;
    private TextView address;
    private Double Lat;
    private Double longitute;
    private UserRepo repo;
    private SharePref sharePref;
    private User user;
    private Button dayOut;
    private SysDate sysDate;
    private Boolean isConnected;
    private AlertDialog mInternetDialog;
    private static final int WIFI_ENABLE_REQUEST = 0x1006;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_attendance);
        try {
            init();
            initlistener();

            new fetchlocationdata().execute();
        } catch (Exception e) {
            Log.e("Test", "" + e);
        }
    }

    private void initlistener() {

        refreshlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new fetchlocationdata().execute();

            }
        });

        dayIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isConnected =  UtilityFunc.IsConnected(getApplicationContext());

                if (isConnected){
                    showRadioButtonDialog();
                }else {
                    Toast.makeText(getApplicationContext(),"No internet connection found",Toast.LENGTH_SHORT).show();
                }



            }
        });

        dayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String atttype = "Day Out";
                String lat = String.valueOf(Lat);
                String long1 = String.valueOf(longitute);
                int soId = user.getEmployeeId();
                String imei = user.getImeiNo();
                Date todayDate = DateFunc.getTodaysDate();
                String Attdate = DateFunc.getDateStr(todayDate);

                //Toast.makeText(getApplicationContext(), atttype + " " + lat + " " + long1 + " " + soId + " " + imei + " " + Attdate, Toast.LENGTH_SHORT).show();
                sysDate.setAttType("day close");
                sysDate.setLatitude(lat);
                sysDate.setLongitude(long1);
                sysDate.setSoId(soId);
                sysDate.setImei(imei);
                sysDate.setTrDate(DateFunc.getDate(Attdate));
                sysDate.setCreatedDateTime(DateFunc.getTodaysDateTime());

                Boolean isConnected =  UtilityFunc.IsConnected(getApplicationContext());

                if (isConnected){

                    UploadData(sysDate, "DayOut");
                }

                dayOut.setEnabled(false);
                dayOut.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));

            }
        });
    }

    private void init() {

        lbl_att_time = findViewById(R.id.lbl_att_time);
        lbl_day_date = findViewById(R.id.lbl_day_date);
        lbl_emp_name = findViewById(R.id.lbl_emp_name);
        refreshlocation = findViewById(R.id.refreshlocation);
        dayIn = findViewById(R.id.btn_mark_attendance);
        address = findViewById(R.id.address);
        dayOut = findViewById(R.id.dayOut);
        sharePref = new SharePref(getApplicationContext());

        repo = new UserRepo();
        user = repo.getUser(sharePref.getSOID());

        lbl_emp_name.setText(user.getEmployeeName());
        lbl_att_time.setText(DateFunc.getTimeStr());

        String day = DateFunc.getDayAndDateStr();

        lbl_day_date.setText(day.toUpperCase());

        sysDate = new SysDate();


        Boolean dayInStatus = sharePref.isDayInDone();

        if (dayInStatus == true) {
            dayIn.setEnabled(false);
            dayIn.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));


            dayIn.setVisibility(View.GONE);
            dayOut.setVisibility(View.VISIBLE);

            dayOut.setEnabled(true);
            dayOut.setBackgroundColor(getResources().getColor(R.color.colorPrimary));



        }

        Boolean dayOutStatus = sharePref.isDayOutDone();

        if (dayOutStatus == true) {
            dayOut.setEnabled(false);
            dayOut.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));
        }


    }

    private void showRadioButtonDialog() {
        final String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
        int itemSelected = 0;
        final String[] attType = new String[1];

        new AlertDialog.Builder(this)
                .setTitle("Select attendance type")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {

                        attType[0] = singleChoiceItems[selectedIndex];

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        String lat = String.valueOf(Lat);
                        String long1 = String.valueOf(longitute);
                        int soId = user.getEmployeeId();
                        String imei = user.getImeiNo();
                        Date todayDate = DateFunc.getTodaysDate();
                        String Attdate = DateFunc.getDateStr(todayDate);

                        if (attType[0] == null) {
                            attType[0] = "Retailing";
                        }

                        sysDate.setAttType(attType[0]);
                        sysDate.setLatitude(lat);
                        sysDate.setLongitude(long1);
                        sysDate.setSoId(soId);
                        sysDate.setImei(imei);
                        sysDate.setTrDate(DateFunc.getDate(Attdate));
                        sysDate.setCreatedDateTime(DateFunc.getTodaysDateTime());

                       // Toast.makeText(getApplicationContext(), attType[0] + " " + lat + " " + long1 + " " + soId + " " + imei + " " + Attdate, Toast.LENGTH_SHORT).show();




                        if (isConnected){

                            if (!attType[0].equalsIgnoreCase("Leave")){

                                UploadData(sysDate, "DayIn");
                            }


                        }else {
                            Toast.makeText(getApplicationContext(),"No internet connection found",Toast.LENGTH_SHORT).show();
                        }


                        dayOut.setEnabled(true);
                        dayOut.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


                        if (attType[0].equalsIgnoreCase("Leave")){

                            Intent homeIntent = new Intent(LatestAttendanceActivity.this, LeaveActivity.class);
                            homeIntent.putExtra("SysData",sysDate);
                            startActivity(homeIntent);

                        }else {
                            Intent homeIntent = new Intent(LatestAttendanceActivity.this, NewMainActivity.class);
                            startActivity(homeIntent);
                            finish();

                        }


                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

    }

    public class fetchlocationdata extends AsyncTask<Object, Object, Object> {


        final ProgressDialog progressDoalog = new ProgressDialog(LatestAttendanceActivity.this);

        private AsyncTask<Object, Object, Object> fetchLocation  = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fetchLocation = this;

            progressDoalog.setMax(100);

            progressDoalog.setMessage("Please Wait....");
            progressDoalog.setTitle("Fetching location");
            progressDoalog.show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   progressDoalog.dismiss();
                    cancel(true);

                    if (address.getText().toString().equalsIgnoreCase("")){

                        address.setText("Location not found.");
                        address.setTextColor(Color.parseColor("#F27474"));

                        Lat = 0.0;
                        longitute = 0.0;

                    }

                }
            }, 10000);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object... objects) {


            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                int count = 0;

                @Override
                public void gotLocation(String provider, Location location) {
                    double[] latLong = new double[2];

                    if (location == null) {
                        latLong[0] = 0;
                        latLong[1] = 0;
                    } else {
                        latLong[0] = location.getLatitude();
                        latLong[1] = location.getLongitude();

                        String locStr = String.valueOf(latLong[0]) + "," + String.valueOf(latLong[1]);

                        Lat = latLong[0];
                        longitute = latLong[1];

                        // address.setText(locStr);
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        String result = null;

                        String city = null;
                        String address1 = null;
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latLong[0], latLong[1], 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            geoAddress = address1;

                            address.setText(address1);
                            address.setTextColor(Color.parseColor("#575757"));
                            progressDoalog.dismiss();

                        } catch (IOException e) {
                            e.printStackTrace();
                            progressDoalog.dismiss();
                        }

                    }

                }
            };

            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(getApplicationContext(), locationResult);
            return null;
        }
    }


    private  void  UploadData(SysDate sysDat , final String status){

        final String TAG = "saveAttendance";

        ApiService apiService =  ApiClient.getClient().create(ApiService.class);
        Call<Void> dataList = apiService.saveDayInDayOut(sysDat);
        dataList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                       if (status.equalsIgnoreCase("DayIn")){
                           sharePref.setDayIn();
                       }else{
                           sharePref.setDayOut();
                           finish();
                       }
                    } catch (Exception ex) {
                        Log.d(TAG ,""+ex);
                    }
                } else {
                    Log.d(TAG , response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private void showNoInternetDialog() {

        if (mInternetDialog != null && mInternetDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Internet Disabled!");
        builder.setMessage("No active Internet connection found.");
        builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(gpsOptionsIntent, WIFI_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mInternetDialog = builder.create();
        mInternetDialog.show();
    }


    private void checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

        } else {
            showNoInternetDialog();
        }
    }




}
