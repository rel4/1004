package com.hongbaogou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.RewardDetailsAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.RewardDetailBean;
import com.hongbaogou.bean.RewardDetailBeans;
import com.hongbaogou.listener.OnRewardDetailListener;
import com.hongbaogou.request.RewardDetailsRequests;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.ArrayList;
import java.util.List;
/*
*
*
* 邀请好友里的更多（奖励详情）
*
*
*/
public class InviteFriendMoreActivity extends BaseAppCompatActivity implements NetErrorView.OnReloadListener, OnRewardDetailListener {
    private String uid;
    private int pageNo = 1;
   // 奖励详情请求
    private RewardDetailsRequests mRequest;

    private ListView mListView;
    private Button mButton;
    private RewardDetailBean mData = new RewardDetailBean();
    private List<RewardDetailBeans> mDatas = new ArrayList<RewardDetailBeans>();
    private RewardDetailsAdapter mAdapter;

    private ImageView mImageView;
    private NetErrorView mNetErrorView;
    private PtrClassicFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend_more);
        initBarUtils.setSystemBar(this);
        initView();
        initData();
        setListener();
        initBarUtils.setSystemBar(this);
    }

    private void setListener() {
        mNetErrorView.setOnReloadListener(this);
    }

    private void initData() {
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        uid = Pref_Utils.getString(getApplicationContext(), "uid");

        mRequest = new RewardDetailsRequests();
        mRequest.rewardDetailsRequests(uid, pageNo, this);

        mAdapter = new RewardDetailsAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_share);
        mListView = (ListView) findViewById(R.id.lv_rewarddetails);
        mImageView = (ImageView) findViewById(R.id.iv_invitecode_back);
        mNetErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        mPtrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);

        mListView.setEmptyView(mNetErrorView);

        mLoadMoreListViewContainer.useDefaultFooter();
        mLoadMoreListViewContainer.setAutoLoadMore(true);
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadMoreContainer.loadMoreFinish(true, true);
                mAdapter.deletData(mDatas);
                mRequest.rewardDetailsRequests(uid, pageNo, InviteFriendMoreActivity.this);
            }
        });

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = mListView.getChildCount() > 0 && (mListView.getFirstVisiblePosition() > 0 || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
                } else {
                    flag = mListView.canScrollVertically(-1);
                }
                return !flag;
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        mPtrFrameLayout.refreshComplete();
                        mAdapter.deletData(mDatas);
                        mLoadMoreListViewContainer.loadMoreFinish(false, false);
                        mRequest.rewardDetailsRequests(uid, pageNo, InviteFriendMoreActivity.this);

                    }
                }, 10);
            }
        });
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_invitecode_back:
                    finish();
                    break;
                case R.id.btn_share:
                    //TODO 分享
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onReload() {
        mAdapter.deletData(mDatas);
        mRequest.rewardDetailsRequests(uid, pageNo, InviteFriendMoreActivity.this);
    }

    @Override
    public void OnRewardDetailListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                mData = (RewardDetailBean) baseObjectBean.getData();

                if (Integer.parseInt(mData.getTotal()) != 0) {
                    mDatas = mData.getList();
                    if (mDatas.size() < 10) {
                        mLoadMoreListViewContainer.loadMoreFinish(false, false);
                        mAdapter.addData(mDatas);
                    } else {
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
                        pageNo += 1;
                        mAdapter.addData(mDatas);
                    }
                }else {
                    mButton.setVisibility(View.GONE);
                    mNetErrorView.setVisibility(View.GONE);
                    ToastUtil.showToast(this, "没有数据可显示");
                }
            }

        } else {
            mNetErrorView.loadFail();
        }
    }

    @Override
    public void OnRewardDetailListenerFailed(VolleyError error) {
        mPtrFrameLayout.refreshComplete();
        mNetErrorView.loadFail();
    }
}
