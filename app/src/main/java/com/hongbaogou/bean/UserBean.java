package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/1.
 */
public class UserBean {
    /**
     * 会员id
     */
    private String uid;
    /**
     * 会员名称
     */
    private String username;
    /**
     * 会员邮箱
     */
    private String email;
    /**
     * 会员手机号码
     */
    private String mobile;
    /**
     * 会员头像
     */
    private String img;
    /**
     * 购买人次
     */
    private int renci;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRenci() {
        return renci;
    }

    public void setRenci(int renci) {
        this.renci = renci;
    }
}
