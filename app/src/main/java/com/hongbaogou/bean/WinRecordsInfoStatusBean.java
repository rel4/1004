package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/6.
 */
public class WinRecordsInfoStatusBean   {
    //"status": 1,
      //      "info": "获得商品",
      //      "time": "2015-12-10 18:55"
    private String status;
    private String info;
    private String time;

    public WinRecordsInfoStatusBean() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
