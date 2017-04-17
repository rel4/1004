package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/18.
 */
public class PersonalDataScaleOneBean extends BaseObjectBean {
   // "num":"1","jifen":8
    private int num;
    private int jifen;

    public PersonalDataScaleOneBean() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }
}
