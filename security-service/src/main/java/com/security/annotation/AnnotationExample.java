package com.security.annotation;


import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
/**
 * use methodInfo annotation
 * @author Alan Fu
 */
@Slf4j
public class AnnotationExample {
	private static String annotation ="123";
	public static void main(String[] args) {
		
	}

	@Override
	@MethodInfo(author = "Alan Fu", comments = "Main method", date = "January 17 2016", revision = 1)
	public String toString() {
		return "Overriden toString method";
	}
	
	@Deprecated
	@MethodInfo(comments = "deprecated method", date = "January 17 2016")
	public static void oldMethod() {
		log.info("old method, don't use it.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@MethodInfo(author = "Alan Fu", comments = "genericsTest method", date = "January 17 2016", revision = 10)
	public static void genericsTest() throws FileNotFoundException {
		List l = new ArrayList();
		l.add("abc");
		oldMethod();
	}

}