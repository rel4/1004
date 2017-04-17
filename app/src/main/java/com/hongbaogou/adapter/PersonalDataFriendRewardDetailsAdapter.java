package com.hongbaogou.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.PersonalDataFriendRewardBean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PersonalDataFriendRewardDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<PersonalDataFriendRewardBean> data;

    public void addData(List<PersonalDataFriendRewardBean> data1) {
        data.addAll(data1);
        notifyDataSetChanged();
    }
    public void deleteData(List<PersonalDataFriendRewardBean> data1) {
        data.clear();
    }
    public PersonalDataFriendRewardDetailsAdapter(Context context, List<PersonalDataFriendRewardBean> data) {
        this.context = context;
        this.data = data;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_rewarddetailadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            viewHolder.tv_jifen = (TextView) convertView.findViewById(R.id.tv_jifen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PersonalDataFriendRewardBean bean = data.get(position);
        viewHolder.tv_time.setText(bean.getUid());
        viewHolder.tv_mobile.setText(bean.getMobile());
        viewHolder.tv_jifen.setText(bean.getJifen());
        return convertView;
    }
    static class ViewHolder {
        public TextView tv_time, tv_mobile, tv_jifen;
    }

}
