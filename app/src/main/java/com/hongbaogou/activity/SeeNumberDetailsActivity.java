package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.SeeNumberAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanSeeNumber;
import com.hongbaogou.listener.OnAllNumberDataListener;
import com.hongbaogou.request.SeeNumberRequest;
import com.hongbaogou.utils.PopWindowUtils;
import com.hongbaogou.utils.initBarUtils;

/**
 * 查看Ta的号码,或查看更多
 */
public class SeeNumberDetailsActivity extends BaseAppCompatActivity implements OnAllNumberDataListener, View.OnClickListener {

    private GridView mGridView;
    private SeeNumberAdapter mAdapter;
    private SeeNumberRequest seeNumberRequest;
    private String id;
    private String uid;
    private String qishu;

    private TextView tv_title;
    private TextView tv_qihao;
    private TextView tv_popnumber;

    private String rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_number_details);

        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        qishu = getIntent().getStringExtra("qishu");

        rid = getIntent().getStringExtra("rid");
        Log.e("TAG", "onCreate---rid = " + rid);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("正在加载数据,请等候...");
        tv_title.setTextSize(24);
        tv_qihao = (TextView) findViewById(R.id.tv_qihao);
        tv_popnumber = (TextView) findViewById(R.id.tv_popnumber);

        //初始化view
        findAllView();
        //请求网络数据
        requestNetData();

        initBarUtils.setSystemBar(this);
    }

    private void requestNetData() {
        seeNumberRequest = new SeeNumberRequest();
        if (rid != null) {
            seeNumberRequest.requestSeeNumber(this, rid);
        } else {
            seeNumberRequest.requestSeeNumber(this, id, uid, qishu);
        }
    }

    private void findAllView() {

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.menuItem).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.number_duobao);

        ImageView imageView = (ImageView) findViewById(R.id.menuItem);
        imageView.setImageResource(R.mipmap.btn_more);
        mAdapter = new SeeNumberAdapter(this);
        mGridView = (GridView) findViewById(R.id.see_number_gridview);
        mGridView.setAdapter(mAdapter);

    }

    /**
     * 请求成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestAllNumberDataSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {//请求成功
            BeanSeeNumber seeNumber = (BeanSeeNumber) baseObjectBean.getData();

            tv_title.setTextColor(getResources().getColor(R.color.black));
            tv_title.setTextSize(16);
            tv_title.setText(seeNumber.getShopname());

            tv_qihao = (TextView) findViewById(R.id.tv_qihao);
            tv_qihao.setText("期号 : " + seeNumber.getQishu());
            tv_qihao.setTextColor(getResources().getColor(R.color.color_gray));

            tv_popnumber = (TextView) findViewById(R.id.tv_popnumber);
            String s = "本期参与了 " + seeNumber.getGonumber() + " 人次,以下是商品获得者的所有号码";
            SpannableStringBuilder ssb = new SpannableStringBuilder(s);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), s.length() - 16, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_popnumber.setTextColor(getResources().getColor(R.color.color_red));
            tv_popnumber.setText(ssb);
            mAdapter.addData(seeNumber);
        }
    }

    /**
     * 请求失败的回调
     *
     * @param error
     */
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
