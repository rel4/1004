package com.hongbaogou.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.Result;
import com.hongbaogou.bean.SSO_UserBean;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ShareMethodUtils;
import com.hongbaogou.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/10/21.
 */
public class ApplyCashActivity extends BaseNetActivity implements View.OnClickListener {

    private AlertDialog alertDialog;
    private boolean isShowing;
    private TextView winxin;
    private String uid;
    private SSO_UserBean sso_userBean;
    private int flag;
   private Handler handler = new Handler() {
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what) {
               case ShareMethodUtils.SSO_LOGIN:
                   sso_userBean = (SSO_UserBean) msg.obj;
                  if(flag==1){
                      updataLogin(sso_userBean.id, sso_userBean.name);
                  }else{
                      otherLogin(sso_userBean.id,sso_userBean.name);
                  }

                   break;
               case ShareMethodUtils.ERROR:
                   ToastUtil.showToast(ApplyCashActivity.this, "授权登陆失败");
                   break;
           }
       }
   };
    private TextView shouquan;
    private TextView pop_tv_title;
    private Button btn_next_action;
    private String str1;
    private String str2;
    private TextView tv_current_money;
    private EditText et_cach_money;
    private String b_code;
    private String yongjin;
    private String nick;
    private String withdrawCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_cash);
        ImageView back=(ImageView)findViewById(R.id.iv_detai_back);
        TextView sure=(TextView)findViewById(R.id.tv_sure);
        //微信号
        winxin=(TextView)findViewById(R.id.et_winxin);
        //授权
        shouquan=(TextView)findViewById(R.id.tv_shouquan);
        //可提现的金额
        tv_current_money=(TextView)findViewById(R.id.tv_current_money);
        //输入的提现金额
        et_cach_money=(EditText)findViewById(R.id.et_cach_money);

        initData();

        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        winxin.setOnClickListener(this);
        shouquan.setOnClickListener(this);

    }

    private void initData() {
        uid= Pref_Utils.getString(this, "uid");
       //判断是否绑定微信
        request.get("shouquan","member/isBandWeiXin?","&uid=" + uid + "&type=" + "app");
    }

    @Override
    public void onSuccess(Result result) {

        switch (result.tag){
            //绑定
            case "shouquan":
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    JSONObject data = jsonObject.getJSONObject("data");
                    yongjin=data.getString("yongjin");
                    withdrawCash=data.getString("withdrawCash");
                    if(status==1){
                        nick=data.getString("nick");
                        b_code=data.getString("b_code");
                        winxin.setText(nick);
                        shouquan.setText("修改");
                      }

                    else{
                        shouquan.setText("未绑定");
                    }
                    shouquan.setVisibility(View.VISIBLE);
                    tv_current_money.setText("当前佣金:" + yongjin);
                    et_cach_money.setHint("您提现的金额不能低于"+withdrawCash);

                    ToastUtil.showToast(ApplyCashActivity.this,messeage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "login":
                //绑定微信
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        JSONObject data = jsonObject.getJSONObject("data");
                        nick=data.getString("nick");
                        winxin.setText(nick);
                        b_code=data.getString("openid");
                        shouquan.setVisibility(View.VISIBLE);
                        shouquan.setText("修改");
                    }else{
                        shouquan.setVisibility(View.VISIBLE);
                        shouquan.setText("未绑定");
                    }
                    ToastUtil.showToast(ApplyCashActivity.this,messeage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case "iscash":
                //判断是否设置提现密码
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        alertAnim();
                    }else{
                        alertAnim2();
                    }
                    ToastUtil.showToast(ApplyCashActivity.this,messeage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "cash":
                //验证提现密码
                alertDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        if("未绑定".equals(shouquan.getText().toString())){
                        //去绑定
                            flag=2;
                            ToastUtil.showToast(ApplyCashActivity.this, "授权中...");
                            ShareMethodUtils.start(this);
                            ShareMethodUtils.login_weixin(handler, true);
                        }

                        else{
                        request.get("commit","member/withdrawCach?","&uid="+ uid+"&openid="+b_code+"&nick="+nick+"&money="+et_cach_money.getText().toString());}
                    }
                    else{
                        //重新输入
                    }
                    ToastUtil.showToast(ApplyCashActivity.this,messeage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
           //修改
            case "updata":
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        flag=1;
                        alertDialog.dismiss();
                        ToastUtil.showToast(ApplyCashActivity.this, "授权中...");
                        ShareMethodUtils.start(this);
                        ShareMethodUtils.login_weixin(handler, true);

                    }else{
                        ToastUtil.showToast(ApplyCashActivity.this,messeage);

                    }
                    ToastUtil.showToast(ApplyCashActivity.this,messeage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case "cash2":
                //设置提现密码
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        flag=2;
                        alertDialog.dismiss();
                        ToastUtil.showToast(ApplyCashActivity.this, "授权中...");
                        ShareMethodUtils.start(this);
                        ShareMethodUtils.login_weixin(handler, true);

                    }else{
                        ToastUtil.showToast(ApplyCashActivity.this,messeage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "updataLogin":
                //更新微信
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    int status=jsonObject.getInt("status");
                    String messeage=jsonObject.getString("message");
                    if(status==1){
                        JSONObject data = jsonObject.getJSONObject("data");
                        nick=data.getString("nick");
                        winxin.setText(nick);
                        b_code=data.getString("openid");
                    }

                    ToastUtil.showToast(ApplyCashActivity.this,messeage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case "commit":
                try {
                    JSONObject jsonObject=new JSONObject(result.result);
                    String messeage=jsonObject.getString("message");
                    int status=jsonObject.getInt("status");
                    if(status==1){
                        String money=jsonObject.getString("money");
                        tv_current_money.setText("当前佣金:"+money);

                    }
                    ToastUtil.showToast(ApplyCashActivity.this,messeage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

        }

    }


    @Override
    public void onError(Result result) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_detai_back:
                finish();
                break;

            case R.id.tv_sure:
                //提现金额
                if(TextUtils.isEmpty(winxin.getText().toString())){
                    ToastUtil.showToast(this,"微信号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_cach_money.getText().toString())){
                    ToastUtil.showToast(this,"提现金额不能为空");
                    return;
                }

                if(Float.valueOf(et_cach_money.getText().toString())>Float.valueOf(yongjin)){
                    ToastUtil.showToast(this,"提现金额不足");
                    return;
                }

                alertAnim();
                break;

            case R.id.tv_shouquan:
                if("修改".equals(shouquan.getText().toString())){
                    alertAnim1();
                }
                if("未绑定".equals(shouquan.getText().toString())){
                    request.get("iscash", "member/withDrawalsPwd?", "&uid=" + uid);
                }
                break;


        }
    }

    private void otherLogin(String id,String nick) {
        //绑定微信
        request.get("login", "member/setBandInfo?", "&uid="+ uid+"&openid="+id+"&nick="+nick);
    }


    private void updataLogin(String id,String nick) {
        //更新微信
        request.get("updataLogin", "member/updateBandInfo?", "&uid="+ uid+"&openid="+id+"&nick="+nick);
    }

    private void alertAnim() {


        if (isShowing) {
            return;
        }
        isShowing = true;

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
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

        View pop_iv_back = layout.findViewById(R.id.pop_iv_back);
        pop_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        TextView pop_tv_title = (TextView) layout.findViewById(R.id.pop_tv_title);
        pop_tv_title.setText("输入提现密码");

        final EditText edt_input_password = (EditText) layout.findViewById(R.id.edt_input_password);

        Button btn_next_action = (Button) layout.findViewById(R.id.btn_next_action);
        btn_next_action.setText("确定");

        if (edt_input_password.getText().toString().trim().length() > 0) {
            btn_next_action.setClickable(true);
        } else {
            btn_next_action.setClickable(false);
        }
        btn_next_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_input_password.getText().toString())) {
                    ToastUtil.showToast(ApplyCashActivity.this, "密码不能为空");
                    return;

                }if(edt_input_password.getText().toString().trim().length()<6){
                    ToastUtil.showToast(ApplyCashActivity.this,"密码不能小于六位");
                    return;
                }else{
                    String pwd=edt_input_password.getText().toString();
                    request.get("cash","member/checkWithdrawPwd?","&uid="+ uid+"&pwd="+pwd);
                }
            }
        });
    }


  //修改要弹的框
    private void alertAnim1() {


        if (isShowing) {
            return;
        }
        isShowing = true;

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
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

        View pop_iv_back = layout.findViewById(R.id.pop_iv_back);
        pop_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        TextView pop_tv_title = (TextView) layout.findViewById(R.id.pop_tv_title);
        pop_tv_title.setText("输入提现密码");

        final EditText edt_input_password = (EditText) layout.findViewById(R.id.edt_input_password);

        Button btn_next_action = (Button) layout.findViewById(R.id.btn_next_action);
        btn_next_action.setText("确定");

        if (edt_input_password.getText().toString().trim().length() > 0) {
            btn_next_action.setClickable(true);
        } else {
            btn_next_action.setClickable(false);
        }
        btn_next_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_input_password.getText().toString())) {
                    ToastUtil.showToast(ApplyCashActivity.this, "密码不能为空");
                    return;

                }if(edt_input_password.getText().toString().trim().length()<6){
                    ToastUtil.showToast(ApplyCashActivity.this,"密码不能小于六位");
                    return;
                }else{
                    String pwd=edt_input_password.getText().toString();
                    request.get("updata","member/checkWithdrawPwd?","&uid="+ uid+"&pwd="+pwd);
                }
            }
        });
    }






    private void alertAnim2() {

        if (isShowing) {
            return;
        }
        isShowing = true;

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
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

        View pop_iv_back = layout.findViewById(R.id.pop_iv_back);
        pop_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        pop_tv_title = (TextView) layout.findViewById(R.id.pop_tv_title);
        pop_tv_title.setText("设置提现密码");

        final EditText edt_input_password = (EditText) layout.findViewById(R.id.edt_input_password);

        btn_next_action = (Button) layout.findViewById(R.id.btn_next_action);
        btn_next_action.setText("下一步");


        btn_next_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("下一步".equals(btn_next_action.getText().toString())){
                    if (TextUtils.isEmpty(edt_input_password.getText().toString())) {
                        ToastUtil.showToast(ApplyCashActivity.this,"密码不能为空");
                        return;
                    }if(edt_input_password.getText().toString().trim().length()<6){
                        ToastUtil.showToast(ApplyCashActivity.this,"密码不能小于六位");
                        return;
                    }
                    else {
                        str1=edt_input_password.getText().toString();
                        pop_tv_title.setText("确认输入密码");
                        btn_next_action.setText("确定");
                        edt_input_password.setText("");
                    }

                }

                if("确定".equals(btn_next_action.getText().toString())){

                    if (TextUtils.isEmpty(edt_input_password.getText().toString())) {
                        ToastUtil.showToast(ApplyCashActivity.this, "密码不能为空");
                        return;

                    }if(edt_input_password.getText().toString().trim().length()<6){
                        ToastUtil.showToast(ApplyCashActivity.this,"密码不能小于六位");
                        return;
                    }

                    else {
                         str2=edt_input_password.getText().toString();
                         if(!str1.equals(str2)){
                            ToastUtil.showToast(ApplyCashActivity.this, "两次输入不一致");
                            return;
                        }else{
                            request.get("cash2","member/setWithDrawalsPwd?","&uid="+ uid+"&pwd="+str2);
                        }
                    }


                }

            }
        });

    }











}
