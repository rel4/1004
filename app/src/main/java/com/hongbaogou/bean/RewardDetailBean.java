package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/18.
 */
public class RewardDetailBean extends BaseObjectBean {
    //"total": "1",
    //        "max_page": 1,
    //         "list": [
    //   {
    //     "jifen": 4,
    //        "addtime": "2015-12-17 20:15",
    //          "mobile": "2532108285@qq.com"
    // }
    //  ]
    private String total;
    private String max_page;
    private List<RewardDetailBeans> list;

    public RewardDetailBean() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMax_page() {
        return max_page;
    }

    public void setMax_page(String max_page) {
        this.max_page = max_page;
    }

    public List<RewardDetailBeans> getList() {
        return list;
    }

    public void setList(List<RewardDetailBeans> list) {
        this.list = list;
    }
}
