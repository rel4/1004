package com.hongbaogou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanLogin;
import com.hongbaogou.bean.OtherBeanLogin;
import com.hongbaogou.bean.SSO_UserBean;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.listener.OnOtherLoginListener;
import com.hongbaogou.request.LoginRequest;
import com.hongbaogou.request.OtherLoginRequest;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.MobileUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ShareMethodUtils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.view.EmailAutoCompleteTextView;

/**
 * 登录的activity的窗口
 */
public class LoginWindowActivity extends BaseAppCompatActivity implements View.OnClickListener, OnAllLogListener,OnOtherLoginListener {
    //dialog的对象
    private ProgressDialog mDialog;

    private LoginRequest loginRequest;
    //账号和密码
    private EmailAutoCompleteTextView mAcc;
    private EditText mPswd;

    //登录的按钮
    private Button mLoginbtn;
    //注册的按钮
    private Button register;
    //忘记密码
    private TextView forget_pswd;

    private String acc;
    private String pswd;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ShareMethodUtils.SSO_LOGIN:
                    SSO_UserBean sso_userBean = (SSO_UserBean) msg.obj;
                    System.out.println("----------------------------weixin");
                    handleLoginRequest(sso_userBean);
                    break;
                case ShareMethodUtils.ERROR:
                    ToastUtil.showToast(LoginWindowActivity.this,"授权登陆失败");
                    break;
            }
        }
    };
    private boolean open_login;

    private void handleLoginRequest(SSO_UserBean sso_userBean) {
        OtherLoginRequest otherLoginRequest = new OtherLoginRequest();
        otherLoginRequest.requestOtherLogin(LoginWindowActivity.this, sso_userBean.id, sso_userBean.type, sso_userBean.name, sso_userBean.userIcon);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        open_login = Pref_Utils.getBoolean(LoginWindowActivity.this, "open_login", false);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_window);
        findAllView();
    }

    /**
     * 出事话view
     */
    //初始化view
    private void findAllView() {
        View openView = findViewById(R.id.ll_open_login);
        if(open_login){
            openView.setVisibility(View.VISIBLE);
        }else {
            openView.setVisibility(View.GONE);
        }



        //注册的按钮
        register = (Button) findViewById(R.id.btn_phone_register);
        register.setOnClickListener(this);

        //忘记密码的按钮
        forget_pswd = (TextView) findViewById(R.id.forget_pswd);
        forget_pswd.setOnClickListener(this);

        //找到账号和密码的输入框
        mAcc = (EmailAutoCompleteTextView) findViewById(R.id.editText_acc);
        mPswd = (EditText) findViewById(R.id.editText_pswd);

        //点击登录按钮的监听器
        mLoginbtn = (Button) findViewById(R.id.btn_login);
        mLoginbtn.setOnClickListener(this);


        findViewById(R.id.img_qq).setOnClickListener(this);
        findViewById(R.id.img_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SelfBuyRecordActivity.class);
        switch (v.getId()) {
            //注册的按钮
            case R.id.btn_phone_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            //跳转到忘记密码的activity
            case R.id.forget_pswd:
                startActivity(new Intent(this, ForgetPassWorldActivity.class));
                break;
            //登录按钮的点击事件
            case R.id.btn_login:
                acc = mAcc.getText().toString();
                pswd = mPswd.getText().toString();

                boolean isTrue = MobileUtils.isMobileNO(acc,this);
                if (isTrue == false){
                    ToastUtil.showToast(this,"请输入正确的手机号码");
                    return;
                }
                if (acc.equals("")){
                    ToastUtil.showToast(this,"手机号码不能为空");
                    return;
                }
                if (pswd.equals("")){
                    ToastUtil.showToast(this,"密码不能为空");
                    return;
                }
                if (pswd.length()<6){
                    ToastUtil.showToast(this,"密码长度不符");
                    return;
                }
                //判断输入的类型只能为字母数字和下划线
                if (!pswd.matches("[a-zA-Z0-9]*")) {
                    ToastUtil.showToast(this, "您设置的密码中包含非法字符");
                    return;
                }

                pswd = BaseUtils.getRandomString(6) + pswd + BaseUtils.getRandomString(6);

                byte[] b = pswd.getBytes();

                pswd = Base64.encodeToString(b, Base64.DEFAULT);


                if (acc.equals("") || pswd.equals("")) {
                    ToastUtil.showToast(this, "账号或密码不能为空!");
                    return;
                }
//                acc = mAcc.getText().toString();
//                pswd = mPswd.getText().toString();
                //判断账号和密码是否正确
                isLoginOk(acc, pswd);
                break;
            case R.id.img_qq:
                ToastUtil.showToast(LoginWindowActivity.this,"授权中...");
                ShareMethodUtils.start(LoginWindowActivity.this);
                ShareMethodUtils.login_qq(handler,true);
                break;
            case R.id.img_weixin:
                ToastUtil.showToast(LoginWindowActivity.this,"授权中...");
                ShareMethodUtils.start(LoginWindowActivity.this);
                ShareMethodUtils.login_weixin(handler,true);
                break;
            default:
                break;
        }
        BaseUtils.colseSoftKeyboard(this);
    }

    /**
     * 判断账号和密码是否正确
     *
     * @param acc
     * @param pswd
     */
    private void isLoginOk(String acc, String pswd) {
        showDialog();
        loginRequest = new LoginRequest();
        loginRequest.requestAllLog(this, acc, pswd);
    }

    /**
     * dialog的显示
     */
    private void showDialog() {
        mDialog = new ProgressDialog(this);
        //声明进度条对话框
        //创建ProgressDialog对象
        // 设置进度条风格，风格为圆形，旋转的
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("登录中...");
        mDialog.setCanceledOnTouchOutside(false);
        // 设置ProgressDialog 标题图标
        //mDialog.setIcon(R.mipmap.ic_loading_wrapper_white);
        // 设置ProgressDialog 的进度条是否不明确
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
        // 让ProgressDialog显示
        mDialog.show();
    }

    /**
     * 登录成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestAllLogSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            //登录成功 就设置为已登录状态
//            YYJXApplication.isLogin = true;
            Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", true);
            BeanLogin beanLogin = (BeanLogin) baseObjectBean.getData();
            Pref_Utils.putString(this, "uid", beanLogin.getUid());
            Pref_Utils.putString(this, "account", acc);
            Pref_Utils.putString(this, "password", pswd);
            Pref_Utils.putString(this, "username", beanLogin.getUsername());
            Pref_Utils.putString(this, "email", beanLogin.getEmail());
            Pref_Utils.putString(this, "mobile", beanLogin.getMobile());
            Pref_Utils.putString(this, "headImage", beanLogin.getImg());
            Pref_Utils.putString(this, "money", beanLogin.getMoney());
            Pref_Utils.putString(this, "yaoqing", beanLogin.getYaoqing());
            Pref_Utils.putString(this, "rebate_total", beanLogin.getRebate_total());

            finish();

        } else {
//            YYJXApplication.isLogin = false;
            Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", false);
            mDialog.cancel();
            ToastUtil.showToast(this, "账号或密码输入错误,请重新输入!");
        }
    }

    /**
     * 登录失败的回调
     *
     * @param error
     */
    @Override
    public void requestAllLogFailed(VolleyError error) {
        mDialog.dismiss();
//        YYJXApplication.isLogin = false;
        Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", false);
    }

    public void back(View view) {
        BaseUtils.colseSoftKeyboard(this);
        onBackPressed();
    }

    @Override
    public void requestOnOtherLoginSuccess(BaseObjectBean baseObjectBean) {
        int status = baseObjectBean.getStatus();
        if(status == 1){

//            YYJXApplication.isLogin = true;
            Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", true);

            OtherBeanLogin otherBeanLogin = (OtherBeanLogin) baseObjectBean.getData();

            Pref_Utils.putString(LoginWindowActivity.this, "uid", otherBeanLogin.getUid());
            Pref_Utils.putString(LoginWindowActivity.this, "account", acc);
            Pref_Utils.putString(LoginWindowActivity.this, "password", pswd);
            Pref_Utils.putString(LoginWindowActivity.this, "username", otherBeanLogin.getUsername());
            Pref_Utils.putString(LoginWindowActivity.this, "email", otherBeanLogin.getEmail());
            Pref_Utils.putString(LoginWindowActivity.this, "mobile", otherBeanLogin.getMobile());
            Pref_Utils.putString(LoginWindowActivity.this, "headImage", otherBeanLogin.getImg());
            Pref_Utils.putString(LoginWindowActivity.this, "money", otherBeanLogin.getMoney());
            Pref_Utils.putString(LoginWindowActivity.this, "yaoqing", otherBeanLogin.getYaoqing());
            Pref_Utils.putString(LoginWindowActivity.this, "rebate_total", otherBeanLogin.getRebate_total());
            finish();

        } else {

//            YYJXApplication.isLogin = false;
            Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", false);
            Toast.makeText(LoginWindowActivity.this, "账号或密码输入错误,请重新输入!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestOnOtherLoginFailed(VolleyError error) {
//        YYJXApplication.isLogin = false;
        Pref_Utils.putBoolean(LoginWindowActivity.this, "isLogin", false);
    }
}
