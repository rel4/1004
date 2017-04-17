package com.hongbaogou.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongbaogou.MainActivity;
import com.hongbaogou.R;

/**
 * 数据加载错误布局
 */
public class NetErrorView extends LinearLayout implements View.OnClickListener {

    private LinearLayout netErrorView;
    private ImageView loading;
    private TextView loadingTextView;
    private ImageView errorView;
    private Button reflesh;

    private TextView nodataTX;
    private ImageView nodata;
    private Button gohome;
    private OnReloadListener onReloadListener;

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    public NetErrorView(Context context) {
        super(context);
        initView(context);
    }

    public NetErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化LinearLayout视图
     */
    private void initView(final Context context) {
        netErrorView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.neterrorview, this);
        loading = (ImageView) netErrorView.findViewById(R.id.loading);
        Animation loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_loading);
        loading.startAnimation(loadingAnimation);
        loadingTextView = (TextView) netErrorView.findViewById(R.id.loadingTextView);
        errorView = (ImageView) netErrorView.findViewById(R.id.errorView);
        reflesh = (Button) netErrorView.findViewById(R.id.reflesh);

        nodataTX = (TextView) netErrorView.findViewById(R.id.nodataTX);
        nodata = (ImageView) netErrorView.findViewById(R.id.nodata);
        gohome = (Button) netErrorView.findViewById(R.id.gohome);
        gohome.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        reflesh.setOnClickListener(this);
    }

    public void loadSuccess() {
        setVisibility(GONE);
    }

    public void loadFail() {
        loading.setVisibility(GONE);
        loadingTextView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
        reflesh.setVisibility(VISIBLE);
        nodataTX.setVisibility(GONE);
        nodata.setVisibility(GONE);
        gohome.setVisibility(GONE);
    }

    public void reLoad() {
        loading.setVisibility(VISIBLE);
        loadingTextView.setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
        reflesh.setVisibility(GONE);
        nodataTX.setVisibility(GONE);
        nodata.setVisibility(GONE);
        gohome.setVisibility(GONE);
    }

    public void emptyView() {
        loading.setVisibility(GONE);
        loadingTextView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        reflesh.setVisibility(GONE);
        nodataTX.setVisibility(VISIBLE);
        nodata.setVisibility(VISIBLE);
        gohome.setVisibility(VISIBLE);
    }

    public void onClick(View v) {
        reLoad();
        if (onReloadListener != null) {
            onReloadListener.onReload();
        }

    }

    public interface OnReloadListener {
        public void onReload();
    }

    public void destory() {
        netErrorView = null;
        loading = null;
        loadingTextView = null;
        errorView = null;
        reflesh = null;
        nodataTX = null;
        nodata = null;
        gohome = null;
        onReloadListener = null;
    }

    public void setNoDataText(String str) {
        nodataTX.setText(str);
    }
}
