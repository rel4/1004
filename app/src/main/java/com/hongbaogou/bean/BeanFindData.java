package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/1.
 */
public class BeanFindData extends BaseListBean {
    /**
     * ID
     */
    private String id;
    /**
     * 標題
     */
    private String title;
    /**
     * 圖片地址
     */
    private String img_path;
    /**
     * 正文
     */
    private String content;
    /**
     * 詳情
     */
    private String description;
    /**
     * 事件
     */
    private String time;
    /**
     * 是否是新的
     */
    private String is_new;
    /**
     * 种类
     */
    private int sort;

    /**
     * 链接地址
     */
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
