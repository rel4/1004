package com.hongbaogou.pay;

import com.heepay.plugin.api.HeepayPlugin;
import com.hongbaogou.activity.BaseNetActivity;

/**
 * Created by L.K.X on 2016/5/25.
 */
public class HfbPay {
    private BaseNetActivity context;
    private String notifyUrl = "http://1yzs.cn/index.php/pay/heepay_url/houtai";
    private static final int INIT_RESULT = 1001;




    public HfbPay(BaseNetActivity context) {
        this.context = context;
    }

    /**
     * 启动汇付宝安全支付服务
     */
    public void pay(String tokenID,String agentId,String billNo,String _payType) {
        final String paramStr = tokenID + "," + agentId + "," + billNo + "," + _payType;
        HeepayPlugin.pay(context, paramStr);
    }

//    /**
//     * 接收支付通知结果
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Constant.RESULTCODE) {
//            String respCode = data.getExtras().getString("respCode");
//            String respMessage = data.getExtras().getString("respMessage");
//            if (!TextUtils.isEmpty(respCode)) {
//                // 支付结果状态（01成功/00处理中/-1 失败）
//                if ("01".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
//                }
//                if ("00".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "处理中...", Toast.LENGTH_SHORT).show();
//                }
//                if ("-1".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//            // 除支付宝sdk支付respMessage均为null
//            if (!TextUtils.isEmpty(respMessage)) {
//                // 同步返回的结果必须放置到服务端进行验证, 建议商户依赖异步通知
//                PayResult result = new PayResult(respMessage);
//                // 同步返回需要验证的信息
//                String resultInfo = result.getResult();
//                String resultStatus = result.getResultStatus();
//                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                if (TextUtils.equals(resultStatus, "9000")) {
//                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    // 判断resultStatus 为非"9000"则代表可能支付失败
//                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                    if (TextUtils.equals(resultStatus, "8000")) {
//                        Toast.makeText(this, "支付结果确认中", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                        Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        }
//    }
}
