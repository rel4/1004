package com.hongbaogou.activity.base;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/12/24.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BaseAppCompatActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"BaseAppCompatActivity"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BaseAppCompatActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"BaseAppCompatActivity"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
