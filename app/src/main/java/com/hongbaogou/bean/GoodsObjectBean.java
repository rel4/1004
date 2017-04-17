package com.hongbaogou.bean;

import java.util.List;

/**
 * 商品对象的列表
 * 分页
 */
public class GoodsObjectBean extends BaseObjectBean {

    private String total;
    private int max_page;
    private List<GoodListBean> list;

    public GoodsObjectBean() {
    }

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

    public List<GoodListBean> getList() {
        return list;
    }

    public void setList(List<GoodListBean> list) {
        this.list = list;
    }
}
