package com.hongbaogou.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hongbaogou.bean.SSO_UserBean;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by L.K.X on 2016/2/27.
 */
public class ShareMethodUtils {
    public static final int SSO_LOGIN = 1;
    public static final int ERROR = 2;

    //注意调用之前,一定要先初始化
    public static void start(Context context) {
        ShareSDK.initSDK(context);
    }
///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 分享网络图片
     *
     * @param imageUrl 网络图片url网址
     * @param title
     * @param content
     * @param shareUrl
     */
    public static void showQQ_url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImageUrl(imageUrl);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.share(shareParams);
    }

    /**
     * 分享本地图片
     *
     * @param imagePath 图片路径
     * @param title
     * @param content
     * @param shareUrl
     */
    public static void showQQ_path(String imagePath, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImagePath(imagePath);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.share(shareParams);
    }
///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 推荐使用
     * QQ空间本身不支持分享本地图片，因此如果想分享本地图片，图片会先上传到ShareSDK的文件服务器，
     * 得到连接以后才分享此链接。由于本地图片更耗流量，因此imageUrl优先级高于imagePath。
     *
     * @param imageUrl
     * @param title
     * @param content
     * @param shareUrl
     */
    public static void showQQZone_Url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImageUrl(imageUrl);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        platform.share(shareParams);
    }

    /**
     * 本地
     *
     * @param imagePath
     * @param title
     * @param content
     * @param shareUrl
     */
    public static void showQQZone_path(String imagePath, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImagePath(imagePath);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        platform.share(shareParams);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public static void showWeiXin_url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setUrl(shareUrl);
        shareParams.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.share(shareParams);
    }

    public static void showWeiXin_path(String imagePath, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setUrl(shareUrl);
        shareParams.setImagePath(imagePath);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.share(shareParams);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public static void showWeiXinZone_url(String imageUrl, String title, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setUrl(shareUrl);
        shareParams.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.share(shareParams);
    }

    public static void showWeiXinZone_path(String imagePath, String title, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setUrl(shareUrl);
        shareParams.setImagePath(imagePath);
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.share(shareParams);
    }

    /////////////////////////////////////
    public static void showSina_url(String imageUrl, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImageUrl(imageUrl);
        shareParams.setText(content);
        shareParams.setUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.share(shareParams);
    }

    public static void showSina_path(String imagePath, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImagePath(imagePath);
        shareParams.setText(content);
        shareParams.setUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.share(shareParams);
    }

    /////////////////////////////////////
    public static void showDuanxin_url(String address, String title, String content, String imageUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setAddress(address);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(ShortMessage.NAME);
        platform.share(shareParams);
    }

    public static void showDuanxin_path(String address, String title, String content, String imagePath) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setAddress(address);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setImagePath(imagePath);
        Platform platform = ShareSDK.getPlatform(ShortMessage.NAME);
        platform.share(shareParams);
    }

    ////////////////////////////////////////////////

    /**
     * 第三方登陆授权,SSO方式授权
     * <p>
     * 只要数据不要功能
     */
    public static void login_qq(final Handler handler, boolean isLogin) {
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);//这里如果想授权其他的第三方平台，只需要通过修改平台的名称。
        qq.SSOSetting(false);  //设置false表示使用SSO授权方式
        // 设置分享事件回调
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                //用户资源都保存到res
                //通过打印res数据看看有哪些数据是你想要的
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    SSO_UserBean sso_userBean = new SSO_UserBean();
                    sso_userBean.token = platDB.getToken();// 获取授权token,令牌
                    sso_userBean.gender = platDB.getUserGender();//性别
                    sso_userBean.userIcon = platDB.getUserIcon();//头像链接
                    sso_userBean.id = platDB.getUserId();// 获取用户在此平台的ID
                    sso_userBean.name = platDB.getUserName();// 获取用户昵称
                    sso_userBean.type = "QQ";

                    Message msg = Message.obtain();
                    msg.what = SSO_LOGIN;
                    msg.obj = sso_userBean;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Message msg = Message.obtain();
                msg.what = ERROR;
                handler.sendMessage(msg);
                qq.removeAccount();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        if (!isLogin) {
            if (qq.isValid()) {
                qq.removeAccount();
            }
        } else {
            qq.showUser(null);
        }
    }

    /**
     * 第三方登陆授权,SSO方式授权
     * <p>
     * * 只要数据不要功能
     *
     * @param handler
     */
    public static void login_weixin(final Handler handler, boolean isLogin) {

        final Platform weixin = ShareSDK.getPlatform(Wechat.NAME);//这里如果想授权其他的第三方平台，只需要通过修改平台的名称。
        if (weixin.isValid ()) {
            weixin.removeAccount();
        }
        weixin.SSOSetting(false);  //设置false表示使用SSO授权方式
        // 设置分享事件回调
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                //用户资源都保存到res
                //通过打印res数据看看有哪些数据是你想要的
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    SSO_UserBean sso_userBean = new SSO_UserBean();
                    sso_userBean.token = platDB.getToken();// 获取授权token,令牌
                    sso_userBean.gender = platDB.getUserGender();//性别
                    sso_userBean.userIcon = platDB.getUserIcon();//头像链接
                    sso_userBean.id = platDB.getUserId();// 获取用户在此平台的ID
                    sso_userBean.name = platDB.getUserName();// 获取用户昵称
                    sso_userBean.type = "WEIXIN";

                    Message msg = Message.obtain();
                    msg.what = SSO_LOGIN;
                    msg.obj = sso_userBean;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Message msg = Message.obtain();
                msg.what = ERROR;
                handler.sendMessage(msg);
                weixin.removeAccount();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        if (!isLogin) {
            if (weixin.isValid()) {
                weixin.removeAccount();
            }
        } else {
            weixin.showUser(null);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public static void stop(Context context) {
        ShareSDK.stopSDK(context);
    }
}
