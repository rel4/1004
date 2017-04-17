package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanShareDetails;
import com.hongbaogou.listener.OnShareDetailsListener;
import com.hongbaogou.request.ShareDetailsRequest;
import com.hongbaogou.utils.PopWindowUtils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;

/**
 * 晒单详情界面
 */
public class ShareSelfActivity extends BaseAppCompatActivity implements View.OnClickListener, OnShareDetailsListener {

    private TextView title;
    private TextView name;
    private TextView time;
    private TextView goods;
    private TextView qishu;
    private TextView totalPeople;
    private TextView luckNumber;
    private TextView endTime;
    private TextView comment;

    private NetworkImageView img_1;
    private NetworkImageView img_2;
    private NetworkImageView img_3;
    private NetworkImageView img_4;
    private NetworkImageView img_5;
    private NetworkImageView img_6;
    private NetworkImageView img_7;
    private NetworkImageView img_8;

    //退出的按钮
    private ImageView mBackbtn;
    //分享的按钮
    private ImageView mSharebtn;
    //更多的按钮
    private ImageView mMorebtn;

    private ImageLoader mImageLoader;

    public ShareSelfActivity() {
        mImageLoader = RequestManager.getImageLoader();
    }

    /**
     * javaBean
     */
    private BeanShareDetails bean;

    /**
     * 设置网络异常的时候显示的view
     */
    private NetErrorView netErrorView;

