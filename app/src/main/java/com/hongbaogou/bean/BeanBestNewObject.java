package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/28.
 * <p/>
 * 最新揭晓data类
 */
public class BeanBestNewObject extends BaseObjectBean {

    /**
     * total
     */
    private String total;

    /**
     * 总页数
     */
    private String max_page;

    /**
     * list数组
     */
    private List<BeanBestNewList> list;

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

    public List<BeanBestNewList> getList() {
        return list;
    }

    public void setList(List<BeanBestNewList> list) {
        this.list = list;
    }
}
