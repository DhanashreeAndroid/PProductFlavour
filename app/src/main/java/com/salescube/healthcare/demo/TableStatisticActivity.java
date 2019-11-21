package com.salescube.healthcare.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.salescube.healthcare.demo.data.repo.TableInfoRepo;
import com.salescube.healthcare.demo.view.TableStatistic;

import java.util.List;

public class TableStatisticActivity extends BaseAppCompatActivity {

    private TableLayout tableView;
    private TableLayout tblHeader;
    private TableLayout tblData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_statistic);
        title("Table Info");

        InitControls();
        loadTables();
    }

    private void loadTables() {

        TableInfoRepo objTSRepo = new TableInfoRepo();
        List<TableStatistic> tables = objTSRepo.getTables();

        addHeader();
        if (tables != null) {
            for (TableStatistic table : tables) {
                loadData(table.getTableName(), table.getRecords());
            }
        }
    }

    private void InitControls() {
        tblHeader = (TableLayout) findViewById(R.id.table_statistic_header);
        tblData = (TableLayout) findViewById(R.id.table_statistic_data);
    }

    private void addHeader() {

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Table Name");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 8f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText("Qty");
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setGravity(Gravity.RIGHT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_header);
        tblHeader.addView(tr);

    }


    private void loadData(String tableName, long rows) {

        TableRow tr;
        TextView tv;

        tr = new TableRow(this);
        tr.setPadding(5, 5, 5, 5);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(tableName);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 8f));
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.table_cell_bg);
        tv.setGravity(Gravity.LEFT);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setPadding(10, 0, 10, 0);
        tv.setText(String.valueOf(Long.toString(rows)));
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 2f));
        tv.setTextSize(14);
        tv.setGravity(Gravity.RIGHT);
        tr.addView(tv);

        tr.setBackgroundResource(R.drawable.table_row_bg);
        tblData.addView(tr);

    }

}
