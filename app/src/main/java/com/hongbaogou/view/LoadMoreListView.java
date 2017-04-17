package com.hongbaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hongbaogou.R;
import com.hongbaogou.utils.ScreenUtils;


/**
 * Created by admin on 2016/3/28.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private final View mFooter;
    private final int mFooterHeight;
    private float downY;
    private float upY;
    private float minLen = -20;
    private boolean isLoading = true;
    private LoadMoreListener listener;
    private MyScrollChangeListener myListener;
    private boolean intercept = true;

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }


    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooter = View.inflate(context, R.layout.footview, null);
        mFooter.measure(0, 0);
        mFooterHeight = mFooter.getMeasuredHeight();
        System.out.println("mFooterHeight--------"+mFooterHeight);
//        mFooter.setPadding(0, mFooterHeight, 0, 0);
        this.addFooterView(mFooter);
        this.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {


        if (scrollState == SCROLL_STATE_IDLE) {
            if (getAdapter() != null && (getLastVisiblePosition() >= getAdapter().getCount() - 1)) {
                if (listener != null && isLoading)
                    listener.onLoadMore();
            }
        }
        if (myListener != null)
            myListener.onMyScrollStateChanged(scrollState);
    }


    public boolean checkLoadMore() {

        System.out.println("getListViewHeght(this)     " + getListViewHeght(this));
        if (getListViewHeght(this) > ScreenUtils.getScreenHeight(getContext()))
            return true;
        return false;

    }

    public int getListViewHeght(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public void setLoading(boolean b) {
        isLoading = b;
        if (isLoading) {
            mFooter.setPadding(0, 0, 0, 0);
        } else {
            mFooter.setPadding(0, -mFooterHeight, 0, 0);
        }
    }


    private boolean isUp() {
        if (upY - downY < 0) {
            upY = 0;
            downY = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (intercept) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                upY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    public void setMyScrollChangeListener(MyScrollChangeListener listener) {
        myListener = listener;
    }

    public interface LoadMoreListener {
        public void onLoadMore();
    }

    public interface MyScrollChangeListener {
        public void onMyScrollStateChanged(int scrollState);
    }


    private MyOnScrollListener onScrollListener;

    public void setMyOnScrollListener(MyOnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface MyOnScrollListener {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}
