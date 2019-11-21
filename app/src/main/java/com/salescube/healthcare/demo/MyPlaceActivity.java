package com.salescube.healthcare.demo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.data.repo.MyPlaceRepo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AdapterData;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.MyLocation;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vSS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyPlaceActivity extends BaseAppCompatActivity {

    private Spinner spnPlaceType;
    private Spinner spnPlaceId;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtAddress;
    private TextView lblPlace;
    private TableRow rowPlace;
    private ProgressDialog dialog;

    private Button btnReCapture;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_place);
        title("SS Agent Place");

        try {
            initControls();
            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {

        spnPlaceType = (Spinner) findViewById(R.id.my_place_spn_type);
        spnPlaceId = (Spinner) findViewById(R.id.my_place_spn_place_id);
        txtLatitude = (TextView) findViewById(R.id.my_place_txt_lat);
        txtLongitude = (TextView) findViewById(R.id.my_place_txt_lng);
        txtAddress = (TextView) findViewById(R.id.my_place_txt_add);

        lblPlace = (TextView) findViewById(R.id.my_place_lbl_place);
        rowPlace = (TableRow) findViewById(R.id.my_place_row_place);

        btnReCapture = (Button) findViewById(R.id.my_place_btn_re_capture);
        btnSubmit = (Button) findViewById(R.id.my_place_btn_submit);

    }

    private void initData() {
        SpinnerHelper.FillPlaceType(spnPlaceType);

        findLocation();
    }

    private void initListener() {

        btnReCapture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitData();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

        spnPlaceType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null) {
                    return;
                }

                TextView tv = (TextView) view;
                String type = tv.getText().toString();
                int rowVisible = View.VISIBLE;
                String placeName = "";

                if (type == Constant.PlaceType.AGENT) {
                    placeName = "Agent Name:";
                    spnPlaceId.setAdapter(AdapterData.getAgent(MyPlaceActivity.this, AppControl.getmEmployeeId(),"---SELECT---"));
                }

                if (type == Constant.PlaceType.SS) {
                    placeName = "SS Name:";
                    rowVisible = View.GONE;
                    Toast.makeText(MyPlaceActivity.this,"SS record not found...!!",Toast.LENGTH_SHORT).show();
//                    SpinnerHelper.FillSS(spnPlaceId,AppControl.getmEmployeeId(),"--- SELECT ---");
                }

                if (type == Constant.PlaceType.HOME) {
                    rowVisible = View.GONE;
                }

                rowPlace.setVisibility(rowVisible);
                lblPlace.setText(placeName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    protected void findLocation() {

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, MyPlaceActivity.this);
        if (!enableGPS) {
            return;
        }

        txtLatitude.setText("");
        txtLongitude.setText("");
        txtAddress.setText("");

        dialog = new ProgressDialog(MyPlaceActivity.this);
        dialog.setMessage("Getting location...");
        dialog.setCancelable(true);
        dialog.show();

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

                    txtLatitude.setText(String.valueOf(latLong[0]));
                    txtLongitude.setText(String.valueOf(latLong[1]));
                }

                try {
                    getAddress(latLong);
                } catch (Exception e) {
                    dialog.dismiss();
                    Logger.e(e.getMessage());
                }

            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);
    }

    private void getAddress(double[] latLong) {

        Geocoder geoCoder;
        List<Address> addresss = new ArrayList<>();
        String formatedAddress = "";

        geoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);

        if ((latLong[0] != 0) && (latLong[0] != 0)) {

            try {
                addresss = geoCoder.getFromLocation(latLong[0], latLong[1], 1);
            } catch (IOException e) {
                dialog.dismiss();
                return;
            }

            if (addresss != null) {
                formatedAddress = "";
                if (addresss.size() > 0) {
                    int maxLines = addresss.get(0).getMaxAddressLineIndex();
                    if (maxLines > 0) {
                        for (int i = 0; i < maxLines; i++) {
                            formatedAddress += addresss.get(0).getAddressLine(i) + ", ";
                        }

                    }else{
                        formatedAddress += addresss.get(0).getAddressLine(0) + " ";
                    }
                }
            }

            txtAddress.setText(formatedAddress);
        }

        dialog.dismiss();
    }

    private void
    submitData() {

        String lat = txtLatitude.getText().toString();
        String lng = txtLongitude.getText().toString();
        String add = txtAddress.getText().toString();

        if (lat.isEmpty() || lng.isEmpty()) {
            Alert("Location not available", "Please click on re-capture or move to open space to catch location accurately.");
            return;
        }

        String placeType = spnPlaceType.getSelectedItem().toString();
        int placeId = 0;
        if (placeType == Constant.PlaceType.AGENT){
            vAgent agent = (vAgent) spnPlaceId.getSelectedItem();
            placeId = agent.getAgentId();

            if (placeId == 0) {
                Alert("Required!","Please select Agent!");
                return;
            }
        }

        if (placeType == Constant.PlaceType.SS) {
            vSS agent = (vSS) spnPlaceId.getSelectedItem();
            placeId = agent.getSsId();

            if (placeId == 0) {
                Alert("Required!","Please select SS!");
                return;
            }
        }

        MyPlace obj= new MyPlace();
        obj.setUserId(AppControl.getmEmployeeId());
        obj.setPlaceType(placeType);
        obj.setPlaceId(placeId);
        obj.setLat(lat);
        obj.setLng(lng);
        obj.setAdd(add);

        new MyPlaceRepo().insert(obj);

        AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
        alert.setTitle("Done!");
        alert.setMessage("Submitted successfully !");

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.create();
        alert.show();
    }

}
