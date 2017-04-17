package com.hongbaogou.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.DialogListViewAdapter;
import com.hongbaogou.adapter.SingleAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.HeadImageBean;
import com.hongbaogou.bean.PersonAddressBean;
import com.hongbaogou.bean.WinEnsureAddressBean;
import com.hongbaogou.bean.WinRecordsInfoAddressBean;
import com.hongbaogou.bean.WinRecordsInfoBean;
import com.hongbaogou.bean.WinRecordsInfoCompanyBean;
import com.hongbaogou.bean.WinRecordsInfoGoodsInfoBean;
import com.hongbaogou.bean.WinRecordsInfoStatusBean;
import com.hongbaogou.listener.OnPersonDataAddressListener;
import com.hongbaogou.listener.OnPersonalDataTouimgListener;
import com.hongbaogou.listener.OnWinEnsureAddressListener;
import com.hongbaogou.listener.OnWinEnsureRequestsListener;
import com.hongbaogou.listener.OnWinRecordsInfoListener;
import com.hongbaogou.request.PersonDataAddressRequests;
import com.hongbaogou.request.WinEnsureAddressRequests;
import com.hongbaogou.request.WinEnsureRequests;
import com.hongbaogou.request.WinRecordsInfoRequests;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//中奖确认
public class WinRecordSignInActivity extends BaseAppCompatActivity implements OnWinRecordsInfoListener, OnWinEnsureRequestsListener, OnPersonDataAddressListener, AdapterView.OnItemClickListener, OnWinEnsureAddressListener, OnPersonalDataTouimgListener {
    private List<WinRecordsInfoStatusBean> mlist = new ArrayList<WinRecordsInfoStatusBean>();
    private WinRecordsInfoRequests mrequest;
    private WinEnsureRequests mWinEnsureRequests;//中奖确认
    private String id;
    private String uid;
    private String is_ten;
    private AlertDialog mDialog;
    private ImageLoader imageLoader;
    private NetworkImageView mNetImage_winrecord;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayout;
    private Button btn_signin_ensureprize, btn_signin_ensureaddress;
    private WinRecordsInfoBean winRecordsInfoBean;
    private WinRecordsInfoStatusBean mWinRecordsInfoStatusBean;
    private WinRecordsInfoAddressBean mWinRecordsInfoAddressBean;
    private WinRecordsInfoCompanyBean mWinRecordsInfoCompanyBean;
    private WinRecordsInfoGoodsInfoBean mWinRecordsInfoGoodsInfoBean;
    private TextView tv_logisticsinformation_title, tv_logisticsinformation_company,
            tv_logisticsinformation_num, tv_winuser_name, tv_winuser_address, tv_winuser_tel,
            tv__winrecord_title, tv__winrecord_qishu, tv__winrecord_zongrenshu, tv__winrecord_q_user_code,
            tv__winrecord_q_user_renci, tv__winrecord_q_user_q_end_time;
    private TextView tv_signin_get, tv_signin_gettime, tv_signin_ensureadress, tv_signin_ensureadresstime,
            tv_signin_prize, tv_signin_prizetime, tv_signin_ensureprize, tv_signin_ensureprizetime, tv_signin_ensure;
    private ImageView mback, img_ten, iv_signin_get, iv_signin_ensureadress, iv_signin_prize, iv_signin_ensureprize, iv_signin_ensure,
            iv_iv_signin_get_line, iv_signin_ensureadress_line, iv_signin_prize_line, iv_signin_ensureprize_line;
    //地址

    private SingleAdapter mSingleAdapter;

