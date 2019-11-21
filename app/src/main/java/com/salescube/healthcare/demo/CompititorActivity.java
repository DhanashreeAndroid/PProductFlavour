package com.salescube.healthcare.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.repo.CompetitorInfoRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.ImageCompressionTask;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.TextFunc;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.AppEvent;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vProduct;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.salescube.healthcare.demo.func.ImageUtils.getRealPathFromURI;

public class CompititorActivity extends BaseTransactionActivity {

    private Spinner spnProduct;
    private EditText edtCompetitorProduct;
    private EditText edtGms;
    private EditText edtRetailerPrice;
    private EditText edtScheme;
    private EditText edtMRP;
    private Button btnTakePhoto;
    private Button btnSumbit;
    private Button btnCancel;
    private ImageView imgProduct;
    private EditText txtStock;
    private Button btnCompetitorReport;

    private String mAppShopId;
    private int mAgentId;
    private Date mTxnDate;
    private Uri mImageUri;
    private String mFileName;
    private File mImageFile;
    private final static int REQUEST_CODE_CLICK_IMAGE = 01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compititor);
        title("Competitor Info.");

        try {
            initExtra();
            initControl();
            initListners();
            initData();
        } catch (Exception e) {
            errMsg("While loading competitors",e);
        }

    }

    private void initExtra(){

        Intent intent = getIntent();
        mAppShopId = intent.getStringExtra(Constant.SHOP_APP_ID);
        mAgentId = intent.getIntExtra(Constant.AGENT_ID, 0);
        String dateString = intent.getStringExtra(Constant.ORDER_DATE);

        if (!TextFunc.isEmpty(dateString)){
            mTxnDate = DateFunc.getDate();
        }
    }

    private void initControl(){

        spnProduct = (Spinner)findViewById(R.id.comp_spn_product);
        edtCompetitorProduct = (EditText)findViewById(R.id.comp_edit_comp_product);
        edtGms = (EditText)findViewById(R.id.comp_edit_gms);
        edtRetailerPrice = (EditText)findViewById(R.id.comp_edit_retailer_rate);
        edtScheme = (EditText)findViewById(R.id.comp_edit_scheme);
        edtMRP = (EditText)findViewById(R.id.comp_edit_mrp);
        imgProduct = (ImageView) findViewById(R.id.comp_img_product);
        txtStock = (EditText)findViewById(R.id.comp_edit_stock);

        btnTakePhoto = (Button) findViewById(R.id.comp_btn_take_photo);
        btnSumbit = (Button) findViewById(R.id.comp_btn_submit);
        btnCancel = (Button) findViewById(R.id.comp_btn_cancel);
        btnCompetitorReport = (Button) findViewById(R.id.comp_btn_report);

    }

    private void initListners(){

        btnTakePhoto.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    takePhoto();
                } catch (Exception e) {
                    errMsg("While photo capturing",e);
                }
            }
        });

        btnSumbit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                boolean enableGPS = UtilityFunc.isGPSEnabled(true, CompititorActivity.this);
                if (!enableGPS) {
                    return;
                }

                try {
                    submit();
                } catch (Exception e) {
                    errMsg("While submitting!",e);
                }


            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCompetitorReport.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(CompititorActivity.this, CompititorReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    Alert("Error!", e.getMessage());
                }

            }
        });

    }

    private void initData(){
        SpinnerHelper.FillProducts(spnProduct);
    }

    private void submit(){

        vProduct pop = ((vProduct) spnProduct.getSelectedItem());
        String competitorProduct = edtCompetitorProduct.getText().toString().trim();
        String gms = edtGms.getText().toString();
        double retailerRate = Parse.toDbl(edtRetailerPrice.getText());
        String scheme = edtScheme.getText().toString().trim();
        double mrp =  Parse.toDbl(edtMRP.getText());
        String stock  = txtStock.getText().toString();

        if (TextFunc.isEmpty(competitorProduct)){
            edtCompetitorProduct.setError("Competitor product name required!");
            return;
        }

        if (pop == null){
            msgShort("Please Select Product!");
            return;
        }

        int productId = pop.getProductId();
        if (productId == 0) {
            msgShort("Please Select Product!");
            return;
        }

        String imageName = "";
        String imagePath = "";

        if (mImageFile != null) {
            imageName =  mImageFile.getName();
            imagePath =  getRealPathFromURI(mImageUri.toString());
        }

        CompetitorInfoRepo popShopRepo = new CompetitorInfoRepo();
        CompetitorInfo popShop = new CompetitorInfo();

        popShop.setSoId(AppControl.getmEmployeeId());
        popShop.setTrDate(mTxnDate);
        popShop.setAgentId(mAgentId);
        popShop.setAppShopId(mAppShopId);
        popShop.setProductId(productId);
        popShop.setCompetitorProduct(competitorProduct);
        popShop.setGms(gms);
        popShop.setRetailerRate(retailerRate);
        popShop.setScheme(scheme);
        popShop.setMrp(mrp);
        popShop.setStock(stock);
        popShop.setImageName(imageName);
        popShop.setImagePath(imagePath);

        popShopRepo.insert(popShop);

        msgShort("Competitor information submitted!");

        doManualUpload("", AppEvent.EVENT_COMPETITOR_INFO);

        finish();
    }

    private void takePhoto(){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        int soid = AppControl.getmEmployeeId();
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String dayTime = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
        mFileName = "COM_" + soid + "_" +  dayTime + ".JPG";

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
            imgProduct.setImageURI(mImageUri);
            new ImageCompressionTask().execute(mImageUri.toString());
        }
    }

}
