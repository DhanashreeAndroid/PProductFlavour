package com.salescube.healthcare.demo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.func.UtilityFunc;
import com.salescube.healthcare.demo.view.vTourPlan;
import com.salescube.healthcare.demo.view.vTourPlanDetail;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HolidayFragment extends Fragment {

    TextView mTvErrorMessage;
    EditText mEdtInput;
    vTourPlan mTourPlan;
    String mErrorMessage;

    public HolidayFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Bundle arguments) {
        HolidayFragment fragment = new HolidayFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holiday, container, false);
        initialization(view);
        setData();
        return view;
    }

    private void initialization(View view) {
        Bundle arguments = getArguments();
        mTourPlan = (vTourPlan) arguments.getSerializable("tourPlan");
        mErrorMessage = arguments.getString("error");
        mEdtInput = view.findViewById(R.id.edtInput);
        mTvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        UtilityFunc.setOnFocusChangeListener(getActivity(), mEdtInput);

        if(!TextUtils.isEmpty(mErrorMessage)){
            mTvErrorMessage.setVisibility(View.VISIBLE);
            mTvErrorMessage.setText(mErrorMessage);
        }else{
            mTvErrorMessage.setVisibility(View.GONE);
        }

        //if tourDate is less than current date , its get disabled
        if (UtilityFunc.isAllowedTourPlan(getActivity(), mTourPlan.getTourDate())) {
            UtilityFunc.setOnOffState(mEdtInput, true);
        }else{
            UtilityFunc.setOnOffState(mEdtInput, false);
        }

    }

    private void setData() {
        ArrayList<vTourPlanDetail> list = (ArrayList<vTourPlanDetail>) mTourPlan.getDetail();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().equalsIgnoreCase("Remark")) {
                    mEdtInput.setText(list.get(i).getValue());
                }
            }
        }
    }


}
