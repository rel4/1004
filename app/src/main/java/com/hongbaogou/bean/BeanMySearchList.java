package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/9.
 */
public class BeanMySearchList {

    /**
     * 晒单记录的id
     */
    private String sd_id;

    /**
     * 会员的ID
     */
    private String sd_userid;

    /**
     * 商品的ID
     */
    private String sd_shopid;

    /**
     * 商品的编号
     */
    private String sd_shopsid;

    /**
     * 期号
     */
    private String sd_qishu;

    /**
     * 晒单的标题
     */
    private String sd_title;

    /**
     * 封面的图片地址
     */
    private String sd_thumbs;

    /**
     * 晒单的内容
     */
    private String sd_content;

    /**
     * 晒单的状态
     *
     * @return
     */
    private int is_audit;

    /**
     * 晒单的事件
     */
    private String sd_time;

    /**
     * 商品的标题
     */
    private String goods_title;

    /**
     * 幸运号码
     */
    private String q_user_code;

    /**
     * 揭晓的时间
     */
    private String q_end_time;

    /**
     * 中奖者参与人次
     */

    private String sd_img;

    private String uid;


    public String getSd_img() {
        return sd_img;
    }

    public void setSd_img(String sd_img) {
        this.sd_img = sd_img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String gonumber;

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }

    /**
     * 是否可以晒单
     *
     * @return
     */
    private int is_shaidan;


    public String getSd_id() {
        return sd_id;
    }

    public void setSd_id(String sd_id) {
        this.sd_id = sd_id;
    }

    public String getSd_userid() {
        return sd_userid;
    }

    public void setSd_userid(String sd_userid) {
        this.sd_userid = sd_userid;
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

    public String getSd_qishu() {
        return sd_qishu;
    }

    public void setSd_qishu(String sd_qishu) {
        this.sd_qishu = sd_qishu;
    }

    public String getSd_title() {
        return sd_title;
    }

    public void setSd_title(String sd_title) {
        this.sd_title = sd_title;
    }

    public String getSd_thumbs() {
        return sd_thumbs;
    }

    public void setSd_thumbs(String sd_thumbs) {
        this.sd_thumbs = sd_thumbs;
    }

    public String getSd_content() {
        return sd_content;
    }

    public void setSd_content(String sd_content) {
        this.sd_content = sd_content;
    }

    public int getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(int is_audit) {
        this.is_audit = is_audit;
    }

    public String getSd_time() {
        return sd_time;
    }

    public void setSd_time(String sd_time) {
        this.sd_time = sd_time;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public int getIs_shaidan() {
        return is_shaidan;
    }

    public void setIs_shaidan(int is_shaidan) {
        this.is_shaidan = is_shaidan;
    }

}
