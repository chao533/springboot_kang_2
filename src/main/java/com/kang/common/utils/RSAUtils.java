package com.kang.common.utils;

import com.kang.common.constant.PropConstants;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class RSAUtils {
	
	private static final byte[] privateKeyBytes = ResourceUtil.readBytes(PropConstants.PRIVATE_KEY);
	
	private static final byte[] publicKeyBytes = ResourceUtil.readBytes(PropConstants.PUBLIC_KEY);

	/**
	 * <p>Title: encryptHex</p>
	 * <p>Description: 加密</p>
	 * @param @param data
	 * @param @return
	 */
	public static String encryptHex(String data) {
		RSA rsa = new RSA(privateKeyBytes,publicKeyBytes);
		String encryptHex = rsa.encryptHex(data, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
		return encryptHex;
	}
	
	/**
	 * <p>Title: decryptHex</p>
	 * <p>Description: 解密</p>
	 * @param @param data
	 * @param @return
	 */
	public static String decryptHex(String data) {
		RSA rsa = new RSA(privateKeyBytes,publicKeyBytes);
		String decryptStr = rsa.decryptStr(data, KeyType.PrivateKey, CharsetUtil.CHARSET_UTF_8);
		return decryptStr;
	}
	
}
