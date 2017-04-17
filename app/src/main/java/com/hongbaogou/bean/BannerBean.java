package com.hongbaogou.bean;

/**
 * 主页导航栏播放广告
 */
public class BannerBean extends BaseListBean {

    private String img;
    private String title;
    private String link;
    private String slide_type;
    private String shopid;
    private String qishu;
    private String keywords;
    private String cateid;

    public BannerBean() {
    }


    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }


    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSlide_type() {
        return slide_type;
    }

    public void setSlide_type(String slide_type) {
        this.slide_type = slide_type;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }
}
