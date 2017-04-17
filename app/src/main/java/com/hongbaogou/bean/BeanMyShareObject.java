package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class BeanMyShareObject extends BaseObjectBean {
    /**
     * 总数
     */
    private String total;
    /**
     * 最大页数
     */
    private int max_page;
    /**
     * 数据列表
     */
    private List<BeanMySearchList> list;


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

    public List<BeanMySearchList> getList() {
        return list;
    }

    public void setList(List<BeanMySearchList> list) {
        this.list = list;
    }
}
