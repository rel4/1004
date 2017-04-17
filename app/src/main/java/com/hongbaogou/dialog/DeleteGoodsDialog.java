package com.hongbaogou.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Created by Administrator on 2015/12/26.
 */
public class DeleteGoodsDialog extends DialogFragment {

    private Button sure;
    private Button cancel;
    private onSureDeleteListener onSureDeleteListener;

    public void setOnSureDeleteListener(DeleteGoodsDialog.onSureDeleteListener onSureDeleteListener) {
        this.onSureDeleteListener = onSureDeleteListener;
    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = inflater.inflate(R.layout.deletegoodsdialog, container);
//        cancel = (Button) view.findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        sure = (Button) view.findViewById(R.id.sure);
//        sure.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(onSureDeleteListener != null){
//                    onSureDeleteListener.onSureDelete();
//                }
//                dismiss();
//            }
//        });
//        return view;
//    }


    /**
     * 覆写Fragment类的onCreateDialog方法，在DialogFragment的show方法执行之后， 系统会调用这个回调方法。
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 创建dialog并设置button的点击事件
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("你确认要删除商品吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(onSureDeleteListener != null){
                    onSureDeleteListener.onSureDelete();
                }
                dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                dismiss();
            }
        });
        return builder.create();
    }

    public interface onSureDeleteListener{
      public void onSureDelete();
    }
}
