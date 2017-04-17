package com.hongbaogou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hongbaogou.fragment.UserBaseFragment;
import com.hongbaogou.utils.ContentTab;

import java.util.List;


public class SlidingTabFragmentAdapter extends FragmentPagerAdapter {

    private List<UserBaseFragment> list ;
    private ContentTab[] tabs;

    public SlidingTabFragmentAdapter(FragmentManager fm, List<UserBaseFragment> list, ContentTab[] tabs) {
        super(fm);
        this.list = list;
        this.tabs = tabs;
    }

    public Fragment getItem(int position) {
        return list.get(position);
    }

    public int getCount() {
        return tabs.length;
    }

    public CharSequence getPageTitle(int position) {
        CharSequence sequence = tabs[position].tname;
        return sequence;
    }
}
