package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PayRecodersBean extends  BaseListBean{
    //"uid": "5",
       //     "pay_type": "支付宝",
      //      "money": "9.00",
        //    "status": 0,
         //   "time": "2015-12-02 12:01:58"

    private String uid;
    private String pay_type;
    private Double money;
    private int status;
    private String time;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PayRecodersBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
