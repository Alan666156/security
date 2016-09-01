package com.security.pdf;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.common.PdfConstants;
import com.security.util.DateUtils;

/**
 * 创建各种PDF协议文件
 * 
 * @author Alan Fu
 */
@Service
@Transactional
public class PdfFactory {
	public final static String BRANDCA_FOR_GUARANTEE = "brandcaForGuarantee";
	public final static String BRANDCA_FOR_MEMBER = "brandca3.0";
	/** 保密协议 */
	public final static String CONFIDENTIALITY = "confidentiality";

	/**
	 * 品牌合作协议
	 * 
	 * @return
	 */
	public byte[] createCooperation(AuditInfoVo auditInfo) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("partyaName", PdfConstants.ISGN_COMPANY_NAME);
			map.put("partyaAddress", PdfConstants.ISGN_COMPANY_ADDRESS);
			map.put("partyaRepresentative", PdfConstants.ISGN_COMPANY_REPRESENTATIVE);
			map.put("partybName", auditInfo.getCompanyName());

			/*map.put("partyaLinkman", "");
			map.put("partyaEmail", "");
			map.put("partyaLinkAddress", "");
			map.put("partyaPhone", "");*/

			map.put("partybLinkman", auditInfo.getContactName());//联系人
			map.put("partybEmail", auditInfo.getContactEmail());//联系人邮箱
			map.put("partybAddress", auditInfo.getCompanyAddress());//联系地址
			map.put("partybPhone", auditInfo.getContactMobile());//联系电话

			Date now = new Date();
			map.put("startDate", DateUtils.dateToString(now, DateUtils.FORMAT_DATE_YYYY_MM_DD));
			map.put("endDate", DateUtils.dateToString(DateUtils.addMonth(now, 12), DateUtils.FORMAT_DATE_YYYY_MM_DD));
			/*if (PdfConstants.SYSTEM_GUARANTEE_MEMBER.equals(member.getMemberType())) {
				return PDFBuilder.createPDF(BRANDCA_FOR_GUARANTEE, map);
			} else {
				return PDFBuilder.createPDF(BRANDCA_FOR_MEMBER, map);
			}*/
			return PDFBuilder.createPDF(BRANDCA_FOR_MEMBER, map);
		} catch (Exception e) {
			throw new RuntimeException("创建经济会员品牌合作协议出错", e);
		}
	}

	/**
	 * 创建保密协议
	 * 
	 * @param partybName 担保会员名称
	 * @param member 经纪会员对象
	 * @return pdf byte[]
	 */
	public byte[] createConfidentiality(AuditInfoVo auditInfo) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("partyaName", PdfConstants.ISGN_COMPANY_NAME);
			map.put("partyaAddress", PdfConstants.ISGN_COMPANY_ADDRESS);
			map.put("partybName", auditInfo.getContactName());
			map.put("partybAddress", auditInfo.getCompanyAddress());

			Date now = new Date();
			map.put("startDate", DateUtils.dateToString(now, DateUtils.FORMAT_DATE_YYYY_MM_DD));
			map.put("endDate", DateUtils.dateToString(DateUtils.addMonth(now, 12), DateUtils.FORMAT_DATE_YYYY_MM_DD));
			return PDFBuilder.createPDF(CONFIDENTIALITY, map);
		} catch (Exception e) {
			throw new RuntimeException("创建保密协议出错", e);
		}
	}

	/**
	 * 创建经济会员品牌合作协议
	 * 
	 * @param partybName 经纪会员名称
	 * @param member 经纪会员对象
	 * @return pdf byte[]
	 */
	public byte[] createBrandcaForMember(String partybName, AuditInfoVo auditInfo) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("partyaName", PdfConstants.ISGN_COMPANY_NAME);
			map.put("partyaAddress", PdfConstants.ISGN_COMPANY_ADDRESS);
			map.put("partyaRepresentative", PdfConstants.ISGN_COMPANY_REPRESENTATIVE);
			map.put("partybName", partybName);

			map.put("partyaLinkman", "");
			map.put("partyaEmail", "");
			map.put("partyaLinkAddress", "");
			map.put("partyaPhone", "");

			map.put("partybLinkman", auditInfo.getContactName());//联系人
			map.put("partybEmail", auditInfo.getContactEmail());//联系人邮箱
			map.put("partybAddress", auditInfo.getCompanyAddress());//联系地址
			map.put("partybPhone", auditInfo.getContactMobile());//联系电话

			Date now = new Date();
			map.put("startDate", DateUtils.dateToString(now, DateUtils.FORMAT_DATE_YYYY_MM_DD));
			map.put("endDate", DateUtils.dateToString(DateUtils.addMonth(now, 12), DateUtils.FORMAT_DATE_YYYY_MM_DD));

			return PDFBuilder.createPDF(BRANDCA_FOR_MEMBER, map);
		} catch (Exception e) {
			throw new RuntimeException("创建经济会员品牌合作协议出错", e);
		}
	}

	/**
	 * 创建担保会员品牌合作协议
	 * 
	 * @param partybName 担保会员名称
	 * @param member 经纪会员对象
	 * @return pdf byte[]
	 */
	public byte[] createBrandcaForGuarantee(String partybName, AuditInfoVo auditInfo) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("partyaName", PdfConstants.ISGN_COMPANY_NAME);
			map.put("partyaAddress", PdfConstants.ISGN_COMPANY_ADDRESS);
			map.put("partyaRepresentative", PdfConstants.ISGN_COMPANY_REPRESENTATIVE);
			map.put("partybName", partybName);

			map.put("partyaLinkman", "");
			map.put("partyaEmail", "");
			map.put("partyaLinkAddress", "");
			map.put("partyaPhone", "");

			map.put("partybLinkman", auditInfo.getContactName());//联系人
			map.put("partybEmail", auditInfo.getContactEmail());//联系人邮箱
			map.put("partybAddress", auditInfo.getCompanyAddress());//联系地址
			map.put("partybPhone", auditInfo.getContactMobile());//联系电话
			Date now = new Date();
			map.put("startDate", DateUtils.dateToString(now, DateUtils.FORMAT_DATE_YYYY_MM_DD));
			map.put("endDate", DateUtils.dateToString(DateUtils.addMonth(now, 12), DateUtils.FORMAT_DATE_YYYY_MM_DD));
			return PDFBuilder.createPDF(BRANDCA_FOR_GUARANTEE, map);
		} catch (Exception e) {
			throw new RuntimeException("创建担保会员品牌合作协议出错", e);
		}
	}

}
