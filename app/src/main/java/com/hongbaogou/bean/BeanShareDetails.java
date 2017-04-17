package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/4.
 */
public class BeanShareDetails extends BaseObjectBean {
    /**
     * 晒单的id
     */
    private String sd_id;

    /**
     * 晒单用户的id
     */
    private String sd_userid;

    /**
     * 晒单商品的id
     */
    private String sd_shopid;

    /**
     * 晒单的标题
     */
    private String sd_title;

    /**
     * 晒单的内容
     */
    private String sd_content;

    /**
     * 晒单的时间
     */
    private String sd_time;

    /**
     * 商品的标题
     */
    private String title;

    /**
     * 参与的人数
     */
    private String canyurenshu;

    /**
     * 幸运号码
     */
    private String q_user_code;

    /**
     * 揭晓的时间
     */
    private String q_end_time;

    /**
     * 期数
     */
    private String sd_qishu;

    /**
     * 晒单者名称
     */
    private String username;

    /**
     * 晒单的图片列表
     */
    private String img[];

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

    public String getSd_time() {
        return sd_time;
    }

    public void setSd_time(String sd_time) {
        this.sd_time = sd_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tile) {
        this.title = tile;
    }

    public String getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(String canyurenshu) {
        this.canyurenshu = canyurenshu;
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

    public String getSd_qishu() {
        return sd_qishu;
    }

    public void setSd_qishu(String sd_qishu) {
        this.sd_qishu = sd_qishu;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getImg() {
        return img;
    }

    public void setImg(String[] img) {
        this.img = img;
    }
}
