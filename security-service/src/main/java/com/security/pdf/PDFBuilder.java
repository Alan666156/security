package com.security.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
/**
 * use freemarker builder agreement template 
 * @author Alan Fu
 */
public class PDFBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(PDFBuilder.class);
	private static Configuration cfg;
	
	static {
		cfg = new Configuration();
		cfg.setTemplateLoader(new ClassTemplateLoader(PDFBuilder.class, "/templates"));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	}
	
	/**
	 * create pdf
	 * @param name
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 * @throws DocumentException
	 */
	public static byte[] createPDF(String name, Object obj) throws IOException, TemplateException, DocumentException {
		String html = getHtmlTemplate(name + ".html", obj);
		LOGGER.debug(html);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont("/fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(data);
		data.close();
		return data.toByteArray();
	}
	
	/**
	 * 获取模板
	 * @param name
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String getHtmlTemplate(String name, Object obj) throws IOException, TemplateException {
		Template temp = cfg.getTemplate(name);
		StringBuilderWriter sbw = new StringBuilderWriter();
		temp.process(obj, sbw);
		sbw.close();
		return sbw.getBuilder().toString();
	}
}
