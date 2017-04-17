package com.hongbaogou.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.BannerAdapter;
import com.hongbaogou.adapter.JoinPeopleAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BuyNumberBean;
import com.hongbaogou.bean.GoodsDetailBean;
import com.hongbaogou.bean.JoinPeopleBean;
import com.hongbaogou.bean.JoinPeopleObject;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.bean.WinBean;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnGoodsDetailListener;
import com.hongbaogou.listener.OnJoinPeopleListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.GoodsDetailRequest;
import com.hongbaogou.request.JoinPeopleRequest;
import com.hongbaogou.utils.DialogShareMenu;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.WinCountDownTime;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.ViewpagerInListView;
import com.hongbaogou.view.headviewpage.HeadViewPager;
import com.hongbaogou.view.loadmoregridview.LoadMoreContainer;
import com.hongbaogou.view.loadmoregridview.LoadMoreHandler;
import com.hongbaogou.view.loadmoregridview.LoadMoreListViewContainer;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

public class GoodsDetailActivity extends BaseAppCompatActivity implements OnJoinPeopleListener, AdapterView.OnItemClickListener, OnGoodsDetailListener, ViewPager.OnPageChangeListener, WinCountDownTime.OnWinCountDownListener, NetErrorView.OnReloadListener, OnAddShopCartListener, HeadViewPager.OnStartActivityListener, ViewpagerInListView.OnTouchInViewListener {

    private TextView listCount;

    private boolean isJump = false;
    /*
    * 下拉刷新空间
    */
    private PtrClassicFrameLayout ptrFrameLayout;
    private int pageNo = 1;
    private int currentDotPosition = 0;
    private List<View> views;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private LinearLayout goodsDetailHeadView;
    private ViewpagerInListView joinPeopleList;
    private JoinPeopleAdapter joinPeopleAdapter;
    //请求参与者消息
    private JoinPeopleRequest joinPeopleRequest;
    private Resources resources;
    /*
     * banner底部的选中的圆点对象
     */
    private List<ImageView> dots;
    //开奖状态
    private TextView state;
    //产品名称
    private TextView name;
    //期号
    private TextView goingIssue;

    private ProgressBar progress;
    //总人数和剩余人数布局
    private RelativeLayout totalAndremainder;
    //总人数
    private TextView total;
    //剩余人数
    private TextView remainder;
    //倒计时的布局对象
    private RelativeLayout countDownLayout;
    //剩余人数
    private TextView countDownIssue;
    //剩余人数
    private TextView countDownTime;
    //获奖布局的顶部布局
    private TextView win_top;
    //获奖布局的消息布局
    private RelativeLayout win_user_message;
    //获奖布局的底部布局
    private RelativeLayout win_bottom;

    //获奖产品的图片
    private NetworkImageView produceImage;
    //中奖者的名称
    private TextView win_name;
    //中奖者Id
    private TextView win_id;
    //期数
    private TextView win_issue;
    //中奖者购买次数
    private TextView win_jion_count;
    //中奖截止时间
    private TextView win_time;
    //中奖的号码
    private TextView win_number;
    private TextView noJoin;
    private TextView joinCount;
    private TableLayout luckNumbers;
    //投注号码对象
    private List<TextView> luckView;
    //点击查看更多号码
    private TextView moreLuckNumber;
    private HeadViewPager bannerPager;

    private NetErrorView netErrorView;
    private RelativeLayout bottomView;
    private ImageView shoppingcar;
    private TextView addToList;
    private TextView goodsAddList;
    private LinearLayout add;
    private TextView next;
    private TextView goNext;
    private TextView stop;
    private TextView startTime;
    private ImageView tenImage;
    //请求返回的商品消息
    private GoodsDetailBean goodsDetailBean;
    //请求商品详情
    private GoodsDetailRequest goodsDetailRequest;
    //加入购物车请求
    private AddShopCartRequest addShopCartRequest;
    private RelativeLayout rootView;

