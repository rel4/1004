package com.hongbaogou.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PersonAddressBean extends  BaseListBean implements Serializable{

   // "id": "32",
       //     "uid": "1",
      //      "sheng": "吉林",
      //      "shi": "长春",
       //     "xian": "南关区",
       //     "jiedao": "好",
       //     "shouhuoren": "哈哈",
       //     "is_default": "N",
        //    "mobile": "15625863698",
        //    "address": "吉林长春南关区好"

    private String id;
    private String uid;
    private String sheng;
    private String shi;
    private String xian;
    private String jiedao;
    private String shouhuoren;
    private String is_default;
    private String mobile;
    private String address;

    public PersonAddressBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        return xian;
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getJiedao() {
        return jiedao;
    }

    public void setJiedao(String jiedao) {
        this.jiedao = jiedao;
    }

    public String getShouhuoren() {
        return shouhuoren;
    }

    public void setShouhuoren(String shouhuoren) {
        this.shouhuoren = shouhuoren;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
