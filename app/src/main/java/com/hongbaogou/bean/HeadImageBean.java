package com.hongbaogou.bean;

/**
 * Created by lenovo on 2016/1/4.
 */
public class HeadImageBean  {
//    "status": 1,
//            "message": "上传成功",
//            "data": "http://192.168.1.50/statics/uploads/touimg/20151207/144948987711.jpg"

private String status;
private String message;
private String data;

    public HeadImageBean() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
