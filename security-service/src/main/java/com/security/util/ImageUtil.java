package com.security.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 
	 * @description: 合并图片
	 * @author: wujinsong
	 * @param folderPath
	 *            图片所在文件夹的绝对路径
	 * @param imgType
	 *            合并后的图片类型（jpg、png...）
	 * @param outAbsolutePath
	 *            （输出合并后文件的绝对路径）
	 * @return
	 */
	public static String mergeFolderImgs(String folderPath, String imgType, String outAbsolutePath) {
		File folder = new File(folderPath);
		File[] imgList = folder.listFiles();
		String[] imgPaths = new String[imgList.length];

		for (int i = 0; i < imgList.length; i++) {
			imgPaths[i] = imgList[i].getAbsolutePath();
		}
		merge(imgPaths, imgType, outAbsolutePath);

		File newImg = new File(outAbsolutePath);
		return newImg.getName();
	}

	/**
	 * 
	 * @description: 设置图片大小（单张图片）
	 * @author: wujinsong
	 * @param path
	 *            路径
	 * @param oldimg
	 *            旧图片名称
	 * @param newimg
	 *            新图片名称
	 * @param newWidth
	 *            新图片宽度
	 * @param newHeight
	 *            新图片高度
	 */
	public static void changeImage(String path, String oldimg, String newimg, int newWidth, int newHeight) {
		try {
			File file = new File(path + oldimg);
			Image img = ImageIO.read(file);
			BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null); // 绘制后的图

			ImageIO.write(tag, /* "GIF" */"GIF" /* format desired */, file /* target */);
		} catch (IOException e) {
			LOGGER.error("图片转换异常", e);
		}
	}

	/**
	 * 
	 * @description: Java拼接多张图片
	 * @author: wujinsong
	 * @param pics
	 *            图片源文件 （必须要宽度一样）
	 * @param type
	 *            图片输出类型
	 * @param dst_pic
	 *            图片输出绝对路径
	 * @return
	 */
	public static boolean merge(String[] pics, String type, String dst_pic) {
		// 图片文件个数
		int len = pics.length;
		if (len < 1) {
			LOGGER.debug("pics len < 1");
			return false;
		}
		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(pics[i]);
				images[i] = ImageIO.read(src[i]);
			} catch (Exception e) {
				LOGGER.error("imageUtile异常", e);
				return false;
			}
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];// 从图片中读取RGB
			ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
		}

		int dst_height = 0;
		int dst_width = images[0].getWidth();
		for (int i = 0; i < images.length; i++) {
			dst_width = dst_width > images[i].getWidth() ? dst_width : images[i].getWidth();

			dst_height += images[i].getHeight();
		}
		if (dst_height < 1) {
			LOGGER.debug("dst_height < 1");
			return false;
		}

		// 生成新图片
		try {
			BufferedImage ImageNew = new BufferedImage(dst_width, dst_height, BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			for (int i = 0; i < images.length; i++) {
				ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(), ImageArrays[i], 0, dst_width);
				height_i += images[i].getHeight();
			}

			File outFile = new File(dst_pic);
			// 写图片
			ImageIO.write(ImageNew, type, outFile);
		} catch (Exception e) {
			LOGGER.error("imageUtil异常", e);
			return false;
		}
		return true;
	}

	public static byte[] createThumbImage(byte[] input, float _width, float _height) throws IOException {
		BufferedImage src = ImageIO.read(new ByteArrayInputStream(input));
		int width = src.getWidth();
		int height = src.getHeight();

		float _stw = _width / width;
		float _sth = _height / height;
		float scale = _stw > _sth ? _stw : _sth;

		// 获取一个宽、长是原来scale的图像实例
		Image image = src.getScaledInstance((int) (width * scale), (int) (height * scale), Image.SCALE_DEFAULT);

		// 缩放图像
		BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = tag.createGraphics();

		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(tag, "png", out);// 输出
		return out.toByteArray();
	}
}
