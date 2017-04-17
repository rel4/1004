package com.hongbaogou.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.BaseUtils;
import com.hongbaogou.utils.ImageUtils;
import com.hongbaogou.utils.MD5;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 发布晒单的界面
 * <p/>
 * 上傳圖片大小 540*450
 */
public class IssueShareActivity extends BaseAppCompatActivity implements View.OnClickListener {


    /**
     * 获取传入的数据
     */
    private String uid;
    private String qishu;
    private String shopid;
    private String shopsid;
    private String titles;
    private String goodstitle;
    private String content;
    private String endtime;
    private String lucknumber;
    private String gonumber;

    private ImageView img_1;
    private ImageView img_2;
    private ImageView img_3;
    private ImageView img_4;
    private ImageView img_5;
    private ImageView img_6;
    private ImageView img_7;
    private ImageView img_8;

    private EditText ed_zhuti;
    private EditText ed_ganyan;

    private ImageView btnimg;
    private TextView title;

    /**
     * 用来记录当前照片的name
     */
    private String nowFileName = "";

    /**
     * 记录上一次选择的图片路径
     */
    private String photoPath = "";

    private LinearLayout layout5_8;

    /**
     * 选择相册的标记
     */
    private int columnIndex = -1;

    /**
     * 晒单上传图片的地址
     */
    private String uploadingUrl = "http://v2.qcread.com/index.php/yungouapi/member/do_shaidan";//上传的地址
    /**
     * 全部照片的名字
     */
    private String shareImg_1 = "SHARE_IMG1.jpg";
    private String shareImg_2 = "SHARE_IMG2.jpg";
    private String shareImg_3 = "SHARE_IMG3.jpg";
    private String shareImg_4 = "SHARE_IMG4.jpg";
    private String shareImg_5 = "SHARE_IMG5.jpg";
    private String shareImg_6 = "SHARE_IMG6.jpg";
    private String shareImg_7 = "SHARE_IMG7.jpg";
    private String shareImg_8 = "SHARE_IMG8.jpg";

    /**
     * 上传图片是弹出的dialog
     */
    private Dialog dialog;

    /**
     * 发布晒单的按钮
     */
    private TextView issueShare;

