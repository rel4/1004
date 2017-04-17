package com.hongbaogou.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.activity.BaseWebViewActivity;
import com.hongbaogou.activity.ClassfyActivity;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.activity.GoodsListActivity;
import com.hongbaogou.activity.SearchActivity;
import com.hongbaogou.activity.ShareActivity;
import com.hongbaogou.adapter.BannerAdapter;
import com.hongbaogou.adapter.ProduceAdapter;
import com.hongbaogou.bean.BannerBean;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.CategoryListBean;
import com.hongbaogou.bean.GoodListBean;
import com.hongbaogou.bean.GoodsObjectBean;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.bean.ShoppingCartBean;
import com.hongbaogou.bean.WinMessageListBean;
import com.hongbaogou.dialog.LoadingDialog;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.httpApi.API;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnBannerListener;
import com.hongbaogou.listener.OnCategoryListener;
import com.hongbaogou.listener.OnGoodsListener;
import com.hongbaogou.listener.OnShoppingCartListListener;
import com.hongbaogou.listener.WinMessageListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.CategoryRequest;
import com.hongbaogou.request.GoodsListRequest;
import com.hongbaogou.request.LoadingBannerRequest;
import com.hongbaogou.request.ShoppingCartListRequest;
import com.hongbaogou.request.WinMessageRequest;
import com.hongbaogou.utils.Constant;
import com.hongbaogou.utils.ImageAnimation;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.StartActivcityByType;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.view.headviewpage.AutoScrollViewPager;
import com.hongbaogou.view.loadmoregridview.GridViewWithHeaderAndFooter;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreGridViewContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener, ViewSwitcher.ViewFactory, OnBannerListener, OnCategoryListener, WinMessageListener, OnGoodsListener, ProduceAdapter.AddListListener, OnAddShopCartListener, GridViewWithHeaderAndFooter.OnTouchInViewListener,OnShoppingCartListListener{

    private int[] category = {R.mipmap.category,
            R.mipmap.ten_area,
            R.mipmap.share,
            R.mipmap.problem};
    private LinearLayout categoryView;
    /*
     * 分页加载的页面序号
     */
    private int pageNo = 1;
    /*
     * 中奖的广告对象
     */
    private LinearLayout toWin;
    private AutoScrollViewPager bannerPager;
    private Dialog loadingDialog;
    /*
     *搜索按钮
     */
    private ImageView search;
    private int loadState = 0;
    /*
     * 栏目中的文字列表对象
     */
    private List<LinearLayout> categroyClickViews;
    /*
     * 栏目中的文字列表对象
     */
    private List<TextView> categroyTextViews;
    /*
     * 栏目中的图片列表对象
     */
    private List<NetworkImageView> categroyNetworkImageViews;

    private ProduceAdapter produceAdapter;
    /*
     *请求的banner对象
     */
    private List<BannerBean> bannerBeans;
    /*
     * 中奖消息列表对象
     */
    private List<WinMessageListBean> winMessageListBeans;
    /*
     * 栏目列表对象
     */
    private List<CategoryListBean> categoryListBean;

    private LayoutInflater layoutInflater;
    /*
     * 当前点的位置
     */
    private int currentDotPosition = 0;
    /**
     * Banner视图对象
     */
    private List<View> views;

    /*
    * banner底部的选中的圆点对象
    */
    private List<ImageView> dots;
    /*
     * gridView的头部布局文件
     */
    private View headerView;
    /*
    * 排序对象的父视图对象
    */
    private LinearLayout sortView;
    /*
    * 当前选中的排序对象
    */
    private TextView selectedSortTextView;
    /*
    * 记录选择的排序对象的序号
    */
    private int sortPosition = 0;
    private int switcherPostion = -1;
    private TextSwitcher switcher;
    /*
     * 返回的根视图对象
     */
    private View rootView;
    private GridViewWithHeaderAndFooter gridView;
    private OnListChangeListener onListChangeListener;
    private Resources resource;
    /*
     * 请求商品列表对象
     */
    private GoodsListRequest goodsListRequest;
    /*
     * 加载更多空间
     */
    private LoadMoreGridViewContainer loadMoreGridViewContainer;
    /*
    * 下拉刷新空间
    */
    private PtrClassicFrameLayout ptrFrameLayout;

    private boolean isPullToFresh = false;

    //滑动页面切换的请求
    private LoadingBannerRequest loadingBannerRequest;
    private boolean isloadingBannerSuccess = false;
    //分类的请求
    private CategoryRequest categoryRequest;
    private boolean isCategoryRequest = false;
    //获奖消息的请求
    private WinMessageRequest winMessageRequest;

    private ShoppingCartListRequest shoppingCartListRequest;
    private boolean isShoppingCartListRequestSuccess = false;

    private AddShopCartRequest addShopCartRequest;

    private TextView bannerError;
    //动画效果
    private ImageAnimation imageAnimation;
    private LocalBroadcastManager localBroadcastManager;


    //在主界面中加入购物车成功后，改变主界面购物车数量的回调
    public interface OnListChangeListener {
        void onListChangeListener(int count);
        View getShopCartView();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imageAnimation = new ImageAnimation(getActivity(), onListChangeListener.getShopCartView(), getActivity().getWindow());
        imageAnimation.createAnimLayout();

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initCategoryView();
        initSort();
        initSwitcher();

        //网络请求
        loadingDialog = LoadingDialog.createLoadingDialog(getActivity(), "加载中");
        LoadingDialog.showLoadingDialog(loadingDialog);

        loadingBannerRequest = new LoadingBannerRequest();
        loadingBannerRequest.bannerRequest(this);

        categoryRequest = new CategoryRequest();
        categoryRequest.categoryRequest(this);

        winMessageRequest = new WinMessageRequest();
        winMessageRequest.winMessageRequest(this);

        goodsListRequest = new GoodsListRequest();
        goodsListRequest.goodsListRequest(getSortType(sortPosition), pageNo, this);
        addShopCartRequest = new AddShopCartRequest();

        shoppingCartListRequest = new ShoppingCartListRequest();

//        if(YYJXApplication.isLogin){
            String uid = Pref_Utils.getString(getActivity(), "uid");
//            if(uid != null)
            shoppingCartListRequest.shoppingCartListRequest(uid, HomeFragment.this);
//        }

    }

    public void initView() {
        resource = getResources();

        categroyClickViews = new ArrayList<LinearLayout>();
        categroyTextViews = new ArrayList<TextView>();
        categroyNetworkImageViews = new ArrayList<NetworkImageView>();

        search = (ImageView) rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("keyIsShow", true);
                startActivity(intent);
            }
        });

        //        // 获取view的引用
        loadMoreGridViewContainer = (LoadMoreGridViewContainer) rootView.findViewById(R.id.load_more_grid_view);
