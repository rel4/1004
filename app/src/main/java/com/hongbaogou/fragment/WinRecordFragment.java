package com.hongbaogou.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.adapter.WinAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.WinRecodersBean;
import com.hongbaogou.listener.OnWinRecodersListListener;
import com.hongbaogou.request.WinRecordsRequests;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.List;

/**
 * 他人的中奖记录
 */
public class WinRecordFragment extends UserBaseFragment implements NetErrorView.OnReloadListener, OnWinRecodersListListener,AdapterView.OnItemClickListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    //加载更多的listview
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private ListView winRecordListView;
    private WinAdapter winAdapter;
    private NetErrorView netErrorView;
    private WinRecordsRequests winRecordsRequests;
    private int pageNo = 1;
    private String uid;
    private boolean isFresh = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        winRecordsRequests = new WinRecordsRequests();
        ptrFrameLayout = (PtrClassicFrameLayout)inflater.inflate(R.layout.fragment_win_record, container, false);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        loadMoreListViewContainer = (LoadMoreListViewContainer) ptrFrameLayout.findViewById(R.id.load_more_list_view);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                winRecordsRequests.payRecodersRequest(uid, pageNo, WinRecordFragment.this);
            }
        });

        winRecordListView = (ListView) ptrFrameLayout.findViewById(R.id.winRecordListView);
        netErrorView = (NetErrorView) ptrFrameLayout.findViewById(R.id.netErrorView);
        winRecordListView.setEmptyView(netErrorView);
        netErrorView.setOnReloadListener(this);
        winAdapter = new WinAdapter(getActivity());
        winRecordListView.setAdapter(winAdapter);
        winRecordListView.setOnItemClickListener(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = winRecordListView.getChildCount() > 0 && (winRecordListView.getFirstVisiblePosition() > 0 || winRecordListView.getChildAt(0).getTop() < winRecordListView.getPaddingTop());
                } else {
                    flag = winRecordListView.canScrollVertically(-1);
                }
                return !flag;
            }

            //从新刷新执行
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        pageNo = 1;
                        isFresh = true;
                        winRecordsRequests.payRecodersRequest(uid, pageNo, WinRecordFragment.this);

                    }
                }, 1500);
            }
        });
        return ptrFrameLayout;
    }

    public void onReload() {
        netErrorView.reLoad();
        winRecordsRequests.payRecodersRequest(uid, pageNo, this);
    }



    public void OnWinRecodersListListenerSuccess(BaseListBean baseListBean) {
        ptrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 1) {
            List<WinRecodersBean> winRecodersBeans = baseListBean.getData();
            if(winRecodersBeans != null){
                if (baseListBean.getData().size() < 8) {
                    loadMoreListViewContainer.loadMoreFinish(true, false);
                } else {
                    loadMoreListViewContainer.loadMoreFinish(false, true);
                    pageNo = pageNo + 1;
                }
                if(isFresh){
                    winAdapter.reloadData(winRecodersBeans);
                } else {
                    winAdapter.addData(winRecodersBeans);
                }
            }

        } else {
            netErrorView.setNoDataText("您还没有中奖纪录");
            netErrorView.emptyView();
        }
        isFresh = false;
    }

    public void OnWinRecodersListListenerFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        netErrorView.setNoDataText("暂时还没有数据");
        netErrorView.loadFail();
        isFresh = false;
    }

    public void requestByPosition(String uid ,int position){
        this.uid = uid;
        pageNo = 1;
        if(winRecordsRequests != null){
            isFresh = true;
            winAdapter.clearData();
            winRecordsRequests.payRecodersRequest(uid, pageNo, this);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        WinRecodersBean winRecodersBean = winAdapter.getData(position);
        intent.putExtra("id", winRecodersBean.getShopid());
        intent.putExtra("issue", winRecodersBean.getQishu());
        startActivity(intent);
    }
}
