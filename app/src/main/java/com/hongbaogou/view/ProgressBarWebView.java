package com.hongbaogou.view;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.hongbaogou.R;

/**
 * Created by Administrator on 2015/11/28.
 * <p/>
 * 带进度条的webview
 */
public class ProgressBarWebView extends WebView {
    private ProgressBar progressbar;

    public ProgressBarWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //创建以个横向的进度条
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);

        //设置进度条的大小
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));

        //动态给进度条添加背景
        ClipDrawable d = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.color_red)), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressbar.setProgressDrawable(d);

        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