    private DialogListViewAdapter adapter;
    private PersonDataAddressRequests mRequset;
    private List<PersonAddressBean> mData = new ArrayList<PersonAddressBean>();
    //确认收货地址
    private WinEnsureAddressRequests mWinEnsureAddressRequests;
    private PersonAddressBean mPersonAddressBean;
    private String aid;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_record_sign_in);
        initBarUtils.setSystemBar(this);
        initview();
        initData();

        mrequest = new WinRecordsInfoRequests();
        id = getIntent().getStringExtra("id");
        is_ten = getIntent().getStringExtra("is_ten");
        if (is_ten.equals("0")) {
            img_ten.setVisibility(View.GONE);
        } else {
            img_ten.setVisibility(View.VISIBLE);
        }
        mrequest.payRecodersRequest(uid, id, this);
        initBarUtils.setSystemBar(this);
    }

    private void initData() {
        winRecordsInfoBean = new WinRecordsInfoBean();
        mWinRecordsInfoAddressBean = new WinRecordsInfoAddressBean();
        mWinRecordsInfoCompanyBean = new WinRecordsInfoCompanyBean();
        mWinRecordsInfoGoodsInfoBean = new WinRecordsInfoGoodsInfoBean();
        imageLoader = RequestManager.getImageLoader();

        mWinEnsureRequests = new WinEnsureRequests();
    }

    private void initview() {

        mNetImage_winrecord = (NetworkImageView) findViewById(R.id.netImage_winrecord);
        mback = (ImageView) findViewById(R.id.iv_winrecodessingin_back);
        img_ten = (ImageView) findViewById(R.id.img_ten);

        iv_signin_get = (ImageView) findViewById(R.id.iv_signin_get);
        iv_signin_prize = (ImageView) findViewById(R.id.iv_signin_prize);
        iv_signin_ensure = (ImageView) findViewById(R.id.iv_signin_ensure);
        iv_signin_ensureadress = (ImageView) findViewById(R.id.iv_signin_ensureadress);
        iv_signin_ensureprize = (ImageView) findViewById(R.id.iv_signin_ensureprize);

        iv_iv_signin_get_line = (ImageView) findViewById(R.id.iv_iv_signin_get_line);
        iv_signin_ensureadress_line = (ImageView) findViewById(R.id.iv_signin_ensureadress_line);
        iv_signin_prize_line = (ImageView) findViewById(R.id.iv_signin_prize_line);
        iv_signin_ensureprize_line = (ImageView) findViewById(R.id.iv_signin_ensureprize_line);

        btn_signin_ensureprize = (Button) findViewById(R.id.btn_signin_ensureprize);
        btn_signin_ensureaddress = (Button) findViewById(R.id.btn_signin_ensureaddress);

        tv_logisticsinformation_num = (TextView) findViewById(R.id.tv_logisticsinformation_num);
        tv_logisticsinformation_title = (TextView) findViewById(R.id.tv_logisticsinformation_title);
        tv_logisticsinformation_company = (TextView) findViewById(R.id.tv_logisticsinformation_company);

        tv_winuser_tel = (TextView) findViewById(R.id.tv_winuser_tel);
        tv_winuser_name = (TextView) findViewById(R.id.tv_winuser_name);
        tv_winuser_address = (TextView) findViewById(R.id.tv_winuser_address);

        tv_winuser_address = (TextView) findViewById(R.id.tv_winuser_address);
        tv__winrecord_title = (TextView) findViewById(R.id.tv__winrecord_title);
        tv__winrecord_qishu = (TextView) findViewById(R.id.tv__winrecord_qishu);
        tv__winrecord_q_user_code = (TextView) findViewById(R.id.tv__winrecord_q_user_code);
        tv__winrecord_zongrenshu = (TextView) findViewById(R.id.tv__winrecord_zongrenshu);
        tv__winrecord_q_user_renci = (TextView) findViewById(R.id.tv__winrecord_q_user_renci);
        tv__winrecord_q_user_q_end_time = (TextView) findViewById(R.id.tv__winrecord_q_user_q_end_time);

        tv_signin_get = (TextView) findViewById(R.id.tv_signin_get);
        tv_signin_prize = (TextView) findViewById(R.id.tv_signin_prize);
        tv_signin_ensure = (TextView) findViewById(R.id.tv_signin_ensure);
        tv_signin_gettime = (TextView) findViewById(R.id.tv_signin_gettime);
        tv_signin_prizetime = (TextView) findViewById(R.id.tv_signin_prizetime);
        tv_signin_ensureprize = (TextView) findViewById(R.id.tv_signin_ensureprize);
        tv_signin_ensureadress = (TextView) findViewById(R.id.tv_signin_ensureadress);
        tv_signin_ensureadresstime = (TextView) findViewById(R.id.tv_signin_ensureadresstime);
        tv_signin_ensureprizetime = (TextView) findViewById(R.id.tv_signin_ensureprizetime);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.ll_signin);
        uid = Pref_Utils.getString(getApplicationContext(), "uid");

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void OnWinRecordsInfoSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            winRecordsInfoBean = (WinRecordsInfoBean) baseObjectBean.getData();
            //奖品状态
            mlist = winRecordsInfoBean.getJindu();
            for (int i = 0; i < mlist.size(); i++) {
                mWinRecordsInfoStatusBean = mlist.get(i);
                String info = mWinRecordsInfoStatusBean.getInfo();
                String status = mWinRecordsInfoStatusBean.getStatus();
                String time = mWinRecordsInfoStatusBean.getTime();

                if ("获得商品".equals(info)) {
                    if ("1".equals(status)) {
                        tv_signin_get.setText(info);
                        tv_signin_gettime.setText(time);
                        iv_signin_get.setBackgroundResource(R.mipmap.winrecord_ensure);
                        tv_signin_get.setVisibility(View.VISIBLE);
                        tv_signin_gettime.setVisibility(View.VISIBLE);
                        iv_signin_get.setVisibility(View.VISIBLE);
                        iv_iv_signin_get_line.setVisibility(View.VISIBLE);
                        btn_signin_ensureaddress.setVisibility(View.VISIBLE);
                    } else {
                        btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                        //   iv_signin_ensureprize.setVisibility(View.INVISIBLE);
                        btn_signin_ensureprize.setVisibility(View.INVISIBLE);
                    }
                }
                if ("确认收获地址".equals(info)) {
                    if ("1".equals(status)) {
                        tv_signin_ensureadress.setText(info);
                        tv_signin_ensureadresstime.setText(time);
                        iv_signin_ensureadress.setBackgroundResource(R.mipmap.winrecord_ensure);
                        tv_signin_ensureadress.setVisibility(View.VISIBLE);
                        tv_signin_ensureadresstime.setVisibility(View.VISIBLE);
                        iv_signin_ensureadress.setVisibility(View.VISIBLE);
                        iv_signin_ensureadress_line.setVisibility(View.VISIBLE);
                        btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                    } else if ("0".equals(status)) {
                        // btn_signin_ensureaddress.setVisibility(View.VISIBLE);
                        //           iv_signin_ensureprize.setVisibility(View.INVISIBLE);
                        // btn_signin_ensureprize.setVisibility(View.VISIBLE);
                    }
                }
                if ("奖品派发".equals(info)) {
                    if ("1".equals(status)) {
                        tv_signin_prize.setText(info);
                        tv_signin_prizetime.setText(time);
                        tv_signin_prize.setVisibility(View.VISIBLE);
                        tv_signin_prizetime.setVisibility(View.VISIBLE);
                        iv_signin_prize.setVisibility(View.VISIBLE);
                        iv_signin_prize_line.setVisibility(View.VISIBLE);
                        iv_signin_prize.setBackgroundResource(R.mipmap.winrecord_ensure);
                        tv_signin_ensureprize.setText("确认收货");
                        btn_signin_ensureprize.setVisibility(View.VISIBLE);
                        tv_signin_ensureprize.setVisibility(View.VISIBLE);
                        iv_signin_ensureprize.setVisibility(View.VISIBLE);
                        btn_signin_ensureprize.setVisibility(View.VISIBLE);
                        btn_signin_ensureaddress.setVisibility(View.INVISIBLE);

                    } else {
                        //iv_signin_ensureprize.setVisibility(View.INVISIBLE);
                        btn_signin_ensureprize.setVisibility(View.INVISIBLE);
                        //btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                    }
                }
                if ("确认收货".equals(info)) {
                    if ("1".equals(status)) {
                        tv_signin_ensureprize.setText(info);
                        tv_signin_ensureprizetime.setText(time);
                        tv_signin_ensureprize.setVisibility(View.VISIBLE);
                        tv_signin_ensureprizetime.setVisibility(View.VISIBLE);
                        iv_signin_ensureprize.setVisibility(View.VISIBLE);
                        iv_signin_ensureprize_line.setVisibility(View.VISIBLE);
                        iv_signin_ensureprize.setBackgroundResource(R.mipmap.winrecord_ensure);
                        btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                    } else if ("0".equals(status)) {
                       /*tv_signin_ensureprize.setText(info);
                       btn_signin_ensureprize.setVisibility(View.VISIBLE);
                        tv_signin_ensureprize.setVisibility(View.VISIBLE);
                        iv_signin_ensureprize.setVisibility(View.VISIBLE);
                        btn_signin_ensureprize.setVisibility(View.VISIBLE);*/
                        // btn_signin_ensureaddress.setVisibility(View.VISIBLE);
                    }

                }
                if ("已签收".equals(info)) {
                    if ("1".equals(status)) {
                        tv_signin_ensure.setText("已签收");
                        tv_signin_ensure.setTextColor(getResources().getColor(R.color.color_signin));
                        iv_signin_ensure.setBackgroundResource(R.mipmap.winrecord_ensure);
                        btn_signin_ensureprize.setVisibility(View.INVISIBLE);
                        tv_signin_ensure.setVisibility(View.VISIBLE);
                        iv_signin_ensure.setVisibility(View.VISIBLE);
                        btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                    } else {
                        //btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                    }

                }
            }

            //地址信息
            mWinRecordsInfoAddressBean = winRecordsInfoBean.getAddress();
            tv_winuser_name.setText(mWinRecordsInfoAddressBean.getShouhuoren());
            tv_winuser_address.setText(mWinRecordsInfoAddressBean.getAddress());
            tv_winuser_tel.setText(mWinRecordsInfoAddressBean.getMobile());

            //商品信息
            mWinRecordsInfoGoodsInfoBean = winRecordsInfoBean.getGoods_info();
            //物流信息
            tv_logisticsinformation_title.setText(mWinRecordsInfoGoodsInfoBean.getTitle());
            mWinRecordsInfoCompanyBean = winRecordsInfoBean.getCompany();
            tv_logisticsinformation_company.setText(mWinRecordsInfoCompanyBean.getCompany());
            tv_logisticsinformation_num.setText(mWinRecordsInfoCompanyBean.getCompany_code());


            tv__winrecord_title.setText(mWinRecordsInfoGoodsInfoBean.getTitle());
            tv__winrecord_qishu.setText("期号：" + mWinRecordsInfoGoodsInfoBean.getQishu());
            tv__winrecord_zongrenshu.setText("总需:" + mWinRecordsInfoGoodsInfoBean.getZongrenshu() + "人次");
            tv__winrecord_q_user_renci.setText("本期参与:" + mWinRecordsInfoGoodsInfoBean.getGonumber() + "人次");
            tv__winrecord_q_user_q_end_time.setText("揭晓时间:" + mWinRecordsInfoGoodsInfoBean.getQ_end_time());
            tv__winrecord_q_user_code.setText(mWinRecordsInfoGoodsInfoBean.getQ_user_code());
            //netImage_winrecord.setImageUrl(mWinRecordsInfoGoodsInfoBean.getThumb(), imageLoader);
            mNetImage_winrecord.setDefaultImageResId(R.mipmap.img_blank);
            mNetImage_winrecord.setErrorImageResId(R.mipmap.img_blank);
            mNetImage_winrecord.setImageUrl(mWinRecordsInfoGoodsInfoBean.getThumb(), imageLoader);
        }

    }

    @Override
    public void OnWinRecordsInfoFailed(VolleyError error) {

    }

    //点击事件
    public void onClick(View view) {
        if (view != null)
            switch (view.getId()) {
                //返回
                case R.id.iv_winrecodessingin_back:
                    finish();
                    break;
                //确认收货
                case R.id.btn_signin_ensureprize:
                    AlertDialog.Builder mdialog = new AlertDialog.Builder(WinRecordSignInActivity.this);
                    mdialog.setMessage("确定收货吗?");
                    mdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认收货
                            mWinEnsureRequests.winEnsureRequests(uid, id, WinRecordSignInActivity.this);
                            btn_signin_ensureaddress.setVisibility(View.INVISIBLE);
                            ToastUtil.showToast(WinRecordSignInActivity.this, "收货成功");
                        }
                    });
                    mdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                        }
                    });
                    mDialog = mdialog.create();
                    mDialog.show();
                    break;
                case R.id.ll_signin:
                    //跳到商品详情
                    Intent intent = new Intent(this, GoodsDetailActivity.class);
                    intent.putExtra("id", mWinRecordsInfoGoodsInfoBean.getId());
                    intent.putExtra("issue", mWinRecordsInfoGoodsInfoBean.getQishu());
                    startActivity(intent);
                    break;
                //确认收货地址
                case R.id.btn_signin_ensureaddress:
                    showListViewDialog();
                    break;
                default:
                    break;
            }
    }

    private List beSelectedData = new ArrayList();
    private Map<Integer, Boolean> isSelected;
    private List cs = null;

    //自定义 显示地址的listview 的Dialog
    private void showListViewDialog() {
        LinearLayout linearLayoutMain = new LinearLayout(this);//自定义一个布局文件
        linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        listView = new ListView(WinRecordSignInActivity.this);//this为获取当前的上下文
        listView.setFadingEdgeLength(0);


        mRequset = new PersonDataAddressRequests();
        mRequset.personDataAddressRequests(uid, WinRecordSignInActivity.this);
        //adapter = new DialogListViewAdapter(this, mData);
        //   listView.setAdapter(adapter);

        mSingleAdapter = new SingleAdapter(this, mData);
        listView.setAdapter(mSingleAdapter);

        linearLayoutMain.addView(listView);//往这个布局中加入listview

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择地址").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .setNegativeButton("添加地址", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(WinRecordSignInActivity.this,PersonaldataAddressActivity.class));
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        if (adapter.getCount() == 0) {
//                            ToastUtil.showToast(WinRecordSignInActivity.this, "请选择地址");
//                            return;
//                        }
//                        if (adapter.getCount() != 1) {
//                            ToastUtil.showToast(WinRecordSignInActivity.this, "地址只能选择一个，请重新确认！");
//                        } else {
//                            mWinEnsureAddressRequests = new WinEnsureAddressRequests();
//                            mWinEnsureAddressRequests.winEnsureAddressRequests(uid, id, aid, WinRecordSignInActivity.this);
//                            System.out.println("=======uid================>" + uid);
//                            System.out.println("=======id================>" + id);
//                            System.out.println("=======aid================>" + aid);
//
//                        }
//                        finish();

                        if (aid == null) {
                            ToastUtil.showToast(WinRecordSignInActivity.this, "请选择地址");
                            return;
                        }
                        mWinEnsureAddressRequests = new WinEnsureAddressRequests();
                        mWinEnsureAddressRequests.winEnsureAddressRequests(uid, id, aid, WinRecordSignInActivity.this);
                        System.out.println("=======uid================>" + uid);
                        System.out.println("=======id================>" + id);
                        System.out.println("=======aid================>" + aid);
//                        }
                        finish();

                    }

                }).create();

        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // adapter.notifyDataSetChanged();
        mSingleAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }

    // 确认收货
    @Override
    public void OnWinEnsureRequestsListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if ("1".equals(baseObjectBean.getData())) {
                tv_signin_ensure.setText("已签收");
            } else {

            }
        }
    }

    @Override
    public void OOnWinEnsureRequestsListenerFailed(VolleyError error) {

    }

    //地址的请求
    @Override
    public void OnPersonDataAddressListenerSuccess(BaseListBean baseListBean) {
        if (baseListBean.getStatus() == 1) {
            if (baseListBean.getData() != null) {
                //adapter.addData(baseListBean.getData());
                mSingleAdapter.addData(baseListBean.getData());

            } else {
                //如果返回的地址为空，则添加地址
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("添加地址")
                        .setMessage("未添加地址，请添加地址后在确认，是否添加？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(WinRecordSignInActivity.this, PersonaldataAddressActivity.class));
                                        dialog.dismiss();
                                    }
                                }

                        ).create();

                dialog.show();
            }
        } else {
            ToastUtil.showToast(this, baseListBean.getMessage().toString());
        }
    }

    @Override
    public void OnPersonDataAddressListenerFailed(VolleyError error) {

    }


    //Dialog 的listview 的Item 点击事件，设置图片
    private boolean isclicked = true;
    CheckBox cb_address_check;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //  ImageView imageView = (ImageView) view.findViewById(R.id.iv_address_check);

