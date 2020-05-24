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
