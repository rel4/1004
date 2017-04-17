package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.PayRecodesAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.PayRecodersBean;
import com.hongbaogou.listener.OnPayRecodersListListener;
import com.hongbaogou.request.PayRecodersRequests;
import com.hongbaogou.utils.Pref_Utils;
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

/**
 * 充值记录的界面
 */
public class RechargeRecordActivity extends BaseAppCompatActivity implements OnPayRecodersListListener, NetErrorView.OnReloadListener {
    private Button mButton;
    private TextView mTextView,mTv_recharge_no;
    private ListView mListView;
    private ImageView mImageView;
    private NetErrorView mNetErrorView;
    private PtrClassicFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;

    private PayRecodesAdapter mAdapter;
    private PayRecodersRequests mPayRecodersRequests;
    private List<PayRecodersBean> mData = new ArrayList<PayRecodersBean>();

    private String uid;
    private int pageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
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

        mButton.setText(R.string.recharge);
        mTextView.setText(R.string.rechargerecodes);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);

        mPayRecodersRequests = new PayRecodersRequests();
        mPayRecodersRequests.payRecodersRequest(uid, pageNo, this);
    }

    private void initView() {
        uid = Pref_Utils.getString(getApplicationContext(), "uid");

        mButton = (Button) findViewById(R.id.titleright);
        mTextView = (TextView) findViewById(R.id.tv_recharge);
        mTv_recharge_no = (TextView) findViewById(R.id.tv_recharge_no);
        mListView = (ListView) findViewById(R.id.lv_payrecodes);
        mNetErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        mImageView = (ImageView) findViewById(R.id.iv_recharge_recodes);
        mPtrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);

        mAdapter = new PayRecodesAdapter(RechargeRecordActivity.this, mData);

        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setEmptyView(mNetErrorView);

        mLoadMoreListViewContainer.useDefaultFooter();
        mLoadMoreListViewContainer.setAutoLoadMore(true);
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadMoreContainer.loadMoreFinish(true, true);

                mPayRecodersRequests.payRecodersRequest(uid, pageNo, RechargeRecordActivity.this);
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
                    }
                }, 10);
            }
        });
    }

    //控件点击事件
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_recharge_recodes:
                    finish();
                    break;
                case R.id.titleright:
                    Intent intent = new Intent(RechargeRecordActivity.this, RechargeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    //充值数据的request
    public void OnPayRecodersListListenerSuccess(BaseListBean baseListBean) {
        mPtrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 1) {
            if (baseListBean.getData().size() < 10) {
                mLoadMoreListViewContainer.loadMoreFinish(false, false);
                mAdapter.addData(baseListBean.getData());
            } else {
                mLoadMoreListViewContainer.loadMoreFinish(false, true);
                pageNo++;
                mAdapter.addData(baseListBean.getData());
            }
        } else {
            mTv_recharge_no.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnPayRecodersListListenerFailed(VolleyError error) {
        mPtrFrameLayout.refreshComplete();
        mNetErrorView.loadFail();

    }

    //重新加载数据
    public void onReload() {
        mData.clear();
        mPayRecodersRequests.payRecodersRequest(uid, pageNo, this);
    }
}
