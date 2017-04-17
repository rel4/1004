package com.hongbaogou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.bean.BeanFindData;
import com.hongbaogou.utils.RequestManager;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class FindListAdapter extends BaseAdapter {

    private List<BeanFindData> list;
    private LayoutInflater mInflater;

    public FindListAdapter(List<BeanFindData> list, LayoutInflater mInflater) {
        this.list = list;
        this.mInflater = mInflater;
    }

    public BeanFindData getObject(int position) {
        return getItem(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BeanFindData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.find_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imgmap = (ImageView) convertView.findViewById(R.id.img_map);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.imgnew = (ImageView) convertView.findViewById(R.id.img_new);
            convertView.setTag(viewHolder);
        }
        //设置数据
        setData2UI(position, convertView, parent);

        return convertView;
    }

    private void setData2UI(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        BeanFindData findData = list.get(position);

        viewHolder.imgmap.setTag(findData.getImg_path());
        ImageLoader.ImageListener lis = ImageLoader.getImageListener(
                viewHolder.imgmap,
                R.mipmap.img_blank,
                R.mipmap.img_blank);

        RequestManager.getImageLoader().get(findData.getImg_path(), lis, 0, 0);
        //设置标题
        viewHolder.title.setText(findData.getTitle());
        //设置内容
        viewHolder.content.setText(findData.getDescription());

        String isnew = findData.getIs_new();
        if (isnew.equals("1")) {
            viewHolder.imgnew.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgnew.setVisibility(View.GONE);
        }
    }

    /**
     * 添加数据到适配器
     *
     * @param data
     */
    public void addData(List data) {
        list.addAll(data);
        notifyDataSetChanged();
    }


    private class ViewHolder {
        ImageView imgmap;
        TextView title;
        TextView content;
        ImageView imgnew;
    }
}
