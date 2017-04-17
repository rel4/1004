package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class BaseListBean<T extends BaseListBean> {

    private int status;
    private String message;
    private List<T> data;

    public BaseListBean() {

    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }
}
