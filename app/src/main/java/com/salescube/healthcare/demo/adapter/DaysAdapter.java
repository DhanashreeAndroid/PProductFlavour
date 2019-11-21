package com.salescube.healthcare.demo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.fragments.WorkingFragment;
import com.salescube.healthcare.demo.view.vTourDates;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<vTourDates> mList;
    private ITourPlanClickListeners mListener;
    SimpleDateFormat dfMain = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
    SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM");
    int mSelectedPosition = 0;

    public DaysAdapter(Context context, ITourPlanClickListeners listner) {
        this.mContext = context;
        this.mListener = listner;
    }

    public void setData(ArrayList<vTourDates> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.days_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            String strdate = mList.get(position).getDate();
            Date date = dfMain.parse(strdate);
            holder.tvTitle.setText(dfDate.format(date));
            holder.tvDay.setText(dfDay.format(date));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        holder.tvDay.setTypeface( holder.tvDay.getTypeface(), Typeface.NORMAL);
        holder.tvTitle.setTypeface( holder.tvTitle.getTypeface(), Typeface.NORMAL);
        holder.tvDay.setTextColor( mContext.getResources().getColor(R.color.colorGray));
        holder.tvTitle.setTextColor( mContext.getResources().getColor(R.color.colorGray));
        holder.mMainContainer.setBackgroundColor(mContext.getResources().getColor(R.color.tour_plan_back));

        if(mList.get(position).isError()){

            holder.tvDay.setTextColor( mContext.getResources().getColor(R.color.red));
            holder.tvTitle.setTextColor( mContext.getResources().getColor(R.color.red));
        }

        if (mSelectedPosition == position) {
            holder.tvDay.setTypeface( holder.tvDay.getTypeface(), Typeface.BOLD);
            holder.tvTitle.setTypeface( holder.tvTitle.getTypeface(), Typeface.BOLD);
            holder.mMainContainer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        holder.mMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDayClick(mList.get(position));
                mSelectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public void setSelectedPosition(int position){
        this.mSelectedPosition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDay;
        RelativeLayout mMainContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTitle = itemView.findViewById(R.id.tvDate);
            mMainContainer = itemView.findViewById(R.id.day_container);
        }
    }
}
