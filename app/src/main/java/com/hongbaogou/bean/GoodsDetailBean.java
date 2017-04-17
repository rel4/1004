package com.hongbaogou.bean;

/**
 * 商品详情对象
 */
public class GoodsDetailBean extends BaseObjectBean {
    private String id;
    private String sid;
    private String title;

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    private String title2;
    private int yunjiage;
    private int tag;
    private String zongrenshu;
    private String canyurenshu;
    private String qishu;
    private String next_id;
    private String default_renci;
    private String start_time;
    private String wap_link;
    private String calc_link;


    //判断是否是10元专区 1表示是 0 否
    private int is_ten;

    //剩余时间
    private long surplus;
    //中奖号码
    private String q_user_code;
    private String q_end_time;
    private String[] picarr;
    //中奖对象
    private WinBean q_user;
    private BuyNumberBean curr_uinfo;

    public GoodsDetailBean() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(int yunjiage) {
        this.yunjiage = yunjiage;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(String zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public String getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(String canyurenshu) {
        this.canyurenshu = canyurenshu;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
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

    public String[] getPicarr() {
        return picarr;
    }

    public void setPicarr(String[] picarr) {
        this.picarr = picarr;
    }

    public WinBean getQ_user() {
        return q_user;
    }

    public void setQ_user(WinBean q_user) {
        this.q_user = q_user;
    }

    public BuyNumberBean getCurr_uinfo() {
        return curr_uinfo;
    }

    public void setCurr_uinfo(BuyNumberBean curr_uinfo) {
        this.curr_uinfo = curr_uinfo;
    }

    public long getSurplus() {
        return surplus;
    }

    public void setSurplus(long surplus) {
        this.surplus = surplus;
    }

    public String getNext_id() {
        return next_id;
    }

    public void setNext_id(String next_id) {
        this.next_id = next_id;
    }

    public String getDefault_renci() {
        return default_renci;
    }

    public void setDefault_renci(String default_renci) {
        this.default_renci = default_renci;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getIs_ten() {
        return is_ten;
    }

    public void setIs_ten(int is_ten) {
        this.is_ten = is_ten;
    }

    public String getWap_link() {
        return wap_link;
    }

    public void setWap_link(String wap_link) {
        this.wap_link = wap_link;
    }

    public String getCalc_link() {
        return calc_link;
    }

    public void setCalc_link(String calc_link) {
        this.calc_link = calc_link;
    }
}