    private boolean isBannerInit = false;

    private String id;
    private String uid;
    private String issue;
    private ImageLoader imageLoader = RequestManager.getImageLoader();
    private ImageView share;
    private TextView maiman_time;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        issue = intent.getStringExtra("issue");
        uid = Pref_Utils.getString(this, "uid");
        Log.e("TAg", "=id============" + id);
        Log.e("TAg", "=issue===========" + issue);

        initView();
        initBarUtils.setSystemBar(this);

        goodsDetailRequest = new GoodsDetailRequest();
        goodsDetailRequest.goodsDetailRequest(id, uid, GoodsDetailActivity.this);
        joinPeopleRequest = new JoinPeopleRequest();

        addShopCartRequest = new AddShopCartRequest();
        ShareSDK.initSDK(this);
    }


    private void initView() {

        isJump = false;

        listCount = (TextView) findViewById(R.id.listCount);

        goodsDetailHeadView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.goodsdetailheadview, null);
        resources = getResources();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.activity_goods_detail);
        ImageView menuItem = (ImageView) findViewById(R.id.menuItem);
        menuItem.setVisibility(View.VISIBLE);
        menuItem.setImageResource(R.mipmap.back_home);
        menuItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });

        share=(ImageView)findViewById(R.id.share);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        state = (TextView) goodsDetailHeadView.findViewById(R.id.state);
        name = (TextView) goodsDetailHeadView.findViewById(R.id.name);
        goingIssue = (TextView) goodsDetailHeadView.findViewById(R.id.goingIssue);
        progress = (ProgressBar) goodsDetailHeadView.findViewById(R.id.progress);
        totalAndremainder = (RelativeLayout) goodsDetailHeadView.findViewById(R.id.totalAndremainder);
        total = (TextView) goodsDetailHeadView.findViewById(R.id.total);
        remainder = (TextView) goodsDetailHeadView.findViewById(R.id.remainder);
        startTime = (TextView) goodsDetailHeadView.findViewById(R.id.startTime);

        countDownLayout = (RelativeLayout) goodsDetailHeadView.findViewById(R.id.countDownLayout);
        countDownIssue = (TextView) goodsDetailHeadView.findViewById(R.id.countDownIssue);
        countDownTime = (TextView) goodsDetailHeadView.findViewById(R.id.countDownTime);
        tenImage = (ImageView) goodsDetailHeadView.findViewById(R.id.tenImage);

        win_top = (TextView) goodsDetailHeadView.findViewById(R.id.win_top);
        win_user_message = (RelativeLayout) goodsDetailHeadView.findViewById(R.id.win_user_message);
        win_bottom = (RelativeLayout) goodsDetailHeadView.findViewById(R.id.win_bottom);
        produceImage = (NetworkImageView) goodsDetailHeadView.findViewById(R.id.produceImage);
        win_name = (TextView) goodsDetailHeadView.findViewById(R.id.win_name);
        win_id = (TextView) goodsDetailHeadView.findViewById(R.id.win_id);
        win_issue = (TextView) goodsDetailHeadView.findViewById(R.id.win_issue);
        win_jion_count = (TextView) goodsDetailHeadView.findViewById(R.id.win_jion_count);
        win_time = (TextView) goodsDetailHeadView.findViewById(R.id.win_time);
       // maiman_time=(TextView)goodsDetailHeadView.findViewById(R.id.maiman_time);
        win_number = (TextView) goodsDetailHeadView.findViewById(R.id.win_number);

        noJoin = (TextView) goodsDetailHeadView.findViewById(R.id.no_join);
        joinCount = (TextView) goodsDetailHeadView.findViewById(R.id.joinCount);
        luckNumbers = (TableLayout) goodsDetailHeadView.findViewById(R.id.luckNumbers);

        luckView = new ArrayList<TextView>();
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber1));
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber2));
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber3));
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber4));
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber5));
        luckView.add((TextView) goodsDetailHeadView.findViewById(R.id.luckNumber6));
        moreLuckNumber = (TextView) goodsDetailHeadView.findViewById(R.id.moreLuckNumber);

        shoppingcar = (ImageView) findViewById(R.id.shoppingcar);
        addToList = (TextView) findViewById(R.id.addToList);
        goodsAddList = (TextView) findViewById(R.id.goods_addlist);

        next = (TextView) findViewById(R.id.next);
        goNext = (TextView) findViewById(R.id.goNext);
        stop = (TextView) findViewById(R.id.stop);
        add = (LinearLayout) findViewById(R.id.add);


        bottomView = (RelativeLayout) findViewById(R.id.bottomView);
        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);
        // 获取view的引用
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setShowLoadingForFirstPage(true);
        loadMoreListViewContainer.loadMoreFinish(false, true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                Log.d("pageNo", "" + pageNo);
                joinPeopleRequest.goodsListRequest(id, issue, pageNo, GoodsDetailActivity.this);
            }
        });

        joinPeopleList = (ViewpagerInListView) findViewById(R.id.joinPeopleList);
        joinPeopleList.setOnTouchInViewListener(this);
        joinPeopleList.addHeaderView(goodsDetailHeadView, null, false);
        joinPeopleAdapter = new JoinPeopleAdapter(this);
        joinPeopleList.setAdapter(joinPeopleAdapter);
        joinPeopleList.setOnItemClickListener(this);

        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = joinPeopleList.getChildCount() > 0 && (joinPeopleList.getFirstVisiblePosition() > 0 || joinPeopleList.getChildAt(0).getTop() < joinPeopleList.getPaddingTop());
                } else {
                    flag = joinPeopleList.canScrollVertically(-1);
                }
                return !flag;

            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        pageNo = 1;
                        loadMoreListViewContainer.loadMoreFinish(false, true);
                        goodsDetailRequest.goodsDetailRequest(id, uid, GoodsDetailActivity.this);
                    }
                }, 10);
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsDetailBean!=null) {
                    if(goodsDetailBean.getPicarr()!=null&&goodsDetailBean.getPicarr().length>0){
                        DialogShareMenu dsm = new DialogShareMenu(GoodsDetailActivity.this, null,goodsDetailBean.getPicarr()[0], goodsDetailBean.getTitle(), goodsDetailBean.getTitle2(),"http://v2.qcread.com/yungou/#/tab/productDetail?id="+goodsDetailBean.getId(),2);
                    }else{
                        DialogShareMenu dsm = new DialogShareMenu(GoodsDetailActivity.this, null,null, goodsDetailBean.getTitle(), goodsDetailBean.getTitle2(),"http://v2.qcread.com/yungou/#/tab/productDetail?id="+goodsDetailBean.getId(),2);
                    }
                }

            }
        });

    }

    /*
     * 显示进行中的视图
     */
    private void showGoingView() {
        goingIssue.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        totalAndremainder.setVisibility(View.VISIBLE);
    }

    /*
    * 显示中奖者的视图
    */
    private void showWinView() {
        win_top.setVisibility(View.VISIBLE);
        win_user_message.setVisibility(View.VISIBLE);
        win_bottom.setVisibility(View.VISIBLE);
    }


    public void onJoinPeopleSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            System.out.println("----------"+baseObjectBean.getData());
            JoinPeopleObject joinPeopleObject = (JoinPeopleObject) baseObjectBean.getData();
            if("0".equalsIgnoreCase(joinPeopleObject.getTotal())){
                loadMoreListViewContainer.loadMoreFinish(false, true);
            } else {
                List<JoinPeopleBean> loadData = joinPeopleObject.getList();
                if (loadData != null) {
                    joinPeopleAdapter.addData(loadData);
                    if (pageNo == Integer.parseInt(joinPeopleObject.getMax_page())) {
                        loadMoreListViewContainer.loadMoreFinish(false, false);
                    } else {
                        pageNo = pageNo + 1;
                        loadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                }
                loadData = null;
            }
            joinPeopleObject = null;
        } else {
            loadMoreListViewContainer.loadMoreError(0, "请求失败");
        }
    }

    public void onJoinPeopleFailed(VolleyError error) {
        loadMoreListViewContainer.loadMoreError(0, "请求失败");
    }


    /*------------------点击事件处理---------------------*/
    public void back(View v) {
        finish();
    }

    /*
     * 分享晒单
     */
    public void share(View v) {
        Log.v("Tag", "分享晒单");
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra("sid", goodsDetailBean.getSid());
        startActivity(intent);
    }

    /*
    * 往期揭晓
    */
    public void winAgo(View v) {
        Log.v("Tag", "往期揭晓");
        Intent intent = new Intent(this, WinAgoActivity.class);
        intent.putExtra("sid", goodsDetailBean.getSid());
        startActivity(intent);
    }

    /*
     * 查看图文详情
     */
    public void picture(View v) {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra("title", "图文详情");
        intent.putExtra("url", goodsDetailBean.getWap_link());
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CenterActivity.class);
        JoinPeopleBean joinPeopleBean = joinPeopleAdapter.getPositionObject(position - 1);
        intent.putExtra("username", joinPeopleBean.getUsername());
        intent.putExtra("headImg", joinPeopleBean.getUphoto());
        intent.putExtra("id", joinPeopleBean.getUid());
        intent.getBooleanExtra("isSelf", false);
        startActivity(intent);
    }

    /*
     * 计算详情
     */
    public void calculateDetial(View v) {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra("title", "计算详情");
        intent.putExtra("url", goodsDetailBean.getCalc_link());
        startActivity(intent);
    }

    /*
     * 查看更多号码
     */
    public void moreNumber(View v) {
        Intent intent = new Intent(this, SeeNumberDetailsActivity.class);
        intent.putExtra("titles", goodsDetailBean.getTitle());
        intent.putExtra("total", goodsDetailBean.getCurr_uinfo().getGonumber());
        intent.putExtra("id", id);
        intent.putExtra("uid", uid);
        intent.putExtra("qishu", issue);
        startActivity(intent);
    }

    /*
    * 查看中奖者信息
    */
    public void winMessage(View v) {
        Intent intent = new Intent(this, CenterActivity.class);
        WinBean winBean = goodsDetailBean.getQ_user();
        intent.putExtra("username", winBean.getUsername());
        intent.putExtra("headImg", winBean.getImg());
        intent.putExtra("id", winBean.getUid());
        intent.getBooleanExtra("isSelf", false);
        startActivity(intent);
    }


    /*
     * 时间截止状态的开奖号码
     */
    public void loadWinData(GoodsDetailBean goodsDetailBean) {
        WinBean winBean = goodsDetailBean.getQ_user();
        ImageLoader imageLoader = RequestManager.getImageLoader();
        produceImage.setImageUrl(winBean.getImg(), imageLoader);

        String username = winBean.getUsername();
        String win = resources.getString(R.string.luck_name, username);
        SpannableStringBuilder usernameBuilder = new SpannableStringBuilder(win);
        ForegroundColorSpan userColor = new ForegroundColorSpan(resources.getColor(R.color.win_username));
        usernameBuilder.setSpan(userColor, win.length() - username.length(), win.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        win_name.setText(usernameBuilder);

        win_id.setText(resources.getString(R.string.luck_id, winBean.getUid()));
        win_issue.setText(resources.getString(R.string.luck_issue, goodsDetailBean.getQishu()));

        String count = String.valueOf(winBean.getRenci());
        String str = resources.getString(R.string.luck_jion_count, count);
        SpannableStringBuilder jointCountBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resources.getColor(R.color.detail_color));
        jointCountBuilder.setSpan(joinCountColor, 5, 5 + count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        win_jion_count.setText(jointCountBuilder);

        win_time.setText(resources.getString(R.string.luck_time, goodsDetailBean.getQ_end_time()));
        win_number.setText(resources.getString(R.string.luck_number, goodsDetailBean.getQ_user_code()));
        //maiman_time.setText("买满时间："+goodsDetailBean.getMtime());
    }

    /*
     * 购买号码的处理
     */
    private void buyNumberData(BuyNumberBean buyNumberBean) {
        int buyCount = buyNumberBean.getGonumber();
        String count = String.valueOf(buyCount);
        String str = resources.getString(R.string.join_self, count);
        SpannableStringBuilder selfJointCountBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resources.getColor(R.color.detail_color));
        selfJointCountBuilder.setSpan(joinCountColor, 5, 5 + count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinCount.setText(selfJointCountBuilder);

        String[] buyNumbers = buyNumberBean.getGoucode().split(",");
        for (int i = 0; i < buyNumbers.length; i++) {
            TextView numberView = luckView.get(i);
            numberView.setVisibility(View.VISIBLE);
            numberView.setText(buyNumbers[i]);
        }

        if (buyCount > buyNumbers.length) {
            moreLuckNumber.setVisibility(View.VISIBLE);
        } else {
            moreLuckNumber.setVisibility(View.GONE);
        }
    }

    /*
     * 进行中的数据处理
     */
    private void LoadGoingData(GoodsDetailBean goodsDetailBean) {
        goingIssue.setText(getString(R.string.issue, goodsDetailBean.getQishu()));
        int canyurenshu = Integer.parseInt(goodsDetailBean.getCanyurenshu());
        int zongrenshu = Integer.parseInt(goodsDetailBean.getZongrenshu());
        progress.setMax(zongrenshu);
        progress.setProgress(canyurenshu);
        total.setText(getString(R.string.total, zongrenshu));

        String str = resources.getString(R.string.residue, zongrenshu - canyurenshu);
        SpannableStringBuilder remainderBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resources.getColor(R.color.win_username));
        remainderBuilder.setSpan(joinCountColor, 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        remainder.setText(remainderBuilder);
    }

    /*
     * 进行中的底部的处理
     */
    private void goingBottomView() {
        shoppingcar.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        addToList.setVisibility(View.VISIBLE);
        goodsAddList.setVisibility(View.VISIBLE);

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = Pref_Utils.getString(GoodsDetailActivity.this, "uid");

                currentBuy = currentBuy == 0 ? 10 : currentBuy;//控制加入购物车数量

                addShopCartRequest.AddShopCartRequest(uid, String.valueOf(currentBuy), goodsDetailBean.getId(), GoodsDetailActivity.this);

                Intent intent = new Intent(GoodsDetailActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("position", 3);
                startActivity(intent);
            }
        });
    }

    /*
     * 过期的底部的处理
     */
    private void nextIssue() {
        next.setVisibility(View.VISIBLE);
        goNext.setVisibility(View.VISIBLE);
    }

    /*
     * 倒计时状态的处理
     */
    private void countDownTimeData(GoodsDetailBean goodsDetailBean) {
        long residueTime = goodsDetailBean.getSurplus();
        WinCountDownTime winCountDownTime = new WinCountDownTime(residueTime, 10);
        winCountDownTime.setOnWinCountDownListener(this);
        winCountDownTime.start();
    }

    private void hideAllView() {
        goingIssue.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        totalAndremainder.setVisibility(View.GONE);
        win_top.setVisibility(View.GONE);
        win_user_message.setVisibility(View.GONE);
        win_bottom.setVisibility(View.GONE);
        countDownLayout.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        shoppingcar.setVisibility(View.GONE);
        addToList.setVisibility(View.GONE);
        goodsAddList.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        goNext.setVisibility(View.GONE);
    }

    /*
      * 倒计时截止时的处理
      */
    public void onFinish() {
        pageNo = 1;

        loadMoreListViewContainer.loadMoreFinish(false, true);
        countDownTime.setText(getResources().getString(R.string.calculate));
        goodsDetailRequest.goodsDetailRequest(id, uid, GoodsDetailActivity.this);
    }

    /*
     * 倒计时时间变化的处理
     */
    public void onTimeChange(long time) {

        System.out.println("-------onTick(time)------"+time);

        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
        String data = formatter.format(new Date(time));
        SpannableString msp = new SpannableString(getString(R.string.countdown_time, data.substring(0, data.length() - 1)));
        //第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素。
        msp.setSpan(new AbsoluteSizeSpan(18, true), msp.length() - data.length(), msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        countDownTime.setText(msp);
    }

    public void onGoodsDetailSuccess(BaseObjectBean baseObjectBean) {
        Log.d("tag", baseObjectBean.toString());
        ptrFrameLayout.refreshComplete();
        if (baseObjectBean.getStatus() == 1) {

            hideAllView();
            bottomView.setVisibility(View.VISIBLE);
            netErrorView.loadSuccess();

            goodsDetailBean = (GoodsDetailBean) baseObjectBean.getData();

            //判断是否是10元专区 1表示是 0 否
            if (goodsDetailBean.getIs_ten() == 1) {
                tenImage.setVisibility(View.VISIBLE);
            } else {
                tenImage.setVisibility(View.GONE);
            }


            String time = goodsDetailBean.getStart_time();
            String str = resources.getString(R.string.win_all_people, time);
            SpannableString msp = new SpannableString(str);
            //第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素。
            msp.setSpan(new AbsoluteSizeSpan(12, true), 8, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resources.getColor(R.color.join_time));
            msp.setSpan(joinCountColor, 8, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startTime.setText(msp);


            if (!isBannerInit) {
                initBanner(goodsDetailBean.getPicarr());
            }
//            name.setText(goodsDetailBean.getTitle());

            SpannableString msp2 = new SpannableString(goodsDetailBean.getTitle()+goodsDetailBean.getTitle2());
//            //第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素。
//            msp2.setSpan(new AbsoluteSizeSpan(14, true), 0, goodsDetailBean.getTitle2().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan joinCountColor2 = new ForegroundColorSpan(resources.getColor(R.color.color_red));
            msp2.setSpan(joinCountColor2, goodsDetailBean.getTitle().length(), goodsDetailBean.getTitle().length() + goodsDetailBean.getTitle2().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            name.setText(goodsDetailBean.getTitle()+msp2);
            name.setText(msp2);

            BuyNumberBean buyNumberBean = goodsDetailBean.getCurr_uinfo();
            //1参加，0未参加
            int isJoin = buyNumberBean.getIs_join();
            if (isJoin == 1) {
                luckNumbers.setVisibility(View.VISIBLE);
                joinCount.setVisibility(View.VISIBLE);
                noJoin.setVisibility(View.GONE);
                buyNumberData(buyNumberBean);
            } else {
                luckNumbers.setVisibility(View.GONE);
                joinCount.setVisibility(View.GONE);
                noJoin.setVisibility(View.VISIBLE);
            }
            //0已揭晓、1进行中、2倒计时,3商品下架
            int tag = goodsDetailBean.getTag();
            if (tag == 0) {
                loadWinData(goodsDetailBean);
                state.setText("已揭晓");
                listCount.setVisibility(View.GONE);
                nextIssue();
                showWinView();
            } else if (tag == 1) {
                showGoingView();
                state.setText("进行中");
                listCount.setVisibility(View.GONE);
                goingBottomView();
                LoadGoingData(goodsDetailBean);


            } else if (tag == 2) {
                countDownLayout.setVisibility(View.VISIBLE);
                state.setText("倒计时");
                listCount.setVisibility(View.GONE);
                nextIssue();
                countDownIssue.setText(getResources().getString(R.string.issue, goodsDetailBean.getQishu()));
                countDownTimeData(goodsDetailBean);
            } else if (tag == 3) {
                stop.setVisibility(View.VISIBLE);
                loadWinData(goodsDetailBean);
                state.setText("已揭晓");
                nextIssue();
                listCount.setVisibility(View.GONE);
                showWinView();
            } else {
                Log.v("Tag", "状态错误,请检查服务器");
            }
            joinPeopleAdapter.clear();
        } else {
            netErrorView.loadFail();
        }
    }

    public void onGoodsDetailFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        netErrorView.loadFail();
    }


    /*
    * 初始化banner对象
    */
    private void initBanner(String[] pictures) {
        //将图片装载到数组中
        views = new ArrayList<View>();
        dots = new ArrayList<ImageView>();
        LinearLayout dotLayout = (LinearLayout) goodsDetailHeadView.findViewById(R.id.dots);
        for (int i = 0; i <= pictures.length; i++) {
            if (i != pictures.length) {
                addDot(i, dotLayout);
                NetworkImageView networkImageView = new com.android.volley.toolbox.NetworkImageView(this);
                networkImageView.setImageUrl(pictures[i], imageLoader);
                views.add(networkImageView);
            } else {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.last_viewpager, null);
                views.add(v);
            }
        }
        bannerPager = (HeadViewPager) goodsDetailHeadView.findViewById(R.id.bannerPager);
        BannerAdapter bannerAdapter = new BannerAdapter(views);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.addOnPageChangeListener(this);
        bannerPager.setOnStartActivityListener(this);
        isBannerInit = true;
    }

    /*
    *  添加banner的圆点
    */
    private void addDot(int position, LinearLayout dotLayout) {
        ImageView imageView = new ImageView(this);
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

    private void switchDot(int selectDotPosition) {
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot);
        currentDotPosition = selectDotPosition;
        dots.get(currentDotPosition).setImageResource(R.mipmap.ad_dot_selected);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        int size = views.size() - 1;
        if (position < size) {
            switchDot(position);
        } else {
            bannerPager.setCurrentItem(size - 1);
        }
    }

    public void onPageScrollStateChanged(int state) {

    }

    /*
     * 请求失败重新加载数据
     */
    public void onReload() {
        goodsDetailRequest.goodsDetailRequest(id, uid, GoodsDetailActivity.this);
    }


    public void goNewIssue(View v) {
        finish();
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("id", goodsDetailBean.getNext_id());
        intent.putExtra("issue", goodsDetailBean.getQishu());
        startActivity(intent);
    }

    //加入购物车点击事件
    public void addToShoppingCart(View v) {
        Intent intent = new Intent(GoodsDetailActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("position", 3);
        startActivity(intent);
    }


    public void onAddShopCartSuccess(MessageCountBean messageCountBean) {
        if (messageCountBean.getStatus() == 1) {
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", messageCountBean.getData());
            sendBroadcast(intent);

            ToastUtil.showToast(this, messageCountBean.getMessage());
        } else {
            ToastUtil.showToast(this, messageCountBean.getMessage());
        }
    }


    public void onAddShopCartFailed(VolleyError error) {
        ToastUtil.showToast(this, R.string.addlistfail);
    }

    private PopupWindow window;
    private EditText buy;
//    private int currentBuy = 10;
    private int currentBuy = 1;
    private int height;

    public void showPopwindow(View v) {
        final View view = getLayoutInflater().inflate(R.layout.goods_detail_popupwindow, null);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (window.isShowing() && window != null) {
                    window.dismiss();
//                    currentBuy = 10;
//                    currentBuy = 1;
                }
            }
        });

        buy = (EditText) view.findViewById(R.id.buy);

