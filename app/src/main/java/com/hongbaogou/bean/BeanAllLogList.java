package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/5.
 */
public class BeanAllLogList {

    public BeanAllLogList() {
    }

    //0 非十元  1表示十元区
    private int is_ten;

    /**
     * 商品的名称
     */
    private String shopname;

    /**
     * 商品id标识
     */
    private String shopid;

    /**
     * 期号
     */
    private String shopqishu;

    /**
     * 本期参与总人数
     */
    private String gonumber;

    /**
     * tag  0已揭晓  1进行中  2倒计时
     */
    private int tag;

    /**
     * 总需人数
     */
    private String zongrenshu;

    /**
     * 已经参与人数
     */
    private String canyurenshu;

    /**
     * 比率
     */
    private int ratio;

    /**
     * 商品图片地址
     */
    private String thumb;

    /**
     * 获奖者信息
     */
    private BeanAllLogUser q_user;

    /**
     * 揭晓时间
     */
    private String q_end_time;


    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopqishu() {
        return shopqishu;
    }

    public void setShopqishu(String shopqishu) {
        this.shopqishu = shopqishu;
    }

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public String getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(String zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public String getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(String canyurenshu) {
        this.canyurenshu = canyurenshu;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public BeanAllLogUser getQ_user() {
        return q_user;
    }

    public void setQ_user(BeanAllLogUser q_user) {
        this.q_user = q_user;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getIs_ten() {
        return is_ten;
    }

    public void setIs_ten(int is_ten) {
        this.is_ten = is_ten;
    }
}
