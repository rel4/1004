package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class GoodsObjectCategoryBean extends BaseObjectBean {
    private String total;
    private String max_page;
    private List<GoodsCategoryBean> list;

    public GoodsObjectCategoryBean() {


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

    public List<GoodsCategoryBean> getList() {
        return list;
    }

    public void setList(List<GoodsCategoryBean> list) {
        this.list = list;
    }
}
