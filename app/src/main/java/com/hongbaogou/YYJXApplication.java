package com.hongbaogou;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hongbaogou.utils.RequestManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/11/26.
 */
public class YYJXApplication extends Application {


    public static String DEVICE_ID;
    public static boolean isStart ;
    public static  Context applicationContext;
    public static final String APP_ID = "wx87263cf89febc0ff";
    // IWXAPI 是第三方app和微信通信的openapi接口
    public static IWXAPI api;

    private PackageInfo packageInfo;

    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
        applicationContext=this;

        api = WXAPIFactory.createWXAPI(this, YYJXApplication.APP_ID, false);
        api.registerApp(YYJXApplication.APP_ID);


        isStart = true;
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();


        try {
            packageInfo =  getApplicationContext().getPackageManager().getPackageInfo("com.cdcm", 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("版本号获取异常", e.getMessage());
        }


        JPushInterface.init(this);
        JPushInterface.setDebugMode(false);//设置debug模式,正式打包的时候,应该关闭


    }


    /**
     * 获得版本号
     */
    public int getVerCode(Context context){
        return packageInfo.versionCode;
    }

    /**
     * 获得版本名称
     */
    public String getVerName(Context context){
        return packageInfo.versionName;
    }

}
