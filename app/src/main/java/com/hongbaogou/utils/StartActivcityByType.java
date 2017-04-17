package com.hongbaogou.utils;

import android.content.Context;
import android.content.Intent;

import com.hongbaogou.MainActivity;
import com.hongbaogou.activity.BaseWebViewActivity;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.activity.GoodsListActivity;
import com.hongbaogou.activity.RechargeActivity;
import com.hongbaogou.activity.SearchActivity;

import java.util.Map;

/**
 * 根据不同的类型进行跳转
 */
public class StartActivcityByType {

    public static void startActivty(Context context, int type, Map<String, String> map) {
        Intent intent = createIntent(context, type);
        if(map != null)
            for (String key : map.keySet()) {
                intent.putExtra(key,map.get(key));
            }
        context.startActivity(intent);
    }

    public static Intent createIntent(Context context, int type) {
        Intent intent = null;
        switch (type) {
            case (Constant.WebView):
                intent = new Intent(context, BaseWebViewActivity.class);
                break;
            case (Constant.GoodsDetail):
                intent = new Intent(context, GoodsDetailActivity.class);
                break;
            case (Constant.Search):
                intent = new Intent(context, SearchActivity.class);
                break;
            case (Constant.CLASSIFY):
                intent = new Intent(context, GoodsListActivity.class);
                break;
            case (Constant.Home):
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("position",0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case (Constant.ShoppingCart):
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("position",3);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case (Constant.Recharge):
                intent = new Intent(context, RechargeActivity.class);
                break;
        }
        return intent;
    }


}
