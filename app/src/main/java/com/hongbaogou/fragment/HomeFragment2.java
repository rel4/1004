package com.hongbaogou.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.hongbaogou.R;
import com.hongbaogou.activity.BaseWebViewActivity;
import com.hongbaogou.activity.ClassfyActivity;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.activity.GoodsListActivity;
import com.hongbaogou.activity.SearchActivity;
import com.hongbaogou.activity.ShareActivity;
import com.hongbaogou.adapter.BannerAdapter;
import com.hongbaogou.bean.BannerBean;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.CategoryListBean;
import com.hongbaogou.bean.WinMessageListBean;
import com.hongbaogou.dialog.LoadingDialog;
import com.hongbaogou.httpApi.API;
import com.hongbaogou.listener.OnBannerListener;
import com.hongbaogou.listener.OnCategoryListener;
import com.hongbaogou.listener.WinMessageListener;
import com.hongbaogou.request.BaseRequest;
import com.hongbaogou.request.CategoryRequest;
import com.hongbaogou.request.LoadingBannerRequest;
import com.hongbaogou.request.WinMessageRequest;
import com.hongbaogou.utils.Constant;
import com.hongbaogou.utils.Dip2PxUtils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.StartActivcityByType;
import com.hongbaogou.view.PagerSlidingTabStrip;
import com.hongbaogou.view.headviewpage.AutoScrollViewPager;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/19.
 */
public class HomeFragment2 extends BaseFragment implements ViewSwitcher.ViewFactory,WinMessageListener,OnCategoryListener,ViewPager.OnPageChangeListener,OnBannerListener, Serializable {

    private View fragmentView;
    public RelativeLayout mTopContainer;
    public PagerSlidingTabStrip mSlidingTab;
    public ViewPager mContentViewpager;
    public List<BaseFragment> mFragmentList;
    public List<String> mTitles;
    public PtrClassicFrameLayout mPtrFrame;
    public ScrollableLayout mScrollLayout;
    private View mTabLine;
    private boolean noTab;
    public int mPosition;
    private View mTopView;
    private boolean isloadingBannerSuccess = false;
    private TextView bannerError;
    private int loadState = 0;
    private int switcherPostion= -1;
    private TextSwitcher switcher;

    private LayoutInflater layoutInflater;

    /*
    * 中奖消息列表对象
    */
    private List<WinMessageListBean> winMessageListBeans;
    /*
  * 中奖的广告对象
  */
    private LinearLayout toWin;

    //获奖消息的请求
    private WinMessageRequest winMessageRequest;

    private boolean isCategoryRequest = false;
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
    private LinearLayout categoryView;
    //分类的请求
    private CategoryRequest categoryRequest;

    /*
     * 栏目列表对象
     */
    private List<CategoryListBean> categoryListBean;

    private int[] category = {R.mipmap.category,
            R.mipmap.ten_area,
            R.mipmap.share,
            R.mipmap.problem};

    private Dialog loadingDialog;

    /*
    * 当前点的位置
    */
    private int currentDotPosition = 0;

    //滑动页面切换的请求
    private LoadingBannerRequest loadingBannerRequest;
    /**
     * Banner视图对象
     */
    private List<View> views;

    /*
    * banner底部的选中的圆点对象
    */
    private List<ImageView> dots;
    /*
   *请求的banner对象
   */
    private List<BannerBean> bannerBeans;

    private AutoScrollViewPager bannerPager;

