package com.hongbaogou.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.adapter.ProduceAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodListBean;
import com.hongbaogou.bean.GoodsObjectBean;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnGoodsListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.GoodsListRequest;
import com.hongbaogou.utils.ImageAnimation;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.GridViewWithHeaderAndFooter;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreGridViewContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;


/**
 * Created by admin on 2016/10/19.
 */
public class HomeContentFragment extends BaseFragment implements ProduceAdapter.AddListListener,OnAddShopCartListener,OnGoodsListener,AdapterView.OnItemClickListener,NetErrorView.OnReloadListener{

    /*
   * 分页加载的页面序号
   */
    private int pageNo = 1;

    //是否刷新
    private boolean isPullToFresh = false;

    //每页多少个
    private int SIZE = 8;

    private ProduceAdapter produceAdapter;
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

    private Handler handler;
    //动画效果
    private ImageAnimation imageAnimation;
    private LocalBroadcastManager localBroadcastManager;
    private OnListChangeListener onListChangeListener;
    private AddShopCartRequest addShopCartRequest;


    private NetErrorView netErrorView;
    private String type;

    private GoodsListRequest goodsListRequest;

    public HomeContentFragment() {
    }

    @SuppressLint("ValidFragment")

    public HomeContentFragment(Handler handler) {
        this.handler = handler;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.new_home, container, false);
        // 获取view的引用
        loadMoreGridViewContainer = (LoadMoreGridViewContainer) rootView.findViewById(R.id.load_more_grid_view);
        // 使用默认样式
        loadMoreGridViewContainer.useDefaultFooter();
        loadMoreGridViewContainer.setAutoLoadMore(true);
        loadMoreGridViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                //bestNewRequest.requestBestNewData(SIZE, pageNo, HomeContentFragment.this);
                goodsListRequest.goodsListRequest(type, pageNo, HomeContentFragment.this);

            }
        });


        gridView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.gridView);
        gridView.addHeaderView(View.inflate(getActivity(), R.layout.tab_holder, null));
        netErrorView = (NetErrorView) rootView.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);
        produceAdapter = new ProduceAdapter(getActivity(), this);
        gridView.setAdapter(produceAdapter);
        gridView.setOnItemClickListener(this);
        addShopCartRequest = new AddShopCartRequest();

        imageAnimation = new ImageAnimation(getActivity(), onListChangeListener.getShopCartView(), getActivity().getWindow());
        imageAnimation.createAnimLayout();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        initView();
        requestNetData();
        return rootView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListChangeListener) {
            onListChangeListener = (OnListChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        onListChangeListener = null;
    }


    //在主界面中加入购物车成功后，改变主界面购物车数量的回调
    public interface OnListChangeListener {
        void onListChangeListener(int count);
        View getShopCartView();
    }


    private  void initView(){

        Bundle arguments = getArguments();
        if (arguments == null) {
            //ToastUtils.showToast("arguments null");
            return;
        } else {
            type = arguments.getString("type");
        }
        //mParentFrag = (HomeFragment2) getFragmentManager().findFragmentByTag("content");
    }



    //请求网络数据
    private void requestNetData() {
        //创建请求对象
        goodsListRequest = new GoodsListRequest();
        goodsListRequest.goodsListRequest(type, pageNo, this);
    }



    @Override
    public void onReload() {
        requestNetData();
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        GoodListBean goodListBean = produceAdapter.getData(position);
        intent.putExtra("id", goodListBean.getId());
        intent.putExtra("issue", goodListBean.getQishu());
        startActivity(intent);
    }

    @Override
    public void onGoodsSuccess(BaseObjectBean baseObjectBean) {

        Message message=new Message();
        message.what=1;
        handler.sendMessage(message);
        if (baseObjectBean.getStatus() == 1) {
            if (isPullToFresh) {
                produceAdapter.clearData();
            }
            GoodsObjectBean goodsObjectBean = (GoodsObjectBean) baseObjectBean.getData();
            produceAdapter.addData(goodsObjectBean.getList());

            if (pageNo == goodsObjectBean.getMax_page()) {
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

    @Override
    public void onGoodsFailed(VolleyError error) {

       // ptrFrameLayout.refreshComplete();
        isPullToFresh = false;
        netErrorView.loadFail();

    }

    public void onAddShopCartSuccess(MessageCountBean messageCountBean) {
        if (messageCountBean.getStatus() == 1) {
            if (onListChangeListener != null) {
                onListChangeListener.onListChangeListener(messageCountBean.getData());
            }
            ToastUtil.showToast(getActivity(), R.string.addlistsuccess);

            Intent intent = new Intent();
            intent.setAction(ConstantValues.REFRESH);
            localBroadcastManager.sendBroadcast(intent);
        } else {
            ToastUtil.showToast(getActivity(), R.string.addlistfail);
        }
    }

    public void onAddShopCartFailed(VolleyError error) {
        ToastUtil.showToast(getActivity(), R.string.addlistfail);
    }


    public void addToList(GoodListBean goodListBean, Drawable drawable, int[] location) {
        imageAnimation.doAnim(drawable, location);
        String uid = Pref_Utils.getString(getActivity(), "uid");
        addShopCartRequest.AddShopCartRequest(uid, goodListBean.getDefault_renci(), goodListBean.getId(), this);
    }

    public void onRefreshing() {
        pageNo = 1;
        produceAdapter.clearData();
        requestNetData();
    }

    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    public void onLowMemory() {
        imageAnimation.clearAnimation();
        super.onLowMemory();
    }

    @Override
    public View getScrollableView() {
        return gridView;
    }
}
