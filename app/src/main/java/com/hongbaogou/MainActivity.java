package com.hongbaogou;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hongbaogou.activity.SplashActivity;
import com.hongbaogou.activity.WelcomeActivity;
import com.hongbaogou.fragment.BaseFragment;
import com.hongbaogou.fragment.FindFragment;
import com.hongbaogou.fragment.HomeContentFragment;
import com.hongbaogou.fragment.HomeFragment2;
import com.hongbaogou.fragment.ListFragment;
import com.hongbaogou.fragment.UserFragment;
import com.hongbaogou.fragment.WinFragment;
import com.hongbaogou.request.BackToWebRequest;
import com.hongbaogou.request.LoginRequest;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, HomeContentFragment.OnListChangeListener {
    private static final String INAPP="1";
    private static final String PAUSEAPP="2";
    private static final String STOPAPP="3";
    // 更新版本要用到的一些信息
    private UpdateInfo info;
    private ProgressDialog pBar;

    private int checkPosition = 0;
    private List<CheckBox> checkBoxs;
    //购物车商品的数量
    private TextView listCount;
    //购物车商品的数量
    private ViewPager pager;
    private MainPagerAdapter mainPagerAdapter;
    private List<BaseFragment> baseFragments;
    private boolean[] isInitRequest = {true, false, false, false, false};
    private String messageStateChange = "messageStateChange";
    private MessageBroadcastReceiver messageBroadcastReceiver;
    private boolean isCheckVersion=false;

    @Override
    protected void onStart() {
        super.onStart();
//        Pref_Utils.putBoolean(MainActivity.this,"hongbao",true);//红包
        Pref_Utils.putBoolean(MainActivity.this,"alipay",true);//支付宝支付
        Pref_Utils.putBoolean(MainActivity.this,"wxpay",true);//微信支付
        Pref_Utils.putBoolean(MainActivity.this,"jdpay",false);//京东支付
        Pref_Utils.putBoolean(MainActivity.this,"aibeipay",false);//爱贝支付
        Pref_Utils.putBoolean(MainActivity.this,"hfbpay",false);//汇付宝支付
        Pref_Utils.putBoolean(MainActivity.this, "open_login", false);//第三方登陆
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (YYJXApplication.isStart) {
            //startInitActivity();
            boolean isFirsrt = Pref_Utils.getDefaultBoolean(this, "isFirst");
            if (isFirsrt) {
                Pref_Utils.putBoolean(this, "isFirst", false);
                initBarUtils.setSystemBar(this);
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                setContentView(R.layout.activity_main);

                messageBroadcastReceiver = new MessageBroadcastReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(messageStateChange);
                registerReceiver(messageBroadcastReceiver, intentFilter);
                initView();

                //设置Bar的颜色
                initBarUtils.setSystemBar(MainActivity.this);


                //针对具体用户发送消息
                JPushInterface.setAlias(this, Pref_Utils.getString(this, "uid"), new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {//i==0代表成功，s为设置的alias
                    }
                });
                sendDataToWeb(INAPP);
                handler1.sendEmptyMessage(1);
            }
            YYJXApplication.isStart = false;
        }

    }

    private void checkUpdateVersion(){
        Toast.makeText(MainActivity.this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                   /* UpdateInfoService updateInfoService = new UpdateInfoService(
                            MainActivity.this);
                    info = updateInfoService.getUpDateInfo();*/
                    info =  getUpDateInfo();
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    isCheckVersion=false;
                }
            };
        }.start();
    }
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    // 如果有更新就提示
                    if (isNeedUpdate()) {   //在下面的代码段
                        showUpdateDialog();  //下面的代码段
                    }
                    break;
                case 1:
                    if(!isCheckVersion){
                        isCheckVersion=true;
                        checkUpdateVersion();
                    }
                    break;
            }
        };
    };


    void downFile(final String url) {
        pBar = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    int length = (int) entity.getContentLength();   //获取文件大小
                    pBar.setMax(length);                            //设置进度条的总长度
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                "hongbaoduobao.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[10];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process);       //这里就是关键的实时更新进度了！
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    isCheckVersion=false;
                }
            }

        }.start();
    }

    void down() {
        handler1.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }
    //安装文件，一般固定写法
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "hongbaoduobao.apk")),
                "application/vnd.android.package-archive");
        this.startActivity(intent);
        isCheckVersion=false;
        android.os.Process.killProcess(android.os.Process.myPid());
