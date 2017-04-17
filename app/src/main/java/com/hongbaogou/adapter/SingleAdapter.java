package com.hongbaogou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.PersonAddressBean;

import java.util.List;

/**
 * Created by lenovo on 2016/1/4.
 */
public class SingleAdapter extends BaseAdapter {
    private Context context;
    public List<PersonAddressBean> data;
    private PersonAddressBean bean;

    public SingleAdapter(Context context, List<PersonAddressBean> data) {
        this.context = context;
        this.data = data;
    }

    public PersonAddressBean getObject(int position) {
        return data.get(position);
    }

    public void addData(List<PersonAddressBean> data1) {
        data.clear();
        data.addAll(data1);
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
//        SingleView singleView = new SingleView(context);
//        bean = data.get(position);
//        singleView.setUserName(bean.getShouhuoren());
//        singleView.setUserTel(bean.getMobile());
//        singleView.setDefault(bean.getIs_default());
//        System.out.print("--------------" + bean.getIs_default());
//        singleView.setAddress(bean.getAddress());
//        if(bean.getIs_default().equals("Y")){
//            singleView.setChecked(true);
//        }else {
//            singleView.setChecked(false);
//        }
//
//        return singleView;

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_single_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_personaldata_username = (TextView) convertView.findViewById(R.id.tv_personaldata_username);
            viewHolder.tv_personaldata_usertel = (TextView) convertView.findViewById(R.id.tv_personaldata_usertel);
            viewHolder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            viewHolder.tv_showAddress = (TextView) convertView.findViewById(R.id.tv_showAddress);
            viewHolder.iv_address_check = (CheckBox) convertView.findViewById(R.id.iv_address_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bean = data.get(position);
        viewHolder.tv_personaldata_username.setText(bean.getShouhuoren());
        viewHolder.tv_personaldata_usertel.setText(bean.getMobile());
        viewHolder.tv_showAddress.setText(bean.getAddress());

        String is_default = bean.getIs_default();
        if ("Y".equals(is_default)) {
//            viewHolder.tv_default.setText("[默认]");
            viewHolder.iv_address_check.setChecked(true);
        } else {
//            viewHolder.tv_default.setVisibility(View.GONE);
            viewHolder.iv_address_check.setChecked(false);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_personaldata_username;
        TextView tv_personaldata_usertel;
        TextView tv_default;
        TextView tv_showAddress;
        CheckBox iv_address_check;
    }
}
