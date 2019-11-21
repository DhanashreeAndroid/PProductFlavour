package com.salescube.healthcare.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.data.repo.OtherWorkRepo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vOtherWorkReason;
import com.salescube.healthcare.demo.view.vSysDate;

import java.util.Date;

import static com.salescube.healthcare.demo.sysctrl.AppEvent.EVENT_OTHER_WORK;

public class OtherWorkActivity extends BaseTransactionActivity {

    private Spinner spnAgent;
    private Spinner spnDate;
    private Spinner spnOtherWork;
    private EditText edtRemark;
    private Button btnSubmit;
    private Button btnCancel;
    private Button btnReport;

    private TableRow trSpnAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_work);
        title("Other Work");
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
////            actionBar.setIcon(R.mipmap.ic_logo_3);
//            actionBar.setTitle("Other Work");
//            actionBar.setDisplayShowHomeEnabled(true);
//        }

        initControls();
        initListners();
        initData();
    }

    private void initControls() {

        trSpnAgent = findViewById(R.id.tr_spn_agent);
        trSpnAgent.setVisibility(View.GONE);

        spnAgent = findViewById(R.id.other_work_spn_agent);
        spnDate = findViewById(R.id.other_work_spn_date);
        spnOtherWork = findViewById(R.id.other_work_spn_work);
        edtRemark = findViewById(R.id.other_work_edit_desc);
        btnSubmit = findViewById(R.id.other_work_btn_sumbit);
        btnCancel = findViewById(R.id.other_work_btn_cancel);
        btnReport = findViewById(R.id.other_work_btn_report);
    }

    private void initListners() {
        spnOtherWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vOtherWorkReason reason = (vOtherWorkReason) spnOtherWork.getSelectedItem();
                if (reason != null) {
                    if (reason.getReason().equalsIgnoreCase("Agent Visit")) {
                        trSpnAgent.setVisibility(View.VISIBLE);
                    } else {
                        trSpnAgent.setVisibility(View.GONE);
                    }
                    if (reason.getReason().equalsIgnoreCase("SS Visit")) {
                        Toast.makeText(OtherWorkActivity.this, "SS Not Found...!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean enableGPS = UtilityFunc.isGPSEnabled(true, OtherWorkActivity.this);
                if (!enableGPS) {
                    return;
                }

                try {
                    save();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReport.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OtherWorkActivity.this, OtherWorkReportActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

    }

    private void save() {

        OtherWork objWork;
        OtherWorkRepo objWorkRepo = new OtherWorkRepo();

        Date orderDate = null;
        vSysDate sysDate = (vSysDate) spnDate.getSelectedItem();
        if (sysDate != null) {
            orderDate = sysDate.getTrDate();
        }

        int agentId = 0;
        vAgent agent = (vAgent) spnAgent.getSelectedItem();
        if (agent != null) {
            agentId = agent.getAgentId();
        }

        String reasonStr = "";
        vOtherWorkReason reason = (vOtherWorkReason) spnOtherWork.getSelectedItem();
        if (reason != null) {
            reasonStr = reason.getReason();
        }

        if (!TextUtils.isEmpty(edtRemark.getText())) {
            reasonStr = reasonStr + " - " + edtRemark.getText().toString();
        }

        objWork = new OtherWork();
        objWork.setSoId(AppControl.getmEmployeeId());
        objWork.setOrderDate(orderDate);
        objWork.setAgentId(agentId);
        objWork.setOrherWork(reasonStr);
        objWorkRepo.insert(objWork);

        msgShort("Other Work Submitted!");

        doManualUpload("", EVENT_OTHER_WORK);
        finish();
    }

    private void initData() {

        SpinnerHelper.FillAgents(spnAgent, AppControl.getmEmployeeId(), "---N/A---");
        SpinnerHelper.FillSysDates(spnDate, AppControl.getmEmployeeId());
        SpinnerHelper.FillOtherWorkReason(spnOtherWork);
    }

    protected void msgShort(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OtherWorkActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void msgLong(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OtherWorkActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
