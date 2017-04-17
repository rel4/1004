package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PayObjectRecodersBean extends BaseObjectBean{
    private String total;
    private String max_page;
    private List<PayRecodersBean> list;

    public PayObjectRecodersBean() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMax_page() {
        return max_page;
    }

    public void setMax_page(String max_page) {
        this.max_page = max_page;
    }

    public List<PayRecodersBean> getList() {
        return list;
    }

    public void setList(List<PayRecodersBean> list) {
        this.list = list;
    }
}
