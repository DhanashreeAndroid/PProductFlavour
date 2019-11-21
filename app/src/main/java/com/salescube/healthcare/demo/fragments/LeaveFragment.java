package com.salescube.healthcare.demo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.adapter.ISpinnerItemClick;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaRepo;
import com.salescube.healthcare.demo.data.repo.RouteRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanDetailRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanRepo;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.sysctrl.AppControl;
import com.salescube.healthcare.demo.sysctrl.Constant;
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vTourPlan;
import com.salescube.healthcare.demo.view.vTourPlanDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveFragment extends Fragment implements ISpinnerItemClick, View.OnClickListener {

    TextView mTvLeaveType, mTvErrorMessage;
    vTourPlan mTourPlan;
    String mErrorMessage;

    public LeaveFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Bundle arguments) {
        LeaveFragment fragment = new LeaveFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);
        initialization(view);
        setData();
        setListeners();
        return view;
    }

    private void initialization(View view) {
        Bundle arguments = getArguments();
        mTourPlan = (vTourPlan) arguments.getSerializable("tourPlan");
        mErrorMessage = arguments.getString("error");
        mTvLeaveType = view.findViewById(R.id.tvLeaveType);
        mTvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        //if tourDate is less than current date , its get disabled
        if (UtilityFunc.isAllowedTourPlan(getActivity(), mTourPlan.getTourDate())) {
            UtilityFunc.setOnOffState(mTvLeaveType, true);
        }else{
            UtilityFunc.setOnOffState(mTvLeaveType, false);
        }

        if(!TextUtils.isEmpty(mErrorMessage)){
            mTvErrorMessage.setVisibility(View.VISIBLE);
            mTvErrorMessage.setText(mErrorMessage);
        }else{
            mTvErrorMessage.setVisibility(View.GONE);
        }

        //once added tourplan restrict to edit either it from app or portal
        boolean isExistsTourDetail = new TourPlanRepo().isExistTouPlanDetail(mTourPlan.getId());
        if(isExistsTourDetail){
            UtilityFunc.setOnOffState(mTvLeaveType, false);
        }
    }

    private void setData() {
        TourPlanDetailRepo detail = new TourPlanDetailRepo();
        ArrayList<vTourPlanDetail> list = (ArrayList<vTourPlanDetail>) detail.getTourPlanDetailAll(mTourPlan.getId());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().equalsIgnoreCase("LeaveType")) {
                    String name =  list.get(i).getValue();
                    mTvLeaveType.setText(name);
                }
            }
        }
    }


    private void setListeners() {
        mTvLeaveType.setOnClickListener(this);
    }

    @Override
    public void onSpinnerItemClick(Object obj, String label) {
        if (label.equalsIgnoreCase("LeaveType")) {
            if (mTvLeaveType.getTag() != null) {
                if (!TextUtils.isEmpty(mTvLeaveType.getTag().toString())) {
                    TourPlanDetail detail = new TourPlanDetail();
                    detail.setTourPlanId(mTourPlan.getId());
                    detail.setTitle("LeaveType");
                    detail.setValue(mTvLeaveType.getText().toString());
                    new TourPlanDetailRepo().insertUpdate(detail);

                    TourPlan plan = new TourPlan();
                    plan.setId(mTourPlan.getId());
                    plan.setSetName(mTourPlan.getSetName());
                    plan.setSoId(mTourPlan.getSoId());
                    plan.setTourDate(mTourPlan.getTourDate());
                    plan.setIsSync(0);
                    plan.setUpdatedOn(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
                    plan.setUpdatedBy(mTourPlan.getSoId());
                    new TourPlanRepo().update(plan);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTvLeaveType) {
            mTvLeaveType.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            UtilityFunc.showSpinnerDialog(getActivity(), mTvLeaveType,
                    getSpinnerList(), "LeaveType", this);
        }
    }

    private ArrayList<vBaseSpinner> getSpinnerList() {

        ArrayList<vBaseSpinner> list = new ArrayList<>();
        vBaseSpinner base1 = new vBaseSpinner();
        base1.setId(1);
        base1.setName(Constant.LeaveType.CL);
        list.add(base1);

        vBaseSpinner base2 = new vBaseSpinner();
        base2.setId(2);
        base2.setName(Constant.LeaveType.PL);
        list.add(base2);

        vBaseSpinner base3 = new vBaseSpinner();
        base3.setId(3);
        base3.setName(Constant.LeaveType.SL);
        list.add(base3);

        vBaseSpinner base4 = new vBaseSpinner();
        base4.setId(1);
        base4.setName(Constant.LeaveType.CO);
        list.add(base4);

        vBaseSpinner base5 = new vBaseSpinner();
        base5.setId(1);
        base5.setName(Constant.LeaveType.WP);
        list.add(base5);

        return list;
    }


}
