package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PayListRecodersBean extends BaseListBean{
    private int status;
    private String message;
    private List<PayRecodersBean> data;

    public PayListRecodersBean() {
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public List<PayRecodersBean> getList() {
        return data;
    }

    public void setList(List<PayRecodersBean> data) {
        this.data = data;
    }
}
