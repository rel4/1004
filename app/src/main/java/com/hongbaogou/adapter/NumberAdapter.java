package com.hongbaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.SeeNumberDetailsActivity;
import com.hongbaogou.bean.BeanNumberList;
import com.hongbaogou.utils.Pref_Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class NumberAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private String uid = "";

    private List<BeanNumberList> list = new ArrayList<BeanNumberList>();

    public NumberAdapter(Context context, String uid) {
        mInflater = LayoutInflater.from(context);
        this.uid = uid;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BeanNumberList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.morenumber_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.duobao_time);
            viewHolder.peopleNumber = (TextView) convertView.findViewById(R.id.number_people);
            viewHolder.seeNumber = (TextView) convertView.findViewById(R.id.see_number);
            convertView.setTag(viewHolder);
        }

        setData2UI(position, parent, convertView);

        return convertView;
    }


    private void setData2UI(final int position, final ViewGroup parent, View convertView) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        final BeanNumberList number = list.get(position);
        String time = number.getTime();
        StringBuilder str = getString(time, "\r\n", 10);
        //设置时间
        viewHolder.time.setText("\t" + str.toString());

        //设置人数
        String s = number.getGonumber() + "人次";
        Log.d("TAG", "-----" + str.length());
        SpannableStringBuilder ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(parent.getResources().getColor(R.color.color_black)), s.length() - 2, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        viewHolder.peopleNumber.setText(ssb);

        String uid = Pref_Utils.getString(parent.getContext(), "uid", "");
        if (this.uid.equals(uid) && uid != "") {
            viewHolder.seeNumber.setText("查看我的号码");
        }

        viewHolder.seeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), SeeNumberDetailsActivity.class);

                intent.putExtra("rid", number.getRid());
                parent.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 拼接字符串
     *
     * @param s1
     * @param s2
     * @param l
     * @return
     */
    public static StringBuilder getString(String s1, String s2, int l) {
        StringBuilder sb = new StringBuilder();
        sb.append(s1).insert(l, s2);
        return sb;
    }

    public void addData(List<BeanNumberList> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView time;
        TextView peopleNumber;
        TextView seeNumber;
    }
}
