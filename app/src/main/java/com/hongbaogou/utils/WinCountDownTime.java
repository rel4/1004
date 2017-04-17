package com.hongbaogou.utils;

import android.os.CountDownTimer;

/**
 * 商品详情倒计时
 */
public class WinCountDownTime extends CountDownTimer {

    private OnWinCountDownListener onWinCountDownListener;

    public void setOnWinCountDownListener(OnWinCountDownListener onWinCountDownListener) {
        this.onWinCountDownListener = onWinCountDownListener;
    }

    public WinCountDownTime(long millisInFuture, long countDownInterval){
        super(millisInFuture,countDownInterval);
    }

    public  void onTick(long millisUntilFinished){
        onWinCountDownListener.onTimeChange(millisUntilFinished);
    }

    public  void onFinish(){
        onWinCountDownListener.onFinish();
    }

    public interface OnWinCountDownListener{
        public void onFinish();
        public void onTimeChange(long time);
    }
}
