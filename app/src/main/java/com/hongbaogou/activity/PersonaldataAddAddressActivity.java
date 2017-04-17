package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.PersonaldataAddAddressAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.PersonAddressBean;
import com.hongbaogou.listener.OnPersonDataAddressListener;
import com.hongbaogou.request.PersonDataAddressRequests;
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

import java.util.ArrayList;
import java.util.List;

public class PersonaldataAddAddressActivity extends BaseAppCompatActivity implements NetErrorView.OnReloadListener, OnPersonDataAddressListener, AdapterView.OnItemClickListener {
    private String uid;

    private ListView mListView;
    private ImageView mImageView;
    private TextView mTvShowAddress;
    private NetErrorView netErrorView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private Button mButton, btn_personaldata_addaddress, btn_personaldata_address;

    private PersonDataAddressRequests mRequset;
    private PersonaldataAddAddressAdapter mAdapter;
    private List<PersonAddressBean> mData = new ArrayList<PersonAddressBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_address);
        initBarUtils.setSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        netErrorView.setOnReloadListener(this);
    }

    private void initData() {
        mRequset = new PersonDataAddressRequests();
        mRequset.personDataAddressRequests(uid, this);
        //   mListView.setEmptyView(btn_personaldata_address);
        mListView.setOnItemClickListener(this);
    }

    private void initView() {
        uid = Pref_Utils.getString(getApplicationContext(), "uid");

        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        mTvShowAddress = (TextView) findViewById(R.id.tv_showAddress);
        mListView = (ListView) findViewById(R.id.lv_persondataaddress);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.refreshView);
        mImageView = (ImageView) findViewById(R.id.iv_personaldata_addaddress);
        btn_personaldata_address = (Button) findViewById(R.id.btn_personaldata_address);
        btn_personaldata_addaddress = (Button) findViewById(R.id.btn_personaldata_addaddress);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_listView);

        mAdapter = new PersonaldataAddAddressAdapter(PersonaldataAddAddressActivity.this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(netErrorView);

        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {

            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadMoreContainer.loadMoreFinish(true, true);
            }
        });

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = mListView.getChildCount() > 0 && (mListView.getFirstVisiblePosition() > 0 || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
                } else {
                    flag = mListView.canScrollVertically(-1);
                }
                return !flag;
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        mAdapter.cleanData();
                        mRequset.personDataAddressRequests(uid, PersonaldataAddAddressActivity.this);
                    }
                }, 10);
            }
        });


    }


    public void onClick(View view) {
        Intent intent = new Intent(PersonaldataAddAddressActivity.this, PersonaldataAddressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (view != null) {
            switch (view.getId()) {
                //添加地址
                case R.id.btn_personaldata_addaddress:
                    startActivity(intent);
                    finish();
                    break;
                //添加地址
                case R.id.btn_personaldata_address:
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                //返回
                case R.id.iv_personaldata_addaddress:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1002) {
            mTvShowAddress.setText(data.getStringExtra("address"));
        }
        if (requestCode == 4) {
            mAdapter.cleanData();
            mRequset.personDataAddressRequests(uid, PersonaldataAddAddressActivity.this);
        }
        Log.e("TAg", "======================" + requestCode);
        if (resultCode == 1) {
//            mAdapter.getData().remove(data.getIntExtra("positon",-1));
//            mAdapter.notifyDataSetChanged();
            Log.e("TAG", "-=-=-+  " + data.getIntExtra("positon", -1));
            mAdapter.cleanPosition(data.getIntExtra("positon", -1));
            mRequset.personDataAddressRequests(uid, PersonaldataAddAddressActivity.this);
        }
    }


    public void onReload() {
        mAdapter.cleanData();
        mRequset.personDataAddressRequests(uid, PersonaldataAddAddressActivity.this);
    }

    //请求成功
    @Override
    public void OnPersonDataAddressListenerSuccess(BaseListBean baseListBean) {
        ptrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 1) {
            mAdapter.notifyDataSetChanged();
            loadMoreListViewContainer.loadMoreFinish(false, false);
            if (baseListBean.getData() != null) {
                if ((baseListBean.getData().size() >= 5)) {
                    btn_personaldata_addaddress.setVisibility(View.INVISIBLE);
                }
                mAdapter.addData(baseListBean.getData());
            } else {
                netErrorView.setVisibility(View.GONE);
                btn_personaldata_address.setVisibility(View.VISIBLE);
            }
        } else {
            ToastUtil.showToast(this, baseListBean.getMessage().toString());
            netErrorView.loadFail();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mAdapter.getCount() == 0) {
            btn_personaldata_address.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnPersonDataAddressListenerFailed(VolleyError error) {
        ptrFrameLayout.refreshComplete();
        netErrorView.loadFail();
    }

    //ListView Item的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PersonAddressBean bean = (PersonAddressBean) mAdapter.getItem(position);
        Intent intent = new Intent(PersonaldataAddAddressActivity.this, PersonaldataAmendressActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("id", bean.getId());
        intent.putExtra("uid", bean.getUid());
        intent.putExtra("sheng", bean.getSheng());
        intent.putExtra("shi", bean.getShi());
        intent.putExtra("xian", bean.getXian());
        intent.putExtra("jiedao", bean.getJiedao());
        intent.putExtra("shouhuoren", bean.getShouhuoren());
        intent.putExtra("default", bean.getIs_default());
        intent.putExtra("mobile", bean.getMobile());
        intent.putExtra("address", bean.getAddress());
        intent.putExtra("is_default", bean.getIs_default());
        startActivityForResult(intent, 1);
//        startActivity(intent);
    }

}
