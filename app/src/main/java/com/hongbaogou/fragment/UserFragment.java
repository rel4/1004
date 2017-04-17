package com.hongbaogou.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.activity.BaseWebViewActivity;
import com.hongbaogou.activity.ForgetPassWorldActivity;
import com.hongbaogou.activity.MyShareActivity;
import com.hongbaogou.activity.PersonaldataActivity;
import com.hongbaogou.activity.PersonaldataBindTelActivity;
import com.hongbaogou.activity.RechargeActivity;
import com.hongbaogou.activity.RechargeRecordActivity;
import com.hongbaogou.activity.RegisterActivity;
import com.hongbaogou.activity.SelfBuyRecordActivity;
import com.hongbaogou.activity.SettingActivity;
import com.hongbaogou.activity.ViteFriendActivity;
import com.hongbaogou.activity.WinRecordActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanLogin;
import com.hongbaogou.bean.BeanMoney;
import com.hongbaogou.bean.OtherBeanLogin;
import com.hongbaogou.bean.SSO_UserBean;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.httpApi.API;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.listener.OnMoneyFreshListener;
import com.hongbaogou.listener.OnOtherLoginListener;
import com.hongbaogou.request.LoginRequest;
import com.hongbaogou.request.MoneyRequest;
import com.hongbaogou.request.OtherLoginRequest;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.HeadImageViewUtils;
import com.hongbaogou.utils.MobileUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.ShareMethodUtils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.EmailAutoCompleteTextView;
import com.hongbaogou.view.circleimageview.CircleImageView;


public class UserFragment extends BaseFragment implements View.OnClickListener, OnAllLogListener, OnOtherLoginListener,OnMoneyFreshListener {

    private TextView layoutTitle;
    //充值的按钮
    private TextView mTopUpBtn;
    //充值记录
    private RelativeLayout mTopUpLog;
    //中奖记录
    private RelativeLayout mWinLog;
    //我的晒单
    private RelativeLayout mBaskLog;
    //未登录的布局
    private RelativeLayout mNoLogin;
    //已经登录的布局
    private RelativeLayout mLogined;
    //跟布局
    private View mView;
    //设置按钮
    private RelativeLayout mSet_click;

    //登录的按钮
    private Button mLoginbtn;

    //账号和密码
    private EmailAutoCompleteTextView mAcc;

    private EditText mPswd;

    //邀请好友的布局
    private RelativeLayout mFirends;

    //全部夺宝记录
    private LinearLayout mAllSnatchLog;
    //进行中
    private LinearLayout mIngLog;
    //已揭晓
    private LinearLayout mOverLog;
    //忘记密码
    private TextView forget_pswd;
    //通知的按钮
    private ImageView mBtnNotify;
    //圆形图片
    private CircleImageView mBtn_usericon;

    private LoginRequest loginRequest;

    //余额多少
    private TextView money;
    //用户名
    private TextView username;

    private CircleImageView usericon;

    //记录账号和密码
    private String acc;
    private String pswd;

    //注册的按钮
    private Button register;

    private ImageView imgQQ;
    private ImageView imgWB;
    private ImageView imgWX;

    private ScrollView scrollView;

