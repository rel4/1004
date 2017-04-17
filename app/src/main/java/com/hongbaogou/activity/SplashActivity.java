package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;

/*
 * 浏览界面
 */
public class SplashActivity extends BaseAppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Pref_Utils.putBoolean(this, "isFirst", false);
        initBarUtils.setSystemBar(this);
    }
}
