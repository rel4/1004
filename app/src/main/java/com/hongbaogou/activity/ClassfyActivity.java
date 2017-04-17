package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.ClasscyAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanClassfy;
import com.hongbaogou.listener.OnClassfyListener;
import com.hongbaogou.request.ClasscyDataRequest;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;

public class ClassfyActivity extends BaseAppCompatActivity implements OnClassfyListener, View.OnClickListener, AdapterView.OnItemClickListener, NetErrorView.OnReloadListener {

    //title上的后退按钮
    private Button mBtn_Back;
    //头部view布局
    private RelativeLayout headview;

    private RelativeLayout mLayoutSearch_btn;
    //listview的适配器
    private ClasscyAdapter mAdapter;
    //布局解析对象
    private LayoutInflater layoutInflater;
    //展示的list
    private ListView mListView;
    //listview头部对象
    private View mHeadView;
    private TextView title;
    private NetErrorView netErrorView;
    private ClasscyDataRequest classcyDataRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classfy);
        //找控件
        findAllView();
        //获取布局解析对象
        layoutInflater = getLayoutInflater();
        //解析listview头部布局
        mHeadView = layoutInflater.inflate(R.layout.classfy_listview_head, null);

        headview = (RelativeLayout) mHeadView.findViewById(R.id.classfy_handview);
        headview.setOnClickListener(this);
        mHeadView.findViewById(R.id.classfy_tv).setOnClickListener(this);
        mLayoutSearch_btn = (RelativeLayout) mHeadView.findViewById(R.id.relativeLayout_search);
        mLayoutSearch_btn.setOnClickListener(this);

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        mListView = (ListView) findViewById(R.id.classfy_listview);
        mListView.setOnItemClickListener(this);
        //添加头部
        mListView.addHeaderView(mHeadView);

        //设置加载异常时候显示的view
        mListView.setEmptyView(netErrorView);

        mAdapter = new ClasscyAdapter(this);
        //设置适配器
        mListView.setAdapter(mAdapter);

        //请求网络数据
        requestNetData();
        initBarUtils.setSystemBar(this);
    }

    /**
     * 寻找所有的控件
     */
    private void findAllView() {

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.title_classfybrowse);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }

    /**
     * 请求网络数据
     */
    private void requestNetData() {

        classcyDataRequest = new ClasscyDataRequest();
        classcyDataRequest.dataRequest(this);

    }

    /**
     * 成功请求的回调
     */
    public void requestClassfyDataSuccess(BaseListBean baseListBean) {

        if (baseListBean.getStatus() == 1) {
            mAdapter.addData(baseListBean.getData());
        } else {
            netErrorView.loadFail();
        }
    }

    /**
     * 失败请求的回调
     *
     * @param error
     */
    public void requestClassfyDataFailed(VolleyError error) {
        netErrorView.loadFail();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //回退按钮
            case R.id.btn_back:
                finish();
                break;
            case R.id.classfy_tv:
                //点击这里什么也不做
                break;
            //搜索框的点击
            case R.id.relativeLayout_search:
                intent.setClass(this, SearchActivity.class);
                intent.putExtra("keyIsShow", true);
                startActivity(intent);
                break;
            case R.id.classfy_handview:
                intent.setClass(this, GoodsListActivity.class);
                intent.putExtra("title", "全部商品");
                intent.putExtra("cateid", 0);
                startActivity(intent);
                break;
            default:
                break;
        }
       // BaseUtils.colseSoftKeyboard(this);
    }

    /**
     * 分类界面的item点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        BeanClassfy classfy = mAdapter.getItem(position - 1);

        Intent intent = new Intent();
        String name = classfy.getName();//跳转的title文字
        intent.setClass(this, GoodsListActivity.class);
        intent.putExtra("title", name);
        intent.putExtra("cateid", classfy.getCateid() + "");
        startActivity(intent);
    }

    /**
     * 重新加载请求数据
     */
    @Override
    public void onReload() {
        requestNetData();
    }
}
