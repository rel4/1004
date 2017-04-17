package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ShareObjectBean extends BaseObjectBean {
    /**
     * 该分类下的商品总数
     */
    private int total;
    /**
     * 最大页数
     */
    private int max_page;

    /**
     * 商品列表
     */
    private List<ShareListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
    }

    public List<ShareListBean> getList() {
        return list;
    }

    public void setList(List<ShareListBean> list) {
        this.list = list;
    }
}
