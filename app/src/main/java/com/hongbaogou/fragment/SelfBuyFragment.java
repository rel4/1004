package com.hongbaogou.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.CenterActivity;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.activity.SeeNumberActivity;
import com.hongbaogou.adapter.SelfBuyAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanAllLog;
import com.hongbaogou.bean.BeanAllLogList;
import com.hongbaogou.bean.BeanAllLogUser;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.AllLogRequest;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.List;

public class SelfBuyFragment extends UserBaseFragment implements NetErrorView.OnReloadListener, OnAllLogListener,SelfBuyAdapter.OnBuyClickListener,OnAddShopCartListener{

    private int position;
    //跟布局view
    private View mRootView;

    private SelfBuyAdapter mAdapter;

    //加载更多的listview
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private PtrClassicFrameLayout ptrFrameLayout;

    private ListView mListView;

    private AddShopCartRequest addShopCartRequest;

    private AllLogRequest allLogRequest;
    //网络异常时加载的view
    private NetErrorView netErrorView;
    private int pageNo = 1;
    private String uid;

    private boolean isPullToFresh = false;
    private BuyStateListener buyStateListener;

    public void setBuyStateListener(BuyStateListener buyStateListener) {
        this.buyStateListener = buyStateListener;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BuyStateListener) {
            buyStateListener = (BuyStateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface BuyStateListener{
        public boolean getSateListener();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_fragment_alllog, null);
        allLogRequest = new AllLogRequest();

        findAllView();

        mAdapter = new SelfBuyAdapter(getActivity(),buyStateListener.getSateListener());
        mAdapter.setOnBuyClickListener(this);
        netErrorView = (NetErrorView) mRootView.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        //设置加载异常时候显示的view
        mListView.setEmptyView(netErrorView);
        mListView.setAdapter(mAdapter);

        //下拉刷新控件
        ptrFrameLayout = (PtrClassicFrameLayout) mRootView.findViewById(R.id.refreshView);
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
                        //刷新数据
                        loadMoreData();
                        isPullToFresh = true;
                    }
                }, 1500);
            }
        });

        allLogRequest = new AllLogRequest();
        addShopCartRequest = new AddShopCartRequest();

        return mRootView;
    }

    private void loadMoreData() {
        //设置页面为第一页
        pageNo = 1;
        mLoadMoreListViewContainer.loadMoreFinish(false, true);
        allLogRequest.requestAllLog(this, uid, pageNo, position);
    }

    /**
     * 初始化view
     */
    private void findAllView() {

        mListView = (ListView) mRootView.findViewById(R.id.log_listview);
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) mRootView.findViewById(R.id.load_more_list_view);
        // 使用默认样式
        mLoadMoreListViewContainer.useDefaultFooter();
        mLoadMoreListViewContainer.setAutoLoadMore(true);
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                allLogRequest.requestAllLog(SelfBuyFragment.this, uid, pageNo, position);
            }
        });
    }


    public void onReload() {
        allLogRequest.requestAllLog(this, uid, pageNo, position);
    }


    public void requestAllLogSuccess(BaseObjectBean baseObjectBean) {
        ptrFrameLayout.refreshComplete();
        if (baseObjectBean.getStatus() == 1) {
            BeanAllLog allLog = (BeanAllLog) baseObjectBean.getData();
            if(allLog != null){
                if (isPullToFresh) {
                    mAdapter.clearData();
                }
                BeanAllLog data = (BeanAllLog) baseObjectBean.getData();
                List<BeanAllLogList> list = data.getList();
                if(list.size() < 8){
                    mLoadMoreListViewContainer.loadMoreFinish(false, false);
                } else {
                    mLoadMoreListViewContainer.loadMoreFinish(false, true);
                    pageNo = pageNo + 1;
                }
                mAdapter.addData(list);
            } else {
                if(mAdapter.getCount() != 0){
                    mLoadMoreListViewContainer.loadMoreFinish(false, false);
                } else {
                    netErrorView.emptyView();
                }
            }

        } else {
            netErrorView.loadFail();
        }
        isPullToFresh = false;
    }

    public void requestAllLogFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        netErrorView.loadFail();
        isPullToFresh = false;
    }

    public void requestByPosition(String uid ,int position){
        Log.d("TAG", "==============================" + position);
        this.position = position;
        this.uid = uid;
        if(allLogRequest != null){
            pageNo = 1;
            mAdapter.clearData();
            netErrorView.reLoad();
            allLogRequest.requestAllLog(this, uid, pageNo, position);
        }
    }

    public void toDetail(int position){
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        BeanAllLogList beanAllLogList = mAdapter.getBeanAllLogListByPosition(position);
        intent.putExtra("id", beanAllLogList.getShopid());
        intent.putExtra("issue", beanAllLogList.getShopqishu());
        intent.putExtra("titles", beanAllLogList.getShopname());
        intent.putExtra("total", beanAllLogList.getGonumber());
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /*
     * 查看我的号码
     *
     */
    public void lookSelfNumber(int position) {
        Intent intent = new Intent(getActivity(), SeeNumberActivity.class);
        BeanAllLogList beanAllLogList = mAdapter.getBeanAllLogListByPosition(position);
        intent.putExtra("" +
                "" +
                "",beanAllLogList.getShopname());
        intent.putExtra("total",beanAllLogList.getGonumber());
        intent.putExtra("id", beanAllLogList.getShopid());
        intent.putExtra("uid", uid);
        intent.putExtra("qishu",beanAllLogList.getShopqishu());
        startActivity(intent);
    }

    /*
     *   获奖者中心
     */
    public void toWinCenter(int position) {
        Intent intent = new Intent(getActivity(), CenterActivity.class);
        BeanAllLogList beanAllLogList = mAdapter.getBeanAllLogListByPosition(position);
        BeanAllLogUser beanAllLogUser = beanAllLogList.getQ_user();
        intent.putExtra("username",beanAllLogUser.getUsername());
        intent.putExtra("headImg",beanAllLogUser.getImg());
        intent.putExtra("id",beanAllLogUser.getUid());
        intent.getBooleanExtra("isSelf", false);
        startActivity(intent);
    }

    /*
     *   追加购买
     */
    public void buyMore(int position) {
        BeanAllLogList beanAllLogList = mAdapter.getBeanAllLogListByPosition(position);
        //1表示10元专区，0表示不是
        String count = beanAllLogList.getIs_ten() == 1 ? "10":"1";
        String uid = Pref_Utils.getString(getContext().getApplicationContext(),"uid");
        addShopCartRequest.AddShopCartRequest(uid,count,beanAllLogList.getShopid(), this);
    }

    public void onAddShopCartSuccess(MessageCountBean messageCountBean) {
        if (messageCountBean.getStatus() == 1){
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", messageCountBean.getData());
            getActivity().sendBroadcast(intent);

            Intent toCart = new Intent(getActivity(), MainActivity.class);
            toCart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toCart.putExtra("position", 3);
            startActivity(toCart);

            ToastUtil.showToast(getActivity(), R.string.addlistsuccess);
        } else {
            ToastUtil.showToast(getActivity(), R.string.addlistfail);
        }
    }

    public void onAddShopCartFailed(VolleyError error){
        Toast.makeText(getActivity(),"加入清单失败",Toast.LENGTH_SHORT).show();
    }
}
