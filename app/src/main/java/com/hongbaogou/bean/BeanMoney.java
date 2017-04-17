package com.hongbaogou.bean;

/**
 * Created by intasect on 2016/11/21.
 */
public class BeanMoney  extends BaseObjectBean{
    /**
     * 会员的id
     */
    private String uid;
    /**
     * 用户的账户余额
     */
    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
