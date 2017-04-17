package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

public class PaySuccessActivity extends BaseAppCompatActivity {
    private TextView mText;
    private Button mbtn_recharge_bindtel,mbtn_recharge_continuesuperise;
    private  Intent mIntent ;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initBarUtils.setSystemBar(this);
        initView();
        initDaata();
    }

    private void initDaata() {
        mText.setText(R.string.recharge_result);
    }

    private void initView() {
        mText = (TextView) findViewById(R.id.title);
        mbtn_recharge_bindtel = (Button) findViewById(R.id.btn_recharge_bindtel);
        mbtn_recharge_continuesuperise = (Button) findViewById(R.id.btn_recharge_continuesuperise);
        mImageView=(ImageView) findViewById(R.id.btn_back);
    }

    public void back(View view) {
        if (view!=null){
            switch (view.getId()){
                case  R.id.btn_back:
                    finish();
                    break;
                case  R.id.btn_recharge_bindtel:
                   mIntent = new Intent(PaySuccessActivity.this,PersonaldataBindTelActivity.class);
                    startActivity(mIntent);
                    break;
                case  R.id.btn_recharge_continuesuperise:
                    mIntent = new Intent(PaySuccessActivity.this,MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.putExtra("position", 0);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    }
}
