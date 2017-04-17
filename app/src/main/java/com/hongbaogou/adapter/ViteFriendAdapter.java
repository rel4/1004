package com.hongbaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.ViteFriendBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/10/21.
 */
public class ViteFriendAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<ViteFriendBean.DataBean> viteFriendBeans = new ArrayList<ViteFriendBean.DataBean>();
    private String tag;


    public void addData(List<ViteFriendBean.DataBean> loadData,String tag) {
        viteFriendBeans.addAll(loadData);
        notifyDataSetChanged();
        this.tag=tag;
        loadData = null;
    }

    public ViteFriendAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return viteFriendBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return viteFriendBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsViewHold goodsViewHold = null;
        if (convertView == null) {
            goodsViewHold = new GoodsViewHold();
            convertView = inflater.inflate(R.layout.viteitem, null);
            goodsViewHold.ll_cash_record=(LinearLayout)convertView.findViewById(R.id.ll_cash_record);
            goodsViewHold.ll_comm_record=(LinearLayout)convertView.findViewById(R.id.ll_comm_record);
            goodsViewHold.ll_vitefriend_record=(LinearLayout)convertView.findViewById(R.id.ll_vitefriend_record);


            goodsViewHold.tv_sq_time=(TextView)convertView.findViewById(R.id.tv_sq_time);
            goodsViewHold.winxin_num=(TextView)convertView.findViewById(R.id.winxin_num);
            goodsViewHold.tv_cash_money=(TextView)convertView.findViewById(R.id.tv_cash_money);
            goodsViewHold.tv_cash_status=(TextView)convertView.findViewById(R.id.tv_cash_status);

            goodsViewHold.tv_comm_time=(TextView)convertView.findViewById(R.id.tv_comm_time);
            goodsViewHold.tv_friend_num=(TextView)convertView.findViewById(R.id.tv_friend_num);
            goodsViewHold.tv_comm_record=(TextView)convertView.findViewById(R.id.tv_comm_record);
            goodsViewHold.tv_get_money=(TextView)convertView.findViewById(R.id.tv_get_money);


            goodsViewHold.tv_username=(TextView)convertView.findViewById(R.id.tv_username);
            goodsViewHold.tv_friend_time=(TextView)convertView.findViewById(R.id.tv_friend_time);
            goodsViewHold.tv_friend_cost=(TextView)convertView.findViewById(R.id.tv_friend_cost);

        } else {
            goodsViewHold = (GoodsViewHold) convertView.getTag();
        }
        if("friend".equals(tag)){

            goodsViewHold.ll_cash_record.setVisibility(View.GONE);
            goodsViewHold.ll_comm_record.setVisibility(View.GONE);
            goodsViewHold.ll_vitefriend_record.setVisibility(View.VISIBLE);
            goodsViewHold.tv_username.setText(viteFriendBeans.get(position).getMobile());
            goodsViewHold.tv_friend_time.setText(timet(viteFriendBeans.get(position).getAddtime()));
            goodsViewHold.tv_friend_cost.setText(viteFriendBeans.get(position).getLev());

        }else if("comm".equals(tag)){
            goodsViewHold.ll_cash_record.setVisibility(View.GONE);
            goodsViewHold.ll_comm_record.setVisibility(View.VISIBLE);
            goodsViewHold.ll_vitefriend_record.setVisibility(View.GONE);

            goodsViewHold.tv_comm_time.setText(timet(viteFriendBeans.get(position).getTime()));
            goodsViewHold.tv_friend_num.setText(viteFriendBeans.get(position).getMobile());
            goodsViewHold.tv_comm_record.setText(viteFriendBeans.get(position).getMoney());
            if(viteFriendBeans.get(position).getType().equals("1")){
                goodsViewHold.tv_comm_record.setText("充值"+viteFriendBeans.get(position).getMoney());
            }
            goodsViewHold.tv_get_money.setText(viteFriendBeans.get(position).getYgmoney());

        }else {
            goodsViewHold.ll_cash_record.setVisibility(View.VISIBLE);
            goodsViewHold.ll_comm_record.setVisibility(View.GONE);
            goodsViewHold.ll_vitefriend_record.setVisibility(View.GONE);

            goodsViewHold.tv_sq_time.setText(timet(viteFriendBeans.get(position).getTime()));
            goodsViewHold.winxin_num.setText(viteFriendBeans.get(position).getWeixin());
            goodsViewHold.tv_cash_money.setText(viteFriendBeans.get(position).getMoney());
            goodsViewHold.tv_cash_status.setText(viteFriendBeans.get(position).getStatus());

        }

        return convertView;
    }


    public final class GoodsViewHold {//提现
        public LinearLayout ll_cash_record;
        public TextView tv_sq_time;
        public TextView winxin_num;
        public TextView tv_cash_money;
        public TextView tv_cash_status;

        //佣金
        public LinearLayout ll_comm_record;
        public TextView tv_comm_time;
        public TextView tv_friend_num;
        public TextView tv_comm_record;
        public TextView tv_get_money;

//好友
        public LinearLayout ll_vitefriend_record;
        public TextView tv_username;
        public TextView tv_friend_time;
        public TextView tv_friend_cost;

    }

    public String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }


}
