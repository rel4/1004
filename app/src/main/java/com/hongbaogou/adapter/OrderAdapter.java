package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.PayOrderGoodsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class OrderAdapter extends BaseAdapter{

    private List<PayOrderGoodsBean> payOrderGoodsBeans = new ArrayList<PayOrderGoodsBean>();
    private LayoutInflater inflater;
    private Resources resource;

    public OrderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        resource = context.getResources();
    }

    public void addData(List<PayOrderGoodsBean> data){
        payOrderGoodsBeans.addAll(data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return payOrderGoodsBeans.size();
    }


    public Object getItem(int position) {
        return payOrderGoodsBeans.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHold orderViewHold = null;
        if (convertView == null) {
            orderViewHold = new OrderViewHold();
            convertView = inflater.inflate(R.layout.order_item, null);
            orderViewHold.title = (TextView) convertView.findViewById(R.id.title);
            orderViewHold.total = (TextView) convertView.findViewById(R.id.total);
            convertView.setTag(orderViewHold);
        } else {
            orderViewHold = (OrderViewHold) convertView.getTag();
        }
        PayOrderGoodsBean payOrderGoodsBean = payOrderGoodsBeans.get(position);
        orderViewHold.title.setText(payOrderGoodsBean.getTitle());

        String goNumber = String.valueOf(payOrderGoodsBean.getGonumber());
        String total = resource.getString(R.string.order_total, goNumber);
        SpannableString totalSpan = new SpannableString(total);
        totalSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#D94251")), 0, goNumber.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        orderViewHold.total.setText(totalSpan);
        return convertView;
    }

    public final class OrderViewHold{
        TextView title;
        TextView total;
    }
}
