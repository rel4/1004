package com.hongbaogou.bean;

/**
 * Created by lenovo on 2015/12/18.
 */
public class PersonalDataFriendsBean extends BaseObjectBean {
    //one":{"num":"1","jifen":8},"two":{"num":0,"jifen":0},"three":{"num":0,"jifen":0


    private PersonalDataScaleOneBean one;
    private PersonalDataScaleTwoBean two;
    private PersonalDataScaleThreeBean three;


    public PersonalDataFriendsBean() {
    }

    public PersonalDataScaleTwoBean getTwo() {
        return two;
    }

    public void setTwo(PersonalDataScaleTwoBean two) {
        this.two = two;
    }

    public PersonalDataScaleThreeBean getThree() {
        return three;
    }

    public void setThree(PersonalDataScaleThreeBean three) {
        this.three = three;
    }

    public PersonalDataScaleOneBean getOne() {
        return one;
    }

    public void setOne(PersonalDataScaleOneBean one) {
        this.one = one;
    }
}
