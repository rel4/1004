package com.hongbaogou.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by L.K.X on 2015/11/20.
 */
public class AlipayUtils {
    private Activity context;

    /**
     *  商户PID
     */
    public static final String PARTNER = "2088521069573110";


    /**
     *  商户收款账号
     */
    public static final String SELLER = "3414410251@qq.com";



    /**
     *  商户私钥，pkcs8格式
     */
//    public static final String RSA_PRIVATE ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALXJ97a4QUA/OOIZBAKRn3aJIg2OVgAfF8GhFGI/kIjZgg58rXtqdA5Tc41qNCvCarsbzEDOeqWkUGAa+OfK+pXqB6RZCynbWGXa1RD6UlxhrRRyp5Qkzv8qnZCpvZjl5MEa2i3pvNIEeyftNWIJz2NKIJDfIN6+kwnSEXq9NHP5AgMBAAECgYEAk5AEQg/C97HaLL39oVAvzvp15+cYLDURBZ4Je875NfzJM5VFBR9eYZqf+7Sv8K16EbP40spIiFie3cRHM3BygwuDVmGppvWivG5R+igYtqqkLNk8+MMncwav6CO+cdvEQMq1tEXuyx/X+UvnVlqEYsaOrgQIb8mk86TS1SBRqnkCQQDk7Lcq4bCFbHs7bQ7C7ZBlyqbQv/T4Jgs//CZs0yyk7qBMbrEytvJFcyHLdBNKO/CH++i6rZzNa4WcLSOgVYYLAkEAy0oYpiZGkfq6aKuUVYEeKQw/sqlo1ZivyNqb5FvM66F6ijChDIL1IOz5TZdfbZxfntfrJOj3Er/puGqDvFWEiwJAXBkXv7q4h5UTJt9DNwzYMPuMjSMM7OBxWjaZRuLWbSxLdTR7OYn8ghBFolJ6iD0BEdQ41xGNqd7+C8zlLlX57QJBAMRIQNB7+HuOqVnTASAl0Sc1CnPJGs3R5Th7yzWMZanFennfRkC8WU53QKdoVt2QtMq8E/DJd3YFEvfa8GNx9s0CQD2+xybUxp0BSX+i4aA1ydVOLvxhmdPJUO60Bk7Ihi1rQJYJIIx1pKAq4sxLHfzb385z0Q941ZH+7cw+blNJiOs=";
    public static final String RSA_PRIVATE ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMhkg+aiid0rLMnX\n" +
            "GNF2KmiixFnP4wA5hQkIxjjLk8qWORFNZI8zWCfUX42toc/J20jtdIjDL8qm8BDw\n" +
            "Wc03b8hlTUdiuuU4950Y2qM1+RxyiL1W+0TzGXGH3KVmJTlTlvK4C4oB9WWZiN6Z\n" +
            "/xJPgc9i1bKMMFAHlCe1nykzWJ1zAgMBAAECgYAJM1w+/Z140x+mUMLp8xP1Y+vB\n" +
            "9+KDQyDRjl6Tgv1RIt6R5czFKmVUxtJwTdWNFcaErapZESUjl9CT5oUo/++Y3epf\n" +
            "nBgU1Nu9WN6xQ5JJaEN9cjeAczqoz7Aspdl/vhtofqc1AcfkFK+uvY4e8XYoGuc5\n" +
            "sW4jVHdUzsMtkbwmYQJBAPxNMA27OC3XLrnkZbJxigom9RwlY4qJHMyDc0ZpCXR8\n" +
            "x76dihxIK61wAGjLyFSt+BG58TyuURvis95vbjSilOMCQQDLVIds2M01Rt5zt7JN\n" +
            "tp81LHZ+patoIQuew24EU9Szh4bwREFSwWbLXLlVSQbNukT6Ny9CwqzwZrsLAA75\n" +
            "dcoxAkEA+xmzLqdbFTxpE7d9g2g+szXsknOfqvwlPmHBM51MPLQHc+2Ey/sEBVxH\n" +
            "/AR8v9JY5wOBVRxEOxWYYFPUgewhcwJAWAJjh+KjmC7vtKjLfcXRTcPMGm99LNo4\n" +
            "3GR851Sd2YONDcyNddOOufGNqvOsBTpeBuwCAeSf1IKy7UT+Nk4c0QJBAN0EVyaf\n" +
            "CF48ATRbqEGnvSzBmQY6vGa++1rGNQ1hXZ/wMuFZVpIwUAorxG3qr7bYo/EB/wS9\n" +
            "Q7v9zIK42exNs/w=";
//    public static final String RSA_PRIVATE ="MIICXQIBAAKBgQC1yfe2uEFAPzjiGQQCkZ92iSINjlYAHxfBoRRiP5CI2YIOfK17anQOU3ONajQrwmq7G8xAznqlpFBgGvjnyvqV6gekWQsp21hl2tUQ+lJcYa0UcqeUJM7/Kp2Qqb2Y5eTBGtot6bzSBHsn7TViCc9jSiCQ3yDevpMJ0hF6vTRz+QIDAQABAoGBAJOQBEIPwvex2iy9/aFQL876defnGCw1EQWeCXvO+TX8yTOVRQUfXmGan/u0r/CtehGz+NLKSIhYnt3ERzNwcoMLg1Zhqab1orxuUfooGLaqpCzZPPjDJ3MGr+gjvnHbxEDKtbRF7ssf1/lL51ZahGLGjq4ECG/JpPOk0tUgUap5AkEA5Oy3KuGwhWx7O20Owu2QZcqm0L/0+CYLP/wmbNMspO6gTG6xMrbyRXMhy3QTSjvwh/vouq2czWuFnC0joFWGCwJBAMtKGKYmRpH6umirlFWBHikMP7KpaNWYr8jam+RbzOuheoowoQyC9SDs+U2XX22cX57X6yTo9xK/6bhqg7xVhIsCQFwZF7+6uIeVEybfQzcM2DD7jI0jDOzgcVo2mUbi1m0sS3U0ezmJ/IIQRaJSeog9ARHUONcRjane/gvM5S5V+e0CQQDESEDQe/h7jqlZ0wEgJdEnNQpzyRrN0eU4e8s1jGWpxXp530ZAvFlOd0CnaFbdkLTKvBPwyXd2BRL32vBjcfbNAkA9vscm1MadAUl/ouGgNcnVTi78YZnTyVDutAZOyIYta0CWCSCMdaSgKuLMSx3829/Oc9EPeNWR/u3MPm5TSYjr";

