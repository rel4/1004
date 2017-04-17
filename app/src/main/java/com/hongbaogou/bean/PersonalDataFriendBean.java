package com.hongbaogou.bean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/18.
 */
public class PersonalDataFriendBean extends  BaseObjectBean {
   /* "friends": {one":{"num":"1","jifen":8},"two":{"num":0,"jifen":0},"three":{"num":0,"jifen":0 },
       "reward": [{"uid": "65","jifen": 4,"mobile": "2532108285@qq.com"},{"uid": "65","jifen": 4,"mobile": "2532108285@qq.com" }*/
   private  PersonalDataFriendsBean friends;
    private List<PersonalDataFriendRewardBean> reward;

    public PersonalDataFriendBean() {
    }

    public PersonalDataFriendsBean getFriends() {
        return friends;
    }

    public void setFriends(PersonalDataFriendsBean friends) {
        this.friends = friends;
    }

    public List<PersonalDataFriendRewardBean> getReward() {
        return reward;
    }

    public void setReward(List<PersonalDataFriendRewardBean> reward) {
        this.reward = reward;
    }
}
