package com.hongbaogou.activity;

import android.app.AlertDialog;
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
import com.hongbaogou.listener.OnPersonalDataInviteCodeListener;
import com.hongbaogou.request.PersonalDataInviteCodeRequests;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

public class PersonaldataInviteCodeActivity extends BaseAppCompatActivity implements OnPersonalDataInviteCodeListener {
    private  String mString;
    private  String uid;
    private Button mBtn_save;
    private ImageView mIv_invitecode_bank;
    private EditText mEt_invitecode_input;
    private ImageButton mIbtn_invitecode_delete;
    private PersonalDataInviteCodeRequests mRequest;
    private AlertDialog mAlertDialog ;
    private AlertDialog.Builder mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_invite_code);
        initBarUtils.setSystemBar(this);
        initView();
        initData();
    }

    private void initView() {
        mBtn_save = (Button) findViewById(R.id.btn_save);
        mIv_invitecode_bank = (ImageView) findViewById(R.id.iv_invitecode_back);
        mEt_invitecode_input = (EditText) findViewById(R.id.et_invitecode_input);
        mIbtn_invitecode_delete = (ImageButton) findViewById(R.id.ibtn_invitecode_delete);

    }
    private void initData() {
        uid = Pref_Utils.getString(getApplicationContext(), "uid");

        mRequest = new PersonalDataInviteCodeRequests();
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_invitecode_back:
                    BaseUtils.colseSoftKeyboard(this);
                    finish();
                    break;
                case R.id.ibtn_invitecode_delete:
                    mEt_invitecode_input.getText().clear();
                    break;
                case R.id.btn_save:
                        mRequest.personalDataInviteCodeRequests(uid, mEt_invitecode_input.getText().toString(), this);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void OnPersonalDataInviteCodeListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean!=null){
            if (baseObjectBean.getStatus()==1){
                ToastUtil.showToast(PersonaldataInviteCodeActivity.this,"邀请成功-。-！");
                finish();
            }else {ToastUtil.showToast(PersonaldataInviteCodeActivity.this,"保存失败，请输入正确的邀请码");
            }
        }
    }

    @Override
    public void OnPersonalDataInviteCodeListenerFailed(VolleyError error) {

    }
}
