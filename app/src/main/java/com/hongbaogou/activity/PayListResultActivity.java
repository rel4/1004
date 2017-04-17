package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

public class PayListResultActivity extends BaseAppCompatActivity {
    private  Intent mIntent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);
        initView();
        initData();
        initBarUtils.setSystemBar(this);
    }

    private void initData() {


    }

    private void initView() {

    }

    public void onClick(View view) {
        if (view!=null){
            switch (view.getId()){
                case  R.id.back:
                    finish();
                    break;
                //继续夺宝
                case R.id.tv_pay_jixu:
                    mIntent = new Intent(PayListResultActivity.this,MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.putExtra("position", 0);
                    startActivity(mIntent);
                    break;
                //查看夺宝记录
                case R.id.tv_pay_chakan:

                    break;
            }
        }
    }
}
