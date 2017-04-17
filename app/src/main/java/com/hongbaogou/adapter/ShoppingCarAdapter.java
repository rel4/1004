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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.ShoppingCartBean;
import com.hongbaogou.utils.RequestManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 清单列表的对象
 */
public class ShoppingCarAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private Resources resource;
    private boolean isEditable = false;
    private List<ShoppingCartBean> shoppingCartBeans = new ArrayList<ShoppingCartBean>();
    private OnStateChangelistener onStateChangelistener;

    public void setOnStateChangelistener(OnStateChangelistener onStateChangelistener) {
        this.onStateChangelistener = onStateChangelistener;
    }


    public ShoppingCarAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        imageLoader = RequestManager.getImageLoader();
        resource = context.getResources();
    }


    public String getDeleteIds() {
        String ids = "";
        for (int i = 0; i < shoppingCartBeans.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeans.get(i);
            if (shoppingCartBean.isSelected()) {
                ids = ids + shoppingCartBean.getId() + ",";
            }
        }
        if(!"".equalsIgnoreCase(ids)){
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
    }

    public String getGoods() {
        String str = "";
        for (ShoppingCartBean shoppingCartBean : shoppingCartBeans) {
            str = str + shoppingCartBean.getId() + "," + shoppingCartBean.getGonumber() + "|";
        }
        return str.substring(0, str.length() - 1);
    }

    public ShoppingCartBean getItemBean(int position) {
        return shoppingCartBeans.get(position);
    }

    public void updateList() {
        Iterator<ShoppingCartBean> item = shoppingCartBeans.iterator();
        while (item.hasNext()) {
            ShoppingCartBean shoppingCartBean = item.next();
            if (shoppingCartBean.isSelected()) {
                item.remove();
            }
        }
        notifyDataSetChanged();
    }

    public int getTotalPrice() {
        int price = 0;
        for (ShoppingCartBean shoppingCartBean : shoppingCartBeans) {
            price = price + shoppingCartBean.getGonumber();
        }
        return price;
    }

    public void selectAction(boolean selectState) {
        for (ShoppingCartBean shoppingCartBean : shoppingCartBeans) {
            shoppingCartBean.setIsSelected(selectState);
        }
        notifyDataSetChanged();
    }

    public void addData(List<ShoppingCartBean> data) {
        shoppingCartBeans.addAll(data);
        notifyDataSetChanged();
    }

    public void reloadData(List<ShoppingCartBean> data) {
        shoppingCartBeans.clear();
        shoppingCartBeans.addAll(data);
        notifyDataSetChanged();
    }

    public void setEditState(boolean isEditable) {
        this.isEditable = isEditable;
        notifyDataSetChanged();
    }

    public void clearData() {
        shoppingCartBeans.clear();
        notifyDataSetChanged();
    }


    public int getCount() {
        return shoppingCartBeans.size();
    }

    public Object getItem(int position) {
        return shoppingCartBeans.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ShoppingCartViewHold shoppingCartViewHold;
        if (convertView == null) {
            shoppingCartViewHold = new ShoppingCartViewHold();
            convertView = inflater.inflate(R.layout.shoppingcart_item, null);

            shoppingCartViewHold.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            shoppingCartViewHold.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            shoppingCartViewHold.tenImage = (ImageView) convertView.findViewById(R.id.tenImage);
            shoppingCartViewHold.title = (TextView) convertView.findViewById(R.id.sd_title);
            shoppingCartViewHold.progress = (TextView) convertView.findViewById(R.id.progress);

            shoppingCartViewHold.buy = (TextView) convertView.findViewById(R.id.buy);
            shoppingCartViewHold.minus = (ImageView) convertView.findViewById(R.id.minus);
            shoppingCartViewHold.add = (ImageView) convertView.findViewById(R.id.add);
            convertView.setTag(shoppingCartViewHold);
        } else {
            shoppingCartViewHold = (ShoppingCartViewHold) convertView.getTag();
        }

        ShoppingCartBean shoppingCartBean = shoppingCartBeans.get(position);
        if (shoppingCartBean.getIs_ten() == 1) {
            shoppingCartViewHold.tenImage.setVisibility(View.VISIBLE);
        } else {
            shoppingCartViewHold.tenImage.setVisibility(View.GONE);
        }

        shoppingCartViewHold.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        shoppingCartViewHold.produceImage.setErrorImageResId(R.mipmap.img_blank);
        shoppingCartViewHold.produceImage.setImageUrl(shoppingCartBean.getThumb(), imageLoader);

        if (isEditable) {
            shoppingCartViewHold.checkBox.setVisibility(View.VISIBLE);
        } else {
            shoppingCartViewHold.checkBox.setVisibility(View.GONE);
        }

        boolean isSelected = shoppingCartBean.isSelected();
        shoppingCartViewHold.checkBox.setChecked(isSelected);

        shoppingCartViewHold.title.setText(shoppingCartBean.getTitle());

        int canyurenshu = Integer.parseInt(shoppingCartBean.getCanyurenshu());
        String allPeople = shoppingCartBean.getZongrenshu();
        final int remainder = Integer.parseInt(allPeople) - canyurenshu;

        String str = resource.getString(R.string.list_progress, allPeople, remainder);
        SpannableStringBuilder progressBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan joinCountColor = new ForegroundColorSpan(resource.getColor(R.color.win_username));
        progressBuilder.setSpan(joinCountColor, str.length() - 2 - String.valueOf(remainder).length(), str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        shoppingCartViewHold.progress.setText(progressBuilder);
        shoppingCartViewHold.buy.setText(String.valueOf(shoppingCartBean.getGonumber()));

        shoppingCartViewHold.buy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onStateChangelistener != null){
                    if(!isEditable)
                    onStateChangelistener.updateBuyCount((TextView)v,position);
                }
            }
        });

        shoppingCartViewHold.minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!isEditable){
                    ShoppingCartBean shoppingCartBean = shoppingCartBeans.get(position);
                    int buyNumber = shoppingCartBean.getGonumber();
                    if (shoppingCartBean.getIs_ten() == 0) {
                        if (buyNumber > 1) {
                            buyNumber = buyNumber - 1;
                            shoppingCartBean.setGonumber(buyNumber);
                        }
                    } else {
                        if (buyNumber > 10) {
                            buyNumber = buyNumber - 10;
                            shoppingCartBean.setGonumber(buyNumber);
                        }
                    }
                    shoppingCartViewHold.buy.setText(String.valueOf(buyNumber));
                    if (onStateChangelistener != null) {
                        onStateChangelistener.priceChange(shoppingCartBean.getId(),shoppingCartBean.getShopid(),buyNumber);
                    }
                }

            }
        });

        shoppingCartViewHold.add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isEditable){
                    ShoppingCartBean shoppingCartBean = shoppingCartBeans.get(position);
                    int buyNumber = shoppingCartBean.getGonumber();
                    if (shoppingCartBean.getIs_ten() == 0) {
                        if (buyNumber < remainder) {
                            buyNumber = buyNumber + 1;
                            shoppingCartBean.setGonumber(buyNumber);
                        }
                    } else {
                        if (buyNumber < remainder) {
                            buyNumber = buyNumber + 10;
                            shoppingCartBean.setGonumber(buyNumber);
                        }
                    }
                    shoppingCartViewHold.buy.setText(String.valueOf(buyNumber));
                    if (onStateChangelistener != null) {
                        onStateChangelistener.priceChange(shoppingCartBean.getId(),shoppingCartBean.getShopid(),buyNumber);
                    }
                }

            }
        });

        return convertView;
    }

    public final class ShoppingCartViewHold {
        public CheckBox checkBox;
        public NetworkImageView produceImage;
        public ImageView tenImage;
        public TextView title;
        public TextView progress;
        private ImageView minus;
        private TextView buy;
        private ImageView add;
    }

    public interface OnStateChangelistener {

        public void priceChange(String id ,String shopid , int num);

        public void updateBuyCount(TextView buyCountView ,int position);
    }
}
