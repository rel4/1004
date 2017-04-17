package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class BeanNumber extends BaseObjectBean {
    private String shopname;
    private int qishu;
    private int renci;

    private List<BeanNumberList> list;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getQishu() {
        return qishu;
    }

    public void setQishu(int qishu) {
        this.qishu = qishu;
    }

    public int getRenci() {
        return renci;
    }

    public void setRenci(int renci) {
        this.renci = renci;
    }

    public List<BeanNumberList> getList() {
        return list;
    }

    public void setList(List<BeanNumberList> list) {
        this.list = list;
    }
}
