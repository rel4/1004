package com.hongbaogou.bean;

import java.util.List;

/**
 * 记录参与购买的对象
 */
public class JoinPeopleObject extends  BaseObjectBean{

    private String total;
    private String max_page;
    private List<JoinPeopleBean> list;

    public JoinPeopleObject() {
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

    public List<JoinPeopleBean> getList() {
        return list;
    }

    public void setList(List<JoinPeopleBean> list) {
        this.list = list;
    }
}
