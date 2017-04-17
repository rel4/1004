package com.hongbaogou.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.HeadImageBean;
import com.hongbaogou.bean.PersonDataBean;
import com.hongbaogou.bean.WinCodeBean;
import com.hongbaogou.bean.WinEnsureAddressBean;
import com.hongbaogou.listener.OnPersonalDataTouimgListener;
import com.hongbaogou.listener.OnQrCodeListener;
import com.hongbaogou.listener.OnWinEnsureAddressListener;
import com.hongbaogou.request.PersonalDataTouimgRequests;
import com.hongbaogou.request.QrCodeRequest;
import com.hongbaogou.utils.HeadImageViewUtils;
import com.hongbaogou.utils.MD5;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.circleimageview.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;


public class
        PersonaldataActivity extends BaseAppCompatActivity implements  OnWinEnsureAddressListener, OnQrCodeListener, OnPersonalDataTouimgListener {

    private ImageLoader mImageLoader;

    private PersonDataBean bean;

    private Intent mIntent;
    private String mRebate_total;
    private static String uid;//用户ID
    private Bitmap head;//头像Bitmap
    private static String path =Environment.getExternalStorageDirectory() + "/myHead/";//sd路径
    private String newName = "head.jpg"; //头像文件名
    private String actionUrl = "http://v2.qcread.com/index.php/yungouapi/member/do_upload_touimg";//上传的地址

//    private PersonalDataRequests mRequest;
    private PersonalDataTouimgRequests mRequestTouimg;

    private ImageView mBtn_back;
    private CircleImageView mCircleImageView;
    private TextView mtv_personaldata_id,
            mtv_personaldata_account,
            mtv_personaldata_nickname,
            mtv_personaldata_telnum,
            mTv_personaldata_friend;
    private RelativeLayout
            mRl_personaldata_head,
            mRl_personaldata_nickname,
            mRl_personaldata_telnum,
            mRl_personaldata_address,
            mRl_personaldata_friend,
            mRl_personaldata_invitecode,
            mRl_personaldata_qrcode;

    private QrCodeRequest mQrCodeRequest;
    private WinCodeBean mWinCodeBean;
    private String mQrCode;

    private ImageLoader.ImageListener lis;
    private ImageLoader.ImageContainer headImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        mImageLoader = RequestManager.getImageLoader();
        initView();
        initBarUtils.setSystemBar(this);
//        mRequest = new PersonalDataRequests();
        //       mRequest.personalDataRequests(uid, this);
        mQrCodeRequest = new QrCodeRequest();

        mQrCodeRequest.WinInfoRequest(uid,this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //mRequest.personalDataRequests(uid, this);

    }

    @Override
    public void onResume() {
        super.onResume();
        //mRequest.personalDataRequests(uid, this);
    }

    private void initView() {

        Log.e("TAG", "--->" + Pref_Utils.getString(getApplicationContext(), "headImage"));

        mBtn_back = (ImageView) findViewById(R.id.btn_back);
        mtv_personaldata_id = (TextView) findViewById(R.id.tv_personaldata_id);
        mtv_personaldata_telnum = (TextView) findViewById(R.id.tv_personaldata_telnum);
        mtv_personaldata_account = (TextView) findViewById(R.id.tv_personaldata_account);
        mtv_personaldata_nickname = (TextView) findViewById(R.id.tv_personaldata_nickname);

        mTv_personaldata_friend = (TextView) findViewById(R.id.tv_personaldata_friend);

        mRl_personaldata_head = (RelativeLayout) findViewById(R.id.rl_personaldata_head);
        mRl_personaldata_telnum = (RelativeLayout) findViewById(R.id.rl_personaldata_telnum);
        mRl_personaldata_address = (RelativeLayout) findViewById(R.id.rl_personaldata_address);
        mRl_personaldata_nickname = (RelativeLayout) findViewById(R.id.rl_personaldata_nickname);

        mRl_personaldata_friend = (RelativeLayout) findViewById(R.id.rl_personaldata_friend);
        mRl_personaldata_qrcode = (RelativeLayout) findViewById(R.id.rl_personaldata_qrcode);
        mRl_personaldata_invitecode = (RelativeLayout) findViewById(R.id.rl_personaldata_invitecode);

        mCircleImageView = (CircleImageView) findViewById(R.id.image_personaldata_head);

        uid = Pref_Utils.getString(getApplicationContext(), "uid");


 //       lis = ImageLoader.getImageListener(mCircleImageView, R.mipmap.img_blank, R.mipmap.img_blank);
  //     ImageLoader.ImageContainer headImage = mImageLoader.get(Pref_Utils.getString(this.getApplicationContext(), "headImage"), lis);
   //     SavePicInLocal(headImage.getBitmap());
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
            mCircleImageView.setImageDrawable(drawable);
        } else {
            /**
             *	如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             */
            lis = ImageLoader.getImageListener(mCircleImageView, R.mipmap.img_blank, R.mipmap.img_blank);

            System.out.println("-------头像--------" + Pref_Utils.getString(getApplicationContext(), "headImage"));
            if(Pref_Utils.getString(getApplicationContext(), "headImage")!=null){
                headImage = mImageLoader.get(Pref_Utils.getString(getApplicationContext(), "headImage"), lis);
                HeadImageViewUtils.setPicToView(headImage.getBitmap(),path);
            }
        }
//        Bitmap bt = BitmapFactory.decodeFile(path + newName);//从Sd中找头像，转换成Bitmap
//        if (bt != null) {
//            mCircleImageView.setImageBitmap(bt);
//        } else {
//            lis = ImageLoader.getImageListener(mCircleImageView, R.mipmap.img_blank, R.mipmap.img_blank);
//            headImage = mImageLoader.get(Pref_Utils.getString(this.getApplicationContext(), "headImage"), lis);
//            SavePicInLocal(headImage.getBitmap());
//
//            mImageLoader.get(Pref_Utils.getString(getApplicationContext(), "headImage"), new ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                    Bitmap imageContainerBitmap = imageContainer.getBitmap();
//                    //圆形头像加载
//                    mCircleImageView.setImageBitmap(imageContainerBitmap);
//                }
//
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                }
//            }, 80, 80);
//        }

        mtv_personaldata_id.setText(Pref_Utils.getString(getApplicationContext(), "uid"));
        mtv_personaldata_account.setText(Pref_Utils.getString(getApplicationContext(), "account"));
        mtv_personaldata_nickname.setText(Pref_Utils.getString(getApplicationContext(), "username"));
        mtv_personaldata_telnum.setText(Pref_Utils.getString(getApplicationContext(), "mobile"));

