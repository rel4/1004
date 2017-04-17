package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanLogin;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.request.LoginRequest;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;

public class WelcomeActivity extends BaseAppCompatActivity implements OnAllLogListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        String account = Pref_Utils.getString(this, "account");
        String password = Pref_Utils.getString(this, "password");

        if (account != null && password != null) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.requestAllLog(this, account, password);
        } else {
            Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    toMainActivity();
                }
            }, 2000);
        }
        initBarUtils.setSystemBar(WelcomeActivity.this);
    }

    public void requestAllLogSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            BeanLogin beanLogin = (BeanLogin) baseObjectBean.getData();
            Pref_Utils.putString(this, "uid", beanLogin.getUid());
            Pref_Utils.putString(this, "username", beanLogin.getUsername());
            Pref_Utils.putString(this, "email", beanLogin.getEmail());
            Pref_Utils.putString(this, "mobil", beanLogin.getMobile());
            Pref_Utils.putString(this, "headImage", beanLogin.getImg());
            Pref_Utils.putString(this, "money", beanLogin.getMoney());
//            YYJXApplication.isLogin = true;
            Pref_Utils.putBoolean(WelcomeActivity.this, "isLogin", true);
        } else {

        }
        toMainActivity();
    }

    public void requestAllLogFailed(VolleyError error) {
        toMainActivity();
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
