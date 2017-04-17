package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/12/8.
 */
public class WinAgoBean {
    private String id;
    private String sid;
    private int tag;
    private String q_end_time;
    private String qishu;
    private String q_user_code;
    private String q_uid;
    private UserBean q_user;

    public WinAgoBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public String getQ_uid() {
        return q_uid;
    }

    public void setQ_uid(String q_uid) {
        this.q_uid = q_uid;
    }

    public UserBean getQ_user() {
        return q_user;
    }

    public void setQ_user(UserBean q_user) {
        this.q_user = q_user;
    }
}
