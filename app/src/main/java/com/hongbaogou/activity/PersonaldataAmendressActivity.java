package com.hongbaogou.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.listener.OnPersonalDataDeleteAddressListener;
import com.hongbaogou.listener.OnPersonalDataSaveAddressListener;
import com.hongbaogou.request.PersonalDataDeleteAddressRequests;
import com.hongbaogou.request.PersonalDataSaveAddressRequests;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;



//修改地址
public class PersonaldataAmendressActivity extends BaseAppCompatActivity implements View.OnTouchListener, OnPersonalDataDeleteAddressListener, OnPersonalDataSaveAddressListener, TextView.OnEditorActionListener {
    private AlertDialog mDialog;
    private Spinner mSpProvince, mSpCity, mSpArea;
    private ImageView iv_personaldataamend_tltle, miv_personaldata_amendaddress_default;
    private Button btn_personaldataamend_tltle, ibtn_personaldata_amendaddress_delete;
    private EditText et_personaldata_amend_user, et_personaldata_amend_tel, et_personaldata_amend_detail;
    private ImageButton ibtn_personaldata_amend_user, ibtn_personaldata_amend_tel, ibtn_personaldata_amend_detail,
            ibtn_personaldata_amendaddress_default;
    private RelativeLayout rl_personaldata_amendaddress_default;


    //删除请求
    private PersonalDataDeleteAddressRequests mRequest;
    private String uid;
    private String id;
    //保存地址请求
    private PersonalDataSaveAddressRequests mRequestSave;
    private String s;
    private String is_default = "0";
    private String eis_default = "N";
    private boolean tag = true;
    private int touch_flag = 0;

