package com.hongbaogou.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.adapter.GridViewAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanBestNewList;
import com.hongbaogou.bean.BeanBestNewObject;
import com.hongbaogou.listener.OnBestNewListener;
import com.hongbaogou.request.BestNewRequest;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.GridViewWithHeaderAndFooter;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreGridViewContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.ArrayList;

public class WinFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnBestNewListener, NetErrorView.OnReloadListener{

    /*
    * 分页加载的页面序号
    */
    private int pageNo = 1;

    //是否刷新
    private boolean isPullToFresh = false;

    //每页多少个
    private int SIZE = 8;

    private BestNewRequest bestNewRequest;
    private PtrClassicFrameLayout ptrFrameLayout;
    /*
     * 加载更多的控件
     */
    private LoadMoreGridViewContainer loadMoreGridViewContainer;
    /**
     * 跟布局
     */
    private View rootView;

    /**
     * 包含页头页页脚的gridview
     */
    private GridViewWithHeaderAndFooter gridView;

    /**
     * gridview的适配器
     */
    private GridViewAdapter mAdapter;

    /**
     * 集合
     */
    private ArrayList<BeanBestNewList> list = new ArrayList<BeanBestNewList>();

    private NetErrorView netErrorView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_win, container, false);

        gridView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.gridView);
        netErrorView = (NetErrorView) rootView.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        gridView.setEmptyView(netErrorView);
        mAdapter = new GridViewAdapter(getActivity(),gridView);
        gridView.setOnItemClickListener(this);

        bestNewRequest = new BestNewRequest();
        // 获取view的引用
        loadMoreGridViewContainer = (LoadMoreGridViewContainer) rootView.findViewById(R.id.load_more_grid_view);
        // 使用默认样式
        loadMoreGridViewContainer.useDefaultFooter();
        loadMoreGridViewContainer.setAutoLoadMore(true);
        loadMoreGridViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                bestNewRequest.requestBestNewData(SIZE, pageNo, WinFragment.this);
            }
        });

        gridView.setAdapter(mAdapter);

        //下拉刷新控件
        ptrFrameLayout = (PtrClassicFrameLayout) rootView.findViewById(R.id.refreshView);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = gridView.getChildCount() > 0 && (gridView.getFirstVisiblePosition() > 0 || gridView.getChildAt(0).getTop() < gridView.getPaddingTop());
                } else {
                    flag = gridView.canScrollVertically(-1);
                }
                return !flag;
            }

            //从新刷新执行
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        //刷新数据
                        isPullToFresh = true;
                        //加载更多数据
                        loadMoreData();
                    }
                }, 1500);
            }
        });
        return rootView;
    }

    //加载更多
    private void loadMoreData() {
        //设置页面为第一页
        pageNo = 1;
        loadMoreGridViewContainer.loadMoreFinish(false, true);
        bestNewRequest.requestBestNewData(SIZE, pageNo, WinFragment.this);
    }

    //请求网络数据
    private void requestNetData() {
        //创建请求对象
        BestNewRequest request = new BestNewRequest();
        request.requestBestNewData(SIZE, pageNo, this);
    }

    /**
     * 成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestBestNewDataSuccess(BaseObjectBean baseObjectBean) {
        ptrFrameLayout.refreshComplete();
        if (baseObjectBean.getStatus() == 1) {
            if (isPullToFresh) {
                mAdapter.clearData();
            }
            BeanBestNewObject data = (BeanBestNewObject) baseObjectBean.getData();
            ArrayList<BeanBestNewList> list = (ArrayList<BeanBestNewList>)data.getList();
            if(list != null){
                mAdapter.addData(list);
            }


            if (pageNo == Integer.parseInt(data.getMax_page())) {
                //第一个参数表示返回的结果集  是否为空
                //第二个参数表示是否有可以继续取的
                loadMoreGridViewContainer.loadMoreFinish(false, false);
            } else {
                pageNo = pageNo + 1;
                loadMoreGridViewContainer.loadMoreFinish(false, true);
            }

        } else {
            loadMoreGridViewContainer.loadMoreError(0, "加载失败");
            netErrorView.loadFail();
        }
        isPullToFresh = false;
    }

    /**
     * 失败的对调
     */
    public void requestBestNewDataFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        isPullToFresh = false;
        netErrorView.loadFail();
    }

    public boolean initRequest() {
        //请求网络数据
        requestNetData();
        return true;
    }

    @Override
    public void onReload() {
        requestNetData();
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), GoodsDetailActivity.class);
        BeanBestNewList newList = mAdapter.getItem(position);
        intent.putExtra("id", newList.getId());
        intent.putExtra("issue", newList.getQishu());
        startActivity(intent);
    }
}
