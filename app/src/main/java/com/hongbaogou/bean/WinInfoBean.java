package com.hongbaogou.bean;

/**
 * 获取中奖者信息
 */
public class WinInfoBean extends  BaseObjectBean{
    private String uid;
    private String q_user_code;
    private String username;
    private String img;
    private int renci;
    private String qushu;
    private String address;
    private String q_end_time;

    public WinInfoBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }

    public String getQushu() {
        return qushu;
    }

    public void setQushu(String qushu) {
        this.qushu = qushu;
    }

    public int getRenci() {
        return renci;
    }

    public void setRenci(int renci) {
        this.renci = renci;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }
}
