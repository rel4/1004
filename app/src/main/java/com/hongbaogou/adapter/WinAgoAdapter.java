package com.hongbaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.activity.CenterActivity;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.bean.UserBean;
import com.hongbaogou.bean.WinAgoBean;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class WinAgoAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private Resources resource;
    private List<WinAgoBean> winAgoBeans = new ArrayList<WinAgoBean>();

    public void addData(List<WinAgoBean> data){
        winAgoBeans.addAll(data);
        notifyDataSetChanged();
    }

    public WinAgoAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();
        resource = context.getResources();
    }

    public int getCount() {
        return winAgoBeans.size();
    }


    public Object getItem(int position) {
        return winAgoBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        WinAgoViewHold winAgoViewHold = null;
        if (convertView == null) {
            winAgoViewHold = new WinAgoViewHold();
            convertView = inflater.inflate(R.layout.win_age_item, null);
            winAgoViewHold.details = (RelativeLayout)convertView.findViewById(R.id.details);
            winAgoViewHold.issue = (TextView)convertView.findViewById(R.id.issue);
            winAgoViewHold.endTime = (TextView)convertView.findViewById(R.id.endTime);
            winAgoViewHold.winHeadImage = (ImageView)convertView.findViewById(R.id.winHeadImage);
            winAgoViewHold.center = (RelativeLayout)convertView.findViewById(R.id.center);
            winAgoViewHold.winUserName = (TextView)convertView.findViewById(R.id.winUserName);
            winAgoViewHold.winId = (TextView)convertView.findViewById(R.id.winId);
            winAgoViewHold.luckNumber = (TextView)convertView.findViewById(R.id.luckNumber);
            winAgoViewHold.total = (TextView)convertView.findViewById(R.id.total);
            convertView.setTag(winAgoViewHold);
        } else {
            winAgoViewHold = (WinAgoViewHold)convertView.getTag();
        }
        WinAgoBean winAgoBean = winAgoBeans.get(position);
        //0已揭晓、1进行中、2倒计时、3期满,已下架
        int tag = winAgoBean.getTag();
        if(tag == 0) {
            winAgoViewHold.center.setVisibility(View.VISIBLE);
            winAgoViewHold.issue.setText(resource.getString(R.string.add_issue, winAgoBean.getQishu()));
            winAgoViewHold.endTime.setText(resource.getString(R.string.luck_time, winAgoBean.getQ_end_time()));
            winAgoViewHold.endTime.setTextColor(resource.getColor(R.color.win_ago_issue));

            UserBean userBean = winAgoBean.getQ_user();
            ImageLoader.ImageListener lis = ImageLoader.getImageListener(winAgoViewHold.winHeadImage, R.mipmap.img_blank, R.mipmap.img_blank);
            RequestManager.getImageLoader().get(userBean.getImg(), lis, 0, 0);


            String name = userBean.getUsername();
            String username  = resource.getString(R.string.win_join_people, name);
            SpannableString usernameSpan = new SpannableString(username);
            usernameSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1361FF")), username.length() - name.length(), username.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            winAgoViewHold.winUserName.setText(usernameSpan);

            winAgoViewHold.winId.setText(resource.getString(R.string.luck_id, userBean.getUid()));


            String luckNumber = winAgoBean.getQ_user_code();

            String luck  = resource.getString(R.string.luck_number, luckNumber);
            SpannableString luckSpan = new SpannableString(luck);
            luckSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#F83061")), luck.length() - luckNumber.length(), luck.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            winAgoViewHold.luckNumber.setText(luckSpan);

            String renci = String.valueOf(userBean.getRenci());
            String str  = resource.getString(R.string.win_jion_count, renci);
            SpannableString renciSpan = new SpannableString(str);
            renciSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#F83061")), str.length() - renci.length()-2, str.length()-2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            winAgoViewHold.total.setText(renciSpan);

        } else if(tag == 2){
            winAgoViewHold.center.setVisibility(View.GONE);
            winAgoViewHold.issue.setText(resource.getString(R.string.luck_issue,winAgoBean.getQishu()));
            winAgoViewHold.endTime.setText(resource.getString(R.string.end_now));
            winAgoViewHold.endTime.setTextColor(resource.getColor(R.color.win_ago_end_time));
        }

        winAgoViewHold.details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, GoodsDetailActivity.class);
                WinAgoBean winAgoBean = winAgoBeans.get(position);
                intent.putExtra("id", winAgoBean.getId());
                intent.putExtra("issue", winAgoBean.getQishu());
                context.startActivity(intent);
            }
        });

        winAgoViewHold.center.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CenterActivity.class);
                WinAgoBean winAgoBean = winAgoBeans.get(position);
                UserBean userBean = winAgoBean.getQ_user();
                intent.putExtra("username", userBean.getUsername());
                intent.putExtra("headImg",userBean.getImg());
                intent.putExtra("id",userBean.getUid());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private final class WinAgoViewHold{
        RelativeLayout details;
        TextView issue;
        TextView endTime;
        ImageView winHeadImage;
        TextView winUserName;
        TextView winId;
        TextView luckNumber;
        TextView total;
        RelativeLayout center;
    }
}
