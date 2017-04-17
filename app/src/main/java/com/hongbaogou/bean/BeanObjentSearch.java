package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
public class BeanObjentSearch extends BaseObjectBean {

    private String total;
    private int max_page;
    private List<BeanListSearch> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
    }

    public List<BeanListSearch> getList() {
        return list;
    }

    public void setList(List<BeanListSearch> list) {
        this.list = list;
    }
}
