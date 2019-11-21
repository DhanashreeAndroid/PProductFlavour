package com.salescube.healthcare.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.Attendance;
import com.salescube.healthcare.demo.data.repo.AttendanceRepo;
import com.salescube.healthcare.demo.sysctrl.AdapterData;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.sysctrl.SpinnerHelper;
import com.salescube.healthcare.demo.view.vEmployee;
import com.salescube.healthcare.demo.view.vSysDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AttendanceActivity extends BaseActivity implements Spinner.OnItemSelectedListener {


    private TableLayout mTblHead;
    private TableLayout mTblData;
    private Spinner mSpnOrderDates;
    private Spinner mSpnASM;
    private Button btnSubmit;

    private final static float WGT_SR_NO = 0.3f;
    private final static float WGT_SO_NAME = 2;
    private final static float WGT_PRSENT = 1;
    private final static float WGT_OTHER_REMARK = 2;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private ArrayAdapter<vEmployee> adpEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_attendance);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null){
            titleBar.setText("Attendance Entry");
        }

        try {
            initControls();
            initData();
            initListener();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {

        mTblHead = (TableLayout) findViewById(R.id.att_tbl_header);
        mTblData = (TableLayout) findViewById(R.id.att_tbl_data);
        mSpnOrderDates = (Spinner) findViewById(R.id.att_spn_dates);
        mSpnASM = (Spinner) findViewById(R.id.att_spn_asm);

        btnSubmit = (Button) findViewById(R.id.att_btn_submit);
    }

    private void initData() {

        SpinnerHelper.FillSysDates(mSpnOrderDates, AppControl.getmEmployeeId());
        SpinnerHelper.FillASM(mSpnASM, AppControl.getmEmployeeId(),"");

        adpEmployee = AdapterData.getEmployeeList(AttendanceActivity.this, AppControl.getmEmployeeId(),"");

        loadGridHeader();
    }

    private void initListener() {

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm() ){
                    return;
                }

                try {
                    submitData();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

        mSpnOrderDates.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSysDate date = (vSysDate) mSpnOrderDates.getSelectedItem();
                vEmployee employee = (vEmployee)mSpnASM.getSelectedItem();

                if ((date != null) && (employee != null)) {
                    loadGridData(date.getTrDate(), employee.getEmpId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpnASM.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSysDate date = (vSysDate) mSpnOrderDates.getSelectedItem();
                vEmployee employee = (vEmployee)mSpnASM.getSelectedItem();

                if ((date != null) && (employee != null)) {
                    loadGridData(date.getTrDate(), employee.getEmpId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validateForm() {

        List<ViewRow> rows = getRows(mTblData);
        Spinner spn;
        EditText editText;
        String workType;
        String remark;


        for (ViewRow row : rows) {

            spn = (Spinner)row.getView(2);
            editText = (EditText)row.getView(3);

            workType =  spn.getSelectedItem().toString();
            remark = editText.getText().toString();

            if (workType.equals(Constant.WorkType.OTHER)) {
                if (remark.trim().equals("")){
                    Alert("Validation","Please enter Other work reason.");
                    return false;
                }
            }
        }

        return true;
    }

    private void submitData() {

        int rows = mTblData.getChildCount();
        TableRow tr;

        Spinner spnEmpName;
        Spinner spnWotkType;
        EditText edtRemark;

        int empId;
        String workType;
        String remark;

        Attendance att;
        List<Attendance> attList = new ArrayList<>();
        Date orderDate = getSelectedDate().getTrDate();
        vEmployee mgr = (vEmployee) mSpnASM.getSelectedItem();
        int mgrId = mgr.getEmpId();

        for (int i = 0; i < rows; i++) {

            tr = (TableRow) mTblData.getChildAt(i);

            spnEmpName = (Spinner)tr.getChildAt(1);
            spnWotkType = (Spinner)tr.getChildAt(2);
            edtRemark = (EditText) tr.getChildAt(3);

            vEmployee emp = (vEmployee)spnEmpName.getSelectedItem();

            empId = emp.getEmpId();
            workType = spnWotkType.getSelectedItem().toString();
            remark = edtRemark.getText().toString();

            att = new Attendance();
            att.setTxnDate(orderDate);
            att.setEmpId(empId);
            att.setMgrId(mgrId);
            att.setWorkType(workType);
            att.setRemark(remark);
            att.setUserId(AppControl.getmEmployeeId());
            attList.add(att);
        }

        AttendanceRepo objAttRepo = new AttendanceRepo();

        try {
            objAttRepo.deleteAll(AppControl.getmEmployeeId(), mgrId, orderDate);
            objAttRepo.insert(attList);

            msgShort("Attendance submitted!");

        } catch (Exception e) {
            Logger.e(e.getMessage());
            msgShort("Error! while submitting attendance.");
        }

//        captureLocation("", EVENT_ATTENDANCE_APPLY);
//        manualUpload();

        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ViewRow {
        private List<View> mObjView;

        public ViewRow() {
            mObjView = new ArrayList<>();
        }

        public View getView(int index) {
            return mObjView.get(index);
        }

        public void addView(View view) {
            mObjView.add(view);
        }

    }

    private List<ViewRow> getRows(TableLayout table) {

        int rows = table.getChildCount();
        TableRow tr;
        ViewRow row;
        List<ViewRow> rowList= new ArrayList<>();

        int childCount;

        for (int i = 0; i < rows; i++) {

            tr = (TableRow) mTblData.getChildAt(i);

            row = new ViewRow();
            childCount = tr.getChildCount();
            for (int x = 0; x < childCount; x++) {
                row.addView(tr.getChildAt(x));
            }
            rowList.add(row);

        }

        return rowList;
    }

    private vSysDate getSelectedDate() {

        vSysDate date = new vSysDate();
        if (mSpnOrderDates.getCount() > 0) {
            date = (vSysDate) mSpnOrderDates.getSelectedItem();
        }
        return date;
    }

    private void loadGridHeader() {

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("#");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SR_NO));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Present");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRSENT));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Remark");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_OTHER_REMARK));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_plain);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        mTblHead.addView(tr);

    }

    private int getEmployeeIndex(Spinner spinner, int empId) {

        int index = 0;
        vEmployee product;

        for (int i = 0; i < spinner.getCount(); i++) {
            product = (vEmployee) spinner.getItemAtPosition(i);
            if (product.getEmpId() == empId) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int getWorkTypeIndex(Spinner spinner, String workType) {

        int index = 0;
        String obj;

        for (int i = 0; i < spinner.getCount(); i++) {
            obj = spinner.getItemAtPosition(i).toString();
            if (obj.equals(workType)) {
                index = i;
                break;
            }
        }

        return index;
    }

    boolean isLoading = false;

    private void  loadGridData(Date txnDate, int mgrId) {

        if (isLoading) {
            return;
        }

        isLoading = true;

        int index;
        int srNo = 0;
        mTblData.removeAllViews();

        List<Attendance> objList = new AttendanceRepo().getAttendance(AppControl.getmEmployeeId(), txnDate, mgrId);

        for (Attendance obj : objList) {
            if (obj.getEmpId() == mgrId) {
                objList.remove(obj);
                objList.add(0, obj);
                break;
            }
        }

        for (Attendance obj : objList) {

            srNo += 1;

            TableRow tr;
            TextView tv;
            EditText editText;
            Spinner spinner;

            int headingColor = Color.BLACK;

            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(srNo));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SR_NO));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTextColor(headingColor);

            tr.addView(tv);

            spinner = new Spinner(this);
            spinner.setPadding(0, 0, 0, 0);
            spinner.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
            spinner.setBackgroundResource(R.drawable.table_cell_bg);
            spinner.setGravity(Gravity.LEFT);
            spinner.setAdapter(adpEmployee);
            tr.addView(spinner);

            index = getEmployeeIndex(spinner, obj.getEmpId());
            spinner.setSelection(index);

            disableSpinner(spinner);

            spinner = new Spinner(this);
            spinner.setPadding(0, 0, 0, 0);
            spinner.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_PRSENT));
            spinner.setBackgroundResource(R.drawable.table_cell_spinner);
            spinner.setGravity(Gravity.LEFT);
            tr.addView(spinner);

            SpinnerHelper.FillWorkType(spinner);
            index = getWorkTypeIndex(spinner,obj.getWorkType());
            spinner.setSelection(index);

            editText = new EditText(this);
            editText.setText(obj.getRemark());
            editText.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            editText.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_OTHER_REMARK));
            editText.setTextSize(FONT_SIZE);
            editText.setBackgroundResource(R.drawable.table_cell_plain);
            editText.setGravity(Gravity.LEFT);
            editText.setTextColor(headingColor);
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            tr.addView(editText);

            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblData.addView(tr);
        }

        isLoading = false;
    }

    private void disableSpinner(final Spinner spinner) {

        spinner.post(new Runnable() {
            @Override
            public void run() {

                View view = ((Spinner) spinner).getSelectedView();
                if (view == null){
                    return;
                }

                view.setEnabled(false);
                spinner.setEnabled(false);
                ((TextView) view).setTextColor(Color.BLACK);
            }
        });
    }

}
