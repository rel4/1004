package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/29.
 */
public class PayCodeBean extends BaseObjectBean {

    private String order_code;
    private String callback_url;

    public PayCodeBean() {
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }
}
