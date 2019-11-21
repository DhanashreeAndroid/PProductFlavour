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


public class AttendanceActivity2 extends BaseActivity implements Spinner.OnItemSelectedListener {


    private TableLayout mTblHead;
    private TableLayout mTblData;
    private Spinner mSpnOrderDates;
    private Spinner mSpnASM;
    private Button btnSubmit;

    private final static float WGT_SR_NO = 0.3f;
    private final static float WGT_SO_NAME = 2.5f;
    private final static float WGT_ACTION = 1.5f;
    private final static float WGT_TIME = 1.0f;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    private ArrayAdapter<vEmployee> adpEmployee;
    private List<Attendance> lstAttend, lstAttendence;
    private int rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_attendance2);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title_bar);

        TextView titleBar = getWindow().findViewById(R.id.title_head_1);
        if (titleBar != null){
            titleBar.setText("SO Attendance Entry");
        }

        try {
            initControls();
            initData();
            initListener();

//            loadAttendence(lstAttendence);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }



    private void initControls() {

        mTblHead = (TableLayout) findViewById(R.id.att2_tbl_header);
        mTblData = (TableLayout) findViewById(R.id.att2_tbl_data);
        mSpnOrderDates = (Spinner) findViewById(R.id.att2_spn_dates);

        btnSubmit = (Button) findViewById(R.id.att2_btn_submit);
    }

    private void initData() {
//        loadAttendence();
        SpinnerHelper.FillSysDates(mSpnOrderDates, AppControl.getmEmployeeId());
//        SpinnerHelper.FillASM(mSpnASM, AppControl.getmEmployeeId(),"");

        adpEmployee = AdapterData.getEmployeeList(AttendanceActivity2.this, AppControl.getmEmployeeId(),"");

        loadGridHeader();
    }

    /*public void loadAttendence() {
        lstAttendence=new ArrayList<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Attendance>> query = apiService.getAttendence(115);
        final XProgressBar dlg = XProgressBar.show(this, "", "");

        query.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if(response.isSuccessful()){
                    try {
                        List<Attendance> objUser = response.body();
                        lstAttendence.addAll(objUser);
                        dlg.dismiss();
                        showAttendence(lstAttendence);
                    } catch (Exception e) {
                        dlg.dismiss();
                        Alert("Error", "Error while loading data! Please try again later.");
                        return;
                    }
                }else {
                    String message;
                    try {
                        message = response.errorBody().string();
                    } catch (Exception e) {
                        message = e.getMessage();
                        errMsg("While downloading", e);
//                        togger.e(e);
                    }

                    if (message.equals("")) {
                        message = response.raw().message();
                    }
                    Alert("Error!", message);

                }
                dlg.dismiss();
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                String message;

                if(t instanceof IOException){
                    message = t.toString();
                }
                if (t instanceof SocketTimeoutException) {
                    message = getString(R.string.connection_timeout);
                } else if (t instanceof ConnectException) {
                    message = getString(R.string.no_connection);
                } else {
                    message = getString(R.string.unknown_error);
                }
                dlg.dismiss();
                Alert("Error!", message);
            }
        });
    }*/

    private void showAttendence(List<Attendance> lstAttendence) {

        int index;
        int srNo = 0;
        mTblData.removeAllViews();

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;

        for(Attendance attendance : lstAttendence) {

            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);
            srNo+=1;

            // Sr.No.
            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(String.valueOf(srNo));
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SR_NO));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            // So Name
            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(attendance.getSoName());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SO_NAME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(headingColor);
            tr.addView(tv);

            // Action
            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(attendance.getAction());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACTION));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(headingColor);
            tr.addView(tv);


            // Time
            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(attendance.getTime());
            tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TIME));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_plain);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(headingColor);
            tr.addView(tv);


            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblData.addView(tr);
        }
    }

    private void initListener() {

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (!validateForm() ){
//                    return;
//                }

                try {
//                    loadAttendence();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });

        mSpnOrderDates.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSysDate date = (vSysDate) mSpnOrderDates.getSelectedItem();
//                vEmployee employee = (vEmployee)mSpnASM.getSelectedItem();

//                if ((date != null) && (employee != null)) {
//                    loadGridData(date.getTrDate(), employee.getEmpId());
//                }
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
        tv.setText("Sr.\nNo.");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_SR_NO));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("So Name");
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
        tv.setText("Action");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACTION));
        tv.setTextSize(FONT_SIZE);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tf);
        tv.setTextColor(headingColor);
        tv.setAllCaps(true);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
        tv.setText("Time");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TIME));
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

//        List<Attendance> objList = new AttendanceRepo().getAttendance(AppControl.getmEmployeeId(), txnDate, mgrId);
        List<Attendance> objList = new AttendanceRepo().insertDummyList();

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
            spinner.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_ACTION));
            spinner.setBackgroundResource(R.drawable.table_cell_spinner);
            spinner.setGravity(Gravity.LEFT);
            tr.addView(spinner);

            SpinnerHelper.FillWorkType(spinner);
            index = getWorkTypeIndex(spinner,obj.getWorkType());
            spinner.setSelection(index);

            editText = new EditText(this);
            editText.setText(obj.getRemark());
            editText.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            editText.setLayoutParams(new TableRow.LayoutParams(0, -1, WGT_TIME));
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