    private ImageLoader mImageLoader;
    private  MoneyRequest mMoneyRequest;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ShareMethodUtils.SSO_LOGIN:
                    sso_userBean = (SSO_UserBean) msg.obj;
                    handleLoginRequest(sso_userBean);
                    break;
                case ShareMethodUtils.ERROR:
                    ToastUtil.showToast(getContext(), "授权登陆失败");
                    break;
            }
        }
    };
    private boolean open_login;
    private LocalBroadcastManager localBroadcastManager;
    private TempmoneyReceiver tempmoneyReceiver;
    private SSO_UserBean sso_userBean;
    private boolean isFreshMoney=false;//是否正在更新金额，true是
    private void handleLoginRequest(SSO_UserBean sso_userBean) {
        OtherLoginRequest otherLoginRequest = new OtherLoginRequest();
        otherLoginRequest.requestOtherLogin(UserFragment.this, sso_userBean.id, sso_userBean.type, sso_userBean.name, sso_userBean.userIcon);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        open_login = Pref_Utils.getBoolean(getContext(), "open_login", false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user, container, false);
        findAllView();
        initBarUtils.setSystemBar(getActivity());

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValues.PAY_SUCCESS);
        intentFilter.addAction(ConstantValues.OTHERLOGIN);
        tempmoneyReceiver = new TempmoneyReceiver();
        localBroadcastManager.registerReceiver(tempmoneyReceiver, intentFilter);

        return mView;
    }

    @Override
    public void requestOnOtherLoginSuccess(BaseObjectBean baseObjectBean) {
        int status = baseObjectBean.getStatus();
        int is_mobile = baseObjectBean.getIs_mobile();
        if (status == 1) {

            OtherBeanLogin otherBeanLogin = (OtherBeanLogin) baseObjectBean.getData();

            Pref_Utils.putString(getActivity(), "uid", otherBeanLogin.getUid());
            Pref_Utils.putString(getActivity(), "account", acc);
            Pref_Utils.putString(getActivity(), "password", pswd);
            Pref_Utils.putString(getActivity(), "username", otherBeanLogin.getUsername());
            Pref_Utils.putString(getActivity(), "email", otherBeanLogin.getEmail());
            Pref_Utils.putString(getActivity(), "mobile", otherBeanLogin.getMobile());
            Pref_Utils.putString(getActivity(), "headImage", otherBeanLogin.getImg());
            Pref_Utils.putString(getActivity(), "money", otherBeanLogin.getMoney());
            Pref_Utils.putString(getActivity(), "yaoqing", otherBeanLogin.getYaoqing());
            Pref_Utils.putString(getActivity(), "rebate_total", otherBeanLogin.getRebate_total());
            String str = "余额 : " + otherBeanLogin.getMoney() + " 夺宝币";
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), str.length() - 3, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            money.setTextColor(getResources().getColor(R.color.color_loading));
            money.setText(ssb);

            username.setText(otherBeanLogin.getUsername());
            //设置图片标记
            usericon.setTag(otherBeanLogin.getImg());

            ImageLoader.ImageListener lis = ImageLoader.getImageListener(
                    usericon,
                    R.mipmap.user_icon_default,
                    R.mipmap.user_icon_default);
            //设置图片
            RequestManager.getImageLoader().get(otherBeanLogin.getImg(), lis, 0, 0);

            mImageLoader = RequestManager.getImageLoader();
            mImageLoader.get(otherBeanLogin.getImg(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

                    Bitmap imageContainerBitmap = imageContainer.getBitmap();
                    if (imageContainerBitmap != null) {

                        HeadImageViewUtils.setPicToView(imageContainerBitmap, path);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });

            if (is_mobile == 1) {
                Pref_Utils.putBoolean(getActivity(), "isLogin", false);
                mNoLogin.setVisibility(View.VISIBLE);
                mLogined.setVisibility(View.GONE);
                mPswd.setText("");
                layoutTitle.setText("登录");
                startActivity(new Intent(getActivity(), PersonaldataBindTelActivity.class).putExtra("fromOherLogin", true));
            } else {
                layoutTitle.setText("我的");
//            YYJXApplication.isLogin = true;
                Pref_Utils.putBoolean(getActivity(), "isLogin", true);
                mAcc.setText("");
                mPswd.setText("");

                mLogined.setVisibility(View.VISIBLE);
                mNoLogin.setVisibility(View.GONE);
                ShareMethodUtils.start(getContext());
                ShareMethodUtils.login_qq(handler, false);
                ShareMethodUtils.login_weixin(handler, false);
            }

        } else {
//            YYJXApplication.isLogin = false;
            Pref_Utils.putBoolean(getActivity(), "isLogin", false);
            mNoLogin.setVisibility(View.VISIBLE);
            mLogined.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "账号或密码输入错误,请重新输入!", Toast.LENGTH_SHORT).show();
            mPswd.setText("");
            layoutTitle.setText("登录");
        }

    }

    @Override
    public void requestOnOtherLoginFailed(VolleyError error) {
        layoutTitle.setText("登录");
//        YYJXApplication.isLogin = false;
        Pref_Utils.putBoolean(getActivity(), "isLogin", false);
    }


    @Override
    public void onStart() {
        super.onStart();
        scrollView.scrollTo(0, 0);
    }

    //初始化view
    private void findAllView() {
        View openView = mView.findViewById(R.id.ll_open_login);
        if (open_login) {
            openView.setVisibility(View.VISIBLE);
        } else {
            openView.setVisibility(View.GONE);
        }

        scrollView = (ScrollView) mView.findViewById(R.id.scrollView);

        layoutTitle = (TextView) mView.findViewById(R.id.titlelayout);

        money = (TextView) mView.findViewById(R.id.money);
        username = (TextView) mView.findViewById(R.id.username);
        usericon = (CircleImageView) mView.findViewById(R.id.btn_usericon);

        mFirends = (RelativeLayout) mView.findViewById(R.id.firends);
        mFirends.setOnClickListener(this);

        //注册的按钮
        register = (Button) mView.findViewById(R.id.btn_phone_register);
        register.setOnClickListener(this);

        //未登录的布局
        mNoLogin = (RelativeLayout) mView.findViewById(R.id.layout_nologin);
        //已经登录的布局
        mLogined = (RelativeLayout) mView.findViewById(R.id.layout_logined);
//        if (YYJXApplication.isLogin == true) {
        if (Pref_Utils.getBoolean(getActivity(), "isLogin")) {
            mLogined.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);
            layoutTitle.setText("我的");
            //如果已经登录过  就直接读取xml文件信息 设置
            setLoginData();
        } else {
            layoutTitle.setText("登录");
            mNoLogin.setVisibility(View.VISIBLE);
            mLogined.setVisibility(View.GONE);
        }

        //默认登录状态为false
        loginRequest = new LoginRequest();
        //设置按钮
        mSet_click = (RelativeLayout) mView.findViewById(R.id.set_click);
        mSet_click.setOnClickListener(this);

        //忘记密码的按钮
        forget_pswd = (TextView) mView.findViewById(R.id.forget_pswd);
        forget_pswd.setOnClickListener(this);

        //找到账号和密码的输入框
        mAcc = (EmailAutoCompleteTextView) mView.findViewById(R.id.editText_acc);
        mPswd = (EditText) mView.findViewById(R.id.editText_pswd);

        //点击登录按钮的监听器
        mLoginbtn = (Button) mView.findViewById(R.id.btn_login);
        mLoginbtn.setOnClickListener(this);

        mBtnNotify = (ImageView) mView.findViewById(R.id.btn_notifi);
        mBtnNotify.setOnClickListener(this);

        //充值按钮
        mTopUpBtn = (TextView) mView.findViewById(R.id.btn_top_up);
        mTopUpBtn.setOnClickListener(this);

        //全部夺宝记录
        mAllSnatchLog = (LinearLayout) mView.findViewById(R.id.all_snatch_log);
        mAllSnatchLog.setOnClickListener(this);

        //已揭晓
        mOverLog = (LinearLayout) mView.findViewById(R.id.overlog);
        mOverLog.setOnClickListener(this);

        //正在进行
        mIngLog = (LinearLayout) mView.findViewById(R.id.inglog);
        mIngLog.setOnClickListener(this);

        //充值记录
        mTopUpLog = (RelativeLayout) mView.findViewById(R.id.top_up_log);
        mTopUpLog.setOnClickListener(this);

        //中奖记录
        mWinLog = (RelativeLayout) mView.findViewById(R.id.win_log);
        mWinLog.setOnClickListener(this);

        //晒单
        mBaskLog = (RelativeLayout) mView.findViewById(R.id.bask_log);
        mBaskLog.setOnClickListener(this);

        mBtn_usericon = (CircleImageView) mView.findViewById(R.id.btn_usericon);
        mBtn_usericon.setOnClickListener(this);

        mView.findViewById(R.id.img_weibo).setOnClickListener(this);
        mView.findViewById(R.id.img_qq).setOnClickListener(this);
        mView.findViewById(R.id.img_weixin).setOnClickListener(this);

    }

    /**
     * 登录过去再次登录  直接设置数据
     */
    private void setLoginData() {
        //uid
        String uid = Pref_Utils.getString(getActivity(), "uid");
        //账号
        String account = Pref_Utils.getString(getActivity(), "account");
        //密码
        String password = Pref_Utils.getString(getActivity(), "password");
        //用户名
        String username = Pref_Utils.getString(getActivity(), "username");
        //邮箱
        String email = Pref_Utils.getString(getActivity(), "email");
        //电话
        String mobil = Pref_Utils.getString(getActivity(), "mobil");
        //用户头像的地址
        String heanImage = Pref_Utils.getString(getActivity(), "headImage");
        //读取余额
        String money = Pref_Utils.getString(getActivity(), "money");

        this.username.setText(username);

        String string = "余额 : " + money + " 夺宝币";
        SpannableStringBuilder ssb = new SpannableStringBuilder(string);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), string.length() - 3, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        this.money.setTextColor(getResources().getColor(R.color.color_loading));
        this.money.setText(ssb);

        //设置头像
        setIcon(heanImage);


    }

    /**
     * 设置头像
     *
     * @param heanImage 头像的url
     */
    private void setIcon(String heanImage) {
        ImageLoader.ImageListener lis = ImageLoader.getImageListener(
                usericon,
                R.mipmap.user_icon_default,
                R.mipmap.user_icon_default);
        ImageLoader.ImageContainer imageContainer = RequestManager.getImageLoader().get(heanImage, lis);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SelfBuyRecordActivity.class);
        int id = v.getId();
        switch (id) {
            //点击设置,跳转设置界面
            case R.id.set_click:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            //注册的按钮
            case R.id.btn_phone_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                BaseUtils.colseSoftKeyboard(getActivity());
                break;
            //跳转到忘记密码的activity
            case R.id.forget_pswd:
                startActivity(new Intent(getActivity(), ForgetPassWorldActivity.class));
                BaseUtils.colseSoftKeyboard(getActivity());
                break;
            //点击通知按钮
            case R.id.btn_notifi:
                intent.setClass(getActivity(), BaseWebViewActivity.class);
                intent.putExtra("url", API.NOTIFICATION_API);
                intent.putExtra("title", "通知");
                startActivity(intent);
                break;
            case R.id.firends:
                startActivity(new Intent(getActivity(), ViteFriendActivity.class));
                break;
            //登录按钮的点击事件
            case R.id.btn_login:
                acc = mAcc.getText().toString();
                pswd = mPswd.getText().toString();
                boolean isTrue = MobileUtils.isMobileNO(acc, getActivity());

                if (isTrue == false) {
                    ToastUtil.showToast(getActivity(), "请输入正确的手机号码");
                    return;
                }
                if (acc.equals("")) {
                    ToastUtil.showToast(getActivity(), "手机号码不能为空");
                    return;
                }
                if (pswd.equals("")) {
                    ToastUtil.showToast(getActivity(), "密码不能为空");
                    return;
                }
                if (pswd.length() < 6) {
                    ToastUtil.showToast(getActivity(), "密码长度不符");
                    return;
                }

                //判断输入的类型只能为字母数字和下划线
                if (!pswd.matches("[a-zA-Z0-9]*")) {
                    ToastUtil.showToast(getActivity(), "您设置的密码中包含非法字符");
                    return;
                }

                pswd = BaseUtils.getRandomString(6) + pswd + BaseUtils.getRandomString(6);

                byte[] b = pswd.getBytes();

                pswd = Base64.encodeToString(b, Base64.DEFAULT);

                //判断账号和密码是否正确
                isLoginOk(acc, pswd);
                BaseUtils.colseSoftKeyboard(getActivity());
                //充值按钮
                break;
            case R.id.btn_top_up:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
//                startActivity(new Intent(getActivity(), PayResultShowActivity.class));
                break;
            //全部夺宝记录
            case R.id.all_snatch_log:
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            //已揭晓
            case R.id.overlog:
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            //进行中
            case R.id.inglog:
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            //充值记录
            case R.id.top_up_log:
                startActivity(new Intent(getActivity(), RechargeRecordActivity.class));
                break;
            //中奖记录
            case R.id.win_log:
                startActivity(new Intent(getActivity(), WinRecordActivity.class));
                break;
            //晒单
            case R.id.bask_log:
                startActivity(new Intent(getActivity(), MyShareActivity.class));
                break;
            case R.id.btn_usericon:
                startActivity(new Intent(getActivity(), PersonaldataActivity.class));
                break;
            case R.id.img_qq:
                ToastUtil.showToast(getContext(), "授权中...");
                ShareMethodUtils.start(getContext());
                ShareMethodUtils.login_qq(handler, true);
                break;
            case R.id.img_weibo:
                break;
            case R.id.img_weixin:
                ToastUtil.showToast(getContext(), "授权中...");
                ShareMethodUtils.start(getContext());
                ShareMethodUtils.login_weixin(handler, true);
                break;
            default:
                break;
        }
    }

    //dialog的对象
    private ProgressDialog mDialog;

    /**
     * 判断账号和密码是否正确
     *
     * @param acc
     * @param pswd
     */
    private void isLoginOk(String acc, String pswd) {
        showDialog();
        loginRequest.requestAllLog(this, acc, pswd);
    }

    /**
     * dialog的显示
     */
    private void showDialog() {
        mDialog = new ProgressDialog(getActivity());
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
     */
    @Override
    public void requestAllLogSuccess(BaseObjectBean baseObjectBean) {
        if(null!=mDialog){
            mDialog.dismiss();
        }
        if (baseObjectBean.getStatus() == 1) {
            layoutTitle.setText("我的");
//            YYJXApplication.isLogin = true;
            Pref_Utils.putBoolean(getActivity(), "isLogin", true);
            mAcc.setText("");
            mPswd.setText("");
            BeanLogin beanLogin = (BeanLogin) baseObjectBean.getData();
            Pref_Utils.putString(getActivity(), "uid", beanLogin.getUid());
            Pref_Utils.putString(getActivity(), "account", acc);
            Pref_Utils.putString(getActivity(), "password", pswd);
            Pref_Utils.putString(getActivity(), "username", beanLogin.getUsername());
            Pref_Utils.putString(getActivity(), "email", beanLogin.getEmail());
            Pref_Utils.putString(getActivity(), "mobile", beanLogin.getMobile());
            Pref_Utils.putString(getActivity(), "headImage", beanLogin.getImg());
            Pref_Utils.putString(getActivity(), "money", beanLogin.getMoney());
            Pref_Utils.putString(getActivity(), "yaoqing", beanLogin.getYaoqing());
            Pref_Utils.putString(getActivity(), "rebate_total", beanLogin.getRebate_total());

            mLogined.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);

            String str = "余额 : " + beanLogin.getMoney() + " 夺宝币";
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), str.length() - 3, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            money.setTextColor(getResources().getColor(R.color.color_loading));
            money.setText(ssb);

            username.setText(beanLogin.getUsername());
            //设置图片标记
            usericon.setTag(beanLogin.getImg());

            ImageLoader.ImageListener lis = ImageLoader.getImageListener(
                    usericon,
                    R.mipmap.user_icon_default,
                    R.mipmap.user_icon_default);
            //设置图片
            RequestManager.getImageLoader().get(beanLogin.getImg(), lis, 0, 0);

            mImageLoader = RequestManager.getImageLoader();
            mImageLoader.get(beanLogin.getImg(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

                    Bitmap imageContainerBitmap = imageContainer.getBitmap();
                    if (imageContainerBitmap != null) {

                        HeadImageViewUtils.setPicToView(imageContainerBitmap, path);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
        } else {
//            YYJXApplication.isLogin = false;
            Pref_Utils.putBoolean(getActivity(), "isLogin", false);
            mNoLogin.setVisibility(View.VISIBLE);
            mLogined.setVisibility(View.GONE);
            mDialog.cancel();
            Toast.makeText(getActivity(), "账号或密码输入错误,请重新输入!", Toast.LENGTH_SHORT).show();
            mPswd.setText("");
            layoutTitle.setText("登录");
        }
    }

    /**
     * 登录失败的回调
     */
    @Override
    public void requestAllLogFailed(VolleyError error) {
        mDialog.dismiss();
        layoutTitle.setText("登录");
//        YYJXApplication.isLogin = false;
        Pref_Utils.putBoolean(getActivity(), "isLogin", false);
    }


    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myHead/";
    // private static String path = "/sdcard/myHead/";//sd路径

    private String newName = "head.jpg";

    @Override
    public void onResume() {
        //获取SD卡中图片的位置,并转换为Bitmap类型
        Bitmap bt = BitmapFactory.decodeFile(path + newName);

        if (!Pref_Utils.getBoolean(getActivity(), "isLogin")) {
            layoutTitle.setText("登录");
            mLogined.setVisibility(View.GONE);
            mNoLogin.setVisibility(View.VISIBLE);
        } else {

            if (bt != null) {
                usericon.setImageBitmap(bt);
            } else {
                //设置头像
                setIcon(Pref_Utils.getString(getActivity(), "headImage", ""));
            }

            setIcon(Pref_Utils.getString(getActivity(), "headImage", ""));
            mLogined.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);

            layoutTitle.setText("我的");
            String string = "余额 : " + Pref_Utils.getString(getActivity(), "money") + " 夺宝币";
            SpannableStringBuilder ssb = new SpannableStringBuilder(string);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), string.length() - 3, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            this.money.setTextColor(getResources().getColor(R.color.color_loading));
            this.money.setText(ssb);

            username.setText(Pref_Utils.getString(getActivity(), "username"));
        }
        initBarUtils.setSystemBar(getActivity());
        super.onResume();
    }

    @Override
    public void requestMoneyFreshSuccess(BaseObjectBean baseObjectBean) {
        isFreshMoney=false;
        if (baseObjectBean.getStatus() == 1) {
            BeanMoney beanLogin = (BeanMoney) baseObjectBean.getData();
            Pref_Utils.putString(getActivity(), "money", beanLogin.getMoney());

            String string = "余额 : " + beanLogin.getMoney() + " 夺宝币";
            SpannableStringBuilder ssb = new SpannableStringBuilder(string);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), string.length() - 3, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            this.money.setTextColor(getResources().getColor(R.color.color_loading));
            this.money.setText(ssb);
        }
    }

    @Override
    public void requestMoneyFreshFailed(VolleyError error) {
        isFreshMoney=false;
    }


    class TempmoneyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (ConstantValues.PAY_SUCCESS.equals(intent.getAction())) {
                    String tempmoney = Pref_Utils.getString(getContext(), "tempmoney", "0")==null? "0": Pref_Utils.getString(getContext(), "tempmoney", "0");
                    Double double_temp = Double.parseDouble(tempmoney);
                    String remainMoney = Pref_Utils.getString(getContext(), "money", "0");
                    Double double_remain = Double.parseDouble(remainMoney);
                    Double money_local = double_temp + double_remain;
                    Pref_Utils.putString(getContext(), "money", String.valueOf(money_local));


                    String string = "余额 : " + Pref_Utils.getString(getActivity(), "money") + " 夺宝币";
                    SpannableStringBuilder ssb = new SpannableStringBuilder(string);
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray_shen)), string.length() - 3, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    money.setTextColor(getResources().getColor(R.color.color_loading));
                    money.setText(ssb);

                    Pref_Utils.putString(getContext(), "tempmoney", "0");


                    System.out.println(ssb + "----------------tempmoney");
                }

                if(ConstantValues.OTHERLOGIN.equals(intent.getAction())){

                    if (sso_userBean != null) {
                        handleLoginRequest(sso_userBean);
                    }
                }
            }
        }
    }
    private void getNewUserInfo(){
        String uid= Pref_Utils.getString(getActivity(), "uid");
        if(!TextUtils.isEmpty(uid)&&!isFreshMoney){
            isFreshMoney=true;
            mMoneyRequest = new MoneyRequest();
            mMoneyRequest.moneyRequest(this, uid);
        }
    }
    @Override
    public void freshDate() {
        super.freshDate();
        getNewUserInfo();
    }
}
