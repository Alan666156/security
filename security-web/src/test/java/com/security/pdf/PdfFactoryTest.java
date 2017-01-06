package com.security.pdf;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.security.SecurityApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecurityApplication.class)
@WebAppConfiguration
public class PdfFactoryTest {
	
	@Autowired
	private PdfFactory pdfFactory;
	
	@Test
	public void test() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File("e:/test.pdf"));
			AuditInfoVo audit = new AuditInfoVo();
			audit.setCompanyAddress("上海市浦东新区陆家嘴软家园");
			audit.setContactMobile("13876768888");
			audit.setContactEmail("test@qq.com");
			audit.setContactName("哈哈哈");
			fileOutputStream.write(pdfFactory.createCooperation(audit));
			fileOutputStream.close();
			fileOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
