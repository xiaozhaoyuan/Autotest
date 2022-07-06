package com.xzy.listener;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.testng.IExecutionListener;

import java.io.File;

public class TestNGListennerSendEmail implements IExecutionListener {
	static Logger logger = Logger.getLogger(TestNGListennerSendEmail.class);

	public void onExecutionStart() {
		logger.info("所有case开始执行***************************************************");
	}

	/**
	 * Invoked once all the suites have been run.
	 */
	public void onExecutionFinish() {
		logger.info("case执行完毕，开始发送邮件*******************************************");
		HtmlEmail mail = new HtmlEmail();
		mail.setHostName("smtp.163.com");
		mail.setAuthentication("18868447608@163.com", "LNSGTRTMPYSGKLOK");
		try {
			mail.setFrom("18868447608@163.com");
			mail.addTo("1561198311@qq.com");
			mail.setSubject("auto-test-api项目");
			mail.setCharset("UTF-8");
			mail.setHtmlMsg("<a href=\"\"> 测试报告");
			EmailAttachment emailattachment = new EmailAttachment();
			emailattachment.setPath(System.getProperty("user.dir") + File.separator + "report" + File.separator + "report.html");
			emailattachment.setName("report.html");
			emailattachment.setDescription(EmailAttachment.ATTACHMENT);
			mail.attach(emailattachment);
			mail.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestNGListennerSendEmail mail = new TestNGListennerSendEmail();
		mail.onExecutionFinish();
	}
}
