package com.hongbaogou.safewebviewbridge;

import android.webkit.WebView;

import com.hongbaogou.utils.Constant;
import com.hongbaogou.utils.StartActivcityByType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/21.
 */
public class HostJsScope {
    public static void backHome(WebView webView) {
        StartActivcityByType.startActivty(webView.getContext(), Constant.Home, null);
    }

    public static void backShoppingCart(WebView webView) {
        StartActivcityByType.startActivty(webView.getContext(), Constant.ShoppingCart, null);
    }

    public static void recharge(WebView webView) {
        StartActivcityByType.startActivty(webView.getContext(), Constant.Recharge, null);
    }


    public static void lookGoodsDetail(WebView webView, String id, String issue) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("issue", issue);
        StartActivcityByType.startActivty(webView.getContext(), Constant.GoodsDetail, map);
    }



    public static void searchByKeyWord(WebView webView , String keyWord) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", keyWord);
        StartActivcityByType.startActivty(webView.getContext(), Constant.Search, map);
    }


    public static void classify(WebView webView , String title , String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", title);
        map.put("cateid", id);
        StartActivcityByType.startActivty(webView.getContext(), Constant.CLASSIFY, map);
    }



}
