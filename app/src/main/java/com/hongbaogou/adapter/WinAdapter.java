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
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.WinRecodersBean;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/12.
 */
public class WinAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Resources resources;
    private ImageLoader imageLoader;
    private List<WinRecodersBean> winRecodersBeans = new ArrayList<WinRecodersBean>();

    public WinAdapter(Context context) {
        resources = context.getResources();
        inflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();
    }

    public WinRecodersBean getData(int position) {
        return winRecodersBeans.get(position);
    }

    public void addData(List<WinRecodersBean> data) {
        winRecodersBeans.addAll(data);
        notifyDataSetChanged();
    }

    public void reloadData(List<WinRecodersBean> data) {
        winRecodersBeans.clear();
        winRecodersBeans.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        winRecodersBeans.clear();
        notifyDataSetChanged();
    }

    public final class WinViewHold {
        ImageView tenImage;
        NetworkImageView produceImage;
        TextView title;
        TextView zongrenshu;
        TextView qishu;
        TextView luckNumber;
        TextView renci;
        TextView endTime;
    }

    public int getCount() {
        return winRecodersBeans.size();
    }

    public Object getItem(int position) {
        return winRecodersBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        WinViewHold winViewHold = null;
        if (convertView == null) {
            winViewHold = new WinViewHold();
            convertView = inflater.inflate(R.layout.win_record_item, null);
            winViewHold.tenImage = (ImageView) convertView.findViewById(R.id.tenImage);
            winViewHold.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            winViewHold.title = (TextView) convertView.findViewById(R.id.title);
            winViewHold.zongrenshu = (TextView) convertView.findViewById(R.id.zongrenshu);
            winViewHold.qishu = (TextView) convertView.findViewById(R.id.qishu);
            winViewHold.luckNumber = (TextView) convertView.findViewById(R.id.luckNumber);
            winViewHold.renci = (TextView) convertView.findViewById(R.id.renci);
            winViewHold.endTime = (TextView) convertView.findViewById(R.id.end_time);
            convertView.setTag(winViewHold);
        } else {
            winViewHold = (WinViewHold) convertView.getTag();
        }

        WinRecodersBean winRecodersBean = winRecodersBeans.get(position);

        String isten = winRecodersBean.getIs_ten();
        if (isten.equals("0")) {
            winViewHold.tenImage.setVisibility(View.GONE);
        }

        winViewHold.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        winViewHold.produceImage.setErrorImageResId(R.mipmap.img_blank);
        winViewHold.produceImage.setImageUrl(winRecodersBean.getThumb(), imageLoader);


        winViewHold.title.setText(winRecodersBean.getTitle());
        winViewHold.zongrenshu.setText(resources.getString(R.string.win_total, winRecodersBean.getZongrenshu()));
        winViewHold.qishu.setText(resources.getString(R.string.win_join_issue, winRecodersBean.getQishu()));

        //设置中将号码
        String number = winRecodersBean.getQ_user_code();
        SpannableStringBuilder numberBuilder = new SpannableStringBuilder(resources.getString(R.string.win_luck_number, number));
        ForegroundColorSpan numberColor = new ForegroundColorSpan(resources.getColor(R.color.self_buy_luck_numner));
        numberBuilder.setSpan(numberColor, numberBuilder.length() - number.length(), numberBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        winViewHold.luckNumber.setText(numberBuilder);

        winViewHold.renci.setText(resources.getString(R.string.win_jion_count, winRecodersBean.getRenci()));
        winViewHold.endTime.setText(resources.getString(R.string.win_time, winRecodersBean.getQ_end_time()));

        return convertView;
    }
}
