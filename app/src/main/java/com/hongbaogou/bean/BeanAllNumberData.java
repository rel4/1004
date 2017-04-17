package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/1.
 */
public class BeanAllNumberData extends BaseObjectBean {

    /**
     * 商品名称
     */
    private String shopname;
    /**
     * 期数
     */
    private String qishu;
    /**
     * 参与人数
     */
    private String gonumber;
    /**
     * 中奖的号码
     */
    private String goucode;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public String getGoucode() {
        return goucode;
    }

    public void setGoucode(String goucode) {
        this.goucode = goucode;
    }
}
