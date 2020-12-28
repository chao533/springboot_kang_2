package com.kang.common.constant;

import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;

/**
　 * <p>Title: PropConstants</p> 
　 * <p>Description: 读取配置文件Constants</p> 
　 * @author CK 
　 * @date 2020年4月26日
 */
public class PropConstants {
	
	
	
	public static final String WXPAY_PROP = "config/wxpay.properties";

	public interface wxPay {
		
		String WXPAY_APPID = "wxpay.appId"; // 微信appid
		
		String WXPAY_APPSECRET = "wxpay.appSecret"; // 微信小程序appsecret
		
		String WXPAY_MCHID = "wxpay.mchId"; // 微信商户号
		
		String WXPAY_PARTNERKEY = "wxpay.partnerKey"; // API秘钥
		
		String WXPAY_CERTPATH = "wxpay.certPath"; // apiclient_cert.p1 证书路径，在微信商户后台下载
		
		String WXPAY_REDPACKET_NOTIFYURL = "wxpay.addRedPacket.notifyUrl"; // 发红包异步通知
		
		String WXPAY_POINTS_NOTIFYURL = "wxpay.rechargePoints.notifyUrl"; // 充值金币异步通知

	}
	
	
	
	public static final String ALIPAY_PROP = "config/alipay.properties";
	
	public interface aliPay {
		
		String ALIPAY_APPID = "alipay.appId"; // 应用编号
		
		String ALIPAY_PRIVATEKEY = "alipay.privateKey"; // 应用私钥
		
		String ALIPAY_PUBLICKEY = "alipay.publicKey"; // 支付宝公钥，通过应用公钥上传到支付宝开放平台换取支付宝公钥(如果是证书模式，公钥与私钥在CSR目录)
		
		String ALIPAY_APPCERTPATH = "alipay.appCertPath"; // 应用公钥证书
		
		String ALIPAY_ALIPAYCERTPATH = "alipay.aliPayCertPath"; // 支付宝公钥证书
		
		String ALIPAY_ALIPAYROOTCERTPATH = "alipay.aliPayRootCertPath"; // 支付宝根证书
		
		String ALIPAY_SERVERURL = "alipay.serverUrl"; // 支付宝支付网关，沙箱环境时设置为 https://openapi.alipaydev.com/gateway.do 使用正式环境时设置为 https://openapi.alipay.com/gateway.do
		
		String ALIPAY_REDPACKET_NOTIFYURL = "alipay.addRedPacket.notifyUrl"; // 发红包异步通知
		
		String ALIPAY_POINTS_NOTIFYURL = "alipay.rechargePoints.notifyUrl"; // 充值金币异步通知
		
	}
	

	/**
	 * jwt配置信息
	 */
	public static final String JWT_PROP = "config/jwt.properties";

	public interface jwtKey {
		
		String EXPIRATION_TIME = "jwt.expiration.time";

		String SECRET = "jwt.secret";

		String TOKEN_PREFIX = "jwt.token.prefix";

		String HEADER_STRING = "jwt.header.string";

		String ROLE = "jwt.role";
	}
	
	public static final String XXL_JOB = "config/xxl-job.properties";

	public interface jobKey {
		
		String ADDRESSES = "xxl.job.admin.addresses";
		
		String USERNAME = "xxl.job.admin.username";
		
		String PASSWORD = "xxl.job.admin.password";
	}
	
	/**
	 * 私钥
	 */
	public static final String PRIVATE_KEY = "config/privateKey.cert";
	
	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY = "config/publicKey.cert";
	
	
	
	public static String getProp_2(String file, String key) {
		Setting setting = new Setting(file);
		return setting.get(key);
	}
	
	public static String getProp_3(String file, String key) {
		Props props = new Props(file);
		return props.getProperty(key);
	}
}
