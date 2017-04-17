package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/11/26.
 * <p/>
 * 常规类
 */
public class BaseObjectBean<T extends BaseObjectBean> {

    private int status;
    private String message;
    private T data;

    public int getIs_mobile() {
        return is_mobile;
    }

    public void setIs_mobile(int is_mobile) {
        this.is_mobile = is_mobile;
    }

    private int is_mobile;
    public BaseObjectBean() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
