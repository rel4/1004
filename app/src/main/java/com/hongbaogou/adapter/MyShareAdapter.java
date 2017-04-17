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
import com.hongbaogou.activity.ShareKnowActivity;
import com.hongbaogou.activity.ShareSelfActivity;
import com.hongbaogou.bean.BeanMySearchList;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class MyShareAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;

    private List<BeanMySearchList> list = new ArrayList<BeanMySearchList>();

    public static final int ON_SHARE_LAYOUT = 0;
    public static final int YES_SHARE_LAYOUT = 1;

    public MyShareAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();
    }


    /**
     * @param position 根据传入的position
     * @return 返回相应的item
     */
    @Override
    public int getItemViewType(int position) {

        BeanMySearchList mySearchList = list.get(position);
        int type = mySearchList.getIs_shaidan();
        if (type == 0) {
            return ON_SHARE_LAYOUT;
        }
        return YES_SHARE_LAYOUT;
    }

    /**
     * 指定item 的个数
     *
     * @return 2个
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

//    public BeanMySearchList getObjectBean(int position) {
//        BeanMySearchList searchList = list.get(position);
//        return searchList;
//    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BeanMySearchList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //获取当前item的类型
        int type = getItemViewType(position);
        Viewholder viewholder = null;
        View_holder view_holder = null;

        if (convertView == null) {
            switch (type) {
                //如果type=0,为不可晒单状态
                case ON_SHARE_LAYOUT:
                    viewholder = new Viewholder();
                    convertView = mInflater.inflate(R.layout.my_share_item, parent, false);
                    //已审核字样
                    viewholder.about = (TextView) convertView.findViewById(R.id.auto);
                    //晒带的标题
                    viewholder.title = (TextView) convertView.findViewById(R.id.title);
                    //时间
                    viewholder.time = (TextView) convertView.findViewById(R.id.time);
                    //商品的图片
                    viewholder.goods_img = (NetworkImageView) convertView.findViewById(R.id.goods_img);
                    //晒单内容的商品标题
                    viewholder.goodstitle = (TextView) convertView.findViewById(R.id.goodstitle);
                    //获奖者的评论
                    viewholder.comment = (TextView) convertView.findViewById(R.id.comment);

                    convertView.setTag(viewholder);

//                    convertView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(parent.getContext(), ShareSelfActivity.class);
//                            intent.putExtra("username", Pref_Utils.getString(parent.getContext(), "username"));
//                            intent.putExtra("headImg", Pref_Utils.getString(parent.getContext(), "headImg"));
//                            intent.putExtra("uid", Pref_Utils.getString(parent.getContext(), "uid"));
//                            Log.e("TAG", "headImgUrl = " + Pref_Utils.getString(parent.getContext(), "username"));
//                            Log.e("TAG", "username = " + Pref_Utils.getString(parent.getContext(), "headImg"));
//                            Log.e("TAG", "uid = " +  Pref_Utils.getString(parent.getContext(),"uid"));
//                            intent.getBooleanExtra("isSelf", false);
//                        }
//                    });
                    break;
                case YES_SHARE_LAYOUT:
                    view_holder = new View_holder();
                    convertView = mInflater.inflate(R.layout.my_no_share_item, parent, false);
                    view_holder.goodsimg = (NetworkImageView) convertView.findViewById(R.id.goodsimg);

                    view_holder.goodsDetails = (TextView) convertView.findViewById(R.id.goodsdetails);
                    view_holder.go_share = (TextView) convertView.findViewById(R.id.go_share);

                    view_holder.go_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(parent.getContext().getApplicationContext(), ShareKnowActivity.class);
                            //传入uid
                            intent.putExtra("uid", list.get(position).getSd_userid());
                            Log.e("TAG", "uid = " + list.get(position).getSd_userid());
                            //传入期数
                            intent.putExtra("qishu", list.get(position).getSd_qishu());
                            //shopid
                            intent.putExtra("shopid", list.get(position).getSd_shopid());
                            //shopsid
                            intent.putExtra("shopsid", list.get(position).getSd_shopsid());

                            //传入title
                            intent.putExtra("title", list.get(position).getSd_title());
                            //传入商品的title
                            intent.putExtra("goodstitle", list.get(position).getGoods_title());
                            //传入晒单的内容
                            intent.putExtra("content", list.get(position).getSd_content());
                            //传入幸运号
                            intent.putExtra("lucknumber", list.get(position).getQ_user_code());
                            //传入揭晓的事件
                            intent.putExtra("endtime", list.get(position).getQ_end_time());
                            //传入中奖者的参与人次
                            intent.putExtra("gonumber", list.get(position).getGonumber());
                            parent.getContext().startActivity(intent);
                        }
                    });
                    convertView.setTag(view_holder);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case ON_SHARE_LAYOUT:
                    setData2UI(position, parent, convertView);
                    break;
                case YES_SHARE_LAYOUT:
                    view_holder = (View_holder) convertView.getTag();
                    final BeanMySearchList mySearchList = list.get(position);
                    view_holder.goodsimg.setImageUrl(mySearchList.getSd_thumbs(), imageLoader);
                    view_holder.goodsimg.setVisibility(View.VISIBLE);
                    view_holder.goodsimg.setDefaultImageResId(R.mipmap.img_blank);
                    view_holder.goodsDetails.setText("(期号 : " + mySearchList.getSd_qishu() + ")" + mySearchList.getGoods_title());

                    break;
                default:
                    break;
            }
        }
        this.notifyDataSetChanged();
        return convertView;
    }

    /**
     * 审核失败
     */
    public static final int LOSE = -1;
    /**
     * 等待审核
     */
    public static final int LODING = 0;
    /**
     * 审核通过
     */
    public static final int PASS = 1;

    //设置数据
    private void setData2UI(int position, final ViewGroup parent, View convertView) {

        Viewholder viewHolder = (Viewholder) convertView.getTag();
        final BeanMySearchList mySearchList = list.get(position);

        //获取晒单的状态
        int state = mySearchList.getIs_audit();

        switch (state) {
            //审核失败
            case LOSE:
                //审核失败 设置字体为审核失败
                //设置背景为红色
                viewHolder.about.setText("审核失败");
                viewHolder.about.setBackgroundColor(parent.getResources().getColor(R.color.color_red_fail));
                break;
            //0 待审核
            case LODING:
                //等待审核 设置字体为等待审核
                //设置背景为黄色
                viewHolder.about.setText("等待审核");
                viewHolder.about.setBackgroundColor(parent.getResources().getColor(R.color.color_orange));
                break;
            //审核通过
            case PASS:
                //已审核 设置字体为已审核
                //设置背景为绿色
                viewHolder.about.setText("已审核");
                viewHolder.about.setBackgroundColor(parent.getResources().getColor(R.color.color_green));
                break;
            default:
                break;
        }

        /**
         * 设置 convertView 的点击事件 跳转到适当的晒单详情
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), ShareSelfActivity.class);
                intent.putExtra("sd_id", mySearchList.getSd_id());
                intent.putExtra("shopid", mySearchList.getSd_shopid());
                intent.putExtra("issue", mySearchList.getSd_qishu());

                intent.putExtra("username", Pref_Utils.getString(parent.getContext(), "username"));
                intent.putExtra("headImg", mySearchList.getSd_img());
                intent.putExtra("uid", mySearchList.getUid());

                parent.getContext().startActivity(intent);
            }
        });

        //设置晒单的标题
        viewHolder.title.setText(mySearchList.getSd_title());
        //设置晒单的事件
        viewHolder.time.setText(mySearchList.getSd_time());
        //设置晒单内容的商品标题
        viewHolder.goodstitle.setText(mySearchList.getGoods_title());
        //设置获奖者的评论内容
        viewHolder.comment.setText(mySearchList.getSd_content());
        viewHolder.goods_img.setImageUrl(mySearchList.getSd_thumbs(), imageLoader);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<BeanMySearchList> data) {
        if (data != null) {
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 可以晒单的viewholder
     */
    private class Viewholder {
        //已审核
        TextView about;
        //晒单的标题
        TextView title;
        //时间
        TextView time;
        //商品的图片
        NetworkImageView goods_img;
        //晒单内容的商品标题
        TextView goodstitle;
        //获奖者的评论
        TextView comment;
    }

    /**
     * 不可以晒单的viewholder
     */
    private class View_holder {
        //商品的图片
        NetworkImageView goodsimg;
        //中奖的商品详情
        TextView goodsDetails;
        //立即晒单的按钮
        TextView go_share;
    }
}
