package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.GoodsAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodsCategoryBean;
import com.hongbaogou.bean.GoodsObjectCategoryBean;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnGoodsCategoryListListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.GoodsCategoryRequest;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

/*
 * 商品列表
 */
public class GoodsListActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener, NetErrorView.OnReloadListener, OnGoodsCategoryListListener, GoodsAdapter.AddShoppingCartListener, OnAddShopCartListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private ListView goodsList;
    private GoodsAdapter goodsAdapter;
    private GoodsCategoryRequest goodsCategoryRequest;
    private AddShopCartRequest addShopCartRequest;

    private NetErrorView netErrorView;
    private int pageNo = 1;
    private TextView titleTextView;
    private String title;
    private String cateid;
    private boolean isFresh = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        initView();

        initBarUtils.setSystemBar(this);
        addShopCartRequest = new AddShopCartRequest();
        goodsCategoryRequest = new GoodsCategoryRequest();
        goodsCategoryRequest.goodsCategoryRequest(cateid, pageNo, this);
    }

    private void initView() {

        titleTextView = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        cateid = intent.getStringExtra("cateid");
        Log.e("TAG", "cateid =-> " + cateid);
        titleTextView.setText(title);


        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);
        // 获取view的引用
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setShowLoadingForFirstPage(true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                goodsCategoryRequest.goodsCategoryRequest(cateid, pageNo, GoodsListActivity.this);

            }
        });

        goodsList = (ListView) findViewById(R.id.goodsList);
        goodsList.setEmptyView(netErrorView);
        goodsAdapter = new GoodsAdapter(getApplicationContext());
        goodsAdapter.setAddShoppingCartListener(this);
        goodsList.setAdapter(goodsAdapter);
        goodsList.setOnItemClickListener(this);

        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = goodsList.getChildCount() > 0 && (goodsList.getFirstVisiblePosition() > 0 || goodsList.getChildAt(0).getTop() < goodsList.getPaddingTop());
                } else {
                    flag = goodsList.canScrollVertically(-1);
                }
                return !flag;

            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        isFresh = true;
                        pageNo = 1;
                        goodsCategoryRequest.goodsCategoryRequest(cateid, pageNo, GoodsListActivity.this);
                    }
                }, 10);
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        GoodsCategoryBean goodsCategoryBean = goodsAdapter.getData(position);
        intent.putExtra("id", goodsCategoryBean.getId());
        intent.putExtra("issue", goodsCategoryBean.getQishu());
        startActivity(intent);
    }

    public void onReload() {
        goodsCategoryRequest.goodsCategoryRequest(cateid, pageNo, GoodsListActivity.this);
    }

    public void onGoodsCategoryListSuccess(BaseObjectBean baseObjectBean) {
        if (ptrFrameLayout != null)
            ptrFrameLayout.refreshComplete();
        if (baseObjectBean.getStatus() == 1) {
            GoodsObjectCategoryBean goodsObjectCategoryBean = (GoodsObjectCategoryBean) baseObjectBean.getData();
            titleTextView.setText(title + "(" + goodsObjectCategoryBean.getTotal() + ")");

            if (pageNo == Integer.parseInt(goodsObjectCategoryBean.getMax_page())) {
                loadMoreListViewContainer.loadMoreFinish(false, false);
                Log.d("tag", "=================================" + goodsObjectCategoryBean.getList().size());
            } else {
                pageNo = pageNo + 1;
                loadMoreListViewContainer.loadMoreFinish(false, true);
            }
            if (isFresh) {
                goodsAdapter.reLoadData(goodsObjectCategoryBean.getList());
            } else {
                goodsAdapter.addData(goodsObjectCategoryBean.getList());
            }
            goodsObjectCategoryBean = null;

        } else {
            loadMoreListViewContainer.loadMoreError(0, "请求失败");
            netErrorView.loadFail();
        }
        isFresh = false;
    }

    public void onGoodsCategoryListFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        loadMoreListViewContainer.loadMoreError(0, "请求失败");
        netErrorView.loadFail();
        isFresh = false;
    }

    public void back(View v) {
        finish();
    }


    public void addShoppingCar(GoodsCategoryBean goodsCategoryBean) {
        String uid = Pref_Utils.getString(this, "uid");
        addShopCartRequest.AddShopCartRequest(uid, goodsCategoryBean.getDefault_renci(), goodsCategoryBean.getId(), this);
    }

    public void onAddShopCartSuccess(MessageCountBean messageCountBean) {
        if (messageCountBean.getStatus() == 1) {
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", messageCountBean.getData());
            sendBroadcast(intent);
            ToastUtil.showToast(this, R.string.addlistsuccess);
        } else {
            ToastUtil.showToast(this, R.string.addlistfail);
        }
    }

    public void onAddShopCartFailed(VolleyError error) {
        ToastUtil.showToast(this, R.string.addlistfail);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (ptrFrameLayout != null) {
            ptrFrameLayout.setLastUpdateTimeKey(null);
            ptrFrameLayout = null;
        }

        loadMoreListViewContainer = null;

        if (goodsList != null) {
            goodsList.setEmptyView(null);
        }
        if (goodsAdapter != null) {
            goodsList.setOnItemClickListener(null);
            goodsAdapter.setAddShoppingCartListener(null);
            goodsAdapter.destory();
            goodsAdapter = null;
        }
        goodsCategoryRequest = null;
        addShopCartRequest = null;
        if (netErrorView != null) {
            netErrorView.setOnReloadListener(null);
            netErrorView.destory();
            netErrorView = null;
        }
        titleTextView = null;
        title = null;
        cateid = null;
        setContentView(R.layout.base_actionbar);
    }
}
