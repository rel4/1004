package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/18.
 */
//奖励详情的list
public class RewardDetailBeans extends BaseObjectBean {
  //  "jifen": 4,
    //        "addtime": "2015-12-17 20:15",
    //          "mobile": "2532108285@qq.com"

    private String jifen;
    private String addtime;
    private String mobile;

    public RewardDetailBeans() {
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
