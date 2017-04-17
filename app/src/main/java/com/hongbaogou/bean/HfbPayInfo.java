package com.hongbaogou.bean;

/**
 * Created by L.K.X on 2016/5/26.
 */
public class HfbPayInfo {
    public int status;
    public DataEntity data;

    public class DataEntity{
        public String tokenID;
        public String agentId;
        public String billNo;
    }
}
