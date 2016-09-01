package com.security.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.security.common.PdfConstants;
import com.security.exception.BusinessException;
import com.security.util.AesUtils;


/**
 * 二维码工具类
 * 
 * @author Administrator
 *
 */
public class QuickMarkUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuickMarkUtil.class);
	private final static String IMG_JPG = "jpg";
	private final static String IMG_PNG = "png";

	/**
	 * 生成二维码 到 图片文件
	 * 
	 * @param content
	 * @param path
	 * @param width
	 * @param height
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void encodeToFile(String content, String path, int width, int height) throws Exception {
		String imgType = IMG_JPG;
		if (!(IMG_JPG.equals(imgType) || IMG_PNG.equals(imgType))) {
			throw new Exception("请设置图片类型为jpg或png");
		}
		if (StringUtils.isEmpty(path) || !path.contains(".")) {
			throw new Exception("无效的图片文件路径");
		} else {
			String suffix = StringUtils.upperCase(path.substring(path.indexOf(".") + 1, path.length()));
			if (IMG_PNG.equals(suffix)) {
				imgType = IMG_PNG;
			}
		}
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		MatrixToImageWriter.writeToFile(bitMatrix, imgType, new File(path));
	}

	/**
	 * 生成二维码 到 字节数组
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeToByteArray(String content, int width, int height) throws Exception {
		byte[] byteArray = null;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		ByteArrayOutputStream outSm = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, IMG_JPG, outSm);
		byteArray = outSm.toByteArray();
		outSm.flush();
		outSm.close();
		return byteArray;
	}

	/**
	 * 组装 二维码内容
	 * 
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static String wrapQuickMarkContent(String fileId, String qmContent, String desKey) throws Exception {
		StringBuffer sbuff = new StringBuffer();
		if (StringUtils.isEmpty(qmContent)) {
			throw new Exception("二维码内容url为空");
		}
		qmContent.trim();
		if (qmContent.endsWith("/")) {
			qmContent = qmContent.substring(0, qmContent.lastIndexOf("/"));
		}
		sbuff.append(qmContent).append("?").append("id=").append(AesUtils.valueEncrypt(fileId)).append("&").append("token=").append(AesUtils.valueEncrypt(fileId));
		return sbuff.toString();
	}

	/**
	 * @param qmContent
	 *            待组装的二维码url
	 * @param margin_x
	 *            二维码上边距
	 * @param margin_y
	 *            二维码右边距
	 * @param qmDesKey
	 *            DES秘钥
	 * @param pdfId
	 *            pdfID
	 * @param pdfData
	 *            pdf字节数组
	 * @return
	 */
	public static byte[] insertQuickmarkToPdfArray(String qmContent, String margin_x, String margin_y, String qmDesKey, String pdfId, byte[] pdfData) {
		try {
			if (Strings.isEmpty(qmContent)) {
				throw new BusinessException("二维码填充内容为空：请检查配置文件 quickmark.content");
			}
			if (Strings.isEmpty(margin_x)) {
				throw new BusinessException("二维码上边距为空");
			}
			if (Strings.isEmpty(margin_y)) {
				throw new BusinessException("二维码右边距为空");
			}
			if (Strings.isEmpty(qmDesKey)) {
				throw new BusinessException("二维码内容DES加密秘钥为空");
			}
			return PDFUtil.insertQRCodeToPdf(wrapQuickMarkContent(pdfId, qmContent, qmDesKey), PdfConstants.QUICK_MARK_TIP_TXT, Integer.parseInt(margin_x.trim()), Integer.parseInt(margin_y.trim()), pdfData);
		} catch (Exception e) {
			LOGGER.error("二维码插入PDF异常,放弃插入操作继续处理：", e);
			return null;
		}
	}

}