//        android.os.Process
    }


    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + info.getVersion());
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getUrl());     //在下面的代码段
                } else {
                    Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                    isCheckVersion=false;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isCheckVersion=false;
            }

        });
        builder.create().show();
    }

    private boolean isNeedUpdate() {

        String v = info.getVersion(); // 最新版本的版本号
        Log.i("update",v);
//        Toast.makeText(MainActivity.this, v, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, getVersion() , Toast.LENGTH_LONG).show();
        if (v.equals(getVersion())) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
//            Toast.makeText(MainActivity.this, packageInfo.versionName , Toast.LENGTH_LONG).show();
//            Toast.makeText(MainActivity.this, String.valueOf( packageInfo.versionCode ), Toast.LENGTH_LONG).show();
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }


    private void startInitActivity() {
        boolean isFirsrt = Pref_Utils.getDefaultBoolean(this, "isFirst");
        if (isFirsrt) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        YYJXApplication.isStart = false;
    }

    public void initView() {
        baseFragments = new ArrayList<BaseFragment>();
        BaseFragment homeFragment = new HomeFragment2();
        BaseFragment winFragment = new WinFragment();
        BaseFragment findFragment = new FindFragment();
        BaseFragment listFragment = new ListFragment();
        BaseFragment userFragment = new UserFragment();
        baseFragments.add(homeFragment);
        baseFragments.add(winFragment);
        baseFragments.add(findFragment);
        baseFragments.add(listFragment);
        baseFragments.add(userFragment);

        int[] ids = {R.id.home, R.id.win, R.id.find, R.id.listing, R.id.user};
        checkBoxs = new ArrayList<CheckBox>();

        for (int i = 0; i < ids.length; i++) {
            CheckBox checkBox = (CheckBox) findViewById(ids[i]);
            checkBoxs.add(checkBox);
            checkBox.setOnClickListener(new TabOnClickListener(i));
        }

        listCount = (TextView) findViewById(R.id.listCount);
        pager = (ViewPager) findViewById(R.id.pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mainPagerAdapter);
        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(checkPosition);


    }

    private class TabOnClickListener implements View.OnClickListener {

        private int currentCheckPosition;

        public TabOnClickListener(int currentCheckPosition) {
            this.currentCheckPosition = currentCheckPosition;
        }

        public void onClick(View v) {
            switchState(currentCheckPosition);
        }
    }

    private void switchState(int currentCheckPosition) {

        CheckBox currentClickCheckBox = checkBoxs.get(currentCheckPosition);
        if (checkPosition != currentCheckPosition) {
            pager.setCurrentItem(currentCheckPosition, false);
            CheckBox clickCheckBox = checkBoxs.get(checkPosition);
            clickCheckBox.setChecked(false);
        }
        currentClickCheckBox.setChecked(true);
        checkPosition = currentCheckPosition;
        if (!isInitRequest[checkPosition]) {
            isInitRequest[checkPosition] = baseFragments.get(checkPosition).initRequest();
        }
        baseFragments.get(checkPosition).freshDate();
        //设置Bar的颜色
        initBarUtils.setSystemBar(MainActivity.this);
    }


    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public int getCount() {
            return checkBoxs.size();
        }

        public Fragment getItem(int position) {
            return baseFragments.get(position);
        }
    }

    public void onListChangeListener(int count) {
        if (count != 0) {
            listCount.setVisibility(View.VISIBLE);
            listCount.setText("" + count);
        } else {
            listCount.setVisibility(View.GONE);
        }
    }

    public View getShopCartView() {
        return checkBoxs.get(3);
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    public void onPageSelected(int position) {
        switchState(position);
    }


    public void onPageScrollStateChanged(int state) {

    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("TAG", " KeyCode=========================" + event.getKeyCode());
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (checkPosition != 0) {
                switchState(0);
                return true;
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出360夺宝", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        sendDataToWeb(STOPAPP);
        YYJXApplication.isStart = true;
        if(null!=messageBroadcastReceiver){
            unregisterReceiver(messageBroadcastReceiver);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int position = intent.getIntExtra("position", 0);
        switchState(position);
    }

    public class MessageBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == messageStateChange) {
                int message = intent.getIntExtra("count", 0);
                if (message == 0) {
                    listCount.setVisibility(View.GONE);
                    baseFragments.get(3).clearCart();
                } else {
                    listCount.setVisibility(View.VISIBLE);
                    listCount.setText(message + "");
                }

            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        sendDataToWeb(PAUSEAPP);
//        MobclickAgent.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
//        MobclickAgent.onResume(this);
//        MobclickAgent.onResume(this);
    }

    public UpdateInfo getUpDateInfo() throws Exception {
        String channel = "1004" ;
        String path = "http://v2.qcread.com/update.php?channel=" + channel;
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            // 创建一个url对象
            URL url = new URL(path);
            // 通過url对象，创建一个HttpURLConnection对象（连接）
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 通过HttpURLConnection对象，得到InputStream
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            // 使用io流读取文件
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String info = sb.toString();
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setVersion(info.split("&")[1]);
        updateInfo.setDescription(info.split("&")[2]);
        updateInfo.setUrl(info.split("&")[3]);
        return updateInfo;
    }
    private void sendDataToWeb(String type){
        String uid= Pref_Utils.getString(this, "uid");
        if(!TextUtils.isEmpty(uid)){
            BackToWebRequest mBackToWebRequest=new BackToWebRequest();
            mBackToWebRequest.backToWebRequest(uid,type);
        }

    }

}
