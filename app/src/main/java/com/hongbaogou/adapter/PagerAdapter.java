package com.hongbaogou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list = new ArrayList<Fragment>();

    public PagerAdapter(FragmentManager fm,List<Fragment> list){
        super(fm);
        this.list = list;
    }

    public PagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
