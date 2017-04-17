package com.hongbaogou.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hongbaogou.R;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanBestNewList;
import com.hongbaogou.bean.WinInfoBean;
import com.hongbaogou.listener.OnWinInfoListener;
import com.hongbaogou.request.WinInfoRequest;
import com.hongbaogou.utils.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 最新揭晓界面
 */
public class GridViewAdapter extends BaseAdapter implements OnWinInfoListener {


    private GridView gridView;

    private List<BeanBestNewList> list = new ArrayList<BeanBestNewList>();
    private Resources resources;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;

    private Handler handler;
    private Runnable run = new Runnable() {
        public void run() {
            updateTime();
            handler.postDelayed(this, 30l);
        }
    };

    public void updateTime() {
        int count = list.size();
        int firstVisiblePosition = gridView.getFirstVisiblePosition();
        int lastVisiblePosition = gridView.getLastVisiblePosition();
        for (int i = 0; i < count; i++) {
            BeanBestNewList beanBestNewList = list.get(i);
            long endTime = beanBestNewList.getJiexiao_time();
            if (beanBestNewList.getTag() == 1) {
                if (endTime > 0) {
                    endTime = endTime - 30l;
                    beanBestNewList.setJiexiao_time(endTime);
                    if (i >= firstVisiblePosition && i < lastVisiblePosition) {
                        updateView(i - firstVisiblePosition, endTime);
                    }
                } else {
                    beanBestNewList.setTag(2);
                    WinInfoRequest winInfoRequest = new WinInfoRequest(i);
                    winInfoRequest.WinInfoRequest(list.get(i).getId(), this);
                }
                notifyDataSetChanged();
            } else {
                break;
            }
        }
    }

    private void updateView(int childIndex, long endTime) {
        ViewHolder viewHolder = (ViewHolder) gridView.getChildAt(childIndex).getTag();
        if (viewHolder != null) {
            String time = formatter.format(new Date(endTime));
            viewHolder.countDownTime.setText(time.substring(0, time.length() - 1));
        }
    }

    public GridViewAdapter(Context context, GridView gridView) {
        resources = context.getResources();
        imageLoader = RequestManager.getImageLoader();
        inflater = LayoutInflater.from(context);
        formatter = new SimpleDateFormat("mm:ss:SSS");
        handler = new Handler();
        this.gridView = gridView;
        handler.post(run);
    }


    //添加数据到适配器
    public void addData(ArrayList<BeanBestNewList> data) {
        this.list.addAll(data);
        //更新ui
        notifyDataSetChanged();
    }


    /**
     * 清除数据
     */
    public void clearData() {
        list.clear();
    }

    public int getCount() {
        return list.size();
    }

