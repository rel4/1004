package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.listener.OnPersonalDataSaveAddressListener;
import com.hongbaogou.request.PersonalDataSaveAddressRequests;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.MobileUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//添加地址
public class PersonaldataAddressActivity extends BaseAppCompatActivity implements OnPersonalDataSaveAddressListener, View.OnTouchListener, View.OnClickListener {
    private Toast mToast;
    private Button mButton;
    private ImageView mImageView, mibtn_personaldata_address_default;
    private EditText met_personaldata_address_user, met_personaldata_address_tel, met_personaldata_address;
    private ImageButton mibtn_personaldata_address_user, mibtn_personaldata_address_tel, mibtn_personaldata_address;
    private RelativeLayout rl_personaldata_address_default;


    private String s;
    private String is_default;
    private String uid;
    private String id = null;
    private boolean tag = false;
    private int touch_flag = 0;
    private PersonalDataSaveAddressRequests mRequestSave;

    public static final int SELECT_CITY = 1002;
    private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null
    private String[] mProvinceDatas;//所有省
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();//key - 省 value - 市s
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();//key - 市 values - 区s
    private String mCurrentProviceName;//当前省的名称
    private String mCurrentAreaName = "";//当前区的名称
    private Spinner mSpProvince;//省下拉控件
    private Spinner mSpCity;//城市下拉控件
    private Spinner mSpArea;//地区下拉控件
    private ArrayAdapter<String> mProvinceAdapter;//省份数据适配器
    private ArrayAdapter<String> mCityAdapter;//城市数据适配器
    private ArrayAdapter<String> mAreaAdapter;//省份数据适配器
    private EditText mEtDetailAddre;//地址的详细信息
    private String mAddress;//原有的地址
    private String[] mAddressList;//原有地址，用空格进行切分地址
    private Boolean isFirstLord = true;//判断是不是最近进入对话框
    private Boolean ifSetFirstAddress = true;//判断是否已经设置了，初始的详细地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldat_adess);
        initBarUtils.setSystemBar(this);
        initJsonData();
        initDatas();
        initView();
        setLisetner();
        initBarUtils.setSystemBar(this);
    }

    private void setLisetner() {
        rl_personaldata_address_default.setOnTouchListener(this);
        met_personaldata_address_user.setOnTouchListener(this);
        met_personaldata_address_tel.setOnTouchListener(this);
        mEtDetailAddre.setOnTouchListener(this);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.titleright);
        mImageView = (ImageView) findViewById(R.id.iv_personaldata_address);
        mibtn_personaldata_address_user = (ImageButton) findViewById(R.id.ibtn_personaldata_address_user);
        mibtn_personaldata_address_tel = (ImageButton) findViewById(R.id.ibtn_personaldata_address_tel);
        mibtn_personaldata_address = (ImageButton) findViewById(R.id.ibtn_personaldata_address);
        mibtn_personaldata_address_default = (ImageView) findViewById(R.id.iv_personaldata_address_default);
        rl_personaldata_address_default = (RelativeLayout) findViewById(R.id.rl_personaldata_address_default);

        met_personaldata_address_user = (EditText) findViewById(R.id.et_personaldata_address_user);
        met_personaldata_address_tel = (EditText) findViewById(R.id.et_personaldata_address_tel);
        mEtDetailAddre = (EditText) findViewById(R.id.et_detailaddre);

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
        mSpProvince.setSelection(selectPro);
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
        uid = Pref_Utils.getString(getApplicationContext(), "uid");
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


    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_personaldata_address:
                    BaseUtils.colseSoftKeyboard(this);
                    finish();
                    break;
                case R.id.ibtn_personaldata_address_user:
                    met_personaldata_address_user.getText().clear();
                    break;
                case R.id.ibtn_personaldata_address_tel:
                    met_personaldata_address_tel.getText().clear();
                    break;
                case R.id.ibtn_personaldata_address:
                    met_personaldata_address.getText().clear();
                    break;
                case R.id.titleright:
                    //保存地址
                    if ("".equals(met_personaldata_address_user.getText().toString().trim())) {
                        met_personaldata_address_user.setError("收货人不能为空");
                        mibtn_personaldata_address_user.setVisibility(View.GONE);
                    }
                    if (TextUtils.isEmpty(met_personaldata_address_user.getText().toString())) {
                        met_personaldata_address_user.setError("收货人不能为空");
                        mibtn_personaldata_address_user.setVisibility(View.GONE);
                    } else if (TextUtils.isEmpty(met_personaldata_address_tel.getText().toString())) {
                        met_personaldata_address_tel.setError("手机号码为空或者不正确！");
                        mibtn_personaldata_address_tel.setVisibility(View.GONE);
                    } else if (!MobileUtils.isMobileNO(met_personaldata_address_tel.getText().toString(),PersonaldataAddressActivity.this)) {
                        met_personaldata_address_tel.setError("手机号码格式不正确！");
                        mibtn_personaldata_address_tel.setVisibility(View.GONE);
                    }else if (TextUtils.isEmpty(mEtDetailAddre.getText().toString())) {
                        mibtn_personaldata_address.setVisibility(View.GONE);
                      ToastUtil.showToast(PersonaldataAddressActivity.this, "详细地址不能为空");
                    } else if ("".equals(mEtDetailAddre.getText().toString().trim())) {
                        mibtn_personaldata_address.setVisibility(View.GONE);
                        mEtDetailAddre.setError("详细地址不能为空");
                    } else if (TextUtils.isEmpty(mEtDetailAddre.getText().toString())) {
                        mEtDetailAddre.setError("详细地址不能为空");
                        return;
                    } else {
                        mRequestSave = new PersonalDataSaveAddressRequests();
                        //&shouhuoren=xiaohe&mobile=15150141010&sheng=广东省&shi=深圳&xian=宝安&jiedao=西乡&default=1
                        mCurrentAreaName = mSpArea.getSelectedItem() == null ? " " : mSpArea.getSelectedItem().toString();
                        try {
                            s = "&shouhuoren=" + URLEncoder.encode(met_personaldata_address_user.getText().toString(), "utf-8") + "&mobile=" + met_personaldata_address_tel.getText().toString()
                                    + "&sheng=" + URLEncoder.encode((String) mSpProvince.getSelectedItem(), "utf-8") + "&shi=" + URLEncoder.encode((String) mSpCity.getSelectedItem(), "utf-8") + "&xian=" + URLEncoder.encode(mCurrentAreaName, "utf-8") + "&jiedao="
                                    + URLEncoder.encode(mEtDetailAddre.getText().toString(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        mRequestSave.personalDataSaveAddressRequests(uid, s, is_default, id, PersonaldataAddressActivity.this);

                    }
                    break;
                case R.id.rl_personaldata_address_default:
                    if (tag) {
                        tag = false;
                        mibtn_personaldata_address_default.setBackgroundResource(R.mipmap.switch_off);
                        is_default = "0";
                        ToastUtil.showToast(this, "已取消设为默认地址");

                    } else {
                        tag = true;
                        mibtn_personaldata_address_default.setBackgroundResource(R.mipmap.switch_on);
                        is_default = "1";
                        ToastUtil.showToast(this, "已设为默认地址");


                    }
                    break;
                default:
                    break;
            }
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
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
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
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
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

    //保存
    @Override
    public void OnPersonalDataSaveAddressListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                ToastUtil.showToast(this, "保存成功");
                Intent intent = new Intent(this, PersonaldataAddAddressActivity.class);
                startActivityForResult(intent, 4);
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
    public boolean onTouch(View v, MotionEvent event) {
        touch_flag++;
        if (touch_flag == 2) {
            mibtn_personaldata_address_user.setVisibility(View.VISIBLE);
            mibtn_personaldata_address_tel.setVisibility(View.VISIBLE);
            mibtn_personaldata_address.setVisibility(View.VISIBLE);
        }
        return false;
    }
}
