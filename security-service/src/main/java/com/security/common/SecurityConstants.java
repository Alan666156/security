package com.security.common;

public class SecurityConstants {
	//编码格式
	public static final String BYTE_TYPE_UTF_8 = "UTF-8";
	public static final String BYTE_TYPE_ISO_8859_1 = "ISO-8859-1";
	
	//加密方式
	public static final String DIGEST_SHA_1 = "SHA-1";
	public static final String DIGEST_MD5 = "MD5";
	//日期格式
	public static final String FORMAT_DATA_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";//带时分秒的日期格式
	public static final String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";//不带时分秒的日期格式
	public static final String FORMAT_DATE_MM_DD = "MM月dd日";
	public static final String FORMAT_DATE_YYMMDD = "yyMMdd";
	public static final String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_DATE_ZHONGWEN = "yyyy年MM月dd日";
	public static final String FORMAT_DATE_YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_YY = "yy";

	public static final String REDIS_KEY_PRE = "sec:limit:{}";
	public static final String REDIS_KEY_MAP_PRE = "sec_map_cache";
	/**
	 * 黑名单
	 */
	public static final String REDIS_KEY_BUCKET_USER_ID = "bucket:black:list:{}";
	/**
	 * 发红包唯一标识
	 */
	public static final String RED_PACKET_SEND ="red:packet:{}:{}";
	/**
	 * 发红包用户
	 */
	public static final String RED_PACKET_USER ="red:packet:user:{}";
	/**
	 * 抢红包集合标识
	 */
	public static final String RED_PACKET_ROB ="red:packet:rob:{}";


}
