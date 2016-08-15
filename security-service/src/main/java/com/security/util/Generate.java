package com.security.util;

import java.util.UUID;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
/**
 * generate string
 * @author Alan Fu
 */
public class Generate {
	
	private static RandomValueStringGenerator randomValueStringGenerator = new RandomValueStringGenerator(32);
	
	private Generate(){}
	/**
	 * generate uuid
	 * @return
	 */
    public static String generateUUID() {
        return "zd"+UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * generate client secret
     * @return
     */
    public static String generateClientSecret() {
        return randomValueStringGenerator.generate();
    }
    
}