    public BeanBestNewList getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item_yes, parent, false);
            //找到所有的控件
            viewHolder = new ViewHolder();
            viewHolder.img_ten = (ImageView) convertView.findViewById(R.id.img_ten);
            viewHolder.produceImage = (NetworkImageView) convertView.findViewById(R.id.produceImage);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_issue = (TextView) convertView.findViewById(R.id.tv_issue);
            viewHolder.tv_holder = (TextView) convertView.findViewById(R.id.tv_holder);
            viewHolder.tv_peoplen_umber = (TextView) convertView.findViewById(R.id.tv_peoplen_umber);
            viewHolder.tv_lucknumber = (TextView) convertView.findViewById(R.id.tv_lucknumber);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.endIssue = (TextView) convertView.findViewById(R.id.endIssue);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.countDownTime = (TextView) convertView.findViewById(R.id.countDownTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        BeanBestNewList newList = list.get(position);

        //获取商品的价格,根据价格判断10元图标是否隐藏
        int isTen = newList.getIs_ten();
        if (isTen == 1) {
            //如果价格等于1元,就让表示图片显示
            viewHolder.img_ten.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img_ten.setVisibility(View.GONE);
        }

        viewHolder.produceImage.setDefaultImageResId(R.mipmap.img_blank);
        viewHolder.produceImage.setErrorImageResId(R.mipmap.img_blank);
        viewHolder.produceImage.setImageUrl(newList.getThumb(), imageLoader);

        //设置title
        viewHolder.tv_title.setText(newList.getTitle());

        int tag = newList.getTag();
        //1表示未揭晓    0表示已揭晓  2表示正在发送请求 3表示获取结果失败
        if (tag == 0) {
            updateUi(true, viewHolder);
            //设置中奖人
            String newMessageInfo = newList.getUsername();
            //设置期号
            viewHolder.tv_issue.setText(resources.getString(R.string.win_issue, newList.getQishu()));
            //获得者
            SpannableString str = new SpannableString(resources.getString(R.string.win_join_people, newMessageInfo));
            str.setSpan(new ForegroundColorSpan(resources.getColor(R.color.color_blue)), str.length() - newMessageInfo.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.tv_holder.setText(str);
            //设置参与人数
            viewHolder.tv_peoplen_umber.setText(resources.getString(R.string.win_jion, newList.getGonumber()));
            //设置幸运号码
            String luck = newList.getQ_user_code();
            SpannableString luckNumber = new SpannableString(resources.getString(R.string.win_luck_number, luck));
            luckNumber.setSpan(new ForegroundColorSpan(resources.getColor(R.color.color_red)), luckNumber.length() - luck.length(), luckNumber.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.tv_lucknumber.setText(luckNumber);
            //设置揭晓事件
            viewHolder.tv_date.setText(resources.getString(R.string.win_time, newList.getQ_end_time()));
        } else if (tag == 1) {
            updateUi(false, viewHolder);
            //设置期号
            viewHolder.endIssue.setText(resources.getString(R.string.end_issue, newList.getQishu()));
            viewHolder.time.setText(resources.getString(R.string.win_now));
            long endTime = newList.getJiexiao_time();
            String time = formatter.format(new Date(endTime));
            viewHolder.countDownTime.setText(time.substring(0, time.length() - 1));
        } else if (tag == 2) {
            updateUi(false, viewHolder);
            viewHolder.time.setText(resources.getString(R.string.calculate));
            viewHolder.countDownTime.setText(resources.getString(R.string.calculate_in));
        } else if (tag == 3) {
            updateUi(false, viewHolder);
            viewHolder.time.setText(resources.getString(R.string.result_fail));
            viewHolder.countDownTime.setText(resources.getString(R.string.result_fail));
        }

        return convertView;
    }

    private void updateUi(boolean flag, ViewHolder viewHolder) {
        if (flag) {
            viewHolder.tv_holder.setVisibility(View.VISIBLE);
            viewHolder.tv_peoplen_umber.setVisibility(View.VISIBLE);
            viewHolder.tv_lucknumber.setVisibility(View.VISIBLE);
            viewHolder.tv_date.setVisibility(View.VISIBLE);
            viewHolder.tv_issue.setVisibility(View.VISIBLE);
            viewHolder.endIssue.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.countDownTime.setVisibility(View.GONE);
        } else {
            viewHolder.tv_holder.setVisibility(View.GONE);
            viewHolder.tv_peoplen_umber.setVisibility(View.GONE);
            viewHolder.tv_lucknumber.setVisibility(View.GONE);
            viewHolder.tv_date.setVisibility(View.GONE);
            viewHolder.tv_issue.setVisibility(View.GONE);
            viewHolder.endIssue.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.countDownTime.setVisibility(View.VISIBLE);
        }
    }


    private class ViewHolder {
        //10元专区图片
        ImageView img_ten;
        //加载的图片
        NetworkImageView produceImage;
        //标题title
        TextView tv_title;
        //期号
        TextView tv_issue;
        //获奖者
        TextView tv_holder;
        //参与人数
        TextView tv_peoplen_umber;
        //中奖号码
        TextView tv_lucknumber;
        //揭晓的事件
        TextView tv_date;

        TextView endIssue;
        TextView time;
        TextView countDownTime;
    }


    public void onWinInfoSuccess(BaseObjectBean baseObjectBean, int position) {
        BeanBestNewList beanBestNewList = list.get(position);
        if (baseObjectBean.getStatus() == 1) {
            WinInfoBean winInfoBean = (WinInfoBean) baseObjectBean.getData();
            beanBestNewList.setTag(0);
            beanBestNewList.setUsername(winInfoBean.getUsername());
            beanBestNewList.setGonumber(String.valueOf(winInfoBean.getRenci()));
            beanBestNewList.setQ_user_code(winInfoBean.getQ_user_code());
            beanBestNewList.setQ_end_time(winInfoBean.getQ_end_time());
        } else {
            beanBestNewList.setTag(3);
        }
        notifyDataSetChanged();
    }

    public void onWinInfoFailed(VolleyError error) {

    }

}