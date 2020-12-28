package com.kang.common.constant;

/**
 * <p>Title: WxPayConstants</p>
 * <p>Description: 微信支付配置<p>
 * @author ChaoKang
 * @date 2020年12月15日
 */
public class WxPayConstants {

	public static final String WXPAY_APPID = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_APPID);
	
	public static final String WXPAY_APPSECRET = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_APPSECRET);
	
	public static final String WXPAY_MCHID = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_MCHID);
	
	public static final String WXPAY_PARTNERKEY = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_PARTNERKEY);
	
	public static final String WXPAY_CERTPATH = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_CERTPATH);
	
	public static final String WXPAY_REDPACKET_NOTIFYURL = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_REDPACKET_NOTIFYURL);

	public static final String WXPAY_POINTS_NOTIFYURL = PropConstants.getProp_2(PropConstants.WXPAY_PROP, PropConstants.wxPay.WXPAY_POINTS_NOTIFYURL);
	
}
