package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/6.
 */
public class WinRecordsInfoBean extends BaseObjectBean {

    private List<WinRecordsInfoStatusBean> jindu;
    private WinRecordsInfoCompanyBean company;
    private WinRecordsInfoAddressBean address;
    private WinRecordsInfoGoodsInfoBean goods_info;

    public WinRecordsInfoBean() {

    }

    public List<WinRecordsInfoStatusBean> getJindu() {
        return jindu;
    }

    public void setJindu(List<WinRecordsInfoStatusBean> jindu) {
        this.jindu = jindu;
    }

    public WinRecordsInfoCompanyBean getCompany() {
        return company;
    }

    public void setCompany(WinRecordsInfoCompanyBean company) {
        this.company = company;
    }

    public WinRecordsInfoAddressBean getAddress() {
        return address;
    }

    public void setAddress(WinRecordsInfoAddressBean address) {
        this.address = address;
    }

    public WinRecordsInfoGoodsInfoBean getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(WinRecordsInfoGoodsInfoBean goods_info) {
        this.goods_info = goods_info;
    }

}
