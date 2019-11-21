package com.salescube.healthcare.demo.ctrl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.salescube.healthcare.demo.MenuScreenFragment;
import com.salescube.healthcare.demo.data.model.MyMenu;

import java.util.ArrayList;
import java.util.List;

public class MyViewPager extends FragmentStatePagerAdapter {

    FragmentManager fm;
    List<MyMenu> menuList;
    List<String> menuType;
    String type;
    boolean found=false;
    int totalScreen;
    int cnt;
    int n;


    public MyViewPager(FragmentManager fm, List<MyMenu> menuList) {
        super(fm);
        this.fm=fm;
        this.menuList=menuList;
        menuType=new ArrayList<>();

        // Find total no of Screen
        totalScreen = 0; // find out from list
        cnt=0;
        n= menuList.size();

        if(n>0) {
            totalScreen = menuList.get(0).getScreenNo();
            type=menuList.get(0).getMenuType();
            for(int s=0;s<n;s++)
            {
                cnt = menuList.get(s).getScreenNo();
                if(cnt > totalScreen){
                    totalScreen=cnt;
                }
                if(type.equals(menuList.get(s).getMenuType())){
                   found=true;
                }
                if(found && !menuType.contains(type)){
                    menuType.add(type);

                }
                type=menuList.get(s).getMenuType();

            }
        }
        Log.d("Menu","MenuList" + menuList.size() +"\tmenuType" + menuType.size());
    }

    @Override
    public Fragment getItem(int position) {

        return MenuScreenFragment.newInstance(menuList,position,totalScreen,menuType);
    }

    @Override
    public int getCount() {
        return totalScreen;
    }



}