    public static final int SELECT_CITY = 1002;
    private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null
    private String[] mProvinceDatas;//所有省
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();//key - 省 value - 市s
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();//key - 市 values - 区s
    private String mCurrentProviceName;//当前省的名称
    private String mCurrentAreaName = "";//当前区的名称
    private ArrayAdapter<String> mProvinceAdapter;//省份数据适配器
    private ArrayAdapter<String> mCityAdapter;//城市数据适配器
    private ArrayAdapter<String> mAreaAdapter;//省份数据适配器
    private EditText mEtDetailAddre;//地址的详细信息
    private String mAddress;//原有的地址
    private String[] mAddressList;//原有地址，用空格进行切分地址
    private Boolean isFirstLord = true;//判断是不是最近进入对话框
    private Boolean ifSetFirstAddress = true;//判断是否已经设置了，初始的详细地址
    private int position;
    private Intent intent;
    private String aDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_amendress);
        initBarUtils.setSystemBar(this);
        initJsonData();
        initDatas();
        initView();
        setListener();
    }

    private void setListener() {
        et_personaldata_amend_user.setOnTouchListener(this);
        et_personaldata_amend_tel.setOnTouchListener(this);
        et_personaldata_amend_detail.setOnTouchListener(this);
        rl_personaldata_amendaddress_default.setOnTouchListener(this);
        et_personaldata_amend_user.setOnEditorActionListener(this);
        et_personaldata_amend_tel.setOnEditorActionListener(this);
        et_personaldata_amend_detail.setOnEditorActionListener(this);


    }

    private void initView() {
        iv_personaldataamend_tltle = (ImageView) findViewById(R.id.iv_personaldataamend_tltle);
        btn_personaldataamend_tltle = (Button) findViewById(R.id.btn_personaldataamend_tltle);
        ibtn_personaldata_amendaddress_delete = (Button) findViewById(R.id.ibtn_personaldata_amendaddress_delete);

        ibtn_personaldata_amend_user = (ImageButton) findViewById(R.id.ibtn_personaldata_amend_user);
        ibtn_personaldata_amend_tel = (ImageButton) findViewById(R.id.ibtn_personaldata_amend_tel);
        ibtn_personaldata_amend_detail = (ImageButton) findViewById(R.id.ibtn_personaldata_amend_detail);
        miv_personaldata_amendaddress_default = (ImageView) findViewById(R.id.iv_personaldata_amendaddress_default);

        rl_personaldata_amendaddress_default = (RelativeLayout) findViewById(R.id.rl_personaldata_amendaddress_default);


        et_personaldata_amend_user = (EditText) findViewById(R.id.et_personaldata_amend_user);
        et_personaldata_amend_tel = (EditText) findViewById(R.id.et_personaldata_amend_tel);
        et_personaldata_amend_detail = (EditText) findViewById(R.id.et_personaldata_amend_detail);

        mSpProvince = (Spinner) findViewById(R.id.spProvince);

        mSpCity = (Spinner) findViewById(R.id.spCity);

        mSpArea = (Spinner) findViewById(R.id.spArea);


        int selectPro = 0;//有传输数据时
        mAddress = getIntent().getStringExtra("address");//通过传入的地区的信息进行初始化，控件数据
        if (mAddress != null && !mAddress.equals("")) {
            mAddressList = mAddress.split(" ");
        }

        /**
         * 设置，省，市，县，的适配器，进行动态设置其中的值  begin
         */
        mProvinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < mProvinceDatas.length; i++) {
            //当有传入值时，进行判断选中的条目，设置默认值
            if (mAddress != null && !mAddress.equals("") && mAddressList.length > 0 && mAddressList[0].equals(mProvinceDatas[i])) {
                selectPro = i;
            }
            mProvinceAdapter.add(mProvinceDatas[i]);
        }
        mProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpProvince.setAdapter(mProvinceAdapter);
        mCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpCity.setAdapter(mCityAdapter);
        mAreaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpArea.setAdapter(mAreaAdapter);
        /**
         * 设置，省，市，县，的适配器，进行动态设置其中的值  end
         */
        setupViewsListener();

        //获得的收货信息
        intent = getIntent();
        et_personaldata_amend_user.setText(intent.getStringExtra("shouhuoren"));
        et_personaldata_amend_tel.setText(intent.getStringExtra("mobile"));
        et_personaldata_amend_detail.setText(intent.getStringExtra("jiedao"));
        String sheng = intent.getStringExtra("sheng");
        String shi = intent.getStringExtra("shi");
        String xian = intent.getStringExtra("xian");

        uid = intent.getStringExtra("uid");
        id = intent.getStringExtra("id");
        // System.out.println("===========id==========" + id);

        position = intent.getIntExtra("position", -1);

        aDefault = intent.getStringExtra("default");
        if ("N".equals(aDefault) && aDefault != null) {
            // miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_on);
            miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_off);
        } else if ("Y".equals(aDefault) && aDefault != null) {
            //  miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_off);
            miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_on);
        }
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_personaldataamend_tltle://返回
                    BaseUtils.colseSoftKeyboard(this);
                    finish();
                    break;
                case R.id.btn_personaldataamend_tltle://保存
                    if ("".equals(et_personaldata_amend_user.getText().toString().trim())) {
                        et_personaldata_amend_user.setError("收货人不能为空");
                        ToastUtil.showToast(this, "输入不规范，请从新输入!");
                        ibtn_personaldata_amend_user.setVisibility(View.GONE);
                    }

                    if (TextUtils.isEmpty(et_personaldata_amend_user.getText().toString())) {
                        et_personaldata_amend_user.setError("收货人不能为空");
                        ibtn_personaldata_amend_user.setVisibility(View.GONE);
                        return;
                    } else if (TextUtils.isEmpty(et_personaldata_amend_tel.getText().toString())) {
                        et_personaldata_amend_tel.setError("手机号码为空或者不正确！");
                        ibtn_personaldata_amend_tel.setVisibility(View.GONE);
                        return;
                    } else if (TextUtils.isEmpty(et_personaldata_amend_detail.getText().toString())) {
                        et_personaldata_amend_detail.setError("详细地址不能为空");
                        ibtn_personaldata_amend_detail.setVisibility(View.GONE);
                        return;
                    } else if ("".equals(et_personaldata_amend_detail.getText().toString().trim())) {
                        et_personaldata_amend_detail.setError("详细地址不能为空");
                        ibtn_personaldata_amend_detail.setVisibility(View.GONE);
                    }

                    if (et_personaldata_amend_user.getText().toString().equals(intent.getStringExtra("shouhuoren")) &&
                            et_personaldata_amend_tel.getText().toString().equals(intent.getStringExtra("mobile")) &&
                            et_personaldata_amend_detail.getText().toString().equals(intent.getStringExtra("jiedao")) &&
                            mSpProvince.getSelectedItem().equals("北京市") &&
                            mSpCity.getSelectedItem().equals("北京市") &&
                            mCurrentAreaName.equals("东城区") &&
                            eis_default.equals(intent.getStringExtra("is_default"))) {
                        ToastUtil.showToast(this, "未做修改，无法保存");
                        //return;
                    } else {
                        mRequestSave = new PersonalDataSaveAddressRequests();
                        //&shouhuoren=xiaohe&mobile=15150141010&sheng=广东省&shi=深圳&xian=宝安&jiedao=西乡&default=1
                        mCurrentAreaName = mSpArea.getSelectedItem() == null ? " " : mSpArea.getSelectedItem().toString();
                        s = "&shouhuoren=" + et_personaldata_amend_user.getText().toString() + "&mobile=" + et_personaldata_amend_tel.getText().toString()
                                + "&sheng=" + mSpProvince.getSelectedItem() + "&shi=" + mSpCity.getSelectedItem() + "&xian=" + mCurrentAreaName + "&jiedao="
                                + et_personaldata_amend_detail.getText().toString();
                        mRequestSave.personalDataSaveAddressRequests(uid, s, is_default, id, PersonaldataAmendressActivity.this);
                        Intent intent = new Intent();
                        intent.putExtra("positon", position);
                        intent.putExtra("action", position);
                        setResult(1, intent);
                        BaseUtils.colseSoftKeyboard(this);

                   /* try {
                        String string = new String(this.s.getBytes("UTF-8"), "GBK");

                    } catch (UnsupportedEncodingException e) {
                    } */
                    }
                    break;
                case R.id.ibtn_personaldata_amend_user:
                    et_personaldata_amend_user.getText().clear();
                    break;
                case R.id.ibtn_personaldata_amend_tel:
                    et_personaldata_amend_tel.getText().clear();
                    break;
                case R.id.ibtn_personaldata_amend_detail:
                    et_personaldata_amend_detail.getText().clear();
                    break;
                case R.id.ibtn_personaldata_amendaddress_delete://删除所有
                    AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
                    mdialog.setMessage("确定要删除吗?");
                    mdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 删除记录请求
                            mRequest = new PersonalDataDeleteAddressRequests();
                            mRequest.personalDataDeleteAddressRequests(getIntent().getStringExtra("uid"), getIntent().getStringExtra("id"), PersonaldataAmendressActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("positon", position);
                            intent.putExtra("action", position);
                            setResult(1, intent);

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
                case R.id.rl_personaldata_amendaddress_default:

                    if (tag) {
                        tag = false;
                        miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_off);
                        is_default = "0";
                        eis_default = "N";
                        ToastUtil.showToast(this, "未设置为默认地址");
                    } else {
                        tag = true;
                        miv_personaldata_amendaddress_default.setBackgroundResource(R.mipmap.switch_on);
                        is_default = "1";
                        eis_default = "Y";
                        ToastUtil.showToast(this, "已设为默认地址");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touch_flag++;
        if (touch_flag == 2) {
            ibtn_personaldata_amend_user.setVisibility(View.VISIBLE);
            ibtn_personaldata_amend_tel.setVisibility(View.VISIBLE);
            ibtn_personaldata_amend_detail.setVisibility(View.VISIBLE);
            rl_personaldata_amendaddress_default.isFocused();
        }
        return false;
    }

    /**
     * 设置控件点击选择监听
     */
    private void setupViewsListener() {
        mSpProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                mCurrentProviceName = arg0.getSelectedItem() + "";
                if (isFirstLord) {
                    if (mAddress != null && !mAddress.equals("") && mAddressList.length > 1 && mAddressList.length < 3) {
                        updateCitiesAndAreas(mCurrentProviceName, mAddressList[1], null);
                    } else if (mAddress != null && !mAddress.equals("") && mAddressList.length >= 3) {
                        updateCitiesAndAreas(mCurrentProviceName, mAddressList[1], mAddressList[2]);
                    } else {
                        updateCitiesAndAreas(mCurrentProviceName, null, null);
                    }
                } else {
                    updateCitiesAndAreas(mCurrentProviceName, null, null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mCurrentProviceName = "请选择";
            }
        });
        mSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (!isFirstLord) {
                    updateAreas(arg0.getSelectedItem(), null);
                } else {
                    if (mAddress != null && !mAddress.equals("") && mAddressList.length == 4) {
                        mEtDetailAddre.setText(mAddressList[3]);
                    }
                }
                isFirstLord = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mCurrentProviceName = "请选择";
            }
        });
        mSpArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mCurrentAreaName = arg0.getSelectedItem() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mCurrentAreaName = "请选择";
            }
        });
    }

    /**
     * 根据当前的省，更新市和地区信息
     */
    private void updateCitiesAndAreas(Object object, Object city, Object myArea) {
        int selectPosition = 0;//当有数据时，进行匹配城市，默认选中
        String[] cities = mCitisDatasMap.get(object);
        mCityAdapter.clear();
        for (int i = 0; i < cities.length; i++) {
            if (city != null && city.toString().equals(cities[i])) {
                selectPosition = i;
            }
            mCityAdapter.add(cities[i]);
        }
        mCityAdapter.notifyDataSetChanged();
        if (city == null) {
            updateAreas(cities[0], null);
        } else {
            mSpCity.setSelection(selectPosition);
            updateAreas(city, myArea);
        }
    }

    /**
     * 根据当前的市，更新地区信息
     */
    private void updateAreas(Object object, Object myArea) {
        boolean isAreas = false;//判断第三个地址是地区还是详细地址
        int selectPosition = 0;//当有数据时，进行匹配地区，默认选中
        String[] area = mAreaDatasMap.get(object);
        mAreaAdapter.clear();
        if (area != null) {
            for (int i = 0; i < area.length; i++) {
                if (myArea != null && myArea.toString().equals(area[i])) {
                    selectPosition = i;
                    isAreas = true;
                }
                mAreaAdapter.add(area[i]);
            }
            mAreaAdapter.notifyDataSetChanged();
            mSpArea.setSelection(selectPosition, true);
        }
        //第三个地址是详细地址，并且是第一次设置edtext值，正好，地址的长度为3的时候，设置详细地址
        if (!isAreas && ifSetFirstAddress && mAddress != null && !mAddress.equals("") && mAddressList.length == 3) {
            mEtDetailAddre.setText(mAddressList[2]);
            ifSetFirstAddress = false;
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try {
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try {
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //删除地址
    @Override
    public void OnPersonalDataDeleteAddressListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                et_personaldata_amend_user.getText().clear();
                et_personaldata_amend_tel.getText().clear();
                et_personaldata_amend_detail.getText().clear();
                ToastUtil.showToast(this, "成功");
                finish();
            } else {
                ToastUtil.showToast(this, "删除失败");
            }
        }
    }

    @Override
    public void OnPersonalDataDeleteAddressListenerFailed(VolleyError error) {

    }

    //保存地址
    @Override
    public void OnPersonalDataSaveAddressListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                ToastUtil.showToast(this, "保存成功");
                mRequest = new PersonalDataDeleteAddressRequests();
                mRequest.personalDataDeleteAddressRequests(getIntent().getStringExtra("uid"), getIntent().getStringExtra("id"), PersonaldataAmendressActivity.this);
                finish();
            } else {
                ToastUtil.showToast(this, "保存失败");
            }
        }
    }

    @Override
    public void OnPersonalDataSaveAddressListenerFailed(VolleyError error) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {


        }
        if (actionId == EditorInfo.IME_ACTION_NONE) {



        }
            return false;


        }
    }
