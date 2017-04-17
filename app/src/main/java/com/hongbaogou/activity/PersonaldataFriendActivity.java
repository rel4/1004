package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.PersonalDataFriendRewardDetailsAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.PersonalDataFriendBean;
import com.hongbaogou.bean.PersonalDataFriendRewardBean;
import com.hongbaogou.bean.PersonalDataFriendsBean;
import com.hongbaogou.bean.PersonalDataScaleOneBean;
import com.hongbaogou.bean.PersonalDataScaleThreeBean;
import com.hongbaogou.bean.PersonalDataScaleTwoBean;
import com.hongbaogou.listener.OnPersonalDataFriendListener;
import com.hongbaogou.request.PersonalDataFriendRequests;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;

import java.util.ArrayList;
import java.util.List;

/*
 邀请好友
  */
public class PersonaldataFriendActivity extends BaseAppCompatActivity implements OnPersonalDataFriendListener, NetErrorView.OnReloadListener {
    private String uid;
    private Button mButton;
    private ImageView mIv_invitecode_back,mIv_refresh;
    private ListView mListview_friend;
    private RelativeLayout mRl_friendrebate, mRl_friendmore,mRl_friend_question;
    private TextView mTv_invitefriends_more, mTv_friend_totalscore, mTv_friend_levelone, mTv_friend_leveltwo, mTv_friend_levelthree,
            mTv_friend_score_levelone, mTv_friend_score_leveltwo, mTv_friend_score_levelthree,mTextView_show;

    private PersonalDataFriendRequests mRequest;

    private PersonalDataFriendsBean mPersonalDataFriendsBean;
    private static List<PersonalDataFriendRewardBean> mData;
    private PersonalDataFriendBean mPersonalDataFriendBean;
    private PersonalDataScaleOneBean mPersonalDataScaleOneBean;
    private PersonalDataScaleTwoBean mPersonalDataScaleTwoBean;
    private PersonalDataScaleThreeBean mPersonalDataScaleThreeBean;
    private PersonalDataFriendRewardDetailsAdapter mAdapter;

    private NetErrorView netErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_friend);
        initBarUtils.setSystemBar(this);
        initView();
        initData();

    }

    private void initData() {

        mPersonalDataFriendsBean = new PersonalDataFriendsBean();

        mData = new ArrayList<PersonalDataFriendRewardBean>();
        mAdapter = new PersonalDataFriendRewardDetailsAdapter(this, mData);
        mListview_friend.setAdapter(mAdapter);

        uid = Pref_Utils.getString(getApplicationContext(), "uid");
        mRequest = new PersonalDataFriendRequests();
        mRequest.personalDataFriendRequests(uid, this);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_share);

        mListview_friend = (ListView) findViewById(R.id.listview_friend);

        mIv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        mIv_invitecode_back = (ImageView) findViewById(R.id.iv_invitecode_back);

        mTv_invitefriends_more = (TextView) findViewById(R.id.tv_invitefriends_more);
        mTextView_show = (TextView) findViewById(R.id.textView_show);

        mRl_friendrebate = (RelativeLayout) findViewById(R.id.rl_friendrebate);
        mRl_friendmore = (RelativeLayout) findViewById(R.id.rl_friendmore);
        mRl_friend_question = (RelativeLayout) findViewById(R.id.rl_friend_question);

        //总积分
        mTv_friend_totalscore = (TextView) findViewById(R.id.tv_friend_totalscore);

        //分级积分
        mTv_friend_score_levelone = (TextView) findViewById(R.id.tv_friend_score_levelone);
        mTv_friend_score_leveltwo = (TextView) findViewById(R.id.tv_friend_score_leveltwo);
        mTv_friend_score_levelthree = (TextView) findViewById(R.id.tv_friend_score_levelthree);

        //好友数
        mTv_friend_levelone = (TextView) findViewById(R.id.tv_friend_levelone);
        mTv_friend_leveltwo = (TextView) findViewById(R.id.tv_friend_leveltwo);
        mTv_friend_levelthree = (TextView) findViewById(R.id.tv_friend_levelthree);

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);

        netErrorView.setOnReloadListener(this);

    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                //回退
                case R.id.iv_invitecode_back:
                    finish();
                    break;
                //最新奖励详情 的更多
                case R.id.tv_invitefriends_more:
                    startActivity(new Intent(this, InviteFriendMoreActivity.class));
                    break;
                //分享好友
                case R.id.btn_share:
                    //todo 第三方分享
                    break;
                //好友返利
                case R.id.rl_friendrebate:
                    startActivity(new Intent(this, FriendRebateActivity.class));
                    break;
                //邀请好友攻略
                case R.id.rl_friendmore:
                    startActivity(new Intent(this, FriendStrategyActivity.class));
                    break;
                //刷新
                case R.id.iv_refresh:
                    netErrorView.setVisibility(View.VISIBLE);
                     mAdapter.deleteData(mData);
                    mRequest.personalDataFriendRequests(uid, this);
                    break;
                //累积好友充值获利的问号点击
                case R.id.rl_friend_question:
 //                   startActivity(new Intent(this, FriendQuestionActivity.class));
                    break;

                default:
                    break;
            }
        }
    }
// 邀请好友的请求成功
    @Override
    public void OnPersonalDataFriendListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            netErrorView.setVisibility(View.GONE);
            if (baseObjectBean.getStatus() == 1) {
                mPersonalDataFriendBean = (PersonalDataFriendBean) baseObjectBean.getData();

                mData = mPersonalDataFriendBean.getReward();
                if (mData.size()==0){
                    mListview_friend.setVisibility(View.GONE);
                    mTextView_show.setVisibility(View.VISIBLE);
                }else {
                    mListview_friend.setVisibility(View.VISIBLE);
                    mTextView_show.setVisibility(View.GONE);

                    mAdapter.addData(mData);
                }
                mPersonalDataFriendsBean = mPersonalDataFriendBean.getFriends();
                //一级好友
                mPersonalDataScaleOneBean = mPersonalDataFriendsBean.getOne();
                mTv_friend_levelone.setText(mPersonalDataScaleOneBean.getNum()+"");
                mTv_friend_score_levelone.setText(mPersonalDataScaleOneBean.getJifen()+"");
                //二级好友
                mPersonalDataScaleTwoBean = mPersonalDataFriendsBean.getTwo();
                mTv_friend_leveltwo.setText(mPersonalDataScaleTwoBean.getNum()+"");
                mTv_friend_score_leveltwo.setText(mPersonalDataScaleTwoBean.getJifen()+"");
                //三级好友
                mPersonalDataScaleThreeBean = mPersonalDataFriendsBean.getThree();
                mTv_friend_levelthree.setText(mPersonalDataScaleThreeBean.getNum()+"");
                mTv_friend_score_levelthree.setText(mPersonalDataScaleThreeBean.getJifen()+"");
                //总积分
                mTv_friend_totalscore.setText((mPersonalDataScaleOneBean.getJifen() + mPersonalDataScaleTwoBean.getJifen() + mPersonalDataScaleThreeBean.getJifen())+"");

            }else {
                ToastUtil.showToast(this,"暂时没有数据显示");
            }
        }else {
            netErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnPersonalDataFriendListenerFailed(VolleyError error) {

    }

    @Override
    public void onReload() {
        mRequest.personalDataFriendRequests(uid, this);
    }
}
