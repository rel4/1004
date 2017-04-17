package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ShareListBean extends ShareObjectBean {



    /**
     * 商品的唯一标示id
     */
    private String sd_id;
    /**
     * 商品详情的id
     */
    private String sd_shopid;
    /**
     * 商品的编号
     */
    private String sd_shopsid;
    /**
     * 标题
     */
    private String sd_title;
    /**
     * 内容
     */
    private String sd_content;
    /**
     * 上单图片列表
     */
    private String[] sd_photolist;
    /**
     * 晒单的时间
     */
    private String sd_time;
    /**
     * 商品的标题
     */
    private String title;

    /**
     * 会员信息
     */
    private UserBean q_user;

    /**
     * 期数
     *
     * @return
     */
    private String qishu;

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getSd_id() {
        return sd_id;
    }

    public void setSd_id(String sd_id) {
        this.sd_id = sd_id;
    }

    public String getSd_shopid() {
        return sd_shopid;
    }

    public void setSd_shopid(String sd_shopid) {
        this.sd_shopid = sd_shopid;
    }

    public String getSd_shopsid() {
        return sd_shopsid;
    }

    public void setSd_shopsid(String sd_shopsid) {
        this.sd_shopsid = sd_shopsid;
    }

    public String getSd_title() {
        return sd_title;
    }

    public void setSd_title(String sd_title) {
        this.sd_title = sd_title;
    }

    public String getSd_content() {
        return sd_content;
    }

    public void setSd_content(String sd_content) {
        this.sd_content = sd_content;
    }

    public String[] getSd_photolist() {
        return sd_photolist;
    }

    public void setSd_photolist(String[] sd_photolist) {
        this.sd_photolist = sd_photolist;
    }

    public String getSd_time() {
        return sd_time;
    }

    public void setSd_time(String sd_time) {
        this.sd_time = sd_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserBean getQ_user() {
        return q_user;
    }

    public void setQ_user(UserBean q_user) {
        this.q_user = q_user;
    }

}
