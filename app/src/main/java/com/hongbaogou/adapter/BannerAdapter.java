package com.hongbaogou.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class BannerAdapter extends PagerAdapter {

    private List<View> bannerViews;

    public BannerAdapter(List<View> bannerViews) {
        this.bannerViews = bannerViews;
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(bannerViews.get(position));
    }

    public void finishUpdate(ViewGroup container) {

    }

    public int getCount() {
        return bannerViews.size();
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        View view = bannerViews.get(position);
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void startUpdate(ViewGroup container) {

    }
}