    //商品名称
    private TextView goods_tv;
    //参与人次
    private TextView number_people;
    //幸运号码啊
    private TextView luck_number;
    //揭晓时间
    private TextView end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_share);
        getData();
        findAllView();
        initBarUtils.setSystemBar(this);
    }

    private void getData() {
        gonumber = getIntent().getStringExtra("gonumber");
        uid = getIntent().getStringExtra("uid");
        qishu = getIntent().getStringExtra("qishu");
        shopid = getIntent().getStringExtra("shopid");
        shopsid = getIntent().getStringExtra("shopsid");
        titles = getIntent().getStringExtra("title");
        goodstitle = getIntent().getStringExtra("goodstitle");
        content = getIntent().getStringExtra("content");
        lucknumber = getIntent().getStringExtra("lucknumber");
        endtime = getIntent().getStringExtra("endtime");

        Log.e("TAG", "-=-=-=- " + endtime);
    }

    private void findAllView() {

        layout5_8 = (LinearLayout) findViewById(R.id.img5_8);

        issueShare = (TextView) findViewById(R.id.issue_share);
        issueShare.setOnClickListener(this);

        //设置商品名称的title
        goods_tv = (TextView) findViewById(R.id.goods_tv);
        goods_tv.setText(goodstitle);


        //设置参与人次
        number_people = (TextView) findViewById(R.id.number_people);
        String s = "参与人次:" + gonumber;
        SpannableStringBuilder ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_tv_gray)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        number_people.setTextColor(getResources().getColor(R.color.color_red));
        number_people.setText(ssb);


        //设置幸运号码啊
        luck_number = (TextView) findViewById(R.id.lucknumber);
        s = "幸运号码:" + lucknumber;
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_tv_gray)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        luck_number.setTextColor(getResources().getColor(R.color.color_blue));
        luck_number.setText(ssb);

        //设置结束时间
        end_time = (TextView) findViewById(R.id.end_time);
        s = "揭晓时间:" + endtime;
        Log.e("TAG", "-=-s=-=- " + s);
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_tv_gray)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        Log.e("TAG", "-=-=ssb-=- " + ssb);
        end_time.setText(ssb);

        ed_zhuti = (EditText) findViewById(R.id.editText_zhuti);
        ed_ganyan = (EditText) findViewById(R.id.editText_ganyan);

        ed_zhuti.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ed_ganyan.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        ed_zhuti.setSingleLine(false);
        ed_ganyan.setSingleLine(false);

        ed_zhuti.setHorizontallyScrolling(false);
        ed_ganyan.setHorizontallyScrolling(false);

        btnimg = (ImageView) findViewById(R.id.btn_back);
        btnimg.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.issueshare);

        img_1 = (ImageView) findViewById(R.id.img_1);
        img_1.setOnClickListener(this);

        img_2 = (ImageView) findViewById(R.id.img_2);
        img_2.setOnClickListener(this);

        img_3 = (ImageView) findViewById(R.id.img_3);
        img_3.setOnClickListener(this);

        img_4 = (ImageView) findViewById(R.id.img_4);
        img_4.setOnClickListener(this);

        img_5 = (ImageView) findViewById(R.id.img_5);
        img_5.setOnClickListener(this);

        img_6 = (ImageView) findViewById(R.id.img_6);
        img_6.setOnClickListener(this);

        img_7 = (ImageView) findViewById(R.id.img_7);
        img_7.setOnClickListener(this);

        img_8 = (ImageView) findViewById(R.id.img_8);
        img_8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //回退按钮
            case R.id.btn_back:
                onBackPressed();
                break;
            //发布晒单的按钮
            case R.id.issue_share:
                Log.e("TAG", " --大小-- >" + maps.size());
                if (ed_zhuti.getText().toString().length() < 6) {
                    ToastUtil.showToast(this, "主题信息不能少于6个字");
                    return;
                }
                if (ed_ganyan.getText().toString().length() < 10) {
                    ToastUtil.showToast(this, "获奖感言,不能少于10个字");
                    return;
                }
                //發佈晒單事件
                if (maps.size() < 1) {
                    ToastUtil.showToast(this, "请至少上传1张图片");
                    return;
                }
                //上传图片
                issueUploading();
                ToastUtil.showToast(this, "晒单成功");
                finish();
                break;
            case R.id.img_1:
                columnIndex = 0;
                ShowDialog(1);
                break;
            case R.id.img_2:
                columnIndex = 1;
                ShowDialog(2);
                break;
            case R.id.img_3:
                columnIndex = 2;
                ShowDialog(3);
                break;
            case R.id.img_4:
                columnIndex = 3;
                ShowDialog(4);
                break;
            case R.id.img_5:
                columnIndex = 4;
                ShowDialog(5);
                break;
            case R.id.img_6:
                columnIndex = 5;
                ShowDialog(6);
                break;
            case R.id.img_7:
                columnIndex = 6;
                ShowDialog(7);
                break;
            case R.id.img_8:
                columnIndex = 7;
                ShowDialog(8);
                break;
            default:
                break;
        }
    }

    /**
     * 发布晒单的事件
     */
    private void issueUploading() {
        //map转list
        List list = new ArrayList();
        //获得map的iterator
        Iterator iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            list.add(entry.getValue());
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("partner", "ZL888ANDROID");
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("sign", MD5.getStringMD5("ZL888ANDROID" + String.valueOf(System.currentTimeMillis() / 1000) + "3860DD4B6E04448D3666B3F94CFA3DD7"));
        params.put("uid", uid);
        params.put("shopid", shopid);
        params.put("shopsid", shopsid);
        params.put("title", ed_zhuti.getText().toString());
        params.put("content", ed_ganyan.getText().toString());
        params.put("qishu", qishu);
        params.put("num", maps.size() + "");
        Log.e("TAG", "---uid = " + uid + "---shopid = " + shopid + "---shopsid = " + shopsid + "---title = " + ed_zhuti.getText().toString() + "---content = " + ed_ganyan.getText().toString()
                + "---qishu = " + qishu);

        for (int i = 0; i < maps.size(); i++) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = (Bitmap) list.get(i);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            String data = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
            params.put("img" + (i + 1), data);
            Log.e("TAG", "上传的第 " + (i + 1) + " 张图片~" + data.toString().substring(0, 20));
        }
        client.post(this, uploadingUrl, params, new AsyncHttpResponseHandler() {
        });

