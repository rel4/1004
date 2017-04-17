package com.hongbaogou.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.BaseWebViewActivity;
import com.hongbaogou.activity.ShareActivity;
import com.hongbaogou.activity.WebViewActivity;
import com.hongbaogou.adapter.FindListAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanFindData;
import com.hongbaogou.listener.OnFindDataListener;
import com.hongbaogou.request.BaseRequest;
import com.hongbaogou.request.FindDataRequest;
import com.hongbaogou.view.NetErrorView;

import java.util.ArrayList;


public class FindFragment extends BaseFragment implements OnFindDataListener, View.OnClickListener, NetErrorView.OnReloadListener, AdapterView.OnItemClickListener {
    //跟布局
    private View rootView;

    private ListView mListView;
    private RelativeLayout mHeadView;
    private FindListAdapter mAdapter;

    private ArrayList<BeanFindData> list = new ArrayList<BeanFindData>();

    private NetErrorView netErrorView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find, container, false);
        mListView = (ListView) rootView.findViewById(R.id.find_listview);

        rootView.findViewById(R.id.find_laytou_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ShareActivity.class));
            }
        });

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        //头部的listview
        View headView = layoutInflater.inflate(R.layout.find_list_head, null);

        netErrorView = (NetErrorView) rootView.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        mListView.setEmptyView(netErrorView);

        mHeadView = (RelativeLayout) headView.findViewById(R.id.find_laytou_head);

        mHeadView.setOnClickListener(this);

        //获取布局解析对象
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置适配器和布局解析器
        mAdapter = new FindListAdapter(list, layoutInflater);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return rootView;
    }

    /**
     * 点击的时候才去请求数据
     *
     * @return 默认为true
     */
    public boolean initRequest() {
        requestNetData();
        return true;
    }

    /**
     * 请求网络数据
     */
    private void requestNetData() {
        FindDataRequest request = new FindDataRequest();
        request.requestFindData(this);
    }

    /**
     * 请求成分股
     *
     * @param baseListBean
     */
    @Override
    public void requestFindDataSuccess(BaseListBean baseListBean) {
        int status = baseListBean.getStatus();
        if (status == 1) {//请求成功
            mAdapter.addData(baseListBean.getData());
        } else {
//            netErrorView.emptyView();
            netErrorView.setVisibility(View.GONE);
        }
    }

    /**
     * 请求失败
     *
     * @param error
     */
    @Override
    public void requestFindDataFailed(VolleyError error) {
//        netErrorView.loadFail();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), ShareActivity.class));
    }

    @Override
    public void onReload() {
        requestNetData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        Log.e("TAG", "position = " + position);
        BeanFindData findData = mAdapter.getItem(position );

        intent.putExtra("title", findData.getTitle());
        intent.putExtra("url", getUrl(findData.getLink()));

        Log.e("TAG", "内容 = " + findData.getTitle() + "-->link = " + getUrl(findData.getLink()));
        startActivity(intent);
    }
    private String getUrl(String link){
        return link+"&"+new BaseRequest().getParams();
    }
}
