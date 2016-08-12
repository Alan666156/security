package com.security.common;

import java.util.UUID;

public class SecurityCommon {
	
	public static String getUUID(){
		return "ZD"+UUID.randomUUID().toString();
	}
}
