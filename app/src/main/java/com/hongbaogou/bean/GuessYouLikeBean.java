package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/3.
 */
public class GuessYouLikeBean extends BaseListBean{
    //"title": "Apple&nbsp;iPad&nbsp;Air&nbsp;2&nbsp;16G&nbsp;WIFI版&nbsp;轻轻地，改变一切(颜色随机)",
     //       "thumb": "http://192.168.1.50/statics/uploads/shopimg/20151210/24124008710231.jpg",
     //       "is_ten": 1,
      //      "shopid": "686",
       //     "bilv": 0
    private String title;
    private String thumb;
    private String is_ten;
    private String shopid;
    private Double bilv;

    public GuessYouLikeBean() {
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getIs_ten() {
        return is_ten;
    }

    public void setIs_ten(String is_ten) {
        this.is_ten = is_ten;
    }

    public Double getBilv() {
        return bilv;
    }

    public void setBilv(Double bilv) {
        this.bilv = bilv;
    }
}