    private String sd_id;
    private String uid;
    private String iconimg;
    private String username;
    private String issue;
    private String shopid;
    private ShareDetailsRequest shareDetailsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_self);

        shopid = getIntent().getStringExtra("shopid");
        sd_id = getIntent().getStringExtra("sd_id");
        uid = getIntent().getStringExtra("uid");
        issue = getIntent().getStringExtra("issue");

        iconimg = getIntent().getStringExtra("headImg");
        username = getIntent().getStringExtra("username");

        findAllView();
        requestNetData();
        initBarUtils.setSystemBar(this);
    }


    /**
     * 请求网络数据
     */
    private void requestNetData() {
        shareDetailsRequest = new ShareDetailsRequest();
        shareDetailsRequest.requestShareDetails(this, sd_id);
        Log.d("TAG", sd_id + "------------------");
    }

    private void findAllView() {

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);

        mBackbtn = (ImageView) findViewById(R.id.btn_back);
        mBackbtn.setOnClickListener(this);

        mMorebtn = (ImageView) findViewById(R.id.btn_more);
        mMorebtn.setOnClickListener(this);

        mSharebtn = (ImageView) findViewById(R.id.btn_share);
        mSharebtn.setOnClickListener(this);


        title = (TextView) findViewById(R.id.sd_title);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        goods = (TextView) findViewById(R.id.titlegoods);
        qishu = (TextView) findViewById(R.id.qishu);
        totalPeople = (TextView) findViewById(R.id.totalpeople);
        luckNumber = (TextView) findViewById(R.id.lucknumber);
        endTime = (TextView) findViewById(R.id.q_end_time);
        comment = (TextView) findViewById(R.id.comments);

        img_1 = (NetworkImageView) findViewById(R.id.img_1);
        img_2 = (NetworkImageView) findViewById(R.id.img_2);
        img_3 = (NetworkImageView) findViewById(R.id.img_3);
        img_4 = (NetworkImageView) findViewById(R.id.img_4);
        img_5 = (NetworkImageView) findViewById(R.id.img_5);
        img_6 = (NetworkImageView) findViewById(R.id.img_6);
        img_7 = (NetworkImageView) findViewById(R.id.img_7);
        img_8 = (NetworkImageView) findViewById(R.id.img_8);

        //点击布局调转到商品详情
        findViewById(R.id.goods_details).setOnClickListener(this);

        //点击姓名跳转个人中心
        name.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            //点击更多
            case R.id.btn_more:
                View popLayout = PopWindowUtils.initPopwindowLayout(this);
                popLayout.findViewById(R.id.action_home).setOnClickListener(this);
                popLayout.findViewById(R.id.action_list).setOnClickListener(this);
                PopupWindow window = PopWindowUtils.creatPopuptWindow(popLayout);
                window.showAsDropDown(v);
                break;
            case R.id.btn_share:
                //TODO 第三方分享
                break;
            case R.id.action_list:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
            case R.id.action_home:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.goods_details:
                //TODO 跳转到商品详情
                intent.setClass(this, GoodsDetailActivity.class);
                intent.putExtra("id", shopid);
                intent.putExtra("issue", issue);
                startActivity(intent);
                break;
            case R.id.name:
                //TODO 跳转到个人中心
                intent.setClass(this, CenterActivity.class);
                intent.putExtra("id", uid);
                intent.putExtra("headImg", iconimg);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            default:
                break;
        }
        //    BaseUtils.colseSoftKeyboard(this);
    }

    @Override
    public void requestShareDetailsSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            netErrorView.setVisibility(View.GONE);
            //请求到数据直接设置UI
            bean = (BeanShareDetails) baseObjectBean.getData();
            setData2UI(bean);
        }
    }

    private void setData2UI(BeanShareDetails bean) {
        //设置晒单的title
        title.setText(bean.getSd_title());
        //设置晒单者名称
        name.setText(bean.getUsername());
        //设置晒单的时间
        time.setText(bean.getSd_time());
        time.setTextColor(getResources().getColor(R.color.issue_tx_color));

        //设置商品的title
        String str = "获奖商品 : " + bean.getTitle();
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.issue_tx_color)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_blue)), 7, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        goods.setText(ssb);

        //设置期数
        qishu.setText("参与期数 : " + bean.getSd_qishu());
        //设置期数的字体颜色
        qishu.setTextColor(getResources().getColor(R.color.issue_tx_color));


        //设置总参与人数
        str = "本期参与 : " + bean.getCanyurenshu() + " 人次";
        ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.issue_tx_color)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.join_text)), str.length() - 2, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        totalPeople.setTextColor(getResources().getColor(R.color.color_red));
        totalPeople.setText(ssb);

        //设置幸运号码
        str = "幸运号码 : " + bean.getQ_user_code();
        ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.issue_tx_color)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_red)), 7, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        luckNumber.setText(ssb);
        //设置揭晓事件
        endTime.setText("揭晓时间 : " + bean.getQ_end_time());
        endTime.setTextColor(getResources().getColor(R.color.issue_tx_color));

        //设置评论内容
        comment.setText(bean.getSd_content());

        //获取晒单图片的数组
        String res[] = bean.getImg();
        Log.e("TAG", "晒单详情中一共有 " + res.length + " 张图片!");

        if (res.length >= 1) {
            img_1.setImageUrl(res[0], mImageLoader);
            img_1.setAdjustViewBounds(true);
            if (res.length >= 2) {
                img_2.setImageUrl(res[1], mImageLoader);
                img_2.setAdjustViewBounds(true);
                if (res.length >= 3) {
                    img_3.setImageUrl(res[2], mImageLoader);
                    img_3.setAdjustViewBounds(true);
                    if (res.length >= 4) {
                        img_4.setImageUrl(res[3], mImageLoader);
                        img_4.setAdjustViewBounds(true);
                        if (res.length >= 5) {
                            img_5.setImageUrl(res[4], mImageLoader);
                            img_5.setAdjustViewBounds(true);
                            if (res.length >= 6) {
                                img_6.setImageUrl(res[5], mImageLoader);
                                img_6.setAdjustViewBounds(true);
                                if (res.length >= 7) {
                                    img_7.setImageUrl(res[6], mImageLoader);
                                    img_7.setAdjustViewBounds(true);
                                    if (res.length >= 8) {
                                        img_8.setImageUrl(res[7], mImageLoader);
                                        img_8.setAdjustViewBounds(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void requestShareDetailsFailed(VolleyError error) {
        netErrorView.loadFail();
    }
}
