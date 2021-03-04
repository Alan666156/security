package com.security.tags;



import com.security.util.AesUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 自定义标签：加密get请求参数
 * @author: fuhongxing
 */
@Configuration
public class ValueEncrypt extends TagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 944551876433969468L;
	private String var;

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().write(AesUtils.valueEncrypt(var));
		} catch (IOException e) {
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}