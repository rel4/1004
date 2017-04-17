package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/11/26.
 * <p/>
 * 常规类
 */
public class WinEnsureAddressBean {
    private int status;
    private String message;
    private String data;

    public WinEnsureAddressBean() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}