    /**
     *  支付宝通知的商户的服务端的回调接口
     */
    private String notify_url = "http://v2.qcread.com/index.php/pay/alipay_url/houtai/";



    private static final int SDK_PAY_FLAG = 1;

    private LocalBroadcastManager mLocalBroadcastManager;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败3", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
        };
    };
    private String price;
    private String order_sn;


    /***
     * 此回调作用是向自己的服务器端存储支付成功的订单列表，以防用户赖账供于证据查询
     */

    public AlipayUtils(Activity context){
        this.context = context;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    /**
     * @param price price,单位:元
     * @param order_sn 订单号
     */
    public void pay(String price,String order_sn){
        this.price = price;
        this.order_sn = order_sn;
        //1.生成订单信息
        String orderInfo = getOrderInfo("商品购买","支付宝支付",price,order_sn);
        //2.对订单做RSA签名. 做URL编码
        String sign = sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //3.整合交付信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        ///////////////////////////////////////////////
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 5.构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 6.调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        //4.异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    public void pay2(String price,String order_sn,String title){
        this.price = price;
        this.order_sn = order_sn;
        //1.生成订单信息
        String orderInfo = getOrderInfo(title,"支付宝支付",price,order_sn);
        //2.对订单做RSA签名. 做URL编码
        String sign = sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //3.
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        ///////////////////////////////////////////////
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 5.构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 6.调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        //4.异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public String getOrderInfo(String subject, String body, String price,String order_sn) {
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        orderInfo += "&out_trade_no=" + "\"" + order_sn + "\"";

        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url
                + "\"";

        // 服务接口名称
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }



    private String getRequestStr(String order_sn) {
        String orderInfo = "app_id=" + "\"" + PARTNER + "\"";
        orderInfo += "&biz_content={\"out_trade_no\":" + "\"" + order_sn + "\"}";
        orderInfo += "charset=\"utf-8\"";
        orderInfo += getSignType();
        String sign = sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestStr = orderInfo + "&sign=\"" + sign + "\"";
        return requestStr;
    }

    /**
     * 签名
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
