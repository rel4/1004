package com.hongbaogou.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.bean.WinRecordsInfoStatusBean;
import com.hongbaogou.utils.ToastUtil;

import java.util.List;

/**
 * Created by lenovo on 2015/12/6.
 */
public class WinRecordSignInAdapter extends BaseAdapter {
    private Context context;
    private  List<WinRecordsInfoStatusBean> list;
    private  AlertDialog mDialog;

    public void addData(List<WinRecordsInfoStatusBean> datalist) {
        list.addAll(datalist);
        notifyDataSetChanged();
    }

    public WinRecordSignInAdapter(List<WinRecordsInfoStatusBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_winrecordsigninadapter,null);
            viewHolder.iv_getprize = (ImageView) convertView.findViewById(R.id.iv_getprize);
            viewHolder.tv_getprize = (TextView) convertView.findViewById(R.id.tv_getprize);
            viewHolder.tv_getprize_time = (TextView) convertView.findViewById(R.id.tv_getprize_time);
            viewHolder.btn_getprize_time = (Button) convertView.findViewById(R.id.btn_getprize_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WinRecordsInfoStatusBean bean =list.get(position);

        if ("1".equals(bean.getStatus())){
            viewHolder.tv_getprize.setText(bean.getInfo());
            viewHolder.tv_getprize_time.setText(bean.getTime());
            if ("确认收货".equals(bean.getInfo())){
                viewHolder.btn_getprize_time.setVisibility(View.VISIBLE);
                viewHolder.btn_getprize_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mdialog = new AlertDialog.Builder(context);
                        mdialog.setMessage("确定收货吗?");
                        mdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.showToast(context,"收货了");
                            }
                        });
                        mdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.dismiss();
                            }
                        });
                        mDialog = mdialog.create();
                        mDialog.show();
                    }
                });
                viewHolder.iv_getprize .setBackground(context.getResources().getDrawable(R.mipmap.winrecord_ensure));
            }else {
                viewHolder.iv_getprize .setBackground(context.getResources().getDrawable(R.mipmap.winrecord_noensure));
            }
        }else {
            viewHolder.tv_getprize.setVisibility(View.GONE);
            viewHolder.tv_getprize_time.setVisibility(View.GONE);
        }
        return convertView;
    }
    static class ViewHolder {
        private ImageView iv_getprize;
        private TextView tv_getprize,tv_getprize_time;
        private Button btn_getprize_time;
    }
}
