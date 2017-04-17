package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/6.
 */
public class WinRecordsInfoGoodsInfoBean {
  //  "id": "116",
   //         "title": "aaaaaaaaaaaaaaa",
   //         "qishu": "9",
   //         "thumb": "http://192.168.1.50/statics/uploads/shopimg/20151201/12948229969573.jpg",
    //        "zongrenshu": "2",
    //        "q_user_code": "10000001",
    //        "q_end_time": "2015-12-03 09:38:29",
    //        "gonumber": 2


    private String id;
    private String title;
    private String qishu;
    private String thumb;
    private String zongrenshu;
    private String q_user_code;
    private String q_end_time;
    private String gonumber;

    public WinRecordsInfoGoodsInfoBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public String getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(String zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
