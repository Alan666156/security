package com.security.config;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.security.util.HttpUtils;

public class SecurityConfig {
	//Available values:   read, read write
    public static final String READ_SCOPE = "read";
	/**
	 * Oauth支持的grant_type(授权方式) 
	 * 
	 */
    public static enum GrantType {
    	/**密码模式(将用户名,密码传过去,直接获取token)*/
        PASSWORD("password"),
        /**授权码模式(即先登录获取code,再获取token)*/
        AUTHORIZATION_CODE("authorization_code"),
        /**刷新access_token*/
        REFRESH_TOKEN("refresh_token"),
        /**简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)*/
        IMPLICIT("refresh_token"),
        /**客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向'服务端'获取资源)*/
        CLIENT_CREDENTIALS("client_credentials");

        private String type;

        GrantType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return name();
        }
    }
    
    public static String tokenURI(String host) {
        if (host.endsWith("/")) {
            return host + "oauth/token";
        }
        return host + "/oauth/token";
    }
    
    
    
    public static void main(String []args){
		// TODO Auto-generated method stub
    	String url ="http://localhost:8888/oauth2/rest_token";
//    	url = MessageFormat.format(url, "zd04fa1e550ecb4877b0f57a8e87988cc9",SecurityConfig.GrantType.AUTHORIZATION_CODE.getType());
    	Map<String,String> parameters = new HashMap<String, String>();
    	parameters.put("client_id", "zd04fa1e550ecb4877b0f57a8e87988cc9");
    	parameters.put("response_type", "code");
    	parameters.put("grant_type", SecurityConfig.GrantType.AUTHORIZATION_CODE.getType());
    	parameters.put("scope", "read");
    	HttpUtils.doPost(url, JSON.toJSONString(parameters), HttpUtils.CHARSET);
	}
}
