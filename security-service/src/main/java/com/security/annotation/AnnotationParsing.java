package com.security.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
 /**
  * methodInfo annotation test
  * @author Alan Fu
  */
public class AnnotationParsing {
	
	
	
	public static void main(String[] args) {
		AnnotationParsing.methodInfoTest();
	}
	
	public static void methodInfoTest(){
		try {
			Method[] methods = AnnotationParsing.class.getClassLoader().loadClass(("com.security.annotation.AnnotationExample")).getMethods();
			
			for (Method method : methods) {
				// checks if MethodInfo annotation is present for the method
				if (method.isAnnotationPresent(MethodInfo.class)) {
					try {
						Annotation[] annotations = method.getAnnotations();
						for (Annotation annotation : annotations) {
				            System.out.println("name=="+annotation.annotationType().getName());
				        }
						System.out.println("==============================================");
						// iterates all the annotations available in the method
						for (Annotation anno : method.getDeclaredAnnotations()) {
							System.out.println("Annotation in Method " + method + " annotation: " + anno);
						}
						MethodInfo methodAnno = method.getAnnotation(MethodInfo.class);
						if (methodAnno.revision() == 1) {
							System.out.println("Method with revision no 1 = " + method);
						}

					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}