package com.hongbaogou.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.listener.OnPersonalDataSMSListener;
import com.hongbaogou.listener.OnPersonalDataSMSSaveListener;
import com.hongbaogou.request.PersonalDataSMSRequests;
import com.hongbaogou.request.PersonalDataSMSSaveRequests;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


//绑定手机
public class PersonaldataBindTelActivity extends BaseAppCompatActivity implements OnPersonalDataSMSListener, OnPersonalDataSMSSaveListener {
    private ImageView mImageView;
    private Button titleright;
    private ImageButton mImageButton;
    private EditText et_personaldata_telnum_input, et_personaldata_sms_input;
    private TimeCount time;
    private PersonalDataSMSRequests mrequest;
    private PersonalDataSMSSaveRequests mrequestSave;
    private String uid;
    private String mMobile;
    private String old_mobile;
    private BroadcastReceiver smsReceiver;
    private IntentFilter filter2;
    private Handler handler;
    private String strContent;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    private AlertDialog mDialog;
    private TextView btn_personaldata_sendsms;
    private boolean fromOherLogin;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_tel);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        fromOherLogin = getIntent().getBooleanExtra("fromOherLogin", false);

        if (fromOherLogin) {
            ((TextView) findViewById(R.id.tv_recharge)).setText("绑定手机号码");
        }

        initBarUtils.setSystemBar(this);
        initView();
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                et_personaldata_sms_input.setText(strContent);
            }
        };
        filter2 = new IntentFilter();
        filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter2.setPriority(Integer.MAX_VALUE);
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    byte[] pdu = (byte[]) obj;
                    SmsMessage sms = SmsMessage.createFromPdu(pdu);
                    // 短信的内容
                    String message = sms.getMessageBody();
                    Log.d("logo", "message     " + message);
                    // 短息的手机号。。+86开头？
                    String from = sms.getOriginatingAddress();
                    Log.d("logo", "from     " + from);
                    // Time time = new Time();
                    // time.set(sms.getTimestampMillis());
                    // String time2 = time.format3339(true);
                    // Log.d("logo", from + "   " + message + "  " + time2);
                    // strContent = from + "   " + message;
                    // handler.sendEmptyMessage(1);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            strContent = code;
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        };
        registerReceiver(smsReceiver, filter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.back);
        titleright = (Button) findViewById(R.id.titleright);
        mImageButton = (ImageButton) findViewById(R.id.ibtn_personaldata_delete);
        btn_personaldata_sendsms = (TextView) findViewById(R.id.btn_personaldata_sendsms);
        et_personaldata_telnum_input = (EditText) findViewById(R.id.et_personaldata_telnum_input);
        et_personaldata_sms_input = (EditText) findViewById(R.id.et_personaldata_sms_input);
        Intent intent = getIntent();
        old_mobile = intent.getStringExtra("phone");
        et_personaldata_telnum_input.setText(old_mobile);
        et_personaldata_telnum_input.setInputType(InputType.TYPE_CLASS_PHONE);
        uid = Pref_Utils.getString(getApplicationContext(), "uid");
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.back:
                    BaseUtils.colseSoftKeyboard(this);
                    finish();
                    break;
                case R.id.titleright:
                    if (TextUtils.isEmpty(et_personaldata_sms_input.getText()) && et_personaldata_sms_input.getText().length() != 6 && !et_personaldata_sms_input.getText().equals(strContent)) {
                        ToastUtil.showToast(this, "填写6位正确的验证码才能保存哦");
                        mImageButton.setVisibility(View.INVISIBLE);
                    } else {
                        mrequestSave = new PersonalDataSMSSaveRequests();
                        mMobile = et_personaldata_telnum_input.getText().toString();

                        if(fromOherLogin){
//                            mrequestSave.personalDatasmsSaveRequests(uid, old_mobile, mMobile, strContent,"weixin", this);
                            mrequestSave.personalDatasmsSaveRequests(uid, old_mobile, mMobile, et_personaldata_sms_input.getText().toString(),"weixin", this);
                        }else {

                            System.out.println("---------------------"+et_personaldata_sms_input.getText().toString());

                            mrequestSave.personalDatasmsSaveRequests(uid, old_mobile, mMobile, et_personaldata_sms_input.getText().toString(),"null", this);
                        }

                        BaseUtils.colseSoftKeyboard(this);
                    }
                    break;

                case R.id.ibtn_personaldata_delete:
                    et_personaldata_telnum_input.getText().clear();
                    break;

                case R.id.btn_personaldata_sendsms:
                    if(fromOherLogin){
                        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                        mrequest = new PersonalDataSMSRequests();
                        mMobile = et_personaldata_telnum_input.getText().toString();
                        if (TextUtils.isEmpty(mMobile)) {
                            ToastUtil.showToast(this, "手机号码为空或者不正确！");
                        } else {

                            if (fromOherLogin) {
                                mrequest.personalDatasmsRequests(uid, mMobile,"weixin", this);
                            }else {
                                mrequest.personalDatasmsRequests(uid, mMobile,"null", this);
                            }

                            time.start();//开始计时
                        }
                    }else {
                        if (old_mobile != null && old_mobile.equals(Pref_Utils.getString(getApplicationContext(), "mobil"))
                                && et_personaldata_telnum_input.getText().toString().equals(Pref_Utils.getString(getApplicationContext(), "mobil"))) {
                            ToastUtil.showToast(this, "手机号码未修改，无需获取验证码");
                        } else {
                            time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                            mrequest = new PersonalDataSMSRequests();
                            mMobile = et_personaldata_telnum_input.getText().toString();
                            if (TextUtils.isEmpty(mMobile)) {
                                ToastUtil.showToast(this, "手机号码为空或者不正确！");
                            } else {

                                if (fromOherLogin) {
                                    mrequest.personalDatasmsRequests(uid, mMobile,"weixin", this);
                                }else {
                                    mrequest.personalDatasmsRequests(uid, mMobile,"null", this);
                                }

                                time.start();//开始计时
                            }
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void OnPersonalDataSMSListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            int status = baseObjectBean.getStatus();
            if (status == 1) {
                ToastUtil.showToast(this, "提交成功");

            } else {
                AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
                mdialog.setMessage("手机号码未修改，无需获取验证码");
                mdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
                mDialog = mdialog.create();
                mDialog.show();
            }
        }
    }

    @Override
    public void OnPersonalDataSMSListeneroFailed(VolleyError error) {

    }

    //修改短信
    @Override
    public void OnPersonalDataSMSSaveListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            int status = baseObjectBean.getStatus();
            if (status == 1) {
                Pref_Utils.putString(getApplicationContext(), "mobile", mMobile);
                Intent intent = new Intent();
                intent.putExtra("mobile", mMobile);
                setResult(4, intent);
                ToastUtil.showToast(this, "保存成功");

                if(fromOherLogin){
                    Intent mBroadIntent = new Intent();
                    mBroadIntent.setAction(ConstantValues.OTHERLOGIN);
                    mLocalBroadcastManager.sendBroadcast(mBroadIntent);
                }

                PersonaldataBindTelActivity.this.finish();

            } else {
                ToastUtil.showToast(this, "保存失败");
            }
        }
    }

    @Override
    public void OnPersonalDataSMSSaveListeneroFailed(VolleyError error) {
        ToastUtil.showToast(this, "保存失败");
    }


    //获取短信验证码倒计时
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btn_personaldata_sendsms.setText("重新验证");
            btn_personaldata_sendsms.setClickable(true);
            btn_personaldata_sendsms.setBackgroundResource(R.mipmap.personaldata_sendsms);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btn_personaldata_sendsms.setClickable(false);
            btn_personaldata_sendsms.setText("(" + millisUntilFinished / 1000 + "s)重新获取");
            btn_personaldata_sendsms.setBackgroundResource(R.mipmap.personaldata_sendsmsagain);
        }
    }
}
