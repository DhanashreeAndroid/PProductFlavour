package com.salescube.healthcare.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.POP;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.data.repo.POPRepo;
import com.salescube.healthcare.demo.data.repo.POPShopRepo;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.func.ImageCompressionTask;
import com.salescube.healthcare.demo.func.Parse;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class POPActivity extends BaseTransactionActivity {


    private Spinner spnPOPList;
    private EditText editQty;
    private Button btnImageCapture;
    private Button btnSubmit;
    private Button btnCancel;
    private ImageView imgView;

    private String mAppShopId;
    private int mAgentId;
    private Date mTxnDate;

    private final static int REQUEST_CODE_CLICK_IMAGE = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        title("POP");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setIcon(R.mipmap.ic_logo_3);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }
        Log.d("POP" , "onCreate");

        Intent intent = getIntent();
        mAppShopId = intent.getStringExtra(Constant.SHOP_APP_ID);
        mAgentId = intent.getIntExtra(Constant.AGENT_ID, 0);
        mTxnDate = DateFunc.getDate(intent.getStringExtra(Constant.ORDER_DATE));

        try {
            initControls();
            initListeners();
            loadData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }



    }

    private void initControls() {

        spnPOPList = (Spinner) findViewById(R.id.pop_spn_pop_name);
        editQty = (EditText) findViewById(R.id.pop_txt_qty);

        btnImageCapture = (Button) findViewById(R.id.pop_btn_pop_capture);
        btnSubmit = (Button) findViewById(R.id.pop_btn_submit);
        btnCancel = (Button) findViewById(R.id.pop_btn_cancel);
        imgView = (ImageView) findViewById(R.id.pop_img_view);

    }

    private Uri mImageUri;
    private String mFileName;
    private File mImageFile;

    private void initListeners() {

        btnImageCapture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                int soid = AppControl.getmEmployeeId();
                File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String dayTime = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
                mFileName = "POP_" + soid + "_"  + dayTime + ".JPG";

                mImageFile = new File(directory, mFileName);
                mImageUri = Uri.fromFile(mImageFile);

                SharedPreferences sharedpreferences = getSharedPreferences("pref", MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("filePath", mFileName);
                editor.commit();

                Intent intentImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentImage.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                if (intentImage.resolveActivity(getPackageManager()) != null) {
                    try{
                    startActivityForResult(intentImage, REQUEST_CODE_CLICK_IMAGE);
                    Log.d("POP" , "Intent Called");
                    }catch(Exception ex){
                        Log.d("POPException",""+ex.getMessage());
                    }
                }

            }
        });

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isGPSOn = UtilityFunc.isGPSEnabled(true, POPActivity.this);
                if (!isGPSOn) {
                    return;
                }

                submit();
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageFile != null) {
                    mImageFile.delete();
                }
                finish();
            }
        });
    }

    private void submit(){

        POP pop = ((POP) spnPOPList.getSelectedItem());
        int popQty = Parse.toInt(editQty.getText());
        int popId = pop.getPopId();

        if (pop.getPopId() == 0) {
            msgShort("Please Select POP!");
            return;
        }

        if (popQty <= 0) {
            msgShort("Please Enter POP Quantity.");
            return;
        }


        String imageName = "";
        String imagePath = "";

        if (imgView.getTag() != null) {
            imagePath = imgView.getTag().toString();
            imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        }

        POPShopRepo popShopRepo = new POPShopRepo();
        POPShop popShop = new POPShop();

        popShop.setSoId(AppControl.getmEmployeeId());
        popShop.setAppShopId(mAppShopId);
        popShop.setTrDate(mTxnDate);
        popShop.setPopId(popId);
        popShop.setPopQty(popQty);
        popShop.setAgentId(mAgentId);
        popShop.setImageName(imageName);
        popShop.setImagePath(imagePath);
        popShopRepo.insert(popShop);

        doManualUpload(popShop.getAppShopId(), Constant.AppEvent.POP);
        msgShort("POP Entry Submitted!");

        finish();
    }

    private void loadData() {

        POPRepo objPOPData = new POPRepo();
        List<POP> popList = objPOPData.getPOPList(AppControl.getmEmployeeId());

        popList.add(0, new POP(0, "---Select---"));

        ArrayAdapter<POP> adpReason = new ArrayAdapter<>(POPActivity.this, android.R.layout.simple_spinner_item, popList);
        adpReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPOPList.setAdapter(adpReason);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("POP" , "onActivityResult");
        try{
            if (resultCode != RESULT_OK) {
                return;
            }

            if (requestCode == REQUEST_CODE_CLICK_IMAGE) {

                SharedPreferences sharedpreferences = getSharedPreferences("pref", MODE_PRIVATE);
                File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mFileName = sharedpreferences.getString("filePath", "");
                mImageFile = new File(directory, mFileName);
                mImageUri = Uri.fromFile(mImageFile);
                imgView.setImageURI(mImageUri);
                imgView.setTag(mImageUri.getPath());
                new ImageCompressionTask().execute(mImageUri.toString());
            }
        }catch(Exception ex){
            Log.d("POPException","\nOnActivityResult : "+ ex.getMessage());
        }
    }


}
