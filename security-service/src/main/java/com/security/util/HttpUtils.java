package com.security.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 基于 httpclient http工具类
 * @author fuhongxing
 *
 */
public class HttpUtils {

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doGet(String url,Map<String,String> params,String charset){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	try {
    		if(params != null && !params.isEmpty()){
    			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
    		}
    		HttpGet httpGet = new HttpGet(url);
    		CloseableHttpResponse response = httpClient.execute(httpGet);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpGet.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doPost(String url,Map<String,String> params,String charset){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	try {
    		List<NameValuePair> pairs = null;
    		if(params != null && !params.isEmpty()){
    			pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    		}
    		HttpPost httpPost = new HttpPost(url);
    		if(pairs != null && pairs.size() > 0){
    			httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
    		}
    		CloseableHttpResponse response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    
    
    /**
     * HTTP Post 发送json请求
     * @param url  请求的url地址 ?之前的地址
     * @param json	 请求的json参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doPost(String url,String json,String charset){
    	if(StringUtils.isEmpty(url)){
    		return null;
    	}
    	try {
    		HttpPost httpPost = new HttpPost(url);
    		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");//设置数据格式
    		//给httpPost设置JSON格式的参数  
            StringEntity stringEntity = new StringEntity(json, CHARSET); // 解决中文乱码问题
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding(CHARSET);
            httpPost.setEntity(stringEntity);
            //发送请求
    		CloseableHttpResponse response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		//获取返回结果
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, CHARSET);
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");

	public static MediaType mediaTypeForm = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	
    /**
     * Spring RestTemplate发送post请求,传递json参数
     * @param requestUrl
     * @param json
     * @param type
     * @return
     */
    public static <T> T postForJson(String requestUrl, String json, Class<T> type) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		org.springframework.http.HttpEntity<String> formEntity = new org.springframework.http.HttpEntity<String>(json, headers);
		restTemplate.postForObject(requestUrl, formEntity, type);
		return restTemplate.postForObject(requestUrl, formEntity, type);
	}
    
    /**
	 * 请求第三方接口
	 * 
	 * @param url 请求目标地址
	 * @param mediaType 头文件信息
	 * @param param 请求参数
	 * @param type 响应数据类型
	 * @return
	 */
	public static <T> T postForEntity(String requestUrl, Map<String, Object> param, Class<T> type) {
		if (StringUtils.isEmpty(requestUrl)) {
			return null;
		}
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<T> responseEntity = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaTypeForm);
			org.springframework.http.HttpEntity<Map<String, Object>> request = new org.springframework.http.HttpEntity<Map<String, Object>>(param, headers);
			responseEntity = restTemplate.postForEntity(requestUrl, request, type);
			org.springframework.http.HttpStatus httpStatus = responseEntity.getStatusCode();
			switch (httpStatus) {
				case OK:
					LOGGER.info("[status:200]" + requestUrl + "参数：" + param);
					/** 正常响应 **/
					T value = responseEntity.getBody();
					if (value instanceof String) {
						//return (T) new String(value.toString().getBytes("ISO-8859-1"), "UTF-8");
					//} else {
						return value;
					}
				default:
					LOGGER.error("[status:" + httpStatus + "]" + requestUrl + "参数：" + param);
					throw new RuntimeException("调用第三方接口异常URL：" + requestUrl + " ; 响应码：" + httpStatus);
			}
		} catch (Exception e) {
			LOGGER.error("http请求接口调用异常URL[{}],参数[{}]!!!", requestUrl, param, e);
		}
		return null;
	}
    
    public static void main(String []args){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("customerIdCard", "555422451158");
    	map.put("customerName", "jack");
    	map.put("queryDate", "2016-11-30");
    	map.put("reportId", "100742");
    	map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    	JSONObject json = new JSONObject();
    	Map<String, String> test = new HashMap<String, String>();
    	test.put("param", JSON.toJSONString(map));
//    	String getData = doGet("http://10.8.30.29:8080/",null);
//    	System.out.println(getData);
    	System.out.println("----------------------分割线-----------------------");
    	String postData = doPost("http://10.8.30.29:8080/creditReport/getCreditInfo", test, null);
    	System.out.println(postData);
    }
    
    
    
}