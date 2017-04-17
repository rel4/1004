package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.SlidingTabFragmentAdapter;
import com.hongbaogou.fragment.SelfBuyFragment;
import com.hongbaogou.fragment.UserBaseFragment;
import com.hongbaogou.utils.ContentTab;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class SelfBuyRecordActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener,SelfBuyFragment.BuyStateListener {

    private TextView title;
    private List<UserBaseFragment> buyFragments;
    private int position;
    private String uid ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_log);

        uid = Pref_Utils.getString(this, "uid");
        findAllView();

        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabFragmentAdapter mAdapter = new SlidingTabFragmentAdapter(getSupportFragmentManager(),buyFragments ,getTabs());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(position);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout);
        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectCurrentTabTilteColor(getResources().getColor(R.color.color_red));
        slidingTabLayout.setUnselectCurrentTabTilteColor(getResources().getColor(R.color.self_buy_win));

        //自定义下划线颜色
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.color_red);
            }
        });

        initBarUtils.setSystemBar(this);
    }

    private void findAllView() {
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.duobao_log);
        buyFragments = new ArrayList<UserBaseFragment>();
        SelfBuyFragment selfBuyFragment = new SelfBuyFragment();
        selfBuyFragment.setBuyStateListener(this);
        buyFragments.add(selfBuyFragment);
        buyFragments.add(new SelfBuyFragment());
        buyFragments.add(new SelfBuyFragment());
    }

    public boolean getSateListener() {
        return true;
    }

    public  ContentTab[] getTabs() {
        ContentTab[] TABS = new ContentTab[]{
                new ContentTab(1, "全部"),
                new ContentTab(2, "进行中"),
                new ContentTab(3, "已揭晓")};
        return TABS;
    }

    public void back(View v) {
        finish();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("TAG", "onPageScrolled");
    }

    public void onPageSelected(int position) {
        Log.d("TAG", "onPageSelected");
        UserBaseFragment buyFragment = buyFragments.get(position);
        if(buyFragment != null){
            buyFragment.requestByPosition(uid, position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        Log.d("TAG", "onPageScrollStateChanged");
    }


    private boolean isEnterRequest = true;
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isEnterRequest){
            buyFragments.get(position).requestByPosition(uid,position);
            isEnterRequest = false;
        }
    }
}
