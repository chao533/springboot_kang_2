package com.kang.common.utils;

import java.util.Map;

import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.RefundModel;
import com.ijpay.wxpay.model.TransferModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.kang.common.constant.WxPayConstants;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>Title: WxPayUtils</p>
 * <p>Description: 微信支付<p>
 * @author ChaoKang
 * @date 2020年12月16日
 */
public class WxPayUtils {
	
	/**
	 * <p>Title: appPay</p>
	 * <p>Description: APP支付</p>
	 * @param @param totalFee 订单总金额 单位为元
	 * @param @param outTradeNo 商户订单号
	 * @param @return
	 */
	public static Object appPay(String outTradeNo, String totalFee,String notifyUrl) {
		Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(WxPayConstants.WXPAY_APPID)
                .mch_id(WxPayConstants.WXPAY_MCHID)
                .nonce_str(WxPayKit.generateStr())
                .body("众点资讯-App微信支付")
                .out_trade_no(outTradeNo)
                .total_fee(NumberUtil.mul(totalFee, "100").intValue() + "")
                .spbill_create_ip(IPUtils.getIpAddr())
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(WxPayConstants.WXPAY_PARTNERKEY, SignType.HMACSHA256);

		Console.log("微信APP支付-请求参数:{}", JSONUtil.toJsonStr(params));
        String xmlResult = WxPayApi.pushOrder(false, params);
        
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        Console.log("微信APP支付-查询结果: {}", JSONUtil.toJsonStr(result));

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return returnMsg;
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return returnMsg;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(WxPayConstants.WXPAY_APPID, WxPayConstants.WXPAY_MCHID, prepayId,
        		WxPayConstants.WXPAY_PARTNERKEY, SignType.HMACSHA256);

        Console.log("微信APP支付-返回apk的参数:{}" ,JSONUtil.toJsonStr(packageParams));
        return JSONUtil.toJsonStr(packageParams);
	}
	
	/**
	 * <p>Title: getOrder</p>
	 * <p>Description: 查询订单</p>
	 * @param @param outTradeNo 商户订单号
	 * @param @return
	 */
	public static String getOrder(String outTradeNo) {
		Map<String, String> params = OrderQueryModel.builder()
                .appid(WxPayConstants.WXPAY_APPID)
                .mch_id(WxPayConstants.WXPAY_MCHID)
                .transaction_id(outTradeNo)
                .out_order_no(outTradeNo)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(WxPayConstants.WXPAY_PARTNERKEY, SignType.MD5);
		Console.log("微信查询订单-请求参数:{}", JSONUtil.toJsonStr(params));
        String query = WxPayApi.orderQuery(params);
        Console.log("微信查询订单-查询结果:{}", JSONUtil.toJsonStr(query));
        return query;
	}
	
	/**
	 * <p>Title: refund</p>
	 * <p>Description: 微信退款</p>
	 * @param @param outTradeNo 微信商户号
	 * @param @param totalFee 订单总金额 单位为元
	 * @param @param refundFee 退款金额 单位为元
	 * @param @return
	 */
	public static Object refund(String outTradeNo,String totalFee,String refundFee,String notifyUrl) {
		 Map<String, String> params = RefundModel.builder()
                 .appid(WxPayConstants.WXPAY_APPID)
                 .mch_id(WxPayConstants.WXPAY_MCHID)
                 .nonce_str(WxPayKit.generateStr())
                 .transaction_id(outTradeNo)
                 .out_trade_no(outTradeNo)
                 .out_refund_no(WxPayKit.generateStr())
                 .total_fee(NumberUtil.mul(totalFee, "100").toString())
                 .refund_fee(NumberUtil.mul(refundFee, "100").toString())
                 .notify_url(notifyUrl)
                 .build()
                 .createSign(WxPayConstants.WXPAY_PARTNERKEY, SignType.MD5);
		 Console.log("微信退款-请求参数:{}", JSONUtil.toJsonStr(params));
         String refundStr = WxPayApi.orderRefund(false, params, WxPayConstants.WXPAY_CERTPATH, WxPayConstants.WXPAY_MCHID);
         Console.log("微信退款-查询结果:{}", JSONUtil.toJsonStr(refundStr));
         return refundStr;
	}
	
	/**
	 * <p>Title: transfer</p>
	 * <p>Description: 微信提现</p>
	 * @param @param openId 用户
	 * @param @param amount 提现金额 单位为元
	 * @param @return
	 */
	public static String transfer(String tradeNo,String openId, String amount) {
        Map<String, String> params = TransferModel.builder()
                .mch_appid(WxPayConstants.WXPAY_APPID)
                .mchid(WxPayConstants.WXPAY_MCHID)
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(tradeNo)
                .openid(openId)
                .check_name("NO_CHECK")
                .amount(NumberUtil.mul(amount, "100").intValue() + "")
                .desc("微信-企业付款")
                .spbill_create_ip(IPUtils.getIpAddr())
                .build()
                .createSign(WxPayConstants.WXPAY_PARTNERKEY, SignType.MD5, false);
        Console.log("微信提现-请求参数:{}", JSONUtil.toJsonStr(params));
        // 提现
        String transfers = WxPayApi.transfers(params, ResourceUtil.getStream(WxPayConstants.WXPAY_CERTPATH), WxPayConstants.WXPAY_MCHID);
        Console.log("微信提现-查询结果:{}", JSONUtil.toJsonStr(transfers));
        return transfers;
    }
}