     private Handler handler1=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             if(msg.what==1){
                 mPtrFrame.refreshComplete();
             }
             super.handleMessage(msg);
         }
     };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.home2, container, false);
        layoutInflater = LayoutInflater.from(getActivity());

        mPtrFrame = (PtrClassicFrameLayout) fragmentView.findViewById(R.id.refreshView);
        mScrollLayout = (ScrollableLayout) fragmentView.findViewById(R.id.scroll_layout);
        mTopContainer = (RelativeLayout) fragmentView.findViewById(R.id.top_container);
        mSlidingTab = (PagerSlidingTabStrip) fragmentView.findViewById(R.id.tab);
        mSlidingTab.setVisibility(View.INVISIBLE);
        mSlidingTab.setTextSize(Dip2PxUtils.sp2px(getActivity(), 12));
        mTabLine = fragmentView.findViewById(R.id.tab_line);
        mTabLine.setVisibility(View.INVISIBLE);
        mContentViewpager = (ViewPager) fragmentView.findViewById(R.id.vp_content);
        addTopView();
        initTab();
        addContentViewPager();


        ImageView  search = (ImageView) fragmentView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("keyIsShow", true);
                startActivity(intent);
            }
        });

        return fragmentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCategoryView();
        initSwitcher();

        //网络请求
        loadingDialog = LoadingDialog.createLoadingDialog(getActivity(), "加载中");
        //LoadingDialog.showLoadingDialog(loadingDialog);
        //banner
        loadingBannerRequest = new LoadingBannerRequest();
        loadingBannerRequest.bannerRequest(this);
        //分类
        categoryRequest = new CategoryRequest();
        categoryRequest.categoryRequest(this);
        //获奖轮播
        winMessageRequest = new WinMessageRequest();
        winMessageRequest.winMessageRequest(this);

    }

    public void addTopView() {
        mTopView = View.inflate(getActivity(), R.layout.homeheadview, null);
        if (mTopView == null) {
            mTopContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.MarginLayoutParams.MATCH_PARENT, 1));
        } else {
            mTopContainer.removeAllViews();
            mTopContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.MarginLayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mTopContainer.addView(mTopView);
        }
        categroyClickViews = new ArrayList<LinearLayout>();
        categroyTextViews = new ArrayList<TextView>();
        categroyNetworkImageViews = new ArrayList<NetworkImageView>();

        bannerPager = (AutoScrollViewPager) mTopView.findViewById(R.id.bannerPager);
        bannerError = (TextView) mTopView.findViewById(R.id.bannerError);
        bannerError.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bannerError.setText(getResources().getString(R.string.loading));
                loadingBannerRequest.bannerRequest(HomeFragment2.this);
            }
        });

        toWin = (LinearLayout) mTopView.findViewById(R.id.toWin);
        toWin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Tag", "" + switcherPostion);
                if (switcherPostion == -1) {
                    winMessageRequest.winMessageRequest(HomeFragment2.this);
                } else {
                    Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                    WinMessageListBean winMessageListBean = winMessageListBeans.get(switcherPostion);
                    intent.putExtra("id", winMessageListBean.getId());
                    intent.putExtra("issue", winMessageListBean.getQishu());
                    startActivity(intent);
                }
            }
        });
    }


    private void addContentViewPager() {
        mFragmentList = initFragmentList();
        if (mFragmentList == null || mFragmentList.size() == 0)
            return;

        mContentViewpager.setOffscreenPageLimit(3);
        mContentViewpager.setAdapter(new ContentAdapter(getFragmentManager()));
        if (!noTab) {
            mSlidingTab.setViewPager(mContentViewpager);
            mTabLine.setVisibility(View.VISIBLE);
        }
        mScrollLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(0));
        mSlidingTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mScrollLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(position));
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupPullToRefresh();
    }



    private void setupPullToRefresh() {
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mScrollLayout.canPtr();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mFragmentList != null && mFragmentList.size() > 0) {
                    mFragmentList.get(mContentViewpager.getCurrentItem()).onRefreshing();
                }
            }
        });

        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
    }


    protected List<String> initTabTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("人气");
        titles.add("最新");
        titles.add("进度");
        titles.add("总需人数");
        return titles;
    }

    private void initTab() {
        mTitles = initTabTitles();
        if (mTitles == null || mTitles.size() == 0) {
            noTab = true;
            ViewGroup.LayoutParams params = mSlidingTab.getLayoutParams();
            params.height = 1;
            mSlidingTab.setLayoutParams(params);
        } else {
            noTab = false;
            mSlidingTab.setVisibility(View.VISIBLE);
        }
    }

    public PtrClassicFrameLayout getmPtrFrame(){

        return mPtrFrame;
    }
    protected List<BaseFragment> initFragmentList() {
        ArrayList<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HomeContentFragment homeContentFragment = new HomeContentFragment(handler1);
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putString("type", "renqi");
                    break;
                case 1:
                    bundle.putString("type", "zuixin");
                    break;
                case 2:
                    bundle.putString("type", "jindu");
                    break;
                case 3:
                    bundle.putString("type", "zongxurenci");
                    break;
            }
            bundle.putInt("position", i);
            if (noTab)
                bundle.putBoolean("no_tab", true);
            else
                bundle.putBoolean("no_tab", false);
            homeContentFragment.setArguments(bundle);
            fragmentList.add(homeContentFragment);
        }
        return fragmentList;
    }


    private void initSwitcher() {
        switcher = (TextSwitcher) mTopView.findViewById(R.id.textSwitcher);
        switcher.setFactory(this);
        switcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top));
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


    private void updateLoadState() {
        if (loadState == 4) {
            LoadingDialog.hideLoadingDialog(loadingDialog);
        } else {
            loadState = loadState + 1;
        }
    }



    public void onBannerFailed(VolleyError error) {
        bannerError.setText(getResources().getString(R.string.load_error));
        bannerError.setVisibility(View.VISIBLE);
       updateLoadState();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switchDot(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private void switchDot(int selectDotPosition) {
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot);
        currentDotPosition = selectDotPosition;
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot_selected);
    }


    private void initCategoryView() {
        categoryView = (LinearLayout) mTopView.findViewById(R.id.categoryView);
        for (int i = 0; i < categoryView.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) categoryView.getChildAt(i);
            categroyClickViews.add(child);
            categroyNetworkImageViews.add((NetworkImageView) child.getChildAt(0));
            categroyTextViews.add((TextView) child.getChildAt(1));
        }
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


    class ContentAdapter extends FragmentStatePagerAdapter {


        public ContentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);

        }
    }


    /*
     * 初始化banner对象
     */
    private void initBanner() {
        //将图片装载到数组中
        views = new ArrayList<View>();
        dots = new ArrayList<ImageView>();
        LinearLayout dots = (LinearLayout) mTopView.findViewById(R.id.dots);
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



            Map<String, String> map = new HashMap<String, String>();
            if (type == Constant.WebView) {
                map.put("title", bannerBean.getTitle());
                String url = getUrl(bannerBean.getLink());
                map.put("url", url);
                System.out.println("---------"+url);
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
    private String getUrl(String link){
        return link+"&"+new BaseRequest().getParams();
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

}