//        if (isclicked){
//            imageView.setImageResource(R.mipmap.address_checked);
//        }else {
//            imageView.setImageResource(R.mipmap.address_nochecked);
//        }
//        isclicked=!isclicked;
//       if (adapter.getCount()>2){
//           ToastUtil.showToast(WinRecordSignInActivity.this,"地址只能选择一个，请重新确认！");
//        }


        // mPersonAddressBean = adapter.getObject(position);
        mPersonAddressBean = mSingleAdapter.getObject(position);

        if( mSingleAdapter.data !=null && mSingleAdapter.data.size()>0){
            for (int i = 0; i < mSingleAdapter.data.size() ;i++) {
                mSingleAdapter.data.get(i).setIs_default("N");
            }

            mPersonAddressBean.setIs_default("Y");
        }
        mSingleAdapter.notifyDataSetChanged();

        aid = mPersonAddressBean.getId();
    }


    //确认收货地址
    @Override
    public void OnWinEnsureAddressListenerSuccess(WinEnsureAddressBean winEnsureAddressBean) {
        if (winEnsureAddressBean != null) {
            if ("1".equals(winEnsureAddressBean.getStatus())) {
                //从新请求中奖确认
                mrequest.payRecodersRequest(uid, id, WinRecordSignInActivity.this);
                Toast.makeText(WinRecordSignInActivity.this, "正在确认。。。请稍候！", Toast.LENGTH_LONG).show();
            } else {

            }
        } else {

        }
    }

    @Override
    public void OnWinEnsureAddressListenerFailed(VolleyError error) {

    }


    @Override
    public void OnPersonalDataTouimgListenerSuccess(HeadImageBean headImageBean) {

        if (headImageBean!=null){
            mWinEnsureRequests.winEnsureRequests(uid, id, WinRecordSignInActivity.this);
            tv_signin_ensureprizetime.setText(headImageBean.getData());
            btn_signin_ensureprize.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public void OnPersonalDataTouimgListenerFailed(VolleyError error) {

    }
}
