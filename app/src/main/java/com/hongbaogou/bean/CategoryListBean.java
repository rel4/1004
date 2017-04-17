package com.hongbaogou.bean;

/**
 * 商品分类对象
 */
public class CategoryListBean extends BaseListBean {


    private String name;
    private String type;
    /*
     * 分类图片
     */
    private String img;

    public CategoryListBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
