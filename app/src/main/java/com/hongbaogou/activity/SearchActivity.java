package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.GoodsAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanKey;
import com.hongbaogou.bean.GoodsCategoryBean;
import com.hongbaogou.bean.GoodsObjectCategoryBean;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.listener.OnSearchResultListener;
import com.hongbaogou.listener.OnShareKeyListener;
import com.hongbaogou.request.AddShopCartRequest;
import com.hongbaogou.request.SearchRequest;
import com.hongbaogou.request.ShareKeyRequest;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

/**
 * 搜索的界面
 */
public class SearchActivity extends BaseAppCompatActivity implements View.OnClickListener, OnSearchResultListener, OnShareKeyListener, AdapterView.OnItemClickListener, GoodsAdapter.AddShoppingCartListener, OnAddShopCartListener {

    private ImageView mBtn_back;
    private EditText mEditText;
    private ImageView mEditText_Clean;
    private GoodsAdapter mAdapter;
    private TextView pingguo;
    private TextView huawei;
    private TextView xiaomi;
    private TextView shouji;

    private TextView title;
    private TextView number;
    private TextView alladd;
    //搜索失败的提示
    private TextView searchHint;
    //关键字的布局
    private RelativeLayout keylayout;

    private RelativeLayout searchListviewlayout;

    private SearchRequest searchRequest;

    private RelativeLayout addListLayout;
    //搜索结果的listview
    private ListView mSearchListview;
    //透明渐变的动画效果
    private Animation alphaAnimation = new AlphaAnimation(0.1f, 1f);

    private String key;

    //加入购物车的请求
    private AddShopCartRequest addShopCartRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        keyIsShow = getIntent().getBooleanExtra("keyIsShow", false);

        key = getIntent().getStringExtra("key");

        String isshow = getIntent().getStringExtra("keyIsShows");

        Log.e("TAG", " ----> isshow = " + isshow + " key = " + key);

        if (isshow != null) {
            if (isshow.equals("0")) {
                keyIsShow = true;
            } else {
                keyIsShow = false;
                searchok = false;
            }
        }


        //设置动画持续的事件
        alphaAnimation.setDuration(1000);
        //找所有的view空间
        findAllView();

        //请求热门搜索的关键字的数据
        requestKeyData();
        //处理跳转过来的数据
        jumpEvent(key);

        mAdapter = new GoodsAdapter(this);
        mAdapter.setAddShoppingCartListener(this);
        //设置适配器
        mSearchListview.setAdapter(mAdapter);
        //处理主界面跳转来的搜索事件
        mSearchListview.setOnItemClickListener(this);

