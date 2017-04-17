package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hongbaogou.R;
import com.hongbaogou.bean.JoinPeopleBean;
import com.hongbaogou.utils.RequestManager;
import com.hongbaogou.view.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/29.
 */
public class JoinPeopleAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ImageLoader imageLoader;
    private List<JoinPeopleBean> joinPeopleBeans = new ArrayList<JoinPeopleBean>();
    private ForegroundColorSpan baseColor;
    private ForegroundColorSpan joinCountColor;
    private Resources resources;

    public void addData(List<JoinPeopleBean> loadData){
        joinPeopleBeans.addAll(loadData);
        notifyDataSetChanged();
    }

    public void clear(){
        joinPeopleBeans.clear();
        notifyDataSetChanged();
    }

    public JoinPeopleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();

        resources = context.getResources();

        baseColor = new ForegroundColorSpan(resources.getColor(R.color.join_time));
        joinCountColor = new ForegroundColorSpan(resources.getColor(R.color.join_count));
    }

    public JoinPeopleBean getPositionObject(int position){
        return joinPeopleBeans.get(position);
    }


    public int getCount() {
        return joinPeopleBeans.size();
    }

    public Object getItem(int position) {
        return joinPeopleBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        JoinPeopleHoldView joinPeopleHoldView;
        if(convertView == null){
            joinPeopleHoldView = new JoinPeopleHoldView();
            convertView = inflater.inflate(R.layout.buygoodpeopleitem,null);
            joinPeopleHoldView.name = (TextView)convertView.findViewById(R.id.name);
            joinPeopleHoldView.joinCount = (TextView)convertView.findViewById(R.id.joinCount);
            joinPeopleHoldView.headImage = (CircleImageView)convertView.findViewById(R.id.headImage);

            convertView.setTag(joinPeopleHoldView);
        } else {
            joinPeopleHoldView = (JoinPeopleHoldView)convertView.getTag();
        }

        JoinPeopleBean joinPeopleBean = joinPeopleBeans.get(position);
        String username = joinPeopleBean.getUsername();
        String area = joinPeopleBean.getArea();
        String ip = joinPeopleBean.getIp();
        String str = username + "(" +area + " IP: " + ip +")";
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(baseColor, username.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinPeopleHoldView.name.setText(builder);

        String joinCount = joinPeopleBean.getCount_gonumber();
        String time = joinPeopleBean.getTime();
        String count = resources.getString(R.string.join_people, joinCount);
        String time_and_count =  count +" " + time;
        Log.d("Tag", time_and_count);
        SpannableStringBuilder jointCountBuilder = new SpannableStringBuilder(time_and_count);
        jointCountBuilder.setSpan(joinCountColor, 3, 3 + joinCount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        jointCountBuilder.setSpan(baseColor, count.length(), time_and_count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinPeopleHoldView.joinCount.setText(jointCountBuilder);

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(joinPeopleHoldView.headImage, R.mipmap.img_blank, R.mipmap.img_blank);
        imageLoader.get(joinPeopleBean.getUphoto(),listener);

        return convertView;
    }

    public final class JoinPeopleHoldView{
        public TextView name;
        public TextView joinCount;
        public CircleImageView headImage;
    }
}
