package com.hongbaogou.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.PersonAddressBean;

import java.util.List;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PersonaldataAddAddressAdapter extends BaseAdapter{

    private Context context;
    private List<PersonAddressBean> data;

    public  List<PersonAddressBean> getData(){
        return data;
    }

    public void addData(List<PersonAddressBean> data1) {
        data.clear();
        data.addAll(data1);
        notifyDataSetChanged();
    }
    public PersonaldataAddAddressAdapter(Context context, List<PersonAddressBean> data) {
        this.context = context;
        this.data = data;
    }

    public void cleanPosition(int position) {
        this.data.remove(position);
        notifyDataSetChanged();
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
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_persondataadressadapter,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_personaldata_username = (TextView) convertView.findViewById(R.id.tv_personaldata_username);
            viewHolder.tv_personaldata_usertel = (TextView) convertView.findViewById(R.id.tv_personaldata_usertel);
            viewHolder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            viewHolder.tv_showAddress = (TextView) convertView.findViewById(R.id.tv_showAddress);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        PersonAddressBean bean = data.get(position);
        viewHolder.tv_personaldata_username.setText(bean.getShouhuoren());
        viewHolder.tv_personaldata_usertel.setText(bean.getMobile());
       if ("Y".equals(bean.getIs_default())){
           viewHolder.tv_default.setText("[默认]");
           viewHolder.tv_default.setVisibility(View.VISIBLE);

       }else{
           viewHolder.tv_default.setVisibility(View.INVISIBLE);
       }
        viewHolder.tv_showAddress.setText(bean.getAddress());

        return convertView;
    }

    public void cleanData() {
        data.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tv_personaldata_username,tv_personaldata_usertel,tv_default,tv_showAddress;
    }
}
