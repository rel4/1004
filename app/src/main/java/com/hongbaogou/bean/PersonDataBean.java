package com.hongbaogou.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/12/7.
 */
public class PersonDataBean extends BaseObjectBean implements Serializable{
    //"uid": "1",
      //      "username": "猫不吃鱼",
      //      "reg_key": "6322270@qq.com",
       //     "email": "6322270@qq.com",
       //     "mobile": "",
        //    "img": "http://192.168.1.50/statics/uploads/photo/member.jpg"

    private String uid;
    private String username;
    private String reg_key;
    private String email;
    private String mobile;
    private String img;

    public PersonDataBean() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReg_key() {
        return reg_key;
    }

    public void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
