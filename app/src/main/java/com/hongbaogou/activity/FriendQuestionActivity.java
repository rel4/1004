package com.hongbaogou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;

public class FriendQuestionActivity extends BaseAppCompatActivity {
    private ImageView iv_friendquestion_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_question);
        initBarUtils.setSystemBar(this);
        initView();
        initBarUtils.setSystemBar(this);
    }

    private void initView() {
        iv_friendquestion_back=(ImageView) findViewById(R.id.iv_friendquestion_back);
    }

    public void onClick(View view) {
        finish();
    }
}
