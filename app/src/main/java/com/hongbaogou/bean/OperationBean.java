package com.hongbaogou.bean;

/**
 * 主界面操作对象的栏目
 */
public class OperationBean extends BaseListBean{

    private String name;
    private String img;
    private String api;

    public OperationBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
