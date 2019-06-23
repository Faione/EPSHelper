package com.example.faione.epshelper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPageAdapter extends FragmentPagerAdapter {
    private String[] title = new String[]{"今日","昨日","最近一星期"};

    public MyPageAdapter(FragmentManager manager){
        super(manager);
    }
    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            return new TDFragment();
        }else if(i == 1){
            return new YDFragment();
        }else{
            return new WekFragment();
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
