package com.hongbaogou.activity;

import android.os.Bundle;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

//绑定手机
public class BindTelActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_tel);
        initBarUtils.setSystemBar(this);
    }
}
