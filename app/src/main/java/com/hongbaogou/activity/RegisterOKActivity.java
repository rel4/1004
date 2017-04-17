package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

public class RegisterOKActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView mBackbtn;

    private Button mLoginbtn;

    private String type;

    private TextView title;

    private TextView tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ok);
        findAllView();

        String type = getIntent().getStringExtra("type");

        if (type != null && type.equals("find")) {
            title = (TextView) findViewById(R.id.title);
            title.setText(R.string.pswdfind);

            tv_ok = (TextView) findViewById(R.id.tv_ok);
            tv_ok.setText(R.string.pswdfindok);
        }

        initBarUtils.setSystemBar(this);
    }


    /**
     * 出事话view
     */
    private void findAllView() {
        mBackbtn = (ImageView) findViewById(R.id.btn_back);
        mBackbtn.setOnClickListener(this);

        mLoginbtn = (Button) findViewById(R.id.btn_go_login);
        mLoginbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //后退按钮
            case R.id.btn_back:
                finish();
                break;
            //登录按钮,调转到登录的界面
            case R.id.btn_go_login:
                //TODO 跳转到登录的界面
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //跳转到userFragment界面
                Log.e("TAG", "-=-=-=");
                intent.putExtra("position", 4);
                startActivity(intent);
                break;
        }
      //  BaseUtils.colseSoftKeyboard(this);
    }
}