//       // 使用默认样式
        loadMoreGridViewContainer.setAutoLoadMore(true);
        loadMoreGridViewContainer.setShowLoadingForFirstPage(true);
        loadMoreGridViewContainer.useDefaultFooter();

        loadMoreGridViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                goodsListRequest.goodsListRequest(getSortType(sortPosition), pageNo, HomeFragment.this);
            }
        });


        gridView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.gridView);
        layoutInflater = LayoutInflater.from(getActivity());
        headerView = layoutInflater.inflate(R.layout.homeheadview, null, false);

        bannerPager = (AutoScrollViewPager) headerView.findViewById(R.id.bannerPager);
        gridView.setOnTouchInViewListener(this);

        gridView.addHeaderView(headerView);

        toWin = (LinearLayout) headerView.findViewById(R.id.toWin);
        toWin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Tag", "" + switcherPostion);
                if (switcherPostion == -1) {
                    winMessageRequest.winMessageRequest(HomeFragment.this);
                } else {
                    Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                    WinMessageListBean winMessageListBean = winMessageListBeans.get(switcherPostion);
                    intent.putExtra("id", winMessageListBean.getId());
                    intent.putExtra("issue", winMessageListBean.getQishu());

                    startActivity(intent);
                }
            }
        });

        bannerError = (TextView) headerView.findViewById(R.id.bannerError);
        bannerError.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bannerError.setText(getResources().getString(R.string.loading));
                loadingBannerRequest.bannerRequest(HomeFragment.this);
            }
        });

        produceAdapter = new ProduceAdapter(getActivity(), this);
        gridView.setAdapter(produceAdapter);
        gridView.setOnItemClickListener(this);


        ptrFrameLayout = (PtrClassicFrameLayout) rootView.findViewById(R.id.refreshView);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                return gridView.getFirstVisiblePosition() == 0 && gridView.getChildAt(0).getTop() == 0;
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        isPullToFresh = true;
                        if (!isloadingBannerSuccess) {
                            loadingBannerRequest.bannerRequest(HomeFragment.this);
                        }
                        if (!isCategoryRequest) {
                            categoryRequest.categoryRequest(HomeFragment.this);
                        }
                        if (!isloadingBannerSuccess) {
                            winMessageRequest.winMessageRequest(HomeFragment.this);
                        }
                        if (!isShoppingCartListRequestSuccess) {
                            String uid = Pref_Utils.getString(getActivity(), "uid");
                            shoppingCartListRequest.shoppingCartListRequest(uid, HomeFragment.this);
                        }
                        refleshGoodsList();
                    }
                }, 10);
            }
        });
      //  ptrFrameLayout.autoRefresh();

    }

    private void initCategoryView() {
        categoryView = (LinearLayout) headerView.findViewById(R.id.categoryView);
        for (int i = 0; i < categoryView.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) categoryView.getChildAt(i);
            categroyClickViews.add(child);
            categroyNetworkImageViews.add((NetworkImageView) child.getChildAt(0));
            categroyTextViews.add((TextView) child.getChildAt(1));
        }
    }


    private void initSwitcher() {
        switcher = (TextSwitcher) headerView.findViewById(R.id.textSwitcher);
        switcher.setFactory(this);
        switcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top));
    }

    Handler handler;
    Runnable run = new Runnable() {
        public void run() {
            if (switcherPostion == winMessageListBeans.size() - 1) {
                switcherPostion = 0;
            } else {
                switcherPostion = switcherPostion + 1;
            }
            switcher.setText(winMessageListBeans.get(switcherPostion).getContent());
            handler.postDelayed(run, 5000);
        }
    };

    public View makeView() {
        TextView tv = (TextView) layoutInflater.inflate(R.layout.winmessage, null);
        tv.setTextSize(12);
        if (winMessageListBeans == null) {
            tv.setText("正在加载中。。。");
        } else {
            tv.setText(winMessageListBeans.get(switcherPostion).getContent());
        }
        return tv;
    }

    /*
     * 初始化排序点击事件
     */
    public void initSort() {
        sortView = (LinearLayout) headerView.findViewById(R.id.sortView);
        for (int i = 0; i < sortView.getChildCount(); i++) {
            TextView childView = (TextView) sortView.getChildAt(i);
            if (i == 0) {
                selectedSortTextView = childView;
            }
            childView.setOnClickListener(new OnSortClickListner(i));
        }

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        GoodListBean goodListBean = produceAdapter.getData(position);
        intent.putExtra("id", goodListBean.getId());
        intent.putExtra("issue", goodListBean.getQishu());
        startActivity(intent);
    }

    /*
     * 初始化banner对象
     */
    private void initBanner() {
        //将图片装载到数组中
        views = new ArrayList<View>();
        dots = new ArrayList<ImageView>();
        LinearLayout dots = (LinearLayout) headerView.findViewById(R.id.dots);
        for (int i = 0; i < bannerBeans.size(); i++) {
            addDot(i, dots);
            if (getActivity() != null) {
                NetworkImageView networkImageView = new com.android.volley.toolbox.NetworkImageView(getActivity());
                ImageLoader imageLoader = RequestManager.getImageLoader();
                networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                networkImageView.setImageUrl(bannerBeans.get(i).getImg(), imageLoader);
                networkImageView.setClickable(true);
                networkImageView.setOnClickListener(new OnBannerClickListener(i));
                views.add(networkImageView);
            }
        }
        BannerAdapter bannerAdapter = new BannerAdapter(views);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setCurrentItem(0);
        bannerPager.addOnPageChangeListener(this);
        bannerPager.setOffscreenPageLimit(bannerBeans.size());
        bannerPager.startTurning(10000);
    }

    /*
     *  添加banner的圆点
     */
    private void addDot(int position, LinearLayout dotLayout) {
        if (getActivity() != null) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (position == 0) {
                imageView.setImageResource(R.mipmap.ad_dot_selected);
            } else {
                imageView.setImageResource(R.mipmap.ad_dot);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            dotLayout.addView(imageView, layoutParams);
            dots.add(imageView);
        }
    }

    private void switchDot(int selectDotPosition) {
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot);
        currentDotPosition = selectDotPosition;
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot_selected);
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    public void onPageSelected(int position) {
        switchDot(position);
    }


    public void onPageScrollStateChanged(int state) {

    }

    /*
     * 排序方式TextView对象，点击监听事件
     */
    public class OnSortClickListner implements View.OnClickListener {
        /*
         * 点击对象的序号
         */
        private int clickIndex;

        public OnSortClickListner(int clickIndex) {
            this.clickIndex = clickIndex;
        }

        public void onClick(View v) {
            if (clickIndex != sortPosition) {
                selectedSortTextView.setTextColor(resource.getColor(R.color.home_sort_tx));
                selectedSortTextView = (TextView) v;
                selectedSortTextView.setTextColor(resource.getColor(R.color.home_sort_tx_selected));
                sortPosition = clickIndex;
            }
            refleshGoodsList();
            //请款数据
            produceAdapter.clearData();
        }
    }

    private void refleshGoodsList() {
        //设置页面为第一页
        pageNo = 1;
        goodsListRequest.goodsListRequest(getSortType(sortPosition), pageNo, HomeFragment.this);
        if (!isPullToFresh) {
            loadingDialog = LoadingDialog.createLoadingDialog(getActivity(), "加载中");
            LoadingDialog.showLoadingDialog(loadingDialog);
        }
    }

    private String getSortType(int sortPosition) {
        String type = "renqi";
        switch (sortPosition) {
            case 0:
                type = "renqi";
                break;
            case 1:
                type = "zuixin";
                break;
            case 2:
                type = "jindu";
                break;
            case 3:
                type = "zongxurenci";
                break;
        }
        return type;
    }

    /*
     * 出来分类栏目，点击监听事件
     */
    public class OnCategoryListener implements View.OnClickListener {
        /*
         * 点击对象的序号
         */
        private int optionIndex;

        public OnCategoryListener(int optionIndex) {
            this.optionIndex = optionIndex;
        }

        public void onClick(View v) {
            Log.d("postion", optionIndex + "");
            switch (optionIndex) {
                //分类
                case 1:
                    startActivity(new Intent(getActivity(), ClassfyActivity.class));
                    break;
                //十元专区
                case 2:
                    Intent goodsListIntent = new Intent(getActivity(), GoodsListActivity.class);
                    goodsListIntent.putExtra("title", "10元专区");
                    goodsListIntent.putExtra("cateid", "-1");
                    startActivity(goodsListIntent);
                    break;
                //晒单
                case 3:
                    Intent stareIntent = new Intent(getActivity(), ShareActivity.class);
                    stareIntent.putExtra("sid", 0);
                    startActivity(stareIntent);
                    break;
                //常见问题
                case 4:
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), BaseWebViewActivity.class);
                    intent.putExtra("title", "常见问题");
                    intent.putExtra("url", API.QUESTION_API);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    public class OnBannerClickListener implements View.OnClickListener {
        /*
         * 点击对象的序号
         */
        private int optionIndex;

        public OnBannerClickListener(int optionIndex) {
            Log.e("optionIndex", "" + optionIndex);
            this.optionIndex = optionIndex;
        }

        public void onClick(View v) {
            Log.e("optionIndex", "" + optionIndex);
            BannerBean bannerBean = bannerBeans.get(optionIndex);
            int type = Integer.parseInt(bannerBean.getSlide_type());

            System.out.println("---------"+bannerBean.getLink());

            Map<String, String> map = new HashMap<String, String>();
            if (type == Constant.WebView) {
                map.put("title", bannerBean.getTitle());
                map.put("url", bannerBean.getLink());
            } else if (type == Constant.GoodsDetail) {
                map.put("id", bannerBean.getShopid());
                map.put("issue", bannerBean.getQishu());
            } else if (type == Constant.Search) {
                map.put("key", bannerBean.getKeywords());
                map.put("keyIsShows", "0");
            } else if(type == Constant.CLASSIFY){
                map.put("title", bannerBean.getTitle());
                map.put("cateid", bannerBean.getCateid());
            }
            StartActivcityByType.startActivty(getActivity(), type, map);
        }
    }

    private void updateLoadState() {
        if (loadState == 4) {
            LoadingDialog.hideLoadingDialog(loadingDialog);
        } else {
            loadState = loadState + 1;
        }
    }

    /*
     * 主界面的banner请求
     */
    public void onBannerSuccess(BaseListBean baseListBean) {
        if (baseListBean.getStatus() == 1) {
            isloadingBannerSuccess = true;
            bannerError.setVisibility(View.GONE);
            bannerBeans = baseListBean.getData();
            initBanner();
        } else {
            bannerError.setText(getResources().getString(R.string.load_error));
            bannerError.setVisibility(View.VISIBLE);
        }
        updateLoadState();
    }

    public void onBannerFailed(VolleyError error) {
        bannerError.setText(getResources().getString(R.string.load_error));
        bannerError.setVisibility(View.VISIBLE);
        updateLoadState();
    }

    /*
     * 主界面banner下面的分类请求
     */
    public void onCategorySuccess(BaseListBean baseListBean) {
        ImageLoader imageLoader = RequestManager.getImageLoader();
        if (baseListBean.getStatus() == 1) {
            isCategoryRequest = true;
            categoryListBean = baseListBean.getData();
            for (int i = 0; i < categoryListBean.size(); i++) {
                categroyClickViews.get(i).setOnClickListener(new OnCategoryListener(Integer.parseInt(categoryListBean.get(i).getType())));
                categroyTextViews.get(i).setText(categoryListBean.get(i).getName());
                NetworkImageView networkImageView = categroyNetworkImageViews.get(i);
                networkImageView.setDefaultImageResId(R.mipmap.img_blank);
                networkImageView.setErrorImageResId(category[i]);
                networkImageView.setImageUrl(categoryListBean.get(i).getImg(), imageLoader);
            }

        } else {

        }
        updateLoadState();
    }

    public void onCategoryFailed(VolleyError error) {
        updateLoadState();
    }

    /*
     * 主界面中奖消息请求
     */
    public void onWinMessageSuccess(BaseListBean baseListBean) {
        if (baseListBean.getStatus() == 1) {
            winMessageListBeans = baseListBean.getData();
            switcherPostion = 0;
            handler = new Handler();
            handler.postDelayed(run, 1000);
        } else {
            switcher.setText("加载失败，点击重新加载");
        }
        updateLoadState();
    }

    public void onWinMessageFailed(VolleyError error) {
        updateLoadState();
        switcher.setText("加载失败，点击重新加载");
    }

    /*
   * 主界面中的商品列表请求
   */
    public void onGoodsSuccess(BaseObjectBean baseObjectBean) {
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
            loadMoreGridViewContainer.loadMoreError(baseObjectBean.getStatus(), baseObjectBean.getMessage());
        }
        loadGoodsFinish();
        updateLoadState();
    }

    public void onGoodsFailed(VolleyError error) {
        updateLoadState();
        loadMoreGridViewContainer.loadMoreError(0, "无法连接网络");
        loadGoodsFinish();
    }

    private void loadGoodsFinish() {
        if (isPullToFresh) {
            ptrFrameLayout.refreshComplete();
        } else {
            if(loadingDialog != null && loadingDialog.isShowing()){
                LoadingDialog.hideLoadingDialog(loadingDialog);
            }
        }
        isPullToFresh = false;
    }

    public void addToList(GoodListBean goodListBean, Drawable drawable, int[] location) {
        imageAnimation.doAnim(drawable, location);
        String uid = Pref_Utils.getString(getActivity(), "uid");
        addShopCartRequest.AddShopCartRequest(uid, goodListBean.getDefault_renci(), goodListBean.getId(), this);
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


    /*
     * 检查按下对象的是否是Viewpager
     */
    public View getTouchView() {
        return bannerPager;
    }


    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    public void onLowMemory() {
        imageAnimation.clearAnimation();
        super.onLowMemory();
    }

    // 开始自动翻页
    public void onResume() {
        super.onResume();
        //开始自动翻页
        bannerPager.startTurning(10000);
    }

    // 停止自动翻页
    public void onPause() {
        super.onPause();
        //停止翻页
        bannerPager.stopTurning();
    }
    private boolean isAttach = true;

    public void onShoppingCartListSuccess(BaseListBean baseListBean) {
        if (!isAttach) {
            return;
        }


        if (baseListBean.getStatus() == 1) {
            isShoppingCartListRequestSuccess = true;
            List<ShoppingCartBean> shoppingCartBeans = baseListBean.getData();
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", shoppingCartBeans.size());
            getActivity().sendBroadcast(intent);
        }
    }

    public void onShoppingCartListFailed(VolleyError error) {

    }

    public void onDestroy() {
        isAttach = false;
        try{
            if(loadingDialog != null && loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
        }catch (Exception e) {
        }
        super.onDestroy();
    }


    @Override
    public View getScrollableView() {
        return gridView;
    }
}