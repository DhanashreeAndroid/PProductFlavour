package com.salescube.healthcare.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.sysctrl.Constant;

public class UtilityActivity extends AppCompatActivity {

    private Button btnNewShopUpdate;
    private Button btnManualUpload;
    private Button btnTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        try {
            initControls();
            initListner();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {
        btnManualUpload = (Button) findViewById(R.id.util_btn_manual_upload);
        btnNewShopUpdate = (Button) findViewById(R.id.util_btn_update_new_shop);
        btnTableView = (Button) findViewById(R.id.util_table_view);
    }

    private void initListner() {

        btnManualUpload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    manualUpdate();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

        btnNewShopUpdate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewShopFlag();
            }
        });

        btnTableView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UtilityActivity.this, TableViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void manualUpdate() {

        Intent intent = new Intent(Constant.ACTION_AUTO_UPDATE);
        getApplicationContext().sendBroadcast(intent);

    }

    private void setNewShopFlag() {

        try {
            new ShopRepo().changeNewShopStatus();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private void deepakMedkrUpdattion() {
//        try {
//            new CompetitorInfoRepo().testDM(AppControl.getmEmployeeId());
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }
    }

}
