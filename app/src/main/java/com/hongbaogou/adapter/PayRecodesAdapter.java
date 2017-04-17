package com.hongbaogou.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.PayRecodersBean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PayRecodesAdapter extends BaseAdapter {
    private Context context;
    private List<PayRecodersBean> data;
    private Resources resources;

    public void addData(List<PayRecodersBean> data1) {
        data.addAll(data1);
        notifyDataSetChanged();
    }

    public PayRecodesAdapter(Context context, List<PayRecodersBean> data) {
        this.context = context;
        this.data = data;
        resources = context.getResources();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_payrecodesadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
            viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.tv_result = (TextView) convertView.findViewById(R.id.tv_result);
            viewHolder.tv_money_rmb = (TextView) convertView.findViewById(R.id.tv_money_rmb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PayRecodersBean bean = data.get(position);
        viewHolder.tv_date.setText(bean.getTime());
        viewHolder.tv_pay.setText(bean.getPay_type());
        viewHolder.tv_money.setText(resources.getString(R.string.symbol));
        viewHolder.tv_money_rmb.setText(bean.getMoney().toString());
        viewHolder.tv_date.setText(bean.getTime());
        //// TODO: 2015/12/5  支付失败的时候调用，显示中划线 以及显示未支付
        // PayFailed(viewHolder.tv_money, viewHolder.tv_money_rmb, viewHolder.tv_result);
        if (bean.getStatus() == 0) {
            viewHolder.tv_result.setVisibility(View.VISIBLE);
            viewHolder.tv_result.setText("未支付");
            viewHolder.tv_money.getPaint().setFlags(0);  // 取消设置的的划线
            viewHolder.tv_money_rmb.getPaint().setFlags(0);  // 取消设置的的划线
        } else if (bean.getStatus() == 1) {
            viewHolder.tv_money.getPaint().setFlags(0);  // 取消设置的的划线
            viewHolder.tv_money_rmb.getPaint().setFlags(0);  // 取消设置的的划线
            viewHolder.tv_result.setVisibility(View.INVISIBLE);
        } else if (bean.getStatus() == -1) {
            viewHolder.tv_result.setText("已过期");
            viewHolder.tv_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tv_money_rmb.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
          //  PayFailed(viewHolder.tv_money, viewHolder.tv_money_rmb, viewHolder.tv_result);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView tv_date, tv_pay, tv_money, tv_result, tv_money_rmb;
    }

    private void DrawMiddleLine(TextView tview, TextView view) {
        if (view != null && tview != null) {
            view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            tview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void PayFailed(TextView tview, TextView view, TextView textview) {
        DrawMiddleLine(tview, view);
        textview.setVisibility(View.VISIBLE);
        textview.setText("已过期");
    }
}
