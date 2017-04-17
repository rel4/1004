/*
package com.hongbaogou.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.xwinfo.globalproduct.BaseActivity;
import com.xwinfo.globalproduct.R;
import com.xwinfo.globalproduct.global.ConstantValues;
import com.xwinfo.globalproduct.utils.Json_U;
import com.xwinfo.globalproduct.utils.PlatformLoginUtils;
import com.xwinfo.globalproduct.utils.SPUtils;
import com.xwinfo.globalproduct.utils.SendUtil;
import com.xwinfo.globalproduct.utils.ToastUtils;
import com.xwinfo.globalproduct.vo.Mine_Order_Param;
import com.xwinfo.globalproduct.widget.ProgressDialog;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

*/
/**
 * Created by ${Jackie} on 2016/4/7.
 *//*

public class WXWalletSettingActivity extends BaseActivity implements View.OnClickListener {

    private View mBackIcon;
    private TextView mTitleText;
    private TextView tv_setting;
    private boolean isShowing = false;
    private TextView tv_click2authorization;
    private EditText edt_input_password;
    private String firstPassWord;
    private String secondPassWord;
    private View pop_iv_back;
    private AlertDialog alertDialog;
    private TextView pop_tv_title;
    private Button btn_next_action;
    private ProgressDialog progressDialog;
    private PlatformLoginUtils platformLoginUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxwallet_setting);
        progressDialog = new com.xwinfo.globalproduct.widget.ProgressDialog(this);
        platformLoginUtils = new PlatformLoginUtils(this);

        ifPassword();

        initTitleBar();
        initView();
//        alertAnim();


    }

    public void ifPassword() {
        progressDialog.show();
        String user_id = SPUtils.getString(this, "user_id", "");
        final String store_id = SPUtils.getString(this, "store_id", "");
        final String json = " {\"user_id\":\"" + user_id + "\",\"store_id\":\"" + store_id + "\"}";

        SendUtil sendUtil = new SendUtil(json);
        sendUtil.send(ConstantValues.STORE_DEF_BASE_URL + "Wechat/ifPassword", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("///////判断是否设置过提现密码///////" + responseInfo.result);
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int status = object.getInt("status");
                    if (status == 1) {
                        SPUtils.saveBoolean(WXWalletSettingActivity.this, "isPasswordSetSuccessed", true);//// TODO: 2016/5/17 每次都进行验证是否设置过密码
                        alertAnim();
                        mBackIcon.setVisibility(View.VISIBLE);
                        if (SPUtils.getBoolean(WXWalletSettingActivity.this, "WX_SETTED", false)) {
                            pop_tv_title.setText("请输入提现密码");
                        } else {
                            pop_tv_title.setText("确认提现密码");
                        }
                        btn_next_action.setText("确定");
                        btn_next_action.setClickable(true);

                    } else {
                        SPUtils.saveBoolean(WXWalletSettingActivity.this, "isPasswordSetSuccessed", false);//// TODO: 2016/5/17 每次都进行验证是否设置过密码
                        alertAnim();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
            }
        });
    }


    private void initTitleBar() {
        mBackIcon = findViewById(R.id.iv_back_black);
        mBackIcon.setVisibility(View.VISIBLE);
        mBackIcon.setOnClickListener(this);
        mTitleText = (TextView) findViewById(R.id.title_tv);
        mTitleText.setText("设置微信钱包");
        tv_setting = ((TextView) findViewById(R.id.tv_setting));
        tv_setting.setText("修改");
        tv_setting.setOnClickListener(this);
    }

    private void initView() {


        tv_click2authorization = ((TextView) findViewById(R.id.tv_click2authorization));
        tv_click2authorization.setOnClickListener(this);

        if (SPUtils.getBoolean(WXWalletSettingActivity.this, "WX_SETTED", false)) {
            String wx_nickname = SPUtils.getString(WXWalletSettingActivity.this, "wx_nickname", null);
            tv_setting.setVisibility(View.VISIBLE);
            tv_click2authorization.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tv_click2authorization.setTextColor(getResources().getColor(R.color.mine_text_color2));
            tv_click2authorization.setText(wx_nickname);
            tv_click2authorization.setClickable(false);
        }

    }


    private void alertAnim() {

        if (isShowing) {
            return;
        }
        isShowing = true;

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
//        alertDialog.setCancelable(false);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowing = false;
            }
        });

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowing = false;
            }
        });

        //设置params.如果设置全屏需要使用此设置,如果不需要,则不需要设置
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(attributes);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //设置动画
        alertDialog.getWindow().setWindowAnimations(R.style.dialog_animation_shopfind);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.set_withdraw_popwindow, null);
        alertDialog.setContentView(layout);

        pop_iv_back = layout.findViewById(R.id.pop_iv_back);
        pop_iv_back.setOnClickListener(this);

        pop_tv_title = (TextView) layout.findViewById(R.id.pop_tv_title);

        edt_input_password = (EditText) layout.findViewById(R.id.edt_input_password);
        edt_input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btn_next_action.getText().equals("下一步") && count > 0) {
                    btn_next_action.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputPassword = edt_input_password.getText().toString().trim();
                if (s.toString().trim().length() > 0 && btn_next_action.getText().equals("下一步")) {
                    firstPassWord = inputPassword;
                } else {
                    secondPassWord = inputPassword;
                }
            }
        });


        btn_next_action = (Button) layout.findViewById(R.id.btn_next_action);
        btn_next_action.setOnClickListener(PopClick);
        btn_next_action.setClickable(false);


//        if (SPUtils.getBoolean(WXWalletSettingActivity.this, "isPasswordSetSuccessed", false)) {//已设置过密码，直接确认就好
//            mBackIcon.setVisibility(View.INVISIBLE);
//            pop_tv_title.setText("确认提现密码");
//            btn_next_action.setText("确定");
//            btn_next_action.setClickable(true);
//        }


        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                WXWalletSettingActivity.this.finish();
            }
        });

    }

    private View.OnClickListener PopClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (btn_next_action.getText().equals("下一步")) {//第一次输入密码
                edt_input_password.setText("");
                pop_tv_title.setText("确认提现密码");
                btn_next_action.setText("确定");
                btn_next_action.setClickable(true);
            } else {
                getData(edt_input_password.getText().toString().trim());//确认密码
            }


        }
    };


    public void getData(String password) {//设置密码接口与密码验证接口
        progressDialog.show();
        String user_id = SPUtils.getString(this, "user_id", "");
        final String store_id = SPUtils.getString(this, "store_id", "");
        final String json = " {\"user_id\":\"" + user_id + "\",\"store_id\":\"" + store_id + "\",\"password\":\"" + password + "\"}";
        System.out.println("222222222222222" + json);
        SendUtil sendUtil = new SendUtil(json);

        String passwordTest = null;
        if (!SPUtils.getBoolean(WXWalletSettingActivity.this, "isPasswordSetSuccessed", false)) {
            passwordTest = "Wechat/setWechatPassword";//设置密码
        } else {
            passwordTest = "Wechat/testWechatPassword";//密码设置成功后，验证密码
        }
        sendUtil.send(ConstantValues.STORE_DEF_BASE_URL + passwordTest, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("//////////////" + responseInfo.result);
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int status = object.getInt("status");
                    if (status == 1) {
                        alertDialog.dismiss();
                        hideKeyboard();
                        SPUtils.saveBoolean(WXWalletSettingActivity.this, "isPasswordSetSuccessed", true);
                    } else {
                        ToastUtils.showToast("密码错误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_black:
                hideKeyboard();
                finish();
                break;

            case R.id.tv_click2authorization:
                impower(SHARE_MEDIA.WEIXIN, 0);
                break;

            case R.id.pop_iv_back:
                alertDialog.dismiss();
                hideKeyboard();
                finish();
                break;

            case R.id.tv_setting://修改微信提现账号
                impower(SHARE_MEDIA.WEIXIN, 1);
                break;
        }

    }


    private void impower(SHARE_MEDIA media, final int size) {
        progressDialog.show();
        platformLoginUtils.login(media, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtils.showToast("开始授权");
            }

            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                ToastUtils.showToast("授权完成");
                progressDialog.dismiss();
                final String uid = bundle.getString("uid");
                if (TextUtils.isEmpty(uid)) {
                    ToastUtils.showToast("uid获取失败");
                    return;
                }

                UMSocialService mController = UMServiceFactory.getUMSocialService(ConstantValues.DESCRIPTOR_LOGIN);
                mController.getPlatformInfo(WXWalletSettingActivity.this, share_media, new SocializeListeners.UMDataListener() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == StatusCode.ST_CODE_SUCCESSED) {
                            String nickname = (String) info.get("nickname");
                            if (size == 0) {
                                onceWithDraw(uid, nickname);
                            } else if (size == 1) {
                                replaceWithDrawNum(uid, nickname);
                            }
                        }
                    }
                });

            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                ToastUtils.showToast("授权错误");
                progressDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showToast("授权取消");
                progressDialog.dismiss();
            }
        });

    }


    public void onceWithDraw(final String uid, final String wx_nickname) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");
        Mine_Order_Param mine_order_param = new Mine_Order_Param();
        mine_order_param.setUser_id(SPUtils.getString(WXWalletSettingActivity.this, "user_id", ""));
        mine_order_param.setStore_id(SPUtils.getString(WXWalletSettingActivity.this, "store_id", ""));
        mine_order_param.setOpenid(uid);//将固定的请求body字段值赋给实体对象
        mine_order_param.setNickname(wx_nickname);//将固定的请求body字段值赋给实体对象
        String jsonStr = Json_U.toJson(mine_order_param);//将已经赋值的实体类对象转换为json字符串

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonStr, "UTF-8");//构建json字符串的body实体
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.setBodyEntity(entity);//设置进请求参数的body中
        utils.send(HttpRequest.HttpMethod.POST, ConstantValues.STORE_DEF_BASE_URL + "Wechat/OnceWechat", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    int status = obj.getInt("status");
                    if (status == 1) {
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", wx_nickname);
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_openid", uid);//// TODO: 2016/4/29
                        SPUtils.saveBoolean(WXWalletSettingActivity.this, "WX_SETTED", true);


                        tv_setting.setVisibility(View.VISIBLE);
                        tv_click2authorization.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        tv_click2authorization.setTextColor(getResources().getColor(R.color.mine_text_color2));
                        tv_click2authorization.setText(wx_nickname);
                        tv_click2authorization.setClickable(false);

                    }else {
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", "");
                    }
                } catch (Exception e) {
                    SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", "");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }
        });

    }


    public void replaceWithDrawNum(final String uid, final String wx_nickname) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");
        Mine_Order_Param mine_order_param = new Mine_Order_Param();
        mine_order_param.setUser_id(SPUtils.getString(WXWalletSettingActivity.this, "user_id", ""));
        mine_order_param.setStore_id(SPUtils.getString(WXWalletSettingActivity.this, "store_id", ""));
        mine_order_param.setNickname(wx_nickname);
        mine_order_param.setOpenid(uid);//将固定的请求body字段值赋给实体对象
        String jsonStr = Json_U.toJson(mine_order_param);//将已经赋值的实体类对象转换为json字符串
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonStr, "UTF-8");//构建json字符串的body实体
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.setBodyEntity(entity);//设置进请求参数的body中
        utils.send(HttpRequest.HttpMethod.POST, ConstantValues.STORE_DEF_BASE_URL + "Wechat/ReplaceWechat", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    int status = obj.getInt("status");
                    if (status == 1) {
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", wx_nickname);
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_openid", uid);//// TODO: 2016/4/29
                        SPUtils.saveBoolean(WXWalletSettingActivity.this, "WX_SETTED", true);


                        tv_setting.setVisibility(View.VISIBLE);
                        tv_click2authorization.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        tv_click2authorization.setTextColor(getResources().getColor(R.color.mine_text_color2));
                        tv_click2authorization.setText(wx_nickname);
                        tv_click2authorization.setClickable(false);

                    }else {
                        SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SPUtils.saveString(WXWalletSettingActivity.this, "wx_nickname", "");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }
        });
    }


    */
/**
     * 隐藏软键盘
     *//*


    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
*/
