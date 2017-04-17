package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.WinAgoAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WinAgoBean;
import com.hongbaogou.bean.WinAgoObjectBean;
import com.hongbaogou.listener.OnWinAgoListener;
import com.hongbaogou.request.WinAgoRequest;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;

import java.util.List;

/**
 * 往期揭晓
 */
public class WinAgoActivity extends BaseAppCompatActivity implements NetErrorView.OnReloadListener, OnWinAgoListener {

    private NetErrorView netErrorView;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private ListView winAgoList;
    private WinAgoAdapter winAgoAdapter;
    private int pageNo = 1;
    private String sid;
    private WinAgoRequest winAgoRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_ago);
        initView();
        initBarUtils.setSystemBar(this);
        winAgoRequest = new WinAgoRequest();
        winAgoRequest.winInfoRequest(pageNo, sid, this);
    }

    private void initView() {
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);
        // 获取view的引用
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                winAgoRequest.winInfoRequest(pageNo, sid, WinAgoActivity.this);
            }
        });

        winAgoList = (ListView) findViewById(R.id.winAgoList);
        winAgoList.setEmptyView(netErrorView);
        winAgoAdapter = new WinAgoAdapter(this);
        winAgoList.setAdapter(winAgoAdapter);
    }

    public void back(View view) {
        finish();
    }

    public void onReload() {
        winAgoRequest.winInfoRequest(pageNo, sid, this);
    }

    public void onWinAgoSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            WinAgoObjectBean winAgoObjectBean = (WinAgoObjectBean) baseObjectBean.getData();
            if (pageNo == Integer.parseInt(winAgoObjectBean.getMax_page())) {
                loadMoreListViewContainer.loadMoreFinish(false, false);
            } else {
                pageNo = pageNo + 1;
                loadMoreListViewContainer.loadMoreFinish(false, true);
            }
            List<WinAgoBean> winAgoBeans = winAgoObjectBean.getList();
            if (winAgoBeans != null) {
                winAgoAdapter.addData(winAgoBeans);
            }
        } else {
            loadMoreListViewContainer.loadMoreError(0, "请求失败");
            netErrorView.loadFail();
        }
    }

    public void onWinAgoFailed(VolleyError error) {
        loadMoreListViewContainer.loadMoreError(0, "请求失败");
        netErrorView.loadFail();
    }
}
