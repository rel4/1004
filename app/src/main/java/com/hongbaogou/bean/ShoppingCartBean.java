package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ShoppingCartBean extends BaseListBean {

    private String id;
    private String uid;
    private String shopid;
    private String cache_id;
    private String qishu;
    private String title;
    private String thumb;
    private String zongrenshu;
    private String canyurenshu;
    private int yunjiage;
    private int gonumber;
    private boolean isSelected;
    private int is_ten;

    public ShoppingCartBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCache_id() {
        return cache_id;
    }

    public void setCache_id(String cache_id) {
        this.cache_id = cache_id;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public int getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(int yunjiage) {
        this.yunjiage = yunjiage;
    }

    public int getGonumber() {
        return gonumber;
    }

    public void setGonumber(int gonumber) {
        this.gonumber = gonumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getIs_ten() {
        return is_ten;
    }

    public void setIs_ten(int is_ten) {
        this.is_ten = is_ten;
    }
}
