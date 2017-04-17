package com.hongbaogou.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.listener.OnGerCodeListener;
import com.hongbaogou.listener.OnRegisterOkListener;
import com.hongbaogou.request.GetCodeRequest;
import com.hongbaogou.request.RegisterOkRequest;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.MobileUtils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 忘记密码的界面
 */
public class ForgetPassWorldActivity extends BaseAppCompatActivity implements View.OnClickListener, OnGerCodeListener, OnRegisterOkListener {


    private ImageView mBackbtn;
    //手机号码
    private EditText mPhoneNumber;
    //密码
    private EditText mPassword;
    //获取验证码
    private TextView mGetCode;

    //清除输入的内容
    private ImageView mBtnClean;
    //保存按钮
    private TextView mSaveTextview;

    //验证码输入框
    private EditText editText;
    //定时器
    private TimeCount time;

    //获取注册吗的请求
    private GetCodeRequest getCodeRequest;

    private RegisterOkRequest registerOkRequest;

    //是否已经收到验证码,并且验证码是否已经被输入
    private boolean isGetCode = false;

    //记录账号和密码
    private String mobile;

    private String password;

    private SmsContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_world);
        findAllView();
        initBarUtils.setSystemBar(this);
    }

    /**
     * 初始化view
     */
    private void findAllView() {
        //手机号码的输入框
        mPhoneNumber = (EditText) findViewById(R.id.editText_phont_number);
        //密码的收入框
        mPassword = (EditText) findViewById(R.id.editText_password);

        //获取验证码的输入框
        mGetCode = (TextView) findViewById(R.id.getcode);
        mGetCode.setOnClickListener(this);

        mBackbtn = (ImageView) findViewById(R.id.btn_back);
        mBackbtn.setOnClickListener(this);

        //保存按钮的点击事件
        mSaveTextview = (TextView) findViewById(R.id.menuItem);
        mSaveTextview.setOnClickListener(this);

        mBtnClean = (ImageView) findViewById(R.id.btn_edtext_clean);
        mBtnClean.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.editText_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击清空图标
            case R.id.btn_edtext_clean:
                //设置内容为空
                mPhoneNumber.setText("");
                break;
            //保存按钮
            case R.id.menuItem:

                //保存成功会跳转到注册成功界面
                if (isGetCode == true) {
                    String code = editText.getText().toString();
                    if (code.length() != 6) {
                        ToastUtil.showToast(this, "请输入正确的验证码");
                        return;
                    }
                    //更改的方法
                    requestChangePswd(mobile, password, code);

                    Log.e("TAG","password 更改密码 = "+password);
                } else {
                    ToastUtil.showToast(this, "找回失败");
                }

                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.getcode:
                String string = getResources().getString(R.string.getcode);
                String string2 = "重新发送";
                String trim = mGetCode.getText().toString().trim();
                if(string.equals(trim)||string2.equals(trim)){
                }else{
                    return;
                }

                mobile = mPhoneNumber.getText().toString();
                password = mPassword.getText().toString();

                boolean isTrue = MobileUtils.isMobileNO(mobile, this);

                if (isTrue == false) {
                    ToastUtil.showToast(this, "请输入正确的手机号码");
                    return;
                }
                if (mobile.equals("")) {
                    ToastUtil.showToast(this, "手机号码不能为空");
                    return;
                }
                if (password.equals("")) {
                    ToastUtil.showToast(this, "密码不能为空");
                    return;
                }
                if (password.length() < 6) {
                    ToastUtil.showToast(this, "密码太短了哦~");
                    return;
                }

                //判断输入的类型只能为字母数字和下划线
//                if (!password.matches("[a-zA-Z0-9]*")) {
//                    ToastUtil.showToast(this, "您设置的密码中包含非法字符");
//                    return;
//                }

                //获取验证码的时候手机号不能被清空
                if (isGetCode == true) {
                    mBtnClean.setClickable(false);
                }

                password = BaseUtils.getRandomString(6) + password + BaseUtils.getRandomString(6);

                byte[] b = password.getBytes();

                password = Base64.encodeToString(b, Base64.DEFAULT);

                getCodeMenthod(mobile, password);

                Log.e("TAG","password 获取验证码 = "+password);

                break;
            default:
                break;
        }
        BaseUtils.colseSoftKeyboard(this);
    }

    /**
     * 修改密码的方法
     *
     * @param mobile   手机号码
     * @param password 密码
     * @param code     验证码
     */
    private void requestChangePswd(String mobile, String password, String code) {
        registerOkRequest = new RegisterOkRequest();
        registerOkRequest.requestFindOutOk(this, mobile, password, code);
    }

    /**
     * 获取验证码的方法
     *
     * @param mobile   电话号码
     * @param password 登录的密码
     */
    private void getCodeMenthod(final String mobile, String password) {

        if (!mobile.equals("") && !password.equals("")) {
            if (time != null) {
                time.cancel();
            }
            time = new TimeCount(60000, 1000);//构造CountDownTimer对象
            time.start();//开始计时
            sendRequestCode(mobile);
        } else {

        }
    }

    /**
     * 验证码的请求
     */
    private void sendRequestCode(String mobile) {
        getCodeRequest = new GetCodeRequest();
        getCodeRequest.requestGetFindCode(this, mobile);
    }

    /**
     * 请求验证码成功
     *
     * @param baseObjectBean
     */
    @Override
    public void requestGerCodeSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            ToastUtil.showToast(this, "验证码已发送至您的手机,请查收~");
            //已经获取到验证码
            isGetCode = true;
        }
    }

    /**
     * 请求验证码失败
     *
     * @param error
     */
    @Override
    public void requestGerCodeFailed(VolleyError error) {
        ToastUtil.showToast(this, "找回密码失败");
    }


    /**
     * 密码找回成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestRegisterOKSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            //注册成功 跳转界面
            Intent intent = new Intent(this, RegisterOKActivity.class);
            intent.putExtra("type", "find");
            startActivity(intent);

        } else {
            ToastUtil.showToast(this, "找回失败");
        }
    }

    /**
     * 面膜找回失败的回调
     *
     * @param error
     */
    @Override
    public void requestRegisterOKFailed(VolleyError error) {

    }

    /**
     * 获取短信验证码倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mGetCode.setText("重新发送");
            mGetCode.setClickable(true);
            //设置字体的颜色
            mGetCode.setTextColor(getResources().getColor(R.color.color_white));
            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.addto_bg));
            time.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mGetCode.setClickable(false);
            mGetCode.setText("重新获取(" + millisUntilFinished / 1000 + "s)");
            //设置字体的颜色
            mGetCode.setTextColor(getResources().getColor(R.color.black_bj));
            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.addto_blue_gray));
        }
    }

    /**
     * 监听短信数据库
     */
    class SmsContent extends ContentObserver {
        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
            Log.d("TAG", "SmsContent-创建了");
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 读取收件箱中指定号码的短信
            cursor = managedQuery(Uri.parse("content://sms/inbox"),
                    new String[]{"_id", "address", "read", "body"},
                    " address=? and read=?",
                    new String[]{"监听的号码", "0"}, "_id desc");
            // 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1"); // 修改短信为已读模式
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);
                Log.d("TAG", "短信的内容 = " + smsBody);
                editText.setText(getDynamicPassword(smsBody));
            }
            // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
    }

    /**
     * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        // 6是验证码的位数一般为六位
        Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
                + 6 + "})(?![0-9])");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            System.out.print(m.group());
            dynamicPassword = m.group();
        }
        Log.d("TAG", "截取6為的驗證碼 = " + dynamicPassword);
        return dynamicPassword;
    }

}
