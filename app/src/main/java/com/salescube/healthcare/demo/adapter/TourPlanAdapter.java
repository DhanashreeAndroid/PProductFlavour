package com.salescube.healthcare.demo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.repo.TourPlanDetailRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanRepo;
import com.salescube.healthcare.demo.fragments.HolidayFragment;
import com.salescube.healthcare.demo.fragments.LeaveFragment;
import com.salescube.healthcare.demo.fragments.OtherWorkFragment;
import com.salescube.healthcare.demo.fragments.WorkingFragment;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vTourPlan;

import java.util.ArrayList;
import java.util.Random;

public class TourPlanAdapter extends RecyclerView.Adapter<TourPlanAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<vTourPlan> mList;
    private ITourPlanClickListeners mListener;
    vBaseSpinner view;
    ViewHolder holder;
    String errorMessage;

    public TourPlanAdapter(Context context, ITourPlanClickListeners listner) {
        this.mContext = context;
        this.mListener = listner;
    }

    public void setData(ArrayList<vTourPlan> list, String errorMessage) {
        this.mList = list;
        this.errorMessage = errorMessage;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tour_plan_list_item, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position == mList.size() - 1) {
            if (!mList.get(position).getSetName().equalsIgnoreCase(Constant.TourPlanSets.LEAVE)) {
                holder.btnAddMore.setVisibility(View.VISIBLE);
            }
        } else {
            holder.btnAddMore.setVisibility(View.GONE);
        }

        //if tourDate is out of date range , its get disabled
        if (UtilityFunc.isAllowedTourPlan(mContext, mList.get(position).getTourDate())) {
            //holder.mDataContainer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.mDataContainer.setAlpha(1f);
            UtilityFunc.setOnOffState(holder.btnAddMore, true);
            UtilityFunc.setOnOffState(holder.tvStatus, true);
            UtilityFunc.setOnOffState(holder.imgClose, true);
        } else {
            // holder.mDataContainer.setBackgroundColor(mContext.getResources().getColor(R.color.disabled_state));
            holder.mDataContainer.setAlpha(0.8f);
            UtilityFunc.setOnOffState(holder.btnAddMore, false);
            UtilityFunc.setOnOffState(holder.tvStatus, false);
            UtilityFunc.setOnOffState(holder.imgClose, false);
            holder.btnAddMore.setVisibility(View.GONE);
            holder.imgClose.setVisibility(View.GONE);
        }

        //once added tourplan restrict to edit either it from app or portal
        boolean isExistsTourDetail = new TourPlanRepo().isExistTouPlanDetail(mList.get(position).getId());
        if (isExistsTourDetail) {
            UtilityFunc.setOnOffState(holder.tvStatus, false);
        }

        holder.container.removeAllViews();

        Random randomGenerator = new Random();
        final int layoutId = randomGenerator.nextInt(999999) + 100;
        holder.container.setId(layoutId);
        refreshContainers(position, mList.get(position).getSetName(), layoutId);

        holder.tvStatus.setText(mList.get(position).getSetName());

        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityFunc.showSpinnerDialog(mContext, holder.tvStatus, getSpinnerList(), "", new ISpinnerItemClick() {
                    @Override
                    public void onSpinnerItemClick(Object obj, String label) {
                        holder.container.removeAllViews();
                        if (!mList.get(position).getSetName().equalsIgnoreCase(((vBaseSpinner) obj).getName())) {
                            //while changing status delete old plan in details
                            new TourPlanDetailRepo().deletePlan(mList.get(position).getId());
                        }
                        mList.get(position).setSetName(((vBaseSpinner) obj).getName());
                        refreshContainers(position, ((vBaseSpinner) obj).getName(), layoutId);

                       /* if (!mList.get(position).getSetName().equalsIgnoreCase(Constant.TourPlanSets.LEAVE)) {
                            holder.btnAddMore.setVisibility(View.VISIBLE);
                        } else {
                            holder.btnAddMore.setVisibility(View.GONE);
                        }*/

                        //update changed status to table
                        TourPlan plan = new TourPlan();
                        plan.setId(mList.get(position).getId());
                        plan.setSetName(mList.get(position).getSetName());
                        plan.setSoId(mList.get(position).getSoId());
                        plan.setTourDate(mList.get(position).getTourDate());
                        new TourPlanRepo().insertUpdate(plan);
                    }
                });
            }
        });

        //temparory set to off
        holder.btnAddMore.setVisibility(View.GONE);
        holder.imgClose.setVisibility(View.GONE);

        holder.btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddMoreClick(mList.get(position));
            }
        });

        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCloseClick(mList.get(position), position);
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

    private void refreshContainers(int position, String set, int layoutId) {
        Fragment fragment;
        if (set.equalsIgnoreCase(Constant.TourPlanSets.WORKING)) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("tourPlan", mList.get(position));
            arguments.putString("error", errorMessage);
            fragment = WorkingFragment.newInstance(arguments);
        } else if (set.equalsIgnoreCase(Constant.TourPlanSets.LEAVE)) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("tourPlan", mList.get(position));
            arguments.putString("error", errorMessage);
            fragment = LeaveFragment.newInstance(arguments);
        }  else if (set.equalsIgnoreCase(Constant.TourPlanSets.OTHER)) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("tourPlan", mList.get(position));
            arguments.putString("error", errorMessage);
            fragment = OtherWorkFragment.newInstance(arguments);
        }else{
            Bundle arguments = new Bundle();
            arguments.putSerializable("tourPlan", mList.get(position));
            arguments.putString("error", errorMessage);
            fragment = HolidayFragment.newInstance(arguments);
        }
        FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
        ft.add(layoutId, fragment);
        ft.commit();
    }

    private ArrayList<vBaseSpinner> getSpinnerList() {
        ArrayList<vBaseSpinner> list = new ArrayList<>();
        vBaseSpinner vo = new vBaseSpinner();
        vo.setId(1);
        vo.setName("Working");
        list.add(vo);
        vBaseSpinner vo1 = new vBaseSpinner();
        vo1.setId(2);
        vo1.setName("Leave");
        list.add(vo1);
        vBaseSpinner vo2 = new vBaseSpinner();
        vo2.setId(3);
        vo2.setName("Other");
        list.add(vo2);
        vBaseSpinner vo3 = new vBaseSpinner();
        vo3.setId(4);
        vo3.setName("Holiday/Weekly Off");
        list.add(vo3);
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout container;
        Button btnAddMore;
        ImageView imgClose;
        View parentView;
        TextView tvStatus;
        RelativeLayout mDataContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            container = itemView.findViewById(R.id.fragment_container);
            btnAddMore = itemView.findViewById(R.id.btnAddMore);
            imgClose = itemView.findViewById(R.id.close);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            mDataContainer = itemView.findViewById(R.id.tour_main_container);
        }

    }


}
