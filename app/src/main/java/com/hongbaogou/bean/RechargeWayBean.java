package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/16.
 */
public class RechargeWayBean extends BaseListBean {
    //"pay_name": "财付通",
     //       "pay_class": "tenpay",
     //       "pay_type": "1"
    private String pay_name;
    private String pay_class;
    private String pay_type;

    public RechargeWayBean() {
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getPay_class() {
        return pay_class;
    }

    public void setPay_class(String pay_class) {
        this.pay_class = pay_class;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
