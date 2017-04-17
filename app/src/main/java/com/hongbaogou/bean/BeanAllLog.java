package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class BeanAllLog extends BaseObjectBean {

    /**
     * 总数
     */
    private String total;

    /**
     * 数据集合
     */
    private List<BeanAllLogList> list;


    public BeanAllLog() {

    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<BeanAllLogList> getList() {
        return list;
    }

    public void setList(List<BeanAllLogList> list) {
        this.list = list;
    }
}
