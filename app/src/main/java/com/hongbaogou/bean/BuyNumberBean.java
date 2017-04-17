package com.hongbaogou.bean;

/**
 * 购买的号码
 */
public class BuyNumberBean {

    //1参加，0未参加
    private int is_join;
    //购买人次
    private int gonumber;
    //购买号码
    private String goucode;
    private String content;

    public BuyNumberBean() {
    }

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
    }

    public int getGonumber() {
        return gonumber;
    }

    public void setGonumber(int gonumber) {
        this.gonumber = gonumber;
    }

    public String getGoucode() {
        return goucode;
    }

    public void setGoucode(String goucode) {
        this.goucode = goucode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
