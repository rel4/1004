package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PayObjectBean extends BaseObjectBean {

    private int count;
    private int zongrenci;
    private List<PayBean> list;

    public PayObjectBean() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getZongrenci() {
        return zongrenci;
    }

    public void setZongrenci(int zongrenci) {
        this.zongrenci = zongrenci;
    }

    public List<PayBean> getList() {
        return list;
    }

    public void setList(List<PayBean> list) {
        this.list = list;
    }
}
