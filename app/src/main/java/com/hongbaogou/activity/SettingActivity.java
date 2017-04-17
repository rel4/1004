package com.hongbaogou.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.AndroidObj;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanAndroid;
import com.hongbaogou.httpApi.API;
import com.hongbaogou.listener.OnGetVersionListener;
import com.hongbaogou.request.GetVersionRequest;
import com.hongbaogou.utils.DataCleanManager;
import com.hongbaogou.utils.GetVersionUtils;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingActivity extends BaseAppCompatActivity implements View.OnClickListener, OnGetVersionListener {

    private TextView title;

    //清除缓存
    private RelativeLayout cacheClean;
    //缓存有多大
    private TextView cache_how_old;
    //记录缓存的大小
    private String cache;
    //关于360夺宝
    private RelativeLayout mRelativeLayout;
    //检查更新
    private RelativeLayout upDate;
    private TextView tv_apdate;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //退出登录按钮
        findViewById(R.id.btn_backlogin).setOnClickListener(this);
        //常见问题按钮
        findViewById(R.id.relativeLayout_question).setOnClickListener(this);
        //回退按钮的点击
        findViewById(R.id.btn_back).setOnClickListener(this);

        //缓存清除的点击事件
        cacheClean = (RelativeLayout) findViewById(R.id.cleancache);
        cacheClean.setOnClickListener(this);

        //获取缓存的大小
        try {
            cache = DataCleanManager.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache_how_old = (TextView) findViewById(R.id.cache_how_old);

        cache_how_old.setText(cache);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.set);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.about);
        mRelativeLayout.setOnClickListener(this);
        upDate = (RelativeLayout) findViewById(R.id.update);
        upDate.setOnClickListener(this);

        tv_apdate = (TextView) findViewById(R.id.tv_apdate);
        tv_apdate.setText(GetVersionUtils.getVersionName(this));

        initBarUtils.setSystemBar(this);
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            //退出登录的按钮
            case R.id.btn_backlogin:
                back();
                break;
            //常见问题
            case R.id.relativeLayout_question:
                intent.setClass(this, BaseWebViewActivity.class);
                intent.putExtra("url", API.QUESTION_API);
                intent.putExtra("title", "常见问题");
                startActivity(intent);
                break;
            //回退按钮
            case R.id.btn_back:
                finish();

 //               BaseUtils.colseSoftKeyboard(this);

                break;
            //点击清除缓存
            case R.id.cleancache:
                String size = "";
                //清除全部的缓存
                boolean isOK = DataCleanManager.clearAllCache(getApplicationContext());
                if (isOK) {
                    try {
                        //重新计算当前缓存的大小
                        size = DataCleanManager.getTotalCacheSize(getApplicationContext());
                        //设置当前缓存
                        cache_how_old.setText(size);
                        ToastUtil.showToast(this, "清除成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
                break;
            case R.id.about:
                intent.setClass(this, BaseWebViewActivity.class);
                intent.putExtra("title", "关于360夺宝");
                intent.putExtra("url", API.ABOUT_API);
                startActivity(intent);
                break;
            //点击更新
            case R.id.update:
                //检测更新
                //ToastUtil.showToast(this, "正在检测更新...");
                upDate.setClickable(false);
                getVersionMenthod();
                break;
            default:
                break;
        }
    }

    private void back() {
        builder = new AlertDialog.Builder(this);  //先得到构造器
        //设置窗口点击外部不消失
        builder.setCancelable(false);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                YYJXApplication.isLogin = false;
                Pref_Utils.putBoolean(SettingActivity.this, "isLogin", false);
                Pref_Utils.cleanAllData(SettingActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    //测试用的下载链接APK
    //private String link = "http://dingphone.ufile.ucloud.com.cn/apk/guanwang/time2plato.apk";

    public void getVersionMenthod() {
        GetVersionRequest getVersionRequest = new GetVersionRequest();
        getVersionRequest.requestGetVersion(this);
    }

    /**
     * 请求成功的回调
     *
     * @param baseObjectBean
     */
    @Override
    public void requestGetVersionSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            //获取当前的版本号
            int versionCode = GetVersionUtils.getVersionCode(this);
            //获得版本的名字
            String versionName = GetVersionUtils.getVersionName(this);

            Log.e("TAG", "versionName = " + versionName + "   --versionCode = " + versionCode);

            BeanAndroid android = (BeanAndroid) baseObjectBean.getData();
            AndroidObj androidObj = android.getAndroid();
            //获取版本号
            int code = androidObj.getCode();
            Log.e("TAG", "Json版本号 = " + code + "  versionCode = " + versionCode + " versionName = " + versionName);
            if (code != versionCode && code > versionCode) {
                //窗口提示更新
                dialogUpdate(baseObjectBean);
            } else {
                ToastUtil.showToast(this, "当前已经是最新版本~");
                upDate.setClickable(true);
            }
        }
    }

//    /**
//     * 显示窗口提示用户是否更新
//     */
//    private void dialogUpDateVersion() {
//        dialogUpdate();
//    }

    /**
     * 请求失败的回调
     *
     * @param error
     */
    @Override
    public void requestGetVersionFailed(VolleyError error) {

    }

    private void dialogUpdate(final BaseObjectBean baseObjectBean) {
        builder = new AlertDialog.Builder(this);  //先得到构造器
        //设置窗口点击外部不消失
        builder.setCancelable(false);
        builder.setTitle("发现新版本"); //设置标题
        builder.setMessage("是否确认更新?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                upDate.setClickable(true);
                updateVersion(baseObjectBean);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                upDate.setClickable(true);
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    /**
     * 处理更新
     */
    private void updateVersion(BaseObjectBean baseObjectBean) {
        BeanAndroid android = (BeanAndroid) baseObjectBean.getData();
        AndroidObj androidObj = android.getAndroid();
        //获取下载的链接
        String link = androidObj.getLink();
        Log.e("TAG", "link = " + link);
        //下载apk通知显示进度
        intoDownloadManager(link);
    }


    /**
     * DownloadManager 系统的方法
     *
     * @param link 下载的链接
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void intoDownloadManager(String link) {

        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("download", "yyzl.apk");
        //设置下载的描述
        request.setDescription("360夺宝新版本下载");
        //设置通知栏显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置是文件类型，具体对应apk类型。
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        SharedPreferences sPreferences = getSharedPreferences("downloadplato", 0);
        sPreferences.edit().putLong("plato", refernece).commit();

    }


    /**
     * 下载apk文件
     *
     * @param path
     * @param pb
     * @return
     * @throws Exception
     */
    private File downloadApk(String path, ProgressDialog pb) throws Exception {
        //创建本地文件对象
        File file = new File(Environment.getExternalStorageDirectory(), getFileName(path));
        //创建HttpURL连接
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            int max = conn.getContentLength();
            //设置进度条对话框的最大值
            pb.setMax(max);
            int count = 0;
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                //设置进度条对话框进度
                count = count + len;
                pb.setProgress(count);
            }
            is.close();
            fos.close();
        }
        return file;
    }

    /**
     * 获取文件名
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
