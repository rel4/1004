package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.SlidingTabFragmentAdapter;
import com.hongbaogou.fragment.SelfBuyFragment;
import com.hongbaogou.fragment.ShareRecordFragment;
import com.hongbaogou.fragment.UserBaseFragment;
import com.hongbaogou.fragment.WinRecordFragment;
import com.hongbaogou.utils.ContentTab;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.SlidingTabLayout;
import com.hongbaogou.view.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;


/*个人中心
 *
 */
public class CenterActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener, SelfBuyFragment.BuyStateListener {

    private ImageLoader imageLoader;
    private List<UserBaseFragment> userFragments;
    private String uid;

    public CenterActivity() {
        imageLoader = RequestManager.getImageLoader();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        initView();
        initBarUtils.setSystemBar(this);
    }

    private void initView() {
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");

        String headImgUrl = intent.getStringExtra("headImg");

        uid = intent.getStringExtra("id");

        Log.e("TAG", "Center id = " + uid);

        CircleImageView headImage = (CircleImageView) findViewById(R.id.headImage);

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(headImage, R.mipmap.img_blank, R.mipmap.img_blank);

        Log.e("TAG", "CenterActivity --headImgUrl = " + headImgUrl);
        Log.e("TAG", "CenterActivity --username = " + username);
        Log.e("TAG", "CenterActivity --uid = " + uid);
        imageLoader.get(headImgUrl, listener);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(username);
        TextView userid = (TextView) findViewById(R.id.userid);
        userid.setText(getResources().getString(R.string.userId, uid));

        userFragments = new ArrayList<UserBaseFragment>();
        SelfBuyFragment selfBuyFragment = new SelfBuyFragment();
        selfBuyFragment.setBuyStateListener(this);
        userFragments.add(selfBuyFragment);
        userFragments.add(new WinRecordFragment());
        userFragments.add(new ShareRecordFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabFragmentAdapter mAdapter = new SlidingTabFragmentAdapter(getSupportFragmentManager(), userFragments, getTabs());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

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

    }

    public ContentTab[] getTabs() {
        ContentTab[] TABS = new ContentTab[]{
                new ContentTab(1, "夺宝记录"),
                new ContentTab(2, "中奖记录"),
                new ContentTab(3, "晒单分享")};
        return TABS;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        Log.d("TAG", "onPageSelected");
        UserBaseFragment userBaseFragment = userFragments.get(position);
        if (userBaseFragment != null) {
            userBaseFragment.requestByPosition(uid, position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        Log.d("TAG", "onPageScrollStateChanged");
    }


    private boolean isEnterRequest = true;

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isEnterRequest) {
            userFragments.get(0).requestByPosition(uid, 0);
            isEnterRequest = false;
        }
    }

    public void back(View v) {
        finish();
    }

    public boolean getSateListener() {
        return false;
    }
}
