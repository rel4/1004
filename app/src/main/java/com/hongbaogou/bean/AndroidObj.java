package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/28.
 */
public class AndroidObj {
    /**
     * 下载apk的链接
     */
    private String link;

    /**
     * 当前的版本名
     */
    private String name;

    /**
     * 获取当前版本号
     */
    private int code;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
