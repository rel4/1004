package com.hongbaogou.activity;

import android.os.Bundle;
import android.view.View;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

public class PayResultActivity extends BaseAppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        initBarUtils.setSystemBar(this);
    }

    public void back(View v){
        finish();
    }
}
