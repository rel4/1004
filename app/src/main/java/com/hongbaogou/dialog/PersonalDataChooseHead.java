package com.hongbaogou.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by lenovo on 2015/12/5.
 */
public class PersonalDataChooseHead {
    private static String[] mListbank = new String[]{ "拍照","相册"};
    private static  AlertDialog.Builder mBuilder;
    private static AlertDialog mAlertDialog;

    public static void ShowDialog(final Context context) {
        mBuilder= new AlertDialog.Builder(context);
        mBuilder.setTitle("修改头像：");
        mBuilder.setItems(mListbank, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            opeanCamera(context);
                            Toast.makeText(context,"准备拍照",Toast.LENGTH_SHORT).show();
                            break;
                        //Todo 拍照上传

                        case 1:
                            opeanPhotoalbum(context);

                            break;
                            default:
                                break;

                    }
            }


        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }
    private static void opeanCamera (Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        context.startActivity(intent);

    } private static void opeanPhotoalbum(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        context.startActivity(intent);

    }
}
