package com.hongbaogou.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.QRCodeUtil;
import com.hongbaogou.utils.initBarUtils;

import java.io.File;


public class PersonaldataQrCodeActivity extends BaseAppCompatActivity {

    private ImageView mImageView, mIv_qrcode_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata_qr_code);
        initBarUtils.setSystemBar(this);
        initView();

  /*
        String text = getIntent().getStringExtra("context");
  //生成普通二维码
        QRCodeUtil.createImage(mImageView,text);
  */


        //生成带有LOGO图片的二维码
        final String filePath = getFileRoot(PersonaldataQrCodeActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
        final String text = getIntent().getStringExtra("context");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(text.trim(), 800, 800, BitmapFactory.decodeResource(getResources(), R.mipmap.logoqr), filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();


    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.image);
        mIv_qrcode_back = (ImageView) findViewById(R.id.iv_qrcode_back);
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_qrcode_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }


}

