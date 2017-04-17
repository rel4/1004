package com.hongbaogou.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.RadioButton;

/**
 * Created by lenovo on 2015/12/3.
 */
public class RechargeChooseBankDialog {
    private static String[] mListbank = new String[]{"建设银行", "中国银行", "招商银行", "农业银行", "银联支付", "交通银行"};

    public static void ShowDialog(Context context, final RadioButton radioButton) {

        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("选择银行：");
        mDialog.setItems(mListbank, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                radioButton.setText(mListbank[which]);
            }
        });
        mDialog.show();
    }

}
