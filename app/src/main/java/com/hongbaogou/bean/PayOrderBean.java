package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PayOrderBean extends BaseObjectBean {

    //总价
    private int total_money;
    //会员余额
    private String user_money;
    //其他支付金额
//    private int pay_money;
    private float pay_money;
    private List<PayOrderGoodsBean> list;

    public PayOrderBean() {
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public float getPay_money() {
        return pay_money;
    }

    public void setPay_money(float pay_money) {
        this.pay_money = pay_money;
    }

    public List<PayOrderGoodsBean> getList() {
        return list;
    }

    public void setList(List<PayOrderGoodsBean> list) {
        this.list = list;
    }
}
