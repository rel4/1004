package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hongbaogou.R;
import com.hongbaogou.bean.Result;
import com.hongbaogou.bean.SanjiBean;
import com.hongbaogou.utils.DialogShareMenu;
import com.hongbaogou.utils.Pref_Utils;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by admin on 2016/10/19.
 */
public class ViteFriendActivity extends BaseNetActivity implements View.OnClickListener{

    private TextView tv_VistFriend;
    private TextView tv_TotalMoney;
    private String shareUrl;
    private String title;
    private String img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitefriend);
        initView();
        initData();
        ShareSDK.initSDK(this);

    }


    private void initView(){

        //返回键
        ImageView back=(ImageView)findViewById(R.id.iv_winrecodessingin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout vite_FriendLog=(RelativeLayout)findViewById(R.id.vite_friedn_log);
        RelativeLayout comm_RebateLog=(RelativeLayout)findViewById(R.id.comm_rebate_log);
        RelativeLayout comm_cashLog=(RelativeLayout)findViewById(R.id.comm_cash_log);
        TextView tv_NowMoney=(TextView)findViewById(R.id.tv_now_money);
        TextView tv_ApplyMoney=(TextView)findViewById(R.id.tv_apply_money);
        tv_VistFriend=(TextView)findViewById(R.id.tv_vist_friend);
        tv_TotalMoney=(TextView)findViewById(R.id.tv_total_money);

        vite_FriendLog.setOnClickListener(this);
        comm_RebateLog.setOnClickListener(this);
        comm_cashLog.setOnClickListener(this);
        //立即赚钱
        tv_NowMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogShareMenu dsm = new DialogShareMenu(ViteFriendActivity.this,"",img, title, "",shareUrl,1);

            }
        });

        //申请提现
       tv_ApplyMoney.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent();
               intent.setClass(ViteFriendActivity.this, ApplyCashActivity.class);
               startActivity(intent);
           }
       });

    }

    private void initData() {
        String uid=Pref_Utils.getString(this,"uid");
        request.get("vite","member/getShareInfo?","&uid="+uid);
    }

    @Override
    public void onSuccess(Result result) {
        switch (result.tag){
            case "vite":
                Gson gson=new Gson();
                SanjiBean sanjiBean = gson.fromJson(result.result, SanjiBean.class);
                if(sanjiBean.getStatus()==1){
                    if(sanjiBean.getData()!=null){
                        String count = sanjiBean.getData().getCount();
                        String yongjin = sanjiBean.getData().getYongjin();
                        shareUrl = sanjiBean.getData().getShareUrl();
                        title = sanjiBean.getData().getTitle();
                        img = sanjiBean.getData().getImg();
                        tv_VistFriend.setText(count);
                        tv_TotalMoney.setText(yongjin);
                    }
                }

                break;
        }

    }

    @Override
    public void onError(Result result) {

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setClass(this, EachDetailedActivity.class);
        switch (v.getId()){
            case R.id.vite_friedn_log:
                intent.putExtra("tag", "friend");
                break;
            case R.id.comm_rebate_log:
                intent.putExtra("tag", "comm");
                break;
            case R.id.comm_cash_log:
                intent.putExtra("tag", "cash");
                break;

        }
        startActivity(intent);
    }
}
