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
import com.salescube.healthcare.demo.view.vBaseSpinner;
import com.salescube.healthcare.demo.view.vTourPlan;
import com.salescube.healthcare.demo.view.vTourPlanDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkingFragment extends Fragment implements View.OnClickListener, ISpinnerItemClick {

    TextView mTvDistributor, mTvCity, mTvRoute, mTvErrorMessage;
    EditText mEdtInput;
    vTourPlan mTourPlan;
    String mErrorMessage;

    public WorkingFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Bundle arguments) {
        WorkingFragment fragment = new WorkingFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working, container, false);
        initialization(view);
        setListeners();
        setData();
        return view;
    }

    private void initialization(View view) {
        Bundle arguments = getArguments();
        mTourPlan = (vTourPlan) arguments.getSerializable("tourPlan");
        mErrorMessage = arguments.getString("error");
        mTvDistributor = view.findViewById(R.id.tvDistributor);
        mTvCity = view.findViewById(R.id.tvCity);
        mTvRoute = view.findViewById(R.id.tvRoute);
        mTvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        mEdtInput = view.findViewById(R.id.edtInput);
        UtilityFunc.setOnFocusChangeListener(getActivity(), mEdtInput);

        if(!TextUtils.isEmpty(mErrorMessage)){
            mTvErrorMessage.setVisibility(View.VISIBLE);
            mTvErrorMessage.setText(mErrorMessage);
        }else{
            mTvErrorMessage.setVisibility(View.GONE);
        }

        final String oldValue = new TourPlanDetailRepo().getInputValue(mTourPlan.getId(), "Target Value");

        mEdtInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    if (!TextUtils.isEmpty(oldValue)) {
                        if (!oldValue.equalsIgnoreCase(s.toString())) {
                            addTourPlanDetailsInputs(mEdtInput, mTourPlan.getId(), "Target Value");
                        }
                    } else {
                        addTourPlanDetailsInputs(mEdtInput, mTourPlan.getId(), "Target Value");
                    }
            }
        });


        //if tourDate is less than current date , its get disabled
        if (UtilityFunc.isAllowedTourPlan(getActivity(), mTourPlan.getTourDate())) {
            UtilityFunc.setOnOffState(mTvDistributor, true);
            UtilityFunc.setOnOffState(mTvCity, true);
            UtilityFunc.setOnOffState(mTvRoute, true);
            //temporary this value not give to change from app
            //UtilityFunc.setOnOffState(mEdtInput, true);
        } else {
            UtilityFunc.setOnOffState(mTvDistributor, false);
            UtilityFunc.setOnOffState(mTvCity, false);
            UtilityFunc.setOnOffState(mTvRoute, false);
            UtilityFunc.setOnOffState(mEdtInput, false);
        }

        //once added tourplan restrict to edit either it from app or portal
        boolean isExistsTourDetail = new TourPlanRepo().isExistTouPlanDetail(mTourPlan.getId());
        if (isExistsTourDetail) {
            UtilityFunc.setOnOffState(mTvDistributor, false);
            UtilityFunc.setOnOffState(mTvCity, false);
            UtilityFunc.setOnOffState(mTvRoute, false);
            UtilityFunc.setOnOffState(mEdtInput, false);
        }
    }


    private void setData() {
        TourPlanDetailRepo detail = new TourPlanDetailRepo();
        ArrayList<vTourPlanDetail> list = (ArrayList<vTourPlanDetail>) detail.getTourPlanDetailAll(mTourPlan.getId());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().equalsIgnoreCase("Distributor")) {
                    String name = getValueById("Distributor", list.get(i).getValue());
                    mTvDistributor.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("City")) {
                    String name = getValueById("City", list.get(i).getValue());
                    mTvCity.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("Route")) {
                    String name = getValueById("Route", list.get(i).getValue());
                    mTvRoute.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("Target Value")) {
                    mEdtInput.setText(list.get(i).getValue());
                }
            }
        }
    }

    private void setListeners() {
        mTvDistributor.setOnClickListener(this);
        mTvCity.setOnClickListener(this);
        mTvRoute.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mTvDistributor) {
            mTvDistributor.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            UtilityFunc.showSpinnerDialog(getActivity(), mTvDistributor,
                    getSpinnerList("Distributor", -1), "Distributor", this);
        } else if (v == mTvCity) {
            mTvCity.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            if (mTvDistributor.getTag() != null) {
                UtilityFunc.showSpinnerDialog(getActivity(), mTvCity,
                        getSpinnerList("City", Integer.parseInt(mTvDistributor.getTag().toString())), "City", this);
            } else {
                UtilityFunc.showSpinnerDialog(getActivity(), mTvCity,
                        getSpinnerList("City", -1), "City", this);
            }
        } else if (v == mTvRoute) {
            mTvRoute.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            if (mTvCity.getTag() != null) {
                UtilityFunc.showSpinnerDialog(getActivity(), mTvRoute,
                        getSpinnerList("Route", Integer.parseInt(mTvCity.getTag().toString())), "Route", this);
            } else {
                UtilityFunc.showSpinnerDialog(getActivity(), mTvRoute,
                        getSpinnerList("Route", -1), "Route", this);
            }
        }

    }

    private void addTourPlanDetails(View view, String id, String title) {
        TextView tv = (TextView) view;
        if (tv.getTag() != null) {
            if (!TextUtils.isEmpty(tv.getTag().toString())) {
                TourPlanDetail detail = new TourPlanDetail();
                detail.setTourPlanId(id);
                detail.setTitle(title);
                detail.setValue(tv.getTag().toString());
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

    private void addTourPlanDetailsInputs(View view, String id, String title) {
        EditText edt = (EditText) view;
        TourPlanDetail detail = new TourPlanDetail();
        detail.setTourPlanId(id);
        detail.setTitle(title);
        detail.setValue(edt.getText().toString());
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

    private ArrayList<vBaseSpinner> getSpinnerList(String label, int dependentId) {
        ArrayList<vBaseSpinner> list = new ArrayList<>();
        int soId = AppControl.getmEmployeeId();
        if (label.equalsIgnoreCase("distributor")) {
            list = (ArrayList<vBaseSpinner>) new AgentRepo().getAgentAll1(soId);
        } else if (label.equalsIgnoreCase("city")) {
            if (dependentId != -1) {
                list = (ArrayList<vBaseSpinner>) new AreaRepo().getAreaByAgent(soId, dependentId);
            } else {
                list = (ArrayList<vBaseSpinner>) new AreaRepo().getAreaAll1(soId);
            }
        } else if (label.equalsIgnoreCase("route")) {
            if (dependentId != -1) {
                list = (ArrayList<vBaseSpinner>) new RouteRepo().getAllRoutesByArea(soId, dependentId);
            } else {
                list = (ArrayList<vBaseSpinner>) new RouteRepo().getAllRoutes1(soId);
            }
        }

        return list;
    }

    private String getValueById(String title, String id) {
        String value = "";
        if (!TextUtils.isEmpty(id)) {
            int selectedId = Integer.parseInt(id);
            if (title.equalsIgnoreCase("distributor")) {
                value = new AgentRepo().getSelected(selectedId);
            } else if (title.equalsIgnoreCase("city")) {
                value = new AreaRepo().getSelected(selectedId);
            } else if (title.equalsIgnoreCase("route")) {
                value = new RouteRepo().getSelected(selectedId);
            }
        }
        return value;
    }

    @Override
    public void onSpinnerItemClick(Object obj, String label) {
        if (label.equalsIgnoreCase("Distributor")) {
            mTvCity.setText("Select City");
            mTvCity.setTag(null);
            addTourPlanDetails(mTvDistributor, mTourPlan.getId(), "Distributor");
        } else if (label.equalsIgnoreCase("City")) {
            mTvRoute.setText("Select Route");
            mTvRoute.setTag(null);
            addTourPlanDetails(mTvCity, mTourPlan.getId(), "City");
        } else if (label.equalsIgnoreCase("Route")) {
            addTourPlanDetails(mTvRoute, mTourPlan.getId(), "Route");
        }
    }
}
