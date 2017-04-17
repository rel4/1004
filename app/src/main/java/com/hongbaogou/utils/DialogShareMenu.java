package com.hongbaogou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hongbaogou.R;


/**
 * 分享
 * Created by ch on 2015/10/15.
 */
public class DialogShareMenu implements View.OnClickListener {

    private Dialog dialog;//分享框

    private View shareCancle;//取消分享

    private ImageView shareWX, shareQQ, shareWebSite, shareWXFriends;//微博，微信，QQ
    private String imageUrl;
    private String title;
    private String content;
    private String shareUrl;
    private int flag;

    //private UMSocialService mController = UMServiceFactory.getUMSocialService(ConstantValues.DESCRIPTOR);

    private Activity mActivity;

    private String textContent;//需要复制的内容
    private ImageView qq_website;

    public DialogShareMenu(Activity activity, String textContent, String imageUrl, String title, String content, String shareUrl,int flag) {
        this.mActivity = activity;
        this.textContent = textContent;
        this.imageUrl=imageUrl;
        this.title=title;
        this.content=content;
        this.shareUrl=shareUrl;
        this.flag=flag;
        share();
    }




    public void share() {
        //分享框弹出
        View diaView = View.inflate(mActivity, R.layout.dialog_share_menu, null);

        shareWX = (ImageView) diaView.findViewById(R.id.share_wx);
        shareQQ = (ImageView) diaView.findViewById(R.id.share_qqzone);
        shareWebSite = (ImageView) diaView.findViewById(R.id.share_website);
        shareWXFriends = (ImageView) diaView.findViewById(R.id.share_wx_friends);
        qq_website = (ImageView) diaView.findViewById(R.id.qq_website);
        shareWX.setOnClickListener(this);
        shareQQ.setOnClickListener(this);
        shareWebSite.setOnClickListener(this);
        shareWXFriends.setOnClickListener(this);
        qq_website.setOnClickListener(this);
        dialog = new Dialog(mActivity, R.style.product_list_share_dialog);
        dialog.setContentView(diaView);
        dialog.setCanceledOnTouchOutside(true);
        shareCancle =  diaView.findViewById(R.id.ll_share_cancle);
        shareCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = Dip2PxUtils.dip2px(mActivity, 175); // 高度
//        lp.alpha = 0.9f; // 透明度   去掉透明显示
        window.setWindowAnimations(R.style.dialog_anim);  //添加动画
        dialog.show();

        System.out.println("imgurl---------" + imageUrl + "title----" + title + "content------" + content + "sharUrl-----" + shareUrl);

    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
        switch (v.getId()) {
            case R.id.share_wx:
                ShareMethodUtils.showWeiXinZone_url(imageUrl, title, shareUrl);
                break;
            case R.id.share_qqzone:
                ShareMethodUtils.showQQZone_path(imageUrl,title,content,shareUrl);
                break;
            case R.id.share_wx_friends://分享到微信好友
                ShareMethodUtils.showWeiXin_url(imageUrl,title,content,shareUrl);
                break;
            case R.id.share_website:
                if(flag==1){
                copy("复制链接邀请好友拿取大奖，快去看看吧！"+shareUrl, mActivity);
                Toast.makeText(mActivity, "已复制", Toast.LENGTH_SHORT).show();}

                else {
                    copy("复制链接，"+title+"快去看看吧！"+shareUrl, mActivity);
                    Toast.makeText(mActivity, "已复制", Toast.LENGTH_SHORT).show();
                }

            case R.id.qq_website:
                ShareMethodUtils.showQQ_url(imageUrl,title,content,shareUrl);
                break;
        }
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }



}

