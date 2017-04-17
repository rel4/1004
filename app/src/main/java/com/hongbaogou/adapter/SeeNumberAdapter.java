package com.hongbaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.BeanSeeNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SeeNumberAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<String> list = new ArrayList<String>();

    public SeeNumberAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.see_number_details_item, parent, false);
            viewholder = new Viewholder();
            viewholder.textView = (TextView) convertView.findViewById(R.id.number_tv);
            convertView.setTag(viewholder);
        }
        setData2UI(convertView, list.get(position));
        return convertView;
    }

    private void setData2UI(View convertView, String str) {
        Viewholder viewholder = (Viewholder) convertView.getTag();
        viewholder.textView.setText(str);
    }

    /**
     * 向适配器中添加数据
     *
     * @param seeNumber
     */
    public void addData(BeanSeeNumber seeNumber) {
        String allNumber = seeNumber.getGoucode();
        //讲这些号码全部转化为字符串数组
        String[] numbers = allNumber.split(",");
        List<String> listNumbers = Arrays.asList(numbers);
        list.addAll(listNumbers);
        notifyDataSetChanged();
    }

    private class Viewholder {
        TextView textView;
    }
}
