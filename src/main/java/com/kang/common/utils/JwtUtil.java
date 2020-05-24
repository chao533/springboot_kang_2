package com.kang.common.utils;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kang.common.constant.PropConstants;
import com.kang.common.exception.TokenValidationException;
import com.kang.model.mybatis.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <p>Title: JwtUtil</p>  
 * <p>Description: JWT工具类</p>  
 * @author chaokang  
 * @date 2018年12月3日
 */
public class JwtUtil {
	
    public static final long EXPIRATION_TIME = new Long(PropConstants.getProp_2(PropConstants.JWT_PROP,PropConstants.jwtKey.EXPIRATION_TIME));
    public static final String SECRET = PropConstants.getProp_2(PropConstants.JWT_PROP,PropConstants.jwtKey.SECRET);
    public static final String TOKEN_PREFIX = PropConstants.getProp_3(PropConstants.JWT_PROP,PropConstants.jwtKey.TOKEN_PREFIX);
    public static final String HEADER_STRING = PropConstants.getProp_2(PropConstants.JWT_PROP,PropConstants.jwtKey.HEADER_STRING);
    public static final String ROLE = PropConstants.getProp_2(PropConstants.JWT_PROP,PropConstants.jwtKey.ROLE);
	

    /**
     * <p>Title: generateToken</p>  
     * <p>Description: 根据用户信息生成token</p>  
     * @param userRole
     * @return
     */
    public static String generateToken(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUserName());
        userMap.put("role", user.getRoleName());

        String jwt = Jwts.builder()
                .setClaims(userMap)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + jwt;
    }
    
    public static String generateToken(Map<String,Object> userMap) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userMap.get("id"));
        map.put("username", userMap.get("userName"));
        map.put("role", userMap.get("roleName"));

        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + jwt;
    }

    /**
     * <p>Title: validateTokenAndGetClaims</p>  
     * <p>Description: 验证token的合法性</p>  
     * @param request
     * @return
     */
    public static Map<String, Object> validateTokenAndGetClaims() {
        String token = getRequest().getHeader(HEADER_STRING);
        if (StringUtils.isBlank(token)) {
            throw new TokenValidationException("认证失败");
        }
        Map<String, Object> body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
        return body;
    }
    
    /**
     * <p>Title: getUser</p>  
     * <p>Description: 根据token获取当前用户信息</p>  
     * @param request
     * @return
     */
    public static Map<String,Object> getUser(){
    	 String token = getRequest().getHeader(HEADER_STRING);
         if (token == null) {
             throw new TokenValidationException("认证失败");
         }
         Map<String, Object> body = Jwts.parser()
                 .setSigningKey(SECRET)
                 .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                 .getBody();
         body.remove("exp");
         return body;
    }
    
    /**
     *<p>Title: getRequest</p> 
     *<p>Description: 获取Request对象</p> 
     * @return
     */
    public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
	}
    
   
}