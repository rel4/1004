package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.MyShareAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanMySearchList;
import com.hongbaogou.bean.BeanMyShareObject;
import com.hongbaogou.listener.OnMyShareListener;
import com.hongbaogou.request.MyShareRequest;
import com.hongbaogou.utils.PopWindowUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.List;

public class MyShareActivity extends BaseAppCompatActivity implements View.OnClickListener, NetErrorView.OnReloadListener, OnMyShareListener {

    private TextView title;
    private ImageView menu;

    /**
     * 是否刷新
     */

    private boolean isPullToFresh = false;

    /**
     * 分页加载的页面序号
     */
    private int pageNo = 1;

    /**
     * 设置网络异常的时候显示的view
     */
    private NetErrorView netErrorView;

    /**
     * 加载更多的listview控件
     */
    private LoadMoreListViewContainer loadMoreListViewContainer;

    /**
     * listview
     */
    private ListView mListView;

    /**
     * 我的晒单的请求对象
     */
    private MyShareRequest myShareRequest;

    /**
     * 没有晒单记录时候的布局
     */
    private RelativeLayout no_share_layout;

    /**
     * 适配器
     */
    private MyShareAdapter mAdapter;

    /**
     * 跳转首页的button
     */
    private TextView jumpHome;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_share);
        uid = Pref_Utils.getString(this, "uid", "error");
        Log.e("TAG", "uid = " + uid);
        //初始化view
        findAllView();

        initBarUtils.setSystemBar(this);
        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        //设置加载异常时候显示的view
        mListView.setEmptyView(netErrorView);
//        mListView.setOnItemClickListener(this);
        mAdapter = new MyShareAdapter(this);
        //设置适配器
        mListView.setAdapter(mAdapter);

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
                myShareRequest.requestMyShare(MyShareActivity.this, uid, pageNo);
            }
        });

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
        no_share_layout.setVisibility(View.GONE);
        initBarUtils.setSystemBar(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 请求网络数据
     */
    private void requestNetData() {
        myShareRequest.requestMyShare(this, uid, pageNo);
    }

    /**
     * 下拉刷新
     */
    private void loadRefreshData() {
        pageNo = 1;
        //不为空,可取
        loadMoreListViewContainer.loadMoreFinish(false, true);
        myShareRequest.requestMyShare(this, uid, pageNo);
    }

    /**
     * 初始化view
     */
    private void findAllView() {
        myShareRequest = new MyShareRequest();
        //没有晒单记录的布局
        no_share_layout = (RelativeLayout) findViewById(R.id.no_share_layout);
        jumpHome = (TextView) findViewById(R.id.go_home_btn);
        jumpHome.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.myshare);
        menu = (ImageView) findViewById(R.id.menuItem);
        menu.setImageResource(R.mipmap.btn_more);
        menu.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.myshare_listview);

    }

    //后退键
    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()) {
            case R.id.menuItem:
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
            case R.id.go_home_btn:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            default:
                break;
        }
        //BaseUtils.colseSoftKeyboard(this);
    }


    @Override
    public void onReload() {
        myShareRequest.requestMyShare(this, uid, pageNo);
    }

    /**
     * 成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestMyShareSuccess(BaseObjectBean baseObjectBean) {

        if (baseObjectBean.getStatus() == 1) {//请求成功

            BeanMyShareObject beanMyShareObject = (BeanMyShareObject) baseObjectBean.getData();

            String total = beanMyShareObject.getTotal();

            if (total.equals("0")) {
                //如果没有晒单记录,就设置该布局显示
                no_share_layout.setVisibility(View.VISIBLE);
            }

            List<BeanMySearchList> data = beanMyShareObject.getList();
            if (isPullToFresh) {
                mAdapter.clearData();
            }
            mAdapter.addData(data);
            if (pageNo == beanMyShareObject.getMax_page()) {
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
     * 失败的回调
     *
     * @param error
     */
    @Override
    public void requestMyShareFailed(VolleyError error) {
        netErrorView.loadFail();
        isPullToFresh = false;
    }
}
