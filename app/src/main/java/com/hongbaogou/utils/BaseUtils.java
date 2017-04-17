package com.hongbaogou.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

/**
 * Created by Administrator on 2015/12/2.
 */
public class BaseUtils {
    /**
     * 获取随机的字符串
     *
     * @param length 字符串的个数
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            buffer.append(str.charAt(number));
        }
        return buffer.toString();
    }

    /**
     * 关闭软键盘的方法
     *
     * @param activity
     */
    public static void colseSoftKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String getChannelCode(Context context) {
        String code = getMetaData(context, "CHANNEL");
        if (code != null) {
            return code;
        }
        return "0";

    }



    private static String getMetaData(Context context, String key) {

        try {
            ApplicationInfo  ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA
            );
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }
}