//设置人数
        mRebate_total = Pref_Utils.getString(getApplicationContext(), "rebate_total");
        System.out.println("====================>" + mRebate_total);
        // String string = getString(mRebate_total);
        if (mRebate_total != null) {
            SpannableString msp = new SpannableString("已有" + mRebate_total + "人获得返利");
            msp.setSpan(new ForegroundColorSpan(Color.RED), 2, mRebate_total.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTv_personaldata_friend.setText(msp);
            //  mTv_personaldata_friend.setText(Html.fromHtml("<font color=red>uid</font>人获得返利"));
        } else {
            SpannableString msp = new SpannableString("已有" + "0" + "人获得返利");
            msp.setSpan(new ForegroundColorSpan(Color.RED), 2, 1 + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTv_personaldata_friend.setText(msp);
        }

    }

    private static String getString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                //修改头像
                case R.id.rl_personaldata_head:
                    ShowDialog();
                    break;
                //修改昵称
                case R.id.rl_personaldata_nickname:
                    mIntent = new Intent(PersonaldataActivity.this, PersonaldataNicknameActivity.class);
                    mIntent.putExtra("nickname", Pref_Utils.getString(getApplicationContext(), "username"));
                    startActivityForResult(mIntent, 5);
                    //startActivity(mIntent);
                    break;
                //手机号码绑定
                case R.id.rl_personaldata_telnum:
                    mIntent = new Intent(PersonaldataActivity.this, PersonaldataBindTelActivity.class);
                    mIntent.putExtra("phone", Pref_Utils.getString(getApplicationContext(), "mobile"));
                    startActivityForResult(mIntent, 4);
                    //  startActivity(mIntent);
                    break;
                //地址管理
                case R.id.rl_personaldata_address:
                    startActivity(new Intent(PersonaldataActivity.this, PersonaldataAddAddressActivity.class));
                    break;
                //邀请好友
                case R.id.rl_personaldata_friend:
                    startActivity(new Intent(PersonaldataActivity.this, PersonaldataFriendActivity.class));
                    break;
                //邀请码
                case R.id.rl_personaldata_invitecode:
                    startActivity(new Intent(PersonaldataActivity.this, PersonaldataInviteCodeActivity.class));
                    break;
                //生成二维码
                case R.id.rl_personaldata_qrcode:
                    if(mQrCode!=null){
                        Intent intent = new Intent(PersonaldataActivity.this, PersonaldataQrCodeActivity.class);
                        intent.putExtra("context", mQrCode);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static String[] mListbank = new String[]{"拍照", "相册"};
    private static AlertDialog.Builder mBuilder;
    private static AlertDialog mAlertDialog;

    public void ShowDialog() {
        mBuilder = new AlertDialog.Builder(PersonaldataActivity.this);
        mBuilder.setTitle("修改头像：");
        mBuilder.setItems(mListbank, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        opeanCamera();
                        ToastUtil.showToast(PersonaldataActivity.this, "准备拍照");
                        break;
                    case 1:
                        opeanPhotoalbum();
                        break;
                    default:
                        break;
                }
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    private void opeanCamera() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));
        startActivityForResult(intent2, 2);//采用ForResult打开

    }

    private void opeanPhotoalbum() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, 1);
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    private boolean b;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));
                }//裁剪图片
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");

                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        HeadImageViewUtils.setPicToView(head, path);
                        sendImage(head, actionUrl);


                        head = HeadImageViewUtils.compressImage(head, 100);
                        mCircleImageView.setImageBitmap(head);
                    } else {
                        //mRequest.personalDataRequests(uid, this);
                    }
                }
                break;
            default:
                break;

        }
        if (resultCode == 6) {
            mtv_personaldata_nickname.setText(data.getStringExtra("nickname"));
            System.out.println("==========data=============>" + data.getStringExtra("nickname"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //压缩图片
    private Bitmap compressImage(Bitmap image, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
  private   String s;
    //上传成功后调用
    @Override
    public void OnPersonalDataTouimgListenerSuccess(HeadImageBean headImageBean) {
        if (headImageBean != null) {
            if (headImageBean.getStatus() == "1") {
                ToastUtil.showToast(PersonaldataActivity.this, "上传成功");
                s =headImageBean.getData();
                System.out.println("========headImageBean==============》" + headImageBean.getData());
                Pref_Utils.putString(getApplicationContext(), "headImage", s);
            } else {

            }
        }
    }

    @Override
    public void OnPersonalDataTouimgListenerFailed(VolleyError error) {

    }

    //向服务器传入图像
    private void sendImage(Bitmap bm, String url) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        String data = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("partner", "ZL888ANDROID");
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("sign", MD5.getStringMD5("ZL888ANDROID" + String.valueOf(System.currentTimeMillis() / 1000) + "3860DD4B6E04448D3666B3F94CFA3DD7"));
        params.put("uid", uid);
        params.put("touimg", data);

        System.out.println("-------------"+params);


        client.post(actionUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);

                System.out.println("------头像上传response--------"+content);

                HeadImageBean headImageBean = JSON.parseObject(content, HeadImageBean.class);
                headImageBean.getData();
                System.out.println("========headImageBean==============》" + headImageBean.getData());
                Pref_Utils.putString(getApplicationContext(), "headImage", headImageBean.getData());
            }
        });

        mBuilder = new AlertDialog.Builder(PersonaldataActivity.this);
        mBuilder.setMessage("头像上传成功");
        mBuilder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();


        mRequestTouimg = new PersonalDataTouimgRequests();
        mRequestTouimg.personalDataTouimgRequests(PersonaldataActivity.this);
    }

    @Override
    public void OnWinEnsureAddressListenerSuccess(WinEnsureAddressBean winEnsureAddressBean) {
        if (winEnsureAddressBean != null) {
            if (winEnsureAddressBean.getStatus() == 1) {
                winEnsureAddressBean.getData();
            }
        }
    }

    @Override
    public void OnWinEnsureAddressListenerFailed(VolleyError error) {

    }

    @Override
    public void OnQrCodeListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                mWinCodeBean = (WinCodeBean) baseObjectBean.getData();
                mQrCode = mWinCodeBean.getDownload();
                System.out.println("===mQrCode=============》" + mQrCode);
            }
        }
    }

    @Override
    public void OnQrCodeListenerFailed(VolleyError error) {

    }
}