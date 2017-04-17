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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.GoodsCategoryBean;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class GoodsAdapter extends BaseAdapter {

    private List<GoodsCategoryBean> goodsCategoryBeans = new ArrayList<GoodsCategoryBean>();
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private Resources resource;
    private AddShoppingCartListener addShoppingCartListener;

    /**
     * 获取对象
     *
     * @param position 位置
     * @return
     */
    public GoodsCategoryBean getObject(int position) {
        return goodsCategoryBeans.get(position);
    }

    public void destory() {
        if (goodsCategoryBeans != null) {
            goodsCategoryBeans.clear();
            goodsCategoryBeans = null;
        }
        inflater = null;
        imageLoader = null;
        resource = null;
        addShoppingCartListener = null;
    }

    public void setAddShoppingCartListener(AddShoppingCartListener addShoppingCartListener) {
        this.addShoppingCartListener = addShoppingCartListener;
    }

    public GoodsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();
        resource = context.getResources();
    }

    public GoodsCategoryBean getData(int position) {
        return goodsCategoryBeans.get(position);
    }

    public void addData(List<GoodsCategoryBean> loadData) {
        goodsCategoryBeans.addAll(loadData);
        notifyDataSetChanged();
        loadData = null;
    }


    /**
     * 清空搜索
     */
    public void cleanData() {
        goodsCategoryBeans.clear();
        notifyDataSetChanged();
    }

    public void reLoadData(List<GoodsCategoryBean> loadData) {
        goodsCategoryBeans.clear();
        addData(loadData);
        loadData = null;
    }


    public int getCount() {
        return goodsCategoryBeans.size();
    }

    public Object getItem(int position) {
        return goodsCategoryBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        GoodsViewHold goodsViewHold = null;
        if (convertView == null) {
            goodsViewHold = new GoodsViewHold();
            convertView = inflater.inflate(R.layout.goodsitem, null);
            goodsViewHold.tenImage = (ImageView) convertView.findViewById(R.id.tenImage);
            goodsViewHold.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            goodsViewHold.title = (TextView) convertView.findViewById(R.id.title);
            goodsViewHold.total = (TextView) convertView.findViewById(R.id.total);
            goodsViewHold.remainder = (TextView) convertView.findViewById(R.id.remainder);
            goodsViewHold.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
            goodsViewHold.addList = (TextView) convertView.findViewById(R.id.addList);
            convertView.setTag(goodsViewHold);
        } else {
            goodsViewHold = (GoodsViewHold) convertView.getTag();
        }

        GoodsCategoryBean goodsBean = goodsCategoryBeans.get(position);
        if (goodsBean.getIs_ten() == 1) {
            goodsViewHold.tenImage.setVisibility(View.VISIBLE);
        } else {
            goodsViewHold.tenImage.setVisibility(View.GONE);
        }

        goodsViewHold.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        goodsViewHold.produceImage.setErrorImageResId(R.mipmap.img_blank);
        goodsViewHold.produceImage.setImageUrl(goodsBean.getThumb(), imageLoader);
        goodsViewHold.title.setText(goodsBean.getTitle());

        goodsViewHold.progressBar.setMax(Integer.parseInt(goodsBean.getZongrenshu()));
        goodsViewHold.progressBar.setProgress(Integer.parseInt(goodsBean.getCanyurenshu()));


        int canyurenshu = Integer.parseInt(goodsBean.getCanyurenshu());
        int zongrenshu = Integer.parseInt(goodsBean.getZongrenshu());

        goodsViewHold.total.setText(resource.getString(R.string.total, zongrenshu));

        String str = resource.getString(R.string.residue, zongrenshu - canyurenshu);
        SpannableStringBuilder remainderBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resource.getColor(R.color.win_username));
        remainderBuilder.setSpan(joinCountColor, 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsViewHold.remainder.setText(remainderBuilder);


        goodsViewHold.addList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               if (addShoppingCartListener != null)
                addShoppingCartListener.addShoppingCar(goodsCategoryBeans.get(position));
            }
        });

        remainderBuilder = null;
        joinCountColor = null;
        goodsBean = null;

        return convertView;
    }

    public final class GoodsViewHold {
        public NetworkImageView produceImage;
        public ImageView tenImage;
        public TextView addList;
        public TextView title;
        public ProgressBar progressBar;
        public TextView total;
        public TextView remainder;
    }

    public interface AddShoppingCartListener {
        public void addShoppingCar(GoodsCategoryBean goodsCategoryBean);
    }
}
