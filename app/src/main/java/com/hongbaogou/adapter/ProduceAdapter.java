package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.GoodListBean;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class ProduceAdapter extends BaseAdapter {

    private AddListListener addListListener;
    private LayoutInflater inflater = null;
    private List<GoodListBean> goodListBeans = new ArrayList<GoodListBean>();
    private Resources resource;
    private ImageLoader imageLoader;

    public void addData(List<GoodListBean> goodData) {
        goodListBeans.addAll(goodData);
        notifyDataSetChanged();
    }

    public void clearData() {
        goodListBeans.clear();
        notifyDataSetChanged();
    }

    public GoodListBean getData(int position) {
        return goodListBeans.get(position);
    }


    public ProduceAdapter(Context context, AddListListener addListListener) {
        inflater = LayoutInflater.from(context);
        this.addListListener = addListListener;
        imageLoader = RequestManager.getImageLoader();
        resource = context.getResources();
    }

    public int getCount() {
        return goodListBeans.size();
    }

    public Object getItem(int position) {
        return goodListBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private final class Holder {
        public ImageView tenImage;
        public NetworkImageView produceImage;
        public TextView progressTextView;
        public TextView title;
        public ProgressBar progress;
        public TextView addList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.griditem, null);
            holder.tenImage = (ImageView) convertView.findViewById(R.id.tenImage);
            holder.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.progressTextView = (TextView) convertView.findViewById(R.id.progressTextView);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.addList = (TextView) convertView.findViewById(R.id.addList);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        GoodListBean goodListBean = goodListBeans.get(position);
        //1表示十元专区 0表示不是
        if (goodListBean.getIs_ten() == 1) {
            holder.tenImage.setVisibility(View.VISIBLE);
        } else {
            holder.tenImage.setVisibility(View.GONE);
        }

        holder.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        holder.produceImage.setErrorImageResId(R.mipmap.img_blank);
        holder.produceImage.setImageUrl(goodListBean.getThumb(), imageLoader);

        holder.title.setText(goodListBean.getTitle());
        int canyurenshu = Integer.parseInt(goodListBean.getCanyurenshu());
        int zongrenshu = Integer.parseInt(goodListBean.getZongrenshu());

        int totalProgress = 0;
        if (canyurenshu != 0) {
            totalProgress = (int) (canyurenshu / (double) zongrenshu * 100);

            if (totalProgress == 0) {
                totalProgress = 1;
            }
        }

        String str = resource.getString(R.string.buy_progress, totalProgress) + "%";
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#568CCE")), 5, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.progressTextView.setText(sp);
        holder.addList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (addListListener != null) {
                    int[] location = new int[2];
                    holder.produceImage.getLocationInWindow(location);//获取点击商品图片的位置
                    Drawable drawable = holder.produceImage.getDrawable();//复制一个新的商品图标
                    addListListener.addToList(goodListBeans.get(position), drawable, location);
                }

            }
        });

        holder.progress.setMax(zongrenshu);
        holder.progress.setProgress(canyurenshu);
        return convertView;
    }

    public interface AddListListener {
        public void addToList(GoodListBean goodListBean, Drawable drawable, int[] location);
    }

}
