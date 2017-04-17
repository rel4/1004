package com.hongbaogou.bean;

/**
 * Created by admin on 2016/10/21.
 */
public class SanjiBean {


    /**
     * status : 1
     * data : {"yongjin":"120.00","count":"3","shareUrl":"www.baidu.com?shareUid=297bBQEHBgQDVAQDUwADBgcKCFAHBlRQAlgGVFU"}
     */

    private int status;
    /**
     * yongjin : 120.00
     * count : 3
     * shareUrl : www.baidu.com?shareUid=297bBQEHBgQDVAQDUwADBgcKCFAHBlRQAlgGVFU
     */

    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String yongjin;
        private String count;
        private String shareUrl;
        private String title;
        private String img;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getYongjin() {
            return yongjin;
        }

        public void setYongjin(String yongjin) {
            this.yongjin = yongjin;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }
}
