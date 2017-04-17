package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/3.
 */
public class BeanListSearch extends BeanObjentSearch {

    /**
     * id
     */
    private String id;


    /**
     * 图片地址
     */
    private String thumb;

    /**
     * 期数
     */
    private String qishu;

    /**
     * title
     */
    private String title;

    /**
     * 总人数
     */
    private String zongrenshu;

    /**
     * 剩余人数
     */
    private String shenyurenshu;

    /**
     * 单词购买价格
     */
    private int yunjiage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(String zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public String getShenyurenshu() {
        return shenyurenshu;
    }

    public void setShenyurenshu(String shenyurenshu) {
        this.shenyurenshu = shenyurenshu;
    }

    public int getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(int yunjiage) {
        this.yunjiage = yunjiage;
    }
}
