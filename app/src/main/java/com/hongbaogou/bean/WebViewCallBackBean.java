package com.hongbaogou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2016/1/3.
 */
public class WebViewCallBackBean extends BaseObjectBean implements Serializable {
//    "zongrenci": 2600,
//            "count": 1,
//            "list": [
//    {
//        "shopid": "505",
//            "shopname": "佳能 EOS 70D 套机 EF-S 18-135mm f/3.5-5.6 IS STM 定义单反新标准！",
//            "shopqishu": "1",
//            "gonumber": "2600",
//            "goucode": "10002480,10000848,10001962,10002393,10002180,10000432"

    private String zongrenci;
    private String count;
    private List<WebViewGoolsListBean> list;

    public WebViewCallBackBean() {
    }

    public String getZongrenci() {
        return zongrenci;
    }

    public void setZongrenci(String zongrenci) {
        this.zongrenci = zongrenci;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<WebViewGoolsListBean> getList() {
        return list;
    }

    public void setList(List<WebViewGoolsListBean> list) {
        this.list = list;
    }
}
