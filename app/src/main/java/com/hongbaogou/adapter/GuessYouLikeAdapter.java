package com.hongbaogou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.GuessYouLikeBean;
import com.hongbaogou.utils.RequestManager;

import java.util.List;

/**
 * Created by lenovo on 2015/12/14.
 */
public class GuessYouLikeAdapter extends RecyclerView.Adapter<GuessYouLikeAdapter.MyViewHolder> {
    private List<GuessYouLikeBean> mData;
    private Context mContext;
    private ImageLoader imageLoader;
    private GuessYouLikeBean bean;
    private MyViewHolder viewHolder;
    public void addData(List<GuessYouLikeBean> datalist) {
        mData.addAll(datalist);
        notifyDataSetChanged();
    }

    public GuessYouLikeAdapter(Context mContext, List<GuessYouLikeBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        imageLoader = RequestManager.getImageLoader();
    }

    @Override
    public GuessYouLikeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.layout_guessyoulike_adapter, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(GuessYouLikeAdapter.MyViewHolder holder, int position) {
        bean = mData.get(position);
        viewHolder.progress_guess.setMax(100);
        viewHolder.progress_guess.setProgress((int) (bean.getBilv() * 100));
        viewHolder.tv_guess_title.setText(bean.getTitle());

        viewHolder.imageView_guess_thumb.setImageResource(R.mipmap.img_blank);
        viewHolder.imageView_guess_thumb.setDefaultImageResId(R.mipmap.img_blank);
        viewHolder.imageView_guess_thumb.setErrorImageResId(R.mipmap.img_blank);
        viewHolder.imageView_guess_thumb.setImageUrl(bean.getThumb(), imageLoader);

        if ("1".equals(bean.getIs_ten())) {
            holder.imageView_guess_ten.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public   class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
            imageView_guess_ten = (ImageView) view.findViewById(R.id.imageView_guess_ten);
            imageView_guess_thumb = (NetworkImageView) view.findViewById(R.id.imageView_guess_thumb);
            tv_guess_title = (TextView) view.findViewById(R.id.tv_guess_title);
            progress_guess = (ProgressBar) view.findViewById(R.id.progress_guess);
        }
        public NetworkImageView imageView_guess_thumb;
        public   ImageView imageView_guess_ten;
        public  TextView tv_guess_title;
        public ProgressBar progress_guess;

    }



}
