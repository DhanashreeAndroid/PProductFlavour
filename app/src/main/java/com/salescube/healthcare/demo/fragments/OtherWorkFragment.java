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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.adapter.ISpinnerItemClick;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkReasonRepo;
import com.salescube.healthcare.demo.data.repo.SSRepo;
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
public class OtherWorkFragment extends Fragment implements View.OnClickListener, ISpinnerItemClick {

    TextView mTvType, mTvAgent, mTvSS, mTvErrorMessage;
    EditText mEdtInput;
    vTourPlan mTourPlan;
    LinearLayout mLnrAgent, mLnrSS;
    String mErrorMessage;

    public OtherWorkFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Bundle arguments) {
        OtherWorkFragment fragment = new OtherWorkFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_work, container, false);
        initialization(view);
        setListeners();
        setData();
        return view;
    }

    private void initialization(View view) {
        Bundle arguments = getArguments();
        mTourPlan = (vTourPlan) arguments.getSerializable("tourPlan");
        mErrorMessage = arguments.getString("error");
        mTvType = view.findViewById(R.id.tvType);
        mTvAgent = view.findViewById(R.id.tvAgent);
        mTvSS = view.findViewById(R.id.tvSS);
        mEdtInput = view.findViewById(R.id.edtInput);
        mTvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        UtilityFunc.setOnFocusChangeListener(getActivity(), mEdtInput);
        mLnrAgent = view.findViewById(R.id.lnrAgent);
        mLnrSS = view.findViewById(R.id.lnrSS);

        if(!TextUtils.isEmpty(mErrorMessage)){
            mTvErrorMessage.setVisibility(View.VISIBLE);
            mTvErrorMessage.setText(mErrorMessage);
        }else{
            mTvErrorMessage.setVisibility(View.GONE);
        }

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
                    addTourPlanDetailsInputs(mEdtInput, mTourPlan.getId(), "Remark");
            }
        });

        //if tourDate is less than current date , its get disabled
        if (UtilityFunc.isAllowedTourPlan(getActivity(), mTourPlan.getTourDate())) {
            UtilityFunc.setOnOffState(mTvType, true);
            UtilityFunc.setOnOffState(mTvAgent, true);
            UtilityFunc.setOnOffState(mTvSS, true);
            UtilityFunc.setOnOffState(mEdtInput, true);
        } else {
            UtilityFunc.setOnOffState(mTvType, false);
            UtilityFunc.setOnOffState(mTvAgent, false);
            UtilityFunc.setOnOffState(mTvSS, false);
            UtilityFunc.setOnOffState(mEdtInput, false);
        }

        //once added tourplan restrict to edit either it from app or portal
        boolean isExistsTourDetail = new TourPlanRepo().isExistTouPlanDetail(mTourPlan.getId());
        if(isExistsTourDetail){
            UtilityFunc.setOnOffState(mTvType, false);
            UtilityFunc.setOnOffState(mTvAgent, false);
            UtilityFunc.setOnOffState(mTvSS, false);
            UtilityFunc.setOnOffState(mEdtInput, false);
        }

    }

    private void setData() {
        TourPlanDetailRepo detail = new TourPlanDetailRepo();
        ArrayList<vTourPlanDetail> list = (ArrayList<vTourPlanDetail>) detail.getTourPlanDetailAll(mTourPlan.getId());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().equalsIgnoreCase("Type")) {
                    String name = getValueById("Type", list.get(i).getValue());
                    mTvType.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("Agent Visit")) {
                    mLnrAgent.setVisibility(View.VISIBLE);
                    String name = getValueById("Agent Visit", list.get(i).getValue());
                    mTvAgent.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("SS Visit")) {
                    mLnrSS.setVisibility(View.VISIBLE);
                    String name = getValueById("SS Visit", list.get(i).getValue());
                    mTvSS.setText(name);
                } else if (list.get(i).getTitle().equalsIgnoreCase("Remark")) {
                    mEdtInput.setText(list.get(i).getValue());
                }
            }
        }
    }

    private void setListeners() {
        mTvType.setOnClickListener(this);
        mTvAgent.setOnClickListener(this);
        mTvSS.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mTvType) {
            mTvType.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            UtilityFunc.showSpinnerDialog(getActivity(), mTvType,
                    getSpinnerList("Type", -1), "Type", this);
        } else if (v == mTvAgent) {
            mTvAgent.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            UtilityFunc.showSpinnerDialog(getActivity(), mTvAgent,
                    getSpinnerList("Agent Visit", -1), "Agent Visit", this);
        } else if (v == mTvSS) {
            mTvSS.setBackground(getResources().getDrawable(R.drawable.bg_bottom_border_selected));
            UtilityFunc.showSpinnerDialog(getActivity(), mTvSS,
                    getSpinnerList("SS Visit", -1), "SS Visit", this);

        }
    }

    @Override
    public void onSpinnerItemClick(Object obj, String label) {

        if (label.equalsIgnoreCase("type")) {
            addTourPlanDetails(mTvType, mTourPlan.getId(), "Type");
            if (((vBaseSpinner) obj).getName().equalsIgnoreCase("Agent Visit")) {
                mLnrAgent.setVisibility(View.VISIBLE);
                mLnrSS.setVisibility(View.GONE);
            } else if (((vBaseSpinner) obj).getName().equalsIgnoreCase("SS Visit")) {
                mLnrSS.setVisibility(View.VISIBLE);
                mLnrAgent.setVisibility(View.GONE);
            } else {
                mLnrAgent.setVisibility(View.GONE);
                mLnrSS.setVisibility(View.GONE);
            }
        } else if (label.equalsIgnoreCase("Agent Visit")) {
            addTourPlanDetails(mTvAgent, mTourPlan.getId(), "Agent Visit");
        } else if (label.equalsIgnoreCase("SS Visit")) {
            addTourPlanDetails(mTvSS, mTourPlan.getId(), "SS Visit");
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
        if (label.equalsIgnoreCase("Type")) {
            list = (ArrayList<vBaseSpinner>) new OtherWorkReasonRepo().getReasonList1();
        } else if (label.equalsIgnoreCase("Agent Visit")) {
            list = (ArrayList<vBaseSpinner>) new AgentRepo().getAgentAll1(soId);
        } else if (label.equalsIgnoreCase("SS Visit")) {
            list = (ArrayList<vBaseSpinner>) new SSRepo().getSSList1(soId);
        }

        return list;
    }

    private String getValueById(String title, String id) {
        String value = "";
        if (!TextUtils.isEmpty(id)) {
            int selectedId = Integer.parseInt(id);
            if (title.equalsIgnoreCase("Type")) {
                ArrayList<vBaseSpinner> list = getSpinnerList("Type", -1);
                for (vBaseSpinner spinner : list) {
                    if (selectedId == spinner.getId()) {
                        value = spinner.getName();
                        break;
                    }
                }
            } else if (title.equalsIgnoreCase("Agent Visit")) {
                value = new AgentRepo().getSelected(selectedId);
            } else if (title.equalsIgnoreCase("SS Visit")) {
                value = new SSRepo().getSelected(selectedId);
            }
        }
        return value;
    }

}