        initBarUtils.setSystemBar(this);

    }

    /**
     * 处理主界面跳转而来的搜索事件
     *
     * @param keywords 搜索的关键字
     */
    private void jumpEvent(String keywords) {
        if (keywords != null) {
            keylayout.setVisibility(View.GONE);
            searchListviewlayout.setVisibility(View.VISIBLE);
            searchListviewlayout.setAnimation(alphaAnimation);
            searchRequest = new SearchRequest();
            searchRequest.searchResult(this, keywords);
            mEditText.setText(key.trim());
        }
    }

    /**
     * 请求搜索的关键字提示
     */
    private void requestKeyData() {
        ShareKeyRequest shareKeyRequest = new ShareKeyRequest();
        shareKeyRequest.requestKey(this);
    }

    //找所有的view空间
    private void findAllView() {

        addListLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        //搜索失败加载的view
        searchHint = (TextView) findViewById(R.id.searchhint);

        //后退按钮的点击事件
        mBtn_back = (ImageView) findViewById(R.id.btn_back);
        mBtn_back.setOnClickListener(this);

        //搜索狂的控件对象
        mEditText = (EditText) findViewById(R.id.ed_tv_search);

        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    if (mEditText.getText().toString().equals("")) {
                        ToastUtil.showToast(SearchActivity.this, "关键字不能为空");
                        return false;
                    }

                    mAdapter.cleanData();
                    searchListviewlayout.setVisibility(View.VISIBLE);
                    searchListviewlayout.setAnimation(alphaAnimation);
                    searchHint.setVisibility(View.GONE);
                    addListLayout.setVisibility(View.GONE);
                    searchHint.setAnimation(alphaAnimation);
                    searchMenthod();
                    return true;
                }
                return false;
            }
        });

        //清除输入框的文字的按钮
        mEditText_Clean = (ImageView) findViewById(R.id.btn_edtext_clean);
        mEditText_Clean.setOnClickListener(this);

        //listview的对象
        mSearchListview = (ListView) findViewById(R.id.search_listview);

        //关键字的布局
        keylayout = (RelativeLayout) findViewById(R.id.hot_search_lauout);
        //搜索结果展示的布局
        searchListviewlayout = (RelativeLayout) findViewById(R.id.search_result_layout);

        pingguo = (TextView) findViewById(R.id.tv_pingguo);
        pingguo.setOnClickListener(this);

        huawei = (TextView) findViewById(R.id.tv_huawei);
        huawei.setOnClickListener(this);

        xiaomi = (TextView) findViewById(R.id.tv_xiaomi);
        xiaomi.setOnClickListener(this);

        shouji = (TextView) findViewById(R.id.tv_shouji);
        shouji.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title_title);
        number = (TextView) findViewById(R.id.canyurenshu);

        alladd = (TextView) findViewById(R.id.all_add_shopping_cart);
    }

    @Override
    public void onClick(View v) {
        searchRequest = new SearchRequest();
        switch (v.getId()) {
            //后退按钮
            case R.id.btn_back:
                //重写onBack
                onBackPressed();
                BaseUtils.colseSoftKeyboard(this);
                break;
            //清空输入框的内容
            case R.id.btn_edtext_clean:
                mEditText.setText("");
                break;
            //搜索苹果的内容
            case R.id.tv_pingguo:
                mAdapter.cleanData();
                searchRequest.searchResult(this, pingguo.getText().toString().trim());
                mEditText.setText(pingguo.getText().toString().trim());
                setEditShow();
                break;
            case R.id.tv_huawei:
                mAdapter.cleanData();
                searchRequest.searchResult(this, huawei.getText().toString().trim());
                mEditText.setText(huawei.getText().toString().trim());
                setEditShow();
                break;
            case R.id.tv_xiaomi:
                mAdapter.cleanData();
                searchRequest.searchResult(this, xiaomi.getText().toString().trim());
                mEditText.setText(xiaomi.getText().toString().trim());
                setEditShow();
                break;
            case R.id.tv_shouji:
                mAdapter.cleanData();
                searchRequest.searchResult(this, shouji.getText().toString().trim());
                mEditText.setText(shouji.getText().toString().trim());
                setEditShow();
                break;
            default:
                break;
        }
    }

    private void setEditShow() {
        mAdapter.notifyDataSetChanged();
        keyIsShow = false;
        searchListviewlayout.setVisibility(View.VISIBLE);
        keylayout.setVisibility(View.GONE);
        searchListviewlayout.setAnimation(alphaAnimation);
    }


    //关键字界面是否显示
    private boolean keyIsShow = false;
    //搜索成功的界面是否显示
    private boolean searchok = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 对返回键进行拦截
     * 判断是否显示关键字界面
     */
    @Override
    public void onBackPressed() {
        mEditText.setText("");
        if (keyIsShow == false) {//关键字没有显示的时候

            if (searchok == true) {//如果搜索成功
                keylayout.setVisibility(View.VISIBLE);
                searchListviewlayout.setVisibility(View.GONE);
                searchListviewlayout.setAnimation(alphaAnimation);
                //设置动画时间
                keylayout.setAnimation(alphaAnimation);
            }
            if (searchok == false) {
                //如果关键字没有显示,但是搜索失败
                keylayout.setVisibility(View.VISIBLE);
                keylayout.setAnimation(alphaAnimation);
                searchHint.setVisibility(View.GONE);
                keyIsShow = true;
                return;
            }
        } else if (keyIsShow == false && searchok == true) {
            keylayout.setVisibility(View.VISIBLE);
            searchListviewlayout.setVisibility(View.GONE);
            searchListviewlayout.setAnimation(alphaAnimation);
            keylayout.setAnimation(alphaAnimation);
        } else {
            finish();
        }
        keyIsShow = true;
    }

    /**
     * 搜索的方法
     */
    private void searchMenthod() {
        keylayout.setVisibility(View.GONE);
        searchListviewlayout.setVisibility(View.VISIBLE);
        searchListviewlayout.setAnimation(alphaAnimation);
        String str = mEditText.getText().toString();
        searchRequest = new SearchRequest();
        searchRequest.searchResult(this, str);
    }

    @Override
    public void requestSearchResultSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            searchok = true;
            GoodsObjectCategoryBean goodsObjectCategoryBean = (GoodsObjectCategoryBean) baseObjectBean.getData();
            mAdapter.cleanData();
            mAdapter.addData(goodsObjectCategoryBean.getList());
            number.setText("共有" + goodsObjectCategoryBean.getList().size() + "件商品");
            title.setText(mEditText.getText().toString() + ",");
            addListLayout.setVisibility(View.VISIBLE);
            searchListviewlayout.setVisibility(View.VISIBLE);
            alphaAnimation.setDuration(1000);
            searchListviewlayout.setAnimation(alphaAnimation);
        } else {
            String s = "抱歉,没有关键字为 " + mEditText.getText().toString() + " 的商品~";
            SpannableStringBuilder ssb = new SpannableStringBuilder(s);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), s.length() - 5, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_gray)), 0, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            searchHint.setTextColor(getResources().getColor(R.color.color_red));
            searchHint.setText(ssb);
            searchHint.setVisibility(View.VISIBLE);
            addListLayout.setVisibility(View.GONE);
            searchListviewlayout.setVisibility(View.GONE);
            searchListviewlayout.setAnimation(alphaAnimation);
            keylayout.setVisibility(View.GONE);
            searchHint.setAnimation(alphaAnimation);
            searchok = false;
        }
    }

    /**
     * 失败的回调
     *
     * @param error volley錯誤請求對象
     */
    @Override
    public void requestSearchResultFailed(VolleyError error) {
        searchok = false;
        keyIsShow = false;
    }

    /**
     * 成功的回调
     *
     * @param beanKey
     */
    @Override
    public void requestShareKeySuccess(BeanKey beanKey) {
        String key[] = beanKey.getData();
        pingguo.setText("\t" + key[0]);
        huawei.setText("\t" + key[1]);
        xiaomi.setText("\t" + key[2]);
        shouji.setText("\t" + key[3]);
    }

    @Override
    public void requestShareKeyFailed(VolleyError error) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, GoodsDetailActivity.class);
        intent.putExtra("id", mAdapter.getObject(position).getId());
        intent.putExtra("issue", mAdapter.getObject(position).getQishu());
        startActivity(intent);
    }

    @Override
    public void addShoppingCar(GoodsCategoryBean goodsCategoryBean) {
        String uid = Pref_Utils.getString(this, "uid");
        addShopCartRequest = new AddShopCartRequest();
        addShopCartRequest.AddShopCartRequest(uid, goodsCategoryBean.getDefault_renci(), goodsCategoryBean.getId(), this);
    }


    public void onAddShopCartSuccess(MessageCountBean messageCountBean) {
        if (messageCountBean.getStatus() == 1) {
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", messageCountBean.getData());
            sendBroadcast(intent);
            ToastUtil.showToast(this, R.string.addlistsuccess);
        } else {
            ToastUtil.showToast(this, R.string.addlistfail);
        }
    }

    public void onAddShopCartFailed(VolleyError error) {
        ToastUtil.showToast(this, R.string.addlistfail);
    }
}
