package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.NumberAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanNumber;
import com.hongbaogou.bean.BeanNumberList;
import com.hongbaogou.listener.OnAllNumberDataListener;
import com.hongbaogou.request.NumberRequest;
import com.hongbaogou.utils.PopWindowUtils;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;

import java.util.List;

/**
 * 奪寶詳情的界面
 */
public class SeeNumberActivity extends BaseAppCompatActivity implements OnAllNumberDataListener, View.OnClickListener {


    private ImageView mBntBack;
    private ImageView mBtnMore;

    private String uid;
    private String qishu;
    private String shopid;

    private NetErrorView netErrorView;
    private ListView mListView;
    private NumberAdapter mAdapter;
    private TextView title;
    private ImageView menu;

    private TextView tv_title;
    private TextView tv_qihao;
    private TextView tv_popnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more_numbers);

        uid = getIntent().getStringExtra("uid");
        shopid = getIntent().getStringExtra("id");
        qishu = getIntent().getStringExtra("qishu");


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("正在加载数据,请等候...");
        tv_title.setTextSize(24);
        tv_qihao = (TextView) findViewById(R.id.tv_qihao);
        tv_popnumber = (TextView) findViewById(R.id.tv_popnumber);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.detail_duobao);

        menu = (ImageView) findViewById(R.id.menuItem);
        menu.setImageResource(R.mipmap.btn_more);

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);

        mBntBack = (ImageView) findViewById(R.id.btn_back);
        mBntBack.setOnClickListener(this);
        mBtnMore = (ImageView) findViewById(R.id.menuItem);

        mBtnMore.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.morenumber_listview);

        mAdapter = new NumberAdapter(this, uid);

        mListView.setAdapter(mAdapter);
        //请求网络数据
        requestNet();

        initBarUtils.setSystemBar(this);

    }

    /**
     * 请求网络
     */
    private void requestNet() {
        NumberRequest numberRequest = new NumberRequest();
        numberRequest.requestNumber(this, shopid, qishu, uid);
    }

    @Override
    public void requestAllNumberDataSuccess(BaseObjectBean baseObjectBean) {

        if (baseObjectBean.getStatus() == 1) {
            netErrorView.setVisibility(View.GONE);
            BeanNumber number = (BeanNumber) baseObjectBean.getData();

            List<BeanNumberList> list = number.getList();
            tv_title.setTextSize(16);
            tv_title.setText(number.getShopname());
            tv_qihao.setText("期号 : " + number.getQishu());
            tv_qihao.setTextColor(getResources().getColor(R.color.color_gray));

            //设置人数
            String s = "本期参与了 " + number.getRenci() + " 人次,以下是商品获得者的所有号码";
            SpannableStringBuilder ssb = new SpannableStringBuilder(s);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), s.length() - 16, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_popnumber.setTextColor(getResources().getColor(R.color.color_red));

            tv_popnumber.setText(ssb);

            mAdapter.addData(list);
        }
    }

    @Override
    public void requestAllNumberDataFailed(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
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
                intent.setClass(this, MainActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            default:
                break;
        }
      //  BaseUtils.colseSoftKeyboard(this);
    }
}
