package com.security.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * generate string
 * @author Alan Fu
 */
public class Generate {
	
	private static final int DEF_COUNT = 20;

    

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
	
	/**
	 * generate uuid
	 * @return
	 */
    public static String generateUUID() {
        return "zd" + UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * generate client secret
     * @return
     */
    public static String generateClientSecret() {
        return RandomStringUtils.random(DEF_COUNT);
    }
    
    /**
     * generate batch number
     * @return
     */
    public static String generateBatchNumber(){
    	return DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
    }
    
    /**
     * generate batch number
     * @param prefix
     * @return
     */
    public static String generateBatchNumber(String prefix){
    	return prefix + DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
    }
}
