package com.hongbaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.SeeNumberActivity;
import com.hongbaogou.bean.WebViewGoolsListBean;
import com.hongbaogou.utils.Pref_Utils;

import java.util.List;

/**
 * Created by lenovo on 2016/1/3.
 */
public class WebViewCallBackAdapter extends BaseAdapter {
    private Context context;
    private List<WebViewGoolsListBean> list;
    private WebViewGoolsListBean bean;

    public WebViewCallBackAdapter(List<WebViewGoolsListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void addData(List<WebViewGoolsListBean> data) {
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_webiewvallackdapter, null);
            viewHolder = new ViewHolder();
            viewHolder.number1 = (TextView) convertView.findViewById(R.id.number1);
            viewHolder.number2 = (TextView) convertView.findViewById(R.id.number2);
            viewHolder.number3 = (TextView) convertView.findViewById(R.id.number3);
            viewHolder.number4 = (TextView) convertView.findViewById(R.id.number4);
            viewHolder.number5 = (TextView) convertView.findViewById(R.id.number5);

            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
            viewHolder.tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeeNumberActivity.class);
                    intent.putExtra("uid", Pref_Utils.getString(parent.getContext(), "uid"));
                    intent.putExtra("id", bean.getShopid());
                    intent.putExtra("qishu", bean.getShopqishu());
                    context.startActivity(intent);
                }
            });
            viewHolder.tv_gonumber = (TextView) convertView.findViewById(R.id.tv_gonumber);
            viewHolder.tv_shopqishu = (TextView) convertView.findViewById(R.id.tv_shopqishu);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        bean = list.get(position);


        String[] arr = bean.getGoucode().split(",");

        if (arr.length > 5) {
            viewHolder.tv_more.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_more.setVisibility(View.GONE);
        }

        Log.e("TAG", " position = changdu = ------>  " + position + "---->" + arr.length);
        if (arr.length >= 1) {
            viewHolder.number1.setText(arr[0]);
            viewHolder.number1.setVisibility(View.VISIBLE);
            viewHolder.number2.setVisibility(View.INVISIBLE);
            viewHolder.number3.setVisibility(View.INVISIBLE);
            viewHolder.number4.setVisibility(View.INVISIBLE);
            viewHolder.number5.setVisibility(View.INVISIBLE);
            if (arr.length >= 2) {
                viewHolder.number2.setText(arr[1]);
                viewHolder.number1.setVisibility(View.VISIBLE);
                viewHolder.number2.setVisibility(View.VISIBLE);
                viewHolder.number3.setVisibility(View.INVISIBLE);
                viewHolder.number4.setVisibility(View.INVISIBLE);
                viewHolder.number5.setVisibility(View.INVISIBLE);
                if (arr.length >= 3) {
                    viewHolder.number3.setText(arr[2]);
                    viewHolder.number1.setVisibility(View.VISIBLE);
                    viewHolder.number2.setVisibility(View.VISIBLE);
                    viewHolder.number3.setVisibility(View.VISIBLE);
                    viewHolder.number4.setVisibility(View.INVISIBLE);
                    viewHolder.number5.setVisibility(View.INVISIBLE);
                    if (arr.length >= 4) {
                        viewHolder.number4.setText(arr[3]);
                        viewHolder.number1.setVisibility(View.VISIBLE);
                        viewHolder.number2.setVisibility(View.VISIBLE);
                        viewHolder.number3.setVisibility(View.VISIBLE);
                        viewHolder.number4.setVisibility(View.VISIBLE);
                        viewHolder.number5.setVisibility(View.INVISIBLE);
                        if (arr.length >= 5) {
                            viewHolder.number1.setVisibility(View.VISIBLE);
                            viewHolder.number2.setVisibility(View.VISIBLE);
                            viewHolder.number3.setVisibility(View.VISIBLE);
                            viewHolder.number4.setVisibility(View.VISIBLE);
                            viewHolder.number5.setVisibility(View.VISIBLE);
                            viewHolder.number5.setText(arr[4]);
                        }
                    }
                }
            }
        }

        SpannableStringBuilder builder = new SpannableStringBuilder((bean.getGonumber()));
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        viewHolder.tv_title.setText(bean.getShopname());

        builder.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        String str = bean.getGonumber() + "人次";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(parent.getResources().getColor(R.color.color_gray)), str.length() - 2, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        viewHolder.tv_gonumber.setText(ssb);

        viewHolder.tv_shopqishu.setText("商品期号：" + bean.getShopqishu());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title, tv_more, tv_gonumber, tv_shopqishu, number1, number2, number3, number4, number5;
    }
}
