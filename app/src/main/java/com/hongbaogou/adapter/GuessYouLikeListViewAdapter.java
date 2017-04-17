package com.hongbaogou.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.hongbaogou.bean.GuessYouLikeBean;
import com.hongbaogou.utils.RequestManager;

import java.util.List;

/**
 * Created by lenovo on 2015/12/21.
 */
public class GuessYouLikeListViewAdapter extends BaseAdapter {
    private List<GuessYouLikeBean> mData;
    private Context mContext;
    private ImageLoader imageLoader;
    private GuessYouLikeBean bean;
    public void addData(List<GuessYouLikeBean> datalist) {

        mData.addAll(datalist);

        notifyDataSetChanged();
    }
    public GuessYouLikeListViewAdapter(Context mContext, List<GuessYouLikeBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        imageLoader = RequestManager.getImageLoader();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder ;
        if (convertView == null){
            viewHolder =new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_guessyoulike_adapter,null);

            viewHolder.imageView_guess_ten = (ImageView) convertView.findViewById(R.id.imageView_guess_ten);
            viewHolder.imageView_guess_thumb = (NetworkImageView) convertView.findViewById(R.id.imageView_guess_thumb);
            viewHolder.tv_guess_title = (TextView) convertView.findViewById(R.id.tv_guess_title);
            viewHolder.progress_guess = (ProgressBar) convertView.findViewById(R.id.progress_guess);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bean = mData.get(position);
        viewHolder.progress_guess.setMax(100);
        if(bean.getBilv()!=null){
            viewHolder.progress_guess.setProgress((int) (bean.getBilv() * 100));
        }
        viewHolder.tv_guess_title.setText(bean.getTitle());

        viewHolder.imageView_guess_thumb.setDefaultImageResId(R.mipmap.img_blank);
        viewHolder.imageView_guess_thumb.setErrorImageResId(R.mipmap.img_blank);

        String thumb = bean.getThumb();
        if(!TextUtils.isEmpty(thumb)){
            viewHolder.imageView_guess_thumb.setImageUrl(bean.getThumb(), imageLoader);
        }
        if ("1".equals(bean.getIs_ten())) {
            viewHolder.imageView_guess_ten.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class  ViewHolder{
        private NetworkImageView imageView_guess_thumb;
        private ImageView imageView_guess_ten;
        private TextView tv_guess_title;
        private ProgressBar progress_guess;
    }

}
