package com.hongbaogou.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/12/12.
 */
public class ImageAnimation {


    private Context context;
    //动画时间
    private int AnimationDuration = 800;
    //正在执行的动画数量
    private int number = 0;
    //是否完成清理
    private boolean isClean = false;
    private FrameLayout animationViewGroup;
    private View endView;
    private Window window;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //用来清除动画后留下的垃圾
                    try {
                        animationViewGroup.removeAllViews();
                    } catch (Exception e) {
                    }
                    isClean = false;
                    break;
                default:
                    break;
            }
        }
    };


    public ImageAnimation(Context context,View endView,Window window){
        this.window = window;
        this.context = context;
        this.endView = endView;
    }

    public void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animationViewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }


    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    public void createAnimLayout() {
        ViewGroup rootView = (ViewGroup) window.getDecorView();
        FrameLayout animLayout = new FrameLayout(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        animationViewGroup = animLayout;
    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dip2px(context, 140), dip2px(context, 140));
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 动画效果设置
     *
     * @param drawable       将要加入购物车的商品
     * @param startLocation 起始位置
     */
    private void setAnim(Drawable drawable, int[] startLocation) {

        final ImageView img = new ImageView(context);
        img.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animationViewGroup, img, startLocation);
        view.setAlpha(0.8f);

        int[] end_location = new int[2];
        endView.getLocationInWindow(end_location);
        int endX = end_location[0] - startLocation[0];
        int endY = end_location[1] - startLocation[1];

        Animation mTranslateAnimation = new TranslateAnimation(0, endX, 0, endY);
        mTranslateAnimation.setDuration(AnimationDuration);

//        Animation mRotateAnimation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.1f);
//        mRotateAnimation.setDuration(AnimationDuration);

        Animation mScaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f);
        mScaleAnimation.setDuration(AnimationDuration);
        mScaleAnimation.setFillAfter(true);

        AnimationSet mAnimationSet = new AnimationSet(true);
        mAnimationSet.setFillAfter(true);
//        mAnimationSet.addAnimation(mRotateAnimation);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mTranslateAnimation);

        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                number++;
            }

            public void onAnimationEnd(Animation animation) {
                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }

            }

            public void onAnimationRepeat(Animation animation) {

            }

        });
        view.startAnimation(mAnimationSet);

    }

    public void clearAnimation(){
        isClean = true;
        try {
            animationViewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
    }
}
