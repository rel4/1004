package com.hongbaogou.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/1/3.
 */




public class WebViewGoolsListBean implements Serializable {
    private String shopid;
    private String shopname;
    private String shopqishu;
    private String gonumber;
    private String goucode;

    public WebViewGoolsListBean() {
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopqishu() {
        return shopqishu;
    }

    public void setShopqishu(String shopqishu) {
        this.shopqishu = shopqishu;
    }

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public String getGoucode() {
        return goucode;
    }

    public void setGoucode(String goucode) {
        this.goucode = goucode;
    }
}
