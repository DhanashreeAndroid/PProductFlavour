package com.salescube.healthcare.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.salescube.healthcare.demo.data.repo.ProductTargetRepo;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vProductTarget;

import java.util.List;

public class ProductTargetActivity extends Activity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);


    }

    private RecyclerView mRecyclerView;
    private TextView txtHeaderLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int height =  getWindow().getAttributes().height;

        setContentView(R.layout.activity_product_target);

        this.setFinishOnTouchOutside(false);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            initExtra();
            initControl();
            initListener();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String mOrderDate;
    String mShopAppId;
    int mAgentId;
    int mEmployeeId;
    String mShopName;

    private void initExtra() {

        Intent intent = getIntent();

        mOrderDate = intent.getStringExtra(Constant.ORDER_DATE);
        // mOrderDate = DateFunc.getDate(strData, "dd/MM/yyyy");

        mShopAppId = intent.getStringExtra(Constant.SHOP_APP_ID);
        mAgentId = intent.getIntExtra(Constant.AGENT_ID, 0);
        mShopName = intent.getStringExtra(Constant.SHOP_NAME);

        mEmployeeId = AppControl.getmEmployeeId();
    }

    private void initControl() {

        mRecyclerView = (RecyclerView) findViewById(R.id.product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtHeaderLabel = (TextView) findViewById(R.id.header_label);
        txtHeaderLabel.setText(mShopName + "\n" + "SHOP TARGET");

    }

    // https://stackoverflow.com/questions/29106484/how-to-add-a-button-at-the-end-of-recyclerview

    private void initListener() {

    }

    private void initData() {

        List<vProductTarget> report = new ProductTargetRepo().getAll(mEmployeeId, mShopAppId);

        double totalValue = 0;
        for (vProductTarget v : report) {
            totalValue += v.getTargetValue();
        }

        vProductTarget total = new vProductTarget();
        total.setTargetValue(totalValue);
        total.setTotal(true);
        report.add(report.size(), total);

        vProductTarget[] list = report.toArray(new vProductTarget[report.size()]);

        ReportAdapter adapter = new ReportAdapter(this, list);

        // mRecyclerView.addItemDecoration(new DividerLine(context, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

    }

    private class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        private Context mCtx;
        private vProductTarget[] forms;

        private int ITEM_TYPE_ROW = 0;
        private int ITEM_TYPE_BUTTON = 1;

        public ReportAdapter(Context mCtx, vProductTarget[] productList) {
            this.mCtx = mCtx;
            this.forms = productList;

        }

        @Override
        public int getItemViewType(int position) {
            return (position == forms.length) ? ITEM_TYPE_BUTTON : ITEM_TYPE_ROW;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view;

            if (viewType == ITEM_TYPE_ROW) {
                view = inflater.inflate(R.layout.item_product_target, parent, false);
            } else {
                view = inflater.inflate(R.layout.item_button, parent, false);
            }

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (getItemViewType(position) == ITEM_TYPE_ROW) {
               vProductTarget data = forms[position];

                holder.txtProductName.setTextColor(Color.BLUE);
                holder.txtProductName.setText(data.getProductName());
                holder.txtTargetQty.setText(String.valueOf(data.getTargetQty()));
                holder.txtTargetAmount.setText(String.valueOf(data.getTargetValue()));

//                holder.txtProductName.setTypeface(holder.txtProductName.getTypeface(), Typeface.NORMAL);
//                holder.txtTargetAmount.setTypeface(holder.txtTargetAmount.getTypeface(), Typeface.NORMAL);

                if (data.isTotal()) {
                    holder.txtProductName.setText("TOTAL");
                    holder.txtTargetQty.setText("");
                    holder.txtProductName.setTextColor(Color.BLACK);
                    holder.txtProductName.setTypeface(holder.txtTargetAmount.getTypeface(), Typeface.BOLD);
                    holder.txtTargetAmount.setTypeface(holder.txtTargetAmount.getTypeface(), Typeface.BOLD);
                }
            }else {
                holder.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return forms.length +1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtProductName, txtTargetQty, txtTargetAmount;
            Button btnOk;

            public ViewHolder(View itemView) {
                super(itemView);

                txtProductName = (TextView) itemView.findViewById(R.id.product_name);
                txtTargetQty = (TextView) itemView.findViewById(R.id.target_qty);
                txtTargetAmount = (TextView) itemView.findViewById(R.id.target_amount);
                btnOk = (Button)itemView.findViewById(R.id.btn_ok);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String msg = "Please press OK button to close this window. If OK button not showing please scroll down." ;
            Toast.makeText(ProductTargetActivity.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}
