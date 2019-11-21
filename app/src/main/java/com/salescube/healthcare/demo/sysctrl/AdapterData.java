package com.salescube.healthcare.demo.sysctrl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.EmployeeRepo;
import com.salescube.healthcare.demo.view.vAgent;
import com.salescube.healthcare.demo.view.vEmployee;

import java.util.List;

/**
 * Created by user on 23/05/2017.
 */

public class AdapterData {

    public static ArrayAdapter<vAgent> getAgent(Context context, int useId, String defaultValue) {

        AgentRepo objRepo = new AgentRepo();
        List<vAgent> objList = objRepo.getAgentAll(useId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            objList.add(0, new vAgent(0, defaultValue));
        }

        ArrayAdapter<vAgent> adp = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  adp;
    }

    public static ArrayAdapter<vEmployee> getEmployeeList(Context context, int useId, String defaultValue) {

        EmployeeRepo objRepo = new EmployeeRepo();
        List<vEmployee> objList = objRepo.getEmployee(useId) ;

        if (!TextUtils.isEmpty(defaultValue)){
            objList.add(0, new vEmployee(0, defaultValue));
        }

        ArrayAdapter<vEmployee> adp = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, objList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  adp;
    }
}
