package com.hongbaogou.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.listener.OnPersonalDataNicknameListener;
import com.hongbaogou.request.PersonalDataNicknameRequests;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PersonaldataNicknameActivity extends BaseAppCompatActivity implements OnPersonalDataNicknameListener {
    private ImageView mImageView;
    private Button mButton;
    private ImageButton mImageButton;
    private EditText mEditText;
    private AlertDialog alertDialog = null;
    private PersonalDataNicknameRequests mRequest;
    private String uid;
    private String username;
    private Intent mIntent;
    private String mStringExtra;
    private String strUTF8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_nickname);
        initBarUtils.setSystemBar(this);
        initview();
    }

    private void initview() {
        mImageView = (ImageView) findViewById(R.id.iv_personaldata_nickname);
        mButton = (Button) findViewById(R.id.titleright);
        mImageButton = (ImageButton) findViewById(R.id.ibtn_personaldata_delete);
        mEditText = (EditText) findViewById(R.id.et_personaldata_nickname_input);
        mIntent = getIntent();
        mStringExtra = mIntent.getStringExtra("nickname");
        mEditText.setText(mStringExtra);
        uid = Pref_Utils.getString(getApplicationContext(), "uid");
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_personaldata_nickname:
                    BaseUtils.colseSoftKeyboard(this);
                    finish();
                    break;
                case R.id.titleright:
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(PersonaldataNicknameActivity.this);
                    if (mEditText.getText().toString().isEmpty()||mEditText.getText().toString().equals(mStringExtra)) {
                        mDialog.setMessage("昵称未修改或没有输入，请从新输入");

//                        mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                BaseUtils.colseSoftKeyboard(PersonaldataNicknameActivity.this);
//                                alertDialog.dismiss();
//                            }
//                        });
                        mDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog = mDialog.create();
                        alertDialog.show();
                    } else {
                        mRequest = new PersonalDataNicknameRequests();
                        username = mEditText.getText().toString();
                        try {
                            strUTF8 = URLDecoder.decode(username, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        mRequest.personalDataNicknameRequestsRequests(uid, strUTF8, this);
                        System.out.println("===============strUTF8==========》" + strUTF8);
                        mIntent = new Intent();
                        mIntent.putExtra("nickname", strUTF8);
                        Pref_Utils.putString(getApplicationContext(), "username", strUTF8);
                        setResult(6, mIntent);
                        finish();
                    }
                    break;
                case R.id.ibtn_personaldata_delete:
                    mEditText.getText().clear();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnPersonalDataNicknameListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            int status = baseObjectBean.getStatus();
            if (status == 1) {
                Pref_Utils.putString(getApplicationContext(), "username", strUTF8);
                ToastUtil.showToast(this, "昵称修改成功");

            } else {
                ToastUtil.showToast(this, "昵称未修改");
            }
        } else {
            ToastUtil.showToast(this, "获取内容失败");
        }
    }

    @Override
    public void OnPersonalDataNicknameListeneroFailed(VolleyError error) {

    }
}