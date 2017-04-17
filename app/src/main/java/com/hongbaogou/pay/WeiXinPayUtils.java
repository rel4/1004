package com.hongbaogou.pay;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Xml;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.hongbaogou.pay.constants.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 * Created by ch on 2015/11/2.
 */
public class WeiXinPayUtils {
    private final String order_sn;
    private Activity mActivity;
    private static final String TAG = "WeiXinPayUtils";

    PayReq req;
    IWXAPI msgApi = null;
    Map<String, String> resultunifiedorder;
    StringBuffer sb;
    private String totalPrice;
    private String notify_url = Constants.NOTIFY_URL;

    public WeiXinPayUtils(Activity mActivity, String totalPrice, String order_sn) {
        SharedPreferencesUtils.putString(mActivity, "price_pay", totalPrice);
        SharedPreferencesUtils.putString(mActivity, "order_sn_pay", order_sn);

        this.order_sn = order_sn;
        this.mActivity = mActivity;
        this.totalPrice = totalPrice;
        msgApi = WXAPIFactory.createWXAPI(mActivity, null);
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Constants.APP_ID);

        //生成prepay_id
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }


    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();
            byte[] buf = Util.httpPost(url, entity);//调微信接口传入商品信息，微信服务器进行验证并且返回XML格式数据的字符串
            String content = new String(buf);
            Map<String, String> xmlMap = decodeXml(content);//将微信服务器返回XML格式数据的字符串解码成Map格式
            return xmlMap;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            resultunifiedorder = result;

            //生成签名参数
            genPayReq();
            //支付
            sendPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private String genProductArgs() {

        try {
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();

            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "360夺宝"));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", genNonceStr()));
            packageParams.add(new BasicNameValuePair("notify_url", notify_url));
            packageParams.add(new BasicNameValuePair("out_trade_no", order_sn));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            double price = Double.parseDouble(totalPrice);
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int) (price * 100))));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);//加入商户秘钥，参与包的签名
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);//将签名信息数据转成XML格式的字符串
//            return xmlstring;
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");//这句加上就可以把xml的中文数据转码下（识别中文）

        } catch (Exception e) {
            return null;
        }

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成签名
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);//商户秘钥

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
        }
        return null;

    }


    private void genPayReq() {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");
    }


    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }


    private void sendPayReq() {
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    private String getRequestStr(String order_sn) {
        String nonceStr = genNonceStr();
        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
        packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
        packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
        packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
        packageParams.add(new BasicNameValuePair("transaction_id", order_sn));
        String sign = genPackageSign(packageParams);
        packageParams.add(new BasicNameValuePair("sign", sign));

        String xmlstring = toXml(packageParams);
        try {
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");//这句加上就可以了吧xml转码下
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
