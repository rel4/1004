package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

/**
 * 晒单须知的界面
 */
public class ShareKnowActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView btnimg;
    private TextView title;

    private TextView goshare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_know);

        getData();

        initView();

        initBarUtils.setSystemBar(this);
    }

    /**
     * 获取传入的数据
     */
    private String uid;
    private String qishu;
    private String shopid;
    private String shopsid;
    private String titles;
    private String goodstitle;
    private String content;
    private String endtime;
    private String lucknumber;
    private String gonumber;

    private void getData() {
        gonumber = getIntent().getStringExtra("gonumber");
        uid = getIntent().getStringExtra("uid");
        qishu = getIntent().getStringExtra("qishu");
        shopid = getIntent().getStringExtra("shopid");
        shopsid = getIntent().getStringExtra("shopsid");
        Log.e("TAG", "SHOPID = " + shopsid);
        titles = getIntent().getStringExtra("title");
        goodstitle = getIntent().getStringExtra("goodstitle");
        content = getIntent().getStringExtra("content");
        lucknumber = getIntent().getStringExtra("lucknumber");
        endtime = getIntent().getStringExtra("endtime");
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.shareknow);

        btnimg = (ImageView) findViewById(R.id.btn_back);
        btnimg.setOnClickListener(this);

        goshare = (TextView) findViewById(R.id.go_share);
        goshare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
               // BaseUtils.colseSoftKeyboard(this);
                break;
            case R.id.go_share:
                //调转到发布晒单的界面
                Intent intent = new Intent(this, IssueShareActivity.class);
                //传入uid
                intent.putExtra("uid", uid);
                //传入期数
                intent.putExtra("qishu", qishu);
                //shopid
                intent.putExtra("shopid", shopid);
                //shopsid
                intent.putExtra("shopsid", shopsid);
                //传入title
                intent.putExtra("title", titles);
                //传入商品的title
                intent.putExtra("goodstitle", goodstitle);
                //传入晒单的内容
                intent.putExtra("content", content);
                //传入幸运号
                intent.putExtra("lucknumber", lucknumber);
                //传入揭晓的事件
                intent.putExtra("endtime", endtime);
                //传入中奖者的参与人次
                intent.putExtra("gonumber", gonumber);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