//        if (goodsDetailBean.getIs_ten() == 1) {
//
//            buy.setText("10");
//        }else {
//            buy.setText("1");
//        }

        ImageView minus = (ImageView) view.findViewById(R.id.minus);
        ImageView add = (ImageView) view.findViewById(R.id.add);
        Button addToList = (Button) view.findViewById(R.id.addToList);

        final int remainder = Integer.parseInt(goodsDetailBean.getZongrenshu()) - Integer.parseInt(goodsDetailBean.getCanyurenshu());
        minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = buy.getText().toString();

                //10元专区法则：
                if (goodsDetailBean.getIs_ten() == 1) {
                    int length = number.length();
                    if (length == 1 || length == 0) {
                        currentBuy = 0;
                    } else {
                        number = number.substring(0, number.length() - 1) + "0";
                        currentBuy = Integer.parseInt(number);
                    }
                } else {
                    currentBuy = Integer.parseInt(number);
                }


                if (goodsDetailBean.getIs_ten() == 1) {
                    if (currentBuy > remainder) {
                        currentBuy = remainder;
                    } else if (currentBuy > 10) {
                        currentBuy = currentBuy - 10;
                    } else {
                        currentBuy = 10;
                    }
                } else {
                    if (currentBuy > remainder) {
                        currentBuy = remainder;
                    } else if (currentBuy > 1) {
                        currentBuy = currentBuy - 1;
                    } else {
//                        currentBuy = 10;
                        currentBuy = 1;
                    }
                }
                buy.setText(String.valueOf(currentBuy));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = buy.getText().toString();

                //10元专区法则：
                if (goodsDetailBean.getIs_ten() == 1) {
                    int length = number.length();
                    if (length == 1 || length == 0) {
                        currentBuy = 0;
                    } else {
                        number = number.substring(0, number.length() - 1) + "0";
                        currentBuy = Integer.parseInt(number);
                    }
                } else {
                    currentBuy = Integer.parseInt(number);
                }

                if (goodsDetailBean.getIs_ten() == 1) {
                    if (currentBuy < remainder) {
                        currentBuy = currentBuy + 10;
                    } else {
                        currentBuy = remainder;
                    }
                } else {
                    if (currentBuy < remainder) {
                        currentBuy = currentBuy + 1;
                    } else {
                        currentBuy = remainder;
                    }
                }
                buy.setText(String.valueOf(currentBuy));
            }
        });


        buy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buy.selectAll();
                height = view.getHeight();
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                //比较Activity根布局与当前布局的大小
                int heightDiff = height - view.getHeight();
                if (heightDiff < 100) {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                    String number = buy.getText().toString();
                    if (number.trim().length() == 0) {
//                        currentBuy = 10;
                        currentBuy = 1;
                    } else {
                        int buyCount = Integer.parseInt(number);
                        if (buyCount == 0) {
                            currentBuy = 1;
                        } else if (buyCount > remainder) {
                            currentBuy = remainder;
                        } else {
                            currentBuy = buyCount;
                        }
                    }
                }
            }

        });

        addToList.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (window.isShowing() && window != null) {
                    window.dismiss();
//                    currentBuy = 10;
//                    currentBuy = 1;
                }
                String uid = Pref_Utils.getString(GoodsDetailActivity.this, "uid");
                currentBuy = currentBuy == 0 ? 10 : currentBuy;
                addShopCartRequest.AddShopCartRequest(uid, String.valueOf(currentBuy), goodsDetailBean.getId(), GoodsDetailActivity.this);

            }
        });


        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.pop_anim_style);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 在底部显示
        window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.window).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        window.dismiss();
//                        currentBuy = 10;
//                        currentBuy = 1;
                    }
                }
                return true;
            }
        });
    }

    //滑动跳转
    public void toStart(int position) {
        bannerPager.setCurrentItem(position);
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra("title", "图文详情");
        intent.putExtra("url", goodsDetailBean.getWap_link());
        startActivity(intent);
    }

    public View getTouchView() {
        return bannerPager;
    }

}
