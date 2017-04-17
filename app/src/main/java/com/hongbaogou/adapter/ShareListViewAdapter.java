package com.hongbaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.activity.CenterActivity;
import com.hongbaogou.bean.ShareListBean;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.view.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ShareListViewAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ShareListBean> list = new ArrayList<ShareListBean>();
    private LayoutInflater mInflater;
    private String headImg;
    private String uid;
    private String username;
    private ImageLoader mImageLoader;

    public ShareListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mImageLoader = RequestManager.getImageLoader();
    }

    public ShareListBean getObjectByPosttion(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShareListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.share_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.img_icon = (CircleImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.username);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tv_goods = (TextView) convertView.findViewById(R.id.goods);
            viewHolder.tv_qihao = (TextView) convertView.findViewById(R.id.qihao);
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.img_1 = (NetworkImageView) convertView.findViewById(R.id.img_1);
            viewHolder.img_2 = (NetworkImageView) convertView.findViewById(R.id.img_2);
            viewHolder.img_3 = (NetworkImageView) convertView.findViewById(R.id.img_3);

            viewHolder.tv_name.setOnClickListener(this);

            convertView.setTag(viewHolder);
        }
        //绘制数据到ui
        setData2UI(convertView, position);

        notifyDataSetChanged();
        return convertView;
    }

    /**
     * 设置ui数据
     *
     * @param convertView
     * @param position
     */
    private void setData2UI(View convertView, int position) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        ShareListBean listBean = list.get(position);

        headImg = listBean.getQ_user().getImg();
        username = listBean.getQ_user().getUsername();
        uid = listBean.getQ_user().getUid();

        //设置名字
        viewHolder.tv_name.setText(listBean.getQ_user().getUsername());
        //设置用户头像
        ImageLoader.ImageListener lis = ImageLoader.getImageListener(
                viewHolder.img_icon,
                R.mipmap.user_icon_default,
                R.mipmap.user_icon_default);

        if(headImg!=null){
            ImageLoader.ImageContainer imageContainer = RequestManager.getImageLoader().get(headImg, lis);
        }

        //设置时间
        viewHolder.tv_time.setText(listBean.getSd_time());
        //设置title
        viewHolder.tv_title.setText(listBean.getSd_title());
        //设置商品描述
        viewHolder.tv_goods.setText(listBean.getTitle());
        //设置期号
        viewHolder.tv_qihao.setText("期号 : " + listBean.getQishu());
        //设置评论
        viewHolder.tv_comment.setText(listBean.getSd_content());
        //获取图片数组
        String res[] = listBean.getSd_photolist();
        if (res.length == 1) {
            viewHolder.img_1.setVisibility(View.VISIBLE);
            viewHolder.img_2.setVisibility(View.INVISIBLE);
            viewHolder.img_3.setVisibility(View.INVISIBLE);
        } else if (res.length == 2) {
            viewHolder.img_1.setVisibility(View.VISIBLE);
            viewHolder.img_2.setVisibility(View.VISIBLE);
            viewHolder.img_3.setVisibility(View.INVISIBLE);
        } else if (res.length == 3) {
            viewHolder.img_1.setVisibility(View.VISIBLE);
            viewHolder.img_2.setVisibility(View.VISIBLE);
            viewHolder.img_3.setVisibility(View.VISIBLE);
        }

        if (res.length >= 1) {
            viewHolder.img_1.setImageUrl(res[0], mImageLoader);
            Log.e("TAG", "图片地址 --第一张-->" + res[0]);
            if (res.length >= 2) {
                viewHolder.img_2.setImageUrl(res[1], mImageLoader);
                Log.e("TAG", "图片地址 --第二张-->" + res[1]);
                if (res.length >= 3) {
                    viewHolder.img_3.setImageUrl(res[2], mImageLoader);
                    Log.e("TAG", "图片地址 --第三张-->" + res[2]);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.username:
                //TODO 调转到个人中心
                intent.setClass(v.getContext().getApplicationContext(), CenterActivity.class);
                intent.putExtra("id", uid);
                Log.e("TAG", "Share id = " + uid);
                intent.putExtra("username", username);
                intent.putExtra("headImg", headImg);
                v.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        CircleImageView img_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_title;
        TextView tv_goods;
        TextView tv_qihao;
        TextView tv_comment;
        NetworkImageView img_1;
        NetworkImageView img_2;
        NetworkImageView img_3;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addData(List<ShareListBean> list) {
        if (list != null){
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }
}
