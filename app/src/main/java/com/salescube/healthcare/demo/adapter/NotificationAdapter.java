package com.salescube.healthcare.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.data.model.NotificationModel;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.widget.SweetAlertDialog;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;

    String Date;
    private List<NotificationModel> notificationlist;


    public NotificationAdapter(Context mcontext,  List<NotificationModel> notificationList) {
        this.context = mcontext;
        this.notificationlist = notificationList;
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row_item, parent, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        final NotificationModel products = notificationlist.get(position);


        holder.title.setText(products.getTitle());
        holder.description.setText(products.getDescription());
        if(!TextUtils.isEmpty(products.getDate())) {
            String date = DateFunc.getDateStr(DateFunc.getDate(products.getDate()), "dd-MMM-yyyy");
            holder.tvDate.setText(date);
        }

        holder.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(products.getTitle())
                        .setContentText(products.getDescription())
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();


                            }
                        })
                        .show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return notificationlist.size();
    }

  /*  public void setClient(List<ProductDetails> productname) {
        this.productlist = productname;
        notifyDataSetChanged();
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView title, tvDate;
        private TextView description;
        private CardView notificationCard;



        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            tvDate = view.findViewById(R.id.tvDate);
            description = view.findViewById(R.id.desc);
            notificationCard = view.findViewById(R.id.notificationCard);



        }


    }

}

