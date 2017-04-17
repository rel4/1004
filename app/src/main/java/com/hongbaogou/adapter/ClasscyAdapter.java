package com.hongbaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.BeanClassfy;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class ClasscyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<BeanClassfy> list = new ArrayList<BeanClassfy>();

    private ImageLoader mImageLoader;

    public void addData(List<BeanClassfy> listData) {
        list.addAll(listData);
        notifyDataSetChanged();
    }


    public ClasscyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mImageLoader = RequestManager.getImageLoader();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BeanClassfy getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.classcy_list_item, null);
            ViewHolder holder = new ViewHolder();
            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.img_shangpin);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_biaoti);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        BeanClassfy beanClassfy = list.get(position);

//        holder.imageView.setTag(beanClassfy.getThumb());
//        ImageLoader.ImageListener lis = ImageLoader.getImageListener(holder.imageView, R.mipmap.img_blank, R.mipmap.img_blank);
        holder.imageView.setImageUrl(beanClassfy.getThumb(), mImageLoader);
        holder.textView.setText(beanClassfy.getName());

//        RequestManager.getImageLoader().get(beanClassfy.getThumb(), lis, 0, 0);
        return convertView;
    }

    private class ViewHolder {
        NetworkImageView imageView;
        TextView textView;
    }
}
