package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
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

import java.util.List;

/**
 * Created by lenovo on 2015/12/5.
 */
public class WinRecordAdapter extends BaseAdapter {
    private Context context;
    private List<WinRecodersBean> data;
    private ImageLoader imageLoader;
    private WinRecodersBean bean;
    private Resources resource;
 //
    //      ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.my_color);
    public void addData(List<WinRecodersBean> datalist) {
        data.clear();
       data.addAll(datalist);
        notifyDataSetChanged();
    }

    public WinRecordAdapter(Context context, List<WinRecodersBean> data) {
        this.context = context;
        this.data = data;
        imageLoader = RequestManager.getImageLoader();
         resource = context.getResources();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_winrecordadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_signin = (TextView) convertView.findViewById(R.id.tv_signin);

            viewHolder.tv__winrecord_title = (TextView) convertView.findViewById(R.id.tv__winrecord_title);
            viewHolder.tv__winrecord_qishu = (TextView) convertView.findViewById(R.id.tv__winrecord_qishu);
            viewHolder.tv__winrecord_zongrenshu = (TextView) convertView.findViewById(R.id.tv__winrecord_zongrenshu);
            viewHolder.tv__winrecord_q_user_code = (TextView) convertView.findViewById(R.id.tv__winrecord_q_user_code);
            viewHolder.tv__winrecord_q_user_renci = (TextView) convertView.findViewById(R.id.tv__winrecord_q_user_renci);
            viewHolder.tv__winrecord_q_user_q_end_time = (TextView) convertView.findViewById(R.id.tv__winrecord_q_user_q_end_time);

            viewHolder.img_ten = (ImageView) convertView.findViewById(R.id.img_ten);

            viewHolder.netImage_winrecord = (NetworkImageView) convertView.findViewById(R.id.netImage_winrecord);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bean = data.get(position);
        viewHolder.netImage_winrecord.setDefaultImageResId(R.mipmap.img_blank);
        viewHolder.netImage_winrecord.setErrorImageResId(R.mipmap.img_blank);
        viewHolder.netImage_winrecord.setImageUrl(bean.getThumb(), imageLoader);

        viewHolder.tv__winrecord_qishu.setText("期号：" + bean.getQishu());
        viewHolder.tv__winrecord_title.setText(bean.getTitle());
        viewHolder.tv__winrecord_q_user_renci.setText("本期参与:" + bean.getRenci() + "人次");
        viewHolder.tv__winrecord_zongrenshu.setText("总需:" + bean.getZongrenshu() + "人次");
        viewHolder.tv__winrecord_q_user_code.setText(bean.getQ_user_code());
        viewHolder.tv__winrecord_q_user_q_end_time.setText("揭晓时间:" + bean.getQ_end_time());

        if ("1".equals(bean.getIs_ten())) {
            viewHolder.img_ten.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img_ten.setVisibility(View.INVISIBLE);
        }
        switch (Integer.parseInt(bean.getQianshou())) {
            case 0:
                viewHolder.tv_signin.setText("待签收");
                viewHolder.tv_signin.setTextColor(resource.getColorStateList(R.color.color_signin_red));
                break;
            case 1:
                viewHolder.tv_signin.setText("已签收");
                viewHolder.tv_signin.setTextColor( resource.getColorStateList(R.color.color_signin) );
                break;
            case 2:
                viewHolder.tv_signin.setText("等待发货");
                viewHolder.tv_signin.setTextColor(resource.getColorStateList(R.color.color_signin_red));
                break;
            case 3:
                viewHolder.tv_signin.setText("请确认收货地址");
                viewHolder.tv_signin.setTextColor(resource.getColorStateList(R.color.color_signin_red));
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {

        public NetworkImageView netImage_winrecord;
        public TextView tv_signin, tv__winrecord_title, tv__winrecord_qishu,
                tv__winrecord_zongrenshu, tv__winrecord_q_user_code,
                tv__winrecord_q_user_renci, tv__winrecord_q_user_q_end_time;
        public ImageView img_ten;
    }
}
