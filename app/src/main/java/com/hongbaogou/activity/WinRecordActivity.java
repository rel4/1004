package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.GuessYouLikeAdapter;
import com.hongbaogou.adapter.GuessYouLikeListViewAdapter;
import com.hongbaogou.adapter.WinRecordAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.GuessYouLikeBean;
import com.hongbaogou.bean.WinRecodersBean;
import com.hongbaogou.listener.OnGuessYouLikeListListener;
import com.hongbaogou.listener.OnWinRecodersListListener;
import com.hongbaogou.request.GuessYouLikeRequests;
import com.hongbaogou.request.WinRecordsRequests;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.horizontallistview.HorizontalListView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录的界面
 */
public class WinRecordActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener, NetErrorView.OnReloadListener, OnGuessYouLikeListListener, OnWinRecodersListListener {
    private Button mButton;
    private ListView mListView, lv_winrecord;
    private ImageView mImageView;
    private NetErrorView mNetErrorView;
    private RecyclerView mRecyclerView;
    private LinearLayout mLy_winrecord_no;

    private PtrClassicFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;

    private HorizontalListView mHlv;

    private WinRecordAdapter mAdapter;
    private WinRecordsRequests winRecordsRequests;
    private List<WinRecodersBean> mData = new ArrayList<WinRecodersBean>();

    private GuessYouLikeAdapter mGuessAdapter;
    private GuessYouLikeRequests mGuessYouLikeRequests;
    private List<GuessYouLikeBean> mGuessData = new ArrayList<GuessYouLikeBean>();
    private GuessYouLikeListViewAdapter mGuessYouLikeListViewAdapter;

    private String uid;
    private int pageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_record);
        initBarUtils.setSystemBar(this);
        initView();
        initData();
        setListener();
        initBarUtils.setSystemBar(this);

    }

    private void setListener() {

        mListView.setOnItemClickListener(this);

        mNetErrorView.setOnReloadListener(this);

        mHlv.setOnItemClickListener(this);
    }

    private void initData() {

        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);

        mGuessYouLikeRequests.guessYouLikeRequests(this);

        winRecordsRequests.payRecodersRequest(uid, pageNo, this);


      //  mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
     //   mRecyclerView.setHasFixedSize(true);
      //  mRecyclerView.setAdapter(mGuessAdapter);
        //listview横向
         mGuessYouLikeListViewAdapter = new GuessYouLikeListViewAdapter(WinRecordActivity.this, mGuessData);
        // lv_winrecord.setAdapter(mGuessYouLikeListViewAdapter);
        mGuessYouLikeListViewAdapter.notifyDataSetChanged();
        mHlv.setAdapter(mGuessYouLikeListViewAdapter);

    }

    private void initView() {


        mButton = (Button) findViewById(R.id.btn_winrecord_no);
        mListView = (ListView) findViewById(R.id.lv_payrecodes);

       // lv_winrecord = (ListView) findViewById(R.id.lv_winrecord);
        mHlv = (HorizontalListView) findViewById(R.id.horizontallistview);

        mImageView = (ImageView) findViewById(R.id.iv_winrecodes);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_winrecord);
        mNetErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        mLy_winrecord_no = (LinearLayout) findViewById(R.id.ly_winrecord_no);
        mPtrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        winRecordsRequests = new WinRecordsRequests();

        mGuessYouLikeRequests = new GuessYouLikeRequests();
        mGuessAdapter = new GuessYouLikeAdapter(WinRecordActivity.this, mGuessData);

        mAdapter = new WinRecordAdapter(WinRecordActivity.this, mData);
        mListView.setAdapter(mAdapter);

        mListView.setEmptyView(mNetErrorView);

        winRecordsRequests = new WinRecordsRequests();

        uid = Pref_Utils.getString(getApplicationContext(), "uid");


        // 获取view的引用
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);
        // 使用默认样式
        mLoadMoreListViewContainer.useDefaultFooter();
        mLoadMoreListViewContainer.setAutoLoadMore(true);
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                loadMoreContainer.loadMoreFinish(true, true);

                winRecordsRequests.payRecodersRequest(uid, pageNo, WinRecordActivity.this);
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
                        mAdapter.addData(mData);
                        winRecordsRequests.payRecodersRequest(uid, pageNo, WinRecordActivity.this);
                    }
                }, 10);
            }
        });
    }

    //重新加载数据请求
    public void onReload() {
        mData.clear();
        winRecordsRequests.payRecodersRequest(uid, pageNo, this);

    }

    //中奖记录的request。
    public void OnWinRecodersListListenerSuccess(BaseListBean baseListBean) {
        mPtrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 0) {
          mLy_winrecord_no.setVisibility(View.VISIBLE);
        } else if (baseListBean.getStatus() == 1) {
            if (baseListBean.getData().size() < 8) {
                mLoadMoreListViewContainer.loadMoreFinish(false, false);
                mAdapter.addData(baseListBean.getData());
            } else {
                mLoadMoreListViewContainer.loadMoreFinish(false, true);
                pageNo = pageNo + 1;
                Log.d("Tag", "==================" + baseListBean.getData().size());
                mAdapter.addData(baseListBean.getData());
            }
        } else {
            mNetErrorView.loadFail();
        }
    }

    public void OnWinRecodersListListenerFailed(VolleyError error) {
        mPtrFrameLayout.refreshComplete();
        mNetErrorView.loadFail();
    }

    // 控件点击事件
    public void onClick(View view) {
        if (view != null)
            switch (view.getId()) {
                case R.id.iv_winrecodes:
                    finish();
                    break;
                case R.id.btn_winrecord_no:
                    Intent intent = new Intent(WinRecordActivity.this, MainActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
    }

    //mListView 的Item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv_payrecodes:
                WinRecodersBean bean = (WinRecodersBean) mAdapter.getItem(position);
                Intent intent = new Intent(WinRecordActivity.this, WinRecordSignInActivity.class);
                intent.putExtra("id", bean.getId());
                intent.putExtra("is_ten", bean.getIs_ten());
                startActivity(intent);
               break;
            case R.id.horizontallistview:
                GuessYouLikeBean beans = (GuessYouLikeBean) mGuessYouLikeListViewAdapter.getItem(position);
                Intent intent1 = new Intent(this, GoodsDetailActivity.class);
                intent1.putExtra("id", beans.getShopid());
                System.out.println("=======id===========>" + beans.getShopid());
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    //猜你喜欢
    @Override
    public void OnGuessYouLikeListListenerSuccess(BaseListBean baseListBean) {
        if (baseListBean != null) {
            if (baseListBean.getStatus() == 1) {

                mGuessData = baseListBean.getData();
                //mGuessAdapter.addData(mGuessData);
                 mGuessYouLikeListViewAdapter.addData(mGuessData);
            }

        }
    }

    @Override
    public void OnGuessYouLikeListListenerFailed(VolleyError error) {

    }
}
