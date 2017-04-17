package com.hongbaogou.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.ShareSelfActivity;
import com.hongbaogou.adapter.UserShareListViewAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanUserShare;
import com.hongbaogou.listener.OnUserShareListener;
import com.hongbaogou.request.UserShareRequest;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.List;

/**
 * 他人的分享晒单
 */
public class ShareRecordFragment extends UserBaseFragment implements AdapterView.OnItemClickListener, OnUserShareListener, NetErrorView.OnReloadListener {

    /**
     * 是否刷新
     */
    private boolean isPullToFresh = false;
    private int pageNo = 1;
    private PtrClassicFrameLayout ptrFrameLayout;
    //加载更多的listview
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private ListView winRecordListView;
    private UserShareListViewAdapter shareAdapter;
    //网络异常时加载的view
    private NetErrorView netErrorView;
    /**
     * 数据请求对象
     */
    private UserShareRequest shareRequest;
    private String uid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //晒单网络请求的对象
        shareRequest = new UserShareRequest();

        ptrFrameLayout = (PtrClassicFrameLayout) inflater.inflate(R.layout.fragment_win_record, container, false);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        netErrorView = (NetErrorView) ptrFrameLayout.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        winRecordListView = (ListView) ptrFrameLayout.findViewById(R.id.winRecordListView);
        winRecordListView.setEmptyView(netErrorView);
        loadMoreListViewContainer = (LoadMoreListViewContainer) ptrFrameLayout.findViewById(R.id.load_more_list_view);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                shareRequest.requestUserShare(ShareRecordFragment.this, pageNo, uid);
            }
        });

        shareAdapter = new UserShareListViewAdapter(getActivity());
        winRecordListView.setAdapter(shareAdapter);
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
                        isPullToFresh = true;
                        //不为空,可取
                        shareRequest.requestUserShare(ShareRecordFragment.this, pageNo, uid);
                    }
                }, 1500);
            }
        });

        return ptrFrameLayout;
    }

    public void onReload() {
        shareRequest.requestUserShare(this, pageNo, uid);
    }

    public void requestByPosition(String uid, int position) {
        this.uid = uid;
        if (shareRequest != null) {
            pageNo = 1;
            shareAdapter.clearData();
            netErrorView.reLoad();
            shareRequest.requestUserShare(this, pageNo, uid);
        }
    }


    /**
     * item的 点击事件
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String sid = shareAdapter.getObjectByPosttion(position).getSd_id();
        Intent intent = new Intent();
        intent.setClass(getContext(), ShareSelfActivity.class);
        intent.putExtra("sd_id", sid);
        intent.putExtra("shopid", shareAdapter.getObjectByPosttion(position).getSd_shopid());
        intent.putExtra("issue", shareAdapter.getObjectByPosttion(position).getQishu());
        intent.putExtra("uid",shareAdapter.getObjectByPosttion(position).getQ_user().getUid());
        intent.putExtra("headImg",shareAdapter.getObjectByPosttion(position).getQ_user().getImg());
        Log.e("TAG","-=-=-"+shareAdapter.getObjectByPosttion(position).getQ_user().getImg());
        intent.putExtra("username",shareAdapter.getObjectByPosttion(position).getQ_user().getUsername());
        startActivity(intent);
    }


    @Override
    public void requestUserShareSuccess(BaseListBean baseListBean) {
        ptrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 1) {//请求成功


            if (isPullToFresh) {
                shareAdapter.clearData();
            }
            List<BeanUserShare> list = baseListBean.getData();
            shareAdapter.addData(list);
        } else {
            netErrorView.setNoDataText("您还没有晒单分享记录");
            netErrorView.emptyView();
            loadMoreListViewContainer.loadMoreError(baseListBean.getStatus(), baseListBean.getMessage());
        }
        isPullToFresh = false;
    }

    @Override
    public void requestUserShareFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        isPullToFresh = false;
        netErrorView.loadFail();
        netErrorView.setNoDataText("暂时还没有数据");
        Log.d("TAG", "请求失败");
    }
}
