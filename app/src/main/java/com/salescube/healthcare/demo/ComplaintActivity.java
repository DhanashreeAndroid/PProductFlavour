package com.salescube.healthcare.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.ctrl.TSAdapter;
import com.salescube.healthcare.demo.data.model.Complaint;
import com.salescube.healthcare.demo.data.repo.AgentLocalityRepo;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintTypeRepo;
import com.salescube.healthcare.demo.data.repo.LocalityRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.RouteRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.func.ImageCompressionTask;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.AppEvent;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vArea;
import com.salescube.healthcare.demo.view.vIssue;
import com.salescube.healthcare.demo.view.vIssueSubType;
import com.salescube.healthcare.demo.view.vLocality;
import com.salescube.healthcare.demo.view.vProduct;
import com.salescube.healthcare.demo.view.vProductSKU;
import com.salescube.healthcare.demo.view.vRoute;
import com.salescube.healthcare.demo.view.vShop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComplaintActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spnReportedBy;
    private TextView lblAgent;
    private Spinner spnAgent;
    private TextView lblArea;
    private Spinner spnArea;
    private TextView lblRoute;
    private Spinner spnRoute;
    private TextView lblLocality;
    private Spinner spnLocality;
    private TextView lblShop;
    private Spinner spnShop;
    private EditText txtCustomerName;
    private EditText txtCustomerContactNo;
    private EditText txtPlace;
    private TextInputLayout layPlace;
    private Spinner spnComplaintAbout;
    private Spinner spnProduct;
    private Spinner spnSKU;
    private TextView lblCriteria;
    private Spinner spnCriteria;
    private TextView lblComplaint;
    private Spinner spnComplaint;
    private TextInputLayout layOthetrComplaint;
    private EditText txtOtherComplaint;
    private EditText txtRemark;

    private TextInputLayout layBatch;
    private EditText txtBatch;

    private TextInputLayout layQty;
    private EditText txtQty;

    private Button btnTakePhoto;
    private Button btnSubmit;

    private List<vIssue> lstIssue;
    private List<vProductSKU> lstProductSKU;
    private TSAdapter adpProduct;
    private TSAdapter adpProductSku;
    private List<vIssueSubType> lstIssueSubType;
    private TSAdapter adpComplaint;

    private String mAppShopId;
    private int mAgentId;
    private Date mTxnDate;
    private Uri mImageUri;
    private String mFileName;
    private File mImageFile;
    private final static int REQUEST_CODE_CLICK_IMAGE = 01;
    private ImageView imgProduct;

    private List<vAgent> lstAgent;
    private List<vArea> lstArea;
    private List<vRoute> lstRoutes;
    private List<vLocality> lstLocalities;
    private List<vShop> lstShops;

    private TSAdapter adpAgent;
    private TSAdapter adpArea;
    private TSAdapter adpRoute;
    private TSAdapter adpLocality;
    private TSAdapter adpShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        title("Complaint");

        try {

            getExtras();
            initControl();
            initListeners();
            initData();
            loadFromOrder();
        } catch (Exception e) {
            errMsg("While loading Leave", e);
        }

    }

    int areaId = 0;
    int routeId = 0;
    int localityId = 0;
    int shopId = 0;

    boolean isSourceOrder = false;

    private void getExtras() {

        Intent thisIntent = this.getIntent();

        String source = thisIntent.getStringExtra("SOURCE");
        if (source == null || !source.equalsIgnoreCase("ORDER")) {
            return;
        }

        areaId = thisIntent.getIntExtra(Constant.AREA_ID, 0);
        routeId = thisIntent.getIntExtra(Constant.ROUTE_ID, 0);
        localityId = thisIntent.getIntExtra(Constant.LOCALITY_ID, 0);
        shopId = thisIntent.getIntExtra(Constant.SHOP_ID, 0);

        isSourceOrder = true;
    }

    private void loadFromOrder() {

        if (!isSourceOrder) {
            return;
        }

        spnReportedBy.setSelection(1);
    }

    private void initControl() {

        spnReportedBy = (Spinner) findViewById(R.id.complaint_spn_reported_by);

        lblAgent = (TextView) findViewById(R.id.complaint_lbl_agent);
        spnAgent = (Spinner) findViewById(R.id.complaint_spn_agent);

        lblArea = (TextView) findViewById(R.id.complaint_lbl_area);
        spnArea = (Spinner) findViewById(R.id.complaint_spn_area);
        lblRoute = (TextView) findViewById(R.id.complaint_lbl_route);
        spnRoute = (Spinner) findViewById(R.id.complaint_spn_route);
        lblLocality = (TextView) findViewById(R.id.complaint_lbl_locality);
        spnLocality = (Spinner) findViewById(R.id.complaint_spn_locality);
        lblShop = (TextView) findViewById(R.id.complaint_lbl_shop);
        spnShop = (Spinner) findViewById(R.id.complaint_spn_shop);

        txtCustomerName = (EditText) findViewById(R.id.complaint_txt_customer_name);
        txtCustomerContactNo = (EditText) findViewById(R.id.complaint_txt_contact_number);
        txtPlace = (EditText) findViewById(R.id.complaint_txt_place);
        layPlace = (TextInputLayout) findViewById(R.id.complaint_lay_place);
        spnComplaintAbout = (Spinner) findViewById(R.id.complaint_spn_complaint_about);

        spnProduct = (Spinner) findViewById(R.id.complaint_spn_product);
        spnSKU = (Spinner) findViewById(R.id.complaint_spn_product_sku);
        lblCriteria = (TextView) findViewById(R.id.complaint_lbl_criteria);
        spnCriteria = (Spinner) findViewById(R.id.complaint_spn_criteria);
        lblComplaint = (TextView) findViewById(R.id.complaint_lbl_complaint);
        spnComplaint = (Spinner) findViewById(R.id.complaint_spn_complaint);
        layOthetrComplaint = (TextInputLayout) findViewById(R.id.complaint_lay_otherComplaint);
        txtOtherComplaint = (EditText) findViewById(R.id.complaint_txt_otherComplaint);
        txtRemark = (EditText) findViewById(R.id.complaint_txt_remark);

        txtBatch = (EditText) findViewById(R.id.complaint_txt_batch_no);
        txtQty = (EditText) findViewById(R.id.complaint_txt_qty);

        btnSubmit = (Button) findViewById(R.id.complaint_btn_submit);
        btnTakePhoto = (Button) findViewById(R.id.complaint_btn_add_photo);

        imgProduct = (ImageView) findViewById(R.id.complaint_img);
        layOthetrComplaint.setVisibility(View.GONE);

        spnArea.setOnItemSelectedListener(this);
        spnRoute.setOnItemSelectedListener(this);
        spnLocality.setOnItemSelectedListener(this);

    }

    private void initListeners() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                try {
                    saveComplaint();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    Alert("Error!", e.getMessage());
                    return;
                }

                doManualUpload("", AppEvent.EVENT_COMPLAINT);

                AlertDialog.Builder alert = new AlertDialog.Builder(ComplaintActivity.this)
                        .setTitle("Done!")
                        .setMessage("Complaint saved successfully");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alert.create();
                alert.show();

            }
        });

        btnTakePhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    takePhoto();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    Alert("Error!", e.getMessage());
                }

            }
        });

    }

    private boolean validate() {

        List<String> errs = new ArrayList<>();
        String emptyStr = "-----";
        String val;
        boolean isCustomerDataRequired = false;

        String reportedBy = Parse.toStr(spnReportedBy.getSelectedItem().toString());

        switch (reportedBy) {
            case Constant.ComplaintBy.SS:
                val = Parse.toStr(spnArea.getSelectedItem());
                if (val.equalsIgnoreCase(emptyStr)) {
                    errs.add("Please select SS.");
                }
                break;
            case Constant.ComplaintBy.AGENT:
                val = Parse.toStr(spnArea.getSelectedItem());
                if (val.equalsIgnoreCase(emptyStr)) {
                    errs.add("Please select agent.");
                }
                break;
            case Constant.ComplaintBy.SHOP:
                val = Parse.toStr(spnShop.getSelectedItem());
                if (val.equalsIgnoreCase(emptyStr)) {
                    errs.add("Please select shop.");
                }
                break;
            default:
                isCustomerDataRequired = true;
                break;
        }

        if (isCustomerDataRequired) {
            val = Parse.toStr(txtCustomerName.getText());
            if (val.equalsIgnoreCase("")) {
                errs.add("Please enter customer name.");
            }

            val = Parse.toStr(txtPlace.getText());
            if (val.equalsIgnoreCase("")) {
                errs.add("Please enter customer place.");
            }
        }

        val = Parse.toStr(spnComplaintAbout.getSelectedItem());
        if (val.equalsIgnoreCase(emptyStr)) {
            errs.add("Please select complaint about.");
        }

        val = Parse.toStr(spnProduct.getSelectedItem().toString());
        if (val.equalsIgnoreCase(emptyStr)) {
            errs.add("Please select complaint product.");
        }

        val = Parse.toStr(spnSKU.getSelectedItem().toString());
        if (val.equalsIgnoreCase(emptyStr)) {
            errs.add("Please select product sku.");
        }

        if (reportedBy != Constant.ComplaintBy.CUSTOMER) {
            val = Parse.toStr(txtQty.getText().toString());
            if (val.equalsIgnoreCase("")) {
                errs.add("Please enter quantity.");
            }
        }

        String complaintType = Parse.toStr(spnComplaintAbout.getSelectedItem());
        if (complaintType.toUpperCase().equalsIgnoreCase("OTHER")) {
            val = Parse.toStr(txtOtherComplaint.getText());
            if (val.equalsIgnoreCase("")) {
                errs.add("Please type your complaint.");
            }
        } else {
            val = Parse.toStr(spnComplaint.getSelectedItem().toString());
            if (val.equalsIgnoreCase(emptyStr)) {
                errs.add("Please select complaint.");
            } else if (val.toUpperCase().equalsIgnoreCase("OTHER")) {
                val = Parse.toStr(txtOtherComplaint.getText());
                if (val.equalsIgnoreCase("")) {
                    errs.add("Please type your complaint.");
                }
            }
        }

        if (errs.size() == 0) {
            return true;
        }

        int i = 0;
        String strErr = "";
        for (String err : errs) {
            i += 1;
            if (strErr != "") {
                strErr += "\n";
            }

            strErr += String.format("%s.%s", i, err);
        }

        Alert("Complaint incomplete", strErr);
        return false;
    }

    TSAdapter adpComplaintAbout;
    TSAdapter adpCriteria;

    private void initData() {


        loadSSAgentShop();

        ProductRepo productRepo = new ProductRepo();
        ComplaintTypeRepo complaintTypeRepo = new ComplaintTypeRepo();

        SpinnerHelper.FillReportedBy(spnReportedBy);
        spnReportedBy.setOnItemSelectedListener(this);

        // complaint about spinner
        // List<String> complaintAbout = complaintTypeRepo.getComplaintType("SHOP");

        adpComplaintAbout = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList());
        adpComplaintAbout.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnComplaintAbout.setAdapter(adpComplaintAbout);
        spnComplaintAbout.setOnItemSelectedListener(this);

        // end

        List<vProduct> lstProduct = productRepo.getProductsAll(AppControl.getmEmployeeId());

        lstProduct.add(0, new vProduct(0, "-----"));

        adpProduct = new TSAdapter(this, android.R.layout.simple_spinner_item, lstProduct);
        adpProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProduct.setAdapter(adpProduct);
        spnProduct.setOnItemSelectedListener(this);

        lstProductSKU = productRepo.getProductSku(AppControl.getmEmployeeId());
        adpProductSku = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<vProductSKU>());
        adpProductSku.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSKU.setAdapter(adpProductSku);


        adpCriteria = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList());
        adpCriteria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriteria.setAdapter(adpCriteria);
        spnCriteria.setOnItemSelectedListener(this);

        adpComplaint = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList());
        adpComplaint.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnComplaint.setAdapter(adpComplaint);
        spnComplaint.setOnItemSelectedListener(this);

        disableCriteria(true);
    }

    private void loadSSAgentShop() {

        AgentLocalityRepo agentRepo = new AgentLocalityRepo();
        ShopRepo shopRepo = new ShopRepo();
        AgentRepo ssRepo = new AgentRepo();

        lstAgent = new AgentRepo().getAgentAll(AppControl.getmEmployeeId());
        lstArea = new AreaRepo().getAreaAll(AppControl.getmEmployeeId());
        lstRoutes = new RouteRepo().getAllRoutes(AppControl.getmEmployeeId());
        lstLocalities = new LocalityRepo().getAllLocalities(AppControl.getmEmployeeId());
        lstShops = shopRepo.getShopsAll(AppControl.getmEmployeeId());

        adpAgent = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<vAgent>());
        adpAgent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAgent.setAdapter(adpAgent);
        spnAgent.setOnItemSelectedListener(this);

        adpArea = new TSAdapter(this, android.R.layout.simple_spinner_item, lstArea);
        adpArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnArea.setAdapter(adpArea);
        spnArea.setOnItemSelectedListener(this);

        adpRoute = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<vRoute>());
        adpRoute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoute.setAdapter(adpRoute);
        spnRoute.setOnItemSelectedListener(this);

        adpLocality = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<vLocality>());
        adpLocality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLocality.setAdapter(adpLocality);
        spnLocality.setOnItemSelectedListener(this);

        adpShop = new TSAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<vShop>());
        adpShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnShop.setAdapter(adpShop);
        spnShop.setOnItemSelectedListener(this);

    }

    private void saveComplaint() {

        String reportedBy = "";
        if (spnReportedBy.getSelectedItem() != null) {
            reportedBy = spnReportedBy.getSelectedItem().toString();
        }

        String customerName = txtCustomerName.getText().toString();
        String contactNo = txtCustomerContactNo.getText().toString();

        String complaintAbout = "";
        if (spnComplaintAbout.getSelectedItem() != null) {
            complaintAbout = spnComplaintAbout.getSelectedItem().toString();
        }

        int productId = 0;
        vProduct product = (vProduct) spnProduct.getSelectedItem();
        if (product != null) {
            productId = product.getProductId();
        }

        int productSkuId = 0;
        vProductSKU sku = (vProductSKU) spnSKU.getSelectedItem();
        if (sku != null) {
            productSkuId = sku.getRlProductSkuId();
        }

        String issue = "";
        if (spnCriteria.getSelectedItem() != null) {
            issue = spnCriteria.getSelectedItem().toString();
        }

        String complaint = "";
        if (spnComplaint.getSelectedItem() != null) {
            complaint = spnComplaint.getSelectedItem().toString();
        }

        String complaintOther = Parse.toStr(txtOtherComplaint.getText());
        String remark = Parse.toStr(txtRemark.getText());

        String imageName = "";
        String imagePath = "";

        if (imgProduct.getTag() != null) {
            imagePath = imgProduct.getTag().toString();
            imageName = imagePath.substring(imagePath.lastIndexOf("/")+1);

            File file = new File(imagePath);
            if (file.exists() && !file.isDirectory()) {

            } else {
                imageName = "";
                imagePath = "";
            }
        }

        int agentId = 0;
        vAgent objAgent = getAgent();
        if (objAgent != null) {
            agentId = objAgent.getAgentId();
        }

        int areaId = getArea().getAreaId();
        int routeId = getRoute().getRouteId();
        int localityId = getLocality().getLocalityId();
        int shopId = getShop().getShopId();

        String place = Parse.toStr(txtPlace.getText());
        String batchNo = Parse.toStr(txtBatch.getText());
        String qty = Parse.toStr(txtQty.getText());

        switch (reportedBy) {
            case Constant.ComplaintBy.CUSTOMER:
                agentId = 0;
                routeId = 0;
                localityId = 0;
                break;
            case Constant.ComplaintBy.SHOP:
                break;
            case Constant.ComplaintBy.SS:
                routeId = 0;
                localityId = 0;
                shopId = 0;
                break;
            case Constant.ComplaintBy.AGENT:
                routeId = 0;
                localityId = 0;
                shopId = 0;
                break;
        }


        Complaint comp = new Complaint();
        comp.setSoId(AppControl.getmEmployeeId());
        comp.setReportedBy(reportedBy);

        comp.setAgentSSId(agentId);

        comp.setAreaId(areaId);
        comp.setRouteId(routeId);
        comp.setLocalityId(localityId);
        comp.setShopId(shopId);

        comp.setCustomerName(customerName);
        comp.setContactNo(contactNo);
        comp.setPlace(place);

        comp.setComplaintAbout(complaintAbout);
        comp.setProduct(productId);
        comp.setProductSku(productSkuId);
        comp.setCriteria(issue);
        comp.setComplaint(complaint);
        comp.setComplaintOther(complaintOther);
        comp.setRemark(remark);

        comp.setBatchNo(batchNo);
        comp.setQty(qty);

        comp.setImageName(imageName);
        comp.setImagePath(imagePath);
        new ComplaintRepo().insert(comp);

    }

    private vAgent getAgent() {

        vAgent agent = new vAgent();
        if (spnAgent.getCount() > 0) {
            agent = (vAgent) spnAgent.getSelectedItem();
        }
        return agent;
    }

    private vArea getArea() {

        vArea shop = new vArea();
        if (spnArea.getCount() > 0) {
            shop = (vArea) spnArea.getSelectedItem();
        }
        return shop;
    }

    private vRoute getRoute() {
        vRoute route = new vRoute();
        if (spnRoute.getCount() > 0) {
            route = (vRoute) spnRoute.getSelectedItem();
        }
        return route;
    }

    private vLocality getLocality() {
        vLocality shop = new vLocality();
        if (spnShop.getCount() > 0) {
            shop = (vLocality) spnLocality.getSelectedItem();
        }
        return shop;
    }

    private vShop getShop() {
        vShop shop = new vShop();
        if (spnShop.getCount() > 0) {
            shop = (vShop) spnShop.getSelectedItem();
        }
        return shop;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        vAgent objvAgent;
        vRoute objvRoute;
        vLocality objvLocality;
        vShop objvShop;
        int index = 0;

        int i = parent.getId();
        if (i == R.id.complaint_spn_reported_by) {
            final String reportedBy = spnReportedBy.getSelectedItem().toString();

            List<String> list = new ComplaintTypeRepo().getComplaintType(reportedBy);

            list.add(0, "-----");

            adpComplaintAbout.clear();
            adpComplaintAbout.addAll(list);
            adpComplaintAbout.notifyDataSetChanged();
            spnComplaintAbout.setSelection(0);

            fillCriteria();
            fillComplaints();

            reportedByChanged(reportedBy);

            final String ssAgentFilter;
            switch (reportedBy) {
                case Constant.ComplaintBy.SS:
                    ssAgentFilter = "SS";
                    lblArea.setText("SS Name");
                    break;
                case Constant.ComplaintBy.AGENT:
                case Constant.ComplaintBy.SHOP:
                    ssAgentFilter = "Agent";
                    lblArea.setText("Area Name");
                    break;
                default:
                    ssAgentFilter = "";
                    break;
            }

            if (!ssAgentFilter.equalsIgnoreCase("")) {
                // filter SS / Agent
                List<vAgent> tmpSSAgent = Lists.newArrayList(Collections2.filter(lstAgent, new Predicate<vAgent>() {
                    @Override
                    public boolean apply(vAgent input) {
                        return true;
                    }
                }));

                vAgent objSS = new vAgent();
                objSS.setAgentId(0);
                objSS.setAgentName("-----");

                tmpSSAgent.add(0, objSS);

                adpAgent.clear();
                adpAgent.addAll(tmpSSAgent);
                adpAgent.notifyDataSetChanged();

                if (areaId != 0) {
                    index = getAreaIndex(spnArea);
                    spnArea.setSelection(index);
                    areaId = 0;
                } else {
                    spnArea.setSelection(0);
                }
            }

        } else if (i == R.id.complaint_spn_complaint_about) {
            fillCriteria();
            fillComplaints();

            String complaintAbout = spnComplaintAbout.getSelectedItem().toString();
            if (complaintAbout.equalsIgnoreCase("Other")) {
                lblComplaint.setVisibility(View.GONE);
                spnComplaint.setVisibility(View.GONE);

                layOthetrComplaint.setVisibility(View.VISIBLE);
            } else {
                lblComplaint.setVisibility(View.VISIBLE);
                spnComplaint.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.complaint_spn_product) {
            vProduct objProduct = ((vProduct) spnProduct.getSelectedItem());
            final int productId = objProduct.getProductId();

            List<vProductSKU> tmpSku = Lists.newArrayList(Collections2.filter(lstProductSKU, new Predicate<vProductSKU>() {
                @Override
                public boolean apply(vProductSKU input) {
                    return input.getProductId() == productId;
                }
            }));

            vProductSKU objSku = new vProductSKU();
            objSku.setProductId(0);
            objSku.setProductSku("-----");

            tmpSku.add(0, objSku);

            adpProductSku.clear();
            adpProductSku.addAll(tmpSku);
            adpProductSku.notifyDataSetChanged();
            spnSKU.setSelection(0);

            fillCriteria();
            fillComplaints();


        } else if (i == R.id.complaint_spn_criteria) {
            fillComplaints();

        } else if (i == R.id.complaint_spn_complaint) {
            refreshOtherComplaint();

        } else if (i == R.id.complaint_spn_area) {
            vArea objArea = ((vArea) spnArea.getSelectedItem());
            final int areaId = objArea.getAreaId();

            List<vRoute> tmpRoutes = Lists.newArrayList(Collections2.filter(lstRoutes, new Predicate<vRoute>() {
                @Override
                public boolean apply(vRoute input) {
                    return input.getAreaId() == areaId;
                }
            }));

            objvRoute = new vRoute();
            objvRoute.setRouteId(0);
            objvRoute.setRouteName("-----");

            tmpRoutes.add(0, objvRoute);

            adpRoute.clear();
            adpRoute.addAll(tmpRoutes);
            adpRoute.notifyDataSetChanged();

            if (routeId != 0) {
                index = getRouteIndex(spnRoute);
                spnRoute.setSelection(index);
//                    routeId = 0;
            } else {
                spnRoute.setSelection(0);
            }


        } else if (i == R.id.complaint_spn_route) {
            objvRoute = ((vRoute) spnRoute.getSelectedItem());
            final int routeId = objvRoute.getRouteId();

            List<vLocality> tmpLocality = Lists.newArrayList(Collections2.filter(lstLocalities, new Predicate<vLocality>() {
                @Override
                public boolean apply(vLocality input) {
                    return input.getRouteId() == routeId;
                }
            }));

            objvLocality = new vLocality();
            objvLocality.setLocalityId(0);
            objvLocality.setLocalityName("-----");

            tmpLocality.add(0, objvLocality);

            adpLocality.clear();
            adpLocality.addAll(tmpLocality);
            adpLocality.notifyDataSetChanged();

            if (localityId != 0) {
                index = getLocalityIndex(spnLocality);
                spnLocality.setSelection(index, false);
//                    localityId = 0;
            } else {
                spnLocality.setSelection(0);
            }


            //spnLocality.setSelection(0);


        } else if (i == R.id.complaint_spn_locality) {
            objvLocality = ((vLocality) spnLocality.getSelectedItem());
            final int localityId = objvLocality.getLocalityId();

            List<vShop> tmpShop = Lists.newArrayList(Collections2.filter(lstShops, new Predicate<vShop>() {
                @Override
                public boolean apply(vShop input) {
                    return input.getLocalityId() == localityId;
                }
            }));

            objvShop = new vShop();
            objvShop.setShopId(0);
            objvShop.setShopName("-----");

            tmpShop.add(0, objvShop);

            adpShop.clear();
            adpShop.addAll(tmpShop);
            adpShop.notifyDataSetChanged();

            if (shopId != 0) {
                index = getShopIndex(spnShop);
                spnShop.setSelection(index);
//                    shopId = 0;
            } else {
                spnShop.setSelection(0);
            }

            //spnShop.setSelection(0);


        } else {
        }

    }

    private void reportedByChanged(String reportedBy) {

        switch (reportedBy) {
            case Constant.ComplaintBy.CUSTOMER:
                lblAgent.setVisibility(View.GONE);
                spnAgent.setVisibility(View.GONE);

                lblArea.setVisibility(View.GONE);
                spnArea.setVisibility(View.GONE);
                lblRoute.setVisibility(View.GONE);
                spnRoute.setVisibility(View.GONE);
                lblLocality.setVisibility(View.GONE);
                spnLocality.setVisibility(View.GONE);
                lblShop.setVisibility(View.GONE);
                spnShop.setVisibility(View.GONE);
                layPlace.setVisibility(View.VISIBLE);
                break;
            case Constant.ComplaintBy.SHOP:
                lblAgent.setVisibility(View.GONE);
                spnAgent.setVisibility(View.GONE);

                lblArea.setVisibility(View.VISIBLE);
                spnArea.setVisibility(View.VISIBLE);
                lblRoute.setVisibility(View.VISIBLE);
                spnRoute.setVisibility(View.VISIBLE);
                lblLocality.setVisibility(View.VISIBLE);
                spnLocality.setVisibility(View.VISIBLE);
                lblShop.setVisibility(View.VISIBLE);
                spnShop.setVisibility(View.VISIBLE);
                layPlace.setVisibility(View.GONE);
                break;
            case Constant.ComplaintBy.SS:
                lblAgent.setVisibility(View.VISIBLE);
                spnAgent.setVisibility(View.VISIBLE);

                lblArea.setVisibility(View.GONE);
                spnArea.setVisibility(View.GONE);
                lblRoute.setVisibility(View.GONE);
                spnRoute.setVisibility(View.GONE);
                lblLocality.setVisibility(View.GONE);
                spnLocality.setVisibility(View.GONE);
                lblShop.setVisibility(View.GONE);
                spnShop.setVisibility(View.GONE);
                layPlace.setVisibility(View.GONE);
                break;
            case Constant.ComplaintBy.AGENT:
                lblAgent.setVisibility(View.VISIBLE);
                spnAgent.setVisibility(View.VISIBLE);
                lblArea.setVisibility(View.GONE);
                spnArea.setVisibility(View.GONE);
                lblRoute.setVisibility(View.GONE);
                spnRoute.setVisibility(View.GONE);
                lblLocality.setVisibility(View.GONE);
                spnLocality.setVisibility(View.GONE);
                lblShop.setVisibility(View.GONE);
                spnShop.setVisibility(View.GONE);
                layPlace.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void fillComplaints() {

        adpComplaint.clear();

        String reportedBy = spnReportedBy.getSelectedItem().toString();
        String complaintAbout = spnComplaintAbout.getSelectedItem().toString();
        vProduct objvAgent = ((vProduct) spnProduct.getSelectedItem());
        String product = "";

        if (objvAgent != null) {
            product = String.valueOf(objvAgent.getProductId());
        }

        Object criteria1 = spnCriteria.getSelectedItem();
        if (criteria1 == null) {
            criteria1 = "";
        }

        if (complaintAbout.equalsIgnoreCase("Master Pack")) {
            product = "";
        }

        List<String> list = new ComplaintTypeRepo().getComplaint(reportedBy, complaintAbout, product, criteria1.toString());

        if (list.size() == 0) {
            list.add(0, "-----");
            list.add("Other");
        } else {
            list.add(0, "-----");
        }

        adpComplaint.addAll(list);
        adpComplaint.notifyDataSetChanged();
        spnComplaint.setSelection(0);

    }

    private void fillCriteria() {

        String reportedBy = spnReportedBy.getSelectedItem().toString();
        String complaintAbout = spnComplaintAbout.getSelectedItem().toString();
        vProduct objvAgent = ((vProduct) spnProduct.getSelectedItem());
        String product = String.valueOf(objvAgent.getProductId());

        if (objvAgent == null) {
            product = "";
        } else {
            if (product.equalsIgnoreCase("0") || complaintAbout.equalsIgnoreCase("Master Pack") || complaintAbout.equalsIgnoreCase("Other")) {
                product = "";
            }
        }

        adpCriteria.clear();

        List<String> criteria = new ComplaintTypeRepo().getCriteria(reportedBy, complaintAbout, product);

        if (criteria.size() != 0) {
            disableCriteria(false);

            criteria.add(0, "-----");

            adpCriteria.addAll(criteria);
            adpCriteria.notifyDataSetChanged();
            spnCriteria.setSelection(0);
        } else {
            disableCriteria(true);
        }

        //criteriaChange();
    }

    private void refreshOtherComplaint() {

        Object objComplaint = spnComplaint.getSelectedItem();
        if (objComplaint == null) {
            objComplaint = "";
        }

        if (objComplaint.toString().equalsIgnoreCase("Other")) {
            layOthetrComplaint.setVisibility(View.VISIBLE);
        } else {
            layOthetrComplaint.setVisibility(View.GONE);
        }
    }

    private void disableCriteria(boolean status) {
        if (status) {
            lblCriteria.setVisibility(View.GONE);
            spnCriteria.setVisibility(View.GONE);
        } else {
            lblCriteria.setVisibility(View.VISIBLE);
            spnCriteria.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void takePhoto() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        int soid = AppControl.getmEmployeeId();
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String dayTime = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
        mFileName = "COMPLAINT_" + soid + "_" + dayTime + ".JPG";

        mImageFile = new File(directory, mFileName);
        mImageUri = Uri.fromFile(mImageFile);

        Intent intentImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentImage.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        if (intentImage.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intentImage, REQUEST_CODE_CLICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CLICK_IMAGE) {

            Bitmap bmp = getResizedBitmap(BitmapFactory.decodeFile(mImageUri.getPath()), 700);
            imgProduct.setImageBitmap(bmp);
            imgProduct.setTag(mImageUri.getPath());
            // imgProduct.setImageURI(mImageUri);
            new ImageCompressionTask().execute(mImageUri.toString());
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private int getAreaIndex(Spinner spinner) {

        int index = 0;
        vArea product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vArea) spinner.getItemAtPosition(i);
            if (product.getAreaId() == areaId) {
                index = i;
                break;

            }
        }
        return index;
    }

    private int getRouteIndex(Spinner spinner) {

        int index = 0;
        vRoute product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vRoute) spinner.getItemAtPosition(i);
            if (product.getRouteId() == routeId) {
                index = i;
                break;

            }
        }
        return index;
    }

    private int getLocalityIndex(Spinner spinner) {

        int index = 0;
        vLocality product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vLocality) spinner.getItemAtPosition(i);
            if (product.getLocalityId() == localityId) {
                index = i;
                break;

            }
        }
        return index;
    }

    private int getShopIndex(Spinner spinner) {

        int index = 0;
        vShop product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vShop) spinner.getItemAtPosition(i);
            if (product.getShopId() == shopId) {
                index = i;
                break;

            }
        }
        return index;
    }
}
