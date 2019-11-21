package com.salescube.healthcare.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.TextFunc;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.MyLocation;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vShop;
import com.salescube.healthcare.demo.view.vShopType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ShopAddActivity extends BaseAppCompatActivity implements Spinner.OnItemSelectedListener {

    private EditText txtShopName;
    private EditText txtRegNo;
    private EditText txtGSTNo;
    private EditText txtOwnerName;
    private EditText txtContactNo;
    private Spinner spnShopType;
    private Spinner spnShopRank;
    private Spinner spnShopStatus;
    private CheckBox chkSMSAlert;
    private TextView lblReplaceWith;
    private Spinner spnReplaceWith;
    private TextView lblHeading;
    private TextView txtLocation;
    private TextView txtAddress;

    private Button btnSubmit;
    private Button btnCancel;
    private Button btnLocationCapture;
    private int mLocalityId;
    private int mShopIdentity;
    private int mShopId;
    private String mAppShopId;
    private KeyListener keyListenerShopName;
    private String mShopOldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_add);

        try {
            getExtras();
            initConttrol();
            initListners();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void getExtras() {

        Intent thisIntent = this.getIntent();
        mAppShopId = thisIntent.getStringExtra(Constant.SHOP_APP_ID);
        mLocalityId = thisIntent.getIntExtra(Constant.LOCALITY_ID, 0);
        mShopIdentity = thisIntent.getIntExtra(Constant.IDENTITY, 0);
        mShopId = thisIntent.getIntExtra(Constant.SHOP_ID, 0);

        if (TextFunc.isEmpty(mAppShopId)) {
            mAppShopId = "";
        }
    }

    private void initConttrol() {

        txtShopName = (EditText) findViewById(R.id.shop_add_txt_shop_name);
        txtRegNo = (EditText) findViewById(R.id.shop_add_txt_reg_number);
        txtGSTNo = (EditText) findViewById(R.id.shop_add_txt_gst_number);
        txtOwnerName = (EditText) findViewById(R.id.shop_add_txt_owner_name);
        txtContactNo = (EditText) findViewById(R.id.shop_add_txt_contact_no);
        spnShopType = (Spinner) findViewById(R.id.shop_add_spn_shop_type);
        spnShopStatus = (Spinner) findViewById(R.id.shop_add_spn_shop_status);
        spnShopRank = (Spinner) findViewById(R.id.shop_add_spn_shop_rank);
        chkSMSAlert = (CheckBox) findViewById(R.id.shop_add_chk_sms_alert);
        lblReplaceWith = (TextView) findViewById(R.id.shop_add_lbl_replace_shop);
        spnReplaceWith = (Spinner) findViewById(R.id.shop_add_spn_replace_shop);
        txtLocation = (TextView) findViewById(R.id.shop_add_txt_location);
        txtAddress = (TextView) findViewById(R.id.my_place_txt_add);

        btnSubmit = (Button) findViewById(R.id.shop_add_btn_submit);
        btnCancel = (Button) findViewById(R.id.shop_add_btn_cancel);
        btnLocationCapture = (Button) findViewById(R.id.shop_add_btn_location);
        lblHeading = (TextView) findViewById(R.id.shop_add_lbl_heading);

        if (mAppShopId == "") {
            spnShopStatus.setEnabled(false);
            spnShopStatus.setOnItemSelectedListener(null);

            lblReplaceWith.setVisibility(View.GONE);
            spnReplaceWith.setVisibility(View.GONE);
            lblHeading.setText("New Shop");
            title("New Shop");
        } else {

            keyListenerShopName = txtShopName.getKeyListener();
            txtShopName.setInputType(InputType.TYPE_NULL);
            txtShopName.setKeyListener(null);
            spnShopStatus.setOnItemSelectedListener(this);

            lblHeading.setText("Modify Shop");
            title("Modify Shop");
        }

        SpinnerHelper.FillShopStatus(spnShopStatus);
        SpinnerHelper.FillShopType(spnShopType);
        SpinnerHelper.FillShopRank(spnShopRank);
        SpinnerHelper.FillShopsExlude(spnReplaceWith, AppControl.getmEmployeeId(), "", mLocalityId, mShopId);

//        spnShopRank.setOnItemSelectedListener(this);

    }

    private void initListners() {

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    submit();
                } catch (Exception e) {
                    Logger.log(Logger.ERROR, "ShopAdd", e.getMessage(), e);
                    msgShort("Error!" + e.getMessage());
                }
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLocationCapture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    findLocation();
                } catch (Exception e) {
                    Logger.log(Logger.ERROR, "ShopAdd", e.getMessage(), e);
                    msgShort("Error while capturing location " + e.getMessage());
                }
            }
        });

    }

    private ProgressDialog dialog;

    protected void findLocation() {

        boolean enableGPS = UtilityFunc.isGPSEnabled(true, ShopAddActivity.this);
        if (!enableGPS) {
            return;
        }

        txtLocation.setText("");
        txtAddress.setText("");

        dialog = new ProgressDialog(ShopAddActivity.this);
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

                    String locStr = String.valueOf(latLong[0]) + "," + String.valueOf(latLong[1]);
                    txtLocation.setText(locStr);
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

                    } else {
                        formatedAddress += addresss.get(0).getAddressLine(0) + " ";
                    }
                }
            }

            txtAddress.setText(formatedAddress);

        }

        dialog.dismiss();
    }

    private void initData() {

        if (!TextFunc.isEmpty(mAppShopId)) {

            ShopRepo repo = new ShopRepo();
            Shop objShop = repo.getShopById(mAppShopId);
            if (objShop == null) {
                return;
            }

            txtShopName.setText(objShop.getShopName());
            txtRegNo.setText(objShop.getRegNo());
            txtGSTNo.setText(objShop.getGstNo());
            txtOwnerName.setText(objShop.getOwnerName());
            txtContactNo.setText(objShop.getContactNo());
            spnShopType.setSelection(getShopTypeIndex(objShop.getShopTypeId()));
            spnShopStatus.setSelection(getShopStatusIndex(objShop.getShopStatus()));
            spnShopRank.setSelection(getShopRankIndex(objShop.getShopRank()));
            chkSMSAlert.setChecked(objShop.getAutoSMS());
            spnReplaceWith.setSelection(getShopIndex(objShop.getReplacedWith()));
            txtLocation.setText(objShop.getShopLocation());
            mShopOldName = objShop.getShopName();

            if (!TextUtils.isEmpty(objShop.getShopLocation())) {
                if (objShop.getShopLocation().contains(",")) {
                    String[] arrLocation = objShop.getShopLocation().split(",");
                    double[] latLong = new double[2];
                    latLong[0] = Double.parseDouble(arrLocation[0]);
                    latLong[1] = Double.parseDouble(arrLocation[1]);
                    getAddress(latLong);
                }
            }
        }

    }

    private int getShopTypeIndex(int shopTypeId) {

        int index = 0;
        vShopType product;

        for (int i = 0; i < spnShopType.getCount(); i++) {
            product = (vShopType) spnShopType.getItemAtPosition(i);
            if (product.getShopTypeId() == shopTypeId) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int getShopIndex(String shopId) {

        int index = 0;
        vShop product;

        for (int i = 0; i < spnReplaceWith.getCount(); i++) {
            product = (vShop) spnReplaceWith.getItemAtPosition(i);
            if (product.getAppShopId().equalsIgnoreCase(shopId)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int getShopRankIndex(String shopRank) {

        int index = 0;
        String rank;

        if (shopRank != null) {
            for (int i = 0; i < spnShopRank.getCount(); i++) {
                rank = spnShopRank.getItemAtPosition(i).toString();
                if (TextFunc.isEqual(rank, shopRank)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private int getShopStatusIndex(String shopStatus) {

        int index = 0;
        String status;

        for (int i = 0; i < spnShopStatus.getCount(); i++) {
            status = spnShopStatus.getItemAtPosition(i).toString();
            if (TextFunc.isEqual(status, shopStatus)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void submit() {


        if (!validate()) {
            return;
        }

        vShopType shopType = (vShopType) spnShopType.getSelectedItem();
        String shopStatus = spnShopStatus.getSelectedItem().toString();
        String rank = spnShopRank.getSelectedItem().toString();

        if (mAppShopId == "") {

            // N[SOOID][yyMMddHHmmss]

            String dateMonth = DateFunc.getDateStr("yyMMddHHmmss");
            mAppShopId = "N_" + AppControl.getmEmployeeId() + "_" + dateMonth;

            Shop objShop = new Shop();
            objShop.setAppShopId(mAppShopId);
            objShop.setShopName(txtShopName.getText().toString());
            objShop.setRegNo(Parse.toStr(txtRegNo.getText().toString()));
            objShop.setGstNo(Parse.toStr(txtGSTNo.getText().toString()));
            objShop.setOwnerName(Parse.toStr(txtOwnerName.getText().toString()));
            objShop.setContactNo(Parse.toStr(txtContactNo.getText().toString()));
            objShop.setLocalityId(Parse.toInt(mLocalityId));
            objShop.setShopTypeId(Parse.toInt(shopType.getShopTypeId()));
            objShop.setShopLocation(Parse.toStr(txtLocation.getText().toString()));
            objShop.setShopStatus(Constant.ShopStatus.LIVE);
            if (spnShopRank.getSelectedItemPosition() != 0) {
                objShop.setShopRank(rank);
            }
            objShop.setAutoSMS(chkSMSAlert.isChecked());
            objShop.setSoId(AppControl.getmEmployeeId());
            objShop.setUpdatable(true);
            objShop.setReplacedWith("0");
            objShop.setUpdatedBy(AppControl.getmEmployeeId());

            new ShopRepo().insert(objShop);
            msgShort("New shop created!");

        } else {

            vShop replaceShop = (vShop) spnReplaceWith.getSelectedItem();

            Shop objShop = new Shop();
            objShop.setId(mShopIdentity);
            objShop.setAppShopId(mAppShopId);
            objShop.setShopName(txtShopName.getText().toString());
            objShop.setRegNo(txtRegNo.getText().toString());
            objShop.setGstNo(txtGSTNo.getText().toString());
            objShop.setOwnerName(txtOwnerName.getText().toString());
            objShop.setContactNo(txtContactNo.getText().toString());
            objShop.setShopTypeId(shopType.getShopTypeId());
            objShop.setAutoSMS(chkSMSAlert.isChecked());
            objShop.setUpdatedBy(AppControl.getmEmployeeId());
            objShop.setShopLocation(Parse.toStr(txtLocation.getText().toString()));
            objShop.setUpdatable(true);
            if (spnShopRank.getSelectedItemPosition() != 0) {
                objShop.setShopRank(rank);
            }

            if (shopStatus.equals(Constant.ShopStatus.LIVE) || shopStatus.equals(Constant.ShopStatus.NAME_CHANGE)) {
                objShop.setReplacedWith("0");
                objShop.setShopStatus(Constant.ShopStatus.LIVE);
            } else if (shopStatus.equals(Constant.ShopStatus.DUPLICATE)) {
                objShop.setReplacedWith(replaceShop.getAppShopId());
                objShop.setShopStatus(Constant.ShopStatus.DUPLICATE);
            } else if (shopStatus.equals(Constant.ShopStatus.CLOSE)) {
                objShop.setReplacedWith("0");
                objShop.setShopStatus(Constant.ShopStatus.CLOSE);
            }

            new ShopRepo().update(objShop);
            msgShort("Shop info updated!");
        }

        Intent intent = new Intent();
        intent.putExtra("Update", "Y");
        intent.putExtra("AppShopId", mAppShopId);
        setResult(RESULT_OK, intent);

        finish();

    }


    private boolean validate() {

        boolean isValid = true;
        String shopName = txtShopName.getText().toString();

        if (TextUtils.isEmpty(shopName)) {
            txtShopName.setError("");
            Alert("Required!", "Please enter shop name.");
            return false;
        }

        if (Pattern.matches("[a-z A-Z 0-9 . / * +]+", shopName) == false) {
            txtShopName.setError("Enter in english only.");
            Alert("Required!", "Please enter information in English only.");
            return false;
        }

        String shopStatus = spnShopStatus.getSelectedItem().toString();
        if (shopStatus != Constant.ShopStatus.DUPLICATE && shopStatus != Constant.ShopStatus.CLOSE) {
            boolean isExist = new ShopRepo().isExist(mAppShopId, shopName, mLocalityId);
            if (isExist) {
                txtShopName.setError("");
                Alert("Duplicate!", "'" + shopName + "' Shop name already exist in this locality.");
                return false;
            }
        }

        //Rank sholud not compulsory : Ticket #000028 => Assigned by Vinayak, Solved by Dhanashree
        /*String shopRank = spnShopRank.getSelectedItem().toString();
        if(shopRank == Constant.ShopRank.SELECT){
            TextView errorText = (TextView)spnShopRank.getSelectedView();
            errorText.setError("Please Select Shop Rank");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText();
            Toast.makeText(ShopAddActivity.this,"Please Select Shop Rank",Toast.LENGTH_SHORT).show();
            return false;
        }*/

        return isValid;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String status = spnShopStatus.getSelectedItem().toString();
        if (status == Constant.ShopStatus.DUPLICATE) {
            //SpinnerHelper.FillShopsExlude(spnShopType, AppControl.getmEmployeeId(), "", mLocalityId, mShopId);
            lblReplaceWith.setVisibility(View.VISIBLE);
            spnReplaceWith.setVisibility(View.VISIBLE);
        } else {
            lblReplaceWith.setVisibility(View.GONE);
            spnReplaceWith.setVisibility(View.GONE);
        }

        if (status == Constant.ShopStatus.NAME_CHANGE) {
            txtShopName.setKeyListener(keyListenerShopName);
            txtShopName.requestFocus();
        } else {
            txtShopName.setText(mShopOldName);
            txtShopName.setKeyListener(null);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
