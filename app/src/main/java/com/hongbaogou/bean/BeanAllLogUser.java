package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/5.
 */
public class BeanAllLogUser {
    public BeanAllLogUser() {
    }

    /**
     * 获奖者ID
     */
    private String uid;

    /**
     * 会员名称
     */
    private String username;

    /**
     * 幸运号码
     */
    private String q_user_code;
    /**
     * 中奖者参与的次数
     */
    private int gonumber;
    /**
     * 中奖者的头像
     */
    private String img;

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

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public int getGonumber() {
        return gonumber;
    }

    public void setGonumber(int gonumber) {
        this.gonumber = gonumber;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
