package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/30.
 */
public class PaySuccessBean extends BaseObjectBean {

    private String count;
    private String zongrenci;
    private String code;

    public PaySuccessBean() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZongrenci() {
        return zongrenci;
    }

    public void setZongrenci(String zongrenci) {
        this.zongrenci = zongrenci;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
