package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/11/27.
 */
public class BeanClassfy extends BaseListBean {
    /**
     * ID
     */
    private String cateid;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String thumb;

    public String getCateid() {
        return cateid;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
