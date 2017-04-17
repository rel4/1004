package com.hongbaogou.bean;

/**
 * 中奖者的对象
 */
public class WinBean {
    private String uid;
    private String username;
    private String email;
    private String mobile;
    private String img;
    private int renci;

    public WinBean() {
    }

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
