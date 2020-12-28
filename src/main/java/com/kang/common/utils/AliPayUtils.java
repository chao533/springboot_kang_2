package com.kang.common.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.kang.common.constant.AliPayConstants;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;

/**
 * <p>Title: AliPayUtils</p>
 * <p>Description: 支付宝支付<p>
 * @author ChaoKang
 * @date 2020年12月17日
 */
public class AliPayUtils {
	
	/**
	 * 配置初始化
	 */
	static {
		 AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
		            .setAppId(AliPayConstants.ALIPAY_APPID)
		            .setAliPayPublicKey(AliPayConstants.ALIPAY_PUBLICKEY)
		            .setAppCertPath(AliPayConstants.ALIPAY_APPCERTPATH)
		            .setAliPayCertPath(AliPayConstants.ALIPAY_ALIPAYCERTPATH)
		            .setAliPayRootCertPath(AliPayConstants.ALIPAY_ALIPAYROOTCERTPATH)
		            .setCharset("UTF-8")
		            .setPrivateKey(AliPayConstants.ALIPAY_PRIVATEKEY)
		            .setServiceUrl(AliPayConstants.ALIPAY_SERVERURL)
		            .setSignType("RSA2")
		            // 普通公钥方式
		            .build();
		            // 证书模式
//			        .buildByCert();
		Console.log("支付宝加载配置信息为：{}",JSONUtil.toJsonStr(aliPayApiConfig));
        AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
	} 
	
	/**
	 * <p>Title: aliPay</p>
	 * <p>Description: 支付宝APP支付</p>
	 * @param @param outTradeNo 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	 * @param @param totalAmount 商户网站唯一订单号
	 * @param @return
	 */
	public static Object appPay(String outTradeNo,String totalAmount,String notifyUrl) {
		String payInfo = null;
		try {
            AlipayTradeAppPayModel alipayTradeAppPayModel = new AlipayTradeAppPayModel();
            alipayTradeAppPayModel.setSubject("众点资讯-支付宝App支付");
            alipayTradeAppPayModel.setOutTradeNo(outTradeNo);
            alipayTradeAppPayModel.setTimeoutExpress("30m");
            alipayTradeAppPayModel.setTotalAmount(totalAmount);
            alipayTradeAppPayModel.setPassbackParams("callback params");
            alipayTradeAppPayModel.setProductCode("QUICK_MSECURITY_PAY");
            Console.log("支付宝APP支付-请求参数:{}",JSONUtil.toJsonStr(alipayTradeAppPayModel));
            payInfo = AliPayApi.appPayToResponse(alipayTradeAppPayModel, notifyUrl).getBody();
            Console.log("支付宝APP支付-查询结果:{}",JSONUtil.toJsonStr(payInfo));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            Console.log("支付宝APP支付错误信息:{}" + e.getMessage());
        }
		return payInfo;
	}
	
	/**
	 * <p>Title: transfer</p>
	 * <p>Description: 支付宝转账</p>
	 * @param @param outBizNo 转账单据ID
	 * @param @param totalAmount 提现金额
	 * @param @param ayeeAccount 收款方账户
	 * @param @param payerRealName 付款方真实姓名
	 * @param @return
	 */
	public static String transfer(String outBizNo,String totalAmount, String ayeeAccount,String payerRealName) {
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount(ayeeAccount);
        model.setAmount(totalAmount);
        model.setPayerRealName(payerRealName);
        model.setRemark("支付宝转账到支付宝账户");
        try {
            return AliPayApi.transferToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * <p>Title: transferQuery</p>
	 * <p>Description: 支付宝转账查询接口</p>
	 * @param @param outBizNo 转账单据ID
	 * @param @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean transferQuery(String outBizNo) {
		AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
	    model.setOutBizNo(outBizNo);
		try {
			return AliPayApi.transferQuery(model);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	    return false;
    }
	
}
