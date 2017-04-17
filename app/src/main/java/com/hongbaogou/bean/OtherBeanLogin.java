package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/8.
 */
public class OtherBeanLogin extends BaseObjectBean {

    /**
     * 会员的id
     */
    private String uid;

    /**
     * 是否已验证手机
     * <p/>
     * 非1 都表示未验证
     */
    private String mobilecode;

    /**
     * 是否已验证邮箱
     * <p/>
     * 非1 都表示未验证
     */
    private String emailcode;

    /**
     * 会员用户名称
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户的手机号码
     */
    private String mobile;

    /**
     * 用户的账户余额
     */
    private String money;

    /**
     * 用户的头像
     */
    private String img;

    /**
     * 我的邀请人的号码
     */
    private String yaoqing;

    /**
     * 返利统计
     * 共有多少人获得返利
     */
    private String rebate_total;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobilecode() {
        return mobilecode;
    }

    public void setMobilecode(String mobilecode) {
        this.mobilecode = mobilecode;
    }

    public String getEmailcode() {
        return emailcode;
    }

    public void setEmailcode(String emailcode) {
        this.emailcode = emailcode;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getYaoqing() {
        return yaoqing;
    }

    public void setYaoqing(String yaoqing) {
        this.yaoqing = yaoqing;
    }

    public String getRebate_total() {
        return rebate_total;
    }

    public void setRebate_total(String rebate_total) {
        this.rebate_total = rebate_total;
    }
}
