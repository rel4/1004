package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/18.
 */
public class PersonalDataFriendRewardBean extends BaseObjectBean {
    //"uid": "65","jifen": 4,"mobile": "2532108285@qq.com"},{"uid": "65","jifen": 4,"mobile": "2532108285@qq.com"
    private String uid;
    private String jifen;
    private String mobile;

    public PersonalDataFriendRewardBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