//        //图片上传的接口回调
//        IssueShareRequest issueShareRequest = new IssueShareRequest();
//        issueShareRequest.requestIssueShare(this);
//        LoadingDialog.hideLoadingDialog(dialog);
        maps.clear();
    }

    private static String[] mListbank = new String[]{"拍照", "相册"};
    private static AlertDialog.Builder mBuilder;
    private static AlertDialog mAlertDialog;

    public void ShowDialog(final int nums) {

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("上传图片：");
        mBuilder.setItems(mListbank, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        opeanCamera(nums);
                        ToastUtil.showToast(IssueShareActivity.this, "准备拍照");
                        break;
                    case 1:
                        opeanPhotoalbum();
                        break;
                    default:
                        break;
                }
                if (nums == 4) {
                    layout5_8.setVisibility(View.VISIBLE);
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

    /**
     * 开启照相
     *
     * @param nums
     */
    private void opeanCamera(int nums) {
        Uri imageUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (nums) {
            case 1:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_1));
                nowFileName = shareImg_1;
                break;
            case 2:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_2));
                nowFileName = shareImg_2;
                break;
            case 3:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_3));
                nowFileName = shareImg_3;
                break;
            case 4:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_4));
                nowFileName = shareImg_4;
                break;
            case 5:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_5));
                nowFileName = shareImg_5;
                break;
            case 6:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_6));
                nowFileName = shareImg_6;
                break;
            case 7:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_7));
                nowFileName = shareImg_7;
                break;
            case 8:
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), shareImg_8));
                nowFileName = shareImg_8;
                break;
            default:
                break;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //回调
        startActivityForResult(intent, Camera_Ok);
    }

    /**
     * 照相完成的回调码
     */
    public static final int Camera_Ok = 10;
    /**
     * 选择图片完成的回调码
     */
    public static final int Photo_Ok = 20;

    /**
     * 打开系统相册
     */
    private void opeanPhotoalbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Photo_Ok);
    }

    private Bitmap bitMap;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {//result is not correct
            return;
        } else {
            if (requestCode == Camera_Ok) {
                //将保存在本地的图片取出并缩小后显示在界面上
                Bitmap camorabitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + nowFileName);
                if (null != camorabitmap) {
                    // 下面这两句是对图片按照一定的比例缩放来显示
                    int scale = ImageUtils.reckonThumbnail(camorabitmap.getWidth(), camorabitmap.getHeight(), 540, 450);
                    bitMap = ImageUtils.PicZoom(camorabitmap, camorabitmap.getWidth() / scale, camorabitmap.getHeight() / scale);
                    //释放内存
                    camorabitmap.recycle();
                    //将处理过的图片显示在界面上，并保存到本地
                    setImgFile();
                }
            }
            //如果返回码是从相册过来的
            if (requestCode == Photo_Ok) {
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    //这里一次只能选择一张照片,所以取0角标
                    int index = cursor.getColumnIndex(filePathColumn[0]);
                    //记录本次的图片路径
                    String picturePath = cursor.getString(index);  //获取照片路径
                    if (photoPath.equals(picturePath)) {
                        ToastUtil.showToast(this, "请不要上传重复的图片哦~");
                        return;
                    }
                    photoPath = picturePath;
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    int scale = ImageUtils.reckonThumbnail(bitmap.getWidth(), bitmap.getHeight(), 540, 450);
                    bitmap = ImageUtils.PicZoom(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);

                    setImgFile(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 存放长传bitmap的集合
     */
    private static Map<String, Bitmap> maps = new HashMap<String, Bitmap>();

    /**
     * 设置压缩后的图片
     *
     * @param bitMap
     */
    private void setImgFile(Bitmap bitMap) {
        if (columnIndex == 0) {
            img_1.setImageBitmap(bitMap);
            maps.put("photo0", bitMap);
            img_2.setClickable(true);
            img_2.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 1) {
            img_2.setImageBitmap(bitMap);
            maps.put("photo1", bitMap);
            img_3.setClickable(true);
            img_3.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 2) {
            img_3.setImageBitmap(bitMap);
            maps.put("photo2", bitMap);
            img_4.setClickable(true);
            img_4.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 3) {
            img_4.setImageBitmap(bitMap);
            maps.put("photo3", bitMap);
            img_5.setClickable(true);
            img_5.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 4) {
            img_5.setImageBitmap(bitMap);
            maps.put("photo4", bitMap);
            img_6.setClickable(true);
            img_6.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 5) {
            img_6.setImageBitmap(bitMap);
            maps.put("photo5", bitMap);
            img_7.setClickable(true);
            img_7.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 6) {
            img_7.setImageBitmap(bitMap);
            maps.put("photo6", bitMap);
            img_8.setClickable(true);
            img_8.setVisibility(View.VISIBLE);
        }
        if (columnIndex == 7) {
            img_8.setImageBitmap(bitMap);
            maps.put("photo7", bitMap);
            ToastUtil.showToast(this, "最多只可上传8张图片哦~");
        }
    }

    /**
     * 返回键的链接事件
     */
    @Override
    public void onBackPressed() {
        if (ed_zhuti.getText().toString().equals("") || ed_ganyan.getText().toString().equals("") || nowFileName.equals("")) {
            showExitDialog();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 确认要退出晒单吗?的dialog
     */
    private void showExitDialog() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(
                "确定要退出晒单吗？").setPositiveButton("继续晒单",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("确认退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseUtils.colseSoftKeyboard(IssueShareActivity.this);
                finish();
            }
        }).create();
        dialog.show();
    }

    /**
     * 压缩图片的方法
     *
     * @param image
     * @param size
     * @return
     */
    private Bitmap compressImage(Bitmap image, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap;
    }

    private void setImgFile() {

        if (nowFileName.equals(shareImg_1)) {
            maps.put("photo0", bitMap);
            img_1.setImageBitmap(bitMap);
            img_2.setClickable(true);
            img_2.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_2)) {
            img_2.setImageBitmap(bitMap);
            maps.put("photo1", bitMap);
            img_3.setClickable(true);
            img_3.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_3)) {
            img_3.setImageBitmap(bitMap);
            maps.put("photo2", bitMap);
            img_4.setClickable(true);
            img_4.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_4)) {
            img_4.setImageBitmap(bitMap);
            maps.put("photo3", bitMap);
            img_5.setClickable(true);
            img_5.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_5)) {
            img_5.setImageBitmap(bitMap);
            maps.put("photo4", bitMap);
            img_6.setClickable(true);
            img_6.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_6)) {
            img_6.setImageBitmap(bitMap);
            maps.put("photo5", bitMap);
            img_7.setClickable(true);
            img_7.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_7)) {
            img_7.setImageBitmap(bitMap);
            maps.put("photo6", bitMap);
            img_8.setClickable(true);
            img_8.setVisibility(View.VISIBLE);
        }
        if (nowFileName.equals(shareImg_8)) {
            maps.put("photo7", bitMap);
            img_8.setImageBitmap(bitMap);
            ToastUtil.showToast(this, "最多只可上传8张图片哦~");
        }
    }

//    /**
//     * 晒单图片上传成功的回调
//     *
//     * @param baseObjectBean
//     */
//    @Override
//    public void requestIssueShareSuccess(BaseObjectBean baseObjectBean) {
//        if (baseObjectBean.getStatus() == 1) {//头像上传成功
//            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
//            LoadingDialog.hideLoadingDialog(dialog);//上传成功 隐藏dialog
//            Toast.makeText(this, "晒单成功", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 晒得那图片上传失败的回调
//     *
//     * @param error
//     */
//    @Override
//    public void requestIssueShareFailed(VolleyError error) {
//        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
//    }
}
