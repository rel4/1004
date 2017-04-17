package com.hongbaogou.bean;

/**
 * Created by Administrator on 2015/11/27.
 * <p/>
 * 最新揭晓数组的bean
 */
public class BeanBestNewList {


    //1表示未揭晓    0表示已揭晓
    private int tag;
    /**
     * 商品的价格
     */
    private int yunjiage;
    /**
     * 商品id,商品的唯一标示
     */
    private String id;

    /**
     * 获奖者的ID
     */
    private String q_uid;

    /**
     * title
     */
    private String title;

    /**
     * 期号
     */
    private String qishu;

    /**
     * 中奖号码
     */
    private String q_user_code;

    /**
     * 揭晓时间
     */
    private String q_end_time;

    /**
     * 商品图片地址
     */
    private String thumb;

    /**
     * 获奖者名称
     */
    private String username;

    /**
     * 购买人次
     */
    private String gonumber;
    //倒计时时间
    private long jiexiao_time;

    private int is_ten;

    public int getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(int yunjiage) {
        this.yunjiage = yunjiage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQ_uid() {
        return q_uid;
    }

    public void setQ_uid(String q_uid) {
        this.q_uid = q_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGonumber() {
        return gonumber;
    }

    public void setGonumber(String gonumber) {
        this.gonumber = gonumber;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public long getJiexiao_time() {
        return jiexiao_time;
    }

    public void setJiexiao_time(long jiexiao_time) {
        this.jiexiao_time = jiexiao_time;
    }

    public int getIs_ten() {
        return is_ten;
    }

    public void setIs_ten(int is_ten) {
        this.is_ten = is_ten;
    }
}
