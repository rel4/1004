package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.BeanAllLogList;
import com.hongbaogou.bean.BeanAllLogUser;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class SelfBuyAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<BeanAllLogList> list  = new ArrayList<BeanAllLogList>();
    private ImageLoader imageLoader;
    private Resources resource;
    private OnBuyClickListener onBuyClickListener;
    private boolean isSelf ;

    public SelfBuyAdapter(Context context,boolean isSelf) {
        imageLoader = RequestManager.getImageLoader();
        resource = context.getResources();
        mInflater = LayoutInflater.from(context);
        this.isSelf = isSelf;
    }

    public void setOnBuyClickListener(OnBuyClickListener onBuyClickListener) {
        this.onBuyClickListener = onBuyClickListener;
    }

    public int getCount() {
        return list.size();
    }

    public BeanAllLogList getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.log_item, null);
            viewHolder = new ViewHolder();
            viewHolder.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            viewHolder.tenImage = (ImageView) convertView.findViewById(R.id.tenImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.qishu = (TextView) convertView.findViewById(R.id.qishu);
            viewHolder.totlenumber = (TextView) convertView.findViewById(R.id.totlenumber);
            viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            viewHolder.canyurenshu = (TextView) convertView.findViewById(R.id.canyurenshu);
            viewHolder.remainder = (TextView) convertView.findViewById(R.id.remainder);
            viewHolder.mynumber = (TextView) convertView.findViewById(R.id.mynumber);
            viewHolder.countDownTime = (TextView) convertView.findViewById(R.id.countDownTime);
            viewHolder.winer = (TextView) convertView.findViewById(R.id.winer);
            viewHolder.peoplenumber = (TextView) convertView.findViewById(R.id.peoplenumber);
            viewHolder.lucknumber = (TextView) convertView.findViewById(R.id.lucknumber);
            viewHolder.endtime = (TextView) convertView.findViewById(R.id.endtime);
            viewHolder.line = (View) convertView.findViewById(R.id.line);
            viewHolder.addtobtn = (TextView) convertView.findViewById(R.id.addtobtn);
            viewHolder.winUser = (LinearLayout) convertView.findViewById(R.id.winUser);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        BeanAllLogList buyBean = list.get(position);

        if(isSelf){
            viewHolder.mynumber.setText(resource.getString(R.string.mynumber));
            viewHolder.addtobtn.setText(resource.getString(R.string.addTo));
        } else {
            viewHolder.mynumber.setText(resource.getString(R.string.othernumber));
            viewHolder.addtobtn.setText(resource.getString(R.string.toBuy));
        }

        //1表示10元专区，0表示不是
        if(buyBean.getIs_ten() == 1){
            viewHolder.tenImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tenImage.setVisibility(View.GONE);
        }

        viewHolder.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        viewHolder.produceImage.setErrorImageResId(R.mipmap.img_blank);
        viewHolder.produceImage.setImageUrl(buyBean.getThumb(), imageLoader);

        //设置商品title
        viewHolder.title.setText(buyBean.getShopname());
        //设置期数
        viewHolder.qishu.setText(resource.getString(R.string.self_qishu, buyBean.getShopqishu()));

        //设置总人数
        String zongrenshu = buyBean.getZongrenshu();
        String canyurenshu = buyBean.getCanyurenshu();
        viewHolder.totlenumber.setText(resource.getString(R.string.self_total, zongrenshu));

        //设置参与人数
        String gonumber = buyBean.getGonumber();
        SpannableStringBuilder canyurenshuBuilder = new SpannableStringBuilder(resource.getString(R.string.win_jion_count,gonumber));
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resource.getColor(R.color.self_buy_in));
        canyurenshuBuilder.setSpan(joinCountColor, 5, 5 + gonumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.canyurenshu.setText(canyurenshuBuilder);

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBuyClickListener.toDetail(position);
            }
        });

        viewHolder.winUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBuyClickListener.toWinCenter(position);
            }
        });

        viewHolder.addtobtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBuyClickListener.buyMore(position);
            }
        });

        viewHolder.mynumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBuyClickListener.lookSelfNumber(position);
            }
        });

        int tag = buyBean.getTag();
        //0已揭晓  1进行中  2倒计时
        if(tag == 0){
            viewHolder.winUser.setVisibility(View.VISIBLE);
            viewHolder.countDownTime.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.addtobtn.setVisibility(View.GONE);
            viewHolder.progress.setVisibility(View.GONE);
            viewHolder.remainder.setVisibility(View.GONE);

            BeanAllLogUser winBean = buyBean.getQ_user();
            //设置获奖者
            String username =winBean.getUsername();
            SpannableStringBuilder usernameBuilder = new SpannableStringBuilder(resource.getString(R.string.self_win_name,username));
            ForegroundColorSpan usernameColor = new ForegroundColorSpan(resource.getColor(R.color.color_blue));
            usernameBuilder.setSpan(usernameColor, usernameBuilder.length() - username.length(), usernameBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.winer.setText(usernameBuilder);

            //设置本期参与人次
            String count = String.valueOf(winBean.getGonumber());
            SpannableStringBuilder countBuilder = new SpannableStringBuilder(resource.getString(R.string.win_jion_count,count));
            ForegroundColorSpan countColor = new ForegroundColorSpan(resource.getColor(R.color.self_buy_in));
            countBuilder.setSpan(countColor,  5, 5 + count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.peoplenumber.setText(countBuilder);

            //设置中将号码
            String number =winBean.getQ_user_code();
            SpannableStringBuilder numberBuilder = new SpannableStringBuilder(resource.getString(R.string.win_luck_number,number));
            ForegroundColorSpan numberColor = new ForegroundColorSpan(resource.getColor(R.color.self_buy_luck_numner));
            numberBuilder.setSpan(numberColor, numberBuilder.length() - number.length(), numberBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.lucknumber.setText(numberBuilder);
            //设置揭晓的时间
            viewHolder.endtime.setText(resource.getString(R.string.win_time, buyBean.getQ_end_time()));

        } else if(tag == 1){
            viewHolder.winUser.setVisibility(View.GONE);
            viewHolder.countDownTime.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.addtobtn.setVisibility(View.VISIBLE);
            viewHolder.progress.setVisibility(View.VISIBLE);
            viewHolder.remainder.setVisibility(View.VISIBLE);
            int all = Integer.parseInt(zongrenshu);
            int in = Integer.parseInt(canyurenshu);
            viewHolder.remainder.setText(resource.getString(R.string.self_remainder, (all - in)));
            viewHolder.progress.setMax(all);
            viewHolder.progress.setProgress(in);

        } else if(tag == 2){
            viewHolder.winUser.setVisibility(View.GONE);
            viewHolder.countDownTime.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.addtobtn.setVisibility(View.GONE);
            viewHolder.progress.setVisibility(View.VISIBLE);
            viewHolder.remainder.setVisibility(View.VISIBLE);
            viewHolder.remainder.setText(resource.getString(R.string.self_remainder, 0));
            viewHolder.progress.setMax(100);
            viewHolder.progress.setProgress(100);
        }
        return convertView;
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addData(List<BeanAllLogList> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 清空数据
     */
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public BeanAllLogList getBeanAllLogListByPosition(int positioin){
        return list.get(positioin);
    }

    private class ViewHolder {
        NetworkImageView produceImage;
        ImageView tenImage;
        TextView title;
        TextView qishu;
        TextView totlenumber;
        ProgressBar progress;
        TextView remainder;
        TextView canyurenshu;
        TextView mynumber;
        TextView winer;
        TextView peoplenumber;
        TextView lucknumber;
        TextView endtime;
        View line;
        TextView addtobtn;
        TextView countDownTime;
        LinearLayout winUser;
    }


    public interface OnBuyClickListener{
        public void lookSelfNumber(int position);
        public void toWinCenter(int position);
        public void buyMore(int position);
        public void toDetail(int position);
    }

}
