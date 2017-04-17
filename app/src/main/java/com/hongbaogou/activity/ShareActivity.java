package com.hongbaogou.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.ShareListViewAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.ShareListBean;
import com.hongbaogou.bean.ShareObjectBean;
import com.hongbaogou.listener.OnShareListener;
import com.hongbaogou.request.ShareRequest;
import com.hongbaogou.utils.PopWindowUtils;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.List;

/**
 * 分享晒单
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ShareActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener, OnShareListener, View.OnClickListener, NetErrorView.OnReloadListener {

    private TextView title;
    /**
     * 是否刷新
     */
    private boolean isPullToFresh = false;
    /**
     * 分页加载的页面序号
     */
    private int pageNo = 1;

    /**
     * 数据请求对象
     */
    private ShareRequest shareRequest;

    /**
     * 加载更多的listview控件
     */
    private LoadMoreListViewContainer loadMoreListViewContainer;

    /**
     * listview
     */
    private ListView mListView;

    private ShareListViewAdapter mAdapter;

    private ImageView mBtnmore;

    private ImageView mBtnback;

    /**
     * 设置网络异常的时候显示的view
     */
    private NetErrorView netErrorView;

    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.win_share);

        sid = getIntent().getStringExtra("sid");

        mBtnmore = (ImageView) findViewById(R.id.menuItem);
        mBtnmore.setVisibility(View.VISIBLE);
        mBtnmore.setImageResource(R.mipmap.btn_more);
        mBtnmore.setOnClickListener(this);
        mBtnback = (ImageView) findViewById(R.id.btn_back);

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        mBtnmore.setOnClickListener(this);
        mBtnback.setOnClickListener(this);
        //加载更多listview的控件
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view);
        //使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        //设置可以加载更多
        loadMoreListViewContainer.setAutoLoadMore(true);
        //设置加载更多
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                shareRequest.requestShare(ShareActivity.this, pageNo, sid);
            }
        });

        //Listview控件
        mListView = (ListView) findViewById(R.id.share_listview);
        //当加载为空时显示的view
        mListView.setEmptyView(netErrorView);
        //listview的item的点击事件
        mListView.setOnItemClickListener(this);
        //晒单网络请求的对象
        shareRequest = new ShareRequest();

        //获取布局解析对象
        mAdapter = new ShareListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        //下拉刷新控件
        final PtrClassicFrameLayout ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView_share);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = mListView.getChildCount() > 0 && (mListView.getFirstVisiblePosition() > 0 || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
                } else {
                    flag = mListView.canScrollVertically(-1);
                }
                return !flag;
            }

            //从新刷新执行
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                        //加载更多数据
                        isPullToFresh = true;
                        loadRefreshData();
                    }
                }, 1500);
            }
        });

        //请求网络数据
        requestNetData();

        initBarUtils.setSystemBar(this);

    }

    private void requestNetData() {
        shareRequest = new ShareRequest();
        shareRequest.requestShare(this, pageNo, sid);
    }

    /**
     * 下拉刷新
     */
    private void loadRefreshData() {
        pageNo = 1;
        //不为空,可取
        loadMoreListViewContainer.loadMoreFinish(false, true);
        shareRequest.requestShare(this, pageNo, sid);
    }

    /**
     * item的 点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String sd_id = mAdapter.getObjectByPosttion(position).getSd_id();
        //商品id
        String shopid = mAdapter.getObjectByPosttion(position).getSd_shopsid();
        String uid = mAdapter.getObjectByPosttion(position).getQ_user().getUid();
        String img = mAdapter.getObjectByPosttion(position).getQ_user().getImg();
        String issue = mAdapter.getObjectByPosttion(position).getQishu();
        String username = mAdapter.getObjectByPosttion(position).getQ_user().getUsername();
        Intent intent = new Intent();
        intent.setClass(this, ShareSelfActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("shopid", mAdapter.getObjectByPosttion(position).getSd_shopid());
        intent.putExtra("headImg", img);
        intent.putExtra("sd_id", sd_id);
        intent.putExtra("issue", issue);
        intent.putExtra("uid", uid);
        startActivity(intent);
        // BaseUtils.colseSoftKeyboard(this);
    }

    /**
     * 请求晒单成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestShareSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {

            ShareObjectBean objectBean = (ShareObjectBean) baseObjectBean.getData();
            if (objectBean.getTotal() == 0) {
                //如果没有数据 设置为空
                netErrorView.emptyView();
                return;
            }

            //请求成功
            if (isPullToFresh) {
                mAdapter.clearData();
                Log.d("TAG", "----------------");
            }
            ShareObjectBean data = (ShareObjectBean) baseObjectBean.getData();
            mAdapter.addData((List<ShareListBean>) (data.getList()));
            if (pageNo == data.getMax_page()) {
                //为空,不可加载更多
                loadMoreListViewContainer.loadMoreFinish(false, false);
            } else {
                pageNo += 1;
                loadMoreListViewContainer.loadMoreFinish(false, true);
            }
        } else {
            netErrorView.loadFail();
            loadMoreListViewContainer.loadMoreError(baseObjectBean.getStatus(), baseObjectBean.getMessage());
        }
        isPullToFresh = false;
    }

    /**
     * 请求晒单失败的回调
     *
     * @param error
     */
    @Override
    public void requestShareFailed(VolleyError error) {
        isPullToFresh = false;
        netErrorView.loadFail();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.menuItem:
//                int[] location = new int[2];
//                v.getLocationOnScreen(location);// 获得指定控件的坐标

                View popLayout = PopWindowUtils.initPopwindowLayout(this);
                popLayout.findViewById(R.id.action_home).setOnClickListener(this);
                popLayout.findViewById(R.id.action_list).setOnClickListener(this);
                PopupWindow window = PopWindowUtils.creatPopuptWindow(popLayout);
                window.showAsDropDown(v);
                break;
            case R.id.action_list:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
            case R.id.action_home:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void onReload() {
        shareRequest.requestShare(this, pageNo, sid);
    }
}
