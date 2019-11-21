package com.salescube.healthcare.demo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.view.TableStatistic;

import java.util.List;

public class TableViewActivity extends BaseActivity {

    private Button btnDeleteAll;

    private Spinner mSpnTableName;
    private Button mBtnShow;

    private TableLayout mTblHead;
    private TableLayout mTblData;

    private final static float WGT_SR_NO = 0.3f;
    private final static float WGT_SO_NAME = 2;
    private final static float WGT_PRSENT = 1;
    private final static float WGT_OTHER_REMARK = 2;
    private final static int FONT_SIZE = 16;
    private final static int LEFT_PADDING = 10;
    private final static int RIGHT_PADDING = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        try {
            initControls();
            initListeners();
            initData();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    private void initControls() {

        mSpnTableName = (Spinner) findViewById(R.id.table_name_spn);
        mBtnShow = (Button) findViewById(R.id.table_show_btn);

        mTblHead = (TableLayout) findViewById(R.id.table_tbl_header);
        mTblData = (TableLayout) findViewById(R.id.table_tbl_data);
        btnDeleteAll = (Button) findViewById(R.id.table_view_btn_delete_all);
    }

    private void initListeners() {

        mBtnShow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpnTableName.getCount() == 0) {
                    return;
                }

                String tableName = mSpnTableName.getSelectedItem().toString();
                showTableData(tableName);
            }
        });

        btnDeleteAll.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSpnTableName.getCount() == 0) {
                    return;
                }

                final String tableName = mSpnTableName.getSelectedItem().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(TableViewActivity.this);
                builder.setTitle("Confirm Delete!");
                // builder.setMessage("Please select download type Complete or Update.");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                        db.execSQL("DELETE FROM " + tableName + "");
                        DatabaseManager.getInstance().closeDatabase();

                        msgShort("[" + tableName + "] Data deleted successfully.");
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    private void initData() {

        List<TableStatistic> objList = new TableInfoRepo().getTables();

        ArrayAdapter<TableStatistic> adpSysDate = new ArrayAdapter<>(mSpnTableName.getContext(), android.R.layout.simple_spinner_item, objList);
        adpSysDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnTableName.setAdapter(adpSysDate);

    }

    private void showTableData(String tableName) {

        String sql;
        Cursor c;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + tableName + "";

//        sql = "";
//        sql += " SELECT * ";
//        sql += " FROM " + Shop.TABLE + "";
//        sql += " WHERE app_shop_id like 'N%'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return;
        }

        String columns[] = c.getColumnNames();

        mTblData.removeAllViews();

        TableRow tr;
        TextView tv;
        int headingColor = Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-condensed", Typeface.BOLD);

        tr = new TableRow(this);
        tr.setPadding(5, 10, 5, 10);

        for (String column : columns) {

            tv = new TextView(this);
            tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
            tv.setText(column);
            //tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 5));
            tv.setTextSize(FONT_SIZE);
            tv.setBackgroundResource(R.drawable.table_cell_bg);
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(tf);
            tv.setTextColor(headingColor);

            tr.addView(tv);


        }

        tr.setBackgroundResource(R.drawable.table_row_header);
        mTblData.addView(tr);

        mTblData.setStretchAllColumns(true);

        while (c.moveToNext()) {

            tr = new TableRow(this);
            tr.setPadding(5, 10, 5, 10);

            for (String column : columns) {
                tv = new TextView(this);
                tv.setPadding(LEFT_PADDING, 0, RIGHT_PADDING, 0);
                tv.setText(c.getString(c.getColumnIndex(column)));
                tv.setTextSize(FONT_SIZE);
                tv.setBackgroundResource(R.drawable.table_cell_bg);
                tv.setGravity(Gravity.RIGHT);
                tv.setTextColor(headingColor);

                tr.addView(tv);
            }

            tr.setBackgroundResource(R.drawable.table_row_header);
            mTblData.addView(tr);

        }
        closeCursor(c);
    }

}
