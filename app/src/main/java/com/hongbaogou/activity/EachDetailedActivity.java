package com.hongbaogou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hongbaogou.R;
import com.hongbaogou.adapter.ViteFriendAdapter;
import com.hongbaogou.bean.Result;
import com.hongbaogou.bean.ViteFriendBean;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 201610/21.
 */
public class EachDetailedActivity extends BaseNetActivity {

    private int pageNo=1;
    private int pageNum=10;
    private ViteFriendBean viteFriendBean;

    private List<ViteFriendBean.DataBean> list1=new ArrayList<ViteFriendBean.DataBean>();
    private ViteFriendAdapter adapter;
    private LoadMoreListView listView;
    private String uid;
    private TextView nullText;
    private String url;
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.each_detail);

        initView();
    }

    @Override
    public void onSuccess(Result result) {

        switch (result.tag){
            case "friend":
               parseSuccess(result,"friend");
                break;

            case "comm":
                parseSuccess(result,"comm");
                break;

            case "cash":
                parseSuccess(result,"cash");
                break;


        }

    }

    @Override
    public void onError(Result result) {

        listView.setLoading(false);

    }


    private void parseSuccess(Result result,String tag){
        Gson gson=new Gson();
        viteFriendBean = gson.fromJson(result.result, ViteFriendBean.class);
        if(viteFriendBean.getStatus()==1) {
            List<ViteFriendBean.DataBean> data = viteFriendBean.getData();
            if (data != null && data.size() > 0){
                nullText.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                if (pageNo == Integer.parseInt(viteFriendBean.getMaxPage())) {
                    listView.setLoading(false);

                } else {
                    pageNo = pageNo + 1;
                    listView.setLoading(true);
                }
                adapter.addData(data,tag);

            }else{
                listView.setLoading(false);

            }

        }else{
            listView.setLoading(false);
            listView.setVisibility(View.GONE);
            nullText.setVisibility(View.VISIBLE);
        }
    }


    private void initView() {
        uid= Pref_Utils.getString(this, "uid");
        listView=(LoadMoreListView)findViewById(R.id.lv_detail_record);
        //提现记录
        LinearLayout llCashRecord=(LinearLayout)findViewById(R.id.ll_cash_record);
        //佣金记录
        LinearLayout llCommRecord=(LinearLayout)findViewById(R.id.ll_comm_record);
        //邀请好友记录
        LinearLayout llViteFriendRecord=(LinearLayout)findViewById(R.id.ll_vitefriend_record);
        TextView tvRecharge=(TextView)findViewById(R.id.tv_recharge);
        findViewById(R.id.iv_detai_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nullText=(TextView)findViewById(R.id.tv_null_text);

        listView.setOnLoadMoreListener(new LoadMoreListView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
               request.get(tag, url, "&uid=" + uid + "&page=" + pageNo + "&size=" + pageNum);
            }
        });


        adapter=new ViteFriendAdapter(this);
        listView.setAdapter(adapter);
        tag=getIntent().getStringExtra("tag");
        if("friend".equals(tag)){
            llCashRecord.setVisibility(View.GONE);
            llCommRecord.setVisibility(View.GONE);
            llViteFriendRecord.setVisibility(View.VISIBLE);
            tvRecharge.setText("邀请好友明细");
            nullText.setText("暂无邀请好友明细，赶紧去邀请好友吧~");
            url="member/getFriends?";
            request.get(tag, url, "&uid=" + uid + "&page=" + pageNo + "&size=" + pageNum);

        }else if("comm".equals(tag)){
            llCashRecord.setVisibility(View.GONE);
            llCommRecord.setVisibility(View.VISIBLE);
            llViteFriendRecord.setVisibility(View.GONE);
            tvRecharge.setText("佣金明细");
            nullText.setText("暂无佣金明细，赶紧去邀请好友吧~");
            url="member/getCommission?";
            request.get(tag,url, "&uid=" + uid + "&page=" + pageNo + "&size=" + pageNum);



        }else {
            llCashRecord.setVisibility(View.VISIBLE);
            llCommRecord.setVisibility(View.GONE);
            llViteFriendRecord.setVisibility(View.GONE);
            tvRecharge.setText("提现记录");
            nullText.setText("暂无提现记录");
            url="member/getWithdrawRecord?";
            request.get(tag, url, "&uid=" + uid + "&page=" + pageNo + "&size=" + pageNum);

        }
    }




}
