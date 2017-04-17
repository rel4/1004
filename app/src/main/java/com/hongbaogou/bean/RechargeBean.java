package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/16.
 */
public class RechargeBean extends BaseObjectBean {
    //"order_code": "C14495547218527647",
    //        "callback_url": "http://192.168.1.50/index.php/pay/alipay_url/houtai/"

    private String order_code;
    private String callback_url;
    public RechargeBean() {